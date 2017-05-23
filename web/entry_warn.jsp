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
    String CustomerName = request.getParameter("txt_CustomerName");
    String BranchName = request.getParameter("txt_BranchName");
    String JobKind = request.getParameter("rdo_JobKind");
    String JobContents = request.getParameter("txt_JobContents");
    String TimeS = request.getParameter("entry_timeS");
    String TimeE = request.getParameter("entry_timeE");
    String ExecDaysAll = request.getParameter("chk_ExecDays_All");
    String ExecDaysSun = request.getParameter("chk_ExecDays_Sun");
    String ExecDaysMon = request.getParameter("chk_ExecDays_Mon");
    String ExecDaysTue = request.getParameter("chk_ExecDays_Tue");
    String ExecDaysWed = request.getParameter("chk_ExecDays_Wed");
    String ExecDaysThr = request.getParameter("chk_ExecDays_Thr");
    String ExecDaysFri = request.getParameter("chk_ExecDays_Fri");
    String ExecDaysSat = request.getParameter("chk_ExecDays_Sat");
    String ExecKindSt = request.getParameter("chk_ExecKind_St");
    String ExecKindEd = request.getParameter("chk_ExecKind_Ed");

    //Beansへの値引渡し
    WarnSchedule.SetRealPath(a_realPath);
    WarnSchedule.putJobId(JobId);
    WarnSchedule.putCustomerName(CustomerName);
    WarnSchedule.putBranchName(BranchName);
    WarnSchedule.putJobKind(JobKind);
    WarnSchedule.putJobContents(JobContents);
    WarnSchedule.putTimeS(TimeS);
    WarnSchedule.putTimeE(TimeE);
    WarnSchedule.putExecDaysAll(ExecDaysAll);
    WarnSchedule.putExecDaysSun(ExecDaysSun);
    WarnSchedule.putExecDaysMon(ExecDaysMon);
    WarnSchedule.putExecDaysTue(ExecDaysTue);
    WarnSchedule.putExecDaysWed(ExecDaysWed);
    WarnSchedule.putExecDaysThr(ExecDaysThr);
    WarnSchedule.putExecDaysFri(ExecDaysFri);
    WarnSchedule.putExecDaysSat(ExecDaysSat);
    WarnSchedule.putExecKindSt(ExecKindSt);
    WarnSchedule.putExecKindEd(ExecKindEd);
    
    //DBの更新
    WarnSchedule.EnteryWarnSchedule();
%>

    