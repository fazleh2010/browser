/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.app;

import browser.termallod.core.AlphabetTermPage;
import browser.termallod.core.PageContentGenerator;
import browser.termallod.core.html.HtmlListOfTerms;
import browser.termallod.core.html.HtmlReaderWriter;
import browser.termallod.core.html.OntologyInfo;
import browser.termallod.core.term.TermDetail;
import browser.termallod.utils.FileRelatedUtils;
import browser.termallod.utils.Partition;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import org.jsoup.nodes.Document;

/**
 *
 * @author Mohammad Fazleh Elahi
 */
public class HtmlCreator {

    private final Set<String> languages;
    private String OUTPUT_PATH;
    private String TEMPLATE_LOCATION;
    private String categoryName;

    public HtmlCreator(String INPUT_PATH, Set<String> languages, String TEMPLATE_LOCATION,String OUTPUT_PATH,String categoryName) throws Exception {
        this.languages = languages;
        this.TEMPLATE_LOCATION=TEMPLATE_LOCATION;
        this.OUTPUT_PATH = OUTPUT_PATH;
        this.categoryName=categoryName;
        this.getInfoFromSavedFiles(INPUT_PATH);
    }

    public void getInfoFromSavedFiles(String INPUT_PATH) throws Exception {
        TreeMap<String, RetrieveAlphabetInfo> langSortedTerms = new TreeMap<String, RetrieveAlphabetInfo>();
        String langCode = null;
        if (languages.contains("en")) {
            langCode = "en";
        } else {
            langCode = languages.iterator().next();
        }

        RetrieveAlphabetInfo retrieveAlphabetInfo=new RetrieveAlphabetInfo(INPUT_PATH, ".txt");
        langSortedTerms.put(langCode, retrieveAlphabetInfo);
        createHtmlForEachLanguage(langSortedTerms);
    }

    private void createHtmlForEachLanguage(TreeMap<String, RetrieveAlphabetInfo> langSortedTerms) throws Exception {
        PageContentGenerator pageContentGenerator = new PageContentGenerator(langSortedTerms);
        for (String language : pageContentGenerator.getLanguages()) {
            List<AlphabetTermPage> alphabetTermPageList = pageContentGenerator.getLangPages(language);
            for (AlphabetTermPage alphabetTermPage : alphabetTermPageList) {
                File LIST_OF_Terms = getTemplate(categoryName, language, ".html");
                createHtmlForEachAlphabetPair(categoryName, LIST_OF_Terms, language, alphabetTermPage, pageContentGenerator);
                //temporay added....
                break;
            }
            //temporary added..
            break;
        }
    }

    
    private File getTemplate(String categoryName, String langCode, String extension) throws Exception {
        return new File(TEMPLATE_LOCATION + categoryName + extension);
    }

   private void createHtmlForEachAlphabetPair(String categoryName, File templateFile, String language, AlphabetTermPage alphabetTermPage, PageContentGenerator pageContentGenerator) throws Exception {
        Partition partition = alphabetTermPage.getPartition();
        HtmlListOfTerms.termAlterUrl = new TreeMap<String, String>();

        for (Integer page = 0; page < partition.size(); page++) {
            Integer currentPageNumber = page + 1;
            List<String> terms = partition.get(page);
            List<TermDetail> termDetails = this.getTermDetails(categoryName, language, terms);
            HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(templateFile);
            Document templateHtml = htmlReaderWriter.getInputDocument();
            OntologyInfo info = new OntologyInfo(language, categoryName, alphabetTermPage);
            HtmlListOfTerms htmlPage = new HtmlListOfTerms(info, htmlReaderWriter);
            File outputFileName = new File(OUTPUT_PATH + info.creatHtmlFileName(currentPageNumber, alphabetTermPage));
            String htmlFileName = outputFileName.getName();

            Document listOfTermHtmlPage = htmlPage.createAllElements(templateHtml, termDetails, pageContentGenerator, htmlFileName, currentPageNumber);
                htmlReaderWriter.writeHtml(listOfTermHtmlPage, outputFileName);
           
            break;
        }

    }


    private List<TermDetail> getTermDetails(String category, String language, List<String> terms) {
        List<TermDetail> termDetails = new ArrayList<TermDetail>();
        String browserName = FileRelatedUtils.getBrowser(category);
        for (String term : terms) {
            TermDetail termDetail = new TermDetail(browserName, language, term);
            termDetails.add(termDetail);
        }
        return termDetails;
    }

}
