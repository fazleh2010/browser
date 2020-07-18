/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.core.sparql;

import static citec.core.sparql.SparqlEndpoint.iate_query1;
import citec.core.termbase.TermInfo;
import citec.core.termbase.Termbase;
import citec.core.utils.FileUrlUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 * //https://stackoverflow.com/questions/13356534/how-to-read-xml-child-node-values-by-using-a-loop-in-java
 * //https://howtodoinjava.com/xml/read-xml-dom-parser-example/
 */
public class CurlSparqlQuery {

    private Termbase termbase = null;

    public CurlSparqlQuery(String endpoint, String query, String termBaseName) throws Exception {
        String resultSparql = executeSparqlQuery(endpoint, query);
        this.termbase = new Termbase(termBaseName, parseResult(resultSparql));
    }

    public CurlSparqlQuery(String endpoint, String query) throws Exception {
        String resultSparql = executeSparqlQuery(endpoint, query);
        System.out.println(resultSparql);
    }

    private String executeSparqlQuery(String endpoint, String query)   {
        String result = null,resultUnicode=null, command =null;
        Process process=null;
        try {
            resultUnicode = FileUrlUtils.stringToUrlUnicode(query);
            command = "curl " + endpoint + "?query=" + resultUnicode;
            process = Runtime.getRuntime().exec(command);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CurlSparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error in unicode in sparql query!"+ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(CurlSparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println("error in sending sparql query!"+ex.getMessage());
             ex.printStackTrace();
        }
       
        /*try {
            process = Runtime.getRuntime().exec(command);
        } catch (IOException ex) {
            Logger.getLogger(CurlSparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println("error in sending sparql query!"+ex.getMessage());
        }*/
        
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            result = builder.toString();
            // System.out.println("result String:");
            //System.out.print(result);
            //convertToXML(result);

        } catch (IOException ex) {
            Logger.getLogger(CurlSparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error in reading sparql query!"+ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }

    public Map<String, TermInfo> parseResult(String xmlStr) {
        Document doc = convertStringToXMLDocument(xmlStr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder=null;
        Map<String, TermInfo> terms=new HashMap<String, TermInfo>();
        try {
            builder = factory.newDocumentBuilder();
            terms=this.parseResult(builder, xmlStr);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CurlSparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error in parsing sparql in XML!"+ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(CurlSparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error in parsing sparql in XML!"+ex.getMessage());
            ex.printStackTrace();
        } catch (DOMException ex) {
            Logger.getLogger(CurlSparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error in parsing sparql in XML!"+ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(CurlSparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error in parsing sparql in XML!"+ex.getMessage());
            ex.printStackTrace();
        }
        return terms;
    }

    private Document convertStringToXMLDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, TermInfo> parseResult(DocumentBuilder builder, String xmlStr) throws SAXException, IOException, DOMException, Exception {
        Map<String, TermInfo> allkeysValues = new HashMap<String, TermInfo>();
        Document document = builder.parse(new InputSource(new StringReader(
                xmlStr)));
        NodeList results = document.getElementsByTagName("results");

       
        for (int i = 0; i < results.getLength(); i++) {
            NodeList childList = results.item(i).getChildNodes();

            for (int j = 0; j < childList.getLength(); j++) {
                Node childNode = childList.item(j);
                if ("result".equals(childNode.getNodeName())) {
                    String string = childList.item(j).getTextContent().trim();
                    //st = st.replaceAll("\\s+","");
                    //System.out.println(string);
                    String[] infos = string.split("\n");
                    List<String> wordList = Arrays.asList(infos);
                    String url = null, predicate = null, term = null;
                    Integer index = 0;
                    for (String http : wordList) {
                        if (http.contains("http") && http.contains("CanonicalForm")) {
                            url = http;
                        } else {
                            term = http.trim();
                        }

                    }
                    TermInfo termInfo = new TermInfo(url, null, term, true);
                    //System.out.println(termInfo.getTermUrl());
                    allkeysValues.put(termInfo.getTermOrg(), termInfo);
                }
            }
        }
        return allkeysValues;
    }

    public Termbase getTermbase() {
        return termbase;
    }

}
