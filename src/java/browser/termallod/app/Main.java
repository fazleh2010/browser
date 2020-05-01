/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.app;

import browser.termallod.api.DataBaseTemp;
import browser.termallod.core.Taskimpl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import browser.termallod.constants.FileAndLocationConst;
import browser.termallod.core.MergingTermInfo;
import browser.termallod.core.html.HtmlParameters;
import browser.termallod.core.matching.MatchingTerminologies;
import browser.termallod.core.term.TermDetail;
import browser.termallod.utils.FileRelatedUtils;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author elahi
 */
public class Main {

    public static FileAndLocationConst constants;
    public static Set<String> browserSet;
    private static Set<String> lang = new TreeSet<String>();
    ///home/melahi/NetBeansProjects/data
    private static String BASE_PATH = "src/java/resources/data/";
    private static String HTML_OUTPUT_PATH = "/home/melahi/NetBeansProjects/data/";
    private static String INPUT_PATH = "/home/melahi/NetBeansProjects/data";

    private static String CONFIG_PATH = "src/java/resources/data/conf/";
    private static Set<String> browsersToRun = new HashSet<String>();

    private static Taskimpl tasks = null;
    private static DataBaseTemp dataBaseTemp = new DataBaseTemp(BASE_PATH);

    private static Boolean listOfTemPageFlag = true;
    private static Boolean termPageFlag = true;
    private static Boolean alternativeFlag = true;
    private static Boolean textFileModifyFlag = true;

    public static Map<String, String> languageMapper = new HashMap<String, String>() {
        {
            put("nl", "Dutch");
            put("en", "English");
            //put("bg", "Bulgarian");
            //put("cs", "Czech");
            //put("da", "Danish");
            //put("de", "German");
            //put("en", "English");
            //put("el", "Greek");
            //put("es", "Spanish");
            //put("et", "Estonian");
            //put("fr", "French");
            //put("ga", "Irish");
            //put("hr", "Croatian");
            //put("hu", "Hungarian");

            /*put("fi", "Finnish");
             put("it", "Italian");
             put("lt", "Lithuanian");
             put("lv", "Latvian");
             put("mt", "Maltese");
             put("nl", "Dutch");
             put("ro", "Romanian");
             put("sk", "Slovak");
             put("sl", "Slovenian");
             put("sv", "Swedish");*/
        }
    };

    public static void main(String[] args) throws Exception {
        HtmlParameters htmlCreateParameters = null;
        MergingTermInfo mergingTermInfo = null;
        alternativeFlag = true;
        lang = new TreeSet<String>(languageMapper.keySet());
        constants = new FileAndLocationConst(BASE_PATH, INPUT_PATH, HTML_OUTPUT_PATH);
        browserSet = new HashSet<String>(Arrays.asList(constants.GENTERM));

        /*cleanDirectory();
        tasks = new Taskimpl(constants.getLANGUAGE_CONFIG_FILE(), browserSet, constants, alternativeFlag, dataBaseTemp,CONFIG_PATH);
        //tasks.matchTerminologies(constants.GENTERM, constants.IATE);
         tasks.saveDataIntoFiles(browserSet);
        
        //Currently does not work
        textFileModifyFlag = true;
        listOfTemPageFlag = false;
        termPageFlag = false;
        alternativeFlag = true;
        tasks = new Taskimpl(constants.getLANGUAGE_CONFIG_FILE(), browserSet, constants, alternativeFlag, dataBaseTemp,CONFIG_PATH);
        browsersToRun=new HashSet<String>(Arrays.asList(constants.GENTERM));
         //tasks = new Taskimpl(constants.getLANGUAGE_CONFIG_FILE(), browserSet, constants, alternativeFlag, dataBaseTemp,CONFIG_PATH);
         htmlCreateParameters = new HtmlParameters(textFileModifyFlag, listOfTemPageFlag,  termPageFlag,alternativeFlag);
         tasks.createHtmlFromSavedFiles(constants,browsersToRun,lang, htmlCreateParameters,dataBaseTemp);*/
        ////////////////////////////////////////////////////
        //2. generate alternative url
        //3. generate HTML
        textFileModifyFlag = false;
        listOfTemPageFlag = true;
        termPageFlag = true;
        alternativeFlag = true;
        htmlCreateParameters=new HtmlParameters( false, true,  true, true);
        tasks = new Taskimpl(constants.getLANGUAGE_CONFIG_FILE(), browserSet, constants, alternativeFlag, dataBaseTemp,CONFIG_PATH);
        tasks.matchTerminologies(constants.GENTERM, constants.IATE);
        //testMatching();
        browsersToRun=new HashSet<String>(Arrays.asList(constants.GENTERM));
        tasks.createHtmlFromSavedFiles(constants,browsersToRun,lang, htmlCreateParameters,dataBaseTemp);
        runJavaScript();

//create java script files
        //it works seperately
        //tasks = new Taskimpl(constants.getLANGUAGE_CONFIG_FILE(), browserSet, constants, alternativeFlag, dataBaseTemp,CONFIG_PATH);
        System.out.println("Processing finished!!!");

        //  System.out.println("Processing finished!!!");
        //this is necessary for other applications!!
        //tasks.readDataFromSavedFiles(GENTERM,alternativeFlag);
        // tasks.readDataFromSavedFiles(GENTERM);
        //tasks.createTermDetailHtmlPage(ATC);
        //tasks.createTermDetailHtmlPage(terms);
        //tasks.matchBrowsers();
        //search Text
        String querystr = "association";
        /*tasks.createIndexing(IATE);
        tasks.search(IATE, "en", querystr);*/
        //tasks.createTermDetailHtmlPage(GENTERM,lang);

        //TermDetail termDetail=new TermDetail(ATC,"eng","test", "http");
        //tasks.createAddDeclineHtmlPage(ATC,"en", termDetail, lang);
        //create java script files
        //tasks.createJavaScriptForAutoComp();
        //tasks.generateScript();
        //match terms..
        //tasks.readDataFromSavedFiles(GENTERM, alternativeFlag);
        //tasks.readDataFromSavedFiles(IATE,false);
        //tasks.createIndexing(IATE);
        //Map<String, Browser> browsersInfoSecond = tasks.readDataFromSavedFiles(IATE,false);
        // List<TermDetail> termDetails=MatchingTerminologies.getTermDetails("alcaftadine");
        // System.out.println(termDetails.toString());
    }
    // run before comit..................to clean all folder

    private static void cleanDirectory() throws IOException {
        FileRelatedUtils.cleanDirectory(constants.CATEGORY_ONTOLOGIES, constants.getOUTPUT_PATH());
        // FileRelatedUtils.cleanDirectory(constants.BROWSER_GROUPS, constants.getBASE_PATH(), constants.TEXT_PATH);
    }

    private static void testMatching() {
        for (String lang : MatchingTerminologies.getLangTermDetails().keySet()) {
            Map<String, List<TermDetail>> matchedTerms = MatchingTerminologies.getLangTermDetails().get(lang);
            for (String term : matchedTerms.keySet()) {
                List<TermDetail> termDetails = matchedTerms.get(term);
                for (TermDetail termDetail : termDetails) {
                    if (term.contains("acipimox")) {
                        for (String termOrg : termDetail.getTermLinks().keySet()) {
                            String url = termDetail.getTermLinks().get(termOrg);
                            //System.out.println(termOrg);
                            //System.out.println(url);
                        }
                    }

                }
            }
        }
    }

    private static void runJavaScript() throws Exception {
        HtmlParameters htmlCreateParameters = new HtmlParameters(false, true, true, true);
        Taskimpl tasks = new Taskimpl(constants.getLANGUAGE_CONFIG_FILE(), browserSet, constants, alternativeFlag, dataBaseTemp, CONFIG_PATH);
        tasks.createJavaScriptForAutoComp(constants.GENTERM);
    }

}
