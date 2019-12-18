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
import org.jsoup.select.Elements;
import static tbx2rdf.utils.virtuoso.Constants.eng;
import tbx2rdf.utils.virtuoso.api.PopulateListofTerms;

/**
 *
 * @author elahi
 */
public class HtmlModify_1 {

    private Document oldDocument;
    private DataManager dataManager;
    private Document newDocument;

    public HtmlModify_1(Document oldDocument, String language, DataManager dataManager) {
        this.oldDocument = oldDocument;
        this.dataManager = dataManager;
        this.newDocument = this.changeBody(oldDocument, language);
    }

    public Document changeBody(Document oldDocument, String language) {
        Element body = oldDocument.body();
     
        //String currentPageAlphabet = body.getElementsByClass("currentpage").get(0).getAllElements().get(1).text();
        modiyCurrentPage(body, language);
        return oldDocument;
    }

    private void modiyCurrentPage(Element body, String language) {
        
        String span="<span>" + dataManager.getAlphabets(language)[0] + "</span>";
        String curentPageLi="<li class="+'"'+"currentpage"+'"'+span+"</li>";
        Element div = body.getElementById("entries-selector");
        //Element div = body.getElementsByClass("currentpage").get(0);
       
        div.append(curentPageLi);
        String url="https://www.oxfordlearnersdictionaries.com/wordlist/english/oxford3000/Oxford3000_"+ dataManager.getAlphabets(language)[0]+"/";
        String a="<a href="+url +">"+ dataManager.getAlphabets(language)[0]+"</a>";
        String li="\n<li>"+a +"</li>\n";
        div.append(li);
        System.out.println(div);
        //String li=modiyAlphebetList(body,language);
        //div.append(li);
    }

    private String modiyAlphebetList(Element body, String language) {
        //Elements div = body.getElementsByClass("side-selector__left");
        Element content = body.getElementById("entries-selector");
        String url="https://www.oxfordlearnersdictionaries.com/wordlist/english/oxford3000/Oxford3000_"+ dataManager.getAlphabets(language)[0]+"/";
        String a="<a href="+url +">"+ dataManager.getAlphabets(language)[0]+"</a>";
        String li="\n<li>"+a +"</li>\n";
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

    public void test(Document doc) {
        Element div = doc.select("div").first(); // <div></div>
        div.html("<p>lorem ipsum</p>"); // <div><p>lorem ipsum</p></div>
        div.prepend("<p>First</p>");
        div.append("<p>Last</p>");
// now: <div><p>First</p><p>lorem ipsum</p><p>Last</p></div>

        Element span = doc.select("span").first(); // <span>One</span>
        span.wrap("<li><a href='http://example.com/'></a></li>");
// now: <li><a href="http://example.com"><span>One</span></a></li>

    }

    public Document getOldDocument() {
        return oldDocument;
    }

    public Document getNewDocument() {
        return newDocument;
    }

}
