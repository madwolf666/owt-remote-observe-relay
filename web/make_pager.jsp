<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp" %>
<jsp:useBean id="Environ" scope="page" class="common.Environ" />
<jsp:useBean id="RemoteTrouble" scope="page" class="remote.RemoteTrouble" />
<jsp:useBean id="ReportMonthly" scope="page" class="report.ReportMonthly" />
<jsp:useBean id="WarnSchedule" scope="page" class="warn.WarnSchedule" />
<jsp:useBean id="SetDB" scope="page" class="maintenance.SetDB" />
<%
    //パスを取得
    String SCRIPT_NAME = request.getServletPath();
    String a_mainPath = GetMainPath(SCRIPT_NAME);
    String a_realPath = application.getRealPath(a_mainPath);
    String a_envPath = "";
    String a_showlist = "";
    String a_findlist = "";

    Environ.SetRealPath(a_realPath);
    a_envPath = Environ.GetEnvironValue("mnt_env_path");
    a_showlist = a_envPath + Environ.GetEnvironValue("mnt_showlist_info");
    a_findlist = a_envPath + Environ.GetEnvironValue("mnt_findlist_info");

    //POSTデータを取得
    int a_Kind = Integer.valueOf(request.getParameter("Kind"));
    int a_PageNo = Integer.valueOf(request.getParameter("PageNo"));

    String UserCode = "";
    String SetNo = "";
    String RecNo = "";
    String UserName = "";
    String TimeS = "";
    String TimeE = "";
    String TroubleMsg = "";
    String TroubleKind_MJ = "";
    String TroubleKind_MN = "";
    String TroubleKind_GN = "";
    String Contact = "";

    String Mnt_Table = "";  //[2017.07.27]
    String Mnt_Find = "";
    int Mnt_pageNo = -1;    //[2017.07.27]
    String a_col_name = "";
    String a_find_key = "";
    String[] a_find_def = null;
    
    String a_sOut = "";
    //int a_list_sum = 0;
    //Beansへの値引渡し
    switch (a_Kind){
        case 1: //1：SFケース詳細
            //Session変数を取得
            String a_sCaseNo = "";
            String a_sCaseNoKey = "";
            if (session.getAttribute("CaseNo") != null){
                a_sCaseNo = String.valueOf(session.getAttribute("CaseNo"));
                String a_split[] = a_sCaseNo.split("\t");
                a_sCaseNoKey = a_split[0];
                try{
                    //頭1文字目がSET-NO
                    a_split = a_sCaseNoKey.split("-");
                    SetNo = a_split[0] + "-SET";
                    RecNo = a_split[1];
                }catch(Exception e){
                    
                }
            }
            
            RemoteTrouble.SetRealPath(a_realPath);
            RemoteTrouble.putSetNo(SetNo);
            RemoteTrouble.putRecNo(RecNo);
            a_sOut = RemoteTrouble.MakePagerRemoteTrouble(a_Kind, a_PageNo);
            //a_sOut = RemoteTrouble.MakePagerSF(a_Kind, a_PageNo);
            break;
        case 2: //2：リモート発報障害検索
            //POSTデータを取得
            /*
            UserCode = request.getParameter("txt_UserCode");
            SetNo = request.getParameter("txt_SetNo");
            RecNo = request.getParameter("txt_RecNo");
            UserName = request.getParameter("txt_UserName");
            TimeS = request.getParameter("entry_timeS");
            TimeE = request.getParameter("entry_timeE");
            TroubleMsg = request.getParameter("txt_TroubleMsg");
            TroubleKind_MJ = request.getParameter("chk_TroubleKind_MJ");
            TroubleKind_MN = request.getParameter("chk_TroubleKind_MN");
            TroubleKind_GN = request.getParameter("chk_TroubleKind_GN");
            Contact = request.getParameter("chk_Contact");
            */
            //セッション変数を取得
            UserCode = GetSessionValue(session.getAttribute("txt_UserCode"));
            SetNo = GetSessionValue(session.getAttribute("txt_SetNo"));
            RecNo = GetSessionValue(session.getAttribute("txt_RecNo"));
            UserName = GetSessionValue(session.getAttribute("txt_UserName"));
            TimeS = GetSessionValue(session.getAttribute("entry_timeS"));
            TimeE = GetSessionValue(session.getAttribute("entry_timeE"));
            TroubleMsg = GetSessionValue(session.getAttribute("txt_TroubleMsg"));
            TroubleKind_MJ = GetSessionValue(session.getAttribute("chk_TroubleKind_MJ"));
            TroubleKind_MN = GetSessionValue(session.getAttribute("chk_TroubleKind_MN"));
            TroubleKind_GN = GetSessionValue(session.getAttribute("chk_TroubleKind_GN"));
            Contact = GetSessionValue(session.getAttribute("chk_Contact"));

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

            a_sOut = RemoteTrouble.MakePagerRemoteTrouble(a_Kind, a_PageNo);
            break;
        case 3: //3：リモート月報作成
            ReportMonthly.SetRealPath(a_realPath);
            a_sOut = ReportMonthly.MakePagerUsers(a_Kind, a_PageNo);
            break;
        case 4: //4：作業・停電スケジュール
            WarnSchedule.SetRealPath(a_realPath);
            a_sOut = WarnSchedule.MakePagerWarnSchedule(a_Kind, a_PageNo);
            break;
        case 5: //5：LOG_CHECK
            WarnSchedule.SetRealPath(a_realPath);
            a_sOut = WarnSchedule.MakePagerAlarmLog(a_Kind, a_PageNo);
            break;
        case 6: //ユーザ選択
            //POSTデータを取得
            UserName = request.getParameter("txt_UserName");

            //Beansへの値引渡し
            RemoteTrouble.SetRealPath(a_realPath);
            RemoteTrouble.putUserName(UserName);

            a_sOut = RemoteTrouble.MakePagerUser(a_Kind, a_PageNo);
            break;
        case 7: //リモートDB設定[2017.07.27]
            String[] a_table_split = null;
            Mnt_Table = GetSessionValue(session.getAttribute("Mnt_Table"));
            Mnt_Find = GetSessionValue(session.getAttribute("Mnt_Find_Condition"));
            if (Mnt_Table.equals("") == false){
                a_table_split = Mnt_Table.split("\t");

                if (a_PageNo < 0){
                    if (GetSessionValue(session.getAttribute("Mnt_pageNo")) != ""){
                        a_PageNo = Integer.valueOf(GetSessionValue(session.getAttribute("Mnt_pageNo")));
                    }
                }
                SetDB.SetRealPath(a_realPath);
                //定義情報の読み込み
                a_find_def = GetDef_FindList(a_findlist, a_table_split[0]);

                a_sOut = SetDB.MakePagerMnt(a_Kind, a_PageNo, a_find_def, Mnt_Find);
                session.setAttribute("Mnt_pageNo", a_PageNo);   //[2017.07.28]
            }
            break;
        case 8: //保全[2017.08.02]
            Mnt_Table = GetSessionValue(session.getAttribute("Mnt_Log_Analyze_Table"));
            //[2017.07.28]
            if (a_PageNo < 0){
                if (GetSessionValue(session.getAttribute("Mnt_Log_Analyze_pageNo")) != ""){
                    a_PageNo = Integer.valueOf(GetSessionValue(session.getAttribute("Mnt_Log_Analyze_pageNo")));
                }
            }
            SetDB.SetRealPath(a_realPath);
            //定義情報の読み込み
            a_find_def = GetDef_FindList(a_findlist, "loganalyzeschedule");
            a_sOut = SetDB.MakePagerMnt(a_Kind, a_PageNo, a_find_def, "");
            session.setAttribute("Mnt_Log_Analyze_pageNo", a_PageNo);   //[2017.07.28]
            break;
        case 9: //リスト表示[2017.08.09]
            a_col_name = request.getParameter("col_name");
            a_find_key = request.getParameter("find_key");
            //Beansへの値引渡し
            SetDB.SetRealPath(a_realPath);

            //定義情報の読み込み
            ArrayList<String> a_arrayList = null;
            String[] a_show_def = GetDef_ShowList(a_showlist, a_col_name);
            if (a_show_def != null){
                a_sOut = SetDB.MakePagerShowList(a_Kind, a_PageNo, a_show_def, a_find_key);
            }
    }

    out.print(a_sOut);
    //out.print("&nbsp;&nbsp;件数：" + a_list_sum + "件");
%>

    