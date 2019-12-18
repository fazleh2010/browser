/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod;

import browser.termallod.datamanager.DataManager;
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
    private DataManager dataManager;
    private Document newDocument;
    private String path;
    private String DEFINITION = "definition";
    private String localhost = "http://localhost/";

    public HtmlModify(Document oldDocument, String language, DataManager dataManager, List<String> terms, String path) {
        this.oldDocument = oldDocument;
        this.dataManager = dataManager;
        this.path = path;
        this.newDocument = this.changeBody(oldDocument, language, terms);
    }

    public Document changeBody(Document oldDocument, String language, List<String> terms) {
        Element body = oldDocument.body();
        //String currentPageAlphabet = body.getElementsByClass("currentpage").get(0).getAllElements().get(1).text();
        modiyCurrentPage(body, language, terms);
        return oldDocument;
    }

    private void modiyCurrentPage(Element body, String language, List<String> terms) {
        String alphebetPair = dataManager.getAlphabets(language)[0];
        Element divAlphabet = body.getElementsByClass("currentpage").get(0);
        divAlphabet.append("<span>" + dataManager.getAlphabets(language)[0] + "</span>");
        for (Integer index = 1; index < dataManager.getAlphabets(language).length; index++) {
            String alphabet = dataManager.getAlphabets(language)[index];
            String li = getAlphebetLi(alphabet);
            divAlphabet.append(li);
        }

        Element divTerm = body.getElementsByClass("result-list1 wordlist-oxford3000 list-plain").get(0);
        for (String term : terms) {
            String liString = getTermLi(language, alphebetPair, term);
            divTerm.append(liString);
        }
        
        Element divPageUper = body.getElementsByClass("paging_links inner").get(0);
        List<String> pageUperliS = getPageLi(alphebetPair,4);
        for (String li : pageUperliS) {
            divPageUper.append(li);
        }
        
        Element divPageDown = body.getElementsByClass("paging_links inner_down").get(0);
        List<String> liS = getPageLi(alphebetPair,4);
        for (String li : liS) {
            divPageDown.append(li);
        }

    }

    private String getTermLi(String language, String alphebetPair, String term) {
        //<li><a href="https://www.oxfordlearnersdictionaries.com/definition/english/abandon_1" title="abandon definition">abandon</a> </li>
        String title = "title=" + '"' + term + " definition" + '"';
        //real version
        //String url = this.path+"/"+DEFINITION+"/" +language+"/" +alphebetPair +"/" +term + "_1";
        String url = localhost+"project"+"/"+DEFINITION+"/"+"termDefination.php";
        String a = "<a href=" + url + " " + title + ">" + term + "</a>";
        String li = "\n<li>" + a + "</li>\n";
        return li;
    }

    private String getAlphebetLi(String alphabet) {
        //Elements divAlphabet = body.getElementsByClass("side-selector__left");
        //Element content = body.getElementById("entries-selector");
        String url = localhost+"project"+"/"+DEFINITION+"/"+alphabet + "_"+"example.php";
        String a = "<a href=" + url + ">" + alphabet + "</a>";
        String li = "\n<li>" + a + "</li>\n";
        return li;
    }
    
    private List<String> getPageLi(String alphabet,Integer pages) {
        //Elements divAlphabet = body.getElementsByClass("side-selector__left");
        //Element content = body.getElementById("entries-selector");
        List<String> liS=new ArrayList<String>();
        for(Integer page=2;page<=pages;page++){
        String url = localhost+"project"+"/"+DEFINITION+"/"+alphabet + "_"+page+ "_"+"example.php";
        String a = "<a href=" + url + ">" + page + "</a>";
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
