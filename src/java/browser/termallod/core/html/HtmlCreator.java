/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.html;

import browser.termallod.api.DataBaseTemp;
import browser.termallod.constants.FileAndLocationConst;
import browser.termallod.core.AlphabetTermPage;
import browser.termallod.core.TxtFileProcessing;
import browser.termallod.core.MergingTermInfo;
import browser.termallod.core.PageContentGenerator;
import browser.termallod.core.term.TermDetail;
import browser.termallod.utils.FileRelatedUtils;
import browser.termallod.utils.Partition;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.jsoup.nodes.Document;

/**
 *
 * @author elahi
 */
public class HtmlCreator  {
    
    private final Set<String> lang;
    private FileAndLocationConst constants;
    private MergingTermInfo merging;
    private HtmlParameters htmlCreateParameters;
    private DataBaseTemp dataBaseTemp;
    private String INPUT_PATH;
     private String OUTPUT_PATH;
    
    public HtmlCreator(FileAndLocationConst constants, Set<String> lang,HtmlParameters htmlCreateParameters,DataBaseTemp dataBaseTemp) {
        this.lang = lang;
        this.constants=constants;
        this.INPUT_PATH=constants.getINPUT_PATH();
        this.OUTPUT_PATH=constants.getOUTPUT_PATH();
        this.dataBaseTemp=dataBaseTemp;
        this.htmlCreateParameters=htmlCreateParameters;
    }

   
    public void createHtmlForEachCategory(List<String> browsers, String source, String MODEL_EXTENSION, String browser) throws Exception {
        for (String categoryBrowser : browsers) {
            String ontologyName = constants.CATEGORY_ONTOLOGIES.get(categoryBrowser);
            List<File> files = FileRelatedUtils.getFiles(source + constants.TEXT_PATH, ontologyName, MODEL_EXTENSION);
            TreeMap<String, TxtFileProcessing> langSortedTerms = new TreeMap<String, TxtFileProcessing>();
            String categoryName = null;
            Map<String, List<File>> languageFiles = FileRelatedUtils.getLanguageFiles(files, MODEL_EXTENSION);
            for (String langCode : languageFiles.keySet()) {
                if (lang.contains(langCode)) {
                    List<File> temFiles = languageFiles.get(langCode);
                    TxtFileProcessing category = new TxtFileProcessing(source, langCode, temFiles, MODEL_EXTENSION);
                    langSortedTerms.put(category.getLangCode(), category);
                    categoryName = category.getCategoryName();
                    //category.print(category.getLangSortedTerms());
                }
            }
            if (!langSortedTerms.isEmpty()) {
                if (categoryBrowser.contains(constants.IATE)) {
                    createHtmlForEachLanguage(langSortedTerms, categoryName, browser);
                } else {
                    createHtmlForEachLanguage(langSortedTerms, categoryName, browser);
                }
                //temporary implemented code
                break;
            }
        }
    }
    
    private void createHtmlForEachLanguage(TreeMap<String, TxtFileProcessing> langSortedTerms, String categoryName, String browser) throws Exception {
        PageContentGenerator pageContentGenerator = new PageContentGenerator(langSortedTerms,dataBaseTemp,htmlCreateParameters);
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
            File outputFileName =new File( OUTPUT_PATH+info.creatHtmlFileName(currentPageNumber,alphabetTermPage));
            String htmlFileName= outputFileName.getName();
            
            Document listOfTermHtmlPage = htmlPage.createAllElements(templateHtml, termDetails, pageContentGenerator,htmlFileName,currentPageNumber);
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
    
    private List<TermDetail> getTermDetails(String category,String language, List<String> terms) {
        List<TermDetail> termDetails = new ArrayList<TermDetail>();
        String browserName=FileRelatedUtils.getBrowser(category);
        for (String term : terms) {
            TermDetail termDetail = new TermDetail(browserName,language, term);
            termDetails.add(termDetail);
        }
        return termDetails;
    }
    
}
