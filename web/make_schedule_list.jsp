<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp" %>
<jsp:useBean id="WarnSchedule" scope="page" class="warn.WarnSchedule" />
<%
    //パスを取得
    String SCRIPT_NAME = request.getServletPath();
    String a_mainPath = GetMainPath(SCRIPT_NAME);
    String a_realPath = application.getRealPath(a_mainPath);

   //POSTデータを取得
    int a_PageNo = Integer.valueOf(request.getParameter("PageNo"));
    String OrderStartTime = request.getParameter("OrderStartTime");
    
    //Beansへの値引渡し
    WarnSchedule.SetRealPath(a_realPath);
    WarnSchedule.putOrderStartTime(OrderStartTime);

    //一覧データを取得
    ArrayList<String> a_arrayList = WarnSchedule.FindWarnSchedule(a_PageNo);
    if (a_arrayList != null){
        out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0'>");
        //ヘッダ部
        out.print("<tr>");
        out.print("<td style='text-align:center;' rowspan='2'>編集</td>");
        //[2016.08.15]bug-fixed.
        out.print("<td style='text-align:center;background-color:#ff91a3;'><font color='#ffffff'><a href='#' onclick='click_schedule_starttime(" + a_PageNo + ");' class='linkA'>作業開始日時</a></font></td>");
        out.print("<td style='text-align:center;'>顧客名</td>");
        out.print("<td style='text-align:center;' colspan='5'>作業内容</td>");
        out.print("</tr>");
        out.print("<tr>");
        out.print("<td style='text-align:center;background-color:#9370db;'><font color='#ffffff'>作業終了日時</font></td>");
        out.print("<td style='text-align:center;'>支店名</td>");
        out.print("<td style='text-align:center;background-color:#ffff00;'>事前確認</td>");
        out.print("<td style='text-align:center;background-color:#ffff00;'>入退管理</td>");
        out.print("<td style='text-align:center;background-color:#ff9900;'>停電</td>");
        out.print("<td style='text-align:center;background-color:#98fb98;'>作業</td>");
        out.print("<td style='text-align:center;background-color:#d3d3d3;'>定刻</td>");
        out.print("</tr>");

        //データ部
        for (int a_iCnt=0; a_iCnt<a_arrayList.size(); a_iCnt++){
            String[] a_split = a_arrayList.get(a_iCnt).split("\t");
            out.print("<tr>");
            out.print("<td style='text-align:center;' rowspan='2'><input type='button' value='編集' onclick='document.location=\"./edit_warn.xhtml?ACT=e&JOBID=" + a_split[0] + "\";'></td>");
            out.print("<td bgcolor='#ff91a3'><font color='#ffffff'>" + a_split[1] + "</font></td>");
            out.print("<td>" + a_split[3] + "</td>");
            out.print("<td bgcolor='" + a_split[5] + "' colspan='5' rowspan='2'>" + a_split[6] + "</td>");
            out.print("</tr>");
            out.print("<tr>");
            out.print("<td bgcolor='#9370db'><font color='#ffffff'>" + a_split[2] + "</font></td>");
            out.print("<td>" + a_split[4] + "</td>");
            out.print("</tr>");
        }

        out.print("</table>");
    }

    out.print("<div id=\"hpb-footer\">");
    out.print("  <div id=\"hpb-footerMain\">");
    out.print("    <p>copyright&#169;2015&#160;OKI&#160;Wintech&#160;all&#160;rights&#160;reserved.</p>");
    out.print("  </div>");
    out.print("</div>");

    //java script
    out.print("<script type='text/javascript'>");
    out.print("resize_edit_warn_list();");
    out.print("</script>");
%>

    