/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.html;

import browser.termallod.constants.FileAndCategory;
import browser.termallod.core.AlphabetTermPage;
import browser.termallod.core.CategoryInfo;
import browser.termallod.core.PageContentGenerator;
import browser.termallod.core.matching.MatchingTerminologies;
import browser.termallod.core.matching.TermDetail;
import browser.termallod.utils.FileRelatedUtils;
import browser.termallod.utils.NameExtraction;
import browser.termallod.utils.Partition;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.jsoup.nodes.Document;

/**
 *
 * @author elahi
 */
public class HtmlCreator implements FileAndCategory {

    private final Set<String> lang;
    private final String PATH;

    public HtmlCreator(String PATH, Set<String> lang) {
        this.PATH = PATH;
        this.lang = lang;

    }

    /*term page creation code..currently it is running for 'list of term page'
    public HtmlCreator(String PATH, Set<String> lang, Map<String, List<TermDetail>> langTerms, String categoryName) throws Exception {
        this(PATH, lang);
        this.createHtmlForTermDetailEachCategory(langTerms, categoryName);

    }*/
    /*
    //Add decline page seperate creation..public HtmlCreator(String PATH, Set<String> givenlang, String category, String lang, TermDetail givenTermDetail, List<TermDetail> termDetails) throws Exception {
        this(PATH, givenlang);
        createHtmlForAddDecPageForEachTerm(category, lang, givenTermDetail, termDetails);
    }*/

    /*
    //Add decline page seperate creation..
    private void createHtmlForAddDecPageForEachTerm(String category, String lang, TermDetail givenTermDetail, List<TermDetail> termDetails) throws Exception {
        String ontologyName = CATEGORY_ONTOLOGIES.get(category);
        File templateFile = getTemplateTermAddDecline(ontologyName, lang, ".html");
        HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(templateFile);
        Document templateHtml = htmlReaderWriter.getInputDocument();

        HtmlModifier htmlPage = new HtmlModifier(PATH, templateHtml, givenTermDetail, termDetails, category);
        htmlReaderWriter.writeHtml(htmlPage.getGeneratedHtmlPage(), htmlPage.getHtmlFileName());

        System.out.println(htmlPage.toString());
    }*/

    //Term page creation code. Currently it is created from 'List of Term' page
    /*public void createHtmlForTermDetailEachCategory(Map<String, List<TermDetail>> langTerms, String category) throws Exception {
        for (String lang : langTerms.keySet()) {
            List<TermDetail> termDetails = langTerms.get(lang);
            for (TermDetail termDetail : termDetails) {
                if (lang.contains(termDetail.getLangCode())) {
                    String ontologyName = CATEGORY_ONTOLOGIES.get(category);
                    System.out.println(ontologyName);
                    File templateFile = getTemplateTermDetail(ontologyName, termDetail.getLangCode(), ".html");
                    HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(templateFile);
                    Document templateHtml = htmlReaderWriter.getInputDocument();
                    System.out.println(templateHtml.toString());
                    HtmlModifier htmlPage = new HtmlModifier(PATH, templateHtml, termDetail, category);
                    htmlReaderWriter.writeHtml(htmlPage.getGeneratedHtmlPage(), htmlPage.getHtmlFileName());
                    //System.out.println(htmlPage.toString());
                    break;
                }

            }
            break;
        }

    }*/
    public void createHtmlForEachCategory(List<String> browsers, String source, String MODEL_EXTENSION, String browser,Boolean termPageFlag,Boolean termLinkPageFlag) throws Exception {
        for (String categoryBrowser : browsers) {
            String ontologyName = CATEGORY_ONTOLOGIES.get(categoryBrowser);
            List<File> files = FileRelatedUtils.getFiles(source + TEXT_PATH, ontologyName, MODEL_EXTENSION);
            TreeMap<String, CategoryInfo> langSortedTerms = new TreeMap<String, CategoryInfo>();
            String categoryName = null;
            Map<String, List<File>> languageFiles = FileRelatedUtils.getLanguageFiles(files, MODEL_EXTENSION);
            for (String langCode : languageFiles.keySet()) {
                if (lang.contains(langCode)) {
                    List<File> temFiles = languageFiles.get(langCode);
                    CategoryInfo category = new CategoryInfo(source, langCode, temFiles, MODEL_EXTENSION);
                    langSortedTerms.put(category.getLangCode(), category);
                    categoryName = category.getCategoryName();
                    category.print(category.getLangSortedTerms());
                }
            }
            if (!langSortedTerms.isEmpty()) {
                if (categoryBrowser.contains(IATE)) {
                    createHtmlForEachLanguage(langSortedTerms, categoryName, browser,termPageFlag,termLinkPageFlag);
                } else {
                    createHtmlForEachLanguage(langSortedTerms, categoryName, browser,termPageFlag,termLinkPageFlag);
                }
            }
         System.out.println(categoryBrowser);
        }
    }

    private void createHtmlForEachLanguage(TreeMap<String, CategoryInfo> langSortedTerms, String categoryName, String browser,Boolean termPageFlag,Boolean termLinkPageFlag) throws Exception {
        PageContentGenerator pageContentGenerator = new PageContentGenerator(langSortedTerms);
        for (String language : pageContentGenerator.getLanguages()) {
            List<AlphabetTermPage> alphabetTermPageList = pageContentGenerator.getLangPages(language);
            for (AlphabetTermPage alphabetTermPage : alphabetTermPageList) {
                File MAIN_PAGE_TEMPLATE = getTemplate(categoryName, language, ".html");
                createHtmlForEachAlphabetPair(categoryName, MAIN_PAGE_TEMPLATE, language, alphabetTermPage, pageContentGenerator,termPageFlag,termLinkPageFlag);
                //temporay added....
               
            }
        }
    }

    private void createHtmlForEachAlphabetPair(String categoryName, File templateFile, String language, AlphabetTermPage alphabetTermPage, PageContentGenerator pageContentGenerator,Boolean termPageFlag,Boolean termLinkPageFlag) throws Exception {
         Map<String, String> termAlterUrlsAll = new TreeMap<String, String>();
        Partition partition = alphabetTermPage.getPartition();
        for (Integer page = 0; page < partition.size(); page++) {
            Integer currentPageNumber = page + 1;
            List<String> terms = partition.get(page);
            List<TermDetail> termDetails = this.getTermDetails(language, terms);
            HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(templateFile);
            Document templateHtml = htmlReaderWriter.getInputDocument();
            HtmlModifier htmlPage = new HtmlModifier(PATH, templateHtml, language, alphabetTermPage, termDetails, categoryName, pageContentGenerator, currentPageNumber,true,termPageFlag,termLinkPageFlag);
            htmlReaderWriter.writeHtml(htmlPage.getGeneratedHtmlPage(), htmlPage.getHtmlFileName());
            for (File termFile : htmlPage.getGeneratedTermHtmlPages().keySet()) {
                Document generatedHtml = htmlPage.getGeneratedTermHtmlPages().get(termFile);
                htmlReaderWriter.writeHtml(generatedHtml, termFile);
            }
            for (File termFile : htmlPage.getTermLinkHtmlPages().keySet()) {
                Document generatedHtml = htmlPage.getTermLinkHtmlPages().get(termFile);
                htmlReaderWriter.writeHtml(generatedHtml, termFile);
            }
             /*Map<String, String> termAlterUrls  = htmlPage.getTermAlterUrls();
             for(String term:termAlterUrls.keySet()){
                 String alterUrl=termAlterUrls.get(term);
                 termAlterUrlsAll.put(term, alterUrl);
             }*/
           
            
           //temporay added....
              
        }
        //System.out.println(termAlterUrlsAll.toString());

    }

    private File getTemplate(String categoryName, String langCode, String extension) throws Exception {
        return new File(TEMPLATE_LOCATION + categoryName + "_" + langCode + extension);
    }

    private File getTemplateTermDetail(String categoryName, String langCode, String extension) throws Exception {
        return new File(TEMPLATE_LOCATION + categoryName + "_" + langCode + "_" + "term" + extension);
    }

    private File getTemplateTermAddDecline(String categoryName, String langCode, String extension) throws Exception {
        return new File(TEMPLATE_LOCATION + categoryName + "_" + langCode + "_" + "add" + extension);
    }

    private List<TermDetail> getTermDetails(String language, List<String> terms) {
        List<TermDetail> termDetails = new ArrayList<TermDetail>();
        for (String term : terms) {
            TermDetail termDetail = new TermDetail(language, term,IATE,"https://terms.tdwg.org/wiki/skos:exactMatch","https://terms.tdwg.org/wiki/skos:exactMatch", false);
            termDetails.add(termDetail);
        }
        return termDetails;
    }

}
