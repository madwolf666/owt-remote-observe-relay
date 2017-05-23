<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp" %>
<jsp:useBean id="WarnSchedule" scope="page" class="warn.WarnSchedule" />
<%
    //パスを取得
    String SCRIPT_NAME = request.getServletPath();
    String a_mainPath = GetMainPath(SCRIPT_NAME);
    String a_realPath = application.getRealPath(a_mainPath);
    
    //POSTデータを取得
    String JobId = request.getParameter("JobId");

    //Beansへの値引渡し
    WarnSchedule.SetRealPath(a_realPath);
    WarnSchedule.putJobId(JobId);
    
    //DBの削除
    WarnSchedule.TestAlaram(JobId);
%>

    