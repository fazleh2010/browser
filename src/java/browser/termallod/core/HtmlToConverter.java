/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import browser.termallod.core.api.HtmlConverter;
import browser.termallod.core.api.LanguageManager;
import browser.termallod.utils.Partition;
import java.io.File;
import java.util.List;
import javafx.util.Pair;
import org.jsoup.nodes.Document;

/**
 *
 * @author elahi
 */
public class HtmlToConverter implements HtmlConverter {

    public HtmlToConverter(String PATH, String categoryName, File templateFile, String language, AlphabetTermPage alphabetTermPage, PageContentGenerator pageContentGenerator) throws Exception {
        this.convertToHtml(PATH, categoryName, templateFile, language, alphabetTermPage, pageContentGenerator);

    }

    @Override
    public void convertToHtml(String PATH, String categoryName, File templateFile, String language, AlphabetTermPage alphabetTermPage, PageContentGenerator pageContentGenerator) throws Exception {
        Partition partition = alphabetTermPage.getPartition();
        for (Integer page = 0; page < partition.size(); page++) {
            File outputFile =generateFileName(PATH, page, categoryName, language, alphabetTermPage);
            List<String> terms = partition.get(page);
            HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(templateFile);
            Document templateHtml = htmlReaderWriter.getInputDocument();
            HtmlModify modifyHtml = new HtmlModify(templateHtml, language, alphabetTermPage, terms, LIST_OF_TERMS_PAGE_LOCATION, LOCALHOST_URL, TERM_DEFINATION_LOCATION, categoryName,pageContentGenerator);
            htmlReaderWriter.writeHtml(modifyHtml.getNewDocument(), outputFile);
        }
    }

    /*private File generateFileName(String PATH, Integer page, File category, String language, AlphabetTermPage alphabetTermPage) {
        File outputFile = new File(PATH + "/html/" + page + category.getName().replace(".ntriple", "") + "_" + language + "_" + alphabetTermPage.getAlpahbetPair()+"_"+page+".html");
        return outputFile;
    }*/
    private File generateFileName(String PATH, Integer page, String categoryName, String language, AlphabetTermPage alphabetTermPage) {
        String outputfileString = categoryName + UNDERSCORE + language + UNDERSCORE;
        File outputFile = new File(LIST_OF_TERMS_PAGE_LOCATION +outputfileString + alphabetTermPage.getAlpahbetPair() + UNDERSCORE + page + HTML_EXTENSION);
        return outputFile;
    }

}
