/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.app;

import browser.termallod.constants.FileAndCategory;
import browser.termallod.core.input.TermallodBrowser;
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
import browser.termallod.utils.FileRelatedUtils;
import java.io.IOException;

/**
 *
 * @author elahi
 */
public class Main  implements FileAndCategory{

    public static Set<String> browserSet = new HashSet<String>(Arrays.asList(IATE));
    private static Set<String> lang = new TreeSet<String>();
    public static Map<String, String> languageMapper = new HashMap<String, String>() {
        {
            put("en", "English");
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
        
        /*Tasks tasks=new TermallodBrowser(LANGUAGE_CONFIG_FILE);
        //tasks.saveDataIntoFiles(browserSet);
        //tasks.createHtmlFromSavedFiles(BROWSER_GROUPS, TEXT_EXTENSION,browserSet,lang);
        
        //this is necessary for other applications!!
    
        tasks.readDataFromSavedFiles(IATE);   
        
        //search Text
        String querystr = "association";
        tasks.createIndexing(IATE);
        tasks.search(IATE, "en", querystr);
       
        //create java script files
       /* tasks.prepareGroundForJs();
        tasks.generateScript();*/
  
    }
    
    private static void cleanDirectory() throws IOException{
        FileRelatedUtils.cleanDirectory(CATEGORY_ONTOLOGIES, BASE_PATH, DATA_PATH);
        FileRelatedUtils.cleanDirectory(BROWSER_GROUPS, BASE_PATH, TEXT_PATH);
        FileRelatedUtils.cleanDirectory(CATEGORY_ONTOLOGIES, BASE_PATH, DATA_PATH);
    }

    /*private static void inputLoader() throws Exception, IOException {
        LanguageManager languageManager = new LanguageAlphabetPro(LANGUAGE_CONFIG_FILE);
        FileRelatedUtils.cleanDirectory(BROWSER_GROUPS, BASE_PATH, TEXT_PATH);
        FileRelatedUtils.cleanDirectory(CATEGORY_ONTOLOGIES, BASE_PATH, DATA_PATH);
        for (String browser : BROWSER_GROUPS) {
            if (browserSet.contains(browser)) {
                String source = FileRelatedUtils.getSourcePath(BASE_PATH, browser);
                File[] files = FileRelatedUtils.getFiles(source, TURTLE_EXTENSION);
                String inputDir = source + RDF_PATH;
                String outputDir = source + TEXT_PATH;
                new RdfReader(inputDir, languageManager, TURTLE, TURTLE_EXTENSION, outputDir);
            }
        }
    }*/

    /*private static void createHtmlFromSavedFiles(List<String> categorySet, String MODEL_EXTENSION) throws Exception, IOException {
        FileRelatedUtils.cleanDirectory(CATEGORY_ONTOLOGIES, BASE_PATH, DATA_PATH);
        for (String browser : categorySet) {
            if (browserSet.contains(browser)) {
                String source = FileRelatedUtils.getSourcePath(BASE_PATH, browser);
                List<String> categoties = BROWSER_CATEGORIES.get(browser);
                HtmlCreator htmlCreator = new HtmlCreator(BASE_PATH,lang);
                htmlCreator.createHtmlForEachCategory(categoties, source, MODEL_EXTENSION, browser);
            }
        }
    }*/

}
