/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import browser.termallod.core.api.HtmlConnversionConstant;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author elahi
 */
public class HtmlPageGenerator implements HtmlConnversionConstant {

    private final AlphabetTermPage alphabetTermPage;
    private final Document generatedHtmlPage;
    private final PageContentGenerator pageContentGenerator;
    private final String categoryName;
    private final Integer currentPageNumber;
    private final String language;
    private File htmlFileName =null;


    public HtmlPageGenerator(Document templateHtml, String language, AlphabetTermPage alphabetTermPage, List<String> terms, String categoryName, PageContentGenerator pageContentGenerator, Integer currentPageNumber) throws Exception {
        this.alphabetTermPage = alphabetTermPage;
        this.pageContentGenerator = pageContentGenerator;
        this.categoryName = categoryName;
        this.currentPageNumber = currentPageNumber;
        this.language = language;
        this.generatedHtmlPage = this.changeBody(templateHtml, terms);
        this.htmlFileName =generateCategoryFileName(LIST_OF_TERMS_PAGE_LOCATION,currentPageNumber, categoryName, language, alphabetTermPage);
    }
    
    private File generateCategoryFileName(String LIST_OF_TERMS_PAGE_LOCATION,Integer page, String categoryName, String language, AlphabetTermPage alphabetTermPage) {
        String outputfileString = categoryName + "_" + language + "_";
        File outputFile = new File(LIST_OF_TERMS_PAGE_LOCATION +outputfileString + alphabetTermPage.getAlpahbetPair() + "_" + page + HTML_EXTENSION);
        return outputFile;
    }

    public Document changeBody(Document templateHtml, List<String> terms) throws Exception {
        Element body = templateHtml.body();
        modiyPage(body, terms);
        return templateHtml;
    }

    private void modiyPage(Element body, List<String> terms) throws Exception {
        String alphebetPair = alphabetTermPage.getAlpahbetPair();
        Integer numberofPages = alphabetTermPage.getNumberOfPages();
        //currently not
        Integer emptyTerm = alphabetTermPage.getEmptyTerm();
        createAlphabet(body, alphebetPair);
        createTerms(body, terms, alphebetPair, emptyTerm);

        /*Element divCurrentPageUpper = body.getElementsByClass("activepageUpper").get(0);
        this.assignCurrentPageNumber(divCurrentPageUpper);
         Element divCurrentPageLower = body.getElementsByClass("activepageLower");
        this.assignCurrentPageNumber(divCurrentPageLower);*/
        createUperPageNumber(body, alphebetPair, numberofPages);
        createLowerPageNumber(body, alphebetPair, numberofPages);

    }

    private String createAlphabet(Element body, String alphebetPair) throws Exception {
        Element divAlphabet = body.getElementsByClass("currentpage").get(0);
        divAlphabet.append("<span>" + alphebetPair + "</span>");
        List<String> alphabetPairsExists = pageContentGenerator.getAlpahbetTermsExists(language);
        for (String pair : alphabetPairsExists) {
            if (!pair.contains(alphebetPair)) {
                String alphabetFileName = categoryName + UNDERSCORE + language + UNDERSCORE + pair + UNDERSCORE + "1" + HTML_EXTENSION;
                String li = getAlphebetLi(pair, alphabetFileName);
                divAlphabet.append(li);
            }

        }
        return alphebetPair;
    }

    private void createUperPageNumber(Element body, String alphebetPair, Integer numberofPages) {
        Element divPageUper = body.getElementsByClass("paging_links inner").get(0);
        List<String> pageUperliS = getPageLi(alphebetPair, numberofPages);
        if (pageUperliS.isEmpty()) {
            return;
        }
        for (String li : pageUperliS) {
            divPageUper.append(li);
        }
    }

    private void createLowerPageNumber(Element body, String alphebetPair, Integer numberofPages) {
        Element divPageDown = body.getElementsByClass("paging_links inner_down").get(0);
        List<String> liS = getPageLi(alphebetPair, numberofPages);
        if (liS.isEmpty()) {
            return;
        }
        for (String li : liS) {
            divPageDown.append(li);
        }
    }

    private void createTerms(Element body, List<String> terms, String alphebetPair, Integer emptyTerm) {
        Element divTerm = body.getElementsByClass("result-list1 wordlist-oxford3000 list-plain").get(0);
        for (String term : terms) {
            String liString = getTermLi(alphebetPair, term);
            divTerm.append(liString);
        }
        /*for (Integer index=0;index<emptyTerm;index++) {
            String liString = "";
            divTerm.append(liString);
        }*/

    }

    private String getTermLi(String alphebetPair, String term) {
        //<li><a href="https://www.oxfordlearnersdictionaries.com/definition/english/abandon_1" title="abandon definition">abandon</a> </li>
        String title = "title=" + '"' + term + " definition" + '"';
        //real version
        //String url = this.path+"/"+DEFINITION+"/" +language+"/" +alphebetPair +"/" +term + "_1";
        String url = LOCALHOST_URL + "termDefination.php";
        String a = "<a href=" + url + " " + title + ">" + term + "</a>";
        String li = "\n<li>" + a + "</li>\n";
        return li;
    }

    private String getAlphebetLi(String alphabet, String alphabetFileName) {
        //Elements divAlphabet = body.getElementsByClass("side-selector__left");
        //Element content = body.getElementById("entries-selector");
        String url = LOCALHOST_URL + alphabetFileName;
        String a = "<a href=" + url + ">" + alphabet + "</a>";
        String li = "\n<li>" + a + "</li>\n";
        return li;
    }

    private List<String> getPageLi(String pair, Integer pages) {
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
        for (Integer page = 0; page < pages; page++) {
            Integer pageNumber = (page + 1);
            pageUrl = LOCALHOST_URL + categoryName + UNDERSCORE + language + UNDERSCORE + pair + UNDERSCORE + pageNumber + HTML_EXTENSION;
            String a = "<a href=" + pageUrl + ">" + pageNumber + "</a>";
            li = "\n<li>" + a + "</li>\n";
            liS.add(li);
        }
        return liS;
    }

    private void assignCurrentPageNumber(Element divCurrentPage) {
        divCurrentPage.append("<span>" + this.currentPageNumber + "</span> </li>");

    }

    private String assignHighLightPageNumber(String pageUrl) {
        String a = "<a href=" + pageUrl + ">" + "&gt;" + "</a>";
        return "\n<li>" + a + "</li>\n";

    }

    public Document getGeneratedHtmlPage() {
        return generatedHtmlPage;
    }

    public File getHtmlFileName() {
        return htmlFileName;
    }

}
