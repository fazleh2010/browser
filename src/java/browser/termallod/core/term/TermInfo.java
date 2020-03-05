/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.term;

import browser.termallod.utils.StringMatcherUtil;
import com.hp.hpl.jena.graph.Triple;

/**
 *
 * @author elahi
 */
public class TermInfo {

    private String termString = null;
    private String termUrl = null;

    public TermInfo(Triple triple) {
        termString = triple.getObject().getLiteralLexicalForm().toLowerCase().trim();
        termString = StringMatcherUtil.encripted(termString);
        termUrl = triple.getSubject().toString();
        try {
            termUrl = termUrl.substring(0, termUrl.lastIndexOf('#'));
        } catch (Exception ie) {
            termUrl = triple.getSubject().toString();
        }

    }

    public TermInfo(String term, String url) {
        this.termString = term;
        this.termUrl = url;
    }
    public TermInfo(String term, String url,String alternativeUrl) {
        this.termString = term;
        this.termUrl = url;
    }

    public String getTermString() {
        return termString;
    }

    /*public String getTermEncripted() {
        return this.encripted(term);
    }
    public String getTermDecripted() {
        return this.decripted(term);
    }*/
    public String getTermUrl() {
        return termUrl;
    }

   

}
