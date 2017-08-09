<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page import="java.util.ArrayList"%>
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
    String a_showlist = "";

   //POSTデータを取得
    int a_Kind = 9; //Integer.valueOf(request.getParameter("Kind"));
    int a_PageNo = Integer.valueOf(request.getParameter("PageNo"));
    String a_col_name = request.getParameter("col_name");
    String a_find_key = request.getParameter("find_key");
    
    Environ.SetRealPath(a_realPath);
    a_envPath = Environ.GetEnvironValue("mnt_env_path");
    a_showlist = a_envPath + Environ.GetEnvironValue("mnt_showlist_info");

    //Beansへの値引渡し
    SetDB.SetRealPath(a_realPath);

    //定義情報の読み込み
    ArrayList<String> a_arrayList = null;
    String[] a_show_def = GetDef_ShowList(a_showlist, a_col_name);
    if (a_show_def != null){
        if (a_show_def[SHOWLIST_FIND_KEY_NAME].equals("") == false){
            if (a_find_key.equals("") == true){
                out.print("NO_FIND_KEY");
                return;
            }
        }
        a_arrayList = SetDB.ShowList(a_PageNo, a_show_def, a_find_key);
    }
        
    String a_sOut = SetDB.MakePagerShowList(a_Kind, a_PageNo, a_show_def, a_find_key);
    out.print(a_sOut);
    
    //一覧データを取得
    if (a_arrayList != null){
        out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0'>");
        //ヘッダ部
        out.print("<tr bgcolor='#003366'>");
        String[] a_items = a_show_def[SHOWLIST_ITEM_NAME].split(",");
        //先頭は番号
        out.print("<td style='text-align:center;'><font color='#ffffff'>No.</font></td>");
        for (int a_iCnt=0; a_iCnt<a_items.length; a_iCnt++){
            out.print("<td style='text-align:center;'><font color='#ffffff'>" + a_items[a_iCnt] + "</font></td>");
        }
        out.print("</tr>");
    
        //データ部
        for (int a_iCnt=0; a_iCnt<a_arrayList.size(); a_iCnt++){
            String[] a_split = a_arrayList.get(a_iCnt).split("\t");
            out.print("<tr bgcolor='#ffffff'>");
            for (int a_iCnt2=0; a_iCnt2<a_split.length; a_iCnt2++){
                out.print("<td>");
                if ((a_show_def[SHOWLIST_SELECT_KEY_NAME].equals("") == false) && (a_show_def[SHOWLIST_SELECT_KEY_NAME].equals(String.valueOf(a_iCnt2 - 1)) == true)){
                        out.print("<a href='#' onclick=\"select_show_list('" + a_show_def[SHOWLIST_COLUMN_NAME] + "','" + a_split[a_iCnt2] + "');\">");
                }
                out.print(a_split[a_iCnt2]);
                if ((a_show_def[SHOWLIST_SELECT_KEY_NAME].equals("") == false) && (a_show_def[SHOWLIST_SELECT_KEY_NAME].equals(String.valueOf(a_iCnt2 - 1)) == true)){
                    out.print("</a>");
                }
                out.print("</td>");
            }
            out.print("</tr>");
        }
       
        out.print("</table>");
    }

    out.print("<a href='#' onclick=\"$('.popup').hide();\">閉じる</a>");
%>

    