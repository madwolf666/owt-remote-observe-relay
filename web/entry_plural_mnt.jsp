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
    String a_mytable = "";
    String a_idx = "";
    String a_user_ipaddr = request.getParameter("opt1");;
    
    //Beansへの値引渡し
    SetDB.SetRealPath(a_realPath);

    if (a_table_split[0].equals("irmsremotecustomer") == true){
        if (a_mode.equals("1") == true){
            a_mytable = "trunkandlticinformation\tusercode:s:n:y";
            a_coldefs = (ArrayList<String>)session.getAttribute("Mnt_Coldefs_LTIC_TN");
        }else if (a_mode.equals("2") == true){
            a_mytable = "usernode\tusercode:s:n:y";
            a_coldefs = (ArrayList<String>)session.getAttribute("Mnt_Coldefs_User_Machine");
        /*}else if (a_mode.equals("3") == true){
            a_coldefs = (ArrayList<String>)session.getAttribute("Mnt_Coldefs_Machine_Code");*/
        }
    }

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
                if (a_colName.equals("usercode") == true){
                    if (a_user_code.equals("") == false){
                        a_val = a_user_code;
                    }
                }
                if (a_colName.equals("id") == true){
                    if (a_user_code.equals("") == false){
                        a_idx = a_val;
                    }
                }
                /*if (a_colName.equals("useripaddr") == true){
                    if (a_user_code.equals("") == false){
                        a_user_ipaddr = a_val;
                    }
                }*/
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

    String a_sRet = "";
    ArrayList<String>[] a_arrayList_src = null;
    ArrayList<String>[] a_arrayList_dst = null;
    if (a_user_code.equals("") == false){
        String[] a_sRets = null;
        if (a_seq.equals("-1") == false){
            //更新
            if (a_mode.equals("1") == true){
                a_sRets = SetDB.EntryMnt(a_mytable, a_coldefs, "e", a_idx, a_post_data);
            }else{
                a_sRets = SetDB.EntryMnt(a_mytable, a_coldefs, "e", a_user_code + "," + a_user_ipaddr, a_post_data);
            }
        }else{
            //新規
            a_sRets = SetDB.EntryMnt(a_mytable, a_coldefs, "n", a_user_code, a_post_data);
        }
        if (a_sRets[0].equals("") == true){
        }else{
            a_sRet = a_sRets[0];
        }
    }else{
        if (a_mode.equals("1") == true){
            a_arrayList_src = (ArrayList<String>[])session.getAttribute("Mnt_Data_LTIC_TN");
        }else if (a_mode.equals("2") == true){
            a_arrayList_src = (ArrayList<String>[])session.getAttribute("Mnt_Data_User_Machine");
        /*}else if (a_mode.equals("3") == true){
            a_arrayList_src = (ArrayList<String>[])session.getAttribute("Mnt_Data_Machine_Code");*/
        }
        
        if (a_seq.equals("-1") == false){
            //更新
            a_arrayList_dst = new ArrayList[a_arrayList_src.length];
            for (int a_iCnt=0; a_iCnt<a_arrayList_src.length; a_iCnt++){
                a_arrayList_dst[a_iCnt] = a_arrayList_src[a_iCnt];
            }
            a_arrayList_dst[Integer.valueOf(a_seq)] = a_post_data;
        }else{
            //追加
            if (a_arrayList_src != null){
                a_arrayList_dst = new ArrayList[a_arrayList_src.length + 1];
                for (int a_iCnt=0; a_iCnt<a_arrayList_src.length; a_iCnt++){
                    a_arrayList_dst[a_iCnt] = a_arrayList_src[a_iCnt];
                }
            }else{
                a_arrayList_dst = new ArrayList[1];
            }
            a_arrayList_dst[a_arrayList_dst.length - 1] = a_post_data;
        }
        
        if (a_mode.equals("1") == true){
            session.setAttribute("Mnt_Data_LTIC_TN", a_arrayList_dst);
        }else if (a_mode.equals("2") == true){
            session.setAttribute("Mnt_Data_User_Machine", a_arrayList_dst);
        /*}else if (a_mode.equals("3") == true){
            session.setAttribute("Mnt_Data_Machine_Code", a_arrayList_dst);*/
        }
    }
    
    out.print(a_sRet);
%>

    