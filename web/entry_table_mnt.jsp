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
        String[] a_colNames = a_split[COLUMN_DEF_NAME].split(":");
        String a_colName = a_colNames[0];
        String a_field = a_split[COLUMN_DEF_FIELD];
        if (a_split[COLUMN_DEF_PULLDOWN].indexOf("p")>=0){
            String a_plural = a_envPath + a_field + ".def";
            ArrayList<String> a_plurals = GetDef_Plurals(a_plural);
            String a_plural_data = "";
            if (a_plurals != null){
                for (int a_iCnt2=1; a_iCnt2<a_plurals.size(); a_iCnt2++){
                    String[] a_split2 = a_plurals.get(a_iCnt2).split("\t");
                    String[] a_split3 = a_split2[COLUMN_DEF_FIELD].split(":");
                    if (a_iCnt2 > 1){
                        a_plural_data += "\b\b\b";
                    }
                    for (int a_iCnt3=0; a_iCnt3<a_split3.length; a_iCnt3++){
                        //該当番目の定義を組み立て
                        String[] a_now_split = new String[COLUMN_DEF_ACTION + 1];
                        for (int a_iCnt4=0; a_iCnt4<COLUMN_DEF_ACTION + 1; a_iCnt4++){
                            String[] a_split4 = a_split2[a_iCnt4].split(":");
                            a_now_split[a_iCnt4] = "";
                            if (a_split4.length > a_iCnt3){
                                a_now_split[a_iCnt4] = a_split4[a_iCnt3];
                            }
                        }
                        if (a_iCnt3 > 0){
                            a_plural_data += "\b\b";
                        }
                        a_field = a_now_split[COLUMN_DEF_FIELD];
                        if (request.getParameter(a_field) != null){
                            String a_val = HtmlEncode(request.getParameter(a_field));
                            if (a_val.length()>0){
                                a_plural_data += a_field + "\b" + a_val;
                            }else{
                                a_plural_data += a_field + "\b ";
                            }
                        }else{
                                a_plural_data += a_field + "\b ";
                        }
                    }
                }
            }
            a_post_data.add(a_field + "\t" + a_plural_data);
        }else{
            if (request.getParameter(a_field) != null){
                String a_val = HtmlEncode(request.getParameter(a_field));
                if (a_val.length()>0){
                    a_post_data.add(a_field + "\t" + a_val);
                }else{
                    a_post_data.add(a_field + "\t");
                }
            }else{
                    a_post_data.add(a_field + "\t");
            }
        }
    }
    
    //Beansへの値引渡し
    SetDB.SetRealPath(a_realPath);
    
    //DBの更新
    String[] a_sRets = SetDB.EntryMnt(a_Mnt_Table, a_coldefs, ACT, IDX, a_post_data);
    String a_sRet = "";
    if (a_sRets[0].equals("") == true){
        if (a_table_split[0].equals("pbxremotecustomer") == true){
            ArrayList<String> a_plurals = GetDef_Plurals(a_envPath + "prt_status.def");
            a_sRet = SetDB.EntryRPTMnt(a_plurals, ACT, a_sRets[1], a_post_data.get(a_post_data.size() - 1));
        }
        if (a_table_split[0].equals("irmsremotecustomer") == true){
            session.setAttribute("Mnt_Data_LTIC_TN", null);
            session.setAttribute("Mnt_Data_User_Machine", null);
            /*session.setAttribute("Mnt_Data_Machine_Code", null);*/
        }
    }else{
        a_sRet = a_sRets[0];
    }

    out.print(a_sRet);
%>

    