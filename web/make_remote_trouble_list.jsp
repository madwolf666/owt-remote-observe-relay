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
    int a_PageNo = Integer.valueOf(request.getParameter("PageNo"));
    /*
    String UserCode = request.getParameter("txt_UserCode");
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
    */
    String OrderInitiationTime = request.getParameter("order_InitiationTime");

    //セッション変数を取得
    String UserCode = GetSessionValue(session.getAttribute("txt_UserCode"));    //[2016.06.09]USERCODEでのリレーションは現状ない
    String SetNo = GetSessionValue(session.getAttribute("txt_SetNo"));
    String RecNo = GetSessionValue(session.getAttribute("txt_RecNo"));
    String UserName = GetSessionValue(session.getAttribute("txt_UserName"));
    String TimeS = GetSessionValue(session.getAttribute("entry_timeS"));
    String TimeE = GetSessionValue(session.getAttribute("entry_timeE"));
    String TroubleMsg = GetSessionValue(session.getAttribute("txt_TroubleMsg"));
    String TroubleKind_MJ = GetSessionValue(session.getAttribute("chk_TroubleKind_MJ"));
    String TroubleKind_MN = GetSessionValue(session.getAttribute("chk_TroubleKind_MN"));
    String TroubleKind_GN = GetSessionValue(session.getAttribute("chk_TroubleKind_GN"));
    String Contact = GetSessionValue(session.getAttribute("chk_Contact"));

    //Beansへの値引渡し
    RemoteTrouble.SetRealPath(a_realPath);
    RemoteTrouble.putUserCode(UserCode);
    RemoteTrouble.putSetNo(SetNo);
    RemoteTrouble.putRecNo(RecNo);
    RemoteTrouble.putUserName(UserName);
    RemoteTrouble.putTimeS(TimeS);
    RemoteTrouble.putTimeE(TimeE);
    RemoteTrouble.putTroubleMsg(TroubleMsg);
    RemoteTrouble.putTroubleKind_MJ(TroubleKind_MJ);
    RemoteTrouble.putTroubleKind_MN(TroubleKind_MN);
    RemoteTrouble.putTroubleKind_GN(TroubleKind_GN);
    RemoteTrouble.putContact(Contact);
    RemoteTrouble.putOrderInitiationTime(OrderInitiationTime);
    
    //一覧データを取得
    ArrayList<String> a_arrayList = RemoteTrouble.FindRemoteTrouble(a_PageNo);
    if (a_arrayList != null){
        out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0'>");
        //ヘッダ部
        out.print("<tr bgcolor='#003366'>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>No</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'><a href='#' onclick='click_remote_trouble_initiationtime(" + a_PageNo + ");'>発報時間</font></font></td>");
        //out.print("<td style='text-align:center;'><font color='#ffffff'>ユーザコード</font></td>");   //[2016.06.16]要望により削除
        out.print("<td style='text-align:center;'><font color='#ffffff'>SET-NO</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>REC番号</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>ユーザ名</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>機種名</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>ユーザ機種名</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>障害種別</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>1st MSG</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>2nd MSG</font></td>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>3rd MSG</font></tdr>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>連絡済み</font></td>");
        out.print("</tr>");
    
        //データ部
        // ■SFケース詳細の場合
        //　リモートDB内の受付時間前に発生している障害情報を最大100件表示する。

        for (int a_iCnt=0; a_iCnt<a_arrayList.size(); a_iCnt++){
            String[] a_split = a_arrayList.get(a_iCnt).split("\t");
            if ((a_iCnt % 2)==0){
                out.print("<tr bgcolor='#ffffff'>");
            }else{
                out.print("<tr bgcolor='#fffff0'>");
            }
            out.print("<td>" + a_split[0] + "</td>");
            out.print("<td>" + a_split[1] + "</td>");
            //out.print("<td>" + a_split[2] + "</td>"); //[2016.06.16]要望により削除
            out.print("<td>" + a_split[3] + "</td>");
            out.print("<td>" + a_split[4] + "</td>");
            out.print("<td>" + a_split[5] + "</td>");
            out.print("<td>" + a_split[6] + "</td>");
            out.print("<td>" + a_split[7] + "</td>");
            out.print("<td>" + a_split[8] + "</td>");
            out.print("<td>" + a_split[9] + "</td>");
            out.print("<td>" + a_split[10] + "</td>");
            out.print("<td>" + a_split[11] + "</td>");
            out.print("<td>" + a_split[12] + "</td>");
            out.print("</tr>");
        }

        out.print("</table>");
    }

    //out.print("<div id=\"pagetop\"><a href=\"#hpb-container\">このページの先頭へ</a></div>");
    out.print(OutCopyRight());
    
    //java script
    out.print("<script type='text/javascript'>");
//    out.print("$('#txt_UserCode').val('" + UserCode + "');"); //[2016.06.09]USERCODEでのリレーションは現状ない
    out.print("$('#txt_SetNo').val('" + SetNo + "');");
    out.print("$('#txt_RecNo').val('" + RecNo + "');");
    out.print("$('#txt_UserName').val('" + UserName + "');");
    out.print("$('#entry_timeS').val('" + TimeS + "');");
    out.print("$('#entry_timeE').val('" + TimeE + "');");
    out.print("$('#txt_TroubleMsg').val('" + TroubleMsg + "');");
    if (TroubleKind_MJ.length() > 0){
        out.print("$('#chk_TroubleKind_MJ').prop('checked'," + TroubleKind_MJ + ");");
    }
    if (TroubleKind_MN.length() > 0){
        out.print("$('#chk_TroubleKind_MN').prop('checked'," + TroubleKind_MN + ");");
    }
    if (TroubleKind_GN.length() > 0){
        out.print("$('#chk_TroubleKind_GN').prop('checked'," + TroubleKind_GN + ");");
    }
    if (Contact.length() > 0){
        out.print("$('#chk_Contact').prop('checked'," + Contact + ");");
    }
    out.print("resize_tbl_list();");
    out.print("</script>");
%>

    