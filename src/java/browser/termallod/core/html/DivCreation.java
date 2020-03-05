/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.html;

import static browser.termallod.constants.Languages.languageMapper;
import browser.termallod.core.term.TermDetail;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author elahi
 */
public class DivCreation {
    private String language=null;
    
    
    public DivCreation( String language){
        this.language=language;
        
    }
     private Document createTermPage(Document templateHtml, TermDetail termDetail) throws Exception {

        String langDetail = languageMapper.get(language);
        Element body = templateHtml.body();
        Element divTerm = body.getElementsByClass("webtop-g").get(0);
        String term=termDetail.getTerm();
        //<a class="academic" href="https://www.oxfordlearnersdictionaries.com/wordlist/english/academic/">
        String classStr = "<a class=" + "\"" + "academic" + "\"" + " href=" + "\"" + "https://www.oxfordlearnersdictionaries.com/wordlist/english/academic/" + "\"" + ">";
        //</a><span class="z"> </span>
        String spanStr = "</a><span class=" + "\"" + "z" + "\"" + "> </span>";
        //<h2 class="h">abandon</h2>
        String wordStr = "<h2 class=" + "\"" + "h" + "\"" + ">" + term + "</h2>";
        //<span class="z"> </span>
        String extraStr = "<span class=" + "\"" + "z" + "\"" + ">" + "</span>";

        String str = classStr + spanStr + wordStr + extraStr ; //language;//+titleStr+langStr;
        divTerm.append(str);

        Element divLang = body.getElementsByClass("top-g").get(0);
        String langDiv = "<span class=" + "\"" + "collapse" + "\"" + " title=" + "\"" + langDetail + "\"" + ">";
        langDiv += "<span class=" + "\"" + "heading" + "\"" + ">" + langDetail + "</span></span>";
        divLang.append(langDiv);

      
        //temporary closed ..it will be added later..
        /*Element multiLingualDiv = body.getElementsByClass("entry").get(0);
        String title = "title=" + '"' + "term" + " definition" + '"';
        String url = "http: term url";
        String a = "<a href=" + url + " " + title + ">" + term + "</a>";
        String li = "\n<li>" + a + "</li>\n";
        multiLingualDiv.append(li);*/

        //System.out.println(multiLingualDiv.toString());
        return templateHtml;
    }

    
}
