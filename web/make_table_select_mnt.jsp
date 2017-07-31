<%-- 
    Document   : make_table_select_mnt
    Created on : 2017/07/27, 12:46:22
    Author     : hal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"
        import="java.io.*,java.util.*,java.text.SimpleDateFormat" %>
<%@ include file="/common.jsp" %>
<jsp:useBean id="Environ" scope="page" class="common.Environ" />
<%
    session.setAttribute("Mnt_Table", "");   //[2017.07.27]

    //パスを取得
    String SCRIPT_NAME = request.getServletPath();
    String a_mainPath = GetMainPath(SCRIPT_NAME);
    String a_realPath = application.getRealPath(a_mainPath);
    String a_envPath = "";
    String a_tableInfo = "";

    Environ.SetRealPath(a_realPath);
    a_envPath = Environ.GetEnvironValue("mnt_env_path");
    a_tableInfo = Environ.GetEnvironValue("mnt_table_info");

    out.print("<select id='set-table-name' name='set-table-name' onclick>");
    out.print("<option value=''></option>");
    try{
        FileInputStream a_fs = new FileInputStream(a_envPath + a_tableInfo);
        InputStreamReader a_isr = new InputStreamReader(a_fs, "UTF8");
        BufferedReader a_br = new BufferedReader(a_isr);
        String a_line = "";
        int a_rec = 0;
        while ((a_line = a_br.readLine())!=null){
            String[] a_split = a_line.split("\t");
            //1行目はタイトル
            if (a_rec > 0){
                if (a_split.length>=2){
                    out.print("<option value='" + a_split[0] + "\t" + a_split[2] + "'>" + a_split[1] + "</option>");
                }
            }
            a_rec++;
        }
        a_br.close();
        a_isr.close();
        a_fs.close();

    }catch(Exception e){

    }
    out.print("</select>");
%>
