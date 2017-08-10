<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp" %>
<jsp:useBean id="SetDB" scope="page" class="maintenance.SetDB" />
<%
    //パスを取得
    String SCRIPT_NAME = request.getServletPath();
    String a_mainPath = GetMainPath(SCRIPT_NAME);
    String a_realPath = application.getRealPath(a_mainPath);
    
    //POSTデータを取得
    String a_id = request.getParameter("id");
    
    //Beansへの値引渡し
    SetDB.SetRealPath(a_realPath);
    
    //nameを取得
    String a_name = SetDB.GetEquipmentTypeName(a_id);

    out.print(a_name);
%>

    