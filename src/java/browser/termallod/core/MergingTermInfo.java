/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import browser.termallod.api.DataBaseTemp;
import browser.termallod.core.term.TermInfo;
import browser.termallod.utils.FileRelatedUtils;
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
        String reliabilityCodeFile = iate_txt_dir + language + "_" + dataBaseTemp.getReliabilityCode();
        String administrativeStatusFile = iate_txt_dir + language + "_" + dataBaseTemp.getAdministrativeStatus();
        //this.prepareSubjectFields();

        //TreeMap<String, SubjectInfo> urlSubjectInfo = this.prepareSubjectFields();
        //this.prepareTermInfo(this.iate_txt_dir + alphabetFileName,reliabilityCodeFile,administrativeStatusFile,urlSubjectInfo);
    }

    public MergingTermInfo(File alphabetFile, String language, DataBaseTemp dataBaseTemp,Boolean alternativeFlag) throws FileNotFoundException, IOException {
        this.location = alphabetFile.getAbsoluteFile().getParent();
        this.language = language;
        this.dataBaseTemp = dataBaseTemp;
        String reliabilityCodeFile = location + File.separator + language + "_" + dataBaseTemp.getReliabilityCode();
        String administrativeStatusFile = location + File.separator + language + "_" + dataBaseTemp.getAdministrativeStatus();
        String conceptFileName = location + File.separator + language + "_" + dataBaseTemp.getSENSE() + ".txt";
        String subjectFileName = location + File.separator + dataBaseTemp.getSubjectFileName();
        String subectDescription = dataBaseTemp.getSubjectDescriptions();

        TreeMap<String, SubjectInfo> urlSubjectInfo = this.prepareSubjectFields(conceptFileName, subjectFileName, subectDescription);
        this.prepareTermInfo(alphabetFile.getAbsolutePath(), reliabilityCodeFile, administrativeStatusFile, urlSubjectInfo,alternativeFlag);
    }

    private void prepareTermInfo(String alphabetFileName, String reliabilityCodeFile, 
                                String administrativeStatusFile, TreeMap<String, SubjectInfo> urlSubjectInfo,
                                Boolean alternativeFlag) throws IOException {
        Properties alphabetProps = FileRelatedUtils.getProperties(alphabetFileName);
        Properties reliabilityCodeProps = FileRelatedUtils.getProperties(reliabilityCodeFile);
        Properties administrativeSTatusProps = FileRelatedUtils.getProperties(administrativeStatusFile);
        Object administrativeSTatus = "";
        Object reliabilityCode = "";
        SubjectInfo subjectTermInfo = new SubjectInfo("", "", "");
        for (Object term : alphabetProps.stringPropertyNames()) {
            Object urls = alphabetProps.get(term);
            Object orgUrl= StringMatcherUtil.getAlternativeUrl(urls.toString(), false);
            Object alterUrl= StringMatcherUtil.getAlternativeUrl(urls.toString(), true);

            if (reliabilityCodeProps.containsKey(orgUrl)) {
                reliabilityCode = reliabilityCodeProps.get(orgUrl);
            }
            if (administrativeSTatusProps.containsKey(orgUrl)) {
                administrativeSTatus = administrativeSTatusProps.get(orgUrl);
            }
            if (urlSubjectInfo.containsKey(orgUrl)) {
                subjectTermInfo = urlSubjectInfo.get(orgUrl);

            }
            TermInfo termInfo = new TermInfo(term, orgUrl, alterUrl,reliabilityCode, administrativeSTatus, subjectTermInfo);
            if(alternativeFlag){
                 urlInfo.put(alterUrl.toString(), termInfo);
                 System.out.println(termInfo);
            }
           
        }

    }

    private TreeMap<String, SubjectInfo> prepareSubjectFields(String conceptFileName, String subjectFileName, String subectDescription) throws IOException {
        TreeMap<String, SubjectInfo> urlSubjectInfo = new TreeMap<String, SubjectInfo>();
        Properties conceptProps = FileRelatedUtils.getProperties(conceptFileName);
        Properties subjectProps = FileRelatedUtils.getProperties(subjectFileName);
        Properties subjectDetailsProps = FileRelatedUtils.getProperties(subectDescription);
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
                SubjectInfo subjectInfo = new SubjectInfo(id, subjectField, subjectDetail);
                urlSubjectInfo.put(url, subjectInfo);
            }
        }
        return urlSubjectInfo;
    }

    public TreeMap<String, TermInfo> getUrlInfo() {
        return urlInfo;
    }

}
