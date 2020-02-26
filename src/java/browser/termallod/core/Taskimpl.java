/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import browser.termallod.api.LanguageManager;
import browser.termallod.constants.FileAndCategory;
import browser.termallod.utils.FileRelatedUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import browser.termallod.core.html.HtmlCreator;
import java.util.Set;
import browser.termallod.api.Tasks;
import static browser.termallod.constants.FileAndCategory.AUTO_COMPLETION_TEMPLATE_LOCATION;
import static browser.termallod.constants.FileAndCategory.BASE_PATH;
import static browser.termallod.constants.FileAndCategory.BROWSER_CATEGORIES;
import static browser.termallod.constants.FileAndCategory.BROWSER_GROUPS;
import static browser.termallod.constants.FileAndCategory.CATEGORY_ONTOLOGIES;
import static browser.termallod.constants.FileAndCategory.DATA_PATH;
import static browser.termallod.constants.FileAndCategory.RDF_PATH;
import static browser.termallod.constants.FileAndCategory.TEXT_PATH;
import static browser.termallod.constants.FileAndCategory.TURTLE;
import static browser.termallod.constants.FileAndCategory.TURTLE_EXTENSION;
import browser.termallod.core.lucene.LuceneIndexing;
import browser.termallod.core.matching.MatchingTerminologies;
import browser.termallod.core.matching.TermDetail;
import browser.termallod.utils.GeneralCompScriptGen;
import java.text.ParseException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class Taskimpl implements Tasks, FileAndCategory {

    private Map<String, Browser> browsersInfor = new HashMap<String, Browser>();
    private final LanguageManager languageManager;
    private LuceneIndexing luceneIndexing = null;
    private GeneralCompScriptGen javaScriptCode = null;
    private MatchingTerminologies matchTerminologies = new MatchingTerminologies();

    public Taskimpl(File LANGUAGE_CONFIG_FILE) throws Exception {
        this.languageManager = new LanguageAlphabetPro(LANGUAGE_CONFIG_FILE);
    }

    //add decline page seperate creation
    /*@Override
    public void createAddDeclineHtmlPage(String category, String lang, TermDetail givenTermDetail, Set<String> givenLangs) {
        try {
            List<TermDetail> termDetails = matchTerminologies.getCategroyTerms(givenTermDetail);
            new HtmlCreator(BASE_PATH, givenLangs, category, lang, givenTermDetail, termDetails);
        } catch (Exception ex) {
            Logger.getLogger(Taskimpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

    @Override
    public void createHtmlFromSavedFiles(List<String> categorySet, String MODEL_EXTENSION, Set<String> browserSet, Set<String> lang,Boolean termPageFlag,Boolean termLinkPageFlag) throws Exception, IOException {
        FileRelatedUtils.cleanDirectory(CATEGORY_ONTOLOGIES, BASE_PATH, DATA_PATH);
        for (String browser : categorySet) {
            if (browserSet.contains(browser)) {
                String source = FileRelatedUtils.getSourcePath(BASE_PATH, browser);
                List<String> categoties = BROWSER_CATEGORIES.get(browser);
                HtmlCreator htmlCreator = new HtmlCreator(BASE_PATH, lang);
                htmlCreator.createHtmlForEachCategory(categoties, source, MODEL_EXTENSION, browser,termPageFlag,termLinkPageFlag);
            }
        }
    }

    @Override
    public void createIndexing() throws IOException, ParseException, Exception {
        this.luceneIndexing = new LuceneIndexing(this.browsersInfor);

    }

    @Override
    public void createIndexing(String browser) throws IOException, ParseException, Exception {
        this.luceneIndexing = new LuceneIndexing(this.browsersInfor, browser);

    }

    @Override
    public void prepareGroundForJs() throws IOException, Exception {
        File templateFile = new File(AUTO_COMPLETION_TEMPLATE_LOCATION + "autoComp" + ".js");
        this.javaScriptCode = new GeneralCompScriptGen(this.browsersInfor, templateFile);
    }

    @Override
    public void readDataFromSavedFiles(Boolean alternativeFlag) throws IOException, Exception {
        for (String browser : BROWSER_GROUPS) {
            List<String> categories = FileAndCategory.BROWSER_CATEGORIES.get(browser);
            this.readDataFromSavedFiles(browser, categories,alternativeFlag);
        }
    }

    @Override
    public void readDataFromSavedFiles(String givenBrowser,Boolean alternativeFlag) throws IOException, Exception {
        for (String browser : BROWSER_GROUPS) {
            if (browser.contains(givenBrowser)) {
                List<String> categories = FileAndCategory.BROWSER_CATEGORIES.get(browser);
                this.readDataFromSavedFiles(browser, categories,alternativeFlag);
            }
        }

    }
    
      private void readDataFromSavedFiles(String browser, List<String> categories, Boolean alternativeFlag) throws IOException, Exception {
        String source = FileRelatedUtils.getSourcePath(BASE_PATH, browser);
        for (String category : categories) {
            String ontologyName = CATEGORY_ONTOLOGIES.get(category);
            List<File> files = FileRelatedUtils.getFiles(source + TEXT_PATH, ontologyName, ".txt");
            if (files.isEmpty()) {
                throw new Exception("Text folder can not be empty!!!");
            }
            Map<String, List<File>> languageFiles = FileRelatedUtils.getLanguageFiles(files, ".txt");
            Map<String, LangSpecificBrowser> langSpecBrowsers = new HashMap<String, LangSpecificBrowser>();
            for (String langCode : languageFiles.keySet()) {
                List<File> termFiles = languageFiles.get(langCode);
                if (termFiles.isEmpty()) {
                    throw new Exception("No language files are found to process!!");
                }
                Map<String, String> allkeysValues = new HashMap<String, String>();
                for (File file : termFiles) {
                    Properties props =new Properties();
                    if (file.getName().contains("alter")) 
                        props = FileRelatedUtils.getPropertyHash(file);
                    else
                        props = FileRelatedUtils.getPropertyHash(file);
                        
                        Map<String, String> tempHash = (Map) props;
                        allkeysValues.putAll(tempHash);
                }
                langSpecBrowsers.put(langCode, new LangSpecificBrowser(langCode, allkeysValues));
            }
            Browser generalBrowser = new Browser(browser, category, langSpecBrowsers);
            browsersInfor.put(category, generalBrowser);
        }

    }

    /*private void readDataFromSavedFiles(String browser, List<String> categories,Boolean alternativeFlag) throws IOException, Exception {
        String source = FileRelatedUtils.getSourcePath(BASE_PATH, browser);
        for (String category : categories) {
            String ontologyName = CATEGORY_ONTOLOGIES.get(category);
            List<File> files = FileRelatedUtils.getFiles(source + TEXT_PATH, ontologyName, ".txt");
            if (files.isEmpty()) {
                throw new Exception("Text folder can not be empty!!!");
            }
            Map<String, List<File>> languageFiles = FileRelatedUtils.getLanguageFiles(files, ".txt");
            Map<String, LangSpecificBrowser> langSpecBrowsers = new HashMap<String, LangSpecificBrowser>();
            for (String langCode : languageFiles.keySet()) {
                List<File> termFiles = languageFiles.get(langCode);
                if (termFiles.isEmpty()) {
                    throw new Exception("No language files are found to process!!");
                }
                Map<String, String> allkeysValues = new HashMap<String, String>();
                for (File file : termFiles) {
                    Properties props = FileRelatedUtils.getPropertyHash(file);
                    Map<String, String> tempHash = (Map) props;
                    allkeysValues.putAll(tempHash);
                }
                langSpecBrowsers.put(langCode, new LangSpecificBrowser(langCode, allkeysValues));
            }
            Browser generalBrowser = new Browser(browser, category, langSpecBrowsers);
            browsersInfor.put(category, generalBrowser);
        }

    }*/

    @Override
    public void saveDataIntoFiles(Set<String> browserSet) throws Exception, IOException {
        FileRelatedUtils.cleanDirectory(BROWSER_GROUPS, BASE_PATH, TEXT_PATH);
        FileRelatedUtils.cleanDirectory(CATEGORY_ONTOLOGIES, BASE_PATH, DATA_PATH);
        for (String browser : BROWSER_GROUPS) {
            if (browserSet.contains(browser)) {
                saveDataIntoFiles(browser);
            }
        }

    }

    @Override
    public void saveDataIntoFiles(Set<String> browserSet, String browser) throws Exception, IOException {
        FileRelatedUtils.cleanDirectory(BROWSER_GROUPS, BASE_PATH, TEXT_PATH);
        FileRelatedUtils.cleanDirectory(CATEGORY_ONTOLOGIES, BASE_PATH, DATA_PATH);
        if (browserSet.contains(browser)) {
            saveDataIntoFiles(browser);
        }

    }

    private void saveDataIntoFiles(String browser) throws Exception {
        String source = FileRelatedUtils.getSourcePath(BASE_PATH, browser);
        File[] files = FileRelatedUtils.getFiles(source, TURTLE_EXTENSION);
        String inputDir = source + RDF_PATH;
        String outputDir = source + TEXT_PATH;
        new RdfReader(inputDir, languageManager, TURTLE, TURTLE_EXTENSION, outputDir);
    }

    @Override
    public List<String> search(String category, String langCode, String searchQuery) throws IOException, ParseException, Exception {
        return luceneIndexing.search(category, langCode, searchQuery);
    }

    @Override
    public Set<TermDetail> matchBrowsers() throws IOException, Exception {
        //currently does not work properly..
        MatchingTerminologies matchTerminologies = new MatchingTerminologies(this.browsersInfor);
        return new HashSet<TermDetail>();
    }

    //temporarily cloded
    /*
    @Override
    public void createTermDetailHtmlPage(String browser, Set<String> givenLangs) throws IOException, Exception {

        List<String> categories = BROWSER_CATEGORIES.get(browser);
        for (String category : categories) {
            MatchingTerminologies matchTerminologies = new MatchingTerminologies(this.browsersInfor, category);
            new HtmlCreator(BASE_PATH, givenLangs, matchTerminologies.getCategroyTerms(), category);
            break;
        }

    }*/

    public static String getOntologyName(String category) {
        return CATEGORY_ONTOLOGIES.get(category);
    }

    @Override
    public void generateScript() throws IOException, Exception {
        this.javaScriptCode.generateScript();
    }

    @Override
    public void generateScript(String category) throws IOException, Exception {
        this.javaScriptCode.generateScript(category);
    }

    @Override
    public Map<String, Browser> getBrowsersInfor() {
        return browsersInfor;
    }

}
