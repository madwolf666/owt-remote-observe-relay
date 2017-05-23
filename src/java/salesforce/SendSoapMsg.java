/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesforce;

import java.io.FileOutputStream;
import java.util.Iterator;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
//[2016.03.25]↓
import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
//[2016.03.25]↑

/**
 *
 * @author Chappy
 */
public class SendSoapMsg{
    
    public static void main(String args[]){
        try{
            //SOAPメッセージの送信先への仮想コネクションを取得
            //String a_endPoint = new String("http://owt:okiwintech@localhost:8080/SalesForce/SoapMsg");

            //[2016.03.25]本番機 Testing...
//            String a_endPoint = new String("https://owt-remote.exaas.jp:8181/SalesForce/SoapMsg");
//            String a_endPoint = new String("http://owt-remote.exaas.jp:8080/SalesForce/SoapMsg");
            //[2016.03.12]Linux Server Testing...
//            String a_endPoint = new String("http://219.163.50.198:8080/SalesForce/SoapMsg");
            //[2016.03.12]開発環境 Testing...
            String a_endPoint = new String("http://localhost:8080/SalesForce/SoapMsg");
            
            //SOAPConnectionFactory オブジェクトからSOAPConnectionオブジェクトを取得
            SOAPConnectionFactory a_scf = SOAPConnectionFactory.newInstance();
            SOAPConnection a_conn = a_scf.createConnection();
            //送信するSOAPメッセージを表すSOAPMessageオブジェクトを取得
            MessageFactory a_mf = MessageFactory.newInstance();
            //SOAPMessageオブジェクトはMessageFactoryオブジェクトから取得
            SOAPMessage a_msg = a_mf.createMessage();
            
            //SAAJ-APIを使用して、送信するSOAPメッセージを作成
            SOAPPart a_part = a_msg.getSOAPPart();
            SOAPEnvelope a_env = a_part.getEnvelope();
            a_env.getHeader().detachNode();
            SOAPBody a_body = a_env.getBody();

            /**/
            //[2016.03.25]本番機 Testing...
            SOAPBodyElement a_elm = a_body.addBodyElement( 
                a_env.createName( "SendCaseNo", "ns2", "http://salesforce/" ));
//            SOAPBodyElement a_elm = a_body.addBodyElement( 
//               a_env.createName( "SendCaseNo", "ns2", "http://salesforce/" ));
            
            a_elm.addChildElement( a_env.createName("No")).
                addTextNode("あいうえお");
            a_elm.addChildElement( a_env.createName("RECNo")).
                addTextNode("999");
            a_elm.addChildElement( a_env.createName("STEP1")).
                addTextNode("かきくけこ");
            a_elm.addChildElement( a_env.createName("STEP2")).
                addTextNode("さしすせそ");
            a_elm.addChildElement( a_env.createName("STEP3")).
                addTextNode("たちつてと");

            //[2016.03.25]本番機 Testing...
            a_elm.addNamespaceDeclaration( "ns2", "http://salesforce/" );
//            a_elm.addNamespaceDeclaration( "ns2", "http://salesforce/" );
            /**/
            /*
            SOAPBodyElement a_elm = a_body.addBodyElement(a_env.createName( "RequestBody", "m", "urn:SampleMsg" ));
            a_elm.addChildElement(a_env.createName("Request")).addTextNode("request string...");
            a_elm.addNamespaceDeclaration( "m", "urn:SampleMsg" );
            */
            a_msg.saveChanges();

            FileOutputStream a_requestFile = new FileOutputStream("D:\\tmp\\tmp\\request.msg");
            a_msg.writeTo(a_requestFile);
            a_requestFile.close();

            /*
            Object context = null;
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) context = sm.getSecurityContext(); 
            */
            
             //Trust to certificates[2016.03.25]
            doTrustToCertificates();
            
            //SOAPConnection.callメソッドで送信
            SOAPMessage a_reply = a_conn.call( a_msg, a_endPoint );
            //String a_reply = a_conn.call( a_msg, a_endPoint );

            /**/
            //受信したSOAPメッセージを"reply.msg"というファイルに出力
            FileOutputStream a_replyFile = new FileOutputStream("D:\\tmp\\tmp\\reply.msg");
            a_reply.writeTo(a_replyFile);
            a_replyFile.close();

            //Faultの判定
            a_part = a_reply.getSOAPPart();
            a_env = a_part.getEnvelope();
            a_body = a_env.getBody();
            if( a_body.hasFault()){
                System.out.println("Body is SOAPFault.");
                SOAPFault fault = a_body.getFault();
                String faultActor = fault.getFaultActor();
                String faultCode = fault.getFaultCode();
                String faultString = fault.getFaultString();
                System.out.println("faultActor :"+faultActor);
                System.out.println("faultCode  :"+faultCode);
                System.out.println("faultString:"+faultString);
                Iterator it = fault.getChildElements();
                while(it.hasNext()){
                  SOAPElement detailElm = (SOAPElement)it.next();
                  System.out.println("Detail:"+detailElm);
                }
            }
            else{
                System.out.println("Body is not SOAPFault.");
                Iterator a_it = a_body.getChildElements();
                while(a_it.hasNext()){
                  SOAPElement a_bodyElm = (SOAPElement)a_it.next();
                  System.out.println("Element:"+a_bodyElm);
                  //Name a_name;
                  //a_name.getLocalName();
                  Iterator a_itc = a_bodyElm.getChildElements();
                  while(a_itc.hasNext()){
                      SOAPElement a_childElm = (SOAPElement)a_itc.next();
                      System.out.println("ChildElement:"+a_childElm + a_childElm.getTextContent());
                  }
                }
            }
            /**/
            
            //不要になった仮想コネクションを閉じる
            a_conn.close();
        }catch( SOAPException e ) {
            System.out.println("SOAPException raised.");
            System.out.println("  Message:"+e.getMessage());
            System.out.println("  Cause  :"+e.getCause());
            e.printStackTrace();
        }catch( Throwable t ) {
            t.printStackTrace();
        }
    }
    
    //[2016.03.25]
    static public void doTrustToCertificates() throws Exception {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
 
                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }
 
                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }
                }
        };
 
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                    System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }
}

