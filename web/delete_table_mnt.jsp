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
    String a_sRet = "";

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
    if (a_Mnt_Table.equals("") == false){
        a_table_split = a_Mnt_Table.split("\t");
        a_column_split = a_table_split[1].split(",");
    }

    //POSTデータを取得
    String a_IDX = request.getParameter("IDX");

    //Beansへの値引渡し
    SetDB.SetRealPath(a_realPath);

    if ((a_table_split[0].equals("pbxremotecustomer") == true) || (a_table_split[0].equals("irmsremotecustomer") == true)){
        a_sRet = SetDB.DeleteMnt(a_table_split[0], "usercode='" + a_IDX + "'");
    }else if (a_table_split[0].equals("nodeinfo") == true){
        a_sRet = SetDB.DeleteMnt(a_table_split[0], "nodecode='" + a_IDX + "'");
    }else{
        a_sRet = SetDB.DeleteMnt(a_table_split[0], "num=" + a_IDX);
    }

    out.print(a_sRet);
%>

    