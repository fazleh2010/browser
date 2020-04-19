/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.html;

import browser.termallod.api.HtmlStringConts;
import browser.termallod.constants.FileAndLocationConst;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import browser.termallod.constants.HtmlPage;
import browser.termallod.constants.Languages;
import browser.termallod.core.AlphabetTermPage;
import browser.termallod.core.MergingTermInfo;
import browser.termallod.core.PageContentGenerator;
import browser.termallod.core.term.TermDetail;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author elahi //this is super dirty and horrible codes ever!!
 */
public class ListOfTerms extends HtmlPageAbstract implements HtmlPage, Languages, HtmlStringConts {

    public static Map<String, String> termAlterUrl = new TreeMap<String, String>();
    private final Integer maximumNumberOfPages = 4;
    private final TermPage termPage;


    public ListOfTerms(FileAndLocationConst constants, HtmlParameters htmlCreateParameters, OntologyInfo info, HtmlReaderWriter htmlReaderWriter, MergingTermInfo merging) throws Exception {
        super(htmlCreateParameters, info, htmlReaderWriter, merging, constants);
        this.termPage=new TermPage(htmlCreateParameters, info, htmlReaderWriter, merging, constants);
    }

    public Document createAllElements(Document templateHtml, List<TermDetail> terms, PageContentGenerator pageContentGenerator, File htmlFileName, Integer currentPageNumber) throws Exception {
        AlphabetTermPage alphabetTermPage = super.info.getAlphabetTermPage();
        Element body = templateHtml.body();
        String alphebetPair = alphabetTermPage.getAlpahbetPair();
        Integer numberofPages = alphabetTermPage.getNumberOfPages();
        //currently not
        Integer emptyTerm = alphabetTermPage.getEmptyTerm();
        //this part of code is used to automatically generated language selection box
        //currently it is hard coded in HTML template
        /*if (!this.categoryType.contains(iate)) {
            this.createLangSelectBox(body, pageContentGenerator);
        }*/

        createAlphabet(body, alphebetPair, pageContentGenerator);
        createTerms(body, terms, alphebetPair, emptyTerm, htmlFileName);

        /*Element divCurrentPageUpper = body.getElementsByClass("activepageUpper").get(0);
        this.assignCurrentPageNumber(divCurrentPageUpper);
         Element divCurrentPageLower = body.getElementsByClass("activepageLower");
        this.assignCurrentPageNumber(divCurrentPageLower);*/
        //createUperPageNumber(body, alphebetPair, numberofPages);
        //upper page number
        createPageNumber(body, "paging_links inner", alphebetPair, numberofPages, currentPageNumber);
        //lower page number
        createPageNumber(body, "paging_links inner_down", alphebetPair, numberofPages, currentPageNumber);
        return templateHtml;
    }

    private void createLangSelectBox(Element body, PageContentGenerator pageContentGenerator) throws Exception {
        Element divLanguage = body.getElementsByClass("langauge selection box").get(0);
        String options = "<ul class=" + "\"" + "language-list" + "\"" + ">";
        for (String languageCode : pageContentGenerator.getLanguages()) {
            if (languageMapper.containsKey(languageCode)) {
                String languageDetail = languageMapper.get(languageCode);
                String pair = pageContentGenerator.getLanguageInitpage(languageCode);
                String url = this.createUrlLink(INITIAL_PAGE);
                String option = "<li>&#8227; <a href=" + "\"" + url + "\"" + ">" + languageDetail + "</a></li>";
                options += option;
            }
        }
        options = options + "</ul>";
        String form = "<form>" + options + "</form>";
        divLanguage.append(form);
    }

    private String createAlphabet(Element body, String alphebetPair, PageContentGenerator pageContentGenerator) throws Exception {
        Element divAlphabet = body.getElementsByClass("currentpage").get(0);
        divAlphabet.append("<span>" + alphebetPair + "</span>");
        List<AlphabetTermPage> alphabetPairs = pageContentGenerator.getLangPages(super.info.getLanguage());
        for (AlphabetTermPage alphabetTermPage : alphabetPairs) {
            if (!alphabetTermPage.getAlpahbetPair().contains(alphebetPair)) {
                String li = getAlphebetLi(INITIAL_PAGE, alphabetTermPage);
                divAlphabet.append(li);
            }
        }
        return alphebetPair;
    }

    private void createPageNumber(Element body, String elementName, String alphebetPair, Integer numberofPages, Integer currentPageNumber) {
        Element divPage = body.getElementsByClass(elementName).get(0);
        List<String> liS = getPageLi(alphebetPair, numberofPages, super.info.getAlphabetTermPage(), currentPageNumber);
        if (liS.isEmpty()) {
            return;
        }
        for (String li : liS) {
            divPage.append(li);
        }
    }

   
    public void createTerms(Element body, List<TermDetail> terms, String alphebetPair, Integer emptyTerm, File htmlFileName) throws Exception {
        Element divTerm = body.getElementsByClass("result-list1 wordlist-oxford3000 list-plain").get(0);
        Integer index = 0;
        for (TermDetail termDetail : terms) {
            TermDetail newTermDetail = termPage.createTerms(termDetail, index++, htmlFileName);
            String liString = termPage.getTermLi(newTermDetail);
            divTerm.append(liString);
        }
    }

    

    private String getAlphebetLi(Integer pageNumber, AlphabetTermPage alphabetTermPage) {
        //there is an error in Hungarian langauge link in HTML template, 
        //since all static htmls are already generated so the problem is now solved by hardcoded.
        //extreme bad solution but quick solution. 

        //Elements divAlphabet = body.getElementsByClass("side-selector__left");
        //Element content = body.getElementById("entries-selector");
        /*String ontologyLocation="";
        if(categoryOntologyMapper.containsKey(this.categoryName)){
            ontologyLocation=categoryOntologyMapper.get(categoryName);
        }*/
        String url = this.createUrlLink(pageNumber);
        //String url = LOCALHOST_URL_LIST_OF_TERMS_PAGE + alphabetFileName;

        if (super.info.getLanguage().contains("hu") && alphabetTermPage.getNumericalValueOfPair() == 1) {
            url = "browser_hu_A_1_1.html";
        }

        String a = "<a href=" + url + ">" + alphabetTermPage.getAlpahbetPair() + "</a>";
        String li = "\n" + "<li>" + a + "</li>" + "\n";
        return li;
    }

    private List<String> getPageLi(String pair, Integer pages, AlphabetTermPage alphabetTermPage, Integer currentPageNumber) {
        //Elements divAlphabet = body.getElementsByClass("side-selector__left");
        //Element content = body.getElementById("entries-selector");
        List<String> liS = new ArrayList<String>();
        String pageUrl = null;
        String li = "";
        /*"<span>" + this.currentPageNumber + "</span>";
        liS.add(li);*/

        //there is an error in Hungarian langauge link in HTML template, 
        //since all static htmls are already generated so the problem is now solved by hardcoded.
        //extreme bad solution but quick solution. 
        if (pages == 1) {
            return new ArrayList<String>();
        }
        if (currentPageNumber > INITIAL_PAGE) {
            pageUrl = createUrlLink(currentPageNumber - 1);
            if (info.getLanguage().contains("hu") && currentPageNumber == 2) {
                pageUrl = "browser_hu_A_1_1.html";
            }
            String a = "<a href=" + pageUrl + ">" + "Previous" + "</a>";
            li = "\n<li>" + a + "</li>\n";
            liS.add(li);
        }
        Integer index = 0;
        for (Integer page = currentPageNumber; page < pages; page++) {
            Integer pageNumber = (page + 1);
            pageUrl = createUrlLink(pageNumber);
            if (info.getLanguage().contains("hu") && pageNumber == 1) {
                pageUrl = "browser_hu_A_1_1.html";
            }

            String a = "<a href=" + pageUrl + ">" + pageNumber + "</a>";
            li = "\n<li>" + a + "</li>\n";
            liS.add(li);
            if (index > this.maximumNumberOfPages && (pageNumber + 1) < pages) {
                pageUrl = createUrlLink(pageNumber + 1);
                a = "<a href=" + pageUrl + ">" + "Next" + "</a>";
                li = "\n<li>" + a + "</li>\n";
                liS.add(li);
                break;
            }

            index++;

        }

        return liS;
    }

    private String createUrlLink(Integer pageNumber) {
        return LOCALHOST_URL_LIST_OF_TERMS_PAGE + info.createFileNameUnicode(pageNumber);
    }

    private String generateTermUrl(String term, AlphabetTermPage alphabetTermPage) {
        return alphabetTermPage.getProps().getProperty(term);
    }

    public Map<String, String> getTermList() {
        return termAlterUrl;
    }

    private void assignCurrentPageNumber(Element divCurrentPage, Integer currentPageNumber) {
        divCurrentPage.append("<span>" + currentPageNumber + "</span> </li>");

    }

    private String assignHighLightPageNumber(String pageUrl) {
        String a = "<a href=" + pageUrl + ">" + "&gt;" + "</a>";
        return "\n<li>" + a + "</li>\n";

    }

}
