/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.app;

import browser.termallod.api.DataBaseTemp;
import browser.termallod.core.MergingTermInfo;
import browser.termallod.core.term.SubjectInfo;
import browser.termallod.core.term.TermDetail;
import browser.termallod.core.term.TermInfo;
import browser.termallod.utils.FileRelatedUtils;
import browser.termallod.utils.UrlMatching;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author elahi
 */
public class UrlMatchingTest {

    private String alphabetFileName = "tbx2rdf_iate_en_A_B.txt";
    public String location = "test/resources/data/";
    public String iate_folder = "iate/txt/";
    private static String CONFIG_PATH = "src/java/resources/data/conf/";
    public DataBaseTemp dataBaseTemp = new DataBaseTemp(CONFIG_PATH);

    @Ignore
    public void testMatchingUrls_MatchingSubject() throws Exception {
        String termUrl = "http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_iate/data/iate/odd+pulse-en#CanonicalForm";
        MergingTermInfo merging = new MergingTermInfo(location,iate_folder,"en",alphabetFileName, dataBaseTemp);
        Object senseUrl = "odd+pulse-en#Sense";
        Object id = "IATE-1608440";
        Object subjectField = "6826";
        Object subjectDetail = "Electrical and electrical engineering";

         SubjectInfo subjectInfo = new SubjectInfo(id, subjectField, subjectDetail);
        UrlMatching urlMatchTesting = new UrlMatching(merging, termUrl);
        TermInfo termInfo = urlMatchTesting.getTermInfo();
        assertEquals(termInfo.getSubjectId(), subjectInfo.getSubjectId());
        assertEquals(termInfo.getTermID(), subjectInfo.getReferenceID());

    }

    /* @Ignore
   public void testMatchingUrls_WhenMatchedAllUrls() throws Exception {

        MergingTermInfo merging = new MergingTermInfo(alphabetFileName, dataBaseTemp);
        Properties alphabetProps = getProperties(alphabetFileName);
        Integer index = 0;
        List<File> files = FileRelatedUtils.getFiles("test/resources/data/iate/txt/", "tbx2rdf_iate", ".txt");
        for (File file : files) {
            if (file.getName().contains("en")) {
                System.out.println(file.getName());
                Properties alphabetPropsFiles = getProperties("test/resources/data/iate/txt/" + file.getName());
                for (Object key : alphabetPropsFiles.keySet()) {
                    Object value = alphabetPropsFiles.get(key);
                    String termUrl = value.toString();
                    termUrl = TermDetail.getAlternativeUrl(termUrl, false);
                    UrlMatching urlMatchTesting = new UrlMatching(merging, termUrl);
                    TermInfo termInfo = urlMatchTesting.getTermInfo();
                    if (termInfo != null) {
                        System.out.println(key + " " + termUrl + " " + termInfo.getSubjectId() + termInfo.getTermID());
                        String detail = merging.getSubjectDetailsProps(termInfo.getSubjectId()).toString();
                        System.out.println(detail);
                        index = index + 1;
                        //break;
                    }
                }

            }

        }
        System.out.println("number of matched" + index);


    }*/

    @Ignore
    public void testMatchingUrls_WhenMatchedEveything() throws Exception {

        MergingTermInfo merging = new MergingTermInfo(location,iate_folder,"en",alphabetFileName, dataBaseTemp);
        System.out.println("finished");

//        System.out.println("finished");*/

        /*TermInfo subjectInfo = new TermInfo(id, subjectField, senseUrl);
        UrlMatching urlMatchTesting = new UrlMatching(merging, termUrl);
        TermInfo termInfo = urlMatchTesting.getTermInfo();
        assertEquals(termInfo.getSubjectId(), subjectInfo.getSubjectId());
        assertEquals(termInfo.getTermID(), subjectInfo.getTermID());*/
    }

    @Ignore
    public void testMatchingUrls_WhenNotMatched() throws Exception {
        String termUrl = "http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_iate/data/iate/A-field-en#CanonicalForm";
        MergingTermInfo merging = new MergingTermInfo(location,iate_folder,"en",alphabetFileName, dataBaseTemp);
        String senseUrl = "odd+pulse-en#Sense";
        Object id = "IATE-1608440";
        Object subjectField = "6826";
        Object subjectDetail = "Electrical and electrical engineering";

        SubjectInfo termInfoTemp = new SubjectInfo(id, subjectField, subjectDetail);
        UrlMatching urlMatchTesting = new UrlMatching(merging, termUrl);
        assertEquals(urlMatchTesting.getTermInfo().getSubjectId(), null);

    }

    private Properties getProperties(String subjectFileName) throws IOException {
        File propFile = new File(subjectFileName);
        Properties props = new Properties();
        props.load(new InputStreamReader(new FileInputStream(propFile), "UTF-8"));
        return props;
    }

}
