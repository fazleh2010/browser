/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import static browser.termallod.constants.FilePathAndConstant.HTML_EXTENSION;
import static browser.termallod.constants.FilePathAndConstant.INITIAL_PAGE;
import static browser.termallod.constants.FilePathAndConstant.LOCALHOST_URL_LIST_OF_TERMS_PAGE;
import static browser.termallod.constants.FilePathAndConstant.PATH;
import static browser.termallod.constants.FilePathAndConstant.UNDERSCORE;
import static browser.termallod.constants.FilePathAndConstant.browser;
import static browser.termallod.constants.FilePathAndConstant.iate;
import static browser.termallod.constants.FilePathAndConstant.languageMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import browser.termallod.constants.HtmlPage;
import browser.termallod.utils.StringMatcherUtil;

/**
 *
 * @author elahi
 */
public class HtmlPageGenerator implements HtmlPage {

    private final Document generatedHtmlPage;
    private final Integer currentPageNumber;
    private final Integer maximumNumberOfPages = 4;
    private final String language;
    private final String ontologyFileName;
    private String categoryType = "";
    private File htmlFileName = null;
    private AlphabetTermPage alphabetTermPage;

    public HtmlPageGenerator(Document templateHtml, String language, AlphabetTermPage alphabetTermPage, List<String> terms, String categoryName, PageContentGenerator pageContentGenerator, Integer currentPageNumber) throws Exception {
        this.currentPageNumber = currentPageNumber;
        this.language = language;
        this.ontologyFileName = categoryName;
        this.alphabetTermPage = alphabetTermPage;
        String[] ontology = ontologyFileName.split("_");
        this.categoryType = ontology[1];
        this.generatedHtmlPage = this.generateHtmlFromTemplate(templateHtml, terms, pageContentGenerator, alphabetTermPage);
        this.htmlFileName = new File(PATH + this.ontologyFileName + "/" + createFileNameUnicode(language, currentPageNumber));
    }

    private Document generateHtmlFromTemplate(Document templateHtml, List<String> terms, PageContentGenerator pageContentGenerator, AlphabetTermPage alphabetTermPage) throws Exception {
        Element body = templateHtml.body();
        String alphebetPair = alphabetTermPage.getAlpahbetPair();
        Integer numberofPages = alphabetTermPage.getNumberOfPages();
        //currently not
        Integer emptyTerm = alphabetTermPage.getEmptyTerm();
        if (!this.categoryType.contains(iate)) {
            this.createLangSelectBox(body, pageContentGenerator);
        }

        createAlphabet(body, alphebetPair, pageContentGenerator);
        createTerms(body, terms, alphebetPair, emptyTerm, alphabetTermPage);

        /*Element divCurrentPageUpper = body.getElementsByClass("activepageUpper").get(0);
        this.assignCurrentPageNumber(divCurrentPageUpper);
         Element divCurrentPageLower = body.getElementsByClass("activepageLower");
        this.assignCurrentPageNumber(divCurrentPageLower);*/
        //createUperPageNumber(body, alphebetPair, numberofPages);
        //upper page number
        createPageNumber(body, "paging_links inner", alphebetPair, numberofPages);
        //lower page number
        createPageNumber(body, "paging_links inner_down", alphebetPair, numberofPages);
        return templateHtml;
    }

    @Override
    public void createLangSelectBox(Element body, PageContentGenerator pageContentGenerator) throws Exception {
        Element divLanguage = body.getElementsByClass("langauge selection box").get(0);
        String options = "<ul class=" + "\"" + "language-list" + "\"" + ">";
        for (String languageCode : pageContentGenerator.getLanguages()) {
            if (languageMapper.containsKey(languageCode)) {
                String languageDetail = languageMapper.get(languageCode);
                String pair = pageContentGenerator.getLanguageInitpage(languageCode);
                String url = this.createUrlLink(languageCode, INITIAL_PAGE);
                String option = "<li>&#8227; <a href=" + "\"" + url + "\"" + ">" + languageDetail + "</a></li>";
                options += option;
            }
        }
        options = options + "</ul>";
        String form = "<form>" + options + "</form>";
        divLanguage.append(form);
    }

    @Override
    public String createAlphabet(Element body, String alphebetPair, PageContentGenerator pageContentGenerator) throws Exception {
        Element divAlphabet = body.getElementsByClass("currentpage").get(0);
        divAlphabet.append("<span>" + alphebetPair + "</span>");
        List<AlphabetTermPage> alphabetPairs = pageContentGenerator.getLangPages(language);
        for (AlphabetTermPage alphabetTermPage : alphabetPairs) {
            if (!alphabetTermPage.getAlpahbetPair().contains(alphebetPair)) {
                String li = getAlphebetLi(INITIAL_PAGE, alphabetTermPage);
                divAlphabet.append(li);
            }

        }
        return alphebetPair;
    }

    @Override
    public void createPageNumber(Element body, String elementName, String alphebetPair, Integer numberofPages) {
        //Element divPageDown = body.getElementsByClass("paging_links inner_down").get(0);
        Element divPage = body.getElementsByClass(elementName).get(0);

        List<String> liS = getPageLi(alphebetPair, numberofPages, alphabetTermPage);
        if (liS.isEmpty()) {
            return;
        }
        for (String li : liS) {
            divPage.append(li);
        }
    }

    @Override
    public void createTerms(Element body, List<String> terms, String alphebetPair, Integer emptyTerm, AlphabetTermPage alphabetTermPage) {
        Element divTerm = body.getElementsByClass("result-list1 wordlist-oxford3000 list-plain").get(0);
        for (String term : terms) {
            String liString = getTermLi(alphebetPair, term, alphabetTermPage);
            divTerm.append(liString);
            /*File termFile =new File(termFileLocation(term));
            HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(TERM_PAGE_TEMPLATE);
            Document templateHtml = htmlReaderWriter.getInputDocument();
            htmlReaderWriter.writeHtml(templateHtml, termFile);*/
        }

        /*for (Integer index=0;index<emptyTerm;index++) {
            String liString = "";
            divTerm.append(liString);
        }*/
    }

    private String getTermLi(String alphebetPair, String term, AlphabetTermPage alphabetTermPage) {
        //<li><a href="https://www.oxfordlearnersdictionaries.com/definition/english/abandon_1" title="abandon definition">abandon</a> </li>
        String title = "title=" + '"' + term + " definition" + '"';
        //real version
        //String url = this.path+"/"+DEFINITION+"/" +language+"/" +alphebetPair +"/" +term + "_1";
        String url = generateTermUrl(term, alphabetTermPage);
        term = StringMatcherUtil.decripted(term);
        System.out.println(term + "..." + url);
        //String url = LOCALHOST_URL + "termDefination.php";
        //System.out.println(url);
        String a = "<a href=" + url + " " + title + ">" + term + "</a>";
        String li = "\n<li>" + a + "</li>\n";
        return li;
    }

    private String getAlphebetLi(Integer pageNumber, AlphabetTermPage alphabetTermPage) {
        //Elements divAlphabet = body.getElementsByClass("side-selector__left");
        //Element content = body.getElementById("entries-selector");
        /*String ontologyLocation="";
        if(categoryOntologyMapper.containsKey(this.categoryName)){
            ontologyLocation=categoryOntologyMapper.get(categoryName);
        }*/
        String url = this.createUrlLink(language, pageNumber, alphabetTermPage);
        //String url = LOCALHOST_URL_LIST_OF_TERMS_PAGE + alphabetFileName;
        String a = "<a href=" + url + ">" + alphabetTermPage.getAlpahbetPair() + "</a>";
        String li = "\n" + "<li>" + a + "</li>" + "\n";
        return li;
    }

    private List<String> getPageLi(String pair, Integer pages, AlphabetTermPage alphabetTermPage) {
        //Elements divAlphabet = body.getElementsByClass("side-selector__left");
        //Element content = body.getElementById("entries-selector");
        List<String> liS = new ArrayList<String>();
        String pageUrl = null;
        String li = "";
        /*"<span>" + this.currentPageNumber + "</span>";
        liS.add(li);*/
        if (pages == 1) {
            return new ArrayList<String>();
        }
        if (this.currentPageNumber > INITIAL_PAGE) {
            pageUrl = createUrlLink(language, currentPageNumber - 1);
            String a = "<a href=" + pageUrl + ">" + "Previous" + "</a>";
            li = "\n<li>" + a + "</li>\n";
            liS.add(li);
        }
        Integer index = 0;
        for (Integer page = this.currentPageNumber; page < pages; page++) {
            Integer pageNumber = (page + 1);
            pageUrl = createUrlLink(language, pageNumber);
            String a = "<a href=" + pageUrl + ">" + pageNumber + "</a>";
            li = "\n<li>" + a + "</li>\n";
            liS.add(li);
            if (index > this.maximumNumberOfPages && (pageNumber + 1) < pages) {
                pageUrl = createUrlLink(language, pageNumber + 1);
                a = "<a href=" + pageUrl + ">" + "Next" + "</a>";
                li = "\n<li>" + a + "</li>\n";
                liS.add(li);
                break;
            }

            index++;

        }

        return liS;
    }

    private String createUrlLink(String languageCode, Integer pageNumber) {
        return LOCALHOST_URL_LIST_OF_TERMS_PAGE + this.createFileNameUnicode(languageCode, pageNumber);
    }

    private String createUrlLink(String languageCode, Integer pageNumber, AlphabetTermPage alphabetTermPage) {
        return LOCALHOST_URL_LIST_OF_TERMS_PAGE + this.createFileNameWithPairNumber(languageCode, pageNumber, alphabetTermPage);
    }

    /*private String createFileNameUnicode(String languageCode, Integer pageNumber) {
        String pair=alphabetTermPage.getAlpahbetPair();
        pair = UrlUtils.getEncodedUrl(pair);
        return browser + UNDERSCORE + languageCode + UNDERSCORE + pair + UNDERSCORE + pageNumber + HTML_EXTENSION;
    }*/
    private String createFileNameUnicode(String languageCode, Integer pageNumber) {
        String pair = getPairValue(alphabetTermPage);
        return browser + UNDERSCORE + languageCode + UNDERSCORE + pair.toString() + UNDERSCORE + pageNumber + HTML_EXTENSION;
    }

    private String createFileNameWithPairNumber(String languageCode, Integer pageNumber, AlphabetTermPage alphabetTermPage) {
        String pair = getPairValue(alphabetTermPage);
        return browser + UNDERSCORE + languageCode + UNDERSCORE + pair.toString() + UNDERSCORE + pageNumber + HTML_EXTENSION;
    }

    private String getPairValue(AlphabetTermPage alphabetTermPage1) {
        String pair;
        if (language.equals("en")) {
            pair = alphabetTermPage1.getAlpahbetPair();
        } else {
            pair = alphabetTermPage1.getNumericalValueOfPair().toString();
        }
        return pair;
    }

    private void assignCurrentPageNumber(Element divCurrentPage) {
        divCurrentPage.append("<span>" + this.currentPageNumber + "</span> </li>");

    }

    private String assignHighLightPageNumber(String pageUrl) {
        String a = "<a href=" + pageUrl + ">" + "&gt;" + "</a>";
        return "\n<li>" + a + "</li>\n";

    }

    /*private String generateTermUrl(String term) {
        String outputfileString = LOCALHOST_URL_LIST_OF_TERMS_PAGE + this.generateTermFileName() + this.termFileExtension(term);
        return outputfileString;
    }*/
    private String generateTermFileName() {
        String outputfileString = ontologyFileName + "/" + "data" + "/" + this.categoryType + "/";
        return outputfileString;
    }

    private String termFileExtension(String term) {
        return term.trim().replace(" ", "+") + "-" + language.toUpperCase() + HTML_EXTENSION;
    }

    private String generateTermUrl(String term, AlphabetTermPage alphabetTermPage) {
        return alphabetTermPage.getUrl(term);
    }

    private String termFileLocation(String term) {
        return PATH + this.generateTermFileName() + this.termFileExtension(term);
    }

    public Document getGeneratedHtmlPage() {
        return generatedHtmlPage;
    }

    public File getHtmlFileName() {
        return htmlFileName;
    }
}
