<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp" %>
<jsp:useBean id="RemoteTrouble" scope="page" class="remote.RemoteTrouble" />
<%
    //パスを取得
    String SCRIPT_NAME = request.getServletPath();
    String a_mainPath = GetMainPath(SCRIPT_NAME);
    String a_realPath = application.getRealPath(a_mainPath);

   //POSTデータを取得
    int a_Kind = 6; //Integer.valueOf(request.getParameter("Kind"));
    int a_PageNo = Integer.valueOf(request.getParameter("PageNo"));
    String UserName = request.getParameter("txt_UserName");
    
    //Beansへの値引渡し
    RemoteTrouble.SetRealPath(a_realPath);
    RemoteTrouble.putUserName(UserName);
    
    String a_sOut = RemoteTrouble.MakePagerUser(a_Kind, a_PageNo);
    out.print(a_sOut);
    
    //一覧データを取得
    ArrayList<String> a_arrayList = RemoteTrouble.FindUser(a_PageNo);
    if (a_arrayList != null){
        out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0'>");
        //ヘッダ部
        out.print("<tr bgcolor='#003366'>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>ユーザコード</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>ユーザ名</font></td>");
        out.print("</tr>");
    
        //データ部
        for (int a_iCnt=0; a_iCnt<a_arrayList.size(); a_iCnt++){
            String[] a_split = a_arrayList.get(a_iCnt).split("\t");
            out.print("<tr bgcolor='#ffffff'>");
            out.print("<td>" + a_split[0] + "</td>");
            out.print("<td><a href='#' onclick=\"click_user('" + a_split[1] + "');\">" + a_split[1] + "</a></td>");
            out.print("</tr>");
        }
       
        out.print("</table>");
    }

    out.print("<a href='#' onclick=\"$('.popup').hide();\">閉じる</a>");
%>

    