<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page import="java.util.ArrayList,java.io.*,java.util.*,java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp" %>
<jsp:useBean id="Environ" scope="page" class="common.Environ" />
<jsp:useBean id="AnalyzeLog" scope="page" class="maintenance.AnalyzeLog" />
<%
    //パスを取得
    String SCRIPT_NAME = request.getServletPath();
    String a_mainPath = GetMainPath(SCRIPT_NAME);
    String a_realPath = application.getRealPath(a_mainPath);
    String a_envPath = "";

    Environ.SetRealPath(a_realPath);
    a_envPath = Environ.GetEnvironValue("mnt_env_path");

    //セッション変数
    String a_Mnt_Table = GetSessionValue(session.getAttribute("Mnt_Log_Analyze_Table"));
    ArrayList<String> a_columns = (ArrayList<String>)session.getAttribute("Mnt_Log_Analyze_Columns");
    String[] a_table_split = null;
    String[] a_column_split = null;
    ArrayList<String> a_coldefs = new ArrayList<String>();
    if (a_Mnt_Table.length() > 0){
        a_table_split = a_Mnt_Table.split("\t");
        a_column_split = a_table_split[1].split(",");
        
        //該当テーブルの定義情報を読み込む
        try{
            FileInputStream a_fs = new FileInputStream(a_envPath + a_table_split[0] + "-analyze.def");
            InputStreamReader a_isr = new InputStreamReader(a_fs, "UTF8");
            BufferedReader a_br = new BufferedReader(a_isr);
            String a_line = "";
            int a_rec = 0;
            while ((a_line = a_br.readLine())!=null){
                //1行目はタイトル
                if (a_rec > 0){
                    a_coldefs.add(a_line);
                }
                a_rec++;
            }
            a_br.close();
            a_isr.close();
            a_fs.close();

        }catch(Exception e){

        }
    }
    
    //QueryStringの取得
    String a_ACT = request.getParameter("ACT");
    String a_IDX = request.getParameter("IDX");

    //Beansへの値引渡し
    AnalyzeLog.SetRealPath(a_realPath);

    //登録済データを取得
    ArrayList<String> a_arrayList = null;
    out.print("<input type='hidden' id='txt_idx' name='txt_idx' value='");
    if (a_ACT.equals("e") == true){
        a_arrayList = AnalyzeLog.GetAnalyze(a_Mnt_Table, a_columns, a_coldefs, a_IDX);
        out.print(a_IDX);
    }else{
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
    
    out.print("<table id='tbl_list' border='1' cellspacing='0' cellpadding='0'>");
    //ヘッダ部
    out.print("<tr>");
    out.print("<td bgcolor='#003366' style='text-align:center;' nowrap=true><font color='#ffffff'>項目</font></td>");
    out.print("<td bgcolor='#ff9900' style='text-align:center; width:45%;'><font color='#ffffff'>登録データ</font></td>");
    out.print("<td bgcolor='#339900' style='text-align:center; width:45%;'><font color='#ffffff'>修正データ</font></td>");
    out.print("</tr>");

    //データ部
    if (a_arrayList != null){
        for (int a_iCnt=0; a_iCnt<a_columns.size(); a_iCnt++){
            String[] a_split = a_columns.get(a_iCnt).split("\t");
            out.print("<tr>");
            out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>" + a_split[1] + "</font></td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'>" + a_arrayList.get(a_iCnt) + "</font></td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'>" + Make_Tag_Mnt(a_ACT, a_split, a_column_split, a_coldefs) + "</font></td>");
            out.print("</tr>");
        }
    }else{
        for (int a_iCnt=0; a_iCnt<a_columns.size(); a_iCnt++){
            String[] a_split = a_columns.get(a_iCnt).split("\t");
            //splitは値が入っている所までしかlengthが返らない[2017.07.31]
            out.print("<tr>");
            out.print("<td bgcolor='#003366' style='text-align:left;'><font color='#ffffff'>" + a_split[1] + "</font></td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'></font></td>");
            out.print("<td bgcolor='transparent' style='text-align:left;'>" + Make_Tag_Mnt(a_ACT, a_split, a_column_split, a_coldefs) + "</font></td>");
            out.print("</tr>");
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
    out.print(Make_entry_table_mnt(a_ACT, a_IDX));
    out.print("</script>");
    
%>
    