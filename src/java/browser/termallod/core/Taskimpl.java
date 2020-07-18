/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import browser.termallod.core.input.LangSpecificBrowser;
import browser.termallod.core.input.Browser;
import browser.termallod.api.DataBaseTemp;
import browser.termallod.api.LanguageManager;
import browser.termallod.utils.FileRelatedUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import browser.termallod.core.html.HtmlCreator;
import java.util.Set;
import static browser.termallod.app.Main.constants;
import browser.termallod.constants.FileAndLocationConst;
import browser.termallod.core.html.HtmlParameters;
import browser.termallod.core.lucene.LuceneIndexing;
import browser.termallod.core.matching.MatchingTerminologies;
import browser.termallod.core.term.TermDetail;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class Taskimpl  {

    private final LanguageManager languageManager;
    private final Set<String> givenBrowserSet;
    private final Boolean alternativeFlag;
    private Map<String, Browser> browsersInfor = new HashMap<String, Browser>();
    private LuceneIndexing luceneIndexing = null;
    private GeneralCompScriptGen javaScriptCode = null;
    private MatchingTerminologies matchTerminologies = new MatchingTerminologies();
    private Boolean indexCreated = false;
    private DataBaseTemp dataBaseTemp=null;
    private String CONFIG_PATH=null;
    private String INPUT_PATH=null;

    public Taskimpl(File LANGUAGE_CONFIG_FILE, Set<String> givenBrowserSet, FileAndLocationConst fileAndCategory, Boolean alternativeFlag,DataBaseTemp dataBaseTemp,String configDir) throws Exception {
        this.languageManager = new LanguageAlphabetPro(LANGUAGE_CONFIG_FILE);
        this.givenBrowserSet = givenBrowserSet;
        this.alternativeFlag = alternativeFlag;
        this.dataBaseTemp=dataBaseTemp;
        this.CONFIG_PATH=configDir;
        this.INPUT_PATH=fileAndCategory.getINPUT_PATH();
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

    public void createHtmlFromSavedFiles(FileAndLocationConst constants, Set<String> browserSet, Set<String> lang, HtmlParameters htmlCreateParameters,DataBaseTemp dataBaseTemp) throws Exception, IOException {
        Set<String> categorySet = constants.BROWSER_GROUPS;
        FileRelatedUtils.cleanDirectory(constants.CATEGORY_ONTOLOGIES, INPUT_PATH, "js");
        
        for (String browser : categorySet) {
            if (browserSet.contains(browser)) {
                String source = FileRelatedUtils.getSourcePath(INPUT_PATH, browser);
                List<String> categoties = constants.BROWSER_CATEGORIES.get(browser);
                HtmlCreator htmlCreator = new HtmlCreator(constants, lang, htmlCreateParameters,dataBaseTemp);
                htmlCreator.createHtmlForEachCategory(categoties, source, constants.TEXT_EXTENSION, browser);
            }
        }
         
    }

    public void createIndexing() throws IOException, ParseException, Exception {
        if (browsersInfor.isEmpty()) {
            this.readDataFromSavedFiles();
        }
        this.luceneIndexing = new LuceneIndexing(browsersInfor);
        this.indexCreated = true;

    }

    public void createIndexing(String browser) throws IOException, ParseException, Exception {
        if (this.browsersInfor.isEmpty()) {
            this.readDataFromSavedFiles();
        }
        this.luceneIndexing = new LuceneIndexing(this.browsersInfor, browser);
        this.indexCreated = true;

    }

    public void createJavaScriptForAutoComp(String browser) throws IOException, Exception {
        if (this.browsersInfor.isEmpty()) {
            this.readDataFromSavedFiles(browser);
        }
        File templateFile = new File(constants.AUTO_COMPLETION_TEMPLATE_LOCATION + "autoComp" + ".js");
        this.javaScriptCode = new GeneralCompScriptGen(this.browsersInfor, templateFile, constants, this.alternativeFlag);
        this.generateScript();
    }

    public void readDataFromSavedFiles() throws IOException, Exception {
        Map<String, Browser> browsersInfor = new HashMap<String, Browser>();
        if (this.checkGeneratedTextFiles(constants.BROWSER_GROUPS)) {
            for (String browser : constants.BROWSER_GROUPS) {
                List<String> categories = constants.BROWSER_CATEGORIES.get(browser);
                readDataFromSavedFiles(browser, categories, alternativeFlag);
            }
        } else  {
                throw new Exception("Text folder can not be empty!!!");
            }



    }

    public void readDataFromSavedFiles(String givenBrowser) throws IOException, Exception {
        Map<String, Browser> browsersInfor = new HashMap<String, Browser>();
        for (String browser : constants.BROWSER_GROUPS) {
            if (browser.contains(givenBrowser)) {
                List<String> categories = constants.BROWSER_CATEGORIES.get(browser);
                readDataFromSavedFiles(browser, categories, alternativeFlag);
            }
        }

    }

    private void readDataFromSavedFiles(String browser, List<String> categories, Boolean alternativeFlag) throws IOException, Exception {
        String source = FileRelatedUtils.getSourcePath(this.INPUT_PATH, browser);
        for (String category : categories) {
            String ontologyName = constants.CATEGORY_ONTOLOGIES.get(category);
            List<File> files = FileRelatedUtils.getFiles(source + constants.TEXT_PATH, ontologyName, ".txt");
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
                    Properties props = new Properties();
                    /*File selectedFile = null;
                    if (alternativeFlag) {
                        if (file.getName().contains("alter")) {
                            selectedFile = file;
                        }
                    } else {
                        selectedFile = file;
                    }*/
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

  
    public void saveDataIntoFiles(Set<String> browserSet) throws Exception, IOException {
        for (String browser : constants.BROWSER_GROUPS) {
            if (browserSet.contains(browser)) {
                saveDataIntoFiles(browser);
            }
        }

    }

    public void saveDataIntoFiles(Set<String> browserSet, String browser) throws Exception, IOException {
        if (browserSet.contains(browser)) {
            saveDataIntoFiles(browser);
        }

    }

    private void saveDataIntoFiles(String browser) throws Exception {
        new RdfToDatabase(browser, dataBaseTemp, languageManager, constants.TURTLE, constants.TURTLE_EXTENSION);
    }

    public List<String> search(String category, String langCode, String searchQuery) throws IOException, ParseException, Exception {
        return luceneIndexing.search(category, langCode, searchQuery);
    }

    public Set<TermDetail> matchTerminologies(String firstTerminology, String secondTerminology) throws IOException, Exception {
        //currently does not work properly..
        //MatchingTerminologies matchTerminologies = new MatchingTerminologies(browsersInforFirst,browsersInfoSecond);
        System.out.println("Matching terms!!!");
        if (!this.indexCreated) {
            this.createIndexing();
        }
        MatchingTerminologies matchTerminologies = new MatchingTerminologies(this.browsersInfor, secondTerminology);
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
        return constants.CATEGORY_ONTOLOGIES.get(category);
    }

    public void generateScript() throws IOException, Exception {
        this.javaScriptCode.generateScript();
    }

    public void generateScript(String category) throws IOException, Exception {
        this.javaScriptCode.generateScript(category);
    }

    public Map<String, Browser> getBrowsersInfor() {
        return browsersInfor;
    }

  
    private Boolean checkGeneratedTextFiles(Set<String> BROWSER_GROUPS) throws Exception {
        for (String browser : BROWSER_GROUPS) {
            String txtDir = constants.getINPUT_TXT_PATH(browser);
            File[] txtFiles = FileRelatedUtils.getFiles(txtDir, constants.TEXT_EXTENSION);
       
            if (txtFiles.length > 0) {
               return true;
            }
        }
       return false;
    }

}
