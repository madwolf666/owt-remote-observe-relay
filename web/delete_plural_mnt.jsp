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
    ArrayList<String> a_coldefs = null;
    if (a_Mnt_Table != ""){
        a_table_split = a_Mnt_Table.split("\t");
        a_column_split = a_table_split[1].split(",");
    }

    //POSTデータを取得
    String a_mode = request.getParameter("mode");
    String a_is_edit = request.getParameter("is_edit");
    String a_user_code = request.getParameter("user_code");
    String a_seq = request.getParameter("seq");
    if (a_seq == null){
        a_seq = "-1";
    }

    //Beansへの値引渡し
    SetDB.SetRealPath(a_realPath);

    if (a_table_split[0].equals("irmsremotecustomer") == true){
        if (a_mode.equals("1") == true){
            a_coldefs = (ArrayList<String>)session.getAttribute("Mnt_Coldefs_LTIC_TN");
        }else if (a_mode.equals("2") == true){
            a_coldefs = (ArrayList<String>)session.getAttribute("Mnt_Coldefs_User_Machine");
        }else if (a_mode.equals("3") == true){
            a_coldefs = (ArrayList<String>)session.getAttribute("Mnt_Coldefs_Machine_Code");
        }
    }

    String a_sRet = "";
    ArrayList<String>[] a_arrayList_src = null;
    ArrayList<String>[] a_arrayList_dst = null;
    if (a_user_code.equals("") == false){
        //DBの更新
        //String a_sRet = SetDB.EnteryMnt(a_Mnt_Table, a_coldefs, ACT, IDX, a_post_data);
    }else{
        if (a_mode.equals("1") == true){
            a_arrayList_src = (ArrayList<String>[])session.getAttribute("Mnt_Data_LTIC_TN");
        }else if (a_mode.equals("2") == true){
            a_arrayList_src = (ArrayList<String>[])session.getAttribute("Mnt_Data_User_Machine");
        }else if (a_mode.equals("3") == true){
            a_arrayList_src = (ArrayList<String>[])session.getAttribute("Mnt_Data_Machine_Code");
        }
        
        if (a_seq.equals("-1") == false){
            //更新
            if (a_arrayList_src.length == 1){
                a_arrayList_dst = null;
            }else{
                a_arrayList_dst = new ArrayList[a_arrayList_src.length - 1];
                int a_idx = 0;
                for (int a_iCnt=0; a_iCnt<a_arrayList_src.length; a_iCnt++){
                    if (a_iCnt != Integer.valueOf(a_seq)){
                        a_arrayList_dst[a_idx] = a_arrayList_src[a_iCnt];
                        a_idx++;
                    }
                }
            }
        }
        
        if (a_mode.equals("1") == true){
            session.setAttribute("Mnt_Data_LTIC_TN", a_arrayList_dst);
        }else if (a_mode.equals("2") == true){
            session.setAttribute("Mnt_Data_User_Machine", a_arrayList_dst);
        }else if (a_mode.equals("3") == true){
            session.setAttribute("Mnt_Data_Machine_Code", a_arrayList_dst);
        }
    }
    
    out.print(a_sRet);
%>

    