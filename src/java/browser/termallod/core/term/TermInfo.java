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
    private String termID = null;
    private String subjectId = null;
    private String conts = "http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_iate/data/iate/";

    public TermInfo(Triple triple) {
        termString = triple.getObject().getLiteralLexicalForm().toLowerCase().trim();
        termString = StringMatcherUtil.encripted(termString);
        termUrl = triple.getSubject().toString();
        //termUrl =termUrl.replace(conts, "");
        try {
            //termUrl = termUrl.substring(0, termUrl.lastIndexOf('#'));
        } catch (Exception ie) {
            termUrl = triple.getSubject().toString();
        }

    }

    public TermInfo(String term, String url) {
        this.termString = term;
        this.termUrl = url;
    }

    /*public TermInfo(String term, String url,String alternativeUrl) {
        this.termString = term;
        this.termUrl = url;
    }*/

    public TermInfo(String termID, String subjectID, String url) {
        this.termID = termID;
        this.subjectId = subjectID;
        this.termUrl = url;
    }
    
    public TermInfo(String term,String termID, String subjectID, String url) {
        this.termID = termID;
        this.subjectId = subjectID;
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

    @Override
    public String toString() {
        return "TermInfo{" + "termString=" + termString + ", termUrl=" + termUrl + ", termID=" + termID + ", subjectId=" + subjectId + '}';
    }

    public String getTermID() {
        return termID;
    }

    public String getSubjectId() {
        return subjectId;
    }

}
