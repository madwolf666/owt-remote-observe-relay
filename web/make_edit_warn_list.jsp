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

    //QueryStringの取得
    String a_ACT = request.getParameter("ACT");
    String a_JobId = request.getParameter("JOBID");

    //Beansへの値引渡し
    WarnSchedule.SetRealPath(a_realPath);

    //登録済データを取得
    ArrayList<String> a_arrayList = null;
    if (a_ACT.equals("e") == true){
        out.print("<input type='hidden' id='txt_JobId' value='" + a_JobId + "'>");
       a_arrayList = WarnSchedule.GetWarnSchedule(a_JobId);
    }else{
        out.print("<input type='hidden' id='txt_JobId' value=''>");
    }
    
    out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0'>");
    //ヘッダ部
    out.print("<tr>");
    out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>顧客名</font></td>");
    out.print("<td colspan='3'><input type='text' size='36' maxlength='17' id='txt_CustomerName' value='");
    if (a_ACT.equals("e") == true){
        out.print(a_arrayList.get(1));
    }
    out.print("'></td>");
    out.print("</tr>");

    out.print("<tr>");
    out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>支店名</font></td>");
    out.print("<td colspan='3'><input type='text' size='36' maxlength='17' id='txt_BranchName' value='");
    if (a_ACT.equals("e") == true){
        out.print(a_arrayList.get(2));
    }
    out.print("'></td>");
    out.print("</tr>");
    
    String[] a_JobKind_checked = {"","","","",""};
    if (a_ACT.equals("e") == true){
        a_JobKind_checked[Integer.valueOf(a_arrayList.get(3))-1] = "checked";
    }else{
        a_JobKind_checked[0] = "checked";
    }
    out.print("<tr>");
    out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>作業内容</font></td>");
    out.print("<td colspan='3'><input type='radio' name='rdo_JobKind' value='1'" + a_JobKind_checked[0] + ">事前確認");
    out.print("<input type='radio' name='rdo_JobKind' value='2'" + a_JobKind_checked[1] + ">入退管理");
    out.print("<input type='radio' name='rdo_JobKind' value='3'" + a_JobKind_checked[2] + ">停電");
    out.print("<input type='radio' name='rdo_JobKind' value='4'" + a_JobKind_checked[3] + ">作業");
    out.print("<input type='radio' name='rdo_JobKind' value='5'" + a_JobKind_checked[4] + ">定刻</td>");
    out.print("</tr>");
    
    out.print("<tr>");
    out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>警告内容</font></td>");
    out.print("<td colspan='3'><input type='text' size='72' maxlength='33' id='txt_JobContents' value='");
    if (a_ACT.equals("e") == true){
        out.print(a_arrayList.get(4));
    }
    out.print("'></td>");
    out.print("</tr>");
    
    out.print("<tr>");
    out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>開始日時</font></td>");
    out.print("<td><input type='text' id='entry_timeS' maxlength='16' style='width:110px;' value='");
    if (a_ACT.equals("e") == true){
        out.print(a_arrayList.get(5));
    }
    out.print("' onchange=\"return chek_WarnStEd();\"></td>");
    out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>終了日時</font></td>");
    out.print("<td><input type='text' id='entry_timeE' maxlength='16' style='width:110px;' value='");
    if (a_ACT.equals("e") == true){
        out.print(a_arrayList.get(6));
    }
    out.print("' onchange=\"return chek_WarnStEd();\"></td>");
    out.print("</tr>");

    String[] a_ExecDays_checked = {"","","","","","","",""};
    if (a_ACT.equals("e") == true){
        int a_iTmp = Integer.valueOf(a_arrayList.get(7));
        if ((a_iTmp & 1) == 1){
            a_ExecDays_checked[0] = "checked=true";
        }
        if ((a_iTmp & 2) == 2){
            a_ExecDays_checked[1] = "checked=true";
        }
        if ((a_iTmp & 4) == 4){
            a_ExecDays_checked[2] = "checked=true";
        }
        if ((a_iTmp & 8) == 8){
            a_ExecDays_checked[3] = "checked=true";
        }
        if ((a_iTmp & 16) == 16){
            a_ExecDays_checked[4] = "checked=true";
        }
        if ((a_iTmp & 32) == 32){
            a_ExecDays_checked[5] = "checked=true";
        }
        if ((a_iTmp & 64) == 64){
            a_ExecDays_checked[6] = "checked=true";
        }
        if ((a_iTmp & 128) == 128){
            a_ExecDays_checked[7] = "checked=true";
        }
    }else{
        a_ExecDays_checked[0] = "checked=true";
    }
    out.print("<tr>");
    out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>曜日</font></td>");
    out.print("<td><input type='checkbox' id='chk_ExecDays_All' " + a_ExecDays_checked[0] + ">all_day</td>");
    out.print("<td colspan='2'><input type='checkbox' id='chk_ExecDays_Sun'" + a_ExecDays_checked[1] + ">Sun");
    out.print("<input type='checkbox' id='chk_ExecDays_Mon'" + a_ExecDays_checked[2] + ">Mon");
    out.print("<input type='checkbox' id='chk_ExecDays_Tue'" + a_ExecDays_checked[3] + ">Tue");
    out.print("<input type='checkbox' id='chk_ExecDays_Wed'" + a_ExecDays_checked[4] + ">Wed");
    out.print("<input type='checkbox' id='chk_ExecDays_Thr'" + a_ExecDays_checked[5] + ">Thr");
    out.print("<input type='checkbox' id='chk_ExecDays_Fri'" + a_ExecDays_checked[6] + ">Fri");
    out.print("<input type='checkbox' id='chk_ExecDays_Sat'" + a_ExecDays_checked[7] + ">Sat</td>");
    out.print("</tr>");

    String[] a_ExecKind_checked = {"",""};
    if (a_ACT.equals("e") == true){
        int a_iTmp = Integer.valueOf(a_arrayList.get(8));
        if ((a_iTmp & 1) == 1){
            a_ExecKind_checked[0] = "checked=true";
        }
        if ((a_iTmp & 2) == 2){
            a_ExecKind_checked[1] = "checked=true";
        }
    }else{
        a_ExecKind_checked[0] = "checked=true";
    }
    out.print("<tr>");
    out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>実行</font></td>");
    out.print("<td colspan='3'><input type='checkbox' id='chk_ExecKind_St' " + a_ExecKind_checked[0] + ">Start<input type='checkbox' id='chk_ExecKind_Ed' " + a_ExecKind_checked[1] + ">End</td>");
    out.print("</tr>");

    out.print("</table>");

    out.print("<div id=\"hpb-footer\">");
    out.print("  <div id=\"hpb-footerMain\">");
    out.print("    <p>copyright&#169;2015&#160;OKI&#160;Wintech&#160;all&#160;rights&#160;reserved.</p>");
    out.print("  </div>");
    out.print("</div>");
    
    //java script
    out.print("<script type='text/javascript'>");
    
//    for (int a_i=1; a_i<10; a_i++){
        out.print("$(function () {$('#entry_timeS').datetimepicker({lang:'ja',step:1});});");
        out.print("$(function () {$('#entry_timeE').datetimepicker({lang:'ja',step:1});});");
        //out.print("$(function () {$('#entry_timeS_" + String.valueOf(a_i) + "').datetimepicker({lang:'ja',step:1});});");
        //out.print("$(function () {$('#entry_timeE_" + String.valueOf(a_i) + "').datetimepicker({lang:'ja',step:1});});");
//    }

    out.print("resize_tbl_list();");
    out.print("</script>");

%>

    