/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.main;

import browser.termallod.core.AlphabetTermPage;
import browser.termallod.core.HtmlToConverter;
import browser.termallod.core.LanguageAlphabetPro;
import browser.termallod.core.Ntriple;
import browser.termallod.core.PageContentGenerator;
import browser.termallod.core.api.LanguageManager;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import browser.termallod.utils.FileRelatedUtils;
import browser.termallod.utils.Partition;

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
        File[] files = FileRelatedUtils.getFiles(GENTERM_PATH, NTRIPLE_EXTENSION);
        LanguageManager languageManager = new LanguageAlphabetPro(configFile);

        for (File categoryFile : files) {
            Ntriple ntriple = new Ntriple((GENTERM_PATH + categoryFile.getName()), languageManager);
            PageContentGenerator pageContentGenerator = new PageContentGenerator(ntriple);
            for (String language : pageContentGenerator.getLanguages()) {
                List<AlphabetTermPage> alphabetTermPageList = pageContentGenerator.getLangPages(language);
                for (AlphabetTermPage alphabetTermPage : alphabetTermPageList) {
                    String categoryName=categoryFile.getName().replace(".ntriple", "");
                    HtmlToConverter htmlConverter = new HtmlToConverter(PATH, categoryName, MAIN_PAGE_TEMPLATE, language, alphabetTermPage, pageContentGenerator);
                }

                break;
            }
            break;
        }
    }

}
