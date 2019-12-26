/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.main;

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
import java.util.List;

/**
 *
 * @author elahi
 */
public class Main {

    public static String PATH = "src/java/browser/termallod/";
    public static File MAIN_PAGE_TEMPLATE = new File(PATH + "listOfTermsFinal.html");
    public static File configFile = new File(PATH + "data/" + "language.conf");
    public static String GENTERM_PATH = PATH + "data/genterm/";
    public static String NTRIPLE_EXTENSION = ".ntriple";

    public static void main(String[] args) throws Exception {
        Main main=new Main();
        File[] files = FileRelatedUtils.getFiles(GENTERM_PATH, NTRIPLE_EXTENSION);
        LanguageManager languageManager = new LanguageAlphabetPro(configFile);

        for (File categoryFile : files) {
            Ntriple ntriple = new Ntriple((GENTERM_PATH + categoryFile.getName()), languageManager);
            PageContentGenerator pageContentGenerator = new PageContentGenerator(ntriple);
            for (String language : pageContentGenerator.getLanguages()) {
                List<AlphabetTermPage> alphabetTermPageList = pageContentGenerator.getLangPages(language);
                for (AlphabetTermPage alphabetTermPage : alphabetTermPageList) {
                    String categoryName=categoryFile.getName().replace(".ntriple", "");
                    main.generateHtml(PATH, categoryName, MAIN_PAGE_TEMPLATE, language, alphabetTermPage, pageContentGenerator);
                }

            }
            break;
        }
    }
    
    
    private void generateHtml(String baseDir, String categoryName, File templateFile, String language, AlphabetTermPage alphabetTermPage, PageContentGenerator pageContentGenerator) throws Exception {
        Partition partition = alphabetTermPage.getPartition();
        for (Integer page = 0; page < partition.size(); page++) {
            Integer currentPageNumber=page+1;
            List<String> terms = partition.get(page);
            HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(templateFile);
            Document templateHtml = htmlReaderWriter.getInputDocument();
            HtmlPageGenerator htmlPage = new HtmlPageGenerator(templateHtml, language, alphabetTermPage, terms,categoryName,pageContentGenerator,currentPageNumber);
            htmlReaderWriter.writeHtml(htmlPage.getGeneratedHtmlPage(), htmlPage.getHtmlFileName());
        }

    }

}
