/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.term;

import browser.termallod.core.SubjectInfo;
import browser.termallod.utils.StringMatcherUtil;
import com.hp.hpl.jena.graph.Triple;

/**
 *
 * @author elahi
 */
public class TermInfo {

    private String termString = "";
    private String termUrl = "";
    private SubjectInfo subject = new SubjectInfo();
    private String reliabilityCode = "";
    private String administrativeStatus = "";

    public TermInfo(Triple triple) {
        termString = triple.getObject().getLiteralLexicalForm().toLowerCase().trim();
        termString = StringMatcherUtil.encripted(termString);
        termUrl = triple.getSubject().toString();
        //termUrl =termUrl.replace(conts, "");
        try {
            //termUrl = termUrl.substring(0, termUrl.lastIndexOf('#'));
            termUrl = StringMatcherUtil.modifyUrl(termUrl);
        } catch (Exception ie) {
            termUrl = triple.getSubject().toString();
        }

    }

    public TermInfo(Object term, Object url) {
        this.termString = term.toString();
        this.termUrl = url.toString();
    }

    /*public TermInfo(String term, String url,String alternativeUrl) {
        this.termString = term;
        this.termUrl = url;
    }*/
    public TermInfo(Object term, Object url, SubjectInfo subject) {
        this(term, url);
        this.subject = subject;
    }

    public TermInfo(Object term, Object url, Object reliabilityCode, Object administrativeStatus, SubjectInfo subjectInfo) {
        this(term, url, subjectInfo);
        if (reliabilityCode != null) {
            this.reliabilityCode = reliabilityCode.toString();
        }
        if (administrativeStatus != null) {
            this.administrativeStatus = administrativeStatus.toString();
        }

    }

    public String getTermString() {
        return termString;
    }

    public String getTermUrl() {
        return termUrl;
    }

    public String getSubjectDescription() {
        return this.subject.getSubjectDescription();
    }

    public String getReliabilityCode() {
        return reliabilityCode;
    }

    public String getAdministrativeStatus() {
        return administrativeStatus;
    }

    public String getTermID() {
        return this.subject.getReferenceID();
    }

    public String getSubjectId() {
        return this.subject.getSubjectId();
    }

}
