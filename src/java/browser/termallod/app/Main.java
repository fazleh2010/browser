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
    private static String OUTPUT_PATH = "src/java/resources/data/";
    private static String INPUT_PATH = "src/java/resources/data/";

    private static String CONFIG_PATH = "src/java/resources/data/conf/";

    private static Taskimpl tasks = null;
    private static DataBaseTemp dataBaseTemp = new DataBaseTemp(BASE_PATH);

    private static Boolean listOfTemPageFlag = true;
    private static Boolean termPageFlag = true;
    private static Boolean alternativeFlag = true;
    private static Boolean textFileModifyFlag = true;
    
    //run german english french italian and all other languages

    public static Map<String, String> languageMapper = new HashMap<String, String>() {
        {
             //put("mt", "Maltese");
             //put("lt", "Lithuanian");
            
             //put("en", "English");
           
             /*put("bg", "Bulgarian");
             put("cs", "Czech");
             put("da", "Danish");
              //put("de", "German");
             // put("en", "English");
             //put("el", "Greek");
             put("es", "Spanish");
             put("et", "Estonian");
              //put("fr", "French");
             put("ga", "Irish");
             put("hr", "Croatian");
             put("hu", "Hungarian");
             put("fi", "Finnish");
              //put("it", "Italian");
             put("lt", "Lithuanian");
             put("lv", "Latvian");
             put("mt", "Maltese");
             put("nl", "Dutch");
             put("ro", "Romanian");
             put("sk", "Slovak");
             put("sl", "Slovenian");
             put("sv", "Swedish");*/
            put("sv", "Swedish");
        }
    };
    
    //maltsease is wrong currently

    public static void main(String[] args) throws Exception {
        HtmlParameters htmlCreateParameters = null;
        MergingTermInfo mergingTermInfo = null;
        
        alternativeFlag = true;
        lang = new TreeSet<String>(languageMapper.keySet());
        constants = new FileAndLocationConst(BASE_PATH, INPUT_PATH, OUTPUT_PATH);
        browserSet = new HashSet<String>(Arrays.asList(constants.GENTERM,constants.IATE));
        tasks = new Taskimpl(constants.getLANGUAGE_CONFIG_FILE(), browserSet, constants, alternativeFlag, dataBaseTemp,CONFIG_PATH);

        //Running and testing genterm
        //before commit run it
         /*cleanDirectoryInput(constants.GENTERM);
         cleanDirectoryInput(constants.IATE);
         cleanDirectoryOutput();*/
          //cleanDirectoryOutput();
         //
         /*cleanDirectoryInput(constants.GENTERM);
         browserSet = new HashSet<String>(Arrays.asList(constants.GENTERM));
         tasks.saveDataIntoFiles(browserSet);         
         tasks.createHtmlFromSavedFiles(constants,browserSet,lang, new HtmlParameters(true, false,  false,true),dataBaseTemp);*/
         
          //Running and testing iate
          /*cleanDirectoryInput(constants.IATE);
          browserSet = new HashSet<String>(Arrays.asList(constants.IATE));
          tasks.saveDataIntoFiles(browserSet);         
          tasks.createHtmlFromSavedFiles(constants,browserSet,lang, new HtmlParameters(true, false,  false,true),dataBaseTemp);*/
        
        //Running Genterm html
         /*tasks.matchTerminologies(constants.GENTERM, constants.IATE);
         browserSet = new HashSet<String>(Arrays.asList(constants.GENTERM));
         tasks.createHtmlFromSavedFiles(constants,browserSet,lang, new HtmlParameters(false, true,  true, true),dataBaseTemp);
        
        tasks.createJavaScriptForAutoComp(constants.GENTERM);
        System.out.println("Processing genterm finished!!!");*/
        
        
         
        
          //cleanDirectoryOutput();
          tasks.matchTerminologies(constants.GENTERM, constants.IATE);
          browserSet = new HashSet<String>(Arrays.asList(constants.IATE));
          tasks.createHtmlFromSavedFiles(constants,browserSet,lang, new HtmlParameters(false, true,  true, true),dataBaseTemp);
        
          tasks.createJavaScriptForAutoComp(constants.IATE);
          System.out.println("Processing iate finished!!!");

    }
    // run before comit..................to clean all folder

    private static void cleanDirectoryInput(String browser) throws IOException {
        FileRelatedUtils.cleanDirectory(constants.BROWSER_GROUPS, constants.getBASE_PATH(), constants.TEXT_PATH,browser);
    }
    
    private static void cleanDirectoryOutput() throws IOException {
        FileRelatedUtils.cleanDirectory(constants.CATEGORY_ONTOLOGIES, constants.getOUTPUT_PATH());
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

    private static void runJavaScript(String browser) throws Exception {
        Taskimpl tasks = new Taskimpl(constants.getLANGUAGE_CONFIG_FILE(), browserSet, constants, alternativeFlag, dataBaseTemp, CONFIG_PATH);
        tasks.createJavaScriptForAutoComp(browser);
    }
    
    /*
            
        //Generated java script...
       Taskimpl tasks = new Taskimpl(constants.getLANGUAGE_CONFIG_FILE(), browserSet, constants, alternativeFlag, dataBaseTemp, CONFIG_PATH);
        tasks.createJavaScriptForAutoComp(constants.GENTERM);*/
        
        

//create java script files
        //it works seperately
        //tasks = new Taskimpl(constants.getLANGUAGE_CONFIG_FILE(), browserSet, constants, alternativeFlag, dataBaseTemp,CONFIG_PATH);
        

        //  System.out.println("Processing finished!!!");
        //this is necessary for other applications!!
        //tasks.readDataFromSavedFiles(GENTERM,alternativeFlag);
        // tasks.readDataFromSavedFiles(GENTERM);
        //tasks.createTermDetailHtmlPage(ATC);
        //tasks.createTermDetailHtmlPage(terms);
        //tasks.matchBrowsers();
        //search Text
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
   

}
