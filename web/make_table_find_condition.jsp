<%-- 
    Document   : make_table_select_mnt
    Created on : 2017/07/27, 12:46:22
    Author     : hal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"
        import="java.io.*,java.util.*,java.text.SimpleDateFormat" %>
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

    //POSTデータを取得
    //String a_table = request.getParameter("table");

    String a_Mnt_Table = GetSessionValue(session.getAttribute("Mnt_Table"));
    String[] a_table_split = null;
    String[] a_column_split = null;
    if (a_Mnt_Table != ""){
        a_table_split = a_Mnt_Table.split("\t");
        a_column_split = a_table_split[1].split(",");
    }else{
        out.print("");
        return;
    }

    ArrayList<String> a_coldefs = null;
    
    String a_out = "";

    if ((a_table_split[0].equals("pbxremotecustomer") == true) || (a_table_split[0].equals("irmsremotecustomer") == true)){
        a_coldefs = GetDef_Field(a_envPath + a_table_split[0] + "-findc.def");
        //a_out += "<table id='tbl_list' border='0' cellspacing='0' cellpadding='0' style='width:auto;'>";
        a_out += "<table id='tbl_list' border='0' cellspacing='0' cellpadding='0' style='width:auto;margin-top:-4px;'>";
        a_out += "<tr>";
        for (int a_iCnt=0; a_iCnt<a_coldefs.size(); a_iCnt++){
            String[] a_split = a_coldefs.get(a_iCnt).split("\t");
            //splitは値が入っている所までしかlengthが返らない[2017.07.31]
            //カラム名を取得：0番目をメインとする
            String[] a_colNames = a_split[COLUMN_DEF_NAME].split(":");
            String a_colName = a_colNames[0];
            String a_val = "";
            a_out += "<td bgcolor='transparent' style='text-align:left;'>";
            a_out += a_split[COLUMN_DEF_COMMENT];
            /*
            if (a_split[COLUMN_DEF_NESS].indexOf("y")>=0){
                out.print("<font color='#ffff00'>*</font>");
            }
            */
            a_out += "</td>";
            a_out += "<td bgcolor='transparent' style='text-align:left;'>";
            a_out += Make_Tag_Mnt(a_envPath, true, true, true, "e", a_split, a_column_split, a_pulldown, a_showlist, "");
            a_out += "</td>";
        }
        a_out += "<td>";
        a_out += "<input type='button' value='検索'  onclick='find_table_mnt_first();'>";
        a_out += "</td>";
        a_out += "</tr>";
        a_out += "</table>";
    }
    
    out.print(a_out);
%>
