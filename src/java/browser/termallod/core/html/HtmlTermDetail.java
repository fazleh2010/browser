/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.html;

import browser.termallod.core.termbase.TermDetail;
import org.jsoup.nodes.Document;

/**
 *
 * @author elahi
 */
public class HtmlTermDetail {
     private Document outputHtml;
      private TermDetail termDetail;

    public HtmlTermDetail(TermDetail termDetail, Document templateHtml) {
        this.termDetail=termDetail;
    }

    public Document getOutputHtml() {
        return outputHtml;
    }
    
}
