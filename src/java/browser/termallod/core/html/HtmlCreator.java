/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.html;

import browser.termallod.core.AlphabetTermPage;
import browser.termallod.core.TxtFileProcessing;
import browser.termallod.core.PageContentGenerator;
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

    public HtmlCreator(String INPUT_PATH, Set<String> languages, String OUTPUT_PATH) throws Exception {
        this.languages = languages;
        this.OUTPUT_PATH = OUTPUT_PATH;
        this.createHtmlForEachCategory(INPUT_PATH);
    }

    public void createHtmlForEachCategory(String INPUT_PATH) throws Exception {
        TreeMap<String, TxtFileProcessing> langSortedTerms = new TreeMap<String, TxtFileProcessing>();
        String langCode = null;
        if (languages.contains("en")) {
            langCode = "en";
        } else {
            langCode = languages.iterator().next();
        }

        List<File> files = FileRelatedUtils.getFiles(INPUT_PATH,langCode, ".txt");
        System.out.println(files.toString());

        //List<File> temFiles = languageFiles.get(langCode);
        //TxtFileProcessing category = new TxtFileProcessing(source, langCode, temFiles, MODEL_EXTENSION);
        //langSortedTerms.put(category.getLangCode(), category);
        //category.print(category.getLangSortedTerms());
    

    /*if (!langSortedTerms.isEmpty () 
        ) {
            if (categoryBrowser.contains(constants.IATE)) {
            createHtmlForEachLanguage(langSortedTerms, categoryName, browser);
        } else {
            createHtmlForEachLanguage(langSortedTerms, categoryName, browser);
        }*/

    }

    /*private void createHtmlForEachLanguage(TreeMap<String, TxtFileProcessing> langSortedTerms, String categoryName, String browser) throws Exception {
        PageContentGenerator pageContentGenerator = new PageContentGenerator(langSortedTerms, dataBaseTemp, htmlCreateParameters);
        for (String language : pageContentGenerator.getLanguages()) {
            List<AlphabetTermPage> alphabetTermPageList = pageContentGenerator.getLangPages(language);
            for (AlphabetTermPage alphabetTermPage : alphabetTermPageList) {
                File MAIN_PAGE_TEMPLATE = getTemplate(categoryName, language, ".html");
                createHtmlForEachAlphabetPair(categoryName, MAIN_PAGE_TEMPLATE, language, alphabetTermPage, pageContentGenerator);
                //temporay added....
                break;
            }
            //temporary added..
            break;
        }
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
            HtmlListOfTerms htmlPage = new HtmlListOfTerms(constants, htmlCreateParameters, info, htmlReaderWriter);
            File outputFileName = new File(OUTPUT_PATH + info.creatHtmlFileName(currentPageNumber, alphabetTermPage));
            String htmlFileName = outputFileName.getName();

            Document listOfTermHtmlPage = htmlPage.createAllElements(templateHtml, termDetails, pageContentGenerator, htmlFileName, currentPageNumber);
            if (this.htmlCreateParameters.getListOfTemPageFlag()) {
                htmlReaderWriter.writeHtml(listOfTermHtmlPage, outputFileName);
            }
            break;
        }
        if (htmlCreateParameters.getTextFileModifyFlag()) {
            String textInputFile = FileRelatedUtils.getSpecificFile(INPUT_PATH, categoryName, language, alphabetTermPage.getAlpahbetPair(), ".txt");
            FileRelatedUtils.writeFile(HtmlListOfTerms.termAlterUrl, textInputFile);
        }

    }

    private File getTemplate(String categoryName, String langCode, String extension) throws Exception {
        return new File(constants.TEMPLATE_LOCATION + categoryName + "_" + langCode + extension);
    }

    private File getTemplateTermDetail(String categoryName, String langCode, String extension) throws Exception {
        return new File(constants.TEMPLATE_LOCATION + categoryName + "_" + langCode + "_" + "term" + extension);
    }

    private File getTemplateTermAddDecline(String categoryName, String langCode, String extension) throws Exception {
        return new File(constants.TEMPLATE_LOCATION + categoryName + "_" + langCode + "_" + "add" + extension);
    }

    private List<TermDetail> getTermDetails(String category, String language, List<String> terms) {
        List<TermDetail> termDetails = new ArrayList<TermDetail>();
        String browserName = FileRelatedUtils.getBrowser(category);
        for (String term : terms) {
            TermDetail termDetail = new TermDetail(browserName, language, term);
            termDetails.add(termDetail);
        }
        return termDetails;
    }*/

}
