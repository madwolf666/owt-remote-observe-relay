<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp" %>
<!-- jsp:useBean id="RemoteTrouble" scope="page" class="remote.RemoteTrouble" / -->
<%
    //パスを取得
    String SCRIPT_NAME = request.getServletPath();
    String a_mainPath = GetMainPath(SCRIPT_NAME);
    String a_realPath = application.getRealPath(a_mainPath);

   //POSTデータを取得[2017.07.27]
    int a_Kind = -1;
    if (request.getParameter("Kind") != null)
        a_Kind = Integer.valueOf(request.getParameter("Kind"));
    
    String UserCode = request.getParameter("txt_UserCode"); //[2016.06.09]USERCODEでのリレーションは現状ない
    String SetNo = request.getParameter("txt_SetNo");
    String RecNo = request.getParameter("txt_RecNo");
    String UserName = request.getParameter("txt_UserName");
    String TimeS = request.getParameter("entry_timeS");
    String TimeE = request.getParameter("entry_timeE");
    String TroubleMsg = request.getParameter("txt_TroubleMsg");
    String TroubleKind_MJ = request.getParameter("chk_TroubleKind_MJ");
    String TroubleKind_MN = request.getParameter("chk_TroubleKind_MN");
    String TroubleKind_GN = request.getParameter("chk_TroubleKind_GN");
    String Contact = request.getParameter("chk_Contact");
        
    String Mnt_Table = request.getParameter("Mnt_Table");   //[2017.07.27]
    
    //セッション変数を設定
    session.setAttribute("txt_UserCode", UserCode);
    session.setAttribute("txt_SetNo", SetNo);
    session.setAttribute("txt_RecNo", RecNo);
    session.setAttribute("txt_UserName", UserName);
    session.setAttribute("entry_timeS", TimeS);
    session.setAttribute("entry_timeE", TimeE);
    session.setAttribute("txt_TroubleMsg", TroubleMsg);
    session.setAttribute("chk_TroubleKind_MJ", TroubleKind_MJ);
    session.setAttribute("chk_TroubleKind_MN", TroubleKind_MN);
    session.setAttribute("chk_TroubleKind_GN", TroubleKind_GN);
    session.setAttribute("chk_Contact", Contact);

    session.setAttribute("Mnt_Table", Mnt_Table);   //[2017.07.27]
%>

    