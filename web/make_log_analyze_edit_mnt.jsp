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
    String a_Mnt_Table = "loganalyzeschedule \tid:n:n:y";;
    //String a_Mnt_Table = GetSessionValue(session.getAttribute("Mnt_Log_Analyze_Table"));
    ArrayList<String> a_coldefs = (ArrayList<String>)session.getAttribute("Mnt_Log_Analyze_Coldefs");
    if (a_coldefs == null){
        response.sendError(HttpServletResponse.SC_SEE_OTHER, "Session Timeout is occured.");
        return;
    }
    String[] a_table_split = null;
    String[] a_column_split = null;
    if (a_Mnt_Table.length() > 0){
        a_table_split = a_Mnt_Table.split("\t");
        a_column_split = a_table_split[1].split(",");
    }
    
    //QueryStringの取得
    String a_ACT = request.getParameter("ACT");
    String a_IDX = request.getParameter("IDX");
    String a_DB = request.getParameter("DB");

    //Beansへの値引渡し
    SetDB.SetRealPath(a_realPath);

    //登録済データを取得
    ArrayList<String> a_arrayList = null;
    out.print("<input type='hidden' id='edit_idx' name='edit_idx' value='");
    if (a_ACT.equals("e") == true){
        a_arrayList = SetDB.GetMnt(a_Mnt_Table, a_coldefs, a_IDX);
        out.print(a_IDX);
    }else{
        if (a_DB.equals("0") == true){
            a_arrayList = new ArrayList<String>();
            for (int a_iCnt=0; a_iCnt<a_coldefs.size(); a_iCnt++){
                String[] a_split = a_coldefs.get(a_iCnt).split("\t");
                //カラム名を取得：0番目をメインとする
                String[] a_colNames = a_split[COLUMN_DEF_NAME].split(":");
                String a_colName = a_colNames[0];
                String a_field = a_split[COLUMN_DEF_FIELD];
                if (request.getParameter(a_field) != null){
                    String a_val = request.getParameter(a_field);
                    //String a_val = HtmlEncode(request.getParameter(a_field));
                    if (a_val.length()>0){
                        a_arrayList.add(a_field + "\t" + a_val);
                    }else{
                        a_arrayList.add(a_field + "\t");
                    }
                }else{
                        a_arrayList.add(a_field + "\t");
                }
            }
        }
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
    
    out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0' style='margin-left:20px;width:640px;'>");

    //データ部
    if (a_arrayList != null){
        for (int a_iCnt=0; a_iCnt<a_coldefs.size(); a_iCnt++){
            boolean a_isOK = true;
            String[] a_split = a_coldefs.get(a_iCnt).split("\t");
            String[] a_edit = a_arrayList.get(a_iCnt).split("\t");
            String[] a_colNames = a_split[COLUMN_DEF_NAME].split(":");
            String a_colName = a_colNames[0];
            String a_val = "";
            if (a_edit.length > 1){
                a_val = a_edit[1];
            }
            if (a_isOK == true){
                out.print("<tr>");
                out.print("<td bgcolor='#003366' style='text-align:left; width:140px;' nowrap><font color='#ffffff'>" + a_split[COLUMN_DEF_COMMENT] + "</font>");
                if (a_split[COLUMN_DEF_NESS].indexOf("y")>=0){
                    out.print("<font color='#ffff00'>*</font>");
                }
                out.print("</td>");
                out.print("<td bgcolor='transparent' style='text-align:left;'>" + Make_Tag_Mnt(a_envPath, true, true, false, a_ACT, a_split, a_column_split, a_pulldown, a_showlist, a_val) + "</td>");
                out.print("</tr>");
            }
        }
    }else{
        for (int a_iCnt=0; a_iCnt<a_coldefs.size(); a_iCnt++){
            String[] a_split = a_coldefs.get(a_iCnt).split("\t");
            //splitは値が入っている所までしかlengthが返らない[2017.07.31]
            //カラム名を取得：0番目をメインとする
            String[] a_colNames = a_split[COLUMN_DEF_NAME].split(":");
            String a_colName = a_colNames[0];
            String a_val = "";
            out.print("<tr>");
            out.print("<td bgcolor='#003366' style='text-align:left; width:140px;'><font color='#ffffff'>" + a_split[COLUMN_DEF_COMMENT] + "</font>");
            if (a_split[COLUMN_DEF_NESS].indexOf("y")>=0){
                out.print("<font color='#ffff00'>*</font>");
            }
            out.print("</td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'>" + Make_Tag_Mnt(a_envPath, true, true, true, a_ACT, a_split, a_column_split, a_pulldown, a_showlist, a_val) + "</td>");
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
    out.print(Make_Entry_Log_Analyze_mnt(a_ACT, a_IDX));
    out.print("</script>");
    
%>
    