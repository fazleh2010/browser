/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.app;

import browser.termallod.api.DataBaseTemp;
import static browser.termallod.api.IATE.LANGUAGE_SEPERATE_SYMBOLE;
import browser.termallod.api.LanguageManager;
import browser.termallod.core.Taskimpl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import browser.termallod.constants.FileAndLocationConst;
import browser.termallod.core.LanguageAlphabetPro;
import browser.termallod.core.MergingTermInfo;
import browser.termallod.core.html.HtmlParameters;
import browser.termallod.core.matching.MatchingTerminologies;
import browser.termallod.core.term.TermDetail;
import browser.termallod.core.term.TermInfo;
import browser.termallod.utils.FileRelatedUtils;
import citec.core.sparql.CurlSparqlQuery;
import citec.core.sparql.SparqlEndpoint;
import citec.core.termbase.TermDetailNew;
import citec.core.termbase.Termbase;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class Main implements SparqlEndpoint {

    public static FileAndLocationConst constants;
    private static String BASE_PATH = "src/java/resources/data/";
    private static String OUTPUT_PATH = BASE_PATH+"/output/";
    private static String INPUT_PATH =BASE_PATH+ "/input/";
    private static String CONFIG_PATH = BASE_PATH+"/conf/";
    private static String TEMPLATE_PATH = BASE_PATH+"/template/";
    
     private static LanguageManager languageInfo;

    private static Taskimpl tasks = null;
    private static DataBaseTemp dataBaseTemp = new DataBaseTemp(BASE_PATH);

    private static Boolean listOfTemPageFlag = true;
    private static Boolean termPageFlag = true;
    private static Boolean alternativeFlag = true;
    private static Boolean textFileModifyFlag = true;
    private String htmlString = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "    <head>\n"
            + "        <title>Example</title>\n"
            + "    </head>\n"
            + "    <body>\n"
            + "        <p>This is an example of a simple HTML page with one paragraph.</p>\n"
            + "    </body>\n"
            + "</html>";

    //run german english french italian and all other languages
    public static Map<String, String> languageMapper = new HashMap<String, String>() {
        {
            //put("mt", "Maltese");
            //put("lt", "Lithuanian");

            put("en", "English");
            put("nl", "Dutch");
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
            // put("sv", "Swedish");
        }
    };

    //maltsease is wrong currently
    public static void main(String[] args) throws Exception {
        Integer index = 1;
       

        String myTermTableName = "myTerminology", otherTermTableName = "otherTerminology", matchedTermTable = "link";
        String myTermSparqlEndpoint = null,list=null;

        System.out.println("called");
        System.out.println("arguments: " + args.length);
        if (args.length > 0) {
            myTermSparqlEndpoint = args[0];
            System.out.println("SparqlEndpoint: " + myTermSparqlEndpoint);
        } else {
            myTermSparqlEndpoint = endpoint_solar;
            System.err.println("no sparql endpoint in arguments");
        }
        if (args.length > 1) {
            OUTPUT_PATH = args[1];
            System.out.println("output folder: " + OUTPUT_PATH);
        } else {
            System.err.println("no output folder in arguments");
           
        }
        if (args.length > 2) {
            list = args[2];
            System.out.println("output folder: " + list);
        } else {
        }
        
        languageInfo = new LanguageAlphabetPro(new File(BASE_PATH+"/conf/"+"language.conf"));
        Termbase myTerminology = new CurlSparqlQuery(myTermSparqlEndpoint, query_writtenRep, myTermTableName).getTermbase();
        
        
        CreateAlphabetFiles alphabetFiles=new CreateAlphabetFiles(languageInfo,myTerminology);
        FileRelatedUtils.deleteDirectory(INPUT_PATH);
        FileRelatedUtils.createDirectory(INPUT_PATH);
        FileRelatedUtils.writeFile(alphabetFiles.getLangTerms(), INPUT_PATH);   
        HtmlCreator htmlCreator = new HtmlCreator(INPUT_PATH,alphabetFiles.getLangTerms().keySet(), TEMPLATE_PATH,OUTPUT_PATH,"ListOfTerms");


        //System.out.println(htmlString);
        //FileRelatedUtils.stringToFile(htmlString, outputLocation + "ListofTerm.html");

        /*
          HtmlParameters htmlCreateParameters = null;
        MergingTermInfo mergingTermInfo = null;
        
        
        alternativeFlag = true;
        lang = new TreeSet<String>(languageMapper.keySet());
        constants = new FileAndLocationConst(BASE_PATH, INPUT_PATH, OUTPUT_PATH);
        browserSet = new HashSet<String>(Arrays.asList(constants.IATE));
        tasks = new Taskimpl(constants.getLANGUAGE_CONFIG_FILE(), browserSet, constants, alternativeFlag, dataBaseTemp,CONFIG_PATH);

        //Running and testing genterm
        //before commit run it
         cleanDirectoryInput(constants.GENTERM);
         //cleanDirectoryInput(constants.IATE);
         cleanDirectoryOutput();
          //cleanDirectoryOutput();
         //
         cleanDirectoryInput(constants.GENTERM);
         browserSet = new HashSet<String>(Arrays.asList(constants.GENTERM));
         tasks.saveDataIntoFiles(browserSet);         
         tasks.createHtmlFromSavedFiles(constants,browserSet,lang, new HtmlParameters(true, false,  false,true),dataBaseTemp);
         
          //Running and testing iate
          /*cleanDirectoryInput(constants.IATE);
          browserSet = new HashSet<String>(Arrays.asList(constants.IATE));
          tasks.saveDataIntoFiles(browserSet);         
          //tasks.createHtmlFromSavedFiles(constants,browserSet,lang, new HtmlParameters(true, false,  false,true),dataBaseTemp);*/
        //Running Genterm html
        /*tasks.matchTerminologies(constants.GENTERM, constants.IATE);
         browserSet = new HashSet<String>(Arrays.asList(constants.GENTERM));
         tasks.createHtmlFromSavedFiles(constants,browserSet,lang, new HtmlParameters(false, true,  true, true),dataBaseTemp);
        
        tasks.createJavaScriptForAutoComp(constants.GENTERM);*/
        System.out.println("Hello world!!!");

        //cleanDirectoryOutput();
        //tasks.matchTerminologies(constants.GENTERM, constants.IATE);
        //browserSet = new HashSet<String>(Arrays.asList(constants.IATE));
        //tasks.createHtmlFromSavedFiles(constants,browserSet,lang, new HtmlParameters(false, true,  true, true),dataBaseTemp);
        //tasks.createJavaScriptForAutoComp(constants.IATE);
        System.out.println("Processing iate finished!!!");

    }
    // run before comit..................to clean all folder

    private static void cleanDirectoryInput(String browser) throws IOException {
        FileRelatedUtils.cleanDirectory(constants.BROWSER_GROUPS, constants.getBASE_PATH(), constants.TEXT_PATH, browser);
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

    /*private static void runJavaScript(String browser) throws Exception {
        Taskimpl tasks = new Taskimpl(constants.getLANGUAGE_CONFIG_FILE(), browserSet, constants, alternativeFlag, dataBaseTemp, CONFIG_PATH);
        tasks.createJavaScriptForAutoComp(browser);
    }*/

    
}
