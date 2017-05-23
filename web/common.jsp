<%-- 
    Document   : make_remote_trouble_list
    Created on : 2015/08/24, 14:45:02
    Author     : Chappy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
//パスを取得
String GetMainPath(String h_script_name){
    String[] a_pathResult = h_script_name.split("/");
    int idx = a_pathResult.length;
    String a_mainPath = "";
    for( int i = 1; i < a_pathResult.length - 1; i++ ) {
        if ( i == 1 ) {
            a_mainPath += a_pathResult[i];
        }
        else {
            a_mainPath += ( "/" + a_pathResult[i] );
        }
    }
    
    return a_mainPath;
}    

//セッション変数取得
String GetSessionValue(Object h_session){
    String a_sRet = "";
    if (h_session == null){
        a_sRet = "";
    }else{
        a_sRet = String.valueOf(h_session);
    }
    return a_sRet;
}
%>

    