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
    if (a_Mnt_Table.equals("") == false){
        a_table_split = a_Mnt_Table.split("\t");
        a_column_split = a_table_split[1].split(",");
    }
    
    //QueryStringの取得
    String a_ACT = request.getParameter("ACT");
    String a_IDX = request.getParameter("IDX");
    String a_DB = request.getParameter("DB");

    //Beansへの値引渡し
    SetDB.SetRealPath(a_realPath);

    //登録済データを取得
    ArrayList<String> a_arrayList = null;
    out.print("<input type='hidden' id='edit_idx' name='edit_idx' value='");
    if ((a_ACT.equals("e") == true) && (a_DB.equals("1") == true)){
        a_arrayList = SetDB.GetMnt(a_Mnt_Table, a_coldefs, a_IDX);
        out.print(a_IDX);
    }else{
        if (a_DB.equals("0") == true){
            a_arrayList = new ArrayList<String>();
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
                                    String a_val = request.getParameter(a_field);
                                    //String a_val = HtmlEncode(request.getParameter(a_field));
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
                    a_arrayList.add(a_field + "\t" + a_plural_data);
                }else{
                    if (request.getParameter(a_field) != null){
                        String a_val = request.getParameter(a_field);
                        //String a_val = HtmlEncode(request.getParameter(a_field));
                        if (a_val.length()>0){
                            a_arrayList.add(a_field + "\t" + a_val);
                        }else{
                            a_arrayList.add(a_field + "\t");
                        }
                    }else{
                            a_arrayList.add(a_field + "\t");
                    }
                }
            }
        }
    }
    out.print("'>");
    out.print("<input type='hidden' id='txt_act' name='txt_act' value='" + a_ACT + "'>");
    
    //out.print("<div id='my-result' style='font-color:#ff0000;'></div>");
    
    g_JScript_Val_Auto = "";
    //g_JScript_Val_Ness = "";
    g_JScript_Program = "";
    g_JScript_IsNumeric = "";
    g_JScript_IsRequired = "";
    g_Post_Data = "";
    
    out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0' style='width:660px;'>");
    //out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0' style='width:768px;'>");

    String a_monitoring_id = "";
    String[] a_prt_status_def = null;
    //データ部
    if (a_arrayList != null){
        String a_user_code = "";
        for (int a_iCnt=0; a_iCnt<a_coldefs.size(); a_iCnt++){
            boolean a_isOK = true;
            String[] a_split = a_coldefs.get(a_iCnt).split("\t");
            String[] a_edit = a_arrayList.get(a_iCnt).split("\t");
            String[] a_colNames = a_split[COLUMN_DEF_NAME].split(":");
            String a_colName = a_colNames[0];
            String a_val = "";
            if (a_edit.length > 1){
                a_val = a_edit[1];
            }
            if (a_table_split[0].equals("pbxremotecustomer") == true){
                if ((a_ACT.equals("e") == true) && (a_DB.equals("1") == true)){
                    if (a_colName.equals("prt_status") == true){
                        a_prt_status_def = a_split;
                        a_isOK = false;
                    }
                    if ((a_iCnt+1)>=a_coldefs.size()){
                        a_monitoring_id = a_val;
                        break;
                    }
                }
            }
            if (a_isOK == true){
                if ((a_colName.equals("usercode") == true) && a_split[COLUMN_DEF_NESS].equals("a") == true){
                    a_user_code = a_val;
                }
                out.print("<tr>");
                out.print("<td bgcolor='#003366' style='text-align:left;' nowrap><font color='#ffffff'>" + a_split[COLUMN_DEF_COMMENT] + "</font>");
                if (a_split[COLUMN_DEF_NESS].indexOf("y")>=0){
                    out.print("<font color='#ffff00'>*</font>");
                }
                out.print("</td>");
                
                out.print("<td bgcolor='transparent' style='text-align:left;'>");

                boolean a_isFixed = false;
                if (a_table_split[0].equals("irmsremotecustomer") == true){
                    if (a_colName.equals("remotesetid") == true){
                        //a_val = "4";
                        a_isFixed = true;
                    }
                    if (a_colName.equals("usernumber") == true){
                        //a_val = "";
                        a_isFixed = true;
                    }
                }
                
                if (a_isFixed == false){
                    out.print(Make_Tag_Mnt(a_envPath, true, true, false, a_ACT, a_split, a_column_split, a_pulldown, a_showlist, a_val));
                }else{
                    out.print(Make_Tag_Mnt(a_envPath, true, false, false, a_ACT, a_split, a_column_split, a_pulldown, a_showlist, a_val));
                }

                out.print("</td>");
                
                out.print("</tr>");
            }
        }
        if (a_table_split[0].equals("pbxremotecustomer") == true){
            if ((a_ACT.equals("e") == true) && (a_DB.equals("1") == true)){
                //PBXユーザ管理
                //RPT使用状況
                ArrayList<String> a_plurals = GetDef_Plurals(a_envPath + "prt_status.def");
                String a_prt_status_data = SetDB.GetRPTMnt(a_plurals, a_monitoring_id);
                out.print("<tr>");
                out.print("<td bgcolor='#003366' style='text-align:left;' nowrap><font color='#ffffff'>" + a_prt_status_def[COLUMN_DEF_COMMENT] + "</font>");
                out.print("<td bgcolor='transparent' style='text-align:left;'>" + Make_Tag_Mnt(a_envPath, true, true, false, a_ACT, a_prt_status_def, a_column_split, a_pulldown, a_showlist, a_prt_status_data) + "</td>");
                out.print("</tr>");
            }
        }
        if (a_table_split[0].equals("irmsremotecustomer") == true){
            //IRMSユーザ管理
            out.print("<tr>");
            out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>LTIC・TN拠点設定</font></td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'><input type='button' value='拠点設定' onclick='set_irms_plural(\"1\", \"1\", \"" + a_user_code + "\");'></td>");
            out.print("</tr>");
            out.print("<tr>");
            out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>ユーザ機器登録</font></td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'><input type='button' value='機器設定' onclick='set_irms_plural(\"2\", \"1\", \"" + a_user_code + "\");'></td>");
            out.print("</tr>");
            /*out.print("<tr>");
            out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>機器コード設定</font></td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'><input type='button' value='機器コード登録' onclick='set_irms_plural(\"3\", \"1\", \"" + a_user_code + "\");'></td>");
            out.print("</tr>");*/
        }
    }else{
        for (int a_iCnt=0; a_iCnt<a_coldefs.size(); a_iCnt++){
            String[] a_split = a_coldefs.get(a_iCnt).split("\t");
            //splitは値が入っている所までしかlengthが返らない[2017.07.31]
            //カラム名を取得：0番目をメインとする
            String[] a_colNames = a_split[COLUMN_DEF_NAME].split(":");
            String a_colName = a_colNames[0];
            String a_val = "";
            /*
            if ((a_colName.equals("usercode") == true) && a_split[COLUMN_DEF_NESS].equals("a") == true){
                //PBXリモートの場合は、0XXXXの最老番号+1
                //IRMASの場合は、1XXXXの最老番号+1
                a_val = SetDB.GetNextUserCode(a_table_split[0]);
            }
            */
            out.print("<tr>");
            out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>" + a_split[COLUMN_DEF_COMMENT] + "</font>");
            if (a_split[COLUMN_DEF_NESS].indexOf("y")>=0){
                out.print("<font color='#ffff00'>*</font>");
            }
            out.print("</td>");

            out.print("<td bgcolor='transparent' style='text-align:left;'>");
            boolean a_isFixed = false;
            if (a_table_split[0].equals("irmsremotecustomer") == true){
                if (a_colName.equals("remotesetid") == true){
                    a_val = "4";
                    a_isFixed = true;
                }
                if (a_colName.equals("usernumber") == true){
                    a_val = "";
                    a_isFixed = true;
                }
            }

            if (a_isFixed == false){
                out.print(Make_Tag_Mnt(a_envPath, true, true, true, a_ACT, a_split, a_column_split, a_pulldown, a_showlist, a_val));
            }else{
                out.print(Make_Tag_Mnt(a_envPath, true, false, true, a_ACT, a_split, a_column_split, a_pulldown, a_showlist, a_val));
            }
            out.print("</td>");
            out.print("</tr>");
        }
        if (a_table_split[0].equals("irmsremotecustomer") == true){
            //IRMSユーザ管理
            out.print("<tr>");
            out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>LTIC・TN拠点設定</font></td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'><input type='button' value='拠点設定' onclick='set_irms_plural(\"1\", \"1\", \"\");'></td>");
            out.print("</tr>");
            out.print("<tr>");
            out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>ユーザ機器登録</font></td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'><input type='button' value='機器設定' onclick='set_irms_plural(\"2\", \"1\", \"\");'></td>");
            out.print("</tr>");
            /*out.print("<tr>");
            out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>機器コード設定</font></td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'><input type='button' value='機器コード登録' onclick='set_irms_plural(\"3\", \"1\", \"\");'></td>");
            out.print("</tr>");*/
        }
    }
    
    out.print("</table>");

    out.print(OutCopyRight());
    
    //java script
    out.print("<script type='text/javascript'>");
    out.print("resize_tbl_list();");
    out.print("var g_val_auto = '" + g_JScript_Val_Auto + "';");
    //out.print("var g_val_ness = '" + g_JScript_Val_Ness + "';" );
    out.print(g_JScript_Program);
    //out.print("alert(g_val_auto);");
    //out.print("alert(g_val_ness);");
    /*
    if (a_ACT.equals("n") == true){
        out.print("$(\"#delete-mnt\").hide();");
    }else{
        out.print("$(\"#delete-mnt\").show();");
    }
    */
    out.print(Make_Confirm_Table_Mnt(a_ACT, a_IDX));
    out.print("</script>");
    
%>
    