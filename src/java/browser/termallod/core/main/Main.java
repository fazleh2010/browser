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
import browser.termallod.core.api.GenTerm;
import browser.termallod.core.api.HtmlPage;
import static browser.termallod.core.api.HtmlPage.PATH;
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

/**
 *
 * @author elahi
 */
public class Main implements GenTerm{

    public static String PATH = "src/java/browser/termallod/data/";
    public static File MAIN_PAGE_TEMPLATE = new File("src/java/browser/termallod/" + "listOfTermsFinal.html");
    public static File configFile = new File(PATH + "conf/" +"language.conf");
    public static String GENTERM_PATH = PATH + "genterm/";
    public static String NTRIPLE_EXTENSION = ".ntriple";
    public static String LIST_OF_TERMS_PAGE_LOCATION = PATH + "html/";

    public static void main(String[] args) throws Exception {
        Main main=new Main();
        main.cleanDirectory();
        
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
        }
    }

    private  void cleanDirectory() throws IOException {
        //deleting all previous files
        for (String dirName:GenTerm.categoryOntologyMapper.keySet()){
            dirName=categoryOntologyMapper.get(dirName);
            FileUtils.deleteDirectory(new File(PATH+dirName));
            Path path = Paths.get(PATH+dirName);
            Files.createDirectories(path);
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
