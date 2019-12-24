/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import static browser.termallod.core.api.HtmlConverter.HTML_EXTENSION;
import static browser.termallod.core.api.HtmlConverter.UNDERSCORE;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author elahi
 */
public class HtmlModify {

    private Document oldDocument;
    private final AlphabetTermPage alphabetTermPage;
    private final Document newDocument;
    private final String localhost;
    private final String DEFINITION;
    private final PageContentGenerator pageContentGenerator;
    private final String categoryName;

    public HtmlModify(Document oldDocument, String language, AlphabetTermPage alphabetTermPage, List<String> terms, String path, String localhost, String DEFINITION, String alphabetFileLocCons, PageContentGenerator pageContentGenerator) throws Exception {
        this.oldDocument = oldDocument;
        this.alphabetTermPage = alphabetTermPage;
        this.localhost = localhost;
        this.DEFINITION = DEFINITION;
        this.pageContentGenerator = pageContentGenerator;
        this.categoryName = alphabetFileLocCons;
        this.newDocument = this.changeBody(oldDocument, language, terms);
    }

    public Document changeBody(Document oldDocument, String language, List<String> terms) throws Exception {
        Element body = oldDocument.body();
        //String currentPageAlphabet = body.getElementsByClass("currentpage").get(0).getAllElements().get(1).text();
        modiyPage(body, language, terms);
        return oldDocument;
    }

    private void modiyPage(Element body, String language, List<String> terms) throws Exception {
        String alphebetPair = alphabetTermPage.getAlpahbetPair();
        Integer numberofPages = alphabetTermPage.getNumberOfPages();
        //currently not
        Integer emptyTerm = alphabetTermPage.getEmptyTerm();
        createAlphabet(body, language, alphebetPair);
        createTerms(body, terms, language, alphebetPair, emptyTerm);
        createUperPageNumber(body, language, alphebetPair, numberofPages);
        createLowerPageNumber(body, language, alphebetPair, numberofPages);

    }

    private void createLowerPageNumber(Element body, String language, String alphebetPair, Integer numberofPages) {
        Element divPageDown = body.getElementsByClass("paging_links inner_down").get(0);
        List<String> liS = getPageLi(language, alphebetPair, numberofPages);
        for (String li : liS) {
            divPageDown.append(li);
        }
    }

    private void createUperPageNumber(Element body, String language, String alphebetPair, Integer numberofPages) {
        Element divPageUper = body.getElementsByClass("paging_links inner").get(0);
        List<String> pageUperliS = getPageLi(language, alphebetPair, numberofPages);
        for (String li : pageUperliS) {
            divPageUper.append(li);
        }
    }

    private void createTerms(Element body, List<String> terms, String language, String alphebetPair, Integer emptyTerm) {
        Element divTerm = body.getElementsByClass("result-list1 wordlist-oxford3000 list-plain").get(0);
        for (String term : terms) {
            String liString = getTermLi(language, alphebetPair, term);
            divTerm.append(liString);
        }
        /*for (Integer index=0;index<emptyTerm;index++) {
            String liString = "";
            divTerm.append(liString);
        }*/

    }

    private String createAlphabet(Element body, String language, String alphebetPair) throws Exception {
        Element divAlphabet = body.getElementsByClass("currentpage").get(0);
        divAlphabet.append("<span>" + alphebetPair + "</span>");
        List<String> alphabetPairsExists = pageContentGenerator.getAlpahbetTermsExists(language);
        for (String pair : alphabetPairsExists) {
            if (!pair.contains(alphebetPair)) {
                String alphabetFileName = categoryName + UNDERSCORE + language + UNDERSCORE + pair + UNDERSCORE + "0" + HTML_EXTENSION;
                String li = getAlphebetLi(pair, alphabetFileName);
                divAlphabet.append(li);
            }

        }
        return alphebetPair;
    }

    private String getTermLi(String language, String alphebetPair, String term) {
        //<li><a href="https://www.oxfordlearnersdictionaries.com/definition/english/abandon_1" title="abandon definition">abandon</a> </li>
        String title = "title=" + '"' + term + " definition" + '"';
        //real version
        //String url = this.path+"/"+DEFINITION+"/" +language+"/" +alphebetPair +"/" +term + "_1";
        String url = localhost + "termDefination.php";
        String a = "<a href=" + url + " " + title + ">" + term + "</a>";
        String li = "\n<li>" + a + "</li>\n";
        return li;
    }

    private String getAlphebetLi(String alphabet, String alphabetFileName) {
        //Elements divAlphabet = body.getElementsByClass("side-selector__left");
        //Element content = body.getElementById("entries-selector");
        String url = localhost + alphabetFileName;
        String a = "<a href=" + url + ">" + alphabet + "</a>";
        String li = "\n<li>" + a + "</li>\n";
        return li;
    }

    private List<String> getPageLi(String language, String pair, Integer pages) {
        //Elements divAlphabet = body.getElementsByClass("side-selector__left");
        //Element content = body.getElementById("entries-selector");
        List<String> liS = new ArrayList<String>();
        String pageUrl = null;
        for (Integer page = 1; page <pages; page++) {
            pageUrl = localhost + categoryName + UNDERSCORE + language + UNDERSCORE + pair + UNDERSCORE + page + HTML_EXTENSION;
            System.out.println(pageUrl);
            String a = "<a href=" + pageUrl + ">" + page + "</a>";
            String li = "\n<li>" + a + "</li>\n";
            liS.add(li);
        }
        return liS;
    }

    public Document test2(String language, List<String> alphabets, List<String> terms, Document template) {
        String html = "<html><head><title>Sample Title</title></head>"
                + "<body>"
                + "<div id='sampleDiv'><a id='googleA' href='www.google.com'>Google</a></div>"
                + "</body></html>";
        Document document = Jsoup.parse(html);

        Element div = document.getElementById("sampleDiv");
        System.out.println("Outer HTML Before Modification :\n" + div.outerHtml());
        div.html("<p>This is a sample content.</p>");
        System.out.println("Outer HTML After Modification :\n" + div.outerHtml());
        div.prepend("<p>Initial Text</p>");
        System.out.println("After Prepend :\n" + div.outerHtml());
        div.append("<p>End Text</p>");
        System.out.println("After Append :\n" + div.outerHtml());
        return null;
    }

    /*public void test(Document doc) {
        Element div = doc.select("div").first(); // <div></div>
        div.html("<p>lorem ipsum</p>"); // <div><p>lorem ipsum</p></div>
        div.prepend("<p>First</p>");
        div.append("<p>Last</p>");
// now: <div><p>First</p><p>lorem ipsum</p><p>Last</p></div>

        Element span = doc.select("span").first(); // <span>One</span>
        span.wrap("<li><a href='http://example.com/'></a></li>");
// now: <li><a href="http://example.com"><span>One</span></a></li>

    }*/
    public Document getOldDocument() {
        return oldDocument;
    }

    public Document getNewDocument() {
        return newDocument;
    }

}
