/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Chappy
 */
public class SenceReportMonthly{
    private static Environ _Environ = new Environ();
    private static ReportMonthly _ReportMonthly = new ReportMonthly();
    private static String _realPath = "C:\\Users\\chappy\\Documents\\NetBeansProjects\\owt-remote-observe-relay\\build\\web";
    private static int _period = 0;   //周期
    
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
        _period = Integer.valueOf(_Environ.GetEnvironValue("report_monthly_period"));
        
        /*
        TimerSenceAlarm a_task = new TimerSenceAlarm();
        Timer a_timer = new Timer();
        a_timer.schedule(a_task, a_delay, a_period);
        */
        
        while(true){
            try{
                Thread.sleep(_period);
                ExecReportMonthly();
            } catch(InterruptedException e){
            }
        }
        
    }
    
    public static void ExecReportMonthly() throws Exception{
        _ReportMonthly.SetRealPath(_realPath);

        //現在時刻を取得
        java.util.Date a_date_start = new java.util.Date();
        String a_sNowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a_date_start);
        String a_sql = "";
        //①開始時刻が現在のもの
        //　毎日もしくは本日の曜日にチェックあり
        a_sql = 
            "SELECT * FROM REPORTSCHEDULE WHERE" +
            " (STATUS='1')" +
            " ORDER BY EXECTIME";
        a_sql += ";";
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;

        try{
            int a_iNum = 0;
            
            Class.forName (_ReportMonthly._db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_ReportMonthly._db_url, _ReportMonthly._db_user, _ReportMonthly._db_pass);
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                a_iNum++;
            }
            
            if (a_iNum<=0){
                //[2016.06.07]bug-fixed.
                a_sql = 
                    "SELECT * FROM REPORTSCHEDULE WHERE" +
                    " (TO_CHAR(EXECTIME,'YYYY-MM-DD HH24:MI:SS')<='" + a_sNowTime + "')" +
                    " AND (STATUS='0')" +
                    " ORDER BY EXECTIME";
                a_sql += ";";
                a_ps = a_con.prepareStatement(a_sql);
                a_rs = a_ps.executeQuery();

                try{
                    _ReportMonthly.make_ReportMonthly(a_rs);
                } catch (Exception e){
                    _ReportMonthly._Environ._MyLogger.severe("[ExecReportMonthly]" + e.getMessage());
                }
            }
            
            a_rs.close();
            a_ps.close();

        } catch (SQLException e) {
            _ReportMonthly._Environ._MyLogger.severe("[ExecReportMonthly]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _ReportMonthly._Environ._MyLogger.severe("[ExecReportMonthly]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        _ReportMonthly._Environ._MyLogger.info("*** ExecReportMonthly is finished. ***");
    }
    
    /*
    static class TimerSenceAlarm extends TimerTask{
        public void run() {
            try{
                ExecSenceAlarm();
            } catch (Exception e){
            }
        }
    }
    */
}

