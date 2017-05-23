/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote;

import common.CheckAny;
import common.Environ;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Chappy
 */
@ManagedBean(name="RemoteTrouble")
@RequestScoped
public class RemoteTrouble implements Serializable {
    //private CheckAny _CheckAny = null;
    public Environ _Environ = null;
    
    public String _db_driver = "";
    public String _db_server = "";
    public String _db_url = "";
    public String _db_user = "";
    public String _db_pass = "";
    public String _db_name = "";
    private int _max_line_page = 0;
    private int _max_line_page_user = 0;

    private Timestamp _TimeS = null;
    private Timestamp _TimeE = null;

    private String UserCode = "";
    private String SetNo = "";
    private String RecNo = "";
    private String UserName = "";
    private String TimeS = "";
    private String TimeE = "";
    private String TroubleMsg = "";
    private String TroubleKind_MJ = "";
    private String TroubleKind_MN = "";
    private String TroubleKind_GN = "";
    private String Contact = "";
    private String OrderInitiationTime = "";
    
    //--------------------------------------------------------------------------
    //ユーザコード
    public void putUserCode(String h_val) {
        UserCode = h_val;
    }
    public String getUserCode() {
        return UserCode;
    }
    //SET-NO
    public void putSetNo(String h_val) {
        SetNo = h_val;
    }
    public String getSetNo() {
        return SetNo;
    }
    //REC番号
    public void putRecNo(String h_val) {
        RecNo = h_val;
    }
    public String getRecNo() {
        return RecNo;
    }
    //ユーザ名
    public void putUserName(String h_val) {
        UserName = h_val;
    }
    public String getUserName() {
        return UserName;
    }
    //開始時間
    public void putTimeS(String h_val) {
        TimeS = h_val;
        try{
            _TimeS = _Environ.ToSqlTimestampFromString(TimeS + ":00");
        } catch (Exception e) {
            _Environ._MyLogger.severe("[putTimeS]" + e.getMessage());
        }
    }
    public String getTimeS() {
        return TimeS;
    }
    //終了時間
    public void putTimeE(String h_val) {
        TimeE = h_val;
        try{
            _TimeE = _Environ.ToSqlTimestampFromString(TimeE + ":00");
        } catch (Exception e) {
            _Environ._MyLogger.severe("[putTimeE]" + e.getMessage());
        }
    }
    public String getTimeE() {
        return TimeE;
    }
    //障害メッセージ
    public void putTroubleMsg(String h_val) {
        TroubleMsg = h_val;
    }
    public String getTroubleMsg() {
        return TroubleMsg;
    }
    //障害種別(MJ)
    public void putTroubleKind_MJ(String h_val) {
        TroubleKind_MJ = h_val;
    }
    public String getTroubleKind_MJ() {
        return TroubleKind_MJ;
    }
    //障害種別(MN)
    public void putTroubleKind_MN(String h_val) {
        TroubleKind_MN = h_val;
    }
    public String getTroubleKind_MN() {
        return TroubleKind_MN;
    }
    //障害種別(一般)
    public void putTroubleKind_GN(String h_val) {
        TroubleKind_GN = h_val;
    }
    public String getTroubleKind_GN() {
        return TroubleKind_GN;
    }
    //連絡済
    public void putContact(String h_val) {
        Contact = h_val;
    }
    public String getContact() {
        return Contact;
    }
    //発報時間（並び順）
    public void putOrderInitiationTime(String h_val) {
        OrderInitiationTime = h_val;
    }
    public String getOrderInitiationTime() {
        return OrderInitiationTime;
    }
    
    /**
     * Creates a new instance of SalesForceApi
     */
    public RemoteTrouble() {
        //_CheckAny = new CheckAny();
        _Environ = new Environ();

    }

    public void SetRealPath(String h_path){
        _Environ.SetRealPath(h_path);
        //_Environ._MyLogger.SetMyLog();
        _db_driver = _Environ.GetEnvironValue("db_driver");
        _db_server = _Environ.GetEnvironValue("db_server");
        _db_url = _Environ.GetEnvironValue("db_url");
        _db_user = _Environ.GetEnvironValue("db_user");
        _db_pass = _Environ.GetEnvironValue("db_pass");
        _db_name = _Environ.GetEnvironValue("db_name");
        _max_line_page = Integer.valueOf(_Environ.GetEnvironValue("max_line_page"));
        _max_line_page_user = Integer.valueOf(_Environ.GetEnvironValue("max_line_page_user"));
        
        _Environ._MyLogger.config("db_driver ---> " + _db_driver);
        _Environ._MyLogger.config("db_server ---> " + _db_server);
        _Environ._MyLogger.config("db_url ---> " + _db_url);
        _Environ._MyLogger.config("db_user ---> " + _db_user);
        _Environ._MyLogger.config("db_pass ---> " + _db_pass);
        _Environ._MyLogger.config("db_name ---> " + _db_name);
        _Environ._MyLogger.config("max_line_page ---> " + _max_line_page);
        _Environ._MyLogger.info("*** SetRealPath is finished. ***");
    }
    
    //pagerの作成
    public String MakePagerSF(int h_kind, int h_pageNo) throws Exception{
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "SELECT COUNT(JOBID) AS REC_SUM FROM JOBSCHEDULE;";
        int a_iSum = 0;
        String a_sRet = "";
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                a_iSum = a_rs.getInt("REC_SUM");
            }
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[MakePagerSF]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[MakePagerSF]" + ex.getMessage());
        } finally{
            if (a_rs != null){
                a_rs.close();
            }
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        
        a_sRet = _Environ.MakePager(h_kind, a_iSum, h_pageNo, _max_line_page);
        _Environ._MyLogger.info("*** MakePagerSF is finished. ***");
        
        return a_sRet;
    }
    
    //pagerの作成
    public String MakePagerRemoteTrouble(int h_kind, int h_pageNo) throws Exception{
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "SELECT COUNT(CUSTOMERID) AS REC_SUM FROM TROUBLEINFORMATION";
        String a_sql_cond = _makeSQLCond();
        if (a_sql_cond.length() > 0){
            a_sql += " WHERE " + a_sql_cond;
        }
        a_sql += ";";
        int a_iSum = 0;
        String a_sRet = "";
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            //a_con.setSchema("remote");
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                a_iSum = a_rs.getInt("REC_SUM");
            }
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[MakePagerRemoteTrouble]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[MakePagerRemoteTrouble]" + ex.getMessage());
        } finally{
            if (a_rs != null){
                a_rs.close();
            }
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        
        a_sRet = _Environ.MakePager(h_kind, a_iSum, h_pageNo, _max_line_page);
        _Environ._MyLogger.info("*** MakePagerRemoteTrouble is finished. ***");
        
        return a_sRet;
    }
    
        public String MakePagerUser(int h_kind, int h_pageNo) throws Exception{
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "SELECT COUNT(ID) AS REC_SUM FROM NEWCUSTOMERMANAGE";
        String a_sql_cond = _makeSQLCond_User();
        if (a_sql_cond.length() > 0){
            a_sql += " WHERE " + a_sql_cond;
        }
        a_sql += ";";
        int a_iSum = 0;
        String a_sRet = "";
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                a_iSum = a_rs.getInt("REC_SUM");
            }
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[MakePagerUser]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[MakePagerUser]" + ex.getMessage());
        } finally{
            if (a_rs != null){
                a_rs.close();
            }
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        
        a_sRet = _Environ.MakePager(h_kind, a_iSum, h_pageNo, _max_line_page_user);
        _Environ._MyLogger.info("*** MakePagerUser is finished. ***");
        
        return a_sRet;
    }
        
    public  ArrayList<String> FindRemoteTrouble(int h_pageNo) throws Exception{
        ArrayList<String> a_arrayRet = null;
        int a_iSum = 0;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "SELECT COUNT(CUSTOMERID) AS REC_SUM FROM TROUBLEINFORMATION";
        String a_sql_cond = _makeSQLCond();
        if (a_sql_cond.length() > 0){
            a_sql += " WHERE " + a_sql_cond;
        }
        a_sql += ";";
        //int a_iRet = 0;
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                a_iSum = a_rs.getInt("REC_SUM");
            }
            a_rs.close();
            a_ps.close();
            
            if (a_iSum > 0){
                a_arrayRet = new ArrayList<String>();
                String a_sVal = "";
                int a_iVal = 0;
                String a_sRet = "";
                int a_start_idx = ((h_pageNo-1)*_max_line_page) + 1;
                int a_end_idx = (h_pageNo*_max_line_page);
                //[2016.02.25]処理速度向上↓
                a_sql = "SELECT row_number() over(ORDER BY INITIATIONTIME " + OrderInitiationTime + "), *"
                        + " FROM TROUBLEINFORMATION";
                if (a_sql_cond.length() > 0){
                    a_sql += " WHERE " + a_sql_cond;
                }
                a_sql += " ORDER BY INITIATIONTIME " + OrderInitiationTime;
                a_sql = "SELECT * FROM (" + a_sql + ") s1 WHERE (s1.row_number BETWEEN " + String.valueOf(a_start_idx) + " AND " + String.valueOf(a_end_idx) + ")";
                //USERCODEでのリレーションは現状ない
                //USERNAMEからUSERCODEが複数返される場合あり
                a_sql = "SELECT t1.*"
                    + ", (SELECT USERCODE FROM NEWCUSTOMERMANAGE WHERE (USERNAME=t1.USERNAME) ORDER BY USERCODE LIMIT 1 OFFSET 1) AS USERCODE"
                    + ", (SELECT DETAIL FROM TROUBLEINFORMATIONEXTENSION WHERE (TROUBLEINFORMATIONID=t1.ID)) AS DETAIL"
                    + " FROM (" + a_sql + ") t1 ORDER BY t1.row_number;";
                //[2016.02.25]処理速度向上↑
                a_ps = a_con.prepareStatement(a_sql);
                a_rs = a_ps.executeQuery();
                int a_iIdx = -1;
                while(a_rs.next()){
                    a_sRet = "";
                    //No.
                    a_iIdx++;
                    a_sVal = String.valueOf(a_start_idx + a_iIdx);
                    a_sRet += a_sVal;
                    //発報時間
                    a_sVal = _Environ.ExistDBString(a_rs,"INITIATIONTIME");
                    if (a_sVal.length()>0){
                        a_sVal = a_sVal.substring(0,16).replace("-", "/");
                    }
                    a_sRet += "\t" + a_sVal;
                    //ユーザコード
                    a_sVal = _Environ.ExistDBString(a_rs,"USERCODE");
                    a_sRet += "\t" + a_sVal;
                    //SET-NO
                    a_sVal = _Environ.ExistDBString(a_rs,"REMOTESETNAME");
                    a_sRet += "\t" + a_sVal;
                    //REC番号
                    a_sVal = _Environ.ExistDBString(a_rs,"USERNUMBER");
                    a_sRet += "\t" + a_sVal;
                    //ユーザ名
                    a_sVal = _Environ.ExistDBString(a_rs,"USERNAME");
                    a_sRet += "\t" + a_sVal;
                    //機種名
                    a_sVal = _Environ.ExistDBString(a_rs,"MACHINENAME");
                    a_sRet += "\t" + a_sVal;
                    //ユーザ機種名
                    a_sVal = _Environ.ExistDBString(a_rs,"IDCODE");
                    a_sRet += "\t" + a_sVal;
                    //障害種別
                    a_iVal = _Environ.ExistDBInt(a_rs,"TROUBLELEVEL");
                    a_sRet += "\t" + _Environ.ToTroubleLevelString(a_iVal);
                    //1st MSG
                    a_sVal = _Environ.ExistDBString(a_rs,"MESSAGE1");
                    if (a_sVal.indexOf("メッセージ長が規定長を超えたため拡張メッセージを確認してください") >= 0){
                        a_sVal = _Environ.ExistDBString(a_rs,"DETAIL");
                    }
                    a_sRet += "\t" + a_sVal;
                    //2nd MSG
                    a_sVal = _Environ.ExistDBString(a_rs,"MESSAGE2");
                    a_sRet += "\t" + a_sVal;
                    //3rd MSG
                    a_sVal = _Environ.ExistDBString(a_rs,"MESSAGE3");
                    a_sRet += "\t" + a_sVal;
                    //連絡済
                    a_iVal = _Environ.ExistDBInt(a_rs,"CONTACTED");
                    a_sRet += "\t" + _Environ.ToContactedString(a_iVal);
                    
                    a_arrayRet.add(a_sRet);
                }
                a_rs.close();
                a_ps.close();
            }
            
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[FindRemoteTrouble]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[FindRemoteTrouble]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        _Environ._MyLogger.info("*** FindRemoteTrouble is finished. ***");
        
        return a_arrayRet;
    }

        public  ArrayList<String> FindUser(int h_pageNo) throws Exception{
        ArrayList<String> a_arrayRet = null;
        int a_iSum = 0;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "SELECT COUNT(ID) AS REC_SUM FROM NEWCUSTOMERMANAGE";
        String a_sql_cond = _makeSQLCond_User();
        if (a_sql_cond.length() > 0){
            a_sql += " WHERE " + a_sql_cond;
        }
        a_sql += ";";
        //int a_iRet = 0;
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                a_iSum = a_rs.getInt("REC_SUM");
            }
            a_rs.close();
            a_ps.close();
            
            if (a_iSum > 0){
                a_arrayRet = new ArrayList<String>();
                String a_sVal = "";
                int a_iVal = 0;
                String a_sRet = "";
                int a_start_idx = ((h_pageNo-1)*_max_line_page_user) + 1;
                int a_end_idx = (h_pageNo*_max_line_page_user);
                a_sql = "SELECT row_number() over(ORDER BY USERCODE), * FROM NEWCUSTOMERMANAGE";
                if (a_sql_cond.length() > 0){
                    a_sql += " WHERE " + a_sql_cond;
                }
                a_sql += " ORDER BY USERCODE";
                a_sql = "SELECT * FROM (" + a_sql + ") s1 WHERE (s1.row_number BETWEEN " + String.valueOf(a_start_idx) + " AND " + String.valueOf(a_end_idx) + ");";
                a_ps = a_con.prepareStatement(a_sql);
                a_rs = a_ps.executeQuery();
                int a_iIdx = -1;
                while(a_rs.next()){
                    a_sRet = "";
                    //ユーザコード
                    a_sVal = _Environ.ExistDBString(a_rs,"USERCODE");
                    a_sRet += a_sVal;
                    //ユーザ名
                    a_sVal = _Environ.ExistDBString(a_rs,"USERNAME");
                    a_sRet += "\t" + a_sVal;
                    
                    a_arrayRet.add(a_sRet);
                }
                a_rs.close();
                a_ps.close();
            }
            
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[FindUser]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[FindUser]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        _Environ._MyLogger.info("*** FindRemoteTrouble is finished. ***");
        
        return a_arrayRet;
    }
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private String _makeSQLCond(){
        String a_sRet = "" ;
        String a_sTmp = "";
        
        //ユーザコード
        /*[2016.06.09]USERCODEでのリレーションが現状ない
        if (UserCode.length() > 0){
            if (a_sRet.length() > 0){
                a_sRet += " AND ";
            }
            a_sRet += "(CUSTOMERID=(SELECT ID FROM NEWCUSTOMERMANAGE WHERE (USERCODE='" + UserCode + "')))";
        }
        */
        //SET-NO
        if (SetNo.length() > 0){
            if (a_sRet.length() > 0){
                a_sRet += " AND ";
            }
            a_sRet += "(REMOTESETNAME='" + SetNo;
            //[2016.06.17]
            if (SetNo.indexOf("-SET") < 0){
                a_sRet += "-SET";
            }
            a_sRet += "')";
        }
        //REC番号
        if (RecNo.length() > 0){
            if (a_sRet.length() > 0){
                a_sRet += " AND ";
            }
            a_sRet += "(USERNUMBER='" + RecNo + "')";
        }
        //ユーザ名
        if (UserName.length() > 0){
            if (a_sRet.length() > 0){
                a_sRet += " AND ";
            }
            a_sRet += "(USERNAME LIKE '%" + UserName + "%')";
        }
        //開始日時
        if (TimeS.length() > 0){
            if (a_sRet.length() > 0){
                a_sRet += " AND ";
            }
            a_sRet += "(INITIATIONTIME >= '" + TimeS + "')";
        }
        //終了日時
        if (TimeE.length() > 0){
            if (a_sRet.length() > 0){
                a_sRet += " AND ";
            }
            a_sRet += "(INITIATIONTIME <= '" + TimeE + "')";
        }
        //障害メッセージ
        if (TroubleMsg.length() > 0){
            if (a_sRet.length() > 0){
                a_sRet += " AND ";
            }
            a_sRet += "(";
            a_sRet += "(MESSAGE1 LIKE '%" + TroubleMsg + "%')";

            //[2016.06.17]bug-fixed.↓
            a_sRet += " OR (EXISTS (SELECT TROUBLEINFORMATIONID FROM TROUBLEINFORMATIONEXTENSION WHERE";
            a_sRet += " (DETAIL LIKE '%" + TroubleMsg + "%')"+ " AND (TROUBLEINFORMATIONID=TROUBLEINFORMATION.ID)" + "))";
            //a_sRet += " OR (ID=(SELECT TROUBLEINFORMATIONID FROM TROUBLEINFORMATIONEXTENSION WHERE (DETAIL LIKE '%" + TroubleMsg + "%')))";
            //[2016.06.17]bug-fixed.↑

            a_sRet += " OR (MESSAGE2 LIKE '%" + TroubleMsg + "%')";
            a_sRet += " OR (MESSAGE3 LIKE '%" + TroubleMsg + "%')";
            a_sRet += ")";
        }
        //障害種別
        if ((TroubleKind_MJ.equals("true") == true) ||
            (TroubleKind_MN.equals("true") == true) ||
            (TroubleKind_GN.equals("true") == true)){
            if (a_sRet.length() > 0){
                a_sRet += " AND ";
            }
            a_sRet += "(";
            a_sTmp = "";
            if (TroubleKind_MJ.equals("true") == true){
                if (a_sTmp.length() > 0){
                    a_sTmp += " OR ";
                }
                a_sTmp += "(TROUBLELEVEL=2)";
            }
            if (TroubleKind_MN.equals("true") == true){
                if (a_sTmp.length() > 0){
                    a_sTmp += " OR ";
                }
                a_sTmp += "(TROUBLELEVEL=1)";
            }
            if (TroubleKind_GN.equals("true") == true){
                if (a_sTmp.length() > 0){
                    a_sTmp += " OR ";
                }
                a_sTmp += "(TROUBLELEVEL=7)";
            }
            a_sRet += a_sTmp;
            a_sRet += ")";
        }
        //連絡済み⇒上手く行かない（pending）
        if (Contact.equals("true") == true){
            if (a_sRet.length() > 0){
                a_sRet += " AND ";
            }
            a_sRet += "(CONTACTED";
            a_sRet += "=1";
            a_sRet += ")";
        }else{
            //a_sRet += "<>1";
        }

        return a_sRet;
    }

    private String _makeSQLCond_User(){
        String a_sRet = "" ;
        String a_sTmp = "";
        
        //ユーザ名
        if (UserName.length() > 0){
            if (a_sRet.length() > 0){
                a_sRet += " AND ";
            }
            a_sRet += "(USERNAME LIKE '%" + UserName + "%')";
        }

        return a_sRet;
    }
}
