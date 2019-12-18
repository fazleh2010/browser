/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tbx2rdf.utils.virtuoso;

import tbx2rdf.utils.virtuoso.datamanager.DataManager;
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
    private DataManager dataManager;
    private Document newDocument;

    public HtmlModify(Document oldDocument, String language, DataManager dataManager, List<String> terms) {
        this.oldDocument = oldDocument;
        this.dataManager = dataManager;
        this.newDocument = this.changeBody(oldDocument, language, terms);
    }

    public Document changeBody(Document oldDocument, String language, List<String> terms) {
        Element body = oldDocument.body();
        //String currentPageAlphabet = body.getElementsByClass("currentpage").get(0).getAllElements().get(1).text();
        modiyCurrentPage(body, language, terms);
        return oldDocument;
    }

    private void modiyCurrentPage(Element body, String language, List<String> terms) {
        Element divAlphabet = body.getElementsByClass("currentpage").get(0);
        divAlphabet.append("<span>" + dataManager.getAlphabets(language)[0] + "</span>");
        for (Integer index = 1; index < dataManager.getAlphabets(language).length; index++) {
            String alphabet = dataManager.getAlphabets(language)[index];
            String li = getAlphebet(alphabet);
            divAlphabet.append(li);
        }

        Element divTerm = body.getElementsByClass("result-list1 wordlist-oxford3000 list-plain").get(0);
        for (String term : terms) {
            String liString = getTerm(term);
            divTerm.append(liString);
        }

    }

    private String getTerm(String term) {
        //<li><a href="https://www.oxfordlearnersdictionaries.com/definition/english/abandon_1" title="abandon definition">abandon</a> </li>
        String title = "title=" + '"' + term + " definition" + '"';
        String url = "https://www.oxfordlearnersdictionaries.com/definition/english/" + term + "_1";
        String a = "<a href=" + url + " " + title + ">" + term + "</a>";
        String li = "\n<li>" + a + "</li>\n";
        return li;
    }

    private String getAlphebet(String alphabet) {
        //Elements divAlphabet = body.getElementsByClass("side-selector__left");
        //Element content = body.getElementById("entries-selector");
        String url = "https://www.oxfordlearnersdictionaries.com/wordlist/english/oxford3000/Oxford3000_" + alphabet + "/";
        String a = "<a href=" + url + ">" + alphabet + "</a>";
        String li = "\n<li>" + a + "</li>\n";
        return li;
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
