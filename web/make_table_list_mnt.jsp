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

    Environ.SetRealPath(a_realPath);
    a_envPath = Environ.GetEnvironValue("mnt_env_path");
    a_pulldown = a_envPath + Environ.GetEnvironValue("mnt_pulldown_info");
    a_showlist = a_envPath + Environ.GetEnvironValue("mnt_showlist_info");

    //セッション変数
    String a_Mnt_Table = GetSessionValue(session.getAttribute("Mnt_Table"));
    String[] a_table_split = null;
    String[] a_column_split = null;
    ArrayList<String> a_coldefs = new ArrayList<String>();
    ArrayList<String> a_coldefs_ltic_tn = new ArrayList<String>();
    ArrayList<String> a_coldefs_user_machine = new ArrayList<String>();
    ArrayList<String> a_coldefs_machine_code = new ArrayList<String>();
    if (a_Mnt_Table != ""){
        a_table_split = a_Mnt_Table.split("\t");
        a_column_split = a_table_split[1].split(",");
        a_coldefs = GetDef_Field(a_envPath + a_table_split[0] + ".def");

        if (a_table_split[0].equals("irmsremotecustomer") == true){
            a_coldefs_ltic_tn = GetDef_Field(a_envPath + "ltic_tn.def");
            a_coldefs_user_machine = GetDef_Field(a_envPath + "user_machine.def");
            a_coldefs_machine_code = GetDef_Field(a_envPath + "machine_code.def");
        }
    }
    session.setAttribute("Mnt_Coldefs", a_coldefs);
    
    if (a_table_split[0].equals("irmsremotecustomer") == true){
        session.setAttribute("Mnt_Coldefs_LTIC_TN", a_coldefs_ltic_tn);
        session.setAttribute("Mnt_Coldefs_User_Machine", a_coldefs_user_machine);
        session.setAttribute("Mnt_Coldefs_Machine_Code", a_coldefs_machine_code);
        session.setAttribute("Mnt_Data_LTIC_TN", null);
        session.setAttribute("Mnt_Data_User_Machine", null);
        session.setAttribute("Mnt_Data_Machine_Code", null);
    }

    //POSTデータを取得
    int a_PageNo = Integer.valueOf(request.getParameter("PageNo"));
    //String OrderStartDate = request.getParameter("OrderStartDate");
    //[2017.07.28]
    if (a_PageNo < 0){
        if (GetSessionValue(session.getAttribute("Mnt_pageNo")) != ""){
            a_PageNo = Integer.valueOf(GetSessionValue(session.getAttribute("Mnt_pageNo")));
        }
    }
    
    //Beansへの値引渡し
    SetDB.SetRealPath(a_realPath);

    //一覧データを取得
    ArrayList<String> a_arrayList = null;
    if ((a_table_split[0].equals("pbxremotecustomer") == true) || (a_table_split[0].equals("irmsremotecustomer") == true)){
        a_coldefs = GetDef_Field(a_envPath + a_table_split[0] + "_find.def");
    }else{
    }
    a_arrayList = SetDB.FindMnt(a_Mnt_Table, a_PageNo, a_coldefs);
    
    if (a_arrayList != null){
        out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0'>");
        //ヘッダ部
        out.print("<tr bgcolor='#003366'>");
        out.print("<td style='text-align:center;'><font color='#ffffff'>No.</font></td>");
        for (int a_iCnt=0; a_iCnt<a_coldefs.size(); a_iCnt++){
            String[] a_split = a_coldefs.get(a_iCnt).split("\t");
            out.print("<td style='text-align:center;'><font color='#ffffff'>" + a_split[COLUMN_DEF_COMMENT] + "</font></td>");
        }
        out.print("</tr>");
        
        //データ部
        for (int a_iCnt=0; a_iCnt<a_arrayList.size(); a_iCnt++){
            String[] a_data = a_arrayList.get(a_iCnt).split("\t");
            if ((a_iCnt % 2)==0){
                out.print("<tr bgcolor='#ffffff'>");
            }else{
                out.print("<tr bgcolor='#fffff0'>");
            }
            out.print("<td>" + String.valueOf((a_PageNo-1)*Integer.valueOf(Environ.GetEnvironValue("max_line_page")) + a_iCnt + 1) + "</td>");
            for (int a_iCnt2=0; a_iCnt2<a_coldefs.size(); a_iCnt2++){
                String[] a_split = a_coldefs.get(a_iCnt2).split("\t");
                if (a_data.length > a_iCnt2){
                    out.print("<td>");
                    out.print(Make_Tag_Mnt(a_envPath, true, false, false, "l", a_split, a_column_split, a_pulldown, a_showlist, a_data[a_iCnt2]));
                    //out.print(a_data[a_iCnt2]);
                    out.print("</td>");
                }else{
                    out.print("<td>&nbsp;</td>");
                }
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

    