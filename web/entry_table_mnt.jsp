<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page import="java.util.ArrayList,java.io.*,java.util.*,java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp" %>
<jsp:useBean id="SetDB" scope="page" class="maintenance.SetDB" />
<%
    //パスを取得
    String SCRIPT_NAME = request.getServletPath();
    String a_mainPath = GetMainPath(SCRIPT_NAME);
    String a_realPath = application.getRealPath(a_mainPath);
    
    //セッション変数
    String a_Mnt_Table = GetSessionValue(session.getAttribute("Mnt_Table"));
    ArrayList<String> a_coldefs = (ArrayList<String>)session.getAttribute("Mnt_Coldefs");
    String[] a_table_split = null;
    String[] a_column_split = null;
    if (a_Mnt_Table != ""){
        a_table_split = a_Mnt_Table.split("\t");
        a_column_split = a_table_split[1].split(",");
    }

    //POSTデータを取得
    String ACT = request.getParameter("ACT");
    String IDX = request.getParameter("IDX");
    ArrayList<String> a_post_data = new ArrayList<String>();
    for (int a_iCnt=0; a_iCnt<a_coldefs.size(); a_iCnt++){
        String[] a_split = a_coldefs.get(a_iCnt).split("\t");
        //カラム名を取得：0番目をメインとする
        String[] a_colNames = a_split[COLUMN_DEF_NAME].split(",");
        String a_colName = a_colNames[0];
        if (request.getParameter(a_colName) != null){
            String a_val = request.getParameter(a_colName);
            if (a_val.length()>0){
                a_post_data.add(a_colName + "\t" + a_val);
            }else{
                a_post_data.add(a_colName + "\t");
            }
        }else{
                a_post_data.add(a_colName + "\t");
        }
    }
    
    //Beansへの値引渡し
    SetDB.SetRealPath(a_realPath);
    
    //DBの更新
    String a_sRet = SetDB.EnteryMnt(a_table_split[0], a_coldefs, a_column_split, ACT, IDX, a_post_data);
    out.print(a_sRet);
%>

    