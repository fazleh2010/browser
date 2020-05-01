/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.app;

import browser.termallod.api.DataBaseTemp;
import static browser.termallod.app.MainTest.browserSet;
import static browser.termallod.app.MainTest.constants;
import static browser.termallod.app.MainTest.languageMapper;
import browser.termallod.constants.FileAndLocationConst;
import browser.termallod.core.AlphabetTermPage;
import browser.termallod.core.MergingTermInfo;
import browser.termallod.core.html.HtmlParameters;
import browser.termallod.core.html.HtmlReaderWriter;
import browser.termallod.core.html.OntologyInfo;
import browser.termallod.core.html.HtmlTermPage;
import browser.termallod.core.term.TermInfo;
import browser.termallod.utils.Partition;
import browser.termallod.utils.UrlMatching;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author elahi
 */
public class TermPageTest {

    public static FileAndLocationConst constants;
    public static Set<String> browserSet;
    private static Set<String> lang = new TreeSet<String>();
    private static String BASE_PATH = "test/resources/data/";
    private Partition<String> partition;
    private String location = "/home/elahi/NetBeansProjects/newBrowser/linux/browser/test/resources/data/iate/txt/";
    private String alphabetFileName = location + "tbx2rdf_iate_en_A_B.txt";
    private String conceptFileName = location + "en.txt";
    private String subjectFileName = location + "subject.txt";
    private String cannonical = location + "canonicalForm.txt";
    private String sense = location + "sense.txt";
    private String subjectDetail = location + "subjectFields.txt";
    private HtmlParameters htmlCreateParameters = null;
    private MergingTermInfo subjectFieldMerging = null;
    private OntologyInfo info;
     private static String CONFIG_PATH = "src/java/resources/data/conf/";
    
    public DataBaseTemp dataBaseTemp=new DataBaseTemp(CONFIG_PATH);

    public TermPageTest() {
        lang = new TreeSet<String>(languageMapper.keySet());
        constants = new FileAndLocationConst(BASE_PATH,"","");
        browserSet = new HashSet<String>(Arrays.asList(constants.IATE));
        List<String> termList = new ArrayList<String>();
        termList.add("A");
        termList.add("Apple");
        termList.add("An appple");
        termList.add("All");
        //System.out.println(termList);
        Collections.sort(termList);
        this.partition = Partition.ofSize(termList, 100);

    }

    @Ignore
    public void testTermPage_WhenWIthoutLinks() throws Exception {
        /*String categoryName="tbx2rdf_iate";
        String language="en";
        String html=".html";
        String pair="A_B";
        String location = "test/resources/data/";
         String iate_folder = "iate/txt/";
        
        File templateFile = getTemplate(categoryName, language, html);
        HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(templateFile);
        AlphabetTermPage alphabetTermPage = new AlphabetTermPage(pair, new File(alphabetFileName), partition, 1);
        
        htmlCreateParameters = new HtmlParameters(false, true, true, true);
        subjectFieldMerging = new MergingTermInfo(location,iate_folder,language,alphabetFileName, dataBaseTemp);
        info = new OntologyInfo(language, categoryName, alphabetTermPage);
        HtmlTermPage termPage = new HtmlTermPage(htmlCreateParameters, info, htmlReaderWriter, subjectFieldMerging, constants);*/
        
    }

    private File getTemplate(String categoryName, String langCode, String extension) throws Exception {
        return new File(constants.TEMPLATE_LOCATION + categoryName + "_" + langCode + extension);
    }

}
