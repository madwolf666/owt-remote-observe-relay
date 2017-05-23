<%-- 
    Document   : check_auth
    Created on : 2015/08/24, 11:56:35
    Author     : Chappy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"
        import="java.io.*,java.util.*,java.text.SimpleDateFormat" %>
<%@ include file="/common.jsp" %>
<jsp:useBean id="Environ" scope="page" class="common.Environ" />
<!-- jsp:getProperty name="SalesForceApi" property="dummy" / -->
<%
            //パスを取得
            String SCRIPT_NAME = request.getServletPath();
            String a_mainPath = GetMainPath(SCRIPT_NAME);
            String a_realPath = application.getRealPath(a_mainPath);
            String a_tmpPath = "";

            Environ.SetRealPath(a_realPath);
            a_tmpPath = Environ.GetEnvironValue("tmp_path");

            //セッション変数を取得
            //Object a_oIsFirst = session.getAttribute("isFirst");  //[2016.03.14]
            int a_iIsFirst = 0;
            String a_sCaseNo = "";
            String a_caseNoKey = "";
            //if (a_oIsFirst == null){  //[2016.03.14]
                //本サイトへの初めてのアクセス
                //salesforce.RecieveSoapMsg()
                //ケース番号
                session.setAttribute("CaseNo", "");
                a_sCaseNo = request.getParameter("CNK");
                a_caseNoKey = a_sCaseNo;
                //受付時間
                //取引先名
                //件名
                //STEP1～STEP4
                //補足
                a_iIsFirst = 0;
                if ((a_sCaseNo != null) && (a_sCaseNo.length()>0)){
                    //一時ファイルから読込
                    try{
                        FileInputStream a_fs = new FileInputStream(a_tmpPath + a_sCaseNo + ".sf");
                        InputStreamReader a_isr = new InputStreamReader(a_fs, "UTF8");
                        BufferedReader a_br = new BufferedReader(a_isr);
                        String a_line = "";
                        int a_i = 0;
                        while ((a_line = a_br.readLine())!=null){
                            if (a_i==0){
                                a_sCaseNo = "";
                            }else{
                                a_sCaseNo += "\t";    //TAB区切り
                            }
                            a_sCaseNo += a_line;
                            a_i++;
                        }
                        a_br.close();
                        a_isr.close();
                        a_fs.close();
                        
                        File a_file = new File(a_tmpPath + a_caseNoKey + ".sf");
                        try{
                            a_file.delete();
                        }catch(Exception e){
                            
                        }
                    }catch(Exception e){
                        
                    }
                    session.setAttribute("CaseNo", a_sCaseNo);
                }
                //[2016.03.09]↓
                session.setAttribute("isFirst", 1);
                /**/
                SimpleDateFormat a_sdf = new SimpleDateFormat("yyyy/MM/dd");
                Date a_date = new Date();
                String a_sNow = a_sdf.format(a_date) + " 00:00";
                session.setAttribute("entry_timeS", a_sNow);
                /**/
                //[2016.03.09]↑
            //}else{    //[2016.03.14]
            //    //2回目以降のアクセス
            //    a_iIsFirst = Integer.valueOf(String.valueOf(a_oIsFirst));
            //    a_sCaseNo = String.valueOf(session.getAttribute("CaseNo"));
            //}
    
            String a_msg = "";
            String a_user = request.getRemoteUser();
           // String a_param = request.getParameter("SFLINK");

            a_msg = "OK!";
            if (a_user.equals("owt") == true){
                if ((a_sCaseNo != null) && (a_sCaseNo.length()>0)){
                    response.sendRedirect("./sf-link.xhtml");
                }else{
                    response.sendRedirect("./find.xhtml");
                }
            }else if (a_user.equals("remote") == true){
                if ((a_sCaseNo != null) && (a_sCaseNo.length()>0)){
                    response.sendRedirect("./sf-link.xhtml");
                }else{
                    response.sendRedirect("./find.xhtml");
                }
            }
        %>
