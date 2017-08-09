<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page import="java.util.ArrayList,java.io.*,java.util.*,java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp" %>
<jsp:useBean id="Environ" scope="page" class="common.Environ" />
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
    ArrayList<String> a_coldefs = (ArrayList<String>)session.getAttribute("Mnt_Coldefs");
    String[] a_table_split = null;
    String[] a_column_split = null;
    if (a_Mnt_Table != ""){
        a_table_split = a_Mnt_Table.split("\t");
        a_column_split = a_table_split[1].split(",");
    }

    //POSTデータを取得
    String a_ACT = request.getParameter("ACT");
    String a_IDX = request.getParameter("IDX");
    ArrayList<String> a_post_data = new ArrayList<String>();
    for (int a_iCnt=0; a_iCnt<a_coldefs.size(); a_iCnt++){
        String[] a_split = a_coldefs.get(a_iCnt).split("\t");
        //カラム名を取得：0番目をメインとする
        String[] a_colNames = a_split[COLUMN_DEF_NAME].split(",");
        String a_colName = a_colNames[0];
        if (request.getParameter(a_colName) != null){
            String a_val = request.getParameter(a_colName);
            if (a_val.length()>0){
                a_post_data.add(a_colName + "\t" + a_val);
            }else{
                a_post_data.add(a_colName + "\t");
            }
        }else{
                a_post_data.add(a_colName + "\t");
        }
    }

    out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0'>");
    
    for (int a_iCnt=0; a_iCnt<a_coldefs.size(); a_iCnt++){
        String[] a_split = a_coldefs.get(a_iCnt).split("\t");
        String[] a_edit = a_post_data.get(a_iCnt).split("\t");
        String a_val = "";
        if (a_edit.length>1){
            a_val = a_edit[1];
        }
        //splitは値が入っている所までしかlengthが返らない[2017.07.31]
        out.print("<tr>");
        out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>" + a_split[COLUMN_DEF_COMMENT] + "</font>");
        if (a_split[COLUMN_DEF_NESS].indexOf("y")>=0){
            out.print("<font color='#ffff00'>*</font>");
        }
        out.print("</td>");
        out.print("<td bgcolor='transparent' style='text-align:left;'>" + Make_Tag_Mnt(false, a_ACT, a_split, a_column_split, a_pulldown, a_showlist, a_val) + "</font></td>");
        out.print("</tr>");
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
    out.print(Make_Entry_Table_Mnt(a_ACT, a_IDX));
    out.print("</script>");
%>

    