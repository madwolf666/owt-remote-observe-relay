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
    String System_IRMS = request.getParameter("chk_System_IRMS");
    String System_MSSV2 = request.getParameter("chk_System_MSSV2");
    String System_PBX = request.getParameter("chk_System_PBX");
    String entry_dateS = request.getParameter("entry_dateS");
    String entry_dateE = request.getParameter("entry_dateE");
    String SelectUser = request.getParameter("rdo_SelectUser");
    String exec_time = request.getParameter("exec_time");
    String SelectUserCode = request.getParameter("SelectUserCode"); 

    //Beansへの値引渡し
    ReportMonthly.SetRealPath(a_realPath);
    ReportMonthly.putSystem_IRMS(System_IRMS);
    ReportMonthly.putSystem_MSSV2(System_MSSV2);
    ReportMonthly.putSystem_PBX(System_PBX);
    ReportMonthly.putTimeS(entry_dateS);
    ReportMonthly.putTimeE(entry_dateE);
    ReportMonthly.putSelectUser(SelectUser);
    ReportMonthly.putExecTime(exec_time);
    ReportMonthly.putSelectUserCode(SelectUserCode);
    
    //月報の作成
   ReportMonthly.InsertReportSchedule();
%>

    