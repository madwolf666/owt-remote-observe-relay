/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 *
 * @author Chappy
 */
public class Environ {
    public MyLogger _MyLogger = new MyLogger();
    public CheckAny _CheckAny = new CheckAny();

    private String _conf = "/resources/environ.conf";
    private Properties _Prop = new Properties();
    private String _realPath = "";
    
    public Environ(){
        //-------------------------------------------------------------------
        // conf
        //-------------------------------------------------------------------
        if (_CheckAny.isWindows() == true){
            _conf = "\\resources\\environ.conf";
        }else{
            _conf = "/resources/environ.conf";
        }
        
//        try{
            //String a_path = "C:\\Users\\chappy\\Documents\\NetBeansProjects\\owt-remote-observe-relay\\web";
            //String a_path = System.getProperty("user.dir");
            //String a_conf = "C:\\Users\\chappy\\Documents\\NetBeansProjects\\WebApplication1\\build\\web\\resources\\test.conf";
/*            
            FacesContext a_fc = FacesContext.getCurrentInstance();
            ExternalContext a_ec= a_fc.getExternalContext();
            Object a_obj = a_ec.getContext();
            ServletContext servletContext = (ServletContext)a_obj;
            m_Prop.load(new FileInputStream(servletContext.getRealPath(m_conf)));
*/
        //ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

            //File file = new File(servletContext.getRealPath(a_conf));            
            //Properties a_prop = new Properties();
            //m_Prop.load(new FileInputStream(a_path + _conf));
//        } catch (IOException ex) {
//            Logger.getLogger(Environ.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    public void SetRealPath(String h_path){
        _realPath = h_path;
         try{
            _Prop.load(new FileInputStream(_realPath + _conf));
            
            //ログパスを設定
            String a_sVal = _Prop.getProperty("log_path");
            _MyLogger.putLogPath(a_sVal);
            a_sVal = _Prop.getProperty("log_name");
            _MyLogger.putLogName(a_sVal);
            a_sVal = _Prop.getProperty("log_level");
            _MyLogger.putLogLevel(Integer.valueOf(a_sVal));
        } catch (IOException ex) {
            Logger.getLogger(Environ.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String GetEnvironValue(String h_keyName){
        String a_sRet = "";
        a_sRet = _Prop.getProperty(h_keyName);
        System.out.println(h_keyName + ":" + a_sRet);
        return a_sRet;
    }
    
    public java.sql.Timestamp ToSqlTimestampFromString(String h_sDate) throws Exception{
        java.sql.Timestamp a_t2 = null;
        try{
            if (h_sDate.length() > 16){ //[2016.03.03]
                SimpleDateFormat a_sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                a_t2 = new java.sql.Timestamp(a_sdf.parse(h_sDate).getTime());
            }
        } catch (Exception e){
            _MyLogger.severe("[ToSqlTimestampFromString]" + e.getMessage());
        }
        return a_t2;
    }
    
    public String MakePager(int h_kind, int h_iSum, int h_pageNo, int h_max_line_page){
        //[2016.09.21]bug-fixed:testing↓
        /*
        h_kind = 2;
        h_iSum = 1101;
        h_pageNo= 11;
        h_max_line_page = 100;
        */
        //[2016.09.21]bug-fixed:testing↑
       
        String a_sRet = "&nbsp;&nbsp;件数：" + String.valueOf(h_iSum) + "件&nbsp;&nbsp";
        String a_sFunc ="";
        switch (h_kind){
            case 1: //1：SFケース詳細
            a_sFunc = "make_sf_remote_trouble_list";
            break;
        case 2: //2：リモート発報障害検索
            a_sFunc = "make_remote_trouble_list";
            break;
        case 3: //3：リモート月報作成
            a_sFunc = "make_report_user_list";
            break;
        case 4: //4：作業・停電スケジュール
            a_sFunc = "make_schedule_list";
            break;
        case 5: //5：LOG_CHECK
            a_sFunc = "make_log_check_list";
            break;
        case 6: //ユーザ選択
            a_sFunc = "select_user";
            break;
        case 7: //リモートDB設定[2017.07.27]
            a_sFunc = "make_table_list_mnt";
            break;
        case 8: //保全[2017.08.02]
            a_sFunc = "make_log_analyze_list_mnt";
            break;
        }
        
        try{
            if (h_iSum > 0){
                int a_page_sum = h_iSum / h_max_line_page;
                if ((h_iSum % h_max_line_page) > 0){
                    a_page_sum++;
                }
                int a_yoko_num = 10;    //最大10ページ
                int a_yoko_syo = (h_pageNo-1) / a_yoko_num;
                int a_yoko_amari = h_pageNo % a_yoko_num;
                int a_start_page = a_yoko_syo*a_yoko_num;
                a_start_page++;
                
                /*
                a_start_page += a_yoko_amari;
                if (a_yoko_amari == 0){
                    a_start_page++;
                }
                */
                
                if (a_start_page > 1){
                    //前へを表示
                    a_sRet += "&nbsp;&nbsp;<a href='#' onclick='" + a_sFunc + "(" + (a_start_page-1) + ");'>前へ</a>";
                    //a_sRet += "&nbsp;&nbsp;<a href='#' onclick='make_schedule_list(" + (a_start_page-1) + ");'>前へ</a>";
                }

                for (int a_iCnt=1; a_iCnt<=a_yoko_num; a_iCnt++){
                    if (a_start_page != h_pageNo){
                        a_sRet += "&nbsp;&nbsp;<a href='#' onclick='" + a_sFunc + "(" + a_start_page + ");'>" + String.valueOf(a_start_page) + "</a>";
                        //a_sRet += "&nbsp;&nbsp;<a href='#' onclick='make_schedule_list(" + a_start_page + ");'>" + String.valueOf(a_start_page) + "</a>";
                    }else{
                        a_sRet += "&nbsp;&nbsp;" + String.valueOf(a_start_page);
                    }
                    a_start_page++;
                    if (a_start_page > a_page_sum){
                        break;
                    }
                }

                if (a_start_page <= a_page_sum){    //[2016.09.21]bug-fixed.
                    //前へを表示
                    a_sRet += "&nbsp;&nbsp;<a href='#' onclick='" + a_sFunc + "(" + (a_start_page) + ");'>次へ</a>";
                    //a_sRet += "&nbsp;&nbsp;<a href='#' onclick='make_schedule_list(" + (a_start_page+1) + ");'>次へ</a>";
                }

            }else if (h_iSum < 0){  //[2017.07.27]
                a_sRet = "";
            }else{
                a_sRet = "<font color='#ff0000'>現在、登録データはありません。</font>";
            }
            /*
            if (a_sRet != ""){
                if (h_kind == 7){
                    a_sRet = 
                        "<table border=\"0\">"
                        + "<tr>"
                        + "<td rowspan=\"1\" valign=\"top\">"
                        + "<div id=\"new-mnt\"><input id=\"change_submit\" type=\"button\" value=\"　新規登録　\" onclick=\"make_table_edit_mnt('n'," + h_pageNo + ");\" /></div>"
                        + "</td>"
                        + "<td rowspan=\"1\" valign=\"top\">"
                        + "<div id=\"reset-mnt\"><input id=\"change_submit\" type=\"button\" value=\"　リセット　\" onclick=\"reset_table_edit_mnt();\" /></div>"
                        + "</td>"
                        + "<td rowspan=\"1\" valign=\"top\">"
                        + "<div id=\"entry-mnt\"><input id=\"change_submit\" type=\"button\" value=\"　登録終了　\" onclick=\"entry_table_mnt();\" /></div>"
                        + "</td>"
                        + "<td rowspan=\"1\" valign=\"top\">"
                        + "<div id=\"back-mnt\"><input id=\"change_submit\" type=\"button\" value=\"　戻る　\" onclick=\"make_table_list_mnt(" + h_pageNo + ");\" /></div>"
                        + "</td>"
                        + "</tr>"
                        + "</table>"
                        + "<p>"
                        + a_sRet;
                }
            }
            */
        } catch (Exception e){
            _MyLogger.severe("[MakePager]" + e.getMessage());
        }

        return a_sRet;
    }
    
    public String ExistDBString(ResultSet h_rs, String h_name) throws Exception{
        String a_sRet = "";
        
        if (h_rs.getString(h_name) != null){
            a_sRet = h_rs.getString(h_name);
        }
        
        return a_sRet;
    }

    public int ExistDBInt(ResultSet h_rs, String h_name) throws Exception{
        int a_iRet = 0;
        
        if (h_rs.getString(h_name) != null){
            a_iRet = h_rs.getInt(h_name);
        }
        
        return a_iRet;
    }

    public String ToTroubleLevelString(int h_level){
        String a_sRet = "";
        switch (h_level){
            case 0:
                a_sRet = "なし";
                break;
            case 1:
                a_sRet = "MN";
                break;
            case 2:
                a_sRet = "MJ";
                break;
            case 3:
                a_sRet = "定期試験";
                break;
            case 4:
                a_sRet = "同期処理試験";
                break;
            case 5:
                a_sRet = "リモート";
                break;
            case 6:
                a_sRet = "警告";
                break;
            case 7:
                a_sRet = "一般";
                break;
            case 8:
                a_sRet = "サポートコール";
                break;
            case 9:
                a_sRet = "予防保全";
                break;
            case 10:
                a_sRet = "作業確認通知";
                break;
        }
        return a_sRet;
    }
    
    public String ToContactedString(int h_kind){
        String a_sRet = "";
        switch (h_kind){
            case 0:
                a_sRet = "未連絡";
                break;
            case 1:
                a_sRet = "連絡済";
                break;
        }
        return a_sRet;
    }

    public String ToRasCodeKigo(String h_kind){
        String a_sRet = "";
        if (h_kind.equals("S001") == true){
            a_sRet = "○";
        }else if (h_kind.equals("S002") == true){
            a_sRet = "×";
        }else{
            a_sRet = "－";
        }
        return a_sRet;
    }

    public String ToJobKindString(int h_kind){
        String a_sRet = "";
        switch (h_kind){
            case 1:
                a_sRet = "事前確認";
                break;
            case 2:
                a_sRet = "入退管理";
                break;
            case 3:
                a_sRet = "停電";
                break;
            case 4:
                a_sRet = "作業";
                break;
            case 5:
                a_sRet = "定刻";
                break;
        }
        return a_sRet;
    }

    //[2016.05.16]bug-fixed.
    public String ToJobKind2String(int h_kind){
        String a_sRet = "";
        switch (h_kind){
            case 1:
                a_sRet = "事前確認作業警告通知";
                break;
            case 2:
                a_sRet = "入退管理警告通知";
                break;
            case 3:
                a_sRet = "計画停電作業警告通知";
                break;
            case 4:
                a_sRet = "作業警告通知";
                break;
            case 5:
                a_sRet = "定刻警告通知";
                break;
        }
        return a_sRet;
    }

    //[2016.06.08]
    public String ToNodeTypeString(int h_type){
        String a_sRet = "";
        switch (h_type){
            case 2:
                a_sRet = "IPStageSX";
                break;
            case 3:
                a_sRet = "IPStageMX";
                break;
            case 4:
                a_sRet = "IPStageEX300";
                break;
        }
        return a_sRet;
    }
}
