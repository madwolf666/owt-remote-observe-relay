/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

import common.Environ;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Chappy
 */
@ManagedBean(name = "ReportMonthly")
@RequestScoped
public class ReportMonthly implements Serializable {
    //private CheckAny _CheckAny = null;
    public Environ _Environ = null;
    
    public String _db_driver = "";
    public String _db_server = "";
    public String _db_url = "";
    public String _db_user = "";
    public String _db_pass = "";
    public String _db_name = "";
    private int _max_line_page = 0;

    String Response;
 /*   HttpServletResponse  ServletResponse; */
        
    private String m_report_out_path = "/var/tmp/"; //[2016.05.17]bug-fixed.
    //private String m_tmp_path = "/var/tmp/";
    private String m_tmp_file = "";
    private String m_template_path = "/resources/template/template-report-monthly.xls";
    private String m_template_report = "/resources/template/template-report-monthly.xls";
            
    private Timestamp _TimeS = null;
    private Timestamp _TimeE = null;
    private Timestamp _ExecTime = null;

    private String _ReportId = "";

    private String System_IRMS = "";
    private String System_MSSV2 = "";
    private String System_PBX = "";
    private String TimeS = "";
    private String TimeE = "";
    private String SelectUser = "";
    private String ExecTime = "";
    private String SelectUserCode = "";
    
    private String OrderUserCode = "";

    //システム(IRMS)
    public void putSystem_IRMS(String h_val) {
        System_IRMS = h_val;
    }
    public String getSystem_IRMS() {
        return System_IRMS;
    }
    //システム(MSSV2)
    public void putSystem_MSSV2(String h_val) {
        System_MSSV2 = h_val;
    }
    public String getSystem_MSSV2() {
        return System_MSSV2;
    }
    //システム(PBX)
    public void putSystem_PBX(String h_val) {
        System_PBX = h_val;
    }
    public String getSystem_PBX() {
        return System_PBX;
    }
    //開始時間
    public void putTimeS(String h_val) {
        TimeS = h_val;
        try{
            _TimeS = _Environ.ToSqlTimestampFromString(TimeS + " 00:00:00");
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
            _TimeE = _Environ.ToSqlTimestampFromString(TimeE + " 00:00:00");
        } catch (Exception e) {
            _Environ._MyLogger.severe("[putTimeE]" + e.getMessage());
        }
    }
    public String getTimeE() {
        return TimeE;
    }
    //選択ユーザ
    public void putSelectUser(String h_val) {
        SelectUser = h_val;
    }
    public String getSelectUser() {
        return SelectUser;
    }
    //作成実施日時
    public void putExecTime(String h_val) {
        if (h_val.length() > 0){
            ExecTime = h_val;
        }else{
            Calendar a_cal = Calendar.getInstance();
            SimpleDateFormat a_sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            ExecTime = a_sdf.format(a_cal.getTime());
        }
        try{
            _ExecTime = _Environ.ToSqlTimestampFromString(ExecTime + ":00");
        } catch (Exception e) {
            _Environ._MyLogger.severe("[putExecTime]" + e.getMessage());
        }
    }
    public String getExecTime() {
        return ExecTime;
    }
    
    public void putSelectUserCode(String h_val) {
        SelectUserCode = h_val;
    }
    public String getSelectUserCode() {
        return SelectUserCode;
    }

    public String getResponse() {
        return Response;
    }

    //並び順
    public void putOrderUserCode(String h_val) {
        OrderUserCode = h_val;
    }
    public String getOrderUserCode() {
        return OrderUserCode;
    }

    private Workbook m_wb = null;
    private Sheet m_sht = null;
    private FormulaEvaluator m_evaluator = null;

    /**
     * Creates a new instance of ReportMonthly
     */
    public ReportMonthly(){
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
        
        _Environ._MyLogger.config("db_driver ---> " + _db_driver);
        _Environ._MyLogger.config("db_server ---> " + _db_server);
        _Environ._MyLogger.config("db_url ---> " + _db_url);
        _Environ._MyLogger.config("db_user ---> " + _db_user);
        _Environ._MyLogger.config("db_pass ---> " + _db_pass);
        _Environ._MyLogger.config("db_name ---> " + _db_name);
        _Environ._MyLogger.config("max_line_page ---> " + _max_line_page);
        _Environ._MyLogger.info("*** SetRealPath is finished. ***");
    }
    
    //月報作成の状態取得
    public String GetReportStatus() throws Exception{
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "SELECT * FROM REPORTSCHEDULE WHERE (STATUS='1');";
        String a_sVal = "";
        String a_sRet = "";
        String a_sUser = "";
        String a_sKind = "";
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                a_sRet = "";
                a_sUser = "";
                a_sKind = "";
                a_sVal= _Environ.ExistDBString(a_rs, "IS_IRMS");
                if (a_sVal.equals("1") == true){
                    a_sKind += "IRMS";
                }
                a_sVal= _Environ.ExistDBString(a_rs, "IS_MSSV2");
                if (a_sVal.equals("1") == true){
                    if (a_sKind.length()>0){
                        a_sKind += "/";
                    }
                    a_sKind += "MSSV2";
                }
                a_sVal= _Environ.ExistDBString(a_rs, "IS_PBX");
                if (a_sVal.equals("1") == true){
                    if (a_sKind.length()>0){
                        a_sKind += "/";
                    }
                    a_sKind += "PBX";
                }

                a_sKind = "(" + a_sKind + ")"; 

                a_sVal = _Environ.ExistDBString(a_rs, "USERKIND");
                if (a_sVal.equals("1") == true){
                    a_sUser = "全ユーザ";
                }else{
                    a_sUser = "ユーザコード：" + _Environ.ExistDBString(a_rs, "USERCODE");
                }
                
                a_sRet = "※" + a_sUser + "の月報を作成中です" + a_sKind;
            }
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[GetReportStatus]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[GetReportStatus]" + ex.getMessage());
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
        
        _Environ._MyLogger.info("*** GetReportStatus is finished. ***");
        
        return a_sRet;
    }
    
    //pagerの作成
    public String MakePagerUsers(int h_kind, int h_pageNo) throws Exception{
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "SELECT COUNT(ID) AS REC_SUM FROM NEWCUSTOMERMANAGE;";
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
            _Environ._MyLogger.severe("[MakePagerUsers]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[MakePagerUsers]" + ex.getMessage());
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
        _Environ._MyLogger.info("*** MakePagerUsers is finished. ***");
        
        return a_sRet;
    }
    
    public  ArrayList<String> FindUsers(int h_pageNo) throws Exception{
        ArrayList<String> a_arrayRet = null;
        int a_iSum = 0;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "SELECT COUNT(ID) AS REC_SUM FROM NEWCUSTOMERMANAGE;";
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
                a_sql = "SELECT row_number() over(ORDER BY USERCODE " + OrderUserCode + "), * FROM NEWCUSTOMERMANAGE ORDER BY USERCODE " + OrderUserCode;
                a_sql = "SELECT * FROM (" + a_sql + ") s1 WHERE (s1.row_number BETWEEN " + String.valueOf(a_start_idx) + " AND " + String.valueOf(a_end_idx) + ");";
                a_ps = a_con.prepareStatement(a_sql);
                a_rs = a_ps.executeQuery();
                while(a_rs.next()){
                    a_sRet = "";
                    //ユーザコード
                    a_sVal = _Environ.ExistDBString(a_rs,"USERCODE");
                    a_sRet += a_sVal;
                    //ユーザ名
                    a_sVal = _Environ.ExistDBString(a_rs,"USERNAME");
                    a_sRet += "\t" + a_sVal;
                    //月報の有無
                    a_sVal = _Environ.ExistDBString(a_rs,"MONTHREPORT");
                    if (a_sVal == "0"){
                        a_sVal = "無";
                    }else{
                        a_sVal = "有";
                    }
                    a_sRet += "\t" + a_sVal;
                    a_arrayRet.add(a_sRet);
                }
                a_rs.close();
                a_ps.close();
            }
            
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[FindUsers]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[FindUsers]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        _Environ._MyLogger.info("*** FindUsers is finished. ***");
        
        return a_arrayRet;
    }
    
    //月報スケジュールの登録
    public void InsertReportSchedule() throws Exception{
        Connection a_con = null;
        PreparedStatement a_ps = null;
        String a_sql = "INSERT INTO REPORTSCHEDULE (" +
                "REPORTID,IS_IRMS,IS_MSSV2,IS_PBX,STARTDATE,ENDDATE,USERKIND,EXECTIME,USERCODE,STATUS" + 
                ") VALUES(" +
                "?,?,?,?,?,?,?,?,?,?";
        a_sql += ");";
        String a_sTmp = "";
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_con.setAutoCommit(false);
            a_ps = a_con.prepareStatement(a_sql);
            _ReportId = make_ReportId();
            a_ps.setString(1, _ReportId);
            a_sTmp = "0";
            if (System_IRMS.equals("true") == true){
                a_sTmp = "1";
            }
            a_ps.setString(2, a_sTmp);
            a_sTmp = "0";
            if (System_MSSV2.equals("true") == true){
                a_sTmp = "1";
            }
            a_ps.setString(3, a_sTmp);
            a_sTmp = "0";
            if (System_PBX.equals("true") == true){
                a_sTmp = "1";
            }
            a_ps.setString(4, a_sTmp);
            a_ps.setTimestamp(5, _TimeS);
            a_ps.setTimestamp(6, _TimeE);
            a_ps.setString(7, SelectUser);
            a_ps.setTimestamp(8, _ExecTime);
            a_ps.setString(9, SelectUserCode);
            a_ps.setString(10, "0");
            /*
            if (ExecTime.length() <= 0){
                a_ps.setString(10, "1");
            }else{
                a_ps.setString(10, "0");
            }
            */
            int a_i = a_ps.executeUpdate();
            a_con.commit();
            
            if (ExecTime.length() <= 0){
                MakeReportNnthly();
            }
        } catch (SQLException e) {
            if (a_con != null){
                a_con.rollback();
            }
            _Environ._MyLogger.severe("[insert_ReportSchedule]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            if (a_con != null){
                a_con.rollback();
            }
            _Environ._MyLogger.severe("[insert_ReportSchedule]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        _Environ._MyLogger.info("*** insert_ReportSchedule is finished. ***");
    }
    
    //月報作成
    public void make_ReportMonthly(ResultSet h_rs) throws Exception{
        String a_sVal = "";
        while(h_rs.next()){
            _ReportId = _Environ.ExistDBString(h_rs, "REPORTID");
            a_sVal= _Environ.ExistDBString(h_rs, "IS_IRMS");
            if (a_sVal.equals("1") == true){
                putSystem_IRMS("true");
            }else{
                putSystem_IRMS("false");
            }
            a_sVal= _Environ.ExistDBString(h_rs, "IS_MSSV2");
            if (a_sVal.equals("1") == true){
                putSystem_MSSV2("true");
            }else{
                putSystem_MSSV2("false");
            }
            a_sVal= _Environ.ExistDBString(h_rs, "IS_PBX");
            if (a_sVal.equals("1") == true){
                putSystem_PBX("true");
            }else{
                putSystem_PBX("false");
            }
            
            _TimeS = _Environ.ToSqlTimestampFromString(_Environ.ExistDBString(h_rs, "STARTDATE").replace("-", "/"));
            TimeS = _TimeS.toString().replace("-", "/");    //[2016.03.04]
            _TimeE = _Environ.ToSqlTimestampFromString(_Environ.ExistDBString(h_rs, "ENDDATE").replace("-", "/"));
            TimeE = _TimeE.toString().replace("-", "/");    //[2016.03.04]
            putSelectUser(_Environ.ExistDBString(h_rs, "USERKIND"));
            _ExecTime = _Environ.ToSqlTimestampFromString(_Environ.ExistDBString(h_rs, "EXECTIME").replace("-", "/"));
            ExecTime = _ExecTime.toString().replace("-", "/");    //[2016.03.04]
            putSelectUserCode(_Environ.ExistDBString(h_rs, "USERCODE"));

            MakeReportNnthly();   //最終的にコメントを外す
        }
    }
    
    //月報作成
    public void MakeReportNnthly() throws Exception{
        Connection a_con = null;
        PreparedStatement a_ps = null;
        String a_sql = "UPDATE REPORTSCHEDULE SET STATUS='1' WHERE (REPORTID=?);";
        String a_sTmp = "";
        int a_i = 0;
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_con.setAutoCommit(false);
            a_ps = a_con.prepareStatement(a_sql);
            a_ps.setString(1, _ReportId);
            a_i = a_ps.executeUpdate();
            a_con.commit();
            
            //対象ユーザ取得
            ArrayList<String> a_arrayUser = null;   //[2016.02.25]
            //対象月取得
            ArrayList<String> a_arrayMonth = _getReport_MonthList();

            //if (a_arrayUser != null){
                if (System_IRMS.equals("true") == true){
                    a_arrayUser = _getReport_UserList("1"); //[2016.02.25]
                    _makeReport_IRMS(a_arrayUser, a_arrayMonth);
                }
                if (System_MSSV2.equals("true") == true){
                    a_arrayUser = _getReport_UserList("2"); //[2016.02.25]
                    _makeReport_MSSV2(a_arrayUser, a_arrayMonth);
                }
                if (System_PBX.equals("true") == true){
                    a_arrayUser = _getReport_UserList("0"); //[2016.02.25]
                    _makeReport_PBX(a_arrayUser, a_arrayMonth);
                }
            //}

        } catch (SQLException e) {
            if (a_con != null){
                a_con.rollback();
            }
            _Environ._MyLogger.severe("[MakeReportNnthly]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            if (a_con != null){
                a_con.rollback();
            }
            _Environ._MyLogger.severe("[MakeReportNnthly]" + ex.getMessage());
        } finally{
            //[2016.05.17]bug-fixed.↓
            try{
                a_sql = "DELETE FROM REPORTSCHEDULE WHERE (REPORTID=?);";
                a_ps = a_con.prepareStatement(a_sql);
                a_ps.setString(1, _ReportId);
                a_i = a_ps.executeUpdate();
                a_con.commit();
            }catch (SQLException e) {
                if (a_con != null){
                    a_con.rollback();
                }
                _Environ._MyLogger.severe("[MakeReportNnthly]" + e.getMessage());
            }
            //[2016.05.17]bug-fixed.↑
            
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        _Environ._MyLogger.info("*** MakeReportNnthly is finished. ***");
    }

    //未使用[2016.02.24]
/*
    public void MakeReportNnthly2(){
        
        
        //_copyToTmpFile();
        _setReportInfo();
        //return null;
    }
*/
    
    //--------------------------------------------------------------------------
    //JobId生成
    private String make_ReportId(){
        String a_sRet = "";
        try{
            java.util.Date a_date = new java.util.Date();
            a_sRet = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(a_date);
        } catch (Exception e) {
            _Environ._MyLogger.severe("[make_ReportId]" + e.getMessage());
        }
        return a_sRet;
    }

    private ArrayList<String> _getReport_UserList(String h_remote_kind) throws Exception{
        ArrayList<String> a_arrayRet = null;
        int a_iSum = 0;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";
        //int a_iRet = 0;
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            
            String a_sVal = "";
            int a_iVal = 0;
            String a_sRet = "";
            a_sql = "SELECT USERCODE,USERNAME,ID FROM NEWCUSTOMERMANAGE WHERE (REMOTEKIND=" + h_remote_kind + ")";
            /*[2016.01.06
            a_sql += " AND (ID IN (";
            a_sql += "SELECT CUSTOMERID FROM TROUBLEINFORMATION WHERE";
            a_sql += "(INITIATIONTIME>='" + TimeS + " 00:00:00')";
            if (TimeE.length() > 0){
                a_sql += "(INITIATIONTIME<='" + TimeE + " 00:00:00')";
            }
            a_sql += "))";
            */
            if (SelectUser.equals("2") == true){
                a_sql += " AND (USERCODE='" + SelectUserCode + "')";
            }
            a_sql += " ORDER BY USERCODE";  //[2016.06.07]
            a_sql += ";";
            
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                if (a_iSum <= 0){
                    a_arrayRet = new ArrayList<String>();
                }
                a_iSum++;
                
                a_sRet = "";
                
                //ユーザコード
                a_sVal = _Environ.ExistDBString(a_rs,"USERCODE");
                a_sRet += a_sVal;
                
                //ユーザ名
                a_sVal = _Environ.ExistDBString(a_rs,"USERNAME");
                a_sRet += "\t" + a_sVal;
                
                //ID
                a_sVal = String.valueOf(_Environ.ExistDBInt(a_rs,"ID"));
                a_sRet += "\t" + a_sVal;
                
                a_arrayRet.add(a_sRet);
            }
            a_rs.close();
            a_ps.close();
            
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[_getReport_UserList]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[_getReport_UserList]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        _Environ._MyLogger.info("*** _getReport_UserList is finished. ***");
        
        return a_arrayRet;
    }

    private ArrayList<String> _getReport_MonthList() throws Exception{
        ArrayList<String> a_arrayRet = null;

        try{
            a_arrayRet = new ArrayList<String>();

            String a_sVal = "";
            String a_sRet = "";

            //終了日を設定
            if (TimeE.length() <= 0){
                try{
                    java.util.Date a_date = new java.util.Date();
                    TimeE = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(a_date);
                } catch (Exception e) {
                    _Environ._MyLogger.severe("[_getReport_MonthList]" + e.getMessage());
                }
            }
            
            //開始日から終了日までを計算
            Calendar a_cal1 = Calendar.getInstance();
            Calendar a_cal2 = Calendar.getInstance();
            SimpleDateFormat a_sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            a_cal1.set(
                Integer.valueOf(TimeS.substring(0, 4)),
                Integer.valueOf(TimeS.substring(5, 7)) -1,
                Integer.valueOf(TimeS.substring(8, 10))
                );
            a_cal2.set(
                Integer.valueOf(TimeE.substring(0, 4)),
                Integer.valueOf(TimeE.substring(5, 7)) - 1,
                Integer.valueOf(TimeE.substring(8, 10))
                );
            
            while (a_cal1.compareTo(a_cal2) <= 0){
                a_sRet = "";
            
                //月始め
                a_sVal = a_sdf1.format(a_cal1.getTime());
                a_sRet += a_sVal;
                
                //月末
                int a_last = a_cal1.getActualMaximum(Calendar.DATE);
                a_cal1.set(
                    Integer.valueOf(a_sVal.substring(0, 4)),
                    Integer.valueOf(a_sVal.substring(5, 7)) - 1,
                    a_last
                );
                if (a_cal1.compareTo(a_cal2) <= 0){
                    a_sVal = a_sdf1.format(a_cal1.getTime());
                }else{
                    a_sVal = a_sdf1.format(a_cal2.getTime());
                }
                a_sRet += "\t" + a_sVal;
                
                a_arrayRet.add(a_sRet);
                
                //1日加算
                a_cal1.add(Calendar.DATE, 1);
            }
            
        } catch (Exception ex) {
            _Environ._MyLogger.severe("[_getReport_MonthList]" + ex.getMessage());
        } finally{
        }

        _Environ._MyLogger.info("*** _getReport_MonthList is finished. ***");
        
        return a_arrayRet;
    }

    private ArrayList<String> _getReport_SouchiList(
        String[] h_splitUser
        ) throws Exception{
        ArrayList<String> a_arrayRet = null;
        int a_iSum = 0;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";
        //int a_iRet = 0;
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            
            String a_sVal = "";
            int a_iVal = 0;
            String a_sRet = "";
            a_sql = "SELECT USERNODENAME,USERIPADDR FROM USERNODE WHERE ";
            a_sql += "(USERCODE='" + h_splitUser[0] + "')";
            a_sql += " ORDER BY NODECODE";
            a_sql += ";";
            
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                if (a_iSum <= 0){
                    a_arrayRet = new ArrayList<String>();
                }
                a_iSum++;
                
                a_sRet = "";
                //装置名
                a_sVal = _Environ.ExistDBString(a_rs,"USERNODENAME");
                a_sRet += a_sVal;
                //IPアドレス
                a_sVal = _Environ.ExistDBString(a_rs,"USERIPADDR");
                a_sRet += "\t" + a_sVal;
                a_arrayRet.add(a_sRet);
            }
            a_rs.close();
            a_ps.close();
            
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[_getReport_SouchiList]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[_getReport_SouchiList]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        _Environ._MyLogger.info("*** _getReport_SouchiList is finished. ***");
        
        return a_arrayRet;
    }
        
    private void _copyToTmpFile(
        String h_tempXLS,
        String h_kind,
        String h_userCode,
        String h_month
        ){
        //カレントディレクトリを取得
        //String a_path = new File(".").getAbsoluteFile().getParent();
        //String a_path = System.getProperty("user.dir");

        //一時ディレクトリを取得
        m_report_out_path = _Environ.GetEnvironValue("report_out_path");    //[2016.05.17]bug-fixed.
        //m_tmp_path = _Environ.GetEnvironValue("tmp_path");
        //テンプレートを取得
        m_template_path = _Environ.GetEnvironValue("template_path");
        m_template_report = _Environ.GetEnvironValue(h_tempXLS);
        //m_template_report = _Environ.GetEnvironValue("template_report_monthly");

        //ディレクトリを作成
        File a_newDir = new File(m_report_out_path + "/" + _ReportId);  //[2016.05.17]bug-fixed.
        //File a_newDir = new File(m_tmp_path + "/" + _ReportId);
        try{
            a_newDir.mkdir();
        }catch(Exception ex){
        }
        
        //一時ディレクトリにコピーする。
        //[2016.03.04]
        m_tmp_file = h_userCode;
        if (h_kind.equals("mssv2_shindan") == false){
            m_tmp_file += "_";
        }
        m_tmp_file += h_kind + "_";
        try{
            Date a_today = DateFormat.getDateInstance().parse(h_month);
            SimpleDateFormat a_dateFormat = new SimpleDateFormat("yyyyMM");
            m_tmp_file +=  a_dateFormat.format(a_today);
        } catch (Exception ex) {
            Logger.getLogger(ReportMonthly.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
        long a_currentTimeMillis = System.currentTimeMillis();
        Date a_today = new Date(a_currentTimeMillis);
        SimpleDateFormat a_dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        */
        
        m_tmp_file += ".xls";
        Response = m_report_out_path + "/" + _ReportId + "/" + m_tmp_file;  //[2016.05.17]bug-fixed.
        //Response = m_tmp_path + "/" + _ReportId + "/" + m_tmp_file;
        try{
            FileChannel a_srcChannel = new FileInputStream(m_template_path + m_template_report).getChannel();
            FileChannel a_dstChannel = new FileOutputStream(Response).getChannel();
            try{
                a_srcChannel.transferTo(0, a_srcChannel.size(), a_dstChannel);
            } catch (IOException ex) {
                Logger.getLogger(ReportMonthly.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                try {
                    a_srcChannel.close();
                } catch (IOException ex) {
                    Logger.getLogger(ReportMonthly.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    a_dstChannel.close();
                } catch (IOException ex) {
                    Logger.getLogger(ReportMonthly.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReportMonthly.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //IRMS
    private void _makeReport_IRMS(
        ArrayList<String> h_arrayUser,
        ArrayList<String> h_arrayMonth
        ) throws Exception{
        int a_iSum = 0;
        
        FileInputStream a_in = null;
        FileOutputStream a_os = null;

        for (int a_iCnt=0; a_iCnt<h_arrayUser.size(); a_iCnt++){
            String[] a_splitUser = h_arrayUser.get(a_iCnt).split("\t");

            for (int a_iCnt2=0; a_iCnt2<h_arrayMonth.size(); a_iCnt2++){
                String[] a_splitMonth = h_arrayMonth.get(a_iCnt2).split("\t");

                //Excelテンプレートファイルのコピー
                _copyToTmpFile("template_report_monthly_ip", "ip", a_splitUser[0], a_splitMonth[0]);

                File a_f = null;    //[2016.03.04]
                try{
                    //Excel
                    a_f = new File(Response);   //[2016.03.04]

                    //ワークブックを開く
                    a_in = new FileInputStream(a_f);
                    m_wb = WorkbookFactory.create(a_in);
                    m_evaluator = m_wb.getCreationHelper().createFormulaEvaluator();
                    a_in.close();   //ここで入力ストリームを閉じる
                    a_f.delete();   //一時ファイルを削除

                    //Cover[2016.06.10]
                    //_setReport_Cover_com(a_splitUser, a_splitMonth);
                    _setReport_Cover_ip(a_splitUser, a_splitMonth);

                    //定期診断
                    _setReport_ShindanKekka_ip(a_splitUser, a_splitMonth);

                    //障害発生件数
                    _setReport_ShougaiKensu_ip(a_splitUser, a_splitMonth);

                    //定期診断詳細
                    _setReport_ShindanSyosai_ip(a_splitUser, a_splitMonth);
                    
                    //障害内容一覧
                    _setReport_ShougaiNaiyo_ip(a_splitUser, a_splitMonth);
                    
                    //再計算
                    //m_evaluator.evaluateAll();
                    //編集内容を保存
                    //a_os = new FileOutputStream(a_f);
                    //m_wb.write(a_os);
                    //m_wb.close();
                }catch (Exception ex){
                    _Environ._MyLogger.severe("[_makeReport_IRMS]" + ex.getMessage());
                }finally{
                    //[2016.03.04]
                    if (m_wb != null){
                        //再計算
                        m_evaluator.evaluateAll();
                        //編集内容を保存
                        a_os = new FileOutputStream(a_f);
                        m_wb.write(a_os);
                        m_wb.close();
                    }
                    if (a_os != null){
                        a_os.close();
                    }
                    if (a_in != null){
                        a_in.close();
                    }
                }
            }
        }

        _Environ._MyLogger.info("*** _makeReport_IRMS is finished. ***");
    }
    
    //未使用[2016.02.24:
 /*
    private void _setReportInfo(){
        //------------------------------------------------------------------
        // Excel確認
        //------------------------------------------------------------------
        File a_f = new File(Response);
        FileInputStream a_in = null;
        FileOutputStream a_os = null;
        try{
            //ワークブックを開く
            a_in = new FileInputStream(a_f);
            m_wb = WorkbookFactory.create(a_in);
            m_evaluator = m_wb.getCreationHelper().createFormulaEvaluator();
            a_in.close();   //ここで入力ストリームを閉じる
            a_f.delete();   //一時ファイルを削除

            //編集
            //_setReportInfo_Cover();     //Cover
            //_setReportInfo_Shindan();   //定期診断結果
            //_setReportInfo_Occur();     //障害発生件数
            //_setReportInfo_List();      //障害内容一覧
           
            //再計算
            m_evaluator.evaluateAll();
            
           //編集内容を保存
            //a_os = new FileOutputStream(a_f);
            //m_wb.write(a_os);
            //m_wb.close();
        }catch(IOException e){
           System.out.println(e.toString());
        } catch (InvalidFormatException ex) {
            Logger.getLogger(ReportMonthly.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EncryptedDocumentException ex) {
            Logger.getLogger(ReportMonthly.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
        }
        
        //Excelファイルをダウンロードさせる。
        //FacesContext a_facesContext;
        ExternalContext a_externalContext  = (ExternalContext)FacesContext.getCurrentInstance().getExternalContext();
        ServletResponse = (HttpServletResponse)a_externalContext.getResponse();
        
        ServletResponse.setContentType("application/msexcel");
        
        // ファイル名に日本語を使うなら、下記のようにエンコードする
        //byte[] sjis = "サンプル.xls".getBytes("Shift_JIS");
        String fname = new String(sjis, "ISO8859_1");
        // 英数字なら単純に String fname = "sample.xml" で良い
        
        ServletResponse.setHeader("Content-Disposition", "attachment; filename=" + m_tmp_file);
        try{
            ServletOutputStream a_out = ServletResponse.getOutputStream();      
            m_wb.write(a_out);
            a_out.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        //m_wb.close();
        
        //return null;
    }
*/
    //共通：表紙
    private void _setReport_Cover_com(
        String[] h_splitUser,
        String[] h_splitMonth
        ){
        String a_sTmp = "";
        
        m_sht = m_wb.getSheet("表紙");
        Row a_row = null;
        Cell a_cell = null;
        
        //yyyy年mm月分
        try{
            Date  a_date = DateFormat.getDateInstance().parse(h_splitMonth[0]);
            a_sTmp = new SimpleDateFormat("(yyyy年MM月分)").format(a_date);
        } catch (Exception e) {
            _Environ._MyLogger.severe("[_setReport_Cover]" + e.getMessage());
        }
        a_row = m_sht.getRow(5);    //[2016.06.10]9⇒5
        _setCellValueStringExist(a_row, 0, a_sTmp, null);   //[2016.06.10]2⇒0
        
        //お客様名
        a_row = m_sht.getRow(7);   //[2016.06.10]11⇒7
        _setCellValueStringExist(a_row, 0, "お客様名：" + h_splitUser[1] + "　様", null);   //[2016.06.10]2⇒0

        //出力日
        try{
            java.util.Date a_date = new java.util.Date();
            a_sTmp = new SimpleDateFormat("yyyy年 MM月 dd日").format(a_date);
        } catch (Exception e) {
            _Environ._MyLogger.severe("[_setReport_Cover]" + e.getMessage());
        }
        a_row = m_sht.getRow(14);   //[2016.06.10]18⇒14
        _setCellValueStringExist(a_row, 3, a_sTmp, null);   //[2016.06.10]5⇒3
        
        //承認[2016.06.10]
        a_sTmp = a_sTmp.replace("年", ".").replace("月", ".").replace("日", "").replace(" ", "");
        a_row = m_sht.getRow(21);   //[2016.06.10]18⇒14
        _setCellValueStringExist(a_row, 3, a_sTmp, null);
        _setCellValueStringExist(a_row, 4, a_sTmp, null);
        _setCellValueStringExist(a_row, 5, a_sTmp, null);
        
        _setCellActivate(0,0);
    }
    
    //IRMS：表紙
    private void _setReport_Cover_ip(
        String[] h_splitUser,
        String[] h_splitMonth
        ){
        String a_sTmp = "";
        
        m_sht = m_wb.getSheet("Cover");
        Row a_row = null;
        Cell a_cell = null;
        
        //yyyy年mm月分
        try{
            Date  a_date = DateFormat.getDateInstance().parse(h_splitMonth[0]);
            a_sTmp = new SimpleDateFormat("(yyyy年MM月分)").format(a_date);
        } catch (Exception e) {
            _Environ._MyLogger.severe("[_setReport_Cover]" + e.getMessage());
        }
        a_row = m_sht.getRow(11);
        _setCellValueStringExist(a_row, 0, a_sTmp, null);
        
        //お客様名
        a_row = m_sht.getRow(14);
        _setCellValueStringExist(a_row, 0, "お客様名：" + h_splitUser[1] + "　様", null);

        //出力日
        try{
            java.util.Date a_date = new java.util.Date();
            a_sTmp = new SimpleDateFormat("yyyy年 MM月 dd日").format(a_date);
        } catch (Exception e) {
            _Environ._MyLogger.severe("[_setReport_Cover]" + e.getMessage());
        }
        a_row = m_sht.getRow(32);
        _setCellValueStringExist(a_row, 3, a_sTmp, null);

        //承認[2016.06.10]
        a_sTmp = a_sTmp.replace("年", ".").replace("月", ".").replace("日", "").replace(" ", "");
        a_row = m_sht.getRow(39);
        _setCellValueStringExist(a_row, 3, a_sTmp, null);
        _setCellValueStringExist(a_row, 4, a_sTmp, null);
        _setCellValueStringExist(a_row, 5, a_sTmp, null);
                
        _setCellActivate(0,0);
    }

    //IRMS：定期診断
    private void _setReport_ShindanKekka_ip(
        String[] h_splitUser,
        String[] h_splitMonth
        ) throws Exception{
        m_sht = m_wb.getSheet("定期診断結果");
        Row a_row = null;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";

        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            String a_sVal = "";
            int a_iVal = 0;
            String a_sRet = "";
            String a_sShindanTime = "";
            String a_sShindanTimeN = "";
            a_sql = "SELECT DIAGINTERVAL,DIAGTIME FROM IRMSREMOTECUSTOMER WHERE ";
            a_sql += "(USERCODE='" + h_splitUser[0] + "')";
            a_sql += ";";
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                a_sRet = "";
 
                //診断間隔
                a_sVal = _Environ.ExistDBString(a_rs,"DIAGINTERVAL");
                a_row = m_sht.getRow(2);
                _setCellValueStringExist(a_row, 0, "診断間隔= " + a_sVal + "日毎", null);

                //診断時刻
                a_sShindanTime = _Environ.ExistDBString(a_rs,"DIAGTIME");
                a_row = m_sht.getRow(2);
                _setCellValueStringExist(a_row, 2, "診断時刻= " + a_sShindanTime, null);
                //[2016.06.07]診断結果取得方法の変更
                String a_split[] = a_sShindanTime.split(":");
                a_sShindanTimeN = a_split[0] + ":" + String.format("%02d", Integer.valueOf(a_split[1]) - 1);
            }
            a_rs.close();
            a_ps.close();

            //期間
            a_sVal = h_splitMonth[0].replace("/", "-").substring(0,10) + " ～ " + h_splitMonth[1].replace("/", "-").substring(0,10);
            a_row = m_sht.getRow(5);
            _setCellValueStringExist(a_row, 0, "期間　 " + a_sVal, null);

            //日付の設定
            int a_iDay1 = Integer.valueOf(h_splitMonth[0].substring(8,10));
            int a_iDay2 = Integer.valueOf(h_splitMonth[1].substring(8,10));
            for (int a_i=1; a_i<=31; a_i++){
                if ((a_i < a_iDay1) || (a_i > a_iDay2)){
                    a_row = m_sht.getRow(9);
                    _setCellValueStringExist(a_row, a_i, "", null);
                }
            }
            
            //装置名
            ArrayList<String> a_arraySouchi = _getReport_SouchiList(h_splitUser);
            if (a_arraySouchi != null){
                CellStyle a_style = m_wb.createCellStyle();
                a_style.setBorderTop(CellStyle.BORDER_THIN);
                a_style.setBorderBottom(CellStyle.BORDER_THIN);
                a_style.setBorderLeft(CellStyle.BORDER_THIN);
                a_style.setBorderRight(CellStyle.BORDER_THIN);
                a_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                //[2016.06.10]
                CellStyle a_style2 = a_style;
                a_style2.setAlignment(CellStyle.ALIGN_CENTER);
                
                for (int a_i=0; a_i<a_arraySouchi.size(); a_i++){
                    String[] a_split = a_arraySouchi.get(a_i).split("\t");
                    a_row = m_sht.createRow(11 + a_i);  //[2016.03.04]
                    _setCellValueStringNew(a_row, 0, a_split[0], a_style);
                    
                    //日付の設定（記事欄を含む）
                    for (int a_j=1; a_j<=32; a_j++){
                        _setCellValueStringNew(a_row, a_j, "", a_style);
                        if ((a_j >= a_iDay1) && (a_j <= a_iDay2)){
                            _setCellValueStringExist(a_row, a_j, "－", a_style2);   //[2016.06.10]
                        }
                    }
                    
                    //[2016.06.07]診断結果の取得方法変更
                    for (int a_j=a_iDay1; a_j<=a_iDay2; a_j++){
                        //診断結果の取得
                        //[2016.06.07]SQL文現地対応
                        a_sql = "SELECT TO_CHAR(DATETIME,'YYYY-MM-DD HH24:MI:SS') AS DATETIME,RASCODE FROM OPERATIONLOG WHERE ";
                        a_sql += "(USERCODE='" + h_splitUser[0] + "')";
                        a_sql += " AND (USERIPADDR='" + a_split[1] + "')";
                        a_sql += " AND (TO_CHAR(DATETIME,'YYYY/MM/DD HH24:MI:SS') BETWEEN '" +
                                h_splitMonth[0].substring(0,8) + String.format("%02d", a_j) + " " + a_sShindanTime + ":00' AND '" +
                                h_splitMonth[0].substring(0,8) + String.format("%02d", a_j + 1) + " " + a_sShindanTimeN + ":59')";
                        a_sql += " AND (RASCODE IN ('S001','S002'))";
                        a_sql += " AND (DIAGMETHOD='1')";  //[2016.06.09]bug-fixed.
                        a_sql += " ORDER BY RASCODE DESC";
//                        a_sql += " ORDER BY DATETIME";
                        a_sql += ";";
                        //_Environ._MyLogger.severe("[_setReport_ShindanKekka_ip]" + a_sql);
                        a_ps = a_con.prepareStatement(a_sql);
                        a_rs = a_ps.executeQuery();
                        while(a_rs.next()){
                            a_sRet = "";

                            a_sVal = _Environ.ExistDBString(a_rs,"RASCODE");
                            //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                            _setCellValueStringExist(a_row, a_j, _Environ.ToRasCodeKigo(a_sVal), a_style2); //[2016.06.10]
/*
                            int a_iDay = 0;
                            String a_sTime = "";
                            String a_sTime2 = "";
                            //日付
                            a_sVal = _Environ.ExistDBString(a_rs,"DATETIME");
                            if (a_sVal.length()>0){
                                a_sVal = a_sVal.substring(0,16).replace("-", "/");
                                a_iDay = Integer.valueOf(a_sVal.substring(8,10));
                                a_sTime = a_sVal.substring(11,16);
                            }
                            //時刻が同じものを対象とする。
                            if (a_sTime.equals(a_sShindanTime) == true){
                                for (int a_k=1; a_k<=31; a_k++){
                                    if (a_k == a_iDay){
                                        //診断結果
                                        a_sVal = _Environ.ExistDBString(a_rs,"RASCODE");
                                        //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                                        _setCellValueStringExist(a_row, a_k, _Environ.ToRasCodeKigo(a_sVal), null);
                                        //記事⇒自動出力不要[2015.07.24]OWT
                                        break;
                                    }
                                }
                            }
*/
                        }
                        a_rs.close();
                        a_ps.close();
                    }
                }
            }

            _setCellActivate(0,0);
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[_setReport_ShindanKekka_ip]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[_setReport_ShindanKekka_ip]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        _Environ._MyLogger.info("*** _setReport_ShindanKekka_ip is finished. ***");
    }
    
    //IRMS;障害発生件数
    private void _setReport_ShougaiKensu_ip(
        String[] h_splitUser,
        String[] h_splitMonth
        ) throws Exception{
        m_sht = m_wb.getSheet("障害発生件数");
        Row a_row = null;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";

        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            String a_sVal = "";
            int a_iVal = 0;
            String a_sRet = "";
            String a_sShindanTime = "";
            a_sql = "SELECT REMOTECLASS FROM NEWCUSTOMERMANAGE WHERE ";
            a_sql += "(ID='" + h_splitUser[2] + "')";   //[2016.02.25]
            //a_sql += "(USERCODE='" + h_splitUser[0] + "')";
            a_sql += ";";
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                a_sRet = "";
 
                //リモート契約クラス
                a_sVal = _Environ.ExistDBString(a_rs,"REMOTECLASS");
                a_row = m_sht.getRow(2);
                _setCellValueStringExist(a_row, 0, "リモート契約クラス= " + a_sVal, null);
            }
            a_rs.close();
            a_ps.close();
            
            //期間
            a_sVal = h_splitMonth[0].replace("/", "-").substring(0,10) + " ～ " + h_splitMonth[1].replace("/", "-").substring(0,10);
            a_row = m_sht.getRow(5);
            _setCellValueStringExist(a_row, 0, "期間　 " + a_sVal, null);

            //日付の設定
            int a_iDay1 = Integer.valueOf(h_splitMonth[0].substring(8,10));
            int a_iDay2 = Integer.valueOf(h_splitMonth[1].substring(8,10));
            for (int a_i=1; a_i<=31; a_i++){
                if ((a_i < a_iDay1) || (a_i > a_iDay2)){
                    a_row = m_sht.getRow(9);
                    _setCellValueStringExist(a_row, a_i, "", null);
                }
            }
            
            //装置名
            ArrayList<String> a_arraySouchi = _getReport_SouchiList(h_splitUser);
            if (a_arraySouchi != null){
                CellStyle a_style = m_wb.createCellStyle();
                a_style.setBorderTop(CellStyle.BORDER_THIN);
                a_style.setBorderBottom(CellStyle.BORDER_THIN);
                a_style.setBorderLeft(CellStyle.BORDER_THIN);
                a_style.setBorderRight(CellStyle.BORDER_THIN);
                a_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                //[2016.06.10]
                CellStyle a_style2 = a_style;
                a_style2.setAlignment(CellStyle.ALIGN_CENTER);
                
                for (int a_i=0; a_i<a_arraySouchi.size(); a_i++){
                    String[] a_split = a_arraySouchi.get(a_i).split("\t");
                    a_row = m_sht.createRow(11 + a_i);  //[2016.03.04]
                    _setCellValueStringNew(a_row, 0, a_split[0], a_style);
                    
                    //合計
                    int a_iGokei = 0;   

                    //日付の設定（合計欄を含む）
                    for (int a_j=1; a_j<=32; a_j++){
                        _setCellValueStringNew(a_row, a_j, "", a_style);
                        if ((a_j >= a_iDay1) && (a_j <= a_iDay2)){
                            _setCellValueStringExist(a_row, a_j, "－", a_style2);   //[2016.06.10]
                        }
                    }
                    
                    String a_sYM = h_splitMonth[0].substring(0,8);

                    //障害発生件数の取得
                    //[2016.06.07]SQL文現地対応
                    for (int a_j=a_iDay1; a_j<=a_iDay2; a_j++){
                        a_sql = "SELECT COUNT(CUSTOMERID) AS REC_NUM FROM TROUBLEINFORMATION WHERE ";
                        a_sql += "(USERNAME=(SELECT USERNAME FROM NEWCUSTOMERMANAGE WHERE (USERCODE='" + h_splitUser[0] + "')))";
                        a_sql += " AND (IDCODE='" + a_split[0] +"')";
                        a_sql += " AND (TO_CHAR(INITIATIONTIME,'YYYY/MM/DD HH24:MI:SS') BETWEEN '" + a_sYM + String.format("%02d", a_j) + " 00:00:00' AND '"+ a_sYM + String.format("%02d", a_j) + " 23:59:59')";
//                        a_sql += " AND (INITIATIONTIME>='" + a_sYM + String.valueOf(a_j) + " 00:00:00')";
//                        a_sql += " AND (INITIATIONTIME<='" + a_sYM + String.valueOf(a_j) + " 23:59:59')";
                        a_sql += "AND (TROUBLELEVEL IN (1,2,7))";
                        a_sql += ";";
                        //_Environ._MyLogger.severe("[_setReport_ShougaiKensu_ip]" + a_sql);
                        a_ps = a_con.prepareStatement(a_sql);
                        a_rs = a_ps.executeQuery();
                        a_iVal = 0; //[2016.06.10]bug-fixed.
                        while(a_rs.next()){
                            a_sRet = "";

                            //件数
                            a_iVal = _Environ.ExistDBInt(a_rs,"REC_NUM");
                            if (a_iVal > 0){
                                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                                //[2016.06.10]
                                //_setCellValueStringExist(a_row, a_j, String.valueOf(a_iVal), null);
                                _setCellValueIntExist(a_row, a_j, a_iVal, a_style2);
                            }
                            
                            //合計
                            a_iGokei += a_iVal;
                        }
                        a_rs.close();
                        a_ps.close();                        
                    }
                    
                    //合計
                    //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                    //[3016.06.10]
                    //_setCellValueStringExist(a_row, 32, String.valueOf(a_iGokei), null);
                    _setCellValueIntExist(a_row, 32, a_iGokei, a_style2);
                }
            }
            
            _setCellActivate(0,0);
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[_setReport_ShougaiKensu_ip]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[_setReport_ShougaiKensu_ip]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        _Environ._MyLogger.info("*** _setReport_ShougaiKensu_ip is finished. ***");
    } 

    //IRMS;定期診断詳細
    private void _setReport_ShindanSyosai_ip(
        String[] h_splitUser,
        String[] h_splitMonth
        ) throws Exception{
        m_sht = m_wb.getSheet("定期診断詳細");
        Row a_row = null;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";

        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            String a_sVal = "";
            int a_iVal = 0;
            String a_sRet = "";
            
            //期間
            a_sVal = h_splitMonth[0].replace("/", "-").substring(0,10) + " ～ " + h_splitMonth[1].replace("/", "-").substring(0,10);
            a_row = m_sht.getRow(2);
            _setCellValueStringExist(a_row, 0, "期間　 " + a_sVal, null);

            //OperationLogより集計期間内における該当UserCodeの診断失敗情報(S001以外)を取得する
            //障害名称はRasCodeをキーにRasPingTrbInfoテーブルより障害名称(FltName)を取得する
            CellStyle a_style = m_wb.createCellStyle();
            a_style.setBorderTop(CellStyle.BORDER_THIN);
            a_style.setBorderBottom(CellStyle.BORDER_THIN);
            a_style.setBorderLeft(CellStyle.BORDER_THIN);
            a_style.setBorderRight(CellStyle.BORDER_THIN);
            a_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                
            String a_sYM = h_splitMonth[0].substring(0,8);

            //障害発生件数の取得
            //[2016,06,07]SQL文現地対応
            a_sql = "SELECT *";
            a_sql += ", TO_CHAR(DATETIME,'YYYY-MM-DD HH24:MI:SS') AS DATETIME2";
            a_sql += ", (SELECT USERNODENAME FROM USERNODE WHERE (USERCODE=OPERATIONLOG.USERCODE) AND (USERIPADDR=OPERATIONLOG.USERIPADDR)) AS USERNODENAME";
            a_sql += ", (SELECT FLTNAME FROM RASPINGTRBINFO WHERE (RASCODE=OPERATIONLOG.RASCODE)) AS FLTNAME";
            a_sql += " FROM OPERATIONLOG WHERE ";
            a_sql += "(USERCODE='" + h_splitUser[0] + "')";
            a_sql += " AND (TO_CHAR(DATETIME,'YYYY/MM/DD HH24:MI:SS') BETWEEN '"+ h_splitMonth[0].substring(0,10) + " 00:00:00' AND '"+ h_splitMonth[1].substring(0,10) + " 23:59:59')";
//            a_sql += " AND (DATETIME>='" + h_splitMonth[0].substring(0,10) + " 00:00:00')";
//            a_sql += " AND (DATETIME<='" + h_splitMonth[1].substring(0,10) + " 23:59:59')";
            a_sql += " AND (RASCODE IN ('S002'))";  //[2016.06.13]bug-fixed.⇒S002のみを対象
            a_sql += " AND (DIAGMETHOD='1')";  //[2016.06.09]bug-fixed.
            a_sql += " ORDER BY DATETIME";
            a_sql += ";";
            //_Environ._MyLogger.severe("[_setReport_ShindanSyosai_ip]" + a_sql);
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            
            int a_i = 0;
            while(a_rs.next()){
                a_sRet = "";
                a_row = m_sht.createRow(7 + a_i);   //[2016.03.04]
                for (int a_j=0; a_j<=5; a_j++){
                    _setCellValueStringNew(a_row, a_j, "", a_style);
                }

                //発生日時
                a_sVal = _Environ.ExistDBString(a_rs,"DATETIME2");  //[2016.06.07]SQL文現地対応
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 0, a_sVal, null);

                //監視装置名
                a_sVal = _Environ.ExistDBString(a_rs,"USERNODENAME");
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 1, a_sVal, null);
                
                //失敗内容
                a_sVal = _Environ.ExistDBString(a_rs,"FLTNAME");
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 2, a_sVal, null);
                
                //要因
                a_sVal = _Environ.ExistDBString(a_rs,"CAUSE");
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 3, a_sVal, null);
                
                //処置
                a_sVal = _Environ.ExistDBString(a_rs,"ACTION");
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 4, a_sVal, null);
                
                //記事
                a_sVal = _Environ.ExistDBString(a_rs,"COMMENTARY");
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 5, a_sVal, null);
            
                a_i++;
            }
            a_rs.close();
            a_ps.close();                        

            _setCellActivate(0,0);
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[_setReport_ShindanSyosai_ip]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[_setReport_ShindanSyosai_ip]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        _Environ._MyLogger.info("*** _setReport_ShindanSyosai_ip is finished. ***");
    }
    
    //IRMS;障害一覧
    private void _setReport_ShougaiNaiyo_ip(
        String[] h_splitUser,
        String[] h_splitMonth
        ) throws Exception{
        m_sht = m_wb.getSheet("障害内容一覧");
        Row a_row = null;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";

        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            String a_sVal = "";
            int a_iVal = 0;
            String a_sRet = "";
            
            //期間
            a_sVal = h_splitMonth[0].replace("/", "-").substring(0,10) + " ～ " + h_splitMonth[1].replace("/", "-").substring(0,10);
            a_row = m_sht.getRow(2);
            _setCellValueStringExist(a_row, 0, "期間　 " + a_sVal, null);

            //OperationLogより集計期間内における該当UserCodeの診断失敗情報(S001以外)を取得する
            //障害名称はRasCodeをキーにRasPingTrbInfoテーブルより障害名称(FltName)を取得する
            CellStyle a_style = m_wb.createCellStyle();
            a_style.setBorderTop(CellStyle.BORDER_THIN);
            a_style.setBorderBottom(CellStyle.BORDER_THIN);
            a_style.setBorderLeft(CellStyle.BORDER_THIN);
            a_style.setBorderRight(CellStyle.BORDER_THIN);
            a_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                
            String a_sYM = h_splitMonth[0].substring(0,8);

            //障害発生件数の取得
            //[2016.06.07]SQL文現地対応
            a_sql = "SELECT *";
            a_sql += ", TO_CHAR(INITIATIONTIME,'YYYY-MM-DD HH24:MI:SS') AS INITIATIONTIME2";
//            a_sql += ", (SELECT USERNODENAME FROM USERNODE WHERE (USERCODE=OPERATIONLOG.USERCODE) AND (USERIPADDR=OPERATIONLOG.USERIPADDR)) AS USERNODENAME";
//            a_sql += ", (SELECT FLTNAME FROM RASPINGTRBINFO WHERE (RASCODE=OPERATIONLOG.RASCODE)) AS FLTNAME";
            a_sql += " FROM TROUBLEINFORMATION WHERE ";
            a_sql += "(USERNAME=(SELECT USERNAME FROM NEWCUSTOMERMANAGE WHERE (USERCODE='" + h_splitUser[0] + "')))";
            a_sql += " AND (TO_CHAR(INITIATIONTIME,'YYYY/MM/DD HH24:MI:SS') BETWEEN '"+ h_splitMonth[0].substring(0,10) + " 00:00:00' AND '"+ h_splitMonth[1].substring(0,10) + " 23:59:59')";
//            a_sql += " AND (DATETIME>='" + h_splitMonth[0].substring(0,10) + " 00:00:00')";
//            a_sql += " AND (DATETIME<='" + h_splitMonth[1].substring(0,10) + " 23:59:59')";
            a_sql += "AND (TROUBLELEVEL IN (1,2,7))";
            a_sql += " ORDER BY INITIATIONTIME";
            a_sql += ";";
            //_Environ._MyLogger.severe("[_setReport_ShougaiNaiyo_ip]" + a_sql);
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            
            int a_i = 0;
            while(a_rs.next()){
                a_sRet = "";

                a_row = m_sht.createRow(7 + a_i);   //[2016.03.04]
                for (int a_j=0; a_j<=5; a_j++){
                    _setCellValueStringNew(a_row, a_j, "", a_style);
                }

                //発生日時
                a_sVal = _Environ.ExistDBString(a_rs,"INITIATIONTIME2");  //[2016.06.07]
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 0, a_sVal, null);

                //監視装置名
                a_sVal = _Environ.ExistDBString(a_rs,"IDCODE");
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 1, a_sVal, null);
                
                //失敗内容
                a_sVal = _Environ.ExistDBString(a_rs,"MESSAGE1");
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 2, a_sVal, null);
                
                //要因
                /*
                a_sVal = _Environ.ExistDBString(a_rs,"CAUSE");
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 3, a_sVal, null);
                */
                //処置
                /*
                a_sVal = _Environ.ExistDBString(a_rs,"ACTION");
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 4, a_sVal, null);
                */
                //記事
                /*
                a_sVal = _Environ.ExistDBString(a_rs,"COMMENTARY");
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 5, a_sVal, null);
                */
                a_i++;
            }
            a_rs.close();
            a_ps.close();
            
            _setCellActivate(0,0);
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[_setReport_ShougaiNaiyo_ip]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[_setReport_ShougaiNaiyo_ip]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        _Environ._MyLogger.info("*** _setReport_ShougaiNaiyo_ip is finished. ***");
    }
    
    //未使用
    private void _setReportInfo_List(){
        CellStyle a_style = m_wb.createCellStyle();
        a_style.setBorderTop(CellStyle.BORDER_THIN);
        a_style.setBorderBottom(CellStyle.BORDER_THIN);
        a_style.setBorderLeft(CellStyle.BORDER_THIN);
        a_style.setBorderRight(CellStyle.BORDER_THIN);
        a_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        m_sht = m_wb.getSheet("障害内容一覧");
        Row a_row = null;
        //期間
        a_row = m_sht.getRow(2);
        _setCellValueStringExist(a_row, 0, "期間　 2015-06-01 ～ 2015-06-30", null);
        //装置名
        for (int a_i=7; a_i<=17; a_i++){
            //行を作成
            a_row = m_sht.createRow(a_i);
            a_row.setHeightInPoints(25.5f); //高さ
            
            //発生日時
            _setCellValueStringNew(a_row, 0, "2015-04-16 09:50:53", a_style);
            //監視装置名
            _setCellValueStringNew(a_row, 1, "L3SW-B2-01 ", a_style);
            //失敗内容
            _setCellValueStringNew(a_row, 2, "PING失敗", a_style);
            //要因⇒自動出力不要[2015.07.24]OWT
            //_setCellValueStringNew(a_row, 3, "現地作業による", a_style);
            //処置⇒自動出力不要[2015.07.24]OWT
            //_setCellValueStringNew(a_row, 4, "処置", a_style);
            //記事⇒自動出力不要[2015.07.24]OWT
            //_setCellValueStringNew(a_row, 5, "記事", a_style);
        }
    }
    
    //MSSV2
    private void _makeReport_MSSV2(
        ArrayList<String> h_arrayUser,
        ArrayList<String> h_arrayMonth
        ) throws Exception{
        int a_iSum = 0;
        
        FileInputStream a_in = null;
        FileOutputStream a_os = null;

        for (int a_iCnt=0; a_iCnt<h_arrayUser.size(); a_iCnt++){
            String[] a_splitUser = h_arrayUser.get(a_iCnt).split("\t");

            for (int a_iCnt2=0; a_iCnt2<h_arrayMonth.size(); a_iCnt2++){
                String[] a_splitMonth = h_arrayMonth.get(a_iCnt2).split("\t");

                //定期診断以外
                //Excelテンプレートファイルのコピー
                _copyToTmpFile("template_report_monthly_mssv2", "mssv2", a_splitUser[0], a_splitMonth[0]);

                File a_f = null;    //[2016.03.04]
                try{
                    //Excel
                    a_f = new File(Response);   //[2016.03.04]

                    //ワークブックを開く
                    a_in = new FileInputStream(a_f);
                    m_wb = WorkbookFactory.create(a_in);
                    m_evaluator = m_wb.getCreationHelper().createFormulaEvaluator();
                    a_in.close();   //ここで入力ストリームを閉じる
                    a_f.delete();   //一時ファイルを削除

                    //Cover[2016.06.10]
                    //_setReport_Cover_com(a_splitUser, a_splitMonth);
                    _setReport_Cover_mssv2(a_splitUser, a_splitMonth);

                    //障害件数
                    _setReport_ShougaiKensu_mssv2(a_splitUser, a_splitMonth);

                    //障害内容
                    _setReport_ShougaiNaiyo_mssv2(a_splitUser, a_splitMonth);

                    //再計算
                    //m_evaluator.evaluateAll();
                    //編集内容を保存
                    //a_os = new FileOutputStream(a_f);
                    //m_wb.write(a_os);
                    //m_wb.close();
                }catch (Exception ex){
                    _Environ._MyLogger.severe("[_makeReport_MSSV2]" + ex.getMessage());
                }finally{
                    //[2016.03.04]
                    if (m_wb != null){
                        //再計算
                        m_evaluator.evaluateAll();
                        //編集内容を保存
                        a_os = new FileOutputStream(a_f);
                        m_wb.write(a_os);
                        m_wb.close();
                    }
                    if (a_os != null){
                        a_os.close();
                    }
                    if (a_in != null){
                        a_in.close();
                    }
                }
            }
        }

        //定期診断
        for (int a_iCnt2=0; a_iCnt2<h_arrayMonth.size(); a_iCnt2++){
            String[] a_splitMonth = h_arrayMonth.get(a_iCnt2).split("\t");
            
            //Excelテンプレートファイルのコピー
            _copyToTmpFile("template_report_monthly_mssv2_shindan", "mssv2_shindan", "", a_splitMonth[0]);

            File a_f = null;    //[2016.03.04]
            try{
                //Excel
                a_f = new File(Response);   //[2016.03.04]

                //ワークブックを開く
                a_in = new FileInputStream(a_f);
                m_wb = WorkbookFactory.create(a_in);
                m_evaluator = m_wb.getCreationHelper().createFormulaEvaluator();
                a_in.close();   //ここで入力ストリームを閉じる
                a_f.delete();   //一時ファイルを削除

                //Cover
                //_setReport_Cover_com(null, a_splitMonth);
                _setReport_Cover_mssv2_shindan(null, a_splitMonth);

                //定期診断結果
                _setReport_ShindanKekka_mssv2(h_arrayUser, a_splitMonth);

                //定期診断詳細
                _setReport_ShindanSyosai_mssv2(h_arrayUser, a_splitMonth);

                //再計算
                //m_evaluator.evaluateAll();
                //編集内容を保存
                //a_os = new FileOutputStream(a_f);
                //m_wb.write(a_os);
                //m_wb.close();
            }catch (Exception ex){
                _Environ._MyLogger.severe("[_makeReport_MSSV2]" + ex.getMessage());
            }finally{
                //[2016.03.04]
                if (m_wb != null){
                    //再計算
                    m_evaluator.evaluateAll();
                    //編集内容を保存
                    a_os = new FileOutputStream(a_f);
                    m_wb.write(a_os);
                    m_wb.close();
                }
                if (a_os != null){
                    a_os.close();
                }
                if (a_in != null){
                    a_in.close();
                }
            }
        }
        
        _Environ._MyLogger.info("*** _makeReport_MSSV2 is finished. ***");
    }
    
    //MSSV2：表紙
    private void _setReport_Cover_mssv2(
        String[] h_splitUser,
        String[] h_splitMonth
        ){
        String a_sTmp = "";
        
        m_sht = m_wb.getSheet("表紙");
        Row a_row = null;
        Cell a_cell = null;
        
        //yyyy年mm月分
        try{
            Date  a_date = DateFormat.getDateInstance().parse(h_splitMonth[0]);
            a_sTmp = new SimpleDateFormat("(yyyy年MM月分)").format(a_date);
        } catch (Exception e) {
            _Environ._MyLogger.severe("[_setReport_Cover]" + e.getMessage());
        }
        a_row = m_sht.getRow(5);    //[2016.06.10]9⇒5
        _setCellValueStringExist(a_row, 0, a_sTmp, null);   //[2016.06.10]2⇒0
        
        //お客様名
        a_row = m_sht.getRow(7);   //[2016.06.10]11⇒7
        _setCellValueStringExist(a_row, 0, "お客様名：" + h_splitUser[1] + "　様", null);   //[2016.06.10]2⇒0

        //出力日
        try{
            java.util.Date a_date = new java.util.Date();
            a_sTmp = new SimpleDateFormat("yyyy年 MM月 dd日").format(a_date);
        } catch (Exception e) {
            _Environ._MyLogger.severe("[_setReport_Cover]" + e.getMessage());
        }
        a_row = m_sht.getRow(14);   //[2016.06.10]18⇒14
        _setCellValueStringExist(a_row, 3, a_sTmp, null);   //[2016.06.10]5⇒3
        
        //承認[2016.06.10]
        a_sTmp = a_sTmp.replace("年", ".").replace("月", ".").replace("日", "").replace(" ", "");
        a_row = m_sht.getRow(21);   //[2016.06.10]18⇒14
        _setCellValueStringExist(a_row, 3, a_sTmp, null);
        _setCellValueStringExist(a_row, 4, a_sTmp, null);
        _setCellValueStringExist(a_row, 5, a_sTmp, null);
        
        _setCellActivate(0,0);
    }

    //MSSV2：表紙
    private void _setReport_Cover_mssv2_shindan(
        String[] h_splitUser,
        String[] h_splitMonth
        ){
        String a_sTmp = "";
        
        m_sht = m_wb.getSheet("表紙");
        Row a_row = null;
        Cell a_cell = null;
        
        //yyyy年mm月分
        try{
            Date  a_date = DateFormat.getDateInstance().parse(h_splitMonth[0]);
            a_sTmp = new SimpleDateFormat("(yyyy年MM月分)").format(a_date);
        } catch (Exception e) {
            _Environ._MyLogger.severe("[_setReport_Cover]" + e.getMessage());
        }
        a_row = m_sht.getRow(9);
        _setCellValueStringExist(a_row, 2, a_sTmp, null);
        
        //お客様名
        //a_row = m_sht.getRow(11);
        //_setCellValueStringExist(a_row, 2, "お客様名：" + h_splitUser[1] + "　様", null);

        //出力日
        try{
            java.util.Date a_date = new java.util.Date();
            a_sTmp = new SimpleDateFormat("yyyy年 MM月 dd日").format(a_date);
        } catch (Exception e) {
            _Environ._MyLogger.severe("[_setReport_Cover]" + e.getMessage());
        }
        a_row = m_sht.getRow(18);
        _setCellValueStringExist(a_row, 5, a_sTmp, null);

        _setCellActivate(0,0);
    }
    
    //MSSV2;定期診断結果
    private void _setReport_ShindanKekka_mssv2(
        ArrayList<String> h_splitUser,
        String[] h_splitMonth
        ) throws Exception{
        m_sht = m_wb.getSheet("通信障害集計");
        Row a_row = null;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";
        String a_sShindanTime = "";

        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            String a_sVal = "";
            int a_iVal = 0;
            String a_sRet = "";

            //期間
            //a_sVal = h_splitMonth[0].replace("/", "-").substring(0,10) + " ～ " + h_splitMonth[1].replace("/", "-").substring(0,10);
            a_row = m_sht.getRow(1);
            _setCellValueStringExist(a_row, 0, "開始日：" + h_splitMonth[0].replace("/", "-").substring(0,10), null);
            _setCellValueStringExist(a_row, 1, "終了日：" + h_splitMonth[1].replace("/", "-").substring(0,10), null);

            //日付の設定
            int a_iDay1 = Integer.valueOf(h_splitMonth[0].substring(8,10));
            int a_iDay2 = Integer.valueOf(h_splitMonth[1].substring(8,10));
            for (int a_i=1; a_i<=31; a_i++){
                if ((a_i < a_iDay1) || (a_i > a_iDay2)){
                    a_row = m_sht.getRow(2);
                    _setCellValueStringExist(a_row, a_i, "", null);
                }
            }
            
            //お客様名
            if (h_splitUser != null){
                CellStyle a_style = m_wb.createCellStyle();
                a_style.setBorderTop(CellStyle.BORDER_THIN);
                a_style.setBorderBottom(CellStyle.BORDER_THIN);
                a_style.setBorderLeft(CellStyle.BORDER_THIN);
                a_style.setBorderRight(CellStyle.BORDER_THIN);
                a_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                //[2016.06.10]
                CellStyle a_style2 = a_style;
                a_style2.setAlignment(CellStyle.ALIGN_CENTER);
                
                for (int a_i=0; a_i<h_splitUser.size(); a_i++){
                    String[] a_split = h_splitUser.get(a_i).split("\t");
                    a_row = m_sht.createRow(4 + a_i);   //[2016.03.04]
                    _setCellValueStringNew(a_row, 0, a_split[1], a_style);    
                    
                    //日付の設定（記事欄を含む）
                    for (int a_j=1; a_j<=32; a_j++){
                        _setCellValueStringNew(a_row, a_j, "", a_style);
                        if ((a_j >= a_iDay1) && (a_j <= a_iDay2)){
                            _setCellValueStringExist(a_row, a_j, "－", a_style2);   //[2016.06.10]
                        }
                    }
                    
                    String a_sYM = h_splitMonth[0].substring(0,8);

                    //合計[2016.06.10]
                    int a_iGokei = 0;   

                    //診断結果の取得
                    //[2016.06.07]SQL文現地対応
                    for (int a_j=a_iDay1; a_j<=a_iDay2; a_j++){
                        a_sql = "SELECT COUNT(NUM) AS REC_NUM";
                        a_sql += " FROM MSS2OPERATIONLOG WHERE ";
                        a_sql += "(USERCODE='" + a_split[0] + "')";
                        //a_sql += " AND (USERIPADDR='" + a_split[1] + "')";    //[2016.06.10]
                        a_sql += " AND (TO_CHAR(DATETIME,'YYYY/MM/DD HH24:MI:SS') BETWEEN '" +
                                a_sYM + String.format("%02d", a_j) + " 00:00:00' AND '" +
                                a_sYM + String.format("%02d", a_j) + " 23:59:59')";
//                        a_sql += " AND (DATETIME>='" + a_sYM + String.valueOf(a_j) + " 00:00:00')";
//                        a_sql += " AND (DATETIME<='" + a_sYM + String.valueOf(a_j) + " 23:59:59')";
                        //a_sql += " ORDER BY DATETIME";
                        //FFEA　ELOG取得OK(Trap)
                        //FFF4　ELOG取得OK(定期診断)
                        //FFE9　ELOG取得NG(Trap)
                        //FFF3　ELOG取得NG(定期診断)
                        a_sql += " AND (RASCODE IN ('R676','R678','T10060'))";    //[2016.06.09]
                        a_sql += " AND (DIAGMETHOD='1')";    //[2016.06.09]
                        a_sql += ";";
                        a_ps = a_con.prepareStatement(a_sql);
                        a_rs = a_ps.executeQuery();
                        a_iVal = 0; //[2016.03.04]
                        while(a_rs.next()){
                            a_sRet = "";

                            //診断結果
                            /* //[2016.06.10]
                            a_sVal = _Environ.ExistDBString(a_rs,"RASCODE");
                            //[2016.06.08]
                            if ((a_sVal.equals("FFEA") == false) && (a_sVal.equals("FFF4") == false)){
                                a_iVal++;
                            }
                            */
                            //a_iVal++;   //[2016.06.10]
                            a_iVal = _Environ.ExistDBInt(a_rs,"REC_NUM");
                            if (a_iVal > 0){
                                _setCellValueIntExist(a_row, a_j, a_iVal, a_style2);    //[2016.06.10]
                            }
                            //合計[2016.06.10]
                            a_iGokei += a_iVal;
                        }
                        a_rs.close();
                        a_ps.close();
                    }
                    //合計[2016.06.10]
                    _setCellValueStringExist(a_row, 32, String.valueOf(a_iGokei), a_style2);    //[2016.06.10]
                }
            }

            _setCellActivate(0,0);
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[_setReport_ShindanKekka_mssv2]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[_setReport_ShindanKekka_mssv2]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        _Environ._MyLogger.info("*** _setReport_ShindanKekka_mssv2 is finished. ***");
     }
    
    //MSSV2;障害集計
    private void _setReport_ShougaiKensu_mssv2(
        String[] h_splitUser,
        String[] h_splitMonth
        ) throws Exception{
        m_sht = m_wb.getSheet("障害集計");
        Row a_row = null;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";

        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            String a_sVal = "";
            int a_iVal = 0;
            String a_sRet = "";
            String a_sShindanTime = "";

            //以下の値はどこから持ってくるのか？
            //・監視装置名
            //・監視装置バージョン
            
            a_sql = "SELECT NODETYPE,VERSION FROM MSS2REMOTECUSTOMER WHERE ";
            a_sql += "(USERCODE='" + h_splitUser[0] + "')";
            a_sql += ";";
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                a_sRet = "";
 
                //監視装置名
                a_sVal = _Environ.ExistDBString(a_rs,"NODETYPE");
                a_row = m_sht.getRow(2);
                if (a_sVal.length() > 0){
                    //[2016.06.10]
                    _setCellValueStringExist(a_row, 0, "監視装置名：" + _Environ.ToNodeTypeString(Integer.valueOf(a_sVal)), null);
                }
                //監視装置バージョン
                a_sVal = _Environ.ExistDBString(a_rs,"VERSION");
                a_row = m_sht.getRow(3);
                //[2016.06.10]
                _setCellValueStringExist(a_row, 0, "監視装置バージョン：" + a_sVal, null);
            }
            a_rs.close();
            a_ps.close();
            
            //期間
            //a_sVal = h_splitMonth[0].replace("/", "-").substring(0,10) + " ～ " + h_splitMonth[1].replace("/", "-").substring(0,10);
            a_row = m_sht.getRow(5);
            //[2016.06.10]
            _setCellValueStringExist(a_row, 0, "開始日：" + h_splitMonth[0].substring(0,10), null);
            _setCellValueStringExist(a_row, 3, "終了日：" + h_splitMonth[1].substring(0,10), null);
            //_setCellValueStringExist(a_row, 0, "開始日：" + h_splitMonth[0].replace("/", "-").substring(0,10), null);
            //_setCellValueStringExist(a_row, 3, "終了日：" + h_splitMonth[1].replace("/", "-").substring(0,10), null);

            //日付の設定
            int a_iDay1 = Integer.valueOf(h_splitMonth[0].substring(8,10));
            int a_iDay2 = Integer.valueOf(h_splitMonth[1].substring(8,10));
            for (int a_i=1; a_i<=31; a_i++){
                if ((a_i < a_iDay1) || (a_i > a_iDay2)){
                    a_row = m_sht.getRow(6);
                    _setCellValueStringExist(a_row, a_i, "", null);
                }
            }
            
            //PBXLogよりUserCodeをキーに日毎に障害発生件数をカウント
            CellStyle a_style = m_wb.createCellStyle();
            a_style.setBorderTop(CellStyle.BORDER_THIN);
            a_style.setBorderBottom(CellStyle.BORDER_THIN);
            a_style.setBorderLeft(CellStyle.BORDER_THIN);
            a_style.setBorderRight(CellStyle.BORDER_THIN);
            a_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            //[2016.06.10]
            CellStyle a_style2 = a_style;
            a_style2.setAlignment(CellStyle.ALIGN_CENTER);

            //日付の設定（合計欄を含む）
            for (int a_j=1; a_j<=32; a_j++){
                a_row = m_sht.getRow(7);    //[2016.03.04]
                _setCellValueStringNew(a_row, a_j, "", a_style);
                if ((a_j >= a_iDay1) && (a_j <= a_iDay2)){
                    _setCellValueStringExist(a_row, a_j, "－", a_style2);   //[2016.06.10]
                }
            }

            String a_sYM = h_splitMonth[0].substring(0,8);

            //合計[2016.06.10]
            int a_iGokei = 0;   

            //障害発生件数の取得
            //[2016.06.07]SQL文現地対応
            for (int a_j=a_iDay1; a_j<=a_iDay2; a_j++){
                a_sql = "SELECT COUNT(NUM) AS REC_NUM FROM MSS2IPSTAGELOG WHERE ";
                a_sql += "(USERCODE='" + h_splitUser[0] + "')";
                a_sql += " AND (TO_CHAR(DATETIME,'YYYY/MM/DD HH24:MI:SS') BETWEEN '" +
                        a_sYM + String.format("%02d", a_j) + " 00:00:00' AND '" +
                        a_sYM + String.format("%02d", a_j) + " 23:59:59')";
                a_sql += " AND (REPORT='1')";   //[2016.06.15]
                /* [2016.06.10]
                a_sql = "SELECT COUNT(USERCODE) AS REC_NUM FROM MSS2OPERATIONLOG WHERE ";
                a_sql += "(USERCODE='" + h_splitUser[0] + "')";
                a_sql += " AND (TO_CHAR(DATETIME,'YYYY/MM/DD HH24:MI:SS') BETWEEN '" +
                        a_sYM + String.format("%02d", a_j) + " 00:00:00' AND '" +
                        a_sYM + String.format("%02d", a_j) + " 23:59:59')";
//                a_sql += " AND (DATETIME>='" + a_sYM + String.valueOf(a_j) + " 00:00:00')";
//                a_sql += " AND (DATETIME<='" + a_sYM + String.valueOf(a_j) + " 23:59:59')";
                //FFEA　ELOG取得OK(Trap)
                //FFF4　ELOG取得OK(定期診断)
                //FFE9　ELOG取得NG(Trap)
                //FFF3　ELOG取得NG(定期診断)
                a_sql += " AND (RASCODE IN ('FFE9','FFF3'))";    //[2016.06.08]
                */
                a_sql += ";";
                a_ps = a_con.prepareStatement(a_sql);
                a_rs = a_ps.executeQuery();
                while(a_rs.next()){
                    a_sRet = "";

                    //件数
                    a_iVal = _Environ.ExistDBInt(a_rs,"REC_NUM");
                    if (a_iVal > 0){
                        a_row = m_sht.getRow(7);    //[2016.03.04]
                        //a_row = m_sht.getRow(11 + a_i);
                        //[2016.06.10]
                        _setCellValueStringExist(a_row, a_j, String.valueOf(a_iVal), a_style2); //[2016.06.10]
                        //_setCellValueIntExist(a_row, a_j, a_iVal, null);
                    }
                    //合計[2016.06.10]
                    a_iGokei += a_iVal;
                }
                a_rs.close();
                a_ps.close();                        
            }
            //合計[2016.06.10]
            _setCellValueStringExist(a_row, 32, String.valueOf(a_iGokei), a_style2);    //[2016.06.10]
            
            _setCellActivate(0,0);
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[_setReport_ShougaiKensu_mssv2]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[_setReport_ShougaiKensu_mssv2]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        _Environ._MyLogger.info("*** _setReport_ShougaiKensu_mssv2 is finished. ***");
    } 
    
    //MSSV2;定期診断詳細
    private void _setReport_ShindanSyosai_mssv2(
        ArrayList<String> h_splitUser,
        String[] h_splitMonth
        ) throws Exception{
        m_sht = m_wb.getSheet("通信障害詳細");
        Row a_row = null;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";

        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            String a_sVal = "";
            int a_iVal = 0;
            String a_sRet = "";
            
            //期間
            //a_sVal = h_splitMonth[0].replace("/", "-").substring(0,10) + " ～ " + h_splitMonth[1].replace("/", "-").substring(0,10);
            a_row = m_sht.getRow(1);
            _setCellValueStringExist(a_row, 0, "開始日：" + h_splitMonth[0].replace("/", "-").substring(0,10), null);
            _setCellValueStringExist(a_row, 1, "終了日：" + h_splitMonth[1].replace("/", "-").substring(0,10), null);

            //OperationLogより集計期間内における該当UserCodeの診断失敗情報(S001以外)を取得する
            //障害名称はRasCodeをキーにRasPingTrbInfoテーブルより障害名称(FltName)を取得する
            CellStyle a_style = m_wb.createCellStyle();
            a_style.setBorderTop(CellStyle.BORDER_THIN);
            a_style.setBorderBottom(CellStyle.BORDER_THIN);
            a_style.setBorderLeft(CellStyle.BORDER_THIN);
            a_style.setBorderRight(CellStyle.BORDER_THIN);
            a_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                
            String a_sYM = h_splitMonth[0].substring(0,8);

            //障害発生件数の取得
            //[2016.06.07]SQL文現地対応
            a_sql = "SELECT *";
            a_sql += ", TO_CHAR(DATETIME,'YYYY-MM-DD HH24:MI:SS') AS DATETIME2";
            a_sql += ", (SELECT USERNAME FROM NEWCUSTOMERMANAGE WHERE (USERCODE=MSS2OPERATIONLOG.USERCODE)) AS USERNAME";
            a_sql += ", (SELECT FLTNAME FROM RASPINGTRBINFO WHERE (RASCODE=MSS2OPERATIONLOG.RASCODE)) AS FLTNAME";
            a_sql += " FROM MSS2OPERATIONLOG WHERE ";
            //ユーザの条件
            String a_sql_user = "";
            for (int a_iCnt=0; a_iCnt<h_splitUser.size(); a_iCnt++){
                String[] a_tmp = h_splitUser.get(a_iCnt).split("\t");
                if (a_iCnt>0){
                    a_sql_user += ",";
                }
                a_sql_user += "'" + a_tmp[0] + "'";
            }
            a_sql += " (USERCODE IN (" + a_sql_user + "))";
            a_sql += " AND (TO_CHAR(DATETIME,'YYYY/MM/DD HH24:MI:SS') BETWEEN '"+ h_splitMonth[0].substring(0,10) + " 00:00:00' AND '"+ h_splitMonth[1].substring(0,10) + " 23:59:59')";
//            a_sql += " AND (DATETIME>='" + h_splitMonth[0].substring(0,10) + " 00:00:00')";
//            a_sql += " AND (DATETIME<='" + h_splitMonth[1].substring(0,10) + " 23:59:59')";
            //FFEA　ELOG取得OK(Trap)
            //FFF4　ELOG取得OK(定期診断)
            //FFE9　ELOG取得NG(Trap)
            //FFF3　ELOG取得NG(定期診断)
            a_sql += " AND (RASCODE IN ('R676','R678','T10060'))";    //[2016.06.09]
            a_sql += " AND (DIAGMETHOD='1')";    //[2016.06.09]
            a_sql += " ORDER BY DATETIME";
            a_sql += ";";
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            
            int a_i = 0;
            while(a_rs.next()){
                a_sRet = "";

                a_row = m_sht.createRow(3 + a_i);   //[2016.03.04]
                for (int a_j=0; a_j<=6; a_j++){
                    _setCellValueStringNew(a_row, a_j, "", a_style);
                }

                //発生日時
                a_sVal = _Environ.ExistDBString(a_rs,"DATETIME2");  //[2016.06.07]SQL文現地対応
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 0, a_sVal, null);

                //お客様名
                a_sVal = _Environ.ExistDBString(a_rs,"USERNAME");   //[2016.06.07]SQL文現地対応
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 1, a_sVal, null);
                
                //使用ポート
                a_sVal = _Environ.ExistDBString(a_rs,"PORTNO");
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 2, a_sVal, null);
                
                //メッセージ内容
                a_sVal = _Environ.ExistDBString(a_rs,"FLTNAME");
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 3, a_sVal, null);
                
                //要因
                
                //処置
            
                //記事

                a_i++;
            }
            a_rs.close();
            a_ps.close();                        

            _setCellActivate(0,0);
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[_setReport_ShindanSyosai_mssv2]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[_setReport_ShindanSyosai_mssv2]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        _Environ._MyLogger.info("*** _setReport_ShindanSyosai_mssv2 is finished. ***");
    }
    
    //MSSV2;障害一覧
    private void _setReport_ShougaiNaiyo_mssv2(
        String[] h_splitUser,
        String[] h_splitMonth
        ) throws Exception{
        m_sht = m_wb.getSheet("障害詳細");
        Row a_row = null;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";

        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            String a_sVal = "";
            int a_iVal = 0;
            String a_sRet = "";
            
            //期間
            //a_sVal = h_splitMonth[0].replace("/", "-").substring(0,10) + " ～ " + h_splitMonth[1].replace("/", "-").substring(0,10);
            a_row = m_sht.getRow(1);
            //[2016.06.10]
            _setCellValueStringExist(a_row, 0, "開始日：" + h_splitMonth[0].substring(0,10), null);
            _setCellValueStringExist(a_row, 1, "終了日：" + h_splitMonth[1].substring(0,10), null);
            //_setCellValueStringExist(a_row, 0, "開始日：" + h_splitMonth[0].replace("/", "-").substring(0,10), null);
            //_setCellValueStringExist(a_row, 1, "終了日：" + h_splitMonth[1].replace("/", "-").substring(0,10), null);

            //集計期間内におけるPBXLogテーブルのMSG1+MSG2+MSG2
            CellStyle a_style = m_wb.createCellStyle();
            a_style.setBorderTop(CellStyle.BORDER_THIN);
            a_style.setBorderBottom(CellStyle.BORDER_THIN);
            a_style.setBorderLeft(CellStyle.BORDER_THIN);
            a_style.setBorderRight(CellStyle.BORDER_THIN);
            a_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                
            String a_sYM = h_splitMonth[0].substring(0,8);

            //障害発生件数の取得
            //[2016.06.07]SQL文現地対応
            a_sql = "SELECT *";
            a_sql += ", TO_CHAR(DATETIME,'YYYY-MM-DD HH24:MI:SS') AS DATETIME2";
            a_sql += ", (SELECT FLTNAME FROM IPSTAGEV5TRBINFO WHERE (FLTCODE=MSS2IPSTAGELOG.FLTCODE)) AS FLTNAME";
            a_sql += " FROM MSS2IPSTAGELOG WHERE ";
            a_sql += "(USERCODE='" + h_splitUser[0] + "')";
            a_sql += " AND (TO_CHAR(DATETIME,'YYYY/MM/DD HH24:MI:SS') BETWEEN '"+ h_splitMonth[0].substring(0,10) + " 00:00:00' AND '" + h_splitMonth[1].substring(0,10) + " 23:59:59')";
            a_sql += " AND (REPORT='1')";   //[2016.06.15]
            a_sql += " ORDER BY DATETIME";
            /* [2016.06.10]
            a_sql = "SELECT *";
            a_sql += ", TO_CHAR(DATETIME,'YYYY-MM-DD HH24:MI:SS') AS DATETIME2";
            a_sql += ", (SELECT FLTNAME FROM RASPINGTRBINFO WHERE (RASCODE=MSS2OPERATIONLOG.RASCODE)) AS FLTNAME";
            a_sql += " FROM MSS2OPERATIONLOG WHERE ";
            a_sql += "(USERCODE='" + h_splitUser[0] + "')";
            a_sql += " AND (TO_CHAR(DATETIME,'YYYY/MM/DD HH24:MI:SS') BETWEEN '"+ h_splitMonth[0].substring(0,10) + " 00:00:00' AND '"+ h_splitMonth[1].substring(0,10) + " 23:59:59')";
//            a_sql += " AND (DATETIME>='" + h_splitMonth[0].substring(0,10) + " 00:00:00')";
//            a_sql += " AND (DATETIME<='" + h_splitMonth[1].substring(0,10) + " 23:59:59')";
            //FFEA　ELOG取得OK(Trap)
            //FFF4　ELOG取得OK(定期診断)
            //FFE9　ELOG取得NG(Trap)
            //FFF3　ELOG取得NG(定期診断)
            a_sql += " AND (RASCODE IN ('FFE9','FFF3'))";    //[2016.06.08]
            a_sql += " ORDER BY DATETIME";
            */
            a_sql += ";";
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            
            int a_i = 0;
            while(a_rs.next()){
                a_sRet = "";

                a_row = m_sht.createRow(3 + a_i);   //[2016.03.04]
                for (int a_j=0; a_j<=4; a_j++){
                    _setCellValueStringNew(a_row, a_j, "", a_style);
                }

                //発生日時
                //a_sVal = _Environ.ExistDBString(a_rs,"INITIATIONTIME2");  //[2016.06.07]SQL文現地対応
                a_sVal = _Environ.ExistDBString(a_rs,"DATETIME2");  //[2016.06.07]SQL文現地対応
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 0, a_sVal, null);

                //メッセージ内容
                //a_sVal = _Environ.ExistDBString(a_rs,"MESSAGE1");
                a_sVal = _Environ.ExistDBString(a_rs,"FLTNAME");
                //a_sVal = _Environ.ExistDBString(a_rs,"MSG1");
                //a_sVal += _Environ.ExistDBString(a_rs,"MSG2");
                //a_sVal += _Environ.ExistDBString(a_rs,"MSG3");
                //a_row = m_sht.getRow(11 + a_i);   //[2016.03.04]
                _setCellValueStringExist(a_row, 1, a_sVal, null);
                
                //要因
                
                //処理
            
                //記事
                
                a_i++;
            }
            a_rs.close();
            a_ps.close();
            
            _setCellActivate(0,0);
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[_setReport_ShougaiNaiyo_mssv2]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[_setReport_ShougaiNaiyo_mssv2]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        _Environ._MyLogger.info("*** _setReport_ShougaiNaiyo_mssv2 is finished. ***");
    }

    //PBX
    private void _makeReport_PBX(
        ArrayList<String> h_arrayUser,
        ArrayList<String> h_arrayMonth
        ) throws Exception{
        int a_iSum = 0;
        
        FileInputStream a_in = null;
        FileOutputStream a_os = null;

        for (int a_iCnt=0; a_iCnt<h_arrayUser.size(); a_iCnt++){
            String[] a_splitUser = h_arrayUser.get(a_iCnt).split("\t");

            for (int a_iCnt2=0; a_iCnt2<h_arrayMonth.size(); a_iCnt2++){
                String[] a_splitMonth = h_arrayMonth.get(a_iCnt2).split("\t");

                //Excelテンプレートファイルのコピー
                _copyToTmpFile("template_report_monthly_pbx", "pbx", a_splitUser[0], a_splitMonth[0]);

                File a_f = null;    //[2016.03.04]
                try{
                    //Excel
                    a_f = new File(Response);   //[2016.03.04]

                    //ワークブックを開く
                    a_in = new FileInputStream(a_f);
                    m_wb = WorkbookFactory.create(a_in);
                    m_evaluator = m_wb.getCreationHelper().createFormulaEvaluator();
                    a_in.close();   //ここで入力ストリームを閉じる
                    a_f.delete();   //一時ファイルを削除

                    //Cover[2016.06.10]
                    //_setReport_Cover_com(a_splitUser, a_splitMonth);
                    //_setReport_Cover_pbx(a_splitUser, a_splitMonth);
                    _setReport_Cover_ip(a_splitUser, a_splitMonth);

                    //障害件数
                    _setReport_ShougaiKensu_pbx(a_splitUser, a_splitMonth);

                    //障害内容
                    _setReport_ShougaiNaiyo_pbx(a_splitUser, a_splitMonth);

                    //再計算
                    //m_evaluator.evaluateAll();
                    //編集内容を保存
                    //a_os = new FileOutputStream(a_f);
                    //m_wb.write(a_os);
                    //m_wb.close();
                }catch (Exception ex){
                    _Environ._MyLogger.severe("[_makeReport_PBX]" + ex.getMessage());
                }finally{
                    //[2016.03.04]
                    if (m_wb != null){
                        //再計算
                        m_evaluator.evaluateAll();
                        //編集内容を保存
                        a_os = new FileOutputStream(a_f);
                        m_wb.write(a_os);
                        m_wb.close();
                    }
                    if (a_os != null){
                        a_os.close();
                    }
                    if (a_in != null){
                        a_in.close();
                    }
                }
            }
        }

        _Environ._MyLogger.info("*** _makeReport_PBX is finished. ***");
    }
    
    //PBX：Cover
    private void _setReport_Cover_pbx(
        String[] h_splitUser,
        String[] h_splitMonth
        ) throws Exception{
    }

    //PBX：障害件数[2016.03.04]⇒未使用？
    private void _setReport_ShougaiKensu_pbx_new(
        String[] h_splitUser,
        String[] h_splitMonth
        ) throws Exception{
        m_sht = m_wb.getSheet("障害発生件数");
        Row a_row = null;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";

        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            String a_sVal = "";
            int a_iVal = 0;
            String a_sRet = "";
            String a_sShindanTime = "";
            a_sql = "SELECT REMOTECLASS FROM NEWCUSTOMERMANAGE WHERE ";
            a_sql += "(ID='" + h_splitUser[2] + "')";   //[2016.02.25]
            //a_sql += "(USERCODE='" + h_splitUser[0] + "')";
            a_sql += ";";
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                a_sRet = "";
 
                //リモート契約クラス
                a_sVal = _Environ.ExistDBString(a_rs,"REMOTECLASS");
                a_row = m_sht.getRow(2);
                _setCellValueStringExist(a_row, 0, "リモート契約クラス= " + a_sVal, null);
            }
            a_rs.close();
            a_ps.close();
            
            //期間
            a_sVal = h_splitMonth[0].replace("/", "-").substring(0,10) + " ～ " + h_splitMonth[1].replace("/", "-").substring(0,10);
            a_row = m_sht.getRow(5);
            _setCellValueStringExist(a_row, 0, "期間　 " + a_sVal, null);

            //日付の設定
            int a_iDay1 = Integer.valueOf(h_splitMonth[0].substring(8,10));
            int a_iDay2 = Integer.valueOf(h_splitMonth[1].substring(8,10));
            for (int a_i=1; a_i<=31; a_i++){
                if ((a_i < a_iDay1) || (a_i > a_iDay2)){
                    a_row = m_sht.getRow(9);
                    _setCellValueStringExist(a_row, a_i, "", null);
                }
            }
            
            //装置名
            ArrayList<String> a_arraySouchi = _getReport_SouchiList(h_splitUser);
            if (a_arraySouchi != null){
                CellStyle a_style = m_wb.createCellStyle();
                a_style.setBorderTop(CellStyle.BORDER_THIN);
                a_style.setBorderBottom(CellStyle.BORDER_THIN);
                a_style.setBorderLeft(CellStyle.BORDER_THIN);
                a_style.setBorderRight(CellStyle.BORDER_THIN);
                a_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                
                for (int a_i=0; a_i<a_arraySouchi.size(); a_i++){
                    String[] a_split = a_arraySouchi.get(a_i).split("\t");
                    a_row = m_sht.getRow(11 + a_i);
                    _setCellValueStringNew(a_row, 0, a_split[0], a_style);
                    
                    //合計
                    int a_iGokei = 0;   

                    //日付の設定（合計欄を含む）
                    for (int a_j=1; a_j<=32; a_j++){
                        _setCellValueStringNew(a_row, a_j, "", a_style);
                        if ((a_j >= a_iDay1) && (a_j <= a_iDay2)){
                            _setCellValueStringExist(a_row, a_j, "－", null);
                        }
                    }
                    
                    String a_sYM = h_splitMonth[0].substring(0,8);

                    //障害発生件数の取得
                    //[2016.06.07]SQL文現地対応
                    for (int a_j=a_iDay1; a_j<=a_iDay2; a_j++){
                        a_sql = "SELECT COUNT(IDCODE) AS REC_NUM FROM TROUBLEINFORMATION WHERE ";
                        a_sql += "(CUSTOMERID=" + h_splitUser[2] + ")";
                        a_sql += " AND (IDCODE='" + a_split[0] + "')";
                        a_sql += " AND (TO_CHAR(INITIATIONTIME,'YYYY/MM/DD HH24:MI:SS') BETWEEN '" + a_sYM + String.format("%02d", a_j) + " 00:00:00' AND '"+ a_sYM + String.format("%02d", a_j) + " 23:59:59')";
//                        a_sql += " AND (INITIATIONTIME>='" + a_sYM + String.valueOf(a_j) + " 00:00:00')";
//                        a_sql += " AND (INITIATIONTIME<='" + a_sYM + String.valueOf(a_j) + " 23:59:59')";
                        a_sql += ";";
                        a_ps = a_con.prepareStatement(a_sql);
                        a_rs = a_ps.executeQuery();
                        while(a_rs.next()){
                            a_sRet = "";

                            //件数
                            a_iVal = _Environ.ExistDBInt(a_rs,"REC_NUM");
                            if (a_iVal > 0){
                                a_row = m_sht.getRow(11 + a_i);
                                _setCellValueIntExist(a_row, a_j, a_iVal, null);
                            }
                            
                            //合計
                            a_iGokei += a_iVal;
                        }
                        a_rs.close();
                        a_ps.close();                        
                    }
                    
                    //合計
                    a_row = m_sht.getRow(11 + a_i);
                    _setCellValueIntExist(a_row, 32, a_iGokei, null);
                }
            }
            
            _setCellActivate(0,0);
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[_setReport_ShougaiKensu_pbx]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[_setReport_ShougaiKensu_pbx]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        _Environ._MyLogger.info("*** _setReport_ShougaiKensu_pbx is finished. ***");
    }
    
    //PBX：障害件数⇒未使用?[2016.03.04]
    private void _setReport_ShougaiKensu_pbx(
        String[] h_splitUser,
        String[] h_splitMonth
        ) throws Exception{
        m_sht = m_wb.getSheet("障害集計");
        Row a_row = null;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";

        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            String a_sVal = "";
            int a_iVal = 0;
            String a_sRet = "";
            String a_sShindanTime = "";

            //以下の値はどこから持ってくるのか？
            //・監視装置名
            //・監視装置バージョン
            
            /*
            a_sql = "SELECT REMOTECLASS FROM NEWCUSTOMERMANAGE WHERE ";
            a_sql += "(ID='" + h_splitUser[2] + "')";
            //a_sql += "(USERCODE='" + h_splitUser[0] + "')";
            a_sql += ";";
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                a_sRet = "";
 
                //リモート契約クラス
                a_sVal = _Environ.ExistDBString(a_rs,"REMOTECLASS");
                a_row = m_sht.getRow(2);
                _setCellValueStringExist(a_row, 0, "リモート契約クラス= " + a_sVal, null);
            }
            a_rs.close();
            a_ps.close();
            */
            
            //期間
            //a_sVal = h_splitMonth[0].replace("/", "-").substring(0,10) + " ～ " + h_splitMonth[1].replace("/", "-").substring(0,10);
            a_row = m_sht.getRow(5);
            _setCellValueStringExist(a_row, 0, "開始日：" + h_splitMonth[0].replace("/", "-").substring(0,10), null);
            _setCellValueStringExist(a_row, 3, "終了日：" + h_splitMonth[1].replace("/", "-").substring(0,10), null);

            //日付の設定
            int a_iDay1 = Integer.valueOf(h_splitMonth[0].substring(8,10));
            int a_iDay2 = Integer.valueOf(h_splitMonth[1].substring(8,10));
            for (int a_i=1; a_i<=31; a_i++){
                if ((a_i < a_iDay1) || (a_i > a_iDay2)){
                    a_row = m_sht.getRow(6);
                    _setCellValueStringExist(a_row, a_i, "", null);
                }
            }
            
            //PBXLogよりUserCodeをキーに日毎に障害発生件数をカウント
            CellStyle a_style = m_wb.createCellStyle();
            a_style.setBorderTop(CellStyle.BORDER_THIN);
            a_style.setBorderBottom(CellStyle.BORDER_THIN);
            a_style.setBorderLeft(CellStyle.BORDER_THIN);
            a_style.setBorderRight(CellStyle.BORDER_THIN);
            a_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            //[2016.06.10]
            CellStyle a_style2 = a_style;
            a_style2.setAlignment(CellStyle.ALIGN_CENTER);

            //日付の設定（合計欄を含む）
            for (int a_j=1; a_j<=32; a_j++){
                a_row = m_sht.getRow(7);
                _setCellValueStringNew(a_row, a_j, "", a_style);
                if ((a_j >= a_iDay1) && (a_j <= a_iDay2)){
                    _setCellValueStringExist(a_row, a_j, "－", a_style2);   //[2016.06.10]
                }
            }

            String a_sYM = h_splitMonth[0].substring(0,8);

            //障害発生件数の取得
            //[2016.06.07]SQL文現地対応
            for (int a_j=a_iDay1; a_j<=a_iDay2; a_j++){
                a_sql = "SELECT COUNT(USERCODE) AS REC_NUM FROM PBXLOG WHERE ";
                a_sql += "(USERCODE='" + h_splitUser[0] + "')";
                a_sql += " AND (TO_CHAR(DATETIME,'YYYY/MM/DD HH24:MI:SS') BETWEEN '"+
                        a_sYM + String.format("%02d", a_j) + " 00:00:00' AND '" +
                        a_sYM + String.format("%02d", a_j) + " 23:59:59')";
//                a_sql += " AND (DATETIME>='" + a_sYM + String.valueOf(a_j) + " 00:00:00')";
//                a_sql += " AND (DATETIME<='" + a_sYM + String.valueOf(a_j) + " 23:59:59')";
                a_sql += ";";
                a_ps = a_con.prepareStatement(a_sql);
                a_rs = a_ps.executeQuery();
                while(a_rs.next()){
                    a_sRet = "";

                    //件数
                    a_iVal = _Environ.ExistDBInt(a_rs,"REC_NUM");
                    if (a_iVal > 0){
                        a_row = m_sht.getRow(7);
                        //a_row = m_sht.getRow(11 + a_i);
                        //[2016.06.10]
                        //_setCellValueStringExist(a_row, a_j, String.valueOf(a_iVal), null);
                        _setCellValueIntExist(a_row, a_j, a_iVal, a_style2);
                    }
                }
                a_rs.close();
                a_ps.close();                        
            }
            
            _setCellActivate(0,0);

            m_wb.removeSheetAt(1);  //[2016.06.08]
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[_setReport_ShougaiKensu_pbx]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[_setReport_ShougaiKensu_pbx]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        _Environ._MyLogger.info("*** _setReport_ShougaiKensu_pbx is finished. ***");
    }

    //PBX：障害内容
    private void _setReport_ShougaiNaiyo_pbx(
        String[] h_splitUser,
        String[] h_splitMonth
        ) throws Exception{
        m_sht = m_wb.getSheet("障害詳細");
        Row a_row = null;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";

        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            String a_sVal = "";
            int a_iVal = 0;
            String a_sRet = "";
            
            //期間
            //a_sVal = h_splitMonth[0].replace("/", "-").substring(0,10) + " ～ " + h_splitMonth[1].replace("/", "-").substring(0,10);
            a_row = m_sht.getRow(1);
            _setCellValueStringExist(a_row, 0, "開始日：" + h_splitMonth[0].replace("/", "-").substring(0,10), null);
            _setCellValueStringExist(a_row, 1, "終了日：" + h_splitMonth[1].replace("/", "-").substring(0,10), null);

            //集計期間内におけるPBXLogテーブルのMSG1+MSG2+MSG2
            CellStyle a_style = m_wb.createCellStyle();
            a_style.setBorderTop(CellStyle.BORDER_THIN);
            a_style.setBorderBottom(CellStyle.BORDER_THIN);
            a_style.setBorderLeft(CellStyle.BORDER_THIN);
            a_style.setBorderRight(CellStyle.BORDER_THIN);
            a_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                
            String a_sYM = h_splitMonth[0].substring(0,8);

            //障害発生件数の取得
            //[2016.06.07]SQL文現地対応
            a_sql = "SELECT *";
            a_sql += ", TO_CHAR(DATETIME,'YYYY-MM-DD HH24:MI:SS') AS DATETIME2";
            a_sql += " FROM PBXLOG WHERE ";
            a_sql += "(USERCODE='" + h_splitUser[0] + "')";
            a_sql += " AND (TO_CHAR(DATETIME,'YYYY/MM/DD HH24:MI:SS') BETWEEN '"+ h_splitMonth[0].substring(0,10) + " 00:00:00' AND '"+ h_splitMonth[1].substring(0,10) + " 23:59:59')";
//            a_sql += " AND (DATETIME>='" + h_splitMonth[0].substring(0,10) + " 00:00:00')";
//            a_sql += " AND (DATETIME<='" + h_splitMonth[1].substring(0,10) + " 23:59:59')";
            a_sql += " ORDER BY DATETIME";
            a_sql += ";";
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            
            int a_i = 0;
            while(a_rs.next()){
                a_sRet = "";

                a_row = m_sht.createRow(3 + a_i);   //[2016.03.4]
                for (int a_j=0; a_j<=4; a_j++){
                    _setCellValueStringNew(a_row, a_j, "", a_style);
                }

                //発生日時
                a_sVal = _Environ.ExistDBString(a_rs,"DATETIME2");
                _setCellValueStringExist(a_row, 0, a_sVal, null);

                //メッセージ内容
                a_sVal = _Environ.ExistDBString(a_rs,"MSG1");
                a_sVal += _Environ.ExistDBString(a_rs,"MSG2");
                a_sVal += _Environ.ExistDBString(a_rs,"MSG3");
                _setCellValueStringExist(a_row, 1, a_sVal, null);
                
                //要因
                
                //処理
            
                a_i++;
            }
            a_rs.close();
            a_ps.close();
            
            _setCellActivate(0,0);
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[_setReport_ShougaiNaiyo_pbx]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[_setReport_ShougaiNaiyo_pbx]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        _Environ._MyLogger.info("*** _setReport_ShougaiNaiyo_pbx is finished. ***");
    }

    private void _setCellValueIntNew(
            Row h_rowObj,
            int h_colNum,
            int h_val,
            CellStyle h_style){
        Cell a_cell = null;

        a_cell = h_rowObj.createCell(h_colNum);
        a_cell.setCellValue(h_val);
        if (h_style != null)
            a_cell.setCellStyle(h_style);
    }

    private void _setCellValueStringNew(
            Row h_rowObj,
            int h_colNum,
            String h_val,
            CellStyle h_style){
        Cell a_cell = null;

        a_cell = h_rowObj.createCell(h_colNum);
        a_cell.setCellValue(h_val);
        if (h_style != null)
            a_cell.setCellStyle(h_style);
    }

    private void _setCellValueIntExist(
            Row h_rowObj,
            int h_colNum,
            int h_val,
            CellStyle h_style){
        Cell a_cell = null;

        a_cell = h_rowObj.getCell(h_colNum);
        a_cell.setCellValue(h_val);
        if (h_style != null)
            a_cell.setCellStyle(h_style);
    }

    private void _setCellValueStringExist(
            Row h_rowObj,
            int h_colNum,
            String h_val,
            CellStyle h_style){
        Cell a_cell = null;

        a_cell = h_rowObj.getCell(h_colNum);
        a_cell.setCellValue(h_val);
        if (h_style != null)
            a_cell.setCellStyle(h_style);
    }
    
    private void _setCellActivate(
        int h_rowNum,
        int h_colNum){
        Row a_row = null;
        Cell a_cell = null;

        //アクティブセルの指定
        try{
            a_row = m_sht.getRow(h_rowNum);
            a_cell = a_row.getCell(h_colNum);
            a_cell.setAsActiveCell();
        }catch (Exception e){
            
        }
    }
    
}
