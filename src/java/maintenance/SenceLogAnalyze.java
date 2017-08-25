/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maintenance;

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
import java.sql.Statement;

/**
 *
 * @author Chappy
 */
@ManagedBean(name="AnalyzeLog")
@RequestScoped
public class SenceLogAnalyze implements Serializable {
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
    public SenceLogAnalyze() {
        //_CheckAny = new CheckAny();
        _Environ = new Environ();

    }

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
    
    //[2017.08.04]
    public  ArrayList<String> GetDbColumns(String h_table) throws Exception{
        return _Environ.GetDbColumns(h_table);
    }

    //登録
    public String EnteryAnalyze(String h_table, ArrayList<String> h_columns, String[] h_column_split, String h_act, String h_idx, ArrayList<String> h_post_data) throws Exception{
        String a_sRet = "";
        Connection a_con = null;
        PreparedStatement a_ps = null;
        String a_sql = "";
        String a_sql_f = "";
        String a_sql_v = "";
        String a_sql_w = "";
        int a_index = 0;
        try{
            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_con.setAutoCommit(false);
            
            if (h_act.equals("n")){
                a_sql = "INSERT INTO " + h_table + "(";
                
                //自動採番のSQL組み立て
                for (int a_iCnt=0; a_iCnt<h_column_split.length; a_iCnt++){
                    String[] a_split = h_column_split[a_iCnt].split(":");
                    if (a_split[_Environ.DB_TABLE_KEY_DEF_AUTOINCREMENT].equals("y")){
                        //自動採番
                        if (a_sql_f != ""){
                            a_sql_f += ",";
                        }
                        a_sql_f += a_split[_Environ.DB_TABLE_KEY_DEF_NAME];
                        
                        if (a_sql_v != ""){
                            a_sql_v += ",";
                        }
                        if (_db_driver.equals("oracle.jdbc.driver.OracleDriver")){
                            a_sql_v += "(SELECT NVL(MAX(" + a_split[_Environ.DB_TABLE_KEY_DEF_NAME] + "),0)+1 FROM " + h_table + ")";
                        }else if (_db_driver.equals("org.postgresql.Driver")){
                            a_sql_v += "(SELECT COALESCE(MAX(" + a_split[_Environ.DB_TABLE_KEY_DEF_NAME] + "),0)+1 FROM " + h_table + ")";
                        }
                    }
                }
            }else{
                a_sql = "UPDATE " + h_table + " SET ";
                
                //WHERE句キーのSQL組み立て
                String[] a_split_idx = h_idx.split(",");
                for (int a_iCnt=0; a_iCnt<h_column_split.length; a_iCnt++){
                    String[] a_split = h_column_split[a_iCnt].split(":");
                    if (a_sql_w != ""){
                        a_sql_w += " AND ";
                    }
                    a_sql_w += "(" + a_split[_Environ.DB_TABLE_KEY_DEF_NAME] + "="; 
                    if (a_split[_Environ.DB_TABLE_KEY_DEF_TYPE].equals("n") == true){
                        //数値の場合
                        a_sql_w += a_split_idx[a_iCnt];
                    }else{
                        //数値以外の場合
                        a_sql_w += "'"  + a_split_idx[a_iCnt] + "'";
                    }
                    a_sql_w += ")";
                }
            }
        
            //POSTデータからSQLを組み立て
            a_index = 0;
            for (int a_iCnt=0; a_iCnt<h_post_data.size(); a_iCnt++){
                String[] a_split = h_post_data.get(a_iCnt).split("\t");
                if (a_split[1] != ""){
                    if (a_sql_f != ""){
                        a_sql_f += ",";
                    }
                    a_sql_f += a_split[0];  //カラム
                    for (int a_iCnt2=0; a_iCnt2<h_columns.size(); a_iCnt2++){
                        String[] a_split2 = h_columns.get(a_iCnt2).split("\t");
                        if (a_split2[_Environ.COLUMN_DEF_NAME].equals(a_split[0]) == true){
                            //カラム検出
                            a_index++;
                            if (a_sql_v != ""){
                                a_sql_v += ",";
                            }
                            
                            if (h_act.equals("n")){
                            }else{
                                a_sql_v += a_split[0] + "=";
                            }
                            
                            a_sql_v += "?";
                            break;
                        }
                    }
                }
            }

            if (h_act.equals("n")){
                a_sql += a_sql_f + ") VALUES(" + a_sql_v + ")";
            }else{
                a_sql += a_sql_v + " WHERE " + a_sql_w;
            }
            
            a_ps = a_con.prepareStatement(a_sql);

            //POSTデータからSQLを組み立て
            a_index = 0;
            for (int a_iCnt=0; a_iCnt<h_post_data.size(); a_iCnt++){
                String[] a_split = h_post_data.get(a_iCnt).split("\t");
                if (a_split[1] != ""){
                    for (int a_iCnt2=0; a_iCnt2<h_columns.size(); a_iCnt2++){
                        String[] a_split2 = h_columns.get(a_iCnt2).split("\t");
                        if (a_split2[_Environ.COLUMN_DEF_NAME].equals(a_split[0]) == true){
                            //カラム検出
                            a_index++;
                            if (a_split2[_Environ.COLUMN_DEF_TYPE].indexOf("num") >= 0){
                                //数値の場合
                                a_ps.setInt(a_index, Integer.valueOf(a_split[1]));
                            }else if (a_split2[_Environ.COLUMN_DEF_TYPE].indexOf("time") >= 0){
                                //timestamp
                                java.sql.Timestamp a_ts = null;
                                SimpleDateFormat a_sdf = null;
                                if (a_split[1].indexOf(" ") >= 0){
                                    a_sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                }else{
                                    a_sdf = new SimpleDateFormat("yyyy/MM/dd");
                                }
                                a_ts = new java.sql.Timestamp(a_sdf.parse(a_split[1]).getTime());
                                a_ps.setTimestamp(a_index, a_ts);
                            }else if (a_split2[_Environ.COLUMN_DEF_TYPE].indexOf("date") >= 0){
                                //date
                                java.sql.Date a_dt = null;
                                SimpleDateFormat a_sdf = null;
                                if (a_split[1].indexOf(" ") >= 0){
                                    a_sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                }else{
                                    a_sdf = new SimpleDateFormat("yyyy/MM/dd");
                                }
                                a_dt = new java.sql.Date(a_sdf.parse(a_split[1]).getTime());
                                a_ps.setDate(a_index, a_dt);
                            }else{
                                //文字列
                                a_ps.setString(a_index, a_split[1]);
                            }
                            break;
                        }
                    }
                }
            }

            if (a_index > 0){
                int a_i = a_ps.executeUpdate();
                a_con.commit();
            }else{
                a_sRet = "登録対象データがありません！";
            }
        } catch (SQLException e) {
            if (a_con != null){
                a_con.rollback();
            }
            _Environ._MyLogger.severe("[EnteryMnt]" + e.getMessage());
            a_sRet = e.getMessage();
        } catch (ClassNotFoundException ex) {
            if (a_con != null){
                a_con.rollback();
            }
            _Environ._MyLogger.severe("[EnteryMnt]" + ex.getMessage());
            a_sRet = ex.getMessage();
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }

        _Environ._MyLogger.info("*** EnteryAnalyze is finished. ***");
        return a_sRet;
    }

    //削除
    public void DeleteAnalyze() throws Exception{
        /*
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
        */
        _Environ._MyLogger.info("*** DeleteAnalyze is finished. ***");
    }
    
    //pagerの作成
    public String MakePagerAnalyze(String h_table, int h_pageNo) throws Exception{
        String[] a_table_split = null;
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";
        /*
        if (_db_driver.equals("oracle.jdbc.driver.OracleDriver")){
        }else if (_db_driver.equals("org.postgresql.Driver")){
        }
        */
        int a_iSum = -1;
        String a_sRet = "";
        try{
            a_table_split = h_table.split("\t");
            if (a_table_split.length<2){
                return a_sRet;
            }

            a_sql = "SELECT COUNT(*) AS REC_SUM FROM " + a_table_split[0];

            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            /*
            Statement a_stmt = a_con.createStatement();
            a_rs = a_stmt.executeQuery(a_sql);
            */
            a_ps = a_con.prepareStatement(a_sql);
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                a_iSum = a_rs.getInt("REC_SUM");
            }
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[MakePagerMnt]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[MakePagerMnt]" + ex.getMessage());
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
        
        a_sRet = _Environ.MakePager(7, a_iSum, h_pageNo, _max_line_page);
        _Environ._MyLogger.info("*** MakePagerAnalyze is finished. ***");
        
        return a_sRet;
    }
    
    public  ArrayList<String> FindAnalyze(String h_table, int h_pageNo, ArrayList<String> h_columns, ArrayList<String> h_coldefs) throws Exception{
        String[] a_table_split = null;
        String[] a_column_split = null;
        String[] a_any_split = null;
        String a_fields = "";
        String[] a_key = null;
        
        ArrayList<String> a_arrayRet = null;
        int a_iSum = 0;
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";
        //int a_iRet = 0;
        try{
            a_table_split = h_table.split("\t");
            if (a_table_split.length < 2){
                return a_arrayRet;
            }
            a_column_split = a_table_split[1].split(",");
            a_key = new String[a_column_split.length];
            for (int a_iCnt=0; a_iCnt<a_column_split.length; a_iCnt++){
                a_any_split = a_column_split[a_iCnt].split(":");
                if (a_iCnt > 0){
                    a_fields += ",";
                }
                a_fields += a_any_split[_Environ.DB_TABLE_KEY_DEF_NAME];
                a_key[a_iCnt] = a_any_split[_Environ.DB_TABLE_KEY_DEF_NAME];
            }
            
            a_sql = "SELECT COUNT(*) AS REC_SUM FROM " + a_table_split[0];
            
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
                
                if (_db_driver.equals("oracle.jdbc.driver.OracleDriver")){
                    a_sql = "SELECT * FROM " + a_table_split[0] + " ORDER BY " + a_fields;
                    a_sql = "SELECT s1.* FROM (" + a_sql + ") s1 WHERE (ROWNUM BETWEEN " + String.valueOf(a_start_idx) + " AND " + String.valueOf(a_end_idx) + ")";
                }else if (_db_driver.equals("org.postgresql.Driver")){
                    a_sql = "SELECT row_number() over(ORDER BY " + a_fields + "), * FROM " + a_table_split[0] + " ORDER BY " + a_fields;
                    a_sql = "SELECT * FROM (" + a_sql + ") s1 WHERE (s1.row_number BETWEEN " + String.valueOf(a_start_idx) + " AND " + String.valueOf(a_end_idx) + ")";
                }

                a_ps = a_con.prepareStatement(a_sql);
                a_rs = a_ps.executeQuery();
                while(a_rs.next()){
                    a_sRet = "";
                    for (int a_iCnt=0; a_iCnt<h_columns.size(); a_iCnt++){
                        String[] a_split2 = h_columns.get(a_iCnt).split("\t");
                        a_sVal = _Environ.ExistDBString(a_rs,a_split2[_Environ.COLUMN_DEF_NAME]);

                        if (a_sVal != ""){
                            if ((a_split2[_Environ.COLUMN_DEF_TYPE].indexOf("time") >= 0) || (a_split2[_Environ.COLUMN_DEF_TYPE].indexOf("date") >= 0)){
                                //日付
                                a_sVal = a_sVal.replace("-", "/");
                                for (int a_iCnt2=0; a_iCnt2<h_coldefs.size(); a_iCnt2++){
                                    String[] a_split3 = h_coldefs.get(a_iCnt2).split(":");
                                    if (a_split3[_Environ.COLUMN_DEF_NAME].equals(a_split2[_Environ.COLUMN_DEF_NAME]) == true){
                                        //カラム一致
                                        if (a_split3[_Environ.COLUMN_DEF_TIME].equals("n")){
                                            //時刻指定なし
                                            a_sVal = a_sVal.substring(0, 10);
                                        }
                                    }
                                }
                            }
                        }
                        
                        String a_sTmp1 = a_sVal;
                        if (a_split2[_Environ.COLUMN_DEF_NAME].equals(a_key[0]) == true){
                            a_sVal = "<a href=\"#\" onClick=\"make_table_edit_mnt('e','" + a_sTmp1;
                            for (int a_iCnt2=1; a_iCnt2<a_key.length; a_iCnt2++){
                                String a_sTmp2 = _Environ.ExistDBString(a_rs,a_key[a_iCnt2]);
                                a_sVal += "," + a_sTmp2;
                            }
                            a_sVal += "');\">" + a_sTmp1 + "</a>";
                        }
                        if (a_iCnt>0){
                            a_sRet += "\t";
                        }
                        a_sRet += a_sVal;
                    }
                    a_arrayRet.add(a_sRet);
                }
                a_rs.close();
                a_ps.close();
            }
            
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[FindMnt]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[FindMnt]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        _Environ._MyLogger.info("*** FindAna;yze is finished. ***");
        
        return a_arrayRet;
    }
    
    public  ArrayList<String> GetAnalyze(String h_table, ArrayList<String> h_columns, ArrayList<String> h_coldefs, String h_idx) throws Exception{
        String[] a_table_split = null;
        String[] a_column_split = null;
        String[] a_any_split = null;
        String[] a_key = null;
        String[] a_type = null;

        ArrayList<String> a_arrayRet = null;
        a_arrayRet = new ArrayList<String>();
        String a_sVal = "";
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";
        try{
            a_table_split = h_table.split("\t");
            if (a_table_split.length<2){
                return a_arrayRet;
            }
            a_column_split = a_table_split[1].split(",");
            a_type = new String[a_column_split.length];
            a_key = new String[a_column_split.length];
            for (int a_iCnt=0; a_iCnt<a_column_split.length; a_iCnt++){
                a_any_split = a_column_split[a_iCnt].split(":");
                a_key[a_iCnt] = a_any_split[_Environ.DB_TABLE_KEY_DEF_NAME];
                a_type[a_iCnt] = a_any_split[_Environ.DB_TABLE_KEY_DEF_TYPE];
            }

            a_sql = "SELECT * FROM " + a_table_split[0] + " WHERE";
            for (int a_iCnt=0; a_iCnt<a_column_split.length; a_iCnt++){
                if (a_iCnt>0){
                     a_sql += " AND ";
                }
                a_sql += " (" + a_key[a_iCnt] + "=?)";
            }

            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_ps = a_con.prepareStatement(a_sql);

            String[] a_split = h_idx.split(",");
            for (int a_iCnt=0; a_iCnt<a_column_split.length; a_iCnt++){
                if (a_type[a_iCnt].equals("n") == true){
                    a_ps.setInt(a_iCnt+1, Integer.valueOf(a_split[a_iCnt]));
                } else{
                    a_ps.setString(a_iCnt+1, a_split[a_iCnt]);
                }
            }

            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                for (int a_iCnt=0; a_iCnt<h_columns.size(); a_iCnt++){
                    String[] a_split2 = h_columns.get(a_iCnt).split("\t");
                    a_sVal = _Environ.ExistDBString(a_rs,a_split2[_Environ.COLUMN_DEF_NAME]);
                    
                    if (a_sVal != ""){
                        if ((a_split2[_Environ.COLUMN_DEF_TYPE].indexOf("time") >= 0) || (a_split2[_Environ.COLUMN_DEF_TYPE].indexOf("date") >= 0)){
                            //日付
                            a_sVal = a_sVal.replace("-", "/");
                            for (int a_iCnt2=0; a_iCnt2<h_coldefs.size(); a_iCnt2++){
                                String[] a_split3 = h_coldefs.get(a_iCnt2).split(":");
                                if (a_split3[_Environ.COLUMN_DEF_NAME].equals(a_split2[_Environ.COLUMN_DEF_NAME]) == true){
                                    //カラム一致
                                    if (a_split3[_Environ.COLUMN_DEF_TIME].equals("n")){
                                        //時刻指定なし
                                        a_sVal = a_sVal.substring(0, 10);
                                    }
                                }
                            }
                        }
                    }
                    
                    a_arrayRet.add(a_sVal);
                }
            }
            a_rs.close();
            a_ps.close();
            
        } catch (SQLException e) {
            _Environ._MyLogger.severe("[GetMnt]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _Environ._MyLogger.severe("[GetMnt]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        _Environ._MyLogger.info("*** GetAnalyze is finished. ***");

        return a_arrayRet;
    }
}
