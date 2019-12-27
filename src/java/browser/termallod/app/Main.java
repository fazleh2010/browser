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
import browser.termallod.core.Ntriple;
import browser.termallod.core.PageContentGenerator;
import browser.termallod.core.api.LanguageManager;
import java.io.File;
import org.jsoup.nodes.Document;
import browser.termallod.utils.FileRelatedUtils;
import browser.termallod.utils.Partition;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.io.FileUtils;
import browser.termallod.constants.FilePathAndConstant;

/**
 *
 * @author elahi
 */
public class Main implements FilePathAndConstant {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.cleanDirectory();
        main.process(FilePathAndConstant.TURTLE, FilePathAndConstant.TURTLE_EXTENSION);
    }

    private void process(String modelType, String modelFileExtension) throws Exception, IOException {
        File[] files = FileRelatedUtils.getFiles(GENTERM_PATH, modelFileExtension);
        LanguageManager languageManager = new LanguageAlphabetPro(configFile);

        for (File categoryFile : files) {
            Ntriple ntriple = new Ntriple((GENTERM_PATH + categoryFile.getName()), languageManager, modelType);
            PageContentGenerator pageContentGenerator = new PageContentGenerator(ntriple);
            for (String language : pageContentGenerator.getLanguages()) {
                List<AlphabetTermPage> alphabetTermPageList = pageContentGenerator.getLangPages(language);
                for (AlphabetTermPage alphabetTermPage : alphabetTermPageList) {
                    String categoryName = categoryFile.getName().replace(modelFileExtension.trim(), "");
                    this.generateHtml(PATH, categoryName, MAIN_PAGE_TEMPLATE, language, alphabetTermPage, pageContentGenerator);
                }

            }
        }
    }

    private void cleanDirectory() throws IOException {
        //deleting all previous files
        for (String key : FilePathAndConstant.categoryOntologyMapper.keySet()) {
            key = categoryOntologyMapper.get(key);
            String mainDir = PATH + key;
            String[] infor = key.split("_");
            String termDir = PATH + key + File.separator + dataPath + File.separator + infor[1];
            deleteDirectory(mainDir, termDir);
            createDirectory(mainDir, termDir);
        }

    }

    private void deleteDirectory(String mainDir, String termDir) throws IOException {
        FileUtils.deleteDirectory(new File(mainDir));
        FileUtils.deleteDirectory(new File(termDir));
    }

    private void createDirectory(String mainDir, String termDir) throws IOException {
        Path mainPath = Paths.get(mainDir);
        Files.createDirectories(mainPath);
        Path termPath = Paths.get(termDir);
        Files.createDirectories(termPath);
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

}
