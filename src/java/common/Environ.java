/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    
    public String _db_driver = "";
    public String _db_server = "";
    public String _db_url = "";
    public String _db_user = "";
    public String _db_pass = "";
    public String _db_name = "";

    //入力項目の定義
    public static int COLUMN_DEF_NAME = 0;  //カラム名（複数）
    public static int COLUMN_DEF_TABLE_NAME = 1;//テーブル名（複数）
    public static int COLUMN_DEF_NESS = 2;   //必須有無（a/n/y）
    public static int COLUMN_DEF_TYPE = 3;  //型（n/s/time/date）
    public static int COLUMN_DEF_TIME = 4;//時刻指定（n/y）
    public static int COLUMN_DEF_LENGTH = 5;    //MAX桁
    public static int COLUMN_DEF_PULLDOWN = 6; //プルダウン（n/y）
    public static int COLUMN_DEF_COMMENT = 7; //コメント

    //DBテーブルのキー定義
    public static int DB_TABLE_KEY_DEF_NAME = 0;           //キーのカラム名
    public static int DB_TABLE_KEY_DEF_TYPE = 1;           //型（n/s）
    public static int DB_TABLE_KEY_DEF_EDITABLE = 2;       //修正可否（n/y）
    public static int DB_TABLE_KEY_DEF_AUTOINCREMENT = 3;  //自動採番（n/y）

    //リスト表示
    public static int SHOWLIST_COLUMN_NAME = 0;        //カラム名
    public static int SHOWLIST_BUTTON_NAME = 1;        //ボタン名
    public static int SHOWLIST_FIND_KEY_NAME = 2;      //検索キー
    public static int SHOWLIST_SELECT_KEY_NAME = 3;    //選択キー
    public static int SHOWLIST_FIND_SQL = 4;           //取得SQL
    public static int SHOWLIST_ITEM_NAME = 5;          //項目表示

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

            _db_driver = GetEnvironValue("mnt_db_driver");
            _db_server = GetEnvironValue("mnt_db_server");
            _db_url = GetEnvironValue("mnt_db_url");
            _db_user = GetEnvironValue("mnt_db_user");
            _db_pass = GetEnvironValue("mnt_db_pass");
            _db_name = GetEnvironValue("mnt_db_name");
            
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
        case 9: //リスト表示[2017.08.09]
            a_sFunc = "make_show_list";
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

    public  ArrayList<String> GetDbColumns(String h_table) throws Exception{
        String[] a_table_split = null;
        ArrayList<String> a_arrayRet = null;
        int a_iSum = 0;
        Connection a_con = null;
        PreparedStatement a_ps = null;
        ResultSet a_rs = null;
        String a_sql = "";
        if (_db_driver.equals("oracle.jdbc.driver.OracleDriver")){
            a_sql =
"SELECT DISTINCT" +
"  main.column_id," +
"  LOWER(main.column_name) AS column_name," +
"  LOWER(main.data_type) AS data_type," +
"  main.data_length," +
"  LOWER(main.nullable) AS nullable," +
"  main.data_precision," +
"  main.data_scale," +
"  LOWER(sub.constraint_type) AS constraint_type," +
"  cmn.comments AS column_comment" +
" FROM " +
"  user_tab_columns main" +
" LEFT JOIN" +
"  (SELECT" +
"    col.column_name, " +
"    con.constraint_type," +
"    con.last_change," +
"    con.index_name," +
"    con.table_name" +
"   FROM " +
"    user_constraints con" +
"   JOIN" +
"    user_cons_columns col " +
"   ON" +
"    con.constraint_name=col.constraint_name " +
"    AND con.table_name=col.table_name" +
"    AND con.constraint_type IN ('P', 'U')" +
"   ORDER BY " +
"    con.table_name) sub" +
" ON " +
"  sub.column_name=main.column_name" +
"  AND sub.table_name=main.table_name" +
" LEFT JOIN" +
"  user_col_comments cmn" +
" ON" +
"  main.column_name=cmn.column_name" +
"  AND main.table_name=cmn.table_name" +
" WHERE " +
"  main.table_name=UPPER(?)" +
" ORDER BY" +
"  column_id";
        }else if (_db_driver.equals("org.postgresql.Driver")){
            a_sql =
"SELECT DISTINCT" +
"  main.ordinal_position AS column_id," +
"  LOWER(main.column_name) AS column_name," +
"  LOWER(main.data_type) AS data_type," +
"  main.character_maximum_length AS data_length," +
"  LOWER(main.is_nullable) AS nullable," +
"  main.numeric_precision AS data_precision," +
"  main.numeric_scale AS data_scale," +
"  LOWER(sub.constraint_type) AS constraint_type," +
"  cmn.column_comment" +
" FROM" +
"  information_schema.columns main" +
" LEFT JOIN" +
"  (SELECT" +
"    col.column_name," +
"    con.constraint_type," +
"    con.table_name" +
"   FROM" +
"    information_schema.table_constraints con" +
"   JOIN" +
"    information_schema.constraint_column_usage col" +
"   ON" +
"    con.constraint_name=col.constraint_name" +
"    AND con.table_name=col.table_name" +
"    AND con.constraint_type IN ('PRIMARY KEY', 'UNIQUE')" +
"   ORDER BY" +
"    con.table_name) sub" +
"  ON" +
"  sub.column_name=main.column_name" +
"  AND sub.table_name=main.table_name" +
" LEFT JOIN" +
"  (" +
"   SELECT" +
"     ns.oid as schema_id" +
"    ,a.oid as table_id" +
"    ,attr.attnum as column_id" +
"    ,ns.nspname as schema_name" +
"    ,a.relname as table_name" +
"    ,des.description as table_comment" +
"    ,attr.attname as column_name" +
"    ,des2.description as column_comment" +
"   FROM" +
"    pg_catalog.pg_class a" +
"    INNER JOIN pg_catalog.pg_namespace ns ON a.relnamespace=ns.oid" +
"    LEFT JOIN pg_catalog.pg_description des ON a.oid=des.objoid AND des.objsubid=0" +
"    INNER JOIN pg_catalog.pg_attribute attr ON a.oid=attr.attrelid AND attr.attnum>0" +
"    LEFT JOIN pg_catalog.pg_description des2 ON a.oid=des2.objoid AND attr.attnum=des2.objsubid" +
"   WHERE" +
"     a.relkind IN ('r', 'v')" +
"     AND ns.nspname IN (?)" +
"  ) cmn" +
" ON" +
"  main.column_name=cmn.column_name" +
"  AND main.table_name=cmn.table_name" +
" WHERE " +
"  main.table_name=?" +
" ORDER BY" +
"  column_id";
        }
        //int a_iRet = 0;
        try{
            a_table_split = h_table.split("\t");
            if (a_table_split.length<2){
                return a_arrayRet;
            }

            Class.forName (_db_driver);
            // データベースとの接続
            a_con = DriverManager.getConnection(_db_url, _db_user, _db_pass);
            a_ps = a_con.prepareStatement(a_sql);
            if (_db_driver.equals("oracle.jdbc.driver.OracleDriver")){
                a_ps.setString(1, a_table_split[0]);
            }else if (_db_driver.equals("org.postgresql.Driver")){
                a_ps.setString(1, _db_name);
                a_ps.setString(2, a_table_split[0]);
            }
            a_rs = a_ps.executeQuery();
            while(a_rs.next()){
                String a_sVal = "";
                String a_sRet = "";
                if (a_iSum == 0){
                    a_arrayRet = new ArrayList<String>();
                }
                
                a_sVal = ExistDBString(a_rs,"column_id");
                a_sRet += a_sVal;
                a_sVal = ExistDBString(a_rs,"column_name");
                a_sRet += "\t" + a_sVal;
                a_sVal = ExistDBString(a_rs,"data_type");
                a_sRet += "\t" + a_sVal;
                a_sVal = ExistDBString(a_rs,"data_length");
                a_sRet += "\t" + a_sVal;
                a_sVal = ExistDBString(a_rs,"nullable");
                a_sRet += "\t" + a_sVal;
                a_sVal = ExistDBString(a_rs,"data_precision");
                a_sRet += "\t" + a_sVal;
                a_sVal = ExistDBString(a_rs,"data_scale");
                a_sRet += "\t" + a_sVal;
                a_sVal = ExistDBString(a_rs,"constraint_type");
                a_sRet += "\t" + a_sVal;
                a_sVal = ExistDBString(a_rs,"column_comment");
                a_sRet += "\t" + a_sVal;

                a_arrayRet.add(a_sRet);
                a_iSum++;
            }
            a_rs.close();
            a_ps.close();
            
        } catch (SQLException e) {
            _MyLogger.severe("[GetDbColumns]" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            _MyLogger.severe("[GetDbColumns]" + ex.getMessage());
        } finally{
            if (a_ps != null){
                a_ps.close();
            }
            if (a_con != null){
                a_con.close();
            }
        }
        _MyLogger.info("*** GetDbColumns is finished. ***");
        
        return a_arrayRet;
    }
}
