<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp" %>
<jsp:useBean id="ReportMonthly" scope="page" class="report.ReportMonthly" />
<%
    //パスを取得
    String SCRIPT_NAME = request.getServletPath();
    String a_mainPath = GetMainPath(SCRIPT_NAME);
    String a_realPath = application.getRealPath(a_mainPath);
    
    //POSTデータを取得

    //Beansへの値引渡し
    ReportMonthly.SetRealPath(a_realPath);
    
    //DBの削除
    String a_status = ReportMonthly.GetReportStatus();
    /*
    if (a_status.length()<=0){
        a_status = "nothing";
    }
    */
    out.print(a_status);
%>

    