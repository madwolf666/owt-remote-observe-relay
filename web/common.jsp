<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page import="java.util.ArrayList,java.io.*,java.util.*,java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
//パスを取得
String GetMainPath(
    String h_script_name
    ){
    String[] a_pathResult = h_script_name.split("/");
    int idx = a_pathResult.length;
    String a_mainPath = "";
    for( int i = 1; i < a_pathResult.length - 1; i++ ) {
        if ( i == 1 ) {
            a_mainPath += a_pathResult[i];
        }
        else {
            a_mainPath += ( "/" + a_pathResult[i] );
        }
    }
    
    return a_mainPath;
}    

//セッション変数取得
String GetSessionValue(
    Object h_session
    ){
    String a_sRet = "";
    if (h_session == null){
        a_sRet = "";
    }else{
        a_sRet = String.valueOf(h_session);
    }
    return a_sRet;
}

//copyright
String OutCopyRight(){
    String a_sRet = "";
    a_sRet += "<div id=\"hpb-footer\">";
    a_sRet += "  <div id=\"hpb-footerMain\">";
    a_sRet += "    <p>copyright&#169;2017&#160;OKI&#160;Wintech&#160;all&#160;rights&#160;reserved.</p>";
    a_sRet += "  </div>";
    a_sRet += "</div>";

    return a_sRet;
}

//入力項目の定義
static int COLUMN_DEF_FIELD = 0;        //入力フィールド
static int COLUMN_DEF_NAME = 1;         //カラム名（複数）
static int COLUMN_DEF_TABLE_NAME = 2;   //テーブル名（複数）
static int COLUMN_DEF_NESS = 3;         //必須有無（a/n/y）
static int COLUMN_DEF_TYPE = 4;         //型（n/s/time/date）
static int COLUMN_DEF_TIME = 5;         //時刻指定（n/y）
static int COLUMN_DEF_LENGTH = 6;       //MAX桁
static int COLUMN_DEF_PULLDOWN = 7;     //プルダウン（n/y）
static int COLUMN_DEF_COMMENT = 8;      //コメント
static int COLUMN_DEF_INIT = 9;         //初期値
static int COLUMN_DEF_ACTION = 10;      //入力時の動作

//DBテーブルのキー定義
static int DB_TABLE_KEY_DEF_NAME = 0;           //キーのカラム名
static int DB_TABLE_KEY_DEF_TYPE = 1;           //型（n/s）
static int DB_TABLE_KEY_DEF_EDITABLE = 2;       //修正可否（n/y）
static int DB_TABLE_KEY_DEF_AUTOINCREMENT = 3;  //自動採番（n/y）

//リスト表示
static int SHOWLIST_COLUMN_NAME = 0;        //カラム名
static int SHOWLIST_BUTTON_NAME = 1;        //ボタン名
static int SHOWLIST_FIND_KEY_NAME = 2;      //検索キー
static int SHOWLIST_SELECT_KEY_NAME = 3;    //選択キー
static int SHOWLIST_FIND_SQL = 4;           //取得SQL
static int SHOWLIST_ITEM_NAME = 5;          //項目表示

//JavaScript生成
String g_JScript_Val_Auto = "";  //javascript変数の出力
String g_JScript_Program = "";  //javascriptコードの出力
String g_JScript_IsNumeric = "";
String g_JScript_IsRequired = "";
String g_Post_Data = "";

String g_JScript_Val_Auto_Plural = "";  //javascript変数の出力
String g_JScript_Program_Plural = "";  //javascriptコードの出力
String g_JScript_IsNumeric_Plural = "";
String g_JScript_IsRequired_Plural = "";
String g_Post_Data_Plural = "";

//定義情報の読み込み
ArrayList<String> GetDef_Field(
    String h_fname
    ){
    ArrayList<String> a_field_def = null;
    try{
        FileInputStream a_fs = new FileInputStream(h_fname);
        InputStreamReader a_isr = new InputStreamReader(a_fs, "UTF8");
        BufferedReader a_br = new BufferedReader(a_isr);
        String a_line = "";
        int a_rec = 0;
        while ((a_line = a_br.readLine())!=null){
            //1行目はタイトル
            if (a_rec > 0){
                a_field_def.add(a_line);
            }else{
                a_field_def = new ArrayList<String>();
            }
            a_rec++;
        }
        a_br.close();
        a_isr.close();
        a_fs.close();

    }catch(Exception e){

    }

    return a_field_def;
}

//プルダウン表示
String[] GetDef_PullDown(
    String h_pulldown,
    String h_col_name
    ){
    String[] a_pull_def = null;
    try{
        FileInputStream a_fs = new FileInputStream(h_pulldown);
        InputStreamReader a_isr = new InputStreamReader(a_fs, "UTF8");
        BufferedReader a_br = new BufferedReader(a_isr);
        String a_line = "";
        int a_rec = 0;
        while ((a_line = a_br.readLine())!=null){
            //1行目はタイトル
            if (a_rec > 0){
                a_pull_def = a_line.split("\t");
                if (a_pull_def[0].equals(h_col_name) == true){
                    break;
                }
            }
            a_rec++;
        }
        a_br.close();
        a_isr.close();
        a_fs.close();

    }catch(Exception e){

    }

    return a_pull_def;
}

//リスト表示
String[] GetDef_ShowList(
    String h_showlist,
    String h_col_name
    ){
    String[] a_show_def = null;
    try{
        FileInputStream a_fs = new FileInputStream(h_showlist);
        InputStreamReader a_isr = new InputStreamReader(a_fs, "UTF8");
        BufferedReader a_br = new BufferedReader(a_isr);
        String a_line = "";
        int a_rec = 0;
        while ((a_line = a_br.readLine())!=null){
            //1行目はタイトル
            if (a_rec > 0){
                a_show_def = a_line.split("\t");
                if (a_show_def[SHOWLIST_COLUMN_NAME].equals(h_col_name) == true){
                    break;
                }
            }
            a_rec++;
        }
        a_br.close();
        a_isr.close();
        a_fs.close();

    }catch(Exception e){

    }

    return a_show_def;
}

//複数入力表示
ArrayList<String> GetDef_Plurals(
    String h_plural
    ){
    ArrayList<String> a_plural_def = null;
    try{
        FileInputStream a_fs = new FileInputStream(h_plural);
        InputStreamReader a_isr = new InputStreamReader(a_fs, "UTF8");
        BufferedReader a_br = new BufferedReader(a_isr);
        String a_line = "";
        int a_rec = 0;
        while ((a_line = a_br.readLine())!=null){
            //1行目はタイトル
            if (a_rec > 0){
                a_plural_def.add(a_line);
            }else{
                a_plural_def = new ArrayList<String>();
            }
            a_rec++;
        }
        a_br.close();
        a_isr.close();
        a_fs.close();

    }catch(Exception e){

    }

    return a_plural_def;
}

//特殊文字の変換
// HTMLエンコードする
String HtmlEncode(String original)
{
    StringBuffer retVal = new StringBuffer();

    // 1文字ずつループ処理する
    for (int i = 0; i < original.length(); i++)
    {
        char c = original.charAt(i);
        switch (c)
        {
            case '<':
                retVal.append("&lt;");
                break;
            case '>':
                retVal.append("&gt;");
                break;
            case '&':
                retVal.append("&#38;");
                break;
            case '\"':
                retVal.append("&#34;");
                break;
            case '\'':
                retVal.append("&#39;");
                break;
            default:
                retVal.append(c);
        }
    }

    return retVal.toString();
}

//  h_isEdit    true:編集、false:参照
//  h_act   
//  h_coldef    入力項目の定義
//  h_key       キーのカラム名：型：修正可否
//  h_pulldown  プルダウン定義ファイル名
//  h_showlist  リスト表示定義ファイル名
//  h_val       DB登録値
String Make_Tag_Mnt(
    String h_envPath,
    boolean h_isMain,
    boolean h_isEdit,
    boolean h_isFirst,
    String h_act,
    String[] h_coldef,
    String[] h_key,
    String h_pulldown,
    String h_showlist,
    String h_val
    ){
    String a_sRet = "";
    String a_isTime = "";
    boolean a_IsNess = false;

    //カラム名を取得：0番目をメインとする
    String[] a_colNames = h_coldef[COLUMN_DEF_NAME].split(":");
    String a_colName = a_colNames[0];
    String a_field = h_coldef[COLUMN_DEF_FIELD];
    String a_comment = h_coldef[COLUMN_DEF_COMMENT];

    if (h_act.equals("l") == false){
        //複数入力
        if (h_coldef[COLUMN_DEF_PULLDOWN].indexOf("p") >= 0){
            String a_plural = h_envPath + a_field + ".def";
            ArrayList<String> a_plurals = GetDef_Plurals(a_plural);
            String[] a_plural_datas = h_val.split("\b\b\b");
            if (a_plurals != null){
                String a_table_id = "tbl_list_" + a_field;
                a_sRet += "<table id='" + a_table_id + "' border='1' cellspacing='0' cellpadding='0' style='width:auto;'>";

                String a_append = "";
                for (int a_iCnt=0; a_iCnt<a_plurals.size(); a_iCnt++){
                    String[] a_split = a_plurals.get(a_iCnt).split("\t");
                    String[] a_split2 = a_split[COLUMN_DEF_FIELD].split(":");
                    a_sRet += "<tr>";
                    if (a_iCnt == 0){
                        //ヘッダー
                        if (a_split2.length > 1){
                            a_append = a_split2[0];
                            for (int a_iCnt2=1; a_iCnt2<a_split2.length; a_iCnt2++){
                                a_sRet += "<td bgcolor='#003366' style='text-align:center;'><font color='#ffffff'>" + a_split2[a_iCnt2] + "</font></td>";
                            }
                            if (a_append.indexOf("a") >= 0){
                                a_sRet += "<td bgcolor='#003366' style='text-align:center;'><font color='#ffffff'></td>";
                            }
                        }
                    }else{
                        String[] a_plural_data  = null;
                        if (a_plural_datas.length > (a_iCnt - 1)){
                            a_plural_data  = a_plural_datas[a_iCnt - 1].split("\b\b");
                        }
                        String[] a_now_split = null;
                        for (int a_iCnt2=0; a_iCnt2<a_split2.length; a_iCnt2++){
                            //該当番目の定義を組み立て
                            a_now_split = new String[COLUMN_DEF_ACTION + 1];
                            for (int a_iCnt3=0; a_iCnt3<COLUMN_DEF_ACTION + 1; a_iCnt3++){
                                String[] a_split3 = a_split[a_iCnt3].split(":");
                                a_now_split[a_iCnt3] = "";
                                if (a_split3.length > a_iCnt2){
                                    a_now_split[a_iCnt3] = a_split3[a_iCnt2];
                                }
                            }
                            if (a_now_split[COLUMN_DEF_COMMENT].equals("") == false){
                                a_sRet += "<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>" + a_now_split[COLUMN_DEF_COMMENT] + "</font>";
                            }else{
                            }
                            if (a_now_split[COLUMN_DEF_NESS].indexOf("y")>=0){
                                a_sRet += "<font color='#ffff00'>*</font>";
                            }
                            if (a_now_split[COLUMN_DEF_COMMENT].equals("") == false){
                                a_sRet += "</td>";
                            }else{
                            }

                            String a_val = "";
                            if (a_plural_data != null){
                                for (int a_iCnt3=0; a_iCnt3<a_plural_data.length; a_iCnt3++){
                                    String[] a_data = a_plural_data[a_iCnt3].split("\b");
                                    if (a_data[0].equals(a_now_split[COLUMN_DEF_FIELD]) == true){
                                        if (a_data.length > 1){
                                            a_val = a_data[1].trim();
                                        }
                                        break;
                                    }
                                }
                            }
                            //初期値
                            if (h_isFirst == true){
                                a_val = a_now_split[COLUMN_DEF_INIT];
                            }
                            a_sRet += "<td bgcolor='transparent' style='text-align:left;'>" + Make_Tag_Mnt(h_envPath, h_isMain, h_isEdit, h_isFirst, h_act, a_now_split, h_key, h_pulldown, h_showlist, a_val) + "</font></td>";
                        }
                        if (a_append.indexOf("a") >= 0){
                            a_sRet += "<td bgcolor='transparent' style='text-align:center;'>";
                            a_sRet += "<div name='" + a_table_id + "_div" + a_iCnt + "' id='" + a_table_id + "_div" + a_iCnt + "'>";
                            a_sRet += "<input type='button' value='追加' onclick='append_field(\"" + a_table_id + "\",\"" + a_iCnt + "\",\"" + a_split[COLUMN_DEF_NAME] + "\");'>";
                            a_sRet += "</div>";
                            a_sRet += "</td>";
                        }
                    }
                    a_sRet += "</tr>";
                }

                a_sRet += "</table>";
            }
            return a_sRet;
        }
    }

    //String a_val = String.valueOf(h_val);

    if (h_coldef[COLUMN_DEF_NESS].equals("a")){
        //自動更新
        if (h_isMain == true){
            g_JScript_Val_Auto += "," + a_colName;
        }else{
            g_JScript_Val_Auto_Plural += "," + a_colName;
        }
        if (h_val.equals("") == true){
            a_sRet = "<font color='#ff0000'>自動付与されます</font>";
        }else{
            a_sRet = h_val;
        }
        if (h_act.equals("l") == false){
            a_sRet += "<input type='hidden' name='"+ a_field + "' id='" + a_field + "' style='' value='" + h_val + "'>";
        }
        if (h_isMain == true){
            g_Post_Data += "            ,'" + a_field + "': $('#" + a_field + "').val()";
        }else{
            g_Post_Data_Plural += "            ,'" + a_field + "': $('#" + a_field + "').val()";
        }
        return a_sRet;
    }else if (h_coldef[COLUMN_DEF_NESS].equals("y")){
        //必須入力
        a_IsNess = true;
    }else{
    }
    a_isTime = h_coldef[COLUMN_DEF_TIME];

    if (a_IsNess == true){
        if (h_isMain == true){
            g_JScript_IsRequired += "    if (!check_IsRequired(\"#" + a_field + "\", \"" + a_comment + "は必須入力です！\")){return false;}";
        }else{
            g_JScript_IsRequired_Plural += "    if (!check_IsRequired(\"#" + a_field + "\", \"" + a_comment + "は必須入力です！\")){return false;}";
        }
    }

    if (h_coldef[COLUMN_DEF_PULLDOWN].indexOf("n") >= 0){
        if (h_coldef[COLUMN_DEF_TYPE].indexOf("n") >= 0){
            //data_precisionが整数桁、data_scaleが小数桁
            a_sRet += Make_Input_Tag_Mnt_Numeric(h_isEdit, h_act, h_coldef, "width:100%;", h_val);
            //g_JScript_IsNumeric += "    alert($(\"#" + h_column[1] + "\"));";
            if (h_isMain == true){
                g_JScript_IsNumeric += "    if (!check_IsNumeric(\"#" + a_field + "\", \"" + a_comment + "は数値入力です！\")){return false;}";
            }else{
                g_JScript_IsNumeric_Plural += "    if (!check_IsNumeric(\"#" + a_field + "\", \"" + a_comment + "は数値入力です！\")){return false;}";
            }
        }else if (h_coldef[COLUMN_DEF_TYPE].indexOf("s") >= 0){
            //data_lengthがMAX桁数
            a_sRet += Make_Input_Tag_Mnt_String(h_isEdit, h_act, h_coldef, "width:100%;", h_val);
        }else if (h_coldef[COLUMN_DEF_TYPE].indexOf("time") >= 0){
            //ミリ秒まで扱う？
            a_sRet += Make_Input_Tag_Mnt_TimeStamp(h_isMain, h_isEdit, h_act, h_coldef, "width:100%;", a_isTime, h_val);
        }else if (h_coldef[COLUMN_DEF_TYPE].indexOf("date") >= 0){
            a_sRet += Make_Input_Tag_Mnt_Date(h_isMain, h_isEdit, h_act, h_coldef, "width:100%;", a_isTime, h_val);
        }
    }else if (h_coldef[COLUMN_DEF_PULLDOWN].indexOf("y") >= 0){
        //該当テーブルの定義情報を読み込む
        String [] a_pull_def = GetDef_PullDown(h_pulldown, a_colName);
        if (a_pull_def != null){
            a_sRet += Make_Input_Tag_Mnt_Option(h_isEdit, h_act, h_coldef, a_pull_def, "width:100%;", h_val);
        }
    }else if (h_coldef[COLUMN_DEF_PULLDOWN].indexOf("l") >= 0){
        //該当テーブルの定義情報を読み込む
        String[] a_show_def = GetDef_ShowList(h_showlist, a_colName);
        if (a_show_def != null){
            a_sRet += Make_Input_Tag_Mnt_ShowList(h_isEdit, h_coldef, a_show_def, "width:auto;", h_val);
        }   
    }else if (h_coldef[COLUMN_DEF_PULLDOWN].equals("") == true){
        if (h_isEdit == false){
            a_sRet += h_val;
        }
        if (h_act.equals("l") == false){
            a_sRet += "<input type='";
            if (h_isEdit == true){
                a_sRet += "text";
            }else{
                a_sRet += "hidden";
            }
            a_sRet += "' name='"+ a_field + "' id='" + a_field + "' style='width:200px;' value='" + h_val + "' readonly='true'>";
        }
    }

    if (h_isMain == true){
        g_Post_Data += "            ,'" + a_field + "': $('#" + a_field + "').val()";
    }else{
        g_Post_Data_Plural += "            ,'" + a_field + "': $('#" + a_field + "').val()";
    }
    
    /*
〇nullable
　'n'の文字が検出された場合は
　　必須入力

〇constraint_type
　'p'もしくは'u'の文字が検出された場合
　　二重登録不可

〇column_comment
　空の場合は、column_nameそのものを出力
    */
    
    return a_sRet; 
}

//inputタグ生成（数値）
String Make_Input_Tag_Mnt_Numeric(
    boolean h_isEdit,
    String h_act,
    String[] h_coldef,
    String h_style,
    String h_val
    ){
    String a_sRet = "";
    int a_max_len = 10;
    
    //カラム名を取得：0番目をメインとする
    String[] a_colNames = h_coldef[COLUMN_DEF_NAME].split(":");
    String a_colName = a_colNames[0];
    String a_field = h_coldef[COLUMN_DEF_FIELD];

    a_max_len = Integer.valueOf(h_coldef[COLUMN_DEF_LENGTH]);

    if (h_isEdit == false){
        a_sRet += h_val;
    }

    if (h_act.equals("l") == false){
        a_sRet += "<input type='";
        if (h_isEdit == true){
            a_sRet += "text";
        }else{
            a_sRet += "hidden";
        }
        a_sRet += "' name='"+ a_field + "' id='" + a_field + "' style='" + h_style + "' value='" + h_val + "' maxlength='" + String.valueOf(a_max_len) + "'";

        if (h_coldef.length > COLUMN_DEF_ACTION){
            if (h_coldef[COLUMN_DEF_ACTION].equals("") == false){
                a_sRet += " onKeyPress='" + h_coldef[COLUMN_DEF_ACTION] + ";'";
            }
        }

        a_sRet += ">";
    }

    return a_sRet;
}

//inputタグ生成（文字）
String Make_Input_Tag_Mnt_String(
    boolean h_isEdit,
    String h_act,
    String[] h_coldef,
    String h_style,
    String h_val
    ){
    String a_sRet = "";
    int a_max_len = 0;

    //カラム名を取得：0番目をメインとする
    String[] a_colNames = h_coldef[COLUMN_DEF_NAME].split(":");
    String a_colName = a_colNames[0];
    String a_field = h_coldef[COLUMN_DEF_FIELD];

    a_max_len = Integer.valueOf(h_coldef[COLUMN_DEF_LENGTH]);
    
    if (h_isEdit == false){
        a_sRet += h_val;
    }

    if (h_act.equals("l") == false){
        if (a_max_len > 256){
            if (h_isEdit == true){
                a_sRet += "<textarea name='"+ a_field + "' id='" + a_field + "' style='" + h_style + "height:100%;' rows=5>" + h_val + "</textarea>";
            }else{
                a_sRet += "<input type='hidden' name='"+ a_field + "' id='" + a_field + "' style='" + h_style + "height:100%;' value='" + h_val + "'>";
            }
        }else{
            a_sRet += "<input type='";
            if (h_isEdit == true){
                a_sRet += "text";
            }else{
                a_sRet += "hidden";
            }
            a_sRet += "' name='"+ a_field + "' id='" + a_field + "' style='" + h_style + "' value='" + h_val + "' maxlength='" + String.valueOf(a_max_len) + "'>";
        }
    }

    return a_sRet;
}

//inputタグ生成（タイムスタンプ）
String Make_Input_Tag_Mnt_TimeStamp(
    boolean h_isMain,
    boolean h_isEdit,
    String h_act,
    String[] h_coldef,
    String h_style,
    String h_isTime,
    String h_val
    ){
    String a_sRet = "";
    int a_max_len = 10 + 1 + 8;

    //カラム名を取得：0番目をメインとする
    String[] a_colNames = h_coldef[COLUMN_DEF_NAME].split(":");
    String a_colName = a_colNames[0];
    String a_field = h_coldef[COLUMN_DEF_FIELD];

    if (h_isEdit == false){
        a_sRet += h_val;
    }

    if (h_act.equals("l") == false){
        a_sRet += "<input type='";
        if (h_isEdit == true){
            a_sRet += "text";
        }else{
            a_sRet += "hidden";
        }
        a_sRet += "' name='"+ a_field + "' id='" + a_field + "' style='" + h_style + "' value='" + h_val + "' maxlength='";

        //g_JScript_Out += "$('#d_" + h_column[1] + "').datepicker({});";
        //g_JScript_Out += "$('#t_" + h_column[1] + "').datepicker({format:'H:i', datepicker:false, lang:'ja', step:1});";
        //g_JScript_Out += "$('#" + h_column[1] + "').datetimepicker({dateFormat:'Y/m/d', showSecond: true, timeFormat:'hh:mm:ss', lang:'ja', step:0.1});";
        if (h_isTime.equals("y")){
            a_max_len = 10 + 1 + 8;
            if (h_isMain == true){
                g_JScript_Program += "$('#" + a_field + "').datetimepicker({format:'Y/m/s H:i:s', lang:'ja', step:1});";
            }else{
                g_JScript_Program_Plural += "$('#" + a_field + "').datetimepicker({format:'Y/m/s H:i:s', lang:'ja', step:1});";
            }
        }else{
            a_max_len = 10;
            if (h_isMain == true){
                g_JScript_Program += "$('#" + a_field + "').datepicker({});";
            }else{
                g_JScript_Program_Plural += "$('#" + a_field + "').datepicker({});";
            }
        }

        a_sRet +=  String.valueOf(a_max_len) + "'>";
    }

    return a_sRet;
}

//inputタグ生成（日付）
String Make_Input_Tag_Mnt_Date(
    boolean h_isMain,
    boolean h_isEdit,
    String h_act,
    String[] h_coldef,
    String h_style,
    String h_isTime,
    String h_val
    ){
    String a_sRet = "";
    int a_max_len = 10;

    //カラム名を取得：0番目をメインとする
    String[] a_colNames = h_coldef[COLUMN_DEF_NAME].split(":");
    String a_colName = a_colNames[0];
    String a_field = h_coldef[COLUMN_DEF_FIELD];

    if (h_isEdit == false){
        a_sRet += h_val;
    }

    if (h_act.equals("l") == false){
        a_sRet += "<input type='";
        if (h_isEdit == true){
            a_sRet += "text";
        }else{
            a_sRet += "hidden";
        }
        a_sRet += "' name='"+ a_field + "' id='" + a_field + "' style='" + h_style + "' value='" + h_val + "' maxlength='";

        if (h_isTime.equals("y")){
            a_max_len = 10 + 1 + 8;
            if (h_isMain == true){
                g_JScript_Program += "$('#" + a_field + "').datetimepicker({format:'Y/m/s H:i:s', lang:'ja', step:1});";
            }else{
                g_JScript_Program_Plural += "$('#" + a_field + "').datetimepicker({format:'Y/m/s H:i:s', lang:'ja', step:1});";
            }
        }else{
            a_max_len = 10;
            if (h_isMain == true){
                g_JScript_Program += "$('#" + a_field + "').datepicker({});";
            }else{
                g_JScript_Program_Plural += "$('#" + a_field + "').datepicker({});";
            }
        }

        a_sRet +=  String.valueOf(a_max_len) + "'>";
    }

    return a_sRet;
}

//optionタグ生成
String Make_Input_Tag_Mnt_Option(
    boolean h_isEdit,
    String h_act,
    String[] h_coldef,
    String[] h_option,
    String h_style,
    String h_val
    ){
    String a_sRet = "";
    String[] a_split1 = null;
    String[] a_split2 = null;

    //カラム名を取得：0番目をメインとする
    String[] a_colNames = h_coldef[COLUMN_DEF_NAME].split(":");
    String a_colName = a_colNames[0];
    String a_field = h_coldef[COLUMN_DEF_FIELD];

    a_split1 = h_option[1].split(",");

    if (h_isEdit == false){
        for (int a_iCnt=0; a_iCnt<a_split1.length; a_iCnt++){
            a_split2 = a_split1[a_iCnt].split(":");
            if (a_split2[0].equals(h_val) == true){
                a_sRet += a_split2[1];
                break;
            }
        }
    }

    if (h_act.equals("l") == false){
        if (h_isEdit == true){
            a_sRet += "<select id='" + a_field + "' name='" + a_field + "' style='" + h_style + "'>";
            a_sRet += "<option value=''></option>";

            for (int a_iCnt=0; a_iCnt<a_split1.length; a_iCnt++){
                a_split2 = a_split1[a_iCnt].split(":");
                a_sRet += "<option value='" + a_split2[0] + "'";
                if (a_split2[0].equals(h_val) == true){
                    a_sRet += " selected";
                }
                a_sRet += ">" + a_split2[1] + "</option>";
            }
            a_sRet += "</select>";
        }else{
            a_sRet += "<input type='hidden' name='"+ a_field + "' id='" + a_field  + "' style='" + h_style + "height:100%;' value='" + h_val + "'>";
        }
    }

    return a_sRet;
}

//リスト表示生成
String Make_Input_Tag_Mnt_ShowList(
    boolean h_isEdit,
    String[] h_coldef,
    String[] h_option,
    String h_style,
    String h_val
    ){
    String a_sRet = "";
    //String[] a_split1 = null;
    //String[] a_split2 = null;
    int a_max_len = 0;

    //カラム名を取得：0番目をメインとする
    String[] a_colNames = h_coldef[COLUMN_DEF_NAME].split(":");
    String a_colName = a_colNames[0];
    String a_field = h_coldef[COLUMN_DEF_FIELD];

    a_max_len = Integer.valueOf(h_coldef[COLUMN_DEF_LENGTH]);
    
    if (h_isEdit == false){
        a_sRet += h_val;
    }

    if (a_max_len > 128){
        if (h_isEdit == true){
            a_sRet += "<textarea name='"+ a_field + "' id='" + a_field + "' style='" + h_style + "height:100%;' rows=5>" + h_val + "</textarea>";
        }else{
            a_sRet += "<input type='hidden' name='"+ a_field + "' id='" + a_field + "' style='" + h_style + "height:100%;' value='" + h_val + "'>";
        }
    }else{
        a_sRet += "<input type='";
        if (h_isEdit == true){
            a_sRet += "text";
        }else{
            a_sRet += "hidden";
        }
        a_sRet += "' name='"+ a_field + "' id='" + a_field + "' style='" + h_style + "' value='" + h_val + "' maxlength='" + String.valueOf(a_max_len) + "'>";
    }

    if (h_isEdit == true){
        a_sRet += "<input type='hidden' name='show_col_name_" + a_field + "' id='show_col_name_" + a_field + "' value='" + a_field + "'>";
        a_sRet += "<input type='hidden' name='show_find_key_" + a_field + "' id='show_find_key_" + a_field + "' value='" + h_option[SHOWLIST_FIND_KEY_NAME] + "'>";
        a_sRet += "&nbsp;&nbsp;<input type='button' value='" + h_option[SHOWLIST_BUTTON_NAME] + "' onclick='set_select_show_list(\"" + a_field + "\");make_show_list(1";
       // a_sRet += ",\"" + a_field + "\"";
        /*a_sRet += ",\"" + h_option[SHOWLIST_FIND_KEY_NAME] + "\"";
        a_sRet += ",\"" + h_option[SHOWLIST_SELECT_KEY_NAME] + "\"";
        a_sRet += ",\"" + h_option[SHOWLIST_FIND_SQL] + "\"";*/
        a_sRet += ");'>";
    }

    return a_sRet;
}

//入力確認
String Make_Confirm_Table_Mnt(String h_act, String h_idx){
    String a_sRet = "" ;
   
    a_sRet = "function confirm_table_mnt(){";
   
    /*
    a_sRet += " var a_act = $(\"#txt_act\").val();";
    a_sRet += " var a_idx = $(\"#txt_idx\").val();";
    */
    
    //数値入力チェック
    a_sRet += g_JScript_IsNumeric;
    
    //必須入力チェック
    //if (h_act.equals("n")){
        a_sRet += g_JScript_IsRequired;
    //}

    //a_sRet += "alert($(\"#ext\").val());";

    a_sRet += " $.ajax({";
    a_sRet += "     url: m_parentURL + \"confirm_table_mnt.jsp\",";
    a_sRet += "     type: 'POST',";
    a_sRet += "     dataType: \"html\",";
    a_sRet += "     async: false,";
    a_sRet += "     data:{";
    a_sRet += "         'ACT': \"" + h_act + "\"";
    a_sRet += "         ,'IDX': \"" + h_idx + "\"";
    
    //入力カラム数分、セットする
    a_sRet += g_Post_Data;
        
    a_sRet += "     },";
    a_sRet += "     success: function(data, dataType){";
    a_sRet += "         $(\"#my-pager\").empty().append(\"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\");";
    a_sRet += "         $(\"#my-list\").empty().append(data);";
    a_sRet += "         $(\"#new-mnt\").hide();";
    //a_sRet += "         //$(\"#reset-mnt\").show();";
    a_sRet += "         $(\"#confirm-mnt\").hide();";
    a_sRet += "         $(\"#entry-mnt\").show();";
    a_sRet += "         $(\"#back-mnt\").show();";
    a_sRet += "         $(\"#cancel-mnt\").show();";
    a_sRet += "         $(\"#list-mnt\").show();";
    a_sRet += "     },";
    a_sRet += "     error: function (XMLHttpRequest, textStatus, errorThrown) {";
    a_sRet += "         alert(errorThrown.message);";
    a_sRet += "     },";
    a_sRet += "     complete: function (data) {";
    a_sRet += "     }";
    a_sRet += " });";
    a_sRet += " return true;";
    a_sRet += "}";

    a_sRet += ";";
    a_sRet += "function back_table_mnt(){";
    a_sRet += " $.ajax({";
    a_sRet += "     url: m_parentURL + \"make_table_edit_mnt.jsp\",";
    a_sRet += "     type: 'POST',";
    a_sRet += "     dataType: \"html\",";
    a_sRet += "     async: false,";
    a_sRet += "     data:{";
    a_sRet += "         'ACT': \"" + h_act + "\"";
    a_sRet += "         ,'IDX': \"" + h_idx + "\"";
    a_sRet += "         ,'DB': '0'";
    
    //入力カラム数分、セットする
    a_sRet += g_Post_Data;
        
    a_sRet += "     },";
    a_sRet += "     success: function(data, dataType){";
    a_sRet += "         $(\"#my-pager\").empty().append(\"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\");";
    a_sRet += "         $(\"#my-list\").empty().append(data);";
    a_sRet += "         $(\"#new-mnt\").hide();";
    //a_sRet += "         //$(\"#reset-mnt\").show();";
    a_sRet += "         $(\"#confirm-mnt\").show();";
    a_sRet += "         $(\"#entry-mnt\").hide();";
    a_sRet += "         $(\"#back-mnt\").hide();";
    a_sRet += "         $(\"#cancel-mnt\").hide();";
    a_sRet += "         $(\"#list-mnt\").show();";
    a_sRet += "     },";
    a_sRet += "     error: function (XMLHttpRequest, textStatus, errorThrown) {";
    a_sRet += "         alert(errorThrown.message);";
    a_sRet += "     },";
    a_sRet += "     complete: function (data) {";
    a_sRet += "     }";
    a_sRet += " });";
    a_sRet += " return true;";
    a_sRet += "}";

    a_sRet += ";";
    a_sRet += "function cancel_table_mnt(){";
    //a_sRet += "alert('cancel_table_mnt');";
    a_sRet += "    make_table_edit_mnt('" + h_act + "', '" + h_idx + "');";
    a_sRet += "}";

   return a_sRet;
}

String Make_Entry_Table_Mnt(String h_act, String h_idx){
    String a_sRet = "" ;
   
    a_sRet = "function entry_table_mnt(){";
   
    /*
    a_sRet += " var a_act = $(\"#txt_act\").val();";
    a_sRet += " var a_idx = $(\"#txt_idx\").val();";
    */
    
    a_sRet += " if (!confirm(\"登録します。よろしいですか？\")){";
    a_sRet += "     return false;";
    a_sRet += " }";

    a_sRet += " $.ajax({";
    a_sRet += "     url: m_parentURL + \"entry_table_mnt.jsp\",";
    a_sRet += "     type: 'POST',";
    a_sRet += "     dataType: \"html\",";
    a_sRet += "     async: false,";
    a_sRet += "     data:{";
    a_sRet += "         'ACT': \"" + h_act + "\"";
    a_sRet += "         ,'IDX': \"" + h_idx + "\"";
    
    //入力カラム数分、セットする
    a_sRet += g_Post_Data;
        
    a_sRet += "     },";
    a_sRet += "     success: function(data, dataType){";
    a_sRet += "         var a_result = data.trim();";
    a_sRet += "         if (a_result != \"\"){";
    a_sRet += "             $(\"#my-pager\").empty().append(\"<font color='#ff0000'>\" + data + \"</font>\");";
    a_sRet += "         } else{";
    a_sRet += "             $(\"#my-pager\").empty();";
    a_sRet += "             alert(\"登録しました。\");";
    a_sRet += "             make_table_list_mnt(-1);";
    a_sRet += "         }";
    a_sRet += "     },";
    a_sRet += "     error: function (XMLHttpRequest, textStatus, errorThrown) {";
    a_sRet += "         alert(errorThrown.message);";
    a_sRet += "     },";
    a_sRet += "     complete: function (data) {";
    a_sRet += "     }";
    a_sRet += " });";
    a_sRet += " return true;";
    a_sRet += "}";
    
   return a_sRet;
}

String Make_Entry_Plural_Mnt(String h_mode, String h_is_edit, String h_user_code, String h_seq){
    String a_sRet = "" ;
   
    a_sRet = "function entry_plural_mnt(){";
   
    /*
    a_sRet += " var a_act = $(\"#txt_act\").val();";
    a_sRet += " var a_idx = $(\"#txt_idx\").val();";
    */
    //数値入力チェック
    a_sRet += g_JScript_IsNumeric_Plural;
    
    //必須入力チェック
    a_sRet += g_JScript_IsRequired_Plural;
    
    a_sRet += " if (!confirm(\"登録します。よろしいですか？\")){";
    a_sRet += "     return false;";
    a_sRet += " }";

    a_sRet += " $.ajax({";
    a_sRet += "     url: m_parentURL + \"entry_plural_mnt.jsp\",";
    a_sRet += "     type: 'POST',";
    a_sRet += "     dataType: \"html\",";
    a_sRet += "     async: false,";
    a_sRet += "     data:{";
    a_sRet += "         'mode': \"" + h_mode + "\"";
    a_sRet += "         ,'is_edit': \"" + h_is_edit + "\"";
    a_sRet += "         ,'user_code': \"" + h_user_code + "\"";
    a_sRet += "         ,'seq': \"" + h_seq + "\"";
    
    //入力カラム数分、セットする
    a_sRet += g_Post_Data_Plural;
        
    a_sRet += "     },";
    a_sRet += "     success: function(data, dataType){";
    a_sRet += "         var a_result = data.trim();";
    a_sRet += "         set_irms_plural(\"" + h_mode + "\",\"" + h_is_edit + "\",\"" + h_user_code + "\");";
    a_sRet += "     },";
    a_sRet += "     error: function (XMLHttpRequest, textStatus, errorThrown) {";
    a_sRet += "         alert(errorThrown.message);";
    a_sRet += "     },";
    a_sRet += "     complete: function (data) {";
    a_sRet += "     }";
    a_sRet += " });";

    a_sRet += " return true;";
    a_sRet += "}";
    
   return a_sRet;
}
//LTIC・TN拠点設定
String Make_Tag_Mnt_LTIC_TN(
    String h_envPath,
    boolean h_isEdit,
    boolean h_isFirst,
    String h_act,
    ArrayList<String> h_coldef,
    String[] h_key,
    String h_pulldown,
    String h_showlist
    ){
    String a_sRet = "";

    return a_sRet;
}

//ユーザ機器登録
String Make_Tag_Mnt_User_Machine(
    String h_envPath,
    boolean h_isEdit,
    boolean h_isFirst,
    String h_act,
    ArrayList<String> h_coldef,
    String[] h_key,
    String h_pulldown,
    String h_showlist
    ){
    String a_sRet = "";

    return a_sRet;
}

//機器コード設定
String Make_Tag_Mnt_Machine_Code(
    String h_envPath,
    boolean h_isEdit,
    boolean h_isFirst,
    String h_act,
    ArrayList<String> h_coldef,
    String[] h_key,
    String h_pulldown,
    String h_showlist
    ){
    String a_sRet = "";

    return a_sRet;
}

%>

    