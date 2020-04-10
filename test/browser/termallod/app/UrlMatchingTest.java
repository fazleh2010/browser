/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.app;

import browser.termallod.core.SubjectFieldMerging;
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

    private String alphabetFileName = "test/resources/data/iate/txt/tbx2rdf_iate_en_A_B.txt";
    private String conceptFileName = "test/resources/data/iate/txt/en.txt";
    private String subjectFileName = "test/resources/data/iate/txt/subject.txt";
    private String cannonical = "test/resources/data/iate/txt/canonicalForm.txt";
    private String sense = "test/resources/data/iate/txt/sense.txt";
    private String subjectDescriptions = "test/resources/data/iate/txt/subjectFields.txt";

    @Ignore
    public void testMatchingUrls_WhenMatched() throws Exception {
        String termUrl = "http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_iate/data/iate/odd+pulse-en#CanonicalForm";
        SubjectFieldMerging merging = new SubjectFieldMerging(alphabetFileName, conceptFileName, subjectFileName, cannonical, sense,subjectDescriptions);
        String senseUrl = "odd+pulse-en#Sense";
        String id = "IATE-1608440";
        String subjectField = "6826";

        TermInfo termInfoTemp = new TermInfo(id, subjectField, senseUrl);
        UrlMatching urlMatchTesting = new UrlMatching(merging, termUrl);
        TermInfo termInfo = urlMatchTesting.getTermInfo();
        assertEquals(termInfo.getSubjectId(), termInfoTemp.getSubjectId());
        assertEquals(termInfo.getTermID(), termInfoTemp.getTermID());

    }

    @Test
    public void testMatchingUrls_WhenMatchedAllUrls() throws Exception {
        
        SubjectFieldMerging merging = new SubjectFieldMerging(alphabetFileName, conceptFileName, subjectFileName, cannonical, sense,subjectDescriptions);
        Properties alphabetProps = getProperties(alphabetFileName);
        Integer index=0;
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
                        System.out.println(key+" "+termUrl+" "+termInfo.getSubjectId()+termInfo.getTermID());
                        String detail=merging.getSubjectDetailsProps(termInfo.getSubjectId()).toString();
                        System.out.println(detail);
                        index=index+1;
                        //break;
                    }
                }

            }

        }
          System.out.println("number of matched"+index);

//        System.out.println("finished");*/

        /*TermInfo termInfoTemp = new TermInfo(id, subjectField, senseUrl);
        UrlMatching urlMatchTesting = new UrlMatching(merging, termUrl);
        TermInfo termInfo = urlMatchTesting.getTermInfo();
        assertEquals(termInfo.getSubjectId(), termInfoTemp.getSubjectId());
        assertEquals(termInfo.getTermID(), termInfoTemp.getTermID());*/
    }

    @Ignore
    public void testMatchingUrls_WhenNotMatched() throws Exception {
        String termUrl = "http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_iate/data/iate/A-field-en#CanonicalForm";
        SubjectFieldMerging merging = new SubjectFieldMerging(alphabetFileName, conceptFileName, subjectFileName, cannonical, sense,null);
        String senseUrl = "odd+pulse-en#Sense";
        String id = "IATE-1608440";
        String subjectField = "6826";

        TermInfo termInfoTemp = new TermInfo(id, subjectField, senseUrl);
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
