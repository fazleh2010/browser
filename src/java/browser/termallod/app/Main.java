/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.app;

import browser.termallod.constants.FileAndCategory;
import browser.termallod.core.Taskimpl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import browser.termallod.api.Tasks;
import static browser.termallod.constants.FileAndCategory.BASE_PATH;
import static browser.termallod.constants.FileAndCategory.CATEGORY_ONTOLOGIES;
import static browser.termallod.constants.FileAndCategory.DATA_PATH;
import browser.termallod.core.matching.TermDetail;
import browser.termallod.utils.FileRelatedUtils;
import java.io.IOException;

/**
 *
 * @author elahi
 */
public class Main implements FileAndCategory {

    public static Set<String> browserSet = new HashSet<String>(Arrays.asList(GENTERM));
    private static Set<String> lang = new TreeSet<String>();
    public static Map<String, String> languageMapper = new HashMap<String, String>() {
        {
            put("en", "English");
            //put("nl", "Dutch");
            /*put("bg", "Bulgarian");
            put("cs", "Czech");
            put("da", "Danish");
            put("de", "German");
            put("el", "Greek");
            put("en", "English");
            put("es", "German");
            put("et", "Estonian");
            put("fi", "Finnish");
            put("fr", "French");
            put("ga", "Irish");
            put("hr", "Croatian");
            put("hu", "Hungarian");
            put("it", "Italian");
            put("lt", "Lithuanian");
            put("lv", "Latvian");
            put("mt", "Maltese");
            put("nl", "Dutch");
            put("ro", "Romanian");
            put("sk", "Slovak");
            put("sl", "Slovenian");
            //put("sv", "Swedish");*/
        }
    };

    public static void main(String[] args) throws Exception {
        lang = new TreeSet<String>(languageMapper.keySet());
        // run before comit..................
        cleanDirectory();
        Boolean termPageFlag=true;
        Boolean termLinkPageFlag=true;

         Tasks tasks = new Taskimpl(LANGUAGE_CONFIG_FILE);
         //tasks.saveDataIntoFiles(browserSet);
        // tasks.createHtmlFromSavedFiles(BROWSER_GROUPS, TEXT_EXTENSION,browserSet,lang,termPageFlag,termLinkPageFlag);

        //this is necessary for other applications!!
        //tasks.readDataFromSavedFiles();
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
        //tasks.prepareGroundForJs();
        //tasks.generateScript();
    }
    // run before comit..................to clean all folder

    private static void cleanDirectory() throws IOException {
        FileRelatedUtils.cleanDirectory(CATEGORY_ONTOLOGIES, BASE_PATH, DATA_PATH);
        FileRelatedUtils.cleanDirectory(BROWSER_GROUPS, BASE_PATH, TEXT_PATH);
        FileRelatedUtils.cleanDirectory(CATEGORY_ONTOLOGIES, BASE_PATH, DATA_PATH);
    }

}
