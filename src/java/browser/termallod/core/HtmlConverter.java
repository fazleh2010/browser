/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import browser.termallod.core.api.LanguageManager;
import browser.termallod.utils.Partition;
import java.io.File;
import java.util.List;
import org.jsoup.nodes.Document;

/**
 *
 * @author elahi
 */
public class HtmlConverter {
    
    public static String PATH = "src/java/browser/termallod/";
    public static File MAIN_PAGE_TEMPLATE = new File(PATH + "listOfTermsFinal.html");
    public static File configFile = new File(PATH + "data/" + "language.conf");
    public static String termHtml = PATH + "html/";
    public static String GENTERM_PATH = PATH + "data/genterm/";
    public static String  DEFINITION = "definition";
    public static String localhost = "http://localhost/";
    public static String NTRIPLE_EXTENSION= ".ntriple";


    public HtmlConverter(String PATH, File category, File templateFile, String language, AlphabetTermPage alphabetTermPage,LanguageManager languageManager) throws Exception {
        this.convertToHtml(PATH, category,templateFile, language, alphabetTermPage,languageManager);

    }

    private void convertToHtml(String PATH, File category,File templateFile, String language, AlphabetTermPage alphabetTermPage,LanguageManager languageManager) throws Exception {
        Partition partition = alphabetTermPage.getPartition();
        for (Integer page = 0; page < partition.size(); page++) {
            File outputFile = generateFileName(PATH, page, category, language, alphabetTermPage);
            List<String> terms = partition.get(page);
            System.out.println(language + "  " + terms.toString());
            HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(templateFile);
            Document templateHtml = htmlReaderWriter.getInputDocument();
            HtmlModify modifyHtml = new HtmlModify(templateHtml, language, alphabetTermPage, terms, termHtml, localhost, DEFINITION,languageManager);
            htmlReaderWriter.writeHtml(modifyHtml.getNewDocument(), outputFile);
        }
    }

    private File generateFileName(String PATH, Integer page, File category, String language, AlphabetTermPage alphabetTermPage) {
        File outputFile = new File(PATH + "/html/" + page + category.getName().replace(".ntriple", "") + "_" + language + "_" + alphabetTermPage.getAlpahbetPair()+"_"+page+".html");
        return outputFile;
    }

}
