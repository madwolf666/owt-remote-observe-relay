/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesforce;

import common.Environ;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 *
 * @author Chappy
 */
@ManagedBean(name="SalesForceApi")
@RequestScoped
public class SalesForceApi implements Serializable {
    //private CheckAny _CheckAny = null;
    public Environ _Environ = null;

    private String _SalesForceURL = "http://219.163.50.198/~chappy/sales-force.xml";

    //SalesforceのURL
/*
    public void putSalesForceURL(String SalesForceURL) {
        SalesForceURL = SalesForceURL;
    }
    public String getSalesForceURL() {
        return SalesForceURL;
    }
*/
    
    /**
     * Creates a new instance of SalesForceApi
     */
    public SalesForceApi() {
        //_CheckAny = new CheckAny();
        _Environ = new Environ();
    }
    
    public void SetRealPath(String h_path){
        _Environ.SetRealPath(h_path);
        //_Environ._MyLogger.SetMyLog();
        
        _Environ._MyLogger.config("SalesForceURL ---> " + _SalesForceURL);
        _Environ._MyLogger.info("*** SetRealPath is finished. ***");
    }

    public  ArrayList<String> GetSalesForceInfo(String h_CaseNo) throws Exception{
        ArrayList<String> a_arrayRet = null;

        try{
            a_arrayRet = new ArrayList<String>();
            String a_sVal = "";

            DocumentBuilderFactory a_factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder a_documentBuilder = a_factory.newDocumentBuilder();
            Document document = a_documentBuilder.parse(_SalesForceURL + "?CASENO=" + h_CaseNo);
            Element a_root = document.getDocumentElement();
            
            //ルート要素のノード名を取得する
            System.out.println("ノード名：" + a_root.getNodeName());
            //ルート要素の属性を取得する
            System.out.println("ルート要素の属性：" + a_root.getAttribute("name"));
            a_sVal = a_root.getAttribute("name");
            //ルート要素の子ノードを取得する
            NodeList a_rootChildren = a_root.getChildNodes();
            System.out.println("子要素の数：" + a_rootChildren.getLength());
            System.out.println("------------------");
            
            for(int a_i=0; a_i < a_rootChildren.getLength(); a_i++) {
                Node a_node = a_rootChildren.item(a_i);
                if(a_node.getNodeType() == Node.ELEMENT_NODE) {
                    Element a_element = (Element)a_node;
                    if (a_element.getNodeName().equals("data")) {
                        System.out.println("名前：" + a_element.getAttribute("name"));
                        
                        a_sVal = a_element.getAttribute("name");
                        a_sVal += "\t" + a_element.getTextContent();
                        a_arrayRet.add(a_sVal);
                        
/*                        
                        NodeList a_personChildren = a_node.getChildNodes();
                        for (int j=0; j < a_personChildren.getLength(); j++) {
                            Node a_personNode = a_personChildren.item(j);
                            if (a_personNode.getNodeType() == Node.ELEMENT_NODE) {
                                if (a_personNode.getNodeName().equals("age")) {
                                    System.out.println("年齢：" + a_personNode.getTextContent());
                                } else if (a_personNode.getNodeName().equals("interest")) {
                                    System.out.println("趣味:" + a_personNode.getTextContent());
                                }
                            }
                        }
*/
                        System.out.println("------------------");
                    }
                }
            }
        } catch (ParserConfigurationException ex) {
            _Environ._MyLogger.severe("[GetSalesForceInfo]" + ex.getMessage());
        } catch (SAXException ex) {
            _Environ._MyLogger.severe("[GetSalesForceInfo]" + ex.getMessage());
        } catch (IOException ex) {
            _Environ._MyLogger.severe("[GetSalesForceInfo]" + ex.getMessage());
        }
        
        _Environ._MyLogger.info("*** GetSalesForceInfo is finished. ***");
        
        return a_arrayRet;
    }
    
    public void getKeyFromSalesForce(){
        try{
            //URL a_url = new URL("http://219.163.50.198/~chappy/sample.xml");
            URL a_url = new URL("http://219.163.50.198/~chappy/SupportInspection.xml");
            URLConnection a_conn = a_url.openConnection();
            //String charset = Arrays.asList(a_conn.getContentType().split(";") ).get(1);
            //String encoding = Arrays.asList(charset.split("=") ).get(1);

            InputStream a_is = a_conn.getInputStream();
            InputStreamReader a_ir = new InputStreamReader(a_is, "UTF-8");
            BufferedReader a_in = new BufferedReader(a_ir); 
            StringBuffer a_response = new StringBuffer();
            String a_line;
            String a_xml = "";
            while ((a_line= a_in.readLine()) != null) {
                a_response.append(a_line + "\n");
                a_xml += a_line + "\n";
            }
            a_in.close();
            
            _domRead(a_is);
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(SalesForceApi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SalesForceApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void _domRead(InputStream h_xml){
        try{
            String a_sVal = "";
            DocumentBuilderFactory a_factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder a_documentBuilder = a_factory.newDocumentBuilder();
            Document document = a_documentBuilder.parse("http://219.163.50.198/~chappy/sales-force.xml");
            //Document document = a_documentBuilder.parse("http://219.163.50.198/~chappy/SupportInspection.xml");
            //Document document = a_documentBuilder.parse(h_xml);
            Element a_root = document.getDocumentElement();
            
            //ルート要素のノード名を取得する
            System.out.println("ノード名：" + a_root.getNodeName());
            //ルート要素の属性を取得する
            System.out.println("ルート要素の属性：" + a_root.getAttribute("name"));
            a_sVal = a_root.getAttribute("name");
            //ルート要素の子ノードを取得する
            NodeList a_rootChildren = a_root.getChildNodes();
            System.out.println("子要素の数：" + a_rootChildren.getLength());
            System.out.println("------------------");
            
            for(int a_i=0; a_i < a_rootChildren.getLength(); a_i++) {
                Node a_node = a_rootChildren.item(a_i);
                if(a_node.getNodeType() == Node.ELEMENT_NODE) {
                    Element a_element = (Element)a_node;
                    if (a_element.getNodeName().equals("data")) {
                        System.out.println("名前：" + a_element.getAttribute("name"));
                        a_sVal = a_element.getTextContent();
/*                        
                        NodeList a_personChildren = a_node.getChildNodes();
                        for (int j=0; j < a_personChildren.getLength(); j++) {
                            Node a_personNode = a_personChildren.item(j);
                            if (a_personNode.getNodeType() == Node.ELEMENT_NODE) {
                                if (a_personNode.getNodeName().equals("age")) {
                                    System.out.println("年齢：" + a_personNode.getTextContent());
                                } else if (a_personNode.getNodeName().equals("interest")) {
                                    System.out.println("趣味:" + a_personNode.getTextContent());
                                }
                            }
                        }
*/
                        System.out.println("------------------");
                    }
                }
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SalesForceApi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(SalesForceApi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SalesForceApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
