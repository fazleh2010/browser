/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.app;

import browser.termallod.core.AlphabetTermPage;
import browser.termallod.core.HtmlPageGenerator;
import browser.termallod.core.HtmlReaderWriter;
import browser.termallod.core.LanguageAlphabetPro;
import browser.termallod.core.RdfReader;
import browser.termallod.core.PageContentGenerator;
import browser.termallod.core.api.LanguageManager;
import java.io.File;
import org.jsoup.nodes.Document;
import browser.termallod.utils.FileRelatedUtils;
import browser.termallod.utils.Partition;
import java.io.IOException;
import java.util.List;
import browser.termallod.constants.FilePathAndConstant;
import browser.termallod.core.CategoryInfo;
import browser.termallod.utils.NameExtraction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class Main implements FilePathAndConstant {

    private static LanguageManager languageManager = null;
    private static Set<String> lang =new HashSet<String>();

    public static void main(String[] args) throws Exception {
        lang.add("bg");
        //lang.add("en");
        //lang.add("cs");
        /*lang.add("en");
        lang.add("sl");
        lang.add("mt");
        lang.add("ro");
        lang.add("hr");
        lang.add("hu");*/
        listOfTerm();
        //termDefination();
    }

    private static void listOfTerm() throws Exception {
        Main main = new Main();
        //main.inputLoader();
        main.process(categorySet, TEXT_EXTENSION);
    }

    private void inputLoader() throws Exception, IOException {
        languageManager = new LanguageAlphabetPro(configFile);
        FileRelatedUtils.cleanDirectory(categorySet, PATH, textPath);
        FileRelatedUtils.cleanDirectory(categoryOntologyMapper, PATH, dataPath);
        readRDFAndExtractInfo(PATH, categorySet, TURTLE, TURTLE_EXTENSION);
    }

    private void readRDFAndExtractInfo(String PATH, List<String> categorySet, String TURTLE, String TURTLE_EXTENSION) throws Exception {
        for (String browser : categorySet) {
            if (browser.contains(iate)) {
                String source = FileRelatedUtils.getSourcePath(PATH, browser);
                File[] files = FileRelatedUtils.getFiles(source, TURTLE_EXTENSION);
                String inputDir = source + rdfPath;
                String outputDir = source + textPath;
                new RdfReader(inputDir, languageManager, TURTLE, TURTLE_EXTENSION, outputDir);
            }
        }
    }

    private void process(List<String> categorySet, String MODEL_EXTENSION) throws Exception, IOException {
        FileRelatedUtils.cleanDirectory(categoryOntologyMapper, PATH, dataPath);
        for (String browser : categorySet) {
            if (browser.contains(iate)) {
                String source = FileRelatedUtils.getSourcePath(PATH, browser);
                List<String> browsers = categoryBrowser.get(browser);
                createHtmlForeachCategory(browsers, source, MODEL_EXTENSION);
            }
        }
    }

    private void createHtmlForeachCategory(List<String> browsers, String source, String MODEL_EXTENSION) throws Exception {
        for (String categoryBrowser : browsers) {
            String ontologyName = categoryOntologyMapper.get(categoryBrowser);
            List<File> files = FileRelatedUtils.getFiles(source + textPath, ontologyName, MODEL_EXTENSION);
            TreeMap<String, CategoryInfo> langSortedTerms = new TreeMap<String, CategoryInfo>();
            String categoryName = null;
            Map<String, List<File>> languageFiles = this.getLanguageFiles(files, MODEL_EXTENSION);
            for (String langCode : languageFiles.keySet()) {
                if (lang.contains(langCode)) {
                    List<File> temFiles = languageFiles.get(langCode);
                    CategoryInfo category = new CategoryInfo(source, langCode, temFiles, MODEL_EXTENSION);
                    langSortedTerms.put(category.getLangCode(), category);
                    categoryName = category.getCategoryName();
                    category.print(category.getLangSortedTerms());
                }
            }
            if (categoryBrowser.contains(iate)) {
                makeHtml(langSortedTerms, categoryName, MAIN_PAGE_TEMPLATE_IATE);
            } else {
                makeHtml(langSortedTerms, categoryName, MAIN_PAGE_TEMPLATE_GENTERM);
            }

        }
    }

    private void makeHtml(TreeMap<String, CategoryInfo> langSortedTerms, String categoryName, File MAIN_PAGE_TEMPLATE) throws Exception {
        PageContentGenerator pageContentGenerator = new PageContentGenerator(langSortedTerms);
        for (String language : pageContentGenerator.getLanguages()) {
            List<AlphabetTermPage> alphabetTermPageList = pageContentGenerator.getLangPages(language);
            for (AlphabetTermPage alphabetTermPage : alphabetTermPageList) {
                this.generateHtml(PATH, categoryName, MAIN_PAGE_TEMPLATE, language, alphabetTermPage, pageContentGenerator);
            }
        }
    }

    private void generateHtml(String baseDir, String categoryName, File templateFile, String language, AlphabetTermPage alphabetTermPage, PageContentGenerator pageContentGenerator) throws Exception {
        Partition partition = alphabetTermPage.getPartition();
        for (Integer page = 0; page < partition.size(); page++) {
            Integer currentPageNumber = page + 1;
            List<String> terms = partition.get(page);
            HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(templateFile);
            Document templateHtml = htmlReaderWriter.getInputDocument();
            HtmlPageGenerator htmlPage = new HtmlPageGenerator(templateHtml, language, alphabetTermPage, terms, categoryName, pageContentGenerator, currentPageNumber);
            htmlReaderWriter.writeHtml(htmlPage.getGeneratedHtmlPage(), htmlPage.getHtmlFileName());
        }

    }

    private Map<String, List<File>> getLanguageFiles(List<File> inputfiles, String model_extension) {
        Map<String, List<File>> languageFiles = new HashMap<String, List<File>>();
        for (File file : inputfiles) {
            String langCode = NameExtraction.getLanCode(file, model_extension);
            if (languageFiles.containsKey(langCode)) {
                List<File> files = languageFiles.get(langCode);
                files.add(file);
                languageFiles.put(langCode, files);
            } else {
                List<File> files = new ArrayList<File>();
                files.add(file);
                languageFiles.put(langCode, files);
            }

        }
        return languageFiles;
    }
    
    private static void termDefination() {
        
    }

}
