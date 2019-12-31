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
import browser.termallod.core.RdfReading;
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
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class Main implements FilePathAndConstant {

    private static LanguageManager languageManager = null;

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        //main.inputLoader();
        main.process(categorySet, TEXT_EXTENSION);
        //read RDF file and save important information in text file. The RDF file is too big (7 GB)
        //reading fine everyting is very slow. Therefore only important information are added here.

    }

    private void inputLoader() throws Exception, IOException {
        languageManager = new LanguageAlphabetPro(configFile);
        FileRelatedUtils.cleanDirectory(categorySet, PATH, textPath);
        FileRelatedUtils.cleanDirectory(categoryOntologyMapper, PATH, dataPath);
        readRDFAndExtractInfo(PATH, categorySet, TURTLE, TURTLE_EXTENSION);
    }

    private void readRDFAndExtractInfo(String PATH, List<String> categorySet, String TURTLE, String TURTLE_EXTENSION) throws Exception {
        for (String browser : categorySet) {
            String source = FileRelatedUtils.getSourcePath(PATH, browser);
            File[] files = FileRelatedUtils.getFiles(source, TURTLE_EXTENSION);
            String inputDir = source + rdfPath;
            String outputDir = source + textPath;
            new RdfReading(inputDir, languageManager, TURTLE, TURTLE_EXTENSION, outputDir);
        }
    }

    private void process(List<String> categorySet, String MODEL_EXTENSION) throws Exception, IOException {
        FileRelatedUtils.cleanDirectory(categoryOntologyMapper, PATH, dataPath);
        for (String browser : categorySet) {
            if (browser.contains(genterm)) {
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
            TreeMap<String, TreeMap<String, List<String>>> langSortedTerms = new TreeMap<String, TreeMap<String, List<String>>>();
            String categoryName = null;
            for (File file : files) {
                CategoryInfo category = new CategoryInfo(source, file, MODEL_EXTENSION);
                langSortedTerms.put(category.getLangCode(), category.getLangSortedTerms());
                categoryName = category.getCategoryName();
            }
            makeHtml(langSortedTerms, categoryName);
        }
    }

    //TreeMap<String, TreeMap<String, List<String>>> langSortedTerms = category.getLangSortedTerms();
    /*PageContentGenerator pageContentGenerator = new PageContentGenerator(langSortedTerms);
            for (String language : pageContentGenerator.getLanguages()) {
                List<AlphabetTermPage> alphabetTermPageList = pageContentGenerator.getLangPages(language);
                for (AlphabetTermPage alphabetTermPage : alphabetTermPageList) {
                    this.generateHtml(PATH, category.getCategoryName(), MAIN_PAGE_TEMPLATE, language, alphabetTermPage, pageContentGenerator);
                }
            }*/
    private void makeHtml(TreeMap<String, TreeMap<String, List<String>>> langSortedTerms, String categoryName) throws Exception {
        PageContentGenerator pageContentGenerator = new PageContentGenerator(langSortedTerms);
        for (String language : pageContentGenerator.getLanguages()) {
            List<AlphabetTermPage> alphabetTermPageList = pageContentGenerator.getLangPages(language);
            for (AlphabetTermPage alphabetTermPage : alphabetTermPageList) {
                this.generateHtml(PATH, categoryName, MAIN_PAGE_TEMPLATE, language, alphabetTermPage, pageContentGenerator);
            }
        }
    }


    /*private void process(RdfReading rdfReading, String source, String modelType, String modelFileExtension) throws Exception, IOException {
        //File[] files = FileRelatedUtils.getFiles(source, modelFileExtension);

         for (File categoryFile : files) {
            String categoryName = getCategoryName(categoryFile, source, modelFileExtension);
            RdfReading ntriple = new RdfReading(source, (source + categoryFile.getName()), languageManager, modelType);
            PageContentGenerator pageContentGenerator = new PageContentGenerator(ntriple);
            for (String language : pageContentGenerator.getLanguages()) {
                List<AlphabetTermPage> alphabetTermPageList = pageContentGenerator.getLangPages(language);
                for (AlphabetTermPage alphabetTermPage : alphabetTermPageList) {
                    this.generateHtml(PATH, categoryName, MAIN_PAGE_TEMPLATE, language, alphabetTermPage, pageContentGenerator);
                }
            }
        }
    }*/
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

}
