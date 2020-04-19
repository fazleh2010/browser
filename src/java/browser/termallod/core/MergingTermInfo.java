/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import browser.termallod.api.DataBaseTemp;
import browser.termallod.core.term.TermInfo;
import browser.termallod.utils.StringMatcherUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class MergingTermInfo {

    private TreeMap<String, TermInfo> urlInfo = new TreeMap<String, TermInfo>();
    private Properties reliabilityCodeProps = new Properties();
    private Properties administrativeSTatusProps = new Properties();
    private String iate_txt_dir;
    private String language;
    private String location;
    private DataBaseTemp dataBaseTemp;

    public MergingTermInfo(String location, String iate_txt_dir, String language, String alphabetFileName, DataBaseTemp dataBaseTemp) throws FileNotFoundException, IOException {
        this.location = location;
        this.iate_txt_dir = location + iate_txt_dir;
        this.language = language;
        this.dataBaseTemp = dataBaseTemp;
        Properties alphabetProps = getProperties(this.iate_txt_dir + alphabetFileName);
        //this.prepareSubjectFields();

        TreeMap<String, SubjectInfo> urlSubjectInfo = this.prepareSubjectFields();
        this.prepareTermInfo(alphabetProps,urlSubjectInfo);
    }

    private void prepareTermInfo(Properties alphabetProps,TreeMap<String, SubjectInfo> urlSubjectInfo) throws IOException {
        String reliabilityCodeFile = iate_txt_dir + language + "_" + dataBaseTemp.getReliabilityCode();
        String administrativeStatusFile = iate_txt_dir + language + "_" + dataBaseTemp.getAdministrativeStatus();
        reliabilityCodeProps = getProperties(reliabilityCodeFile);
        administrativeSTatusProps = getProperties(administrativeStatusFile);
        Object administrativeSTatus = "";
        Object reliabilityCode = "";
        SubjectInfo subjectTermInfo =new SubjectInfo();
        for (Object term : alphabetProps.keySet()) {
            Object url = alphabetProps.get(term);

            if (reliabilityCodeProps.containsKey(url)) {
                reliabilityCode = reliabilityCodeProps.get(url);
            }
            if (administrativeSTatusProps.containsKey(url)) {
                administrativeSTatus = administrativeSTatusProps.get(url);
            }
            if (urlSubjectInfo.containsKey(url)) {
                 subjectTermInfo = urlSubjectInfo.get(url);
              
            }
            TermInfo termInfo = new TermInfo(term,  url,reliabilityCode, administrativeSTatus, subjectTermInfo);
            //System.out.println(termInfo.toString());
            urlInfo.put(url.toString(), termInfo);
        }

    }

    private TreeMap<String, SubjectInfo> prepareSubjectFields() throws IOException {
        TreeMap<String, SubjectInfo> urlSubjectInfo = new TreeMap<String, SubjectInfo>();

        String conceptFileName = iate_txt_dir + language + "_" + dataBaseTemp.getSENSE() + ".txt";
        String subjectFileName = iate_txt_dir + dataBaseTemp.getSubjectFileName();
        String subectDescription = location + dataBaseTemp.getSubjectDescriptions();

        Properties conceptProps = getProperties(conceptFileName);
        Properties subjectProps = getProperties(subjectFileName);
        Properties subjectDetailsProps = getProperties(subectDescription);
        //List<String> subjectFields = new ArrayList<String>();

        for (Object id : conceptProps.keySet()) {
            Object senseUrl = conceptProps.get(id);
            Object subjectDetail = null;
            if (subjectProps.containsKey(id)) {
                Object subjectField = subjectProps.get(id);
                if (subjectDetailsProps.containsKey(subjectField)) {
                    subjectDetail = subjectDetailsProps.get(subjectField);
                }
                String url = StringMatcherUtil.getUrl(senseUrl.toString());
                //subjectFields.add(subjectField.toString());

                SubjectInfo subjectInfo = new SubjectInfo(id, subjectField, subjectDetail);
                urlSubjectInfo.put(url, subjectInfo);
                //System.out.println(subjectInfo.toString());

            }
        }
        return urlSubjectInfo;
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

}
