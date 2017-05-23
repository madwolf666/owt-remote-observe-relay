/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesforce;

import common.Environ;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import report.ReportMonthly;
        
/**
 *
 * @author Chappy
 */
@WebService(serviceName = "SalesForce")
@Stateless()
//public class SoapMsg implements ReqRespListener {
public class SoapMsg {
    //private CheckAny _CheckAny = null;
    private Environ _Environ = null;
    private String _my_path = "C:\\Users\\chappy\\Documents\\NetBeansProjects\\owt-remote-observe-relay\\build\\web";
    private String _my_url = "";
    private String _caseNoKey = "";
    private String _tmp_path = "";

    public SoapMsg(){
        _Environ = new Environ();
    }
    
    @WebMethod(operationName = "SendCaseNo")
    public String SendCaseNo (
            @WebParam(name = "RECNo") String h_RECNo,   //[2016.06.09]bug-fixed.
            @WebParam(name = "No") String h_No,
            @WebParam(name = "STEP1") String h_step1,
            @WebParam(name = "STEP2") String h_step2,
            @WebParam(name = "STEP3") String h_step3
            ) {
        //パラメータを取得
        String a_RECNo = h_RECNo;   //[2016.06.09]bug-fixed.
        String a_No = h_No;
        String a_step1 = h_step1;
        String a_step2 = h_step2;
        String a_step3 = h_step3;
        
        //URLを取得
        _getMyURL();
        
        //一時ファイル書込み[2016.06.09]bug-fixed.
        _makeTmp_caceNoKey(
                a_RECNo,
                a_No,
                a_step1,
                a_step2,
                a_step3
                );
        try{
            _Environ._MyLogger.info("*** [WebService:SalesForce]SendCaseNo is finished. ***" + _caseNoKey);
            return _my_url + "?CNK=" + _caseNoKey;
        } catch(Exception e){
             e.printStackTrace();
            _Environ._MyLogger.severe("*** [WebService:SalesForce]SendCaseNo is finished. ***" + e.getMessage());
            return "";
        }
    }

    private void _getMyURL(){
        if (_Environ._CheckAny.isLinux() == true){
            //システム固定とする[2016.06.13]bug-fixed.
            _my_path = "/opt/glassfish4/glassfish/domains/domain1/applications/remote";
            //_my_path = "/opt/glassfish4/glassfish/domains/domain1/applications/owt-remote-observe-relay";
        }
        _Environ.SetRealPath(_my_path);
        _my_url = _Environ.GetEnvironValue("my_url");
    }
    
    private void _makeTmp_caceNoKey(
        String h_RECNo, //[2016.06.09]bug-fixed.
        String h_no,
        String h_step1,
        String h_step2,
        String h_step3
        ){
        try{
            _tmp_path = _Environ.GetEnvironValue("tmp_path");
            
            try{
                Date a_today = new Date();
                SimpleDateFormat a_dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                _caseNoKey =  a_dateFormat.format(a_today);
            } catch (Exception ex) {
                Logger.getLogger(ReportMonthly.class.getName()).log(Level.SEVERE, null, ex);
            }

            // FileOutputStreamオブジェクト生成（出力ファイルの指定）
            FileOutputStream a_fo = new FileOutputStream(_tmp_path + _caseNoKey + ".sf");
            // OutputStreamWriterオブジェクト生成（文字コードの指定）
            OutputStreamWriter a_ow = new OutputStreamWriter(a_fo, "UTF-8");
            // 書き出す内容をセット
            a_ow.write(h_RECNo + "\r\n");   //[2016.06.09]
            a_ow.write(h_no + "\r\n");
            //[2016.06.09]bug-fixed.↓
            a_ow.write(h_step1 + "\r\n");
            a_ow.write(h_step2 + "\r\n");
            a_ow.write(h_step3 + "\r\n");
            //[2016.06.09]bug-fixed.↑
            // ストリームの解放
            a_ow.close();
            a_fo.close();
        }catch(IOException e){
            _Environ._MyLogger.severe("*** [WebService:SalesForce]SendCaseNo is finished. ***" + e.getMessage());
        }
            
    }
    
    /*
    public SOAPMessage SendCaseNo (
            @WebParam(name = "No") String h_No,
            @WebParam(name = "STEP1") String h_step1,
            @WebParam(name = "STEP2") String h_step2,
            @WebParam(name = "STEP3") String h_step3
            ) {
        
        String a_No = h_No;
        String a_step1 = h_step1;
        String a_step2 = h_step2;
        String a_step3 = h_step3;
        
        _caceNoKey = "chappy";

        try{
            MessageFactory a_mf = MessageFactory.newInstance();
            SOAPMessage a_resp = a_mf.createMessage();
            SOAPEnvelope a_env = a_resp.getSOAPPart().getEnvelope();
            SOAPBody a_body = a_env.getBody();

            SOAPBodyElement a_elm = a_body.addBodyElement(
                a_env.createName( "SendCaseNoResponse", "ns2", "http://salesforce/" ));
            
            a_elm.addChildElement(a_env.createName("return")).
                addTextNode("chappy");
                
            a_elm.addNamespaceDeclaration( "ns2", "http://salesforce/");

            a_resp.saveChanges();

            FileOutputStream a_requestFile = new FileOutputStream("D:\\tmp\\tmp\\response.msg");
            a_resp.writeTo(a_requestFile);
            a_requestFile.close();

            //**(5)**
            //return null;
            return a_resp;
        } catch(Exception e){
             e.printStackTrace();
            return null;
        }
    }
    */
    /*
    @Override
    public SOAPMessage onMessage(SOAPMessage h_message){
        //
        try{
            h_message.writeTo(System.out);
            
            MessageFactory a_mf = MessageFactory.newInstance();
            SOAPMessage a_resp = a_mf.createMessage();
            SOAPEnvelope a_env = a_resp.getSOAPPart().getEnvelope();
            SOAPBody a_body = a_env.getBody();
            SOAPBodyElement a_elm = a_body.addBodyElement(
                a_env.createName( "ResponseMessage", "m", "urn:SampleMsg" ));
                a_elm.addChildElement( a_env.createName("Response")).
                addTextNode("response string...");
                a_elm.addNamespaceDeclaration( "m", "urn:SampleMsg");
                a_resp.saveChanges();

            //**(5)**
            return a_resp;
        } catch(Exception e){
             e.printStackTrace();
            return null;
        }
    }
    */
}
