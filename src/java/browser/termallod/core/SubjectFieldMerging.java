/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import browser.termallod.core.term.TermInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class SubjectFieldMerging {

    private TreeMap<String, TermInfo> urlInfo = new TreeMap<String, TermInfo>();
    private Properties subjectDetailsProps;

    public SubjectFieldMerging(String alphabetFileName, String conceptFileName, String subjectFileName, String cannonical, String sense, String subjectDetails) throws FileNotFoundException, IOException {
        Properties alphabetProps = getProperties(alphabetFileName);
        Properties conceptProps = getProperties(conceptFileName);
        Properties subjectProps = getProperties(subjectFileName);
        this.subjectDetailsProps = getProperties(subjectDetails);
        //Properties cannonicalProps = getProperties(cannonical);
        //Properties senseProps = getProperties(sense);
        //TreeMap<Object, TermInfo> urlInfo = new TreeMap<Object, TermInfo>();
        //16
        List<String> subjectFields = new ArrayList<String>();

        for (Object id : conceptProps.keySet()) {
            Object senseUrl = conceptProps.get(id);
            if (subjectProps.containsKey(id)) {

                Object subjectField = subjectProps.get(id);
                
                subjectFields.add(subjectField.toString());
                TermInfo termInfo = new TermInfo(id.toString(), subjectField.toString(), senseUrl.toString());
                urlInfo.put(senseUrl.toString(), termInfo);
                System.out.println(senseUrl + " " + termInfo);
                /*if (senseProps.containsKey(senseUrl)) {
                     Object url=senseProps.get(senseUrl);
                     //System.out.println(url + " " + id + " " + subjectField+ " " +url);
                TermInfo termInfo = new TermInfo(id.toString(), subjectField.toString(), senseUrl.toString());
                urlInfo.put(url, termInfo);
                 }*/

            }
        }

        /*Collections.sort(subjectFields); 
        //System.out.println(urlInfo.size());
         Set<String> subjectFieldsSet=new HashSet<String>();
        for(String subjectField:subjectFields){
            subjectFieldsSet.add(subjectField);
        }
        
         List<String> subjectFields2=new ArrayList<String>();
        
        for(String subjectField:subjectFieldsSet){
             subjectFields2.add(subjectField);
        }
         
         Collections.sort(subjectFields2);
          for(String subjectField2:subjectFields2){
            System.out.println(subjectField2+" "+"=");
        }
        
        
         System.out.println(subjectFields.size());
        for(Object key:urlInfo.keySet()){
            TermInfo termInfo =urlInfo.get(key);
           //System.out.println(key + " " + termInfo.getTermString());

        }*/
 /*for (Object id : cannonicalProps.keySet()) {
            Object url = cannonicalProps.get(id);
            //System.out.println(id + " " + url);
            if (alphabetProps.containsKey(id)) {
                Object term = alphabetProps.get(id);
                //System.out.println(term);
                if (urlInfo.containsKey(url)) {
                    TermInfo termInfo = urlInfo.get(term);
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println(id);
                    System.out.println(url);
                    System.out.println(term);
                    System.out.println(termInfo.toString());
                }

            }
        }*/

 /*for (Object id : conceptProps.keySet()) {
            Object url = conceptProps.get(id);
            if (subjectProps.containsKey(id)) {
                Object subjectField = subjectProps.get(id);
                System.out.println(id + " " + url + " " + subjectField);
                TermInfo termInfo = new TermInfo(id.toString(), subjectField.toString(), url.toString());
                //System.out.println(url + " ");
                urlInfo.put(url, termInfo);
            }
        }*/
 /*for (Object term : alphabetProps.keySet()) {
             Object url=alphabetProps.get(term);
            if (urlInfo.containsKey(url)) {
               
                System.out.println(url + " ");
               
            }
        }*/

 /*if (conceptProps.containsKey(url)) {
                Object id = conceptProps.get(url);
                System.out.println("id:" + id);
                if (subjectProps.containsKey(id)) {
                    
                    
                }
            }*/
    }

    private Properties getProperties(String subjectFileName) throws IOException {
        File propFile = new File(subjectFileName);
        Properties props = new Properties();
        props.load(new InputStreamReader(new FileInputStream(propFile), "UTF-8"));
        return props;
    }

    public TreeMap<String, TermInfo> getUrlInfo() {
        return urlInfo;
    }

    public TermInfo getUrlInfo(String key) {
        return urlInfo.get(key);
    }

    public Properties getSubjectDetailsProps() {
        return subjectDetailsProps;
    }

    public Object getSubjectDetailsProps(Object key) {
        return subjectDetailsProps.get(key);
    }

}
