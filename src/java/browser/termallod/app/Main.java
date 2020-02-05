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

    public static Set<String> browserSet = new HashSet<String>(Arrays.asList(GENTERM));
    private static Set<String> lang = new TreeSet<String>();
    public static Map<String, String> languageMapper = new HashMap<String, String>() {
        {
            put("en", "English");
            put("nl", "Dutch");
        }
    };

    public static void main(String[] args) throws Exception {
        lang = new TreeSet<String>(languageMapper.keySet());
        cleanDirectory();
        
        Tasks tasks=new TermallodBrowser(LANGUAGE_CONFIG_FILE);
        tasks.saveDataIntoFiles(browserSet);
        tasks.createHtmlFromSavedFiles(BROWSER_GROUPS, TEXT_EXTENSION,browserSet,lang);
        
        //this is necessary for other applications!!
        tasks.readDataFromSavedFiles();
        //search Text
        String querystr = "prednison";
        tasks.createIndexing();
        tasks.search(ATC, "en", querystr);
       
        //create java script files
        tasks.prepareGroundForJs();
        tasks.generateScript();
  
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
