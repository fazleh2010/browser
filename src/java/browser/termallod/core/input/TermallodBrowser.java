/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.input;

import browser.termallod.api.LanguageManager;
import browser.termallod.constants.FileAndCategory;
import browser.termallod.core.LanguageAlphabetPro;
import browser.termallod.core.RdfReader;
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
import browser.termallod.core.lucene.LuceneIndexing;
import browser.termallod.core.matching.MatchingTerminologies;
import browser.termallod.core.matching.TermDetail;
import browser.termallod.utils.GeneralCompScriptGen;
import java.text.ParseException;
import java.util.HashSet;

/**
 *
 * @author elahi
 */
public class TermallodBrowser implements Tasks, FileAndCategory {

    private Map<String, Browser> browsersInfor = new HashMap<String, Browser>();
    private final LanguageManager languageManager;
    private LuceneIndexing luceneIndexing = null;
    private GeneralCompScriptGen javaScriptCode = null;

    public TermallodBrowser(File LANGUAGE_CONFIG_FILE) throws Exception {
        this.languageManager = new LanguageAlphabetPro(LANGUAGE_CONFIG_FILE);
    }

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
    public void readDataFromSavedFiles() throws IOException, Exception {
        for (String browser : BROWSER_GROUPS) {
            List<String> categories = FileAndCategory.BROWSER_CATEGORIES.get(browser);
            this.readDataFromSavedFiles(browser, categories);
        }
    }

    @Override
    public void readDataFromSavedFiles(String givenBrowser) throws IOException, Exception {
        for (String browser : BROWSER_GROUPS) {
            if (browser.contains(givenBrowser)) {
                List<String> categories = FileAndCategory.BROWSER_CATEGORIES.get(browser);
                this.readDataFromSavedFiles(browser, categories);
            }
        }

    }

    private void readDataFromSavedFiles(String browser, List<String> categories) throws IOException, Exception {
        String source = FileRelatedUtils.getSourcePath(BASE_PATH, browser);
        for (String category : categories) {
            String ontologyName = CATEGORY_ONTOLOGIES.get(category);
            List<File> files = FileRelatedUtils.getFiles(source + TEXT_PATH, ontologyName, ".txt");
            if (files.isEmpty()) {
                throw new Exception("Text folder can not be empty!!!");
            }
            Map<String, List<File>> languageFiles = FileRelatedUtils.getLanguageFiles(files, ".txt");
            Browser generalBrowser = new Browser(browser, category);
            for (String langCode : languageFiles.keySet()) {
                List<File> temFiles = languageFiles.get(langCode);
                if (temFiles.isEmpty()) {
                    throw new Exception("No language files are found to process!!");
                }
                Map<String, String> allkeysValues = new HashMap<String, String>();
                for (File file : files) {
                    Properties props = FileRelatedUtils.getPropertyHash(file);
                    Map<String, String> tempHash = (Map) props;
                    allkeysValues.putAll(tempHash);
                }
                generalBrowser.setLangTermUrls(langCode, allkeysValues);
            }
            browsersInfor.put(category, generalBrowser);
        }
        
    }

    @Override
    public void createHtmlFromSavedFiles(List<String> categorySet, String MODEL_EXTENSION, Set<String> browserSet, Set<String> lang) throws Exception, IOException {
        FileRelatedUtils.cleanDirectory(CATEGORY_ONTOLOGIES, BASE_PATH, DATA_PATH);
        for (String browser : categorySet) {
            if (browserSet.contains(browser)) {
                String source = FileRelatedUtils.getSourcePath(BASE_PATH, browser);
                List<String> categoties = BROWSER_CATEGORIES.get(browser);
                HtmlCreator htmlCreator = new HtmlCreator(BASE_PATH, lang);
                htmlCreator.createHtmlForEachCategory(categoties, source, MODEL_EXTENSION, browser);
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
    public List<String> search(String category, String langCode, String searchQuery) throws IOException, ParseException, Exception {
        return luceneIndexing.search(category, langCode, searchQuery);
    }

    public static String getOntologyName(String category) {
        return CATEGORY_ONTOLOGIES.get(category);
    }

    @Override
    public void prepareGroundForJs() throws IOException, Exception {
        File templateFile = new File(AUTO_COMPLETION_TEMPLATE_LOCATION + "autoComp" + ".js");
        this.javaScriptCode = new GeneralCompScriptGen(this.browsersInfor, templateFile);
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
    public Set<TermDetail> matchBrowsers() throws IOException, Exception {
        //currently does not work properly..
        MatchingTerminologies matchTerminologies=new MatchingTerminologies(this.browsersInfor);
        return new HashSet<TermDetail>();
    }

}
