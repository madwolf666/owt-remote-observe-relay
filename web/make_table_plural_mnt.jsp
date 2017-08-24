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
    String a_pulldown = "";
    String a_showlist = "";

    Environ.SetRealPath(a_realPath);
    a_envPath = Environ.GetEnvironValue("mnt_env_path");
    a_pulldown = a_envPath + Environ.GetEnvironValue("mnt_pulldown_info");
    a_showlist = a_envPath + Environ.GetEnvironValue("mnt_showlist_info");

    //セッション変数
    String a_Mnt_Table = GetSessionValue(session.getAttribute("Mnt_Table"));
    String[] a_table_split = null;
    String[] a_column_split = null;
    ArrayList<String> a_coldefs = null;
    if (a_Mnt_Table.equals("") == false){
        a_table_split = a_Mnt_Table.split("\t");
        a_column_split = a_table_split[1].split(",");
    }

    //QueryStringの取得
    String a_mode = request.getParameter("mode");
    String a_is_edit = request.getParameter("is_edit");
    String a_user_code = request.getParameter("user_code");
    String a_seq = request.getParameter("seq");
    if (a_seq == null){
        a_seq = "-1";
    }
    String a_user_ipaddr = "";

    //Beansへの値引渡し
    SetDB.SetRealPath(a_realPath);

    if (a_table_split[0].equals("irmsremotecustomer") == true){
        if (a_mode.equals("1") == true){
            a_coldefs = (ArrayList<String>)session.getAttribute("Mnt_Coldefs_LTIC_TN");
        }else if (a_mode.equals("2") == true){
            a_coldefs = (ArrayList<String>)session.getAttribute("Mnt_Coldefs_User_Machine");
        /*}else if (a_mode.equals("3") == true){
            a_coldefs = (ArrayList<String>)session.getAttribute("Mnt_Coldefs_Machine_Code");*/
        }
    }

    ArrayList<String>[] a_arrayLists = null;
    if (a_user_code.equals("") == false){
        if (a_mode.equals("1") == true){
            a_arrayLists = SetDB.GetPluralMnt("trunkandlticinformation\tusercode:s:n:y", a_coldefs, a_user_code);
        }else if (a_mode.equals("2") == true){
            a_arrayLists = SetDB.GetPluralMnt("usernode\tusercode:s:n:y", a_coldefs, a_user_code);
        /*}else if (a_mode.equals("3") == true){
            //a_arrayList = SetDB.GetPluralMnt("nodeinfo  nodecode:s:y:n", a_coldefs, a_user_code);*/
        }
    }else{
        if (a_mode.equals("1") == true){
            a_arrayLists = (ArrayList<String>[])session.getAttribute("Mnt_Data_LTIC_TN");
        }else if (a_mode.equals("2") == true){
            a_arrayLists = (ArrayList<String>[])session.getAttribute("Mnt_Data_User_Machine");
        /*}else if (a_mode.equals("3") == true){
            a_arrayLists = (ArrayList<String>[])session.getAttribute("Mnt_Data_Machine_Code");*/
        }
    }
    
    //一覧データを取得
    if (a_arrayLists != null){
        out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0' width='100%'>");
        //ヘッダ部
        out.print("<tr bgcolor='#003366'>");
        int a_field_num = 0;
        String[] a_items = null;
        //先頭は番号
        out.print("<td style='text-align:center;'><font color='#ffffff'>No.</font></td>");
        for (int a_iCnt=0; a_iCnt<a_coldefs.size(); a_iCnt++){
            String[] a_field = a_coldefs.get(a_iCnt).split("\t");
            out.print("<td style='text-align:center;'><font color='#ffffff'>" + a_field[COLUMN_DEF_COMMENT] + "</font></td>");
        }
        out.print("</tr>");
    
        //データ部
        for (int a_iCnt=0; a_iCnt<a_arrayLists.length; a_iCnt++){
            ArrayList<String> a_arrayList = a_arrayLists[a_iCnt];
            out.print("<tr bgcolor='#ffffff'>");
            out.print("<td>");
            out.print("<a href='#' onclick=\'select_plural_list(\"" + a_mode + "\",\"" + a_is_edit + "\",\"" + a_user_code + "\","+ a_iCnt + ");\'>");
            out.print(String.valueOf(a_iCnt + 1));
            out.print("</a>");
            out.print("</td>");
            for (int a_iCnt2=0; a_iCnt2<a_arrayList.size(); a_iCnt2++){
                String[] a_field = a_coldefs.get(a_iCnt2).split("\t");
                String[] a_split = a_arrayList.get(a_iCnt2).split("\t");
                String a_sVal = "";
                if (a_split.length > 1){
                    a_sVal = a_split[1];
                }
                out.print("<td>");
                out.print(Make_Tag_Mnt(a_envPath, true, false, false, "l", a_field, a_column_split, a_pulldown, a_showlist, a_sVal));
                //out.print(a_sVal);
                out.print("</td>");
            }
            out.print("</tr>");
        }
       
        out.print("</table>");
    }

    if (a_is_edit.equals("1") == true){
        g_JScript_Val_Auto_Plural = "";
        //g_JScript_Val_Ness = "";
        g_JScript_Program_Plural = "";
        g_JScript_IsNumeric_Plural = "";
        g_JScript_IsRequired_Plural = "";
        g_Post_Data_Plural = "";

        out.print("<p>");
        out.print("<table id='tbl_list2' border='1' cellspacing='0' cellpadding='0' style='width:auto;'>");
        ArrayList<String> a_arrayList = null;
        if (a_arrayLists != null){
            //if (a_seq != null){
                if (a_seq.equals("-1") == false){
                    a_arrayList = a_arrayLists[Integer.valueOf(a_seq)];
                }
            //}
        }
        for (int a_iCnt=0; a_iCnt<a_coldefs.size(); a_iCnt++){
            String[] a_split = a_coldefs.get(a_iCnt).split("\t");
            //splitは値が入っている所までしかlengthが返らない[2017.07.31]
            //カラム名を取得：0番目をメインとする
            String[] a_colNames = a_split[COLUMN_DEF_NAME].split(":");
            String a_colName = a_colNames[0];
            String a_val = "";
            /*
            if ((a_colName.equals("usercode") == true) && a_split[COLUMN_DEF_NESS].equals("a") == true){
                //PBXリモートの場合は、0XXXXの最老番号+1
                //IRMASの場合は、1XXXXの最老番号+1
                a_val = SetDB.GetNextUserCode(a_table_split[0]);
            }
            */
            if (a_arrayList != null){
                String[] a_split2 = a_arrayList.get(a_iCnt).split("\t");
                //out.print(a_arrayList.get(a_iCnt)+"<br>");
                if (a_split2.length > 1){
                    a_val = a_split2[1];
                }
                if (a_colName.equals("useripaddr") == true){
                    a_user_ipaddr = a_val;
                }
            }
            out.print("<tr>");
            out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>" + a_split[COLUMN_DEF_COMMENT] + "</font>");
            if (a_split[COLUMN_DEF_NESS].indexOf("y")>=0){
                out.print("<font color='#ffff00'>*</font>");
            }
            out.print("</td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'>" + Make_Tag_Mnt(a_envPath, false, true, true, "e", a_split, a_column_split, a_pulldown, a_showlist, a_val) + "</td>");
            out.print("</tr>");
        }
        out.print("</table>");
        
        out.print("<table><tr>");
        out.print("<td rowspan='1' valign='top'><div id='delete-plural-mnt' style='display:none;'><input id='change_submit' type='button' value='　削除　' onclick='delete_plural_mnt(\"" + a_mode + "\",\"" + a_is_edit + "\",\"" + a_user_code + "\");' /></div></td>");
        out.print("<td rowspan='1' valign='top'><div id='entry-plural-mnt'><input id='change_submit' type='button' value='　登録　' onclick='entry_plural_mnt();' /></div></td>");
        out.print("<input type='hidden' name='select-plural-seq' id='select-plural-seq' value=''>");
        out.print("</tr></table>");
    }
    
    out.print("<a href='#' onclick=\"$('.popup').hide();\">閉じる</a>");

    //java script
    out.print("<script type='text/javascript'>");
    //out.print("resize_tbl_list();");
    out.print("var g_val_auto_plural = '" + g_JScript_Val_Auto_Plural + "';");
    //out.print("var g_val_ness = '" + g_JScript_Val_Ness + "';" );
    out.print(g_JScript_Program_Plural);
    //out.print("alert(g_val_auto);");
    //out.print("alert(g_val_ness);");
    out.print(Make_Entry_Plural_Mnt(a_mode, a_is_edit, a_user_code, a_seq, a_user_ipaddr));
    out.print("</script>");
%>

    