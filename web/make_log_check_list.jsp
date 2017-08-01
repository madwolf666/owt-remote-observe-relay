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
    String OrderStartDate = request.getParameter("OrderStartDate");
    
    //Beansへの値引渡し
    WarnSchedule.SetRealPath(a_realPath);
    WarnSchedule.putOrderStartDate(OrderStartDate);

    //一覧データを取得
    ArrayList<String> a_arrayList = WarnSchedule.FindAlarmLog(a_PageNo);
    if (a_arrayList != null){
        out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0'>");
        //ヘッダ部
        out.print("<tr bgcolor='#003366'>");
        out.print("<td style='text-align:center;'><font color='#ffffff'><a href='#' onclick='click_log_check_start_date(" + a_PageNo + ");'>START_DATE</a></font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>END_DATE</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>FILE_NAME</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>JOBKIND</td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>USER_NAME</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>BRANCH_NAME</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>RESULT</font></td>");
        out.print("</tr>");
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
            out.print("<td>" + a_split[3] + "</td>");
            out.print("<td>" + a_split[4] + "</td>");
            out.print("<td>" + a_split[5] + "</td>");
            out.print("<td>" + a_split[6] + "</td>");
            out.print("</tr>");
        }
        
        out.print("</table>");
    }

    out.print(OutCopyRight());

    //java script
    out.print("<script type='text/javascript'>");
    out.print("resize_tbl_list();");
    out.print("</script>");
%>

    