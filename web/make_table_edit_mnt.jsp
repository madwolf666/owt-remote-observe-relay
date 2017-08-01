<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page import="java.util.ArrayList,java.io.*,java.util.*,java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp" %>
<jsp:useBean id="Environ" scope="page" class="common.Environ" />
<jsp:useBean id="SetDB" scope="page" class="maintenance.SetDB" />
<%
    //パスを取得
    String SCRIPT_NAME = request.getServletPath();
    String a_mainPath = GetMainPath(SCRIPT_NAME);
    String a_realPath = application.getRealPath(a_mainPath);
    String a_envPath = "";

    Environ.SetRealPath(a_realPath);
    a_envPath = Environ.GetEnvironValue("mnt_env_path");

    //セッション変数
    String a_Mnt_Table = GetSessionValue(session.getAttribute("Mnt_Table"));
    ArrayList<String> a_columns = (ArrayList<String>)session.getAttribute("Mnt_Columns");
    String[] a_table_split = null;
    String[] a_column_split = null;
    ArrayList<String> a_coldefs = new ArrayList<String>();
    if (a_Mnt_Table.length() > 0){
        a_table_split = a_Mnt_Table.split("\t");
        a_column_split = a_table_split[1].split(",");
        
        //該当テーブルの定義情報を読み込む
        try{
            FileInputStream a_fs = new FileInputStream(a_envPath + a_table_split[0] + ".txt");
            InputStreamReader a_isr = new InputStreamReader(a_fs, "UTF8");
            BufferedReader a_br = new BufferedReader(a_isr);
            String a_line = "";
            int a_rec = 0;
            while ((a_line = a_br.readLine())!=null){
                //1行目はタイトル
                if (a_rec > 0){
                    a_coldefs.add(a_line);
                }
                a_rec++;
            }
            a_br.close();
            a_isr.close();
            a_fs.close();

        }catch(Exception e){

        }
    }
    
    //QueryStringの取得
    String a_ACT = request.getParameter("ACT");
    String a_IDX = request.getParameter("IDX");

    //Beansへの値引渡し
    SetDB.SetRealPath(a_realPath);

    //登録済データを取得
    ArrayList<String> a_arrayList = null;
    out.print("<input type='hidden' id='txt_idx' name='txt_idx' value='");
    if (a_ACT.equals("e") == true){
        a_arrayList = SetDB.GetMnt(a_Mnt_Table, a_columns, a_coldefs, a_IDX);
        out.print(a_IDX);
    }else{
    }
    out.print("'>");
    out.print("<input type='hidden' id='txt_act' name='txt_act' value='" + a_ACT + "'>");
    
    //out.print("<div id='my-result' style='font-color:#ff0000;'></div>");
    
    g_JScript_Val_Auto = "";
    //g_JScript_Val_Ness = "";
    g_JScript_Program = "";
    g_JScript_IsNumeric = "";
    g_JScript_IsRequired = "";
    g_Post_Data = "";
    
    out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0'>");
    //ヘッダ部
    out.print("<tr>");
    out.print("<td bgcolor='#003366' style='text-align:center;' nowrap=true><font color='#ffffff'>項目</font></td>");
    out.print("<td bgcolor='#ff9900' style='text-align:center; width:45%;'><font color='#ffffff'>登録データ</font></td>");
    out.print("<td bgcolor='#339900' style='text-align:center; width:45%;'><font color='#ffffff'>修正データ</font></td>");
    out.print("</tr>");

    //データ部
    if (a_arrayList != null){
        for (int a_iCnt=0; a_iCnt<a_columns.size(); a_iCnt++){
            String[] a_split = a_columns.get(a_iCnt).split("\t");
            out.print("<tr>");
            out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>" + a_split[1] + "</font></td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'>" + a_arrayList.get(a_iCnt) + "</font></td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'>" + Make_Tag_Mnt(a_ACT, a_split, a_column_split, a_coldefs) + "</font></td>");
            out.print("</tr>");
        }
    }else{
        for (int a_iCnt=0; a_iCnt<a_columns.size(); a_iCnt++){
            String[] a_split = a_columns.get(a_iCnt).split("\t");
            //splitは値が入っている所までしかlengthが返らない[2017.07.31]
            out.print("<tr>");
            out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>" + a_split[1] + "</font></td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'></font></td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'>" + Make_Tag_Mnt(a_ACT, a_split, a_column_split, a_coldefs) + "</font></td>");
            out.print("</tr>");
        }
    }
    
    out.print("</table>");

    out.print(OutCopyRight());
    
    //java script
    out.print("<script type='text/javascript'>");
    out.print("resize_tbl_list();");
    out.print("var g_val_auto = '" + g_JScript_Val_Auto + "';");
    //out.print("var g_val_ness = '" + g_JScript_Val_Ness + "';" );
    out.print(g_JScript_Program);
    //out.print("alert(g_val_auto);");
    //out.print("alert(g_val_ness);");
    out.print(Make_entry_table_mnt(a_ACT, a_IDX));
    out.print("</script>");
    
%>

<%!
String g_JScript_Val_Auto = "";  //javascript変数の出力
//String g_JScript_Val_Ness = "";  //javascript変数の出力
String g_JScript_Program = "";  //javascriptコードの出力
String g_JScript_IsNumeric = "";
String g_JScript_IsRequired = "";
String g_Post_Data = "";
    
String Make_Tag_Mnt(String h_act, String[] h_column, String[] h_key, ArrayList<String> h_coldef){
    //h_coulmn
    //0 カラムの順番
    //1 カラム名
    //2 型
    //3 長さ
    //4 NULLの許可
    //5 数値型の長さ（整数部分）
    //6 数値型の長さ（小数点）
    //7 制約（P、U）
    //8 コメント
    
    //h_key
    //キーのカラム名：型：修正可否

    //h_coldef
    //カラム名：必須有無（a/n/y）：時刻指定（n/y）
    
    String a_sRet = "";
    String a_isTime = "";
    boolean a_IsNess = false;

    for (int a_iCnt=0; a_iCnt<h_key.length; a_iCnt++){
        String[] a_key_split = h_key[a_iCnt].split(":");
        if (h_column[1].equals(a_key_split[0]) == true){
            //カラムが一致
            if (h_act.equals("n")){
                if (a_key_split[3].equals("y")){
                    //自動採番
                    return a_sRet;
                }
            }else{
                if (a_key_split[2].equals("n")){
                    //修正不可
                    return a_sRet;
                }
            }
        }
    }
    
    for (int a_iCnt=0; a_iCnt<h_coldef.size(); a_iCnt++){
        String[] a_coldef_split = h_coldef.get(a_iCnt).split(":");
        if (h_column[1].equals(a_coldef_split[0]) == true){
            //カラムが一致
            if (a_coldef_split[1].equals("a")){
                //自動更新
                g_JScript_Val_Auto += "," + h_column[1];
            }else if (a_coldef_split[1].equals("y")){
                //必須入力
                //g_JScript_Val_Ness += "," + h_column[1];
                a_IsNess = true;
            }else{
            }
            a_isTime = a_coldef_split[2];
            break;
        }
    }

    if (h_column.length > 4){
        //a_sRet += h_column[4] + "chappy";
        if (h_column[4].indexOf("n") >= 0){
            //NULLを許容しない
            //g_JScript_Val_Ness += "," + h_column[1];
            a_IsNess = true;
        }
    }
    
    if (a_IsNess == true){
        g_JScript_IsRequired += "    if (!check_IsRequired(\"#" + h_column[1] + "\", \"" + h_column[1] + "は必須入力です！\")){return false;}";
    }

    if (h_column[2].indexOf("num") >= 0){
        //data_precisionが整数桁、data_scaleが小数桁
        a_sRet += Make_Input_Tag_Mnt_Numeric(h_column, "width:100%;");
        //g_JScript_IsNumeric += "    alert($(\"#" + h_column[1] + "\"));";
        g_JScript_IsNumeric += "    if (!check_IsNumeric(\"#" + h_column[1] + "\", \"" + h_column[1] + "は数値入力です！\")){return false;}";
    }else if (h_column[2].indexOf("char") >= 0){
        //data_lengthがMAX桁数
        a_sRet += Make_Input_Tag_Mnt_String(h_column, "width:100%;");
    }else if (h_column[2].indexOf("time") >= 0){
        //ミリ秒まで扱う？
        a_sRet += Make_Input_Tag_Mnt_TimeStamp(h_column, "width:100%;", a_isTime);
    }else if (h_column[2].indexOf("date") >= 0){
        a_sRet += Make_Input_Tag_Mnt_Date(h_column, "width:100%;", a_isTime);
    }

    
    g_Post_Data += "            ,'" + h_column[1] + "': $('#" + h_column[1] + "').val()";
    
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
String Make_Input_Tag_Mnt_Numeric(String[] h_column, String h_style){
    //0 カラムの順番
    //1 カラム名
    //2 型
    //3 長さ
    //4 NULLの許可
    //5 数値型の長さ（整数部分）
    //6 数値型の長さ（小数点）
    //7 制約（P、U）
    //8 コメント

    String a_sRet = "";
    int a_max_len = 10;
    int a_itmp = 0;
    
    if (h_column.length > 5){
        if (h_column[5] != null){
            if (h_column[5].length() > 0){
                a_max_len = Integer.valueOf(h_column[5]);
            }
        }
        if (h_column[6] != null){
            if (h_column[6].length() > 0){
                a_itmp = Integer.valueOf(h_column[6]);
                if (a_itmp > 0){
                    a_max_len += a_itmp + 1;
                }
            }
        }
    }
    
    a_sRet = "<input type='text' name='"+ h_column[1] + "' id='" + h_column[1] + "' style='" + h_style + "' value='' maxlength='" + String.valueOf(a_max_len) + "'>";
    
    return a_sRet;
}

//inputタグ生成（文字）
String Make_Input_Tag_Mnt_String(String[] h_column, String h_style){
    //0 カラムの順番
    //1 カラム名
    //2 型
    //3 長さ
    //4 NULLの許可
    //5 数値型の長さ（整数部分）
    //6 数値型の長さ（小数点）
    //7 制約（P、U）
    //8 コメント

    String a_sRet = "";
    int a_max_len = 0;
    int a_itmp = 0;
    
    if (h_column.length > 3){
        if (h_column[3] != null){
            if (h_column[3].length() > 0){
                a_max_len = Integer.valueOf(h_column[3]);
            }
        }
    }
    
    if (a_max_len > 128){
        a_sRet = "<textarea name='"+ h_column[1] + "' id='" + h_column[1] + "' style='" + h_style + "height:100%;' rows=5></textarea>";
    }else{
        a_sRet = "<input type='text' name='"+ h_column[1] + "' id='" + h_column[1] + "' style='" + h_style + "' value='' maxlength='" + String.valueOf(a_max_len) + "'>";
    }
    return a_sRet;
}

//inputタグ生成（タイムスタンプ）
String Make_Input_Tag_Mnt_TimeStamp(String[] h_column, String h_style, String h_isTime){
    //0 カラムの順番
    //1 カラム名
    //2 型
    //3 長さ
    //4 NULLの許可
    //5 数値型の長さ（整数部分）
    //6 数値型の長さ（小数点）
    //7 制約（P、U）
    //8 コメント

    String a_sRet = "";
    int a_max_len = 10 + 1 + 8;
    int a_itmp = 0;
        
    a_sRet = "<input type='text' name='"+ h_column[1] + "' id='" + h_column[1] + "' style='" + h_style + "' value='' maxlength='" + String.valueOf(a_max_len) + "'>";

    //g_JScript_Out += "$('#d_" + h_column[1] + "').datepicker({});";
    //g_JScript_Out += "$('#t_" + h_column[1] + "').datepicker({format:'H:i', datepicker:false, lang:'ja', step:1});";
    //g_JScript_Out += "$('#" + h_column[1] + "').datetimepicker({dateFormat:'Y/m/d', showSecond: true, timeFormat:'hh:mm:ss', lang:'ja', step:0.1});";
    if (h_isTime.equals("y")){
        g_JScript_Program += "$('#" + h_column[1] + "').datetimepicker({format:'Y/m/s H:i:s', lang:'ja', step:1});";
    }else{
        g_JScript_Program += "$('#" + h_column[1] + "').datepicker({});";
    }

    return a_sRet;
}

//inputタグ生成（日付）
String Make_Input_Tag_Mnt_Date(String[] h_column, String h_style, String h_isTime){
    //0 カラムの順番
    //1 カラム名
    //2 型
    //3 長さ
    //4 NULLの許可
    //5 数値型の長さ（整数部分）
    //6 数値型の長さ（小数点）
    //7 制約（P、U）
    //8 コメント

    String a_sRet = "";
    int a_max_len = 10;
    int a_itmp = 0;
    
    a_sRet = "<input type='text' name='"+ h_column[1] + "' id='" + h_column[1] + "' style='" + h_style + "' value='' maxlength='" + String.valueOf(a_max_len) + "'>";

    if (h_isTime.equals("y")){
        g_JScript_Program += "$('#" + h_column[1] + "').datetimepicker({format:'Y/m/s H:i:s', lang:'ja', step:1});";
    }else{
        g_JScript_Program += "$('#" + h_column[1] + "').datepicker({});";
    }
    
    return a_sRet;
}

String Make_entry_table_mnt(String h_act, String h_idx){
    String a_sRet = "" ;
   
    a_sRet = "function entry_table_mnt(){";
   
    /*
    a_sRet += " var a_act = $(\"#txt_act\").val();";
    a_sRet += " var a_idx = $(\"#txt_idx\").val();";
    */
    
    //数値入力チェック
    a_sRet += g_JScript_IsNumeric;
    
    //必須入力チェック
    if (h_act.equals("n")){
        a_sRet += g_JScript_IsRequired;
    }
    
    a_sRet += " if (!confirm(\"登録します。よろしいですか？\")){";
    a_sRet += "     return false;";
    a_sRet += " }";

    a_sRet += " $.ajax({";
    a_sRet += "     url: m_parentURL + \"entry_mnt.jsp\",";
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
%>
    