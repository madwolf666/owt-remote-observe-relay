/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maintenance;

import common.Environ;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Chappy
 */
@ManagedBean(name="AnalyzeLog")
@RequestScoped
public class AnalyzeLog implements Serializable {
    //private CheckAny _CheckAny = null;
    public Environ _Environ = null;
    
    public String _db_driver = "";
    public String _db_server = "";
    public String _db_url = "";
    public String _db_user = "";
    public String _db_pass = "";
    public String _db_name = "";
    private int _max_line_page = 0;

    //--------------------------------------------------------------------------

    /**
     * Creates a new instance of SalesForceApi
     */
    public AnalyzeLog() {
        //_CheckAny = new CheckAny();
        _Environ = new Environ();

    }

    //--------------------------------------------------------------------------
    //システム絶対パスを設定する
    //--------------------------------------------------------------------------
    public void SetRealPath(String h_path){
        _Environ.SetRealPath(h_path);
        //_Environ._MyLogger.SetMyLog();
        _db_driver = _Environ.GetEnvironValue("mnt_db_driver");
        _db_server = _Environ.GetEnvironValue("mnt_db_server");
        _db_url = _Environ.GetEnvironValue("mnt_db_url");
        _db_user = _Environ.GetEnvironValue("mnt_db_user");
        _db_pass = _Environ.GetEnvironValue("mnt_db_pass");
        _db_name = _Environ.GetEnvironValue("mnt_db_name");
        _max_line_page = Integer.valueOf(_Environ.GetEnvironValue("max_line_page"));
        
        _Environ._MyLogger.config("mnt_db_driver ---> " + _db_driver);
        _Environ._MyLogger.config("mnt_db_server ---> " + _db_server);
        _Environ._MyLogger.config("mnt_db_url ---> " + _db_url);
        _Environ._MyLogger.config("mnt_db_user ---> " + _db_user);
        _Environ._MyLogger.config("mnt_db_pass ---> " + _db_pass);
        _Environ._MyLogger.config("mnt_db_name ---> " + _db_name);
        _Environ._MyLogger.config("max_line_page ---> " + _max_line_page);
        
        _Environ._MyLogger.info("*** SetRealPath is finished. ***");
    }

    //--------------------------------------------------------------------------
    //保全データを解析する
    // h_kind   種別
    // h_rs     スケジュール検索レコード
    //--------------------------------------------------------------------------
    public void analyze_Log(
        int h_kind,
        ResultSet h_rs
        ) throws Exception{
        int a_idx = 0;
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";
        boolean a_isFound = false;
        try{
            a_idx = 0;
            //------------------------------------------------------------------
            //スケジュール検索レコード数分、処理を繰り返す
            //------------------------------------------------------------------
            while(h_rs.next()){
                a_idx++;
                
                //--------------------------------------------------------------
                //スケジュール検索レコードデータを取得
                //--------------------------------------------------------------
                String a_logname = _Environ.ExistDBString(h_rs, "logname");
                String a_start_time = _Environ.ExistDBString(h_rs, "START_EXECTIME");
                String a_end_time = _Environ.ExistDBString(h_rs, "END_EXECTIME");
                String a_findkeyword = _Environ.ExistDBString(h_rs, "findkeyword");
                String a_mailaddr = _Environ.ExistDBString(h_rs, "sendmailaddress");
                        
                //--------------------------------------------------------------
                //ログ検索SQLの組み立て
                //--------------------------------------------------------------
                a_sql = "SELECT * FROM " + a_logname + " WHERE ";
                if ((a_logname.equals("mss2operationlog") == true) || (a_logname.equals("operationlog") == true)){
                    //mss2operationlog、operationlogの場合
                    //RASCODE：英数4文字完全一致で検索
                    if (_db_driver.equals("oracle.jdbc.driver.OracleDriver")){
                        //Oracleの場合
                        a_sql += " (datetime>=?) AND (datetime<?)";
                    }else if (_db_driver.equals("org.postgresql.Driver")){
                        //PostgerSQLの場合
                        a_sql += " (datetime>=?) AND (datetime<?)";
                    }
                    a_sql += " AND (rascode=?) ORDER BY datetime";
                }else if (a_logname.equals("pbxoperationlog") == true){
                    //pbxoperationlogの場合
                    //DETAIL：部分一致で検索
                    if (_db_driver.equals("oracle.jdbc.driver.OracleDriver")){
                        //Oracleの場合
                        a_sql += " (logdate>=?) AND (logdate<?)";
                    }else if (_db_driver.equals("org.postgresql.Driver")){
                        //PostgreSQLの場合
                        a_sql += " (logdate>=?) AND (logdate<?)";
                    }
                    a_sql += " AND (detail like ?) ORDER BY logdate";
                }
            
                Class.forName (_db_driver);
                // データベースとの接続
                a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
                a_ps = a_con.prepareStatement(a_sql);
                
                //SQLパラメータの設定
                java.sql.Timestamp a_ts = null;
                SimpleDateFormat a_sdf = null;
                a_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                a_ts = new java.sql.Timestamp(a_sdf.parse(a_start_time).getTime());
                a_ps.setTimestamp(1, a_ts);
                a_ts = new java.sql.Timestamp(a_sdf.parse(a_end_time).getTime());
                a_ps.setTimestamp(2, a_ts);
                if ((a_logname.equals("mss2operationlog") == true) || (a_logname.equals("operationlog") == true)){
                    //mss2operationlog、operationlogの場合
                    a_ps.setString(3, a_findkeyword);
                }else if (a_logname.equals("pbxoperationlog") == true){
                    //pbxoperationlogの場合
                    a_ps.setString(3, "%" + a_findkeyword + "%");
                }

                a_rs = a_ps.executeQuery();
                while(a_rs.next()){
                    a_isFound = true;
                }
                a_rs.close();
                a_ps.close();
                
                //--------------------------------------------------------------
                //メールを送信する。
                //--------------------------------------------------------------
                if (a_isFound == true){
                    if (a_mailaddr.equals("") == false){
                        _sendmail(a_mailaddr);
                    }
                }
            }
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[analyze_Log]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[analyze_Log]" + ex.getMessage());
        } catch (Exception e){
            _Environ._MyLogger.severe("[analyze_Log]" + e.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        _Environ._MyLogger.info("*** analyze_Log is finished. ***");
    }
    
    //--------------------------------------------------------------------------
    //メールを送信する
    // h_mailaddr   送信先メールアドレス
    //--------------------------------------------------------------------------
    private void _sendmail(String h_mailaddr){
        String[] a_mailToAddr = null;
        String[] a_sVal = null;

        //----------------------------------------------------------------------
        // メール送信元情報の取得
        //----------------------------------------------------------------------
        String a_mailSubject = _Environ.GetEnvironValue("mnt_sendmail_subject");
        String a_fromName = _Environ.GetEnvironValue("mnt_sendmail_from_name");
        String a_fromAddr = _Environ.GetEnvironValue("mnt_sendmail_from_addr");
        String a_fromPass = _Environ.GetEnvironValue("mnt_sendmail_from_pass");

        try{
            Properties a_props = new Properties();

            //------------------------------------------------------------------
            // SMTP情報の取得
            //------------------------------------------------------------------
            a_props.put("mail.smtp.host", _Environ.GetEnvironValue("mnt_sendmail_smtp_host")); 	//●SMTPサーバ名
            a_props.put("mail.host", _Environ.GetEnvironValue("mnt_sendmail_mail_host"));      	//●接続するホスト名
            a_props.put("mail.smtp.port", _Environ.GetEnvironValue("mnt_sendmail_smtp_port"));       		//●SMTPサーバポート
            a_props.put("mail.smtp.auth", _Environ.GetEnvironValue("mnt_sendmail_smtp_auth"));    		//●smtp auth
            a_props.put("mail.smtp.starttls.enable", _Environ.GetEnvironValue("mnt_sednmail_smtp_starttls_enable"));	//●STTLS

            // セッション
            Session a_session = Session.getDefaultInstance(a_props);
            a_session.setDebug(true);

            MimeMessage a_msg = new MimeMessage(a_session);

            //------------------------------------------------------------------
            // 送信先メールアドレスの取得
            //------------------------------------------------------------------
            a_mailToAddr = h_mailaddr.split(";");	
            InternetAddress[] a_toAddress = new InternetAddress[a_mailToAddr.length];	//●To
            for (int a_i=0; a_i<a_mailToAddr.length; a_i++){
                    a_toAddress[a_i] = new InternetAddress(a_mailToAddr[a_i].trim());
            }

            //------------------------------------------------------------------
            // 件名の設定
            //------------------------------------------------------------------
            a_msg.setSubject(a_mailSubject, "shift-jis");

            //------------------------------------------------------------------
            // 送信元メールアドレスの設定
            //------------------------------------------------------------------
            byte[] a_byteData = _Environ.GetEnvironValue("mnt_sendmail_from_name").getBytes("Shift_JIS");
            String a_sData = new String(a_byteData, "Shift_JIS");
            a_msg.setFrom(new InternetAddress(a_fromAddr, a_fromName, "ISO-2022-JP"));	//●From
            //a_msg.setFrom(new InternetAddress(a_fromAddr));	//●From

            //------------------------------------------------------------------
            // 送信先メールアドレスの設定
            //------------------------------------------------------------------
            a_msg.setRecipients(Message.RecipientType.TO, a_toAddress);	//●To

            Multipart a_mixedPart = new MimeMultipart("mixed");

            //------------------------------------------------------------------
            // 本文の設定
            //------------------------------------------------------------------
            MimeBodyPart a_textBodyPart = new MimeBodyPart();
            //[2015.02.19]---↓
            a_textBodyPart.setText("This is test.", "shift-jis", "plain");  
            a_textBodyPart.setHeader("Content-Transfer-Encoding", "base64");  
            a_mixedPart.addBodyPart(a_textBodyPart);  

            // attach image 添付ファイルの設定 
            /*
            MimeBodyPart imageBodyPart = new MimeBodyPart();  
            DataSource dataSource = new FileDataSource(h_fname);  
            DataHandler dataHandler=new DataHandler(dataSource);  
            imageBodyPart.setDataHandler(dataHandler);  
            try {
                    //[2014.11.05]---↓
                    a_sVal = h_fname.split("/");
                imageBodyPart.setFileName(MimeUtility.encodeWord(a_sVal[a_sVal.length - 1]));
//	            imageBodyPart.setFileName(MimeUtility.encodeWord(a_subject+".jpg"));
                //[2014.11.05]---↑
            } catch (UnsupportedEncodingException e) {  
                // TODO 自動生成された catch ブロック  
                e.printStackTrace();  
            }  
            imageBodyPart.setDisposition("attachment ");   
            a_mixedPart.addBodyPart(imageBodyPart);  
            */

            // set mixed  
            a_msg.setContent(a_mixedPart);  

            //------------------------------------------------------------------
            //メールアカウントの設定＆メール送信
            //------------------------------------------------------------------
            Transport a_t = a_session.getTransport("smtp");
            a_t.connect(a_fromAddr,a_fromPass); //●Gmailアカウント設定
            a_t.sendMessage(a_msg, a_msg.getAllRecipients());  
        }catch(MessagingException e){
            _Environ._MyLogger.severe("[_sendmail]" + e.getMessage());
        }catch(Exception e){
            _Environ._MyLogger.severe("[_sendmail]" + e.getMessage());
        }
    }
}
