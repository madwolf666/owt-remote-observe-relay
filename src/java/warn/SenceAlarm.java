/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warn;

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
public class SenceAlarm{
    private static Environ _Environ = new Environ();
    private static WarnSchedule _WarnSchedule = new WarnSchedule();
    private static String _realPath = "C:\\Users\\chappy\\Documents\\NetBeansProjects\\owt-remote-observe-relay\\build\\web";
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
        _period = Integer.valueOf(_Environ.GetEnvironValue("alarm_period"));
        
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
                ExecAlarm();
            } catch(InterruptedException e){
            }
        }
        
    }
    
    public static void ExecAlarm() throws Exception{
        _WarnSchedule.SetRealPath(_realPath);

        //現在時刻を取得
        java.util.Date a_date_start = new java.util.Date();
        SimpleDateFormat a_sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String a_sNowTime = a_sdf1.format(a_date_start);
        a_sNowTime = _sPrevTime;    //前回の終わり時刻

        //次の周期の時間
        Calendar a_cal1 = Calendar.getInstance();
        a_cal1.setTime(a_date_start);
        a_cal1.add(Calendar.SECOND, _period / 1000);
        String a_sPeriodTime = a_sdf1.format(a_cal1.getTime());

        //現在の曜日を取得
        String a_sNowWeek = "";
        Calendar a_cal = Calendar.getInstance();
        int a_week =a_cal.get(Calendar.DAY_OF_WEEK);
        switch (a_week){
            case 1:
                a_sNowWeek = "00000010";
                break;
            case 2:
                a_sNowWeek = "00000100";
                break;
            case 3:
                a_sNowWeek = "00001000";
                break;
            case 4:
                a_sNowWeek = "00010000";
                break;
            case 5:
                a_sNowWeek = "00100000";
                break;
            case 6:
                a_sNowWeek = "01000000";
                break;
            case 7:
                a_sNowWeek = "10000000";
                break;
        }

        String a_sql = "";
        //①開始時刻が現在のもの⇒開始時刻のみ
        //　毎日もしくは本日の曜日にチェックあり
        a_sql = 
            "SELECT *,SUBSTR(TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS'),12) AS EXECTIME FROM JOBSCHEDULE WHERE" +
            " ((cast(cast(execkind as integer) as bit(2)) & B'01')=B'01')" +
            " AND (" +
            " ((cast(cast(execdays as integer) as bit(8)) & B'00000001') = B'00000001')" +
            "  OR ((cast(cast(execdays as integer) as bit(8)) & B'" + a_sNowWeek + "') = B'" + a_sNowWeek + "')" +
            " )" +
            " AND (STARTTIME<'" + a_sPeriodTime + "') AND ((ENDTIME IS NULL) OR ((ENDTIME IS NOT NULL) AND (ENDTIME>'" + a_sPeriodTime + "')))" +
            " AND ('" + a_sNowTime.substring(0,11) + "'||SUBSTR(TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS'),12)>='" + a_sNowTime + "') AND ('" + a_sNowTime.substring(0,11) + "'||SUBSTR(TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS'),12)<'" + a_sPeriodTime + "')";
        //②開始時刻が現在のもの⇒開始時刻のみ
        //　指定曜日にチェックなし
        a_sql += " UNION " +
            "SELECT *,SUBSTR(TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS'),12) AS EXECTIME FROM JOBSCHEDULE WHERE" +
            " ((cast(cast(execkind as integer) as bit(2)) & B'01')=B'01')" +
            " AND (EXECDAYS=0)" +
            " AND ('" + a_sNowTime.substring(0,11) + "'||SUBSTR(TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS'),12)>='" + a_sNowTime + "') AND ('" + a_sNowTime.substring(0,11) + "'||SUBSTR(TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS'),12)<'" + a_sPeriodTime + "')" + 
            " AND (CAST(SUBSTR(TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS'),1,11) AS CHARACTER(12))='" + a_sNowTime.substring(0,10) + "')";
        //③終了時刻が現在のもの⇒終了時刻のみ
        //　毎日もしくは本日の曜日にチェックあり
        a_sql += " UNION " +
            "SELECT *,SUBSTR(TO_CHAR(ENDTIME,'YYYY-MM-DD HH24:MI:SS'),12) AS EXECTIME FROM JOBSCHEDULE WHERE" +
            " ((cast(cast(execkind as integer) as bit(2)) & B'10')=B'10')" +
            " AND (" +
            " ((cast(cast(execdays as integer) as bit(8)) & B'00000001') = B'00000001')" +
            "  OR ((cast(cast(execdays as integer) as bit(8)) & B'" + a_sNowWeek + "') = B'" + a_sNowWeek + "')" +
            " )" +
            " AND (ENDTIME>='" + a_sNowTime + "') AND ((STARTTIME IS NULL) OR ((STARTTIME IS NOT NULL) AND (STARTTIME<='" + a_sNowTime + "')))" +
            " AND ('" + a_sNowTime.substring(0,11) + "'||SUBSTR(TO_CHAR(ENDTIME,'YYYY-MM-DD HH24:MI:SS'),12)>='" + a_sNowTime + "') AND ('" + a_sNowTime.substring(0,11) + "'||SUBSTR(TO_CHAR(ENDTIME,'YYYY-MM-DD HH24:MI:SS'),12)<'" + a_sPeriodTime + "')";
        //④終了時刻が現在のもの⇒終了時刻のみ
        //　指定曜日にチェックなし
        a_sql += " UNION " +
            "SELECT *,SUBSTR(TO_CHAR(ENDTIME,'YYYY-MM-DD HH24:MI:SS'),12) AS EXECTIME FROM JOBSCHEDULE WHERE" +
            " ((cast(cast(execkind as integer) as bit(2)) & B'10')=B'10')" +
            " AND (EXECDAYS=0)" +
            " AND ('" + a_sNowTime.substring(0,11) + "'||SUBSTR(TO_CHAR(ENDTIME,'YYYY-MM-DD HH24:MI:SS'),12)>='" + a_sNowTime + "') AND ('" + a_sNowTime.substring(0,11) + "'||SUBSTR(TO_CHAR(ENDTIME,'YYYY-MM-DD HH24:MI:SS'),12)<'" + a_sPeriodTime + "')" +
            " AND (CAST(SUBSTR(TO_CHAR(ENDTIME,'YYYY-MM-DD HH24:MI:SS'),1,11) AS CHARACTER(12))='" + a_sNowTime.substring(0,10) + "')";
        
        a_sql = "SELECT t1.* FROM (" + a_sql + ") t1 ORDER BY EXECTIME"; 
        a_sql += ";";
        _WarnSchedule._Environ._MyLogger.finest("[ExecAlarm]" + a_sql);
        
        //[2016.06.02]現地で動作しない件の調査↓
        //a_sql = "SELECT jobid,cast(cast(execkind as integer) as bit(2)) as execkind FROM JOBSCHEDULE ORDER BY JOBID;";
        //a_sql = "SELECT jobid,TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS') as starttime FROM JOBSCHEDULE ORDER BY JOBID;";
//        a_sql =
//            "SELECT *,SUBSTR(TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS'),12) AS EXECTIME FROM JOBSCHEDULE WHERE" +
//            " ((cast(cast(execkind as integer) as bit(2)) & B'01')=B'01')" +
//            " AND (EXECDAYS=0)" +
//            " AND ('2016-06-02 '||SUBSTR(TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS'),12)>='2016-06-02 16:27:54') AND ('2016-06-02 '||SUBSTR(TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS'),12)<'2016-06-02 16:28:04')" + 
//            " AND (SUBSTR(TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS'),0,11)='2016-06-02')";
//        a_sql = "SELECT jobid,SUBSTR(TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS'),12) as starttime FROM JOBSCHEDULE ORDER BY JOBID;";
//        a_sql = "SELECT jobid,STARTTIME FROM JOBSCHEDULE WHERE";
//        //a_sql += " (EXECDAYS=0)";
//        //a_sql += " AND ('2016-06-02 '||SUBSTR(TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS'),12)>='2016-06-02 16:27:54')";
//        //a_sql += " AND ('2016-06-02 '||SUBSTR(TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS'),12)<'2016-06-02 16:28:04')";
//        a_sql += " (SUBSTR(TO_CHAR(STARTTIME,'YYYY/MM/DD HH24:MI:SS'),0,11)='2016/06/02')";
//        a_sql += " ORDER BY JOBID;";
//        //a_sql = "SELECT jobid,SUBSTR(TO_CHAR(STARTTIME,'YYYY-MM-DD HH24:MI:SS'),1,11) as starttime FROM JOBSCHEDULE ORDER BY JOBID;";
//        _WarnSchedule._Environ._MyLogger.info("[ExecAlarm]" + a_sql);
        //[2016.06.02]現地で動作しない件の調査↑

        //h_kind⇒1：発報試験、0：スケジューラ
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;

        try{
            Class.forName (_WarnSchedule._db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_WarnSchedule._db_url, _WarnSchedule._db_user, _WarnSchedule._db_pass);
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();

            try{
                //[2016.06.02]現地で動作しない件の調査↓
//                while(a_rs.next()){
//                    _Environ._MyLogger.info("[ExecAlarm]jobid=" + _Environ.ExistDBString(a_rs,"jobid"));
//                    //_Environ._MyLogger.info("[ExecAlarm]execkind=" + _Environ.ExistDBString(a_rs,"execkind"));
//                    _Environ._MyLogger.info("[ExecAlarm]starttime=" + _Environ.ExistDBString(a_rs,"starttime"));
//                }
                //[2016.06.02]現地で動作しない件の調査↓
                _WarnSchedule.make_AlarmFile(0,a_rs);
            } catch (Exception e){
                _WarnSchedule._Environ._MyLogger.severe("[ExecAlarm]" + e.getMessage());
            }

            a_rs.close();
            a_ps.close();

        } catch (SQLException e) {
            _WarnSchedule._Environ._MyLogger.severe("[ExecAlarm]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _WarnSchedule._Environ._MyLogger.severe("[ExecAlarm]" + ex.getMessage());
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

        _WarnSchedule._Environ._MyLogger.info("*** ExecAlarm is finished. ***");
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

