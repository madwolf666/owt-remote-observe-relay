<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page import="java.util.ArrayList,java.io.*,java.util.*,java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp" %>
<jsp:useBean id="Environ" scope="page" class="common.Environ" />
<jsp:useBean id="SetDB" scope="page" class="maintenance.SetDB" />
<%
    //パスを取得
    String SCRIPT_NAME = request.getServletPath();
    String a_mainPath = GetMainPath(SCRIPT_NAME);
    String a_realPath = application.getRealPath(a_mainPath);
    String a_envPath = "";
    String a_pulldown = "";
    String a_showlist = "";
    String a_findlist = "";
    String a_logdef = "";

    Environ.SetRealPath(a_realPath);
    a_envPath = Environ.GetEnvironValue("mnt_env_path");
    a_pulldown = a_envPath + Environ.GetEnvironValue("mnt_pulldown_info");
    a_showlist = a_envPath + Environ.GetEnvironValue("mnt_showlist_info");
    a_findlist = a_envPath + Environ.GetEnvironValue("mnt_findlist_info");
    a_logdef = a_envPath + Environ.GetEnvironValue("mnt_log_analyze");

    //セッション変数
    //String a_Mnt_Table = GetSessionValue(session.getAttribute("Mnt_Log_Analyze_Table"));
    String[] a_table_split = null;
    String[] a_column_split = null;
    ArrayList<String> a_coldefs = new ArrayList<String>();
    a_coldefs = GetDef_Field(a_logdef);
    /*
    if (a_Mnt_Table != ""){
        a_table_split = a_Mnt_Table.split("\t");
        a_column_split = a_table_split[1].split(",");
        a_coldefs = GetDef_Field(a_envPath + a_table_split[0] + "-analyze.def");
    }
    */
    session.setAttribute("Mnt_Log_Analyze_Coldefs", a_coldefs);

    //POSTデータを取得
    int a_PageNo = Integer.valueOf(request.getParameter("PageNo"));
    //String OrderStartDate = request.getParameter("OrderStartDate");
    //[2017.07.28]
    if (a_PageNo < 0){
        if (GetSessionValue(session.getAttribute("Mnt_Log_Analyze_pageNo")) != ""){
            a_PageNo = Integer.valueOf(GetSessionValue(session.getAttribute("Mnt_Log_Analyze_pageNo")));
        }
    }
    
    //Beansへの値引渡し
    SetDB.SetRealPath(a_realPath);

    //定義情報の読み込み
    ArrayList<String> a_arrayList = null;
    String[] a_find_def = GetDef_FindList(a_findlist, "loganalyzeschedule");
    if (a_find_def != null){
        /*
        if (a_find_def[FINDLIST_FIND_KEY_NAME].equals("") == false){
            if (a_find_key.equals("") == true){
                out.print("NO_FIND_KEY");
                return;
            }
        }
        */
        a_arrayList = SetDB.FindMnt(a_PageNo, a_find_def, "");
    }
    
    //一覧データを取得
    if (a_arrayList != null){
        out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0' width='99%'>");
        //ヘッダ部
        out.print("<tr bgcolor='#003366'>");
        String[] a_items = a_find_def[FINDLIST_ITEM_NAME].split(":");
        //先頭は番号
        out.print("<td style='text-align:center;'><font color='#ffffff'>No.</font></td>");
        for (int a_iCnt=0; a_iCnt<a_items.length; a_iCnt++){
            out.print("<td style='text-align:center;'><font color='#ffffff'>" + a_items[a_iCnt] + "</font></td>");
        }
        out.print("</tr>");
        
        //データ部
        String[] a_split_column = null;
        String[] a_split_type = null;
        String[] a_split_pulldown = null;
        a_split_column = a_find_def[FINDLIST_COLUMN_NAME].split(":");
        a_split_type = a_find_def[FINDLIST_COLUMN_TYPE].split(":");
        a_split_pulldown = a_find_def[FINDLIST_COLUMN_PULLDOWN].split(":");

        for (int a_iCnt=0; a_iCnt<a_arrayList.size(); a_iCnt++){
            String[] a_data = a_arrayList.get(a_iCnt).split("\t");
            if ((a_iCnt % 2)==0){
                out.print("<tr bgcolor='#ffffff'>");
            }else{
                out.print("<tr bgcolor='#fffff0'>");
            }
            for (int a_iCnt2=0; a_iCnt2<a_data.length; a_iCnt2++){
                out.print("<td>");
                if ((a_find_def[FINDLIST_SELECT_KEY_NAME].equals("") == false) && (a_find_def[FINDLIST_SELECT_KEY_NAME].equals(String.valueOf(a_iCnt2 - 1)) == true)){
                        out.print("<a href='#' onclick='make_log_analyze_edit_mnt(\"e\",\"" + a_data[a_iCnt2] + "\");'>");
                }
                //0番目はNo.
                if (a_iCnt2 > 0){
                    //COLUMN DEFを組み立てる
                    String[] a_now_split = null;
                    a_now_split = new String[COLUMN_DEF_ACTION + 1];
                    a_now_split[COLUMN_DEF_FIELD] = "";
                    a_now_split[COLUMN_DEF_NAME] = a_split_column[a_iCnt2 - 1];
                    a_now_split[COLUMN_DEF_TABLE_NAME] = "";
                    a_now_split[COLUMN_DEF_NESS] = "";
                    a_now_split[COLUMN_DEF_TYPE] = a_split_type[a_iCnt2 - 1];
                    a_now_split[COLUMN_DEF_LENGTH] = "";
                    a_now_split[COLUMN_DEF_PULLDOWN] = a_split_pulldown[a_iCnt2 - 1];
                    a_now_split[COLUMN_DEF_COMMENT] = "";
                    a_now_split[COLUMN_DEF_INIT] = "";
                    a_now_split[COLUMN_DEF_ACTION] = "";

                    out.print(Make_Tag_Mnt(a_envPath, false, false, false, "l", a_now_split, null, a_pulldown, a_showlist, a_data[a_iCnt2]));
                }else{
                    out.print(a_data[a_iCnt2]);
                }
                if ((a_find_def[FINDLIST_SELECT_KEY_NAME].equals("") == false) && (a_find_def[FINDLIST_SELECT_KEY_NAME].equals(String.valueOf(a_iCnt2 - 1)) == true)){
                    out.print("</a>");
                }
                out.print("</td>");
            }
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

    