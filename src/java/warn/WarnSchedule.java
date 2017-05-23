/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warn;

import common.Environ;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.Date;

/**
 *
 * @author Chappy
 */
@ManagedBean(name="WarnSchedule")
@RequestScoped
public class WarnSchedule implements Serializable {
    //private CheckAny _CheckAny = null;
    public Environ _Environ = null;
    
    public String _db_driver = "";
    public String _db_server = "";
    public String _db_url = "";
    public String _db_user = "";
    public String _db_pass = "";
    public String _db_name = "";
    private int _max_line_page = 0;

    private Timestamp _TimeS = null;
    private Timestamp _TimeE = null;
    private Integer _ExecDays = 0;
    private Integer _ExecKind = 0;
    
    private String JobId = "";
    private String CustomerName = "";
    private String BranchName = "";
    private String JobKind = "";
    private String JobContents = "";
    private String TimeS = "";
    private String TimeE = "";
    private String ExecDaysAll = "";
    private String ExecDaysSun = "";
    private String ExecDaysMon = "";
    private String ExecDaysTue = "";
    private String ExecDaysWed = "";
    private String ExecDaysThr = "";
    private String ExecDaysFri = "";
    private String ExecDaysSat = "";
    private String ExecKindSt = "";
    private String ExecKindEd = "";
    
    private String OrderStartTime = "";
    private String OrderStartDate = "";
    
    //--------------------------------------------------------------------------
    //JOBID
    public void putJobId(String h_val) {
        JobId = h_val;
    }
    public String getJobId() {
        return JobId;
    }
    //顧客名
    public void putCustomerName(String h_val) {
        CustomerName = h_val;
    }
    public String getCustomerName() {
        return CustomerName;
    }
    //支店名
    public void putBranchName(String h_val) {
        BranchName = h_val;
    }
    public String getBranchName() {
        return BranchName;
    }
    //作業種別
    public void putJobKind(String h_val) {
        JobKind = h_val;
    }
    public String getJobKind() {
        return JobKind;
    }
    //警告内容
    public void putJobContents(String h_val) {
        JobContents = h_val;
    }
    public String getJobContents() {
        return JobContents;
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
    
    //曜日
    public void putExecDaysAll(String h_val) {
        ExecDaysAll = h_val;
    }
    public String getExecDaysAll() {
        return ExecDaysAll;
    }
    public void putExecDaysSun(String h_val) {
        ExecDaysSun = h_val;
    }
    public String getExecDaysSun() {
        return ExecDaysSun;
    }
    public void putExecDaysMon(String h_val) {
        ExecDaysMon = h_val;
    }
    public String getExecDaysMon() {
        return ExecDaysMon;
    }
    public void putExecDaysTue(String h_val) {
        ExecDaysTue = h_val;
    }
    public String getExecDaysTue() {
        return ExecDaysTue;
    }
    public void putExecDaysWed(String h_val) {
        ExecDaysWed = h_val;
    }
    public String getExecDaysWed() {
        return ExecDaysWed;
    }
    public void putExecDaysThr(String h_val) {
        ExecDaysThr = h_val;
    }
    public String getExecDaysThr() {
        return ExecDaysThr;
    }
    public void putExecDaysFri(String h_val) {
        ExecDaysFri = h_val;
    }
    public String getExecDaysFri() {
        return ExecDaysFri;
    }
    public void putExecDaysSat(String h_val) {
        ExecDaysSat = h_val;
    }
    public String getExecDaysSat() {
        return ExecDaysSat;
    }

    //実行
    public void putExecKindSt(String h_val) {
        ExecKindSt = h_val;
    }
    public String getExecKindSt() {
        return ExecKindSt;
    }
    public void putExecKindEd(String h_val) {
        ExecKindEd = h_val;
    }
    public String getExecKindEd() {
        return ExecKindEd;
    }

    //並び順
    public void putOrderStartTime(String h_val) {
        OrderStartTime = h_val;
    }
    public String getOrderStartTime() {
        return OrderStartTime;
    }
    public void putOrderStartDate(String h_val) {
        OrderStartDate = h_val;
    }
    public String getOrderStartDate() {
        return OrderStartDate;
    }
    
    /**
     * Creates a new instance of SalesForceApi
     */
    public WarnSchedule() {
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
    
    //警告情報の登録
    public void EnteryWarnSchedule() throws Exception{
        /*
        System.out.println(JobId);
        System.out.println(CustomerName);
        System.out.println(BranchName);
        System.out.println(JobKind);
        System.out.println(JobContents);
        System.out.println(TimeS);
        System.out.println(TimeE);
        System.out.println(ExecDaysAll);
        System.out.println(ExecDaysSun);
        System.out.println(ExecDaysMon);
        System.out.println(ExecDaysTue);
        System.out.println(ExecDaysWed);
        System.out.println(ExecDaysThr);
        System.out.println(ExecDaysFri);
        System.out.println(ExecDaysSat);
        System.out.println(ExecKindSt);
        System.out.println(ExecKindEd);
        */
        set_ExecDays();
        set_ExecKind();
        chek_WarnStEd();
        
        if (JobId.length() > 0){
            update_WarnSchedule();
        }else{
            insert_WarnSchedule();
        }
        _Environ._MyLogger.info("*** EnteryWarnSchedule is finished. ***");
    }

    //警告情報の削除
    public void DeleteWarnSchedule() throws Exception{
        Connection a_con = null;
        PreparedStatement a_ps = null;
        String a_sql = "DELETE FROM JOBSCHEDULE WHERE (JOBID=?);";
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_con.setAutoCommit(false);
            a_ps = a_con.prepareStatement(a_sql);
            a_ps.setString(1, JobId);
            int a_i = a_ps.executeUpdate();
            a_con.commit();
        } catch (SQLException e) {
            if (a_con != null){
                a_con.rollback();
            }
            _Environ._MyLogger.severe("[DeleteWarnSchedule]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            if (a_con != null){
                a_con.rollback();
            }
            _Environ._MyLogger.severe("[DeleteWarnSchedule]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        _Environ._MyLogger.info("*** DeleteWarnSchedule is finished. ***");
    }
    
    //警告情報の登録件数⇒未使用
/*
    public int GetEntryCount() throws Exception{
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "SELECT COUNT(JOBID) AS REC_SUM FROM JOBSCHEDULE;";
        int a_iRet = 0;
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                a_iRet = a_rs.getInt("REC_SUM");
            }
        } catch (SQLException e) {
            System.err.println("SQL failed.");
            e.printStackTrace ();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace ();
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        return a_iRet;
    }
*/    
    //pagerの作成
    public String MakePagerWarnSchedule(int h_kind, int h_pageNo) throws Exception{
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
            _Environ._MyLogger.severe("[MakePagerWarnSchedule]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[MakePagerWarnSchedule]" + ex.getMessage());
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
        _Environ._MyLogger.info("*** MakePagerWarnSchedule is finished. ***");
        
        return a_sRet;
    }
    
    //pagerの作成
    public String MakePagerAlarmLog(int h_kind, int h_pageNo) throws Exception{
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "SELECT COUNT(STARTDATE) AS REC_SUM FROM ALARMLOG;";
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
            _Environ._MyLogger.severe("[MakePagerAlarmLog]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[MakePagerAlarmLog]" + ex.getMessage());
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
        _Environ._MyLogger.info("*** MakePagerAlarmLog is finished. ***");
        
        return a_sRet;
    }
    
    public  ArrayList<String> FindWarnSchedule(int h_pageNo) throws Exception{
        ArrayList<String> a_arrayRet = null;
        int a_iSum = 0;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "SELECT COUNT(JOBID) AS REC_SUM FROM JOBSCHEDULE;";
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
                a_sql = "SELECT row_number() over(ORDER BY STARTTIME " + OrderStartTime + ",ENDTIME), * FROM JOBSCHEDULE ORDER BY STARTTIME " + OrderStartTime + ",ENDTIME";
                a_sql = "SELECT * FROM (" + a_sql + ") s1 WHERE (s1.row_number BETWEEN " + String.valueOf(a_start_idx) + " AND " + String.valueOf(a_end_idx) + ");";
                a_ps = a_con.prepareStatement(a_sql);
                a_rs = a_ps.executeQuery();
                while(a_rs.next()){
                    a_sRet = "";
                    a_sVal = _Environ.ExistDBString(a_rs,"jobid");
                    a_sRet += a_sVal;
                    a_sVal = _Environ.ExistDBString(a_rs,"starttime");
                    if (a_sVal.length()>0){
                        a_sVal = a_sVal.substring(0,16).replace("-", "/");
                    }
                    a_sRet += "\t" + a_sVal;
                    a_sVal = _Environ.ExistDBString(a_rs,"endtime");
                    if (a_sVal.length()>0){
                        a_sVal = a_sVal.substring(0,16).replace("-", "/");
                    }
                    a_sRet += "\t" + a_sVal;
                    a_sVal = _Environ.ExistDBString(a_rs,"customername");
                    a_sRet += "\t" + a_sVal;
                    a_sVal = _Environ.ExistDBString(a_rs,"branchname");
                    a_sRet += "\t" + a_sVal;
                    a_iVal = _Environ.ExistDBInt(a_rs,"jobkind");
                    //a_sVal = _Environ.ToJobKindString(a_iVal);
                    a_sVal = ToJobKindColor(a_iVal);
                    a_sRet += "\t" + a_sVal;
                    a_sVal = _Environ.ExistDBString(a_rs,"jobcontents");
                    a_sRet += "\t" + a_sVal;
                    a_arrayRet.add(a_sRet);
                }
                a_rs.close();
                a_ps.close();
            }
            
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[FindWarnSchedule]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[FindWarnSchedule]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        _Environ._MyLogger.info("*** FindWarnSchedule is finished. ***");
        
        return a_arrayRet;
    }
    
    public  ArrayList<String> GetWarnSchedule(String h_JobId) throws Exception{
        ArrayList<String> a_arrayRet = null;
        a_arrayRet = new ArrayList<String>();
        String a_sVal = "";
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "SELECT * FROM JOBSCHEDULE WHERE (JOBID='" + h_JobId + "');";
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                    a_sVal = _Environ.ExistDBString(a_rs,"jobid");
                    a_arrayRet.add(a_sVal);
                    a_sVal = _Environ.ExistDBString(a_rs,"customername");
                    a_arrayRet.add(a_sVal);
                    a_sVal = _Environ.ExistDBString(a_rs,"branchname");
                    a_arrayRet.add(a_sVal);
                    a_sVal = _Environ.ExistDBString(a_rs,"jobkind");
                    a_arrayRet.add(a_sVal);
                    a_sVal = _Environ.ExistDBString(a_rs,"jobcontents");
                    a_arrayRet.add(a_sVal);
                    a_sVal = _Environ.ExistDBString(a_rs,"starttime");
                    if (a_sVal.length()>0){
                        a_sVal = a_sVal.substring(0,16).replace("-", "/");
                    }
                    a_arrayRet.add(a_sVal);
                    a_sVal = _Environ.ExistDBString(a_rs,"endtime");
                    if (a_sVal.length()>0){
                        a_sVal = a_sVal.substring(0,16).replace("-", "/");
                    }
                    a_arrayRet.add(a_sVal);
                    a_sVal = _Environ.ExistDBString(a_rs,"execdays");
                    a_arrayRet.add(a_sVal);
                    a_sVal = _Environ.ExistDBString(a_rs,"execkind");
                    a_arrayRet.add(a_sVal);
            }
            a_rs.close();
            a_ps.close();
            
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[GetWarnSchedule]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[GetWarnSchedule]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        
        _Environ._MyLogger.info("*** GetWarnSchedule is finished. ***");

        return a_arrayRet;
    }
    
    public  ArrayList<String> FindAlarmLog(int h_pageNo) throws Exception{
        ArrayList<String> a_arrayRet = null;
        int a_iSum = 0;
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "SELECT COUNT(STARTDATE) AS REC_SUM FROM ALARMLOG;";
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
                a_sql = "SELECT row_number() over(ORDER BY STARTDATE " + OrderStartDate + "), * FROM ALARMLOG ORDER BY STARTDATE " + OrderStartDate;
                a_sql = "SELECT * FROM (" + a_sql + ") s1 WHERE (s1.row_number BETWEEN " + String.valueOf(a_start_idx) + " AND " + String.valueOf(a_end_idx) + ");";
                a_ps = a_con.prepareStatement(a_sql);
                a_rs = a_ps.executeQuery();
                while(a_rs.next()){
                    a_sRet = "";
                    a_sVal = _Environ.ExistDBString(a_rs,"startdate");
                    a_sRet += a_sVal;
                    a_sVal = _Environ.ExistDBString(a_rs,"enddate");
                    a_sRet += "\t" + a_sVal;
                    a_sVal = _Environ.ExistDBString(a_rs,"filename");
                    a_sRet += "\t" + a_sVal;
                    a_iVal = _Environ.ExistDBInt(a_rs,"jobkind");
                    //a_sVal = _Environ.ToJobKindString(a_iVal);
                    a_sVal = _Environ.ToJobKindString(a_iVal);
                    a_sRet += "\t" + a_sVal;
                    a_sVal = _Environ.ExistDBString(a_rs,"customername");
                    a_sRet += "\t" + a_sVal;
                    a_sVal = _Environ.ExistDBString(a_rs,"branchname");
                    a_sRet += "\t" + a_sVal;
                    a_sVal = _Environ.ExistDBString(a_rs,"msg");
                    a_sRet += "\t" + a_sVal;
                    a_arrayRet.add(a_sRet);
                }
                a_rs.close();
                a_ps.close();
            }
            
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[FindAlarmLog]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[FindAlarmLog]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        _Environ._MyLogger.info("*** FindAlarmLog is finished. ***");
        
        return a_arrayRet;
    }
    
    //発報試験
    public void TestAlaram(String h_JobId) throws Exception{
        //h_kind⇒1：発報試験、0：スケジューラ
        ArrayList<String> a_arrayRet = null;
        a_arrayRet = new ArrayList<String>();
        String a_sVal = "";
        
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "SELECT * FROM JOBSCHEDULE WHERE (JOBID='" + h_JobId + "');";
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();

            make_AlarmFile(1,a_rs);
            
            a_rs.close();
            a_ps.close();
            
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[TestAlaram]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[TestAlaram]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        
        _Environ._MyLogger.info("*** TestAlaram is finished. ***");
    }
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //警告情報の登録
    private void insert_WarnSchedule() throws Exception{
        Connection a_con = null;
        PreparedStatement a_ps = null;
        String a_sql = "INSERT INTO JOBSCHEDULE (" +
                "JOBID,CUSTOMERNAME,BRANCHNAME,JOBKIND,JOBCONTENTS,STARTTIME,ENDTIME,EXECDAYS,EXECKIND" + 
                ") VALUES(" +
                "?,?,?,?,?,?,?,?,?";
        a_sql += ");";
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_con.setAutoCommit(false);
            a_ps = a_con.prepareStatement(a_sql);
            a_ps.setString(1, make_JobId());
            a_ps.setString(2, CustomerName);
            a_ps.setString(3, BranchName);
            a_ps.setInt(4, Integer.parseInt(JobKind));
            a_ps.setString(5, JobContents);
            a_ps.setTimestamp(6, _TimeS);
            a_ps.setTimestamp(7, _TimeE);
            a_ps.setInt(8, _ExecDays);
            a_ps.setInt(9, _ExecKind);
            int a_i = a_ps.executeUpdate();
            a_con.commit();
        } catch (SQLException e) {
            if (a_con != null){
                a_con.rollback();
            }
            _Environ._MyLogger.severe("[insert_WarnSchedule]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            if (a_con != null){
                a_con.rollback();
            }
            _Environ._MyLogger.severe("[insert_WarnSchedule]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        _Environ._MyLogger.info("*** insert_WarnSchedule is finished. ***");
    }
    
    //警告情報の更新
    private void update_WarnSchedule() throws Exception{
        Connection a_con = null;
        PreparedStatement a_ps = null;
        String a_sql = "UPDATE JOBSCHEDULE SET " +
                "CUSTOMERNAME=?,BRANCHNAME=?,JOBKIND=?,JOBCONTENTS=?,STARTTIME=?,ENDTIME=?,EXECDAYS=?,EXECKIND=?" + 
                " WHERE (JOBID=?);";
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_con.setAutoCommit(false);
            a_ps = a_con.prepareStatement(a_sql);
            a_ps.setString(1, CustomerName);
            a_ps.setString(2, BranchName);
            a_ps.setInt(3, Integer.parseInt(JobKind));
            a_ps.setString(4, JobContents);
            a_ps.setTimestamp(5, _TimeS);
            a_ps.setTimestamp(6, _TimeE);
            a_ps.setInt(7, _ExecDays);
            a_ps.setInt(8, _ExecKind);
            a_ps.setString(9, JobId);
            int a_i = a_ps.executeUpdate();
            a_con.commit();
        } catch (SQLException e) {
            if (a_con != null){
                a_con.rollback();
            }
            _Environ._MyLogger.severe("[update_WarnSchedule]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            if (a_con != null){
                a_con.rollback();
            }
            _Environ._MyLogger.severe("[update_WarnSchedule]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        _Environ._MyLogger.info("*** update_WarnSchedule is finished. ***");
    }
    
    //曜日の設定
    private void set_ExecDays(){
        _ExecDays = 0;
        try{
            if (ExecDaysAll.toLowerCase().equals("true") == true){
                _ExecDays +=1;
            }
            if (ExecDaysSun.toLowerCase().equals("true") == true){
                _ExecDays +=2;
            }
            if (ExecDaysMon.toLowerCase().equals("true") == true){
                _ExecDays +=4;
            }
            if (ExecDaysTue.toLowerCase().equals("true") == true){
                _ExecDays +=8;
            }
            if (ExecDaysWed.toLowerCase().equals("true") == true){
                _ExecDays +=16;
            }
            if (ExecDaysThr.toLowerCase().equals("true") == true){
                _ExecDays +=32;
            }
            if (ExecDaysFri.toLowerCase().equals("true") == true){
                _ExecDays +=64;
            }
            if (ExecDaysSat.toLowerCase().equals("true") == true){
                _ExecDays +=128;
            }
        } catch (Exception e) {
            _Environ._MyLogger.severe("[set_ExecDays]" + e.getMessage());
        }
    }
       
    //実行の設定
    private void set_ExecKind(){
        _ExecKind = 0;
        try{
            if (ExecKindSt.toLowerCase().equals("true") == true){
                _ExecKind |=1;
            }
            if (ExecKindEd.toLowerCase().equals("true") == true){
                _ExecKind |=2;
            }
        } catch (Exception e) {
            _Environ._MyLogger.severe("[set_ExecKind]" + e.getMessage());
        }
    }
    
    //開始日時・終了日時チェック
    private void chek_WarnStEd(){
        String[] a_dateS = null;
        String[] a_dateE = null;
        String[] a_timeS = null;
        String[] a_timeE = null;
    
        try{
            if ((TimeS.length() > 0)  && (TimeE.length() > 0)){
                a_dateS = TimeS.split("/");
                a_dateE = TimeE.split("/");
                if ((Integer.valueOf(a_dateS[0]) == Integer.valueOf(a_dateE[0])) &&
                    (Integer.valueOf(a_dateS[1]) == Integer.valueOf(a_dateE[1]))){
                    //年月が同じ
                    //alert('年が同じ');
                    a_timeS = a_dateS[2].split(" ");
                    a_timeE = a_dateE[2].split(" ");
                    if (Integer.valueOf(a_timeS[0]) == Integer.valueOf(a_timeE[0])){
                        //日が同じ
                        _ExecDays = 0;

                        a_timeS = a_timeS[1].split(":");
                        a_timeE = a_timeE[1].split(":");
                        if ((Integer.valueOf(a_timeS[0]) == Integer.valueOf(a_timeE[0])) &&
                            (Integer.valueOf(a_timeS[1]) == Integer.valueOf(a_timeE[1]))){
                            //時刻が同じ
                            _ExecKind = 1;
                        }
                    }
                }
            }
        } catch (Exception e) {
            _Environ._MyLogger.severe("[chek_WarnStEd]" + e.getMessage());
        }
    }
    
    //JobId生成
    private String make_JobId(){
        String a_sRet = "";
        try{
            java.util.Date a_date = new java.util.Date();
            a_sRet = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(a_date);
        } catch (Exception e) {
            _Environ._MyLogger.severe("[make_JobId]" + e.getMessage());
        }
        return a_sRet;
    }
    
    //作業内容の色生成
    public String ToJobKindColor(int h_kind){
        String a_sRet = "";
        switch (h_kind){
            case 1: //事前確認
                a_sRet = "#ffff00";
                break;
            case 2: //入退管理
                a_sRet = "#ffff00";
                break;
            case 3: //停電
                a_sRet = "#ff9900";
                break;
            case 4: //作業
                a_sRet = "#98fb98";
                break;
            case 5: //
                a_sRet = "#d3d3d3";
                break;
        }
        return a_sRet;
    }

    //ALARAMファイルの作成
    public void make_AlarmFile(
        int h_kind,
        ResultSet h_rs) throws Exception{
        //h_kind⇒1：発報試験、0：スケジューラ
        java.util.Date a_date_start = null; //new java.util.Date();
        String a_sTmp = ""; //new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(a_date_start);
        String a_sTmp2 = "";    //[2016.06.18]
        int a_iTmp = 0;
        Timestamp a_startDate = null;   //_Environ.ToSqlTimestampFromString(a_sTmp);
        Timestamp a_endDate = null;
        int a_result = 0;
        String a_msg = "";
        String a_fname = "";
        String a_sNo_Now = "001";   //[2016.05.21]bug-fixed.
        boolean a_isExp = false;
        
        //AlarmFileの格納パスを取得
        String a_sAlarmPath = _Environ.GetEnvironValue("alarm_path");
        String a_sAlarmNoFile = a_sAlarmPath + "ALARM.NO";      //[2016.05.12]bug-fixed.
//        String a_sAlarmNoTmpFile = a_sAlarmPath + "ALARM.TMP";  //[2016.05.12]bug-fixed.
        
        //①ファイルをコピー
        /*[2016.05.18]
        FileChannel a_srcCh = new FileInputStream(a_sAlarmNoFile).getChannel();
        FileChannel a_dstCh = new FileOutputStream(a_sAlarmNoTmpFile).getChannel();
        try{
            a_srcCh.transferTo(0,a_srcCh.size(), a_dstCh);
        } catch (Exception e){
            _Environ._MyLogger.severe("[make_AlarmFile]" + e.getMessage());
            a_msg = e.getMessage();
        }finally{
            a_srcCh.close();
            a_dstCh.close();
        }
        */
        
        //②alarm.noファイルを開く（排他あり）
        File a_lockFile = null;
        FileOutputStream a_fos = null;
        FileInputStream a_fin = null;
        BufferedWriter a_bw = null;
        BufferedReader a_br = null;
        FileChannel a_ch = null;    //[2016.06.17]
        FileLock a_lock = null;     //[2016.06.17]
        int a_idx = 0;
        try{
            String a_JobId = "";
            a_idx = 0;
            while(h_rs.next()){
                a_msg = "";
                a_date_start = new java.util.Date();
                a_sTmp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(a_date_start);
                a_startDate = _Environ.ToSqlTimestampFromString(a_sTmp);

                //[2016.06.17]発報対象があった場合のみ
                if (a_idx == 0){
                    //①ファイルをコピー[2016.06.22]排除
                    /*
                    FileChannel a_srcCh = new FileInputStream(a_sAlarmNoFile).getChannel();
                    FileChannel a_dstCh = new FileOutputStream(a_sAlarmNoTmpFile).getChannel();
                    try{
                        a_srcCh.transferTo(0,a_srcCh.size(), a_dstCh);
                    } catch (Exception e){
                        _Environ._MyLogger.severe("[make_AlarmFile]" + e.getMessage());
                        a_msg = e.getMessage();
                    }finally{
                        a_srcCh.close();
                        a_dstCh.close();
                    }
                    */
                    
                    //②alarm.noファイルを開く（排他あり）
                    a_lockFile = null;
                    a_fin = null;
                    a_br = null;
                    a_fos = null;
                    a_bw = null;
                    a_ch = null;
                    a_lock = null;
                    try{
                        //[2016.06.22]ALARM.NOから最新NOを取得
                        a_fin = new FileInputStream(a_sAlarmNoFile);
                        a_br = new BufferedReader(new InputStreamReader(a_fin));
                        a_sNo_Now = a_br.readLine(); 
                        a_br.close();
                        a_fin.close();
                        a_br = null;
                        a_fin = null;

                        //[2016.06.22]ALARM.NOをロック
                        a_lockFile = new File(a_sAlarmNoFile);
                        a_fos = new FileOutputStream(a_lockFile);
                        a_bw = new BufferedWriter(new OutputStreamWriter(a_fos));
                        a_ch = a_fos.getChannel();
                        a_lock = a_ch.tryLock();
                        if (a_lock != null){    //[2016.06.22]
                            //a_lock = a_ch.lock(0, Long.MAX_VALUE, false);
                        }else{
                            a_isExp = true;
                            _Environ._MyLogger.severe("[make_AlarmFile]ALARM.NO is locked.");
                            a_msg = "ALARM.NO is locked.";
                        }
                    } catch (Exception e){
                        a_isExp = true;
                        _Environ._MyLogger.severe("[make_AlarmFile]" + e.getMessage());
                        a_msg = e.getMessage();
                        if (a_msg == null){
                            a_msg = "ALARM.NO is locked.";
                        }
                    } finally{
                        //[2016.06.22]ALARM.NO
                        if (a_br != null){
                            a_br.close();
                        }
                        if (a_fin != null){
                            a_fin.close();
                        }
                    }
                    
                    if (a_lock == null){    //[2016.06.22]
                        break;
                    }
                }

                a_idx++;
                a_result = 0;
                a_JobId =  _Environ.ExistDBString(h_rs,"jobid");
                _Environ._MyLogger.finest("[make_AlarmFile]JOBID=" + a_JobId);

                if (a_sNo_Now != null){
                    if (a_sNo_Now.length() > 0){
                        int a_iNow = Integer.valueOf(a_sNo_Now);
                        if (a_iNow < 999){
                            //　それ以外の場合はプラス1した値とする
                            a_iNow++;
                        }else{
                            //　現在の番号がない場合、もしくは999の場合は、001とする。
                            a_iNow = 1;
                        }
                        //左0パディング
                        String a_sPad = "000" +  String.valueOf(a_iNow);
                        int a_iLen = a_sPad.length();
                        a_sNo_Now = a_sPad.substring(a_iLen - 3, a_iLen);
                    }else{
                        //　ファイルサイズが0の場合は、001とする。
                        a_sNo_Now = "001";
                    }
                }else{
                    a_sNo_Now = "001";
                }

                //③alarm.<xxx>ファイルを開く
                //　内容を書き込む
                //[2016.04.27]★↓
                String a_fA = a_sAlarmPath + "ALARM." + a_sNo_Now;  //[2016.05.12]bug-fixed.
                FileOutputStream a_fosA = null;
                OutputStreamWriter a_osA = null;
                int a_len = 0;
                try{
                    a_fosA = new FileOutputStream(a_fA);
                    a_osA = new OutputStreamWriter(a_fosA,"shift-jis");

                    //1行目：ユーザ番号(7)PC-No(2)1stメッセージ(80)2ndメッセージ(40)3rdメッセージ(40)
                    a_sTmp = "A-SET19";
                    a_iTmp = a_sTmp.getBytes().length;
                    a_sTmp += _paddingAlarmValue(7, a_iTmp);
                    a_osA.write(a_sTmp);

                    a_sTmp = "99";
                    a_iTmp = a_sTmp.getBytes().length;
                    a_sTmp += _paddingAlarmValue(2, a_iTmp);
                    a_osA.write(a_sTmp);

                    //[2016.05.16]bug-fixed.
                    a_sTmp = "※" + _Environ.ToJobKind2String(_Environ.ExistDBInt(h_rs, "JOBKIND"));
                    //[2016.06.18]要望
                    a_sTmp += "[";
                    a_sTmp2 = _Environ.ExistDBString(h_rs, "JOBCONTENTS");
                    a_sTmp += a_sTmp2;
                    a_iTmp = a_sTmp2.length();
                    a_sTmp += _paddingAlarmValue(33, a_iTmp);
                    a_sTmp += "]";
                    a_iTmp = a_sTmp.length();
                    a_sTmp += _paddingAlarmValue(80, a_iTmp);
                    /*
                    a_iTmp = a_sTmp.getBytes().length;
                    a_sTmp += _paddingAlarmValue2(80, a_iTmp);
                    */
                    a_osA.write(a_sTmp);

                    //[2016.05.16]bug-fixed.⇒何の時刻？
                    a_sTmp = "";
                    String a_sExecTime = "";
                    SimpleDateFormat a_sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date a_dExecTime = null;
                    Date a_dEnd = null;
                    Date a_dNow = new Date();   //現在時刻
                    String a_sNow = a_sdf.format(a_dNow.getTime());
                    if (h_kind == 1){
                        //発報試験⇒現在時刻を設定
                        a_sTmp = a_sNow;
                    }else{
                        a_sExecTime = _Environ.ExistDBString(h_rs,"exectime");
                        /*
                        if (a_sExecTime.length()>0){
                            a_sExecTime = a_sExecTime.substring(0,16).replace("-", "/") + ":00";
                            a_dExecTime = a_sdf.parse(a_sExecTime);
                        }
                        a_sTmp = a_sNow.substring(0,10) + " " + a_sExecTime.subSequence(11, 19);
                        */
                        a_sTmp = a_sNow.substring(0,10) + " " + a_sExecTime;
                    }

                    a_iTmp = a_sTmp.getBytes().length;
                    a_sTmp += _paddingAlarmValue(40, a_iTmp);
                    a_osA.write(a_sTmp);

                    //[2016.05.16]bug-fixed.
                    a_sTmp = _Environ.ExistDBString(h_rs, "customername");
                    a_sTmp += " " + _Environ.ExistDBString(h_rs, "branchname");
                    //[2016.06.15]要望
                    a_iTmp = a_sTmp.length();
                    a_sTmp += _paddingAlarmValue2(40, a_iTmp);
                    /*
                    a_iTmp = a_sTmp.getBytes().length;
                    a_sTmp += _paddingAlarmValue(40, a_iTmp);
                    */
                    a_osA.write(a_sTmp + "\r\n");

                    //2行目：ユーザ名(44)
                    a_sTmp = _Environ.ToJobKindString(_Environ.ExistDBInt(h_rs, "JOBKIND"));
                    //[2016.06.15]要望
                    a_iTmp = a_sTmp.length();
                    a_sTmp += _paddingAlarmValue2(44, a_iTmp);
                    /*
                    a_iTmp = a_sTmp.getBytes().length;
                    a_sTmp += _paddingAlarmValue(44, a_iTmp);
                    */
                    a_osA.write(a_sTmp + "\r\n");

                    //3行目：機種名(12)
                    a_sTmp = "見える化";
                    //[2016.06.15]要望
                    a_iTmp = a_sTmp.length();
                    a_sTmp += _paddingAlarmValue2(12, a_iTmp);
                    /*
                    a_iTmp = a_sTmp.getBytes().length;
                    a_sTmp += _paddingAlarmValue(12, a_iTmp);
                    */
                    a_osA.write(a_sTmp + "\r\n");

                    //4行目：Version(10)
                    a_sTmp = "1.00";
                    a_iTmp = a_sTmp.getBytes().length;
                    a_sTmp += _paddingAlarmValue(10, a_iTmp);
                    a_osA.write(a_sTmp + "\r\n");

                    //5行名：ユーザIDコード(32)
                    a_sTmp = "";
                    a_iTmp = a_sTmp.getBytes().length;
                    a_sTmp += _paddingAlarmValue(32, a_iTmp);
                    a_osA.write(a_sTmp + "\r\n");

                    //6行目：保守担当者(60)
                    a_sTmp = "OKI Wintech";
                    a_iTmp = a_sTmp.getBytes().length;
                    a_sTmp += _paddingAlarmValue(60, a_iTmp);
                    a_osA.write(a_sTmp + "\r\n");

                    //7行目：MJ(3)MN(3)未使用(6)
                    a_sTmp = "";
                    a_iTmp = a_sTmp.getBytes().length;
                    a_sTmp += _paddingAlarmValue(12, a_iTmp);
                    a_osA.write(a_sTmp + "\r\n");
                } catch (Exception e){
                    _Environ._MyLogger.severe("[make_AlarmFile]" + e.getMessage());
                    a_msg = e.getMessage();
                } finally{
                    if (a_osA != null){
                        a_osA.close();
                    }
                    if (a_fosA != null){
                        a_fosA.close();
                    }
                }                    
                //[2016.04.27]★↑
                //　alarm.<xxx>を閉じる。

                //ALARMログの書込み
                if (a_msg.length() <= 0){
                    a_result = 0;
                    a_msg = "正常";
                }else{
                    a_result = 1;
                }
                insert_AlarmLog(
                        h_rs,
                        a_startDate,
                        a_result,
                        a_msg,
                        "ALARM." + a_sNo_Now
                    );
            }

            //④alarm.noファイルの内容を最新の番号に書き換えて、閉じる。
            //[2016.06.22]ALARM.NO
            if (a_idx > 0){
                a_bw.write(a_sNo_Now + "\r\n");
                //[2016.06.17]発報対象があった場合のみ
                /*
                if (a_bw != null){
                   a_bw.close();
                }
                if (a_fos != null){
                    a_fos.close();
                }
                */
                if (a_lock != null){
                    a_lock.release();
                }
            }
        } catch (Exception e){
            a_isExp = true;
            _Environ._MyLogger.severe("[make_AlarmFile]" + e.getMessage());
            a_msg = e.getMessage();
        } finally{
            if (a_bw != null){
               a_bw.close();
            }
            if (a_fos != null){
                a_fos.close();
            }
        }
        
        //[2016.06.22]ALARM.TMPをALARM.NOへ
        /*
        if (a_idx > 0){
            //⑤一時ファイルの削除
            File a_file_src = new File(a_sAlarmNoTmpFile);
            File a_file_dst= new File(a_sAlarmNoFile);
            try{
                a_file_src.renameTo(a_file_dst);
            } catch (Exception e){
                a_isExp = true;
                _Environ._MyLogger.severe("[make_AlarmFile]" + e.getMessage());
                a_msg = e.getMessage();
            }
        }
        */
        
        if (a_isExp == true){
            a_result = 1;
            /*
            if (a_msg.length() <= 0){
                a_result = 0;
                a_msg = "正常";
            }else{
                a_result = 1;
            }
            */
            insert_AlarmLog(
                    h_rs,
                    a_startDate,
                    a_result,
                    a_msg,
                    "ALARM." + a_sNo_Now
                );
        }
        
        _Environ._MyLogger.info("*** make_AlarmFile is finished. ***");
    }
    
    //[2016.05.13]bug-fixed.
    private String _paddingAlarmValue(
            int h_MaxLen,
            int h_nowLen
        ){
        String a_sRet = "";
        
        int a_iLen = h_MaxLen - h_nowLen;
        if (a_iLen > 0){
            a_sRet = String.format("%-"+ String.valueOf(a_iLen) + "s", "");
        }

        return a_sRet;
    }
    
    //[2016.06.15]要望（全角スペース埋め）
    private String _paddingAlarmValue2(
            int h_MaxLen,
            int h_nowLen
        ){
        StringBuilder a_sb = new StringBuilder();

        int a_iLen = h_MaxLen - h_nowLen;
        
        for (int a_i=1; a_i<=a_iLen; a_i++){
            a_sb.append("　");
        }

        return a_sb.toString();
    }

    //ALARMログの登録
    private void insert_AlarmLog(
            ResultSet h_rs,
            Timestamp h_startDate,
            int h_result,
            String h_msg,
            String h_fname
        ) throws Exception{
        Connection a_con = null;
        PreparedStatement a_ps = null;
        String a_sql = "INSERT INTO ALARMLOG (" +
                "STARTDATE,ENDDATE,RESULT,MSG,CUSTOMERNAME,BRANCHNAME,JOBKIND,FILENAME" + 
                ") VALUES(" +
                "?,?,?,?,?,?,?,?";
        a_sql += ");";
        try{
            java.util.Date a_date_start = new java.util.Date();
            String a_sTmp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(a_date_start);
            Timestamp a_endDate = _Environ.ToSqlTimestampFromString(a_sTmp);
            
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_con.setAutoCommit(false);
            a_ps = a_con.prepareStatement(a_sql);
            a_ps.setTimestamp(1, h_startDate);
            a_ps.setTimestamp(2, a_endDate);
            a_ps.setInt(3, h_result);
            a_ps.setString(4, h_msg);

            a_ps.setString(5, _Environ.ExistDBString(h_rs, "customername"));
            a_ps.setString(6, _Environ.ExistDBString(h_rs, "branchname"));
            a_ps.setInt(7, _Environ.ExistDBInt(h_rs, "jobkind"));

            a_ps.setString(8, h_fname);
            int a_i = a_ps.executeUpdate();
            a_con.commit();
        } catch (SQLException e) {
            if (a_con != null){
                a_con.rollback();
            }
            _Environ._MyLogger.severe("[insert_WarnSchedule]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            if (a_con != null){
                a_con.rollback();
            }
            _Environ._MyLogger.severe("[insert_WarnSchedule]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        _Environ._MyLogger.info("*** insert_AlarmLog is finished. ***");
    }
    
}
