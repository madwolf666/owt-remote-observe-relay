/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maintenance;

import warn.*;
import common.Environ;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Chappy
 */
public class SenceAnalyzeLog{
    private static Environ _Environ = new Environ();
    private static AnalyzeLog _AnalyzeLog = new AnalyzeLog();
    private static String _realPath = "C:\\Users\\hal\\Documents\\NetBeansProjects\\owt-remote-observe-relay\\build\\web";
    private static int _period = 0;   //周期
    private static String _sPrevTime = "";
    
    public static void main(String args[]) throws Exception{

        //引数
        if (args.length<=0){
            //return;
        }
        
        for (int a_i=0; a_i<args.length; a_i++){
            _realPath = args[0];
            System.out.println(args[a_i]);
        }
        
        _Environ.SetRealPath(_realPath);
        //a_delay = Integer.valueOf(_Environ.GetEnvironValue("sence_alarm_delay"));
        _period = Integer.valueOf(_Environ.GetEnvironValue("mnt_analyze_log_period"));
        
        /*
        TimerSenceAlarm a_task = new TimerSenceAlarm();
        Timer a_timer = new Timer();
        a_timer.schedule(a_task, a_delay, a_period);
        */
        
        //現在時刻を取得
        java.util.Date a_date_start = new java.util.Date();
        SimpleDateFormat a_sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        _sPrevTime = a_sdf1.format(a_date_start);

        while(true){
            try{
                Thread.sleep(_period);
                ExecAalyzeLog();
            } catch(InterruptedException e){
            }
        }
        
    }
    
    public static void ExecAalyzeLog() throws Exception{
        _AnalyzeLog.SetRealPath(_realPath);

        //----------------------------------------------------------------------
        //現在時刻を取得
        //----------------------------------------------------------------------
        java.util.Date a_date_start = new java.util.Date();
        SimpleDateFormat a_sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String a_sNowTime = a_sdf1.format(a_date_start);
        a_sNowTime = _sPrevTime;    //前回の終わり時刻

        //----------------------------------------------------------------------
        //次の周期の時間
        //----------------------------------------------------------------------
        Calendar a_cal1 = Calendar.getInstance();
        a_cal1.setTime(a_date_start);
        a_cal1.add(Calendar.SECOND, _period / 1000);
        String a_sPeriodTime = a_sdf1.format(a_cal1.getTime());

        String a_sql = "";
        //----------------------------------------------------------------------
        //SQL組み立て
        //----------------------------------------------------------------------
        //開始時間が現在以前（現在を含む）
        //期間が前回処理時間より以降
        //期間が、期間（前日のhh:mm）～期間（hh:mm）
        if (_AnalyzeLog._db_driver.equals("oracle.jdbc.driver.OracleDriver")){
            //Oracleの場合
            a_sql = 
                "SELECT *,TO_CHAR(SYSDATE-1,'YYYY-MM-DD ')||PERIOD AS START_EXECTIME" +
                ",TO_CHAR(SYSDATE,'YYYY-MM-DD ')||PERIOD AS END_EXECTIME";
        }else if (_AnalyzeLog._db_driver.equals("org.postgresql.Driver")){
            //PostgreSQLの場合
            a_sql = 
                "SELECT *,TO_CHAR(CURRENT_TIMESTAMP+'-1 days','YYYY-MM-DD ')||PERIOD AS START_EXECTIME" +
                ",TO_CHAR(CURRENT_TIMESTAMP,'YYYY-MM-DD ')||PERIOD AS END_EXECTIME";
        }
        a_sql += 
            " FROM LOGANALYZESCHEDULE WHERE" +
            " (TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI')<='" + a_sNowTime.substring(0,16) + ":00')" +
            " AND (PERIOD>='" + a_sNowTime.substring(11,16) + "')" +
            " AND (PERIOD<'" + a_sPeriodTime.substring(11,16) + "')";
        
        a_sql = "SELECT t1.* FROM (" + a_sql + ") t1 ORDER BY START_EXECTIME"; 
        a_sql += ";";
        _AnalyzeLog._Environ._MyLogger.finest("[ExecAalyzeLog]" + a_sql);
        
        //h_kind⇒1：発報試験、0：スケジューラ
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;

        try{
            Class.forName (_AnalyzeLog._db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_AnalyzeLog._db_url, _AnalyzeLog._db_user, _AnalyzeLog._db_pass);
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            try{
                //保全データを解析
                _AnalyzeLog.analyze_Log(0,a_rs);
            } catch (Exception e){
                _AnalyzeLog._Environ._MyLogger.severe("[ExecAalyzeLog]" + e.getMessage());
            }
            a_rs.close();
            a_ps.close();

        } catch (SQLException e) {
            _AnalyzeLog._Environ._MyLogger.severe("[ExecAalyzeLog]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _AnalyzeLog._Environ._MyLogger.severe("[ExecAalyzeLog]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        //次の開始時刻
        _sPrevTime = a_sPeriodTime;

        _AnalyzeLog._Environ._MyLogger.info("*** ExecAalyzeLog is finished. ***");
    }
    
}

