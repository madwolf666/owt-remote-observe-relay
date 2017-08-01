<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp" %>
<jsp:useBean id="ReportMonthly" scope="page" class="report.ReportMonthly" />
<%
    //パスを取得
    String SCRIPT_NAME = request.getServletPath();
    String a_mainPath = GetMainPath(SCRIPT_NAME);
    String a_realPath = application.getRealPath(a_mainPath);

   //POSTデータを取得
    int a_PageNo = Integer.valueOf(request.getParameter("PageNo"));
    String OrderUserCode = request.getParameter("OrderUserCode");
    
    //Beansへの値引渡し
    ReportMonthly.SetRealPath(a_realPath);
    ReportMonthly.putOrderUserCode(OrderUserCode);
    
    //一覧データを取得
    ArrayList<String> a_arrayList = ReportMonthly.FindUsers(a_PageNo);
    if (a_arrayList != null){
        out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0'>");
        //ヘッダ部
        out.print("<tr bgcolor='#003366'>");
        out.print("<td style='text-align:center;'><font color='#ffffff'><a href='#' onclick='click_report_usercode(" + a_PageNo + ");'>ユーザコード</a></font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>ユーザ名</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>月報の有無</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>選択</font></td>");
        out.print("</tr>");

        //データ部
        for (int a_iCnt=0; a_iCnt<a_arrayList.size(); a_iCnt++){
            String[] a_split = a_arrayList.get(a_iCnt).split("\t");
            if ((a_iCnt % 2)==0){
                out.print("<tr bgcolor='#ffffff'>");
            }else{
                out.print("<tr bgcolor='#fffff0'>");
            }
            out.print("<td>" + a_split[0] + "</td>");
            out.print("<td>" + a_split[1] + "</td>");
            out.print("<td>" + a_split[2] + "</td>");
            out.print("<td><input type=\"radio\" name=\"rdo_user\" value=\"" + a_split[0] + "\"></td>");
            out.print("</tr>");
        }

        out.print("</table>");
    }
    
    //out.print("<div id=\"pagetop\"><a href=\"#hpb-container\">このページの先頭へ</a></div>");
    out.print(OutCopyRight());
    
    //java script
    out.print("<script type='text/javascript'>");
    out.print("resize_tbl_list();");
    out.print("</script>");
%>

    