<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"
        import="java.io.*,java.util.*" %>
<%@ include file="/common.jsp" %>
<!- jsp:useBean id="Environ" scope="page" class="common.Environ" / -->
<!-- jsp:useBean id="salesforce" scope="page" class="salesforce.SalesForceApi" / -->
<%
/*
    //パスを取得
    String SCRIPT_NAME = request.getServletPath();
    String a_mainPath = GetMainPath(SCRIPT_NAME);
    String a_realPath = application.getRealPath(a_mainPath);
    String a_tmpPath = "";

    Environ.SetRealPath(a_realPath);
    a_tmpPath = Environ.GetEnvironValue("tmp_path");
*/
    
   //Session変数を取得
    String a_sCaseNo = "";
    if (session.getAttribute("CaseNo") != null){
        out.print("<table border=\"0\">");

        a_sCaseNo = String.valueOf(session.getAttribute("CaseNo"));
        String a_split[] = a_sCaseNo.split("\t");
        for (int a_i=0; a_i<a_split.length; a_i++){
            String a_sVal = a_split[a_i];
            String a_sTitle = "";
            switch(a_i){
                case 0:
                    a_sTitle = "REC番号";
                    break;
                case 1:
                    a_sTitle = "ケース番号";
                    break;
                case 2:
                    a_sTitle = "STEP1";
                    break;
                case 3:
                    a_sTitle = "STEP2";
                    break;
                case 4:
                    a_sTitle = "STEP3";
                    break;
                case 5:
                    a_sTitle = "STEP4";
                    break;
                case 6:
                    a_sTitle = "STEP5";
                    break;
                case 7:
                    a_sTitle = "STEP6";
                    break;
            }
            
            out.print("<tr>");
            out.print("<td width=\"70px\">" + a_sTitle + ":</td><td><font color=\"#3333cc\">" + a_sVal + "</font></td>");
            out.print("</tr>");
        }
    
        out.print("</table>");
    }

/*
    //Beansへの値引渡し
    salesforce.SetRealPath(a_realPath);
    
        //一覧データを取得
    ArrayList<String> a_arrayList = salesforce.GetSalesForceInfo(a_sCaseNo);
    if (a_arrayList != null){
        out.print("<table border=\"0\">");

        //データ部
        for (int a_iCnt=0; a_iCnt<a_arrayList.size(); a_iCnt++){
            String[] a_split = a_arrayList.get(a_iCnt).split("\t");
            out.print("<tr>");
            out.print("<td width=\"70px\">" + a_split[0] + ":</td><td><font color=\"#3333cc\">" + a_split[1] + "</font></td>");
            out.print("</tr>");
        }
        
        out.print("</table>");
    }
*/
%>

    