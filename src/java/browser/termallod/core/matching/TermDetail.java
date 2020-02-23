/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.matching;

import static browser.termallod.constants.FileAndCategory.IATE;
import browser.termallod.utils.StringMatcherUtil;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class TermDetail {

    private String term = null;
    private String termModified = null;
    private String url = null;
    private String alternativeUrl = null;
    private String category = null;
    private String langCode = null;
    private Set<String> links = new HashSet<String>();
    private Boolean alternativeFlag;

   /* public TermDetail(String category,String langCode, String term, String givenUrl, String url) {
        this.category=category;
        this.langCode = langCode;
        this.term = term;
        this.links.add(givenUrl);
        this.links.add(url);
    }*/
    public void setAlternativeUrl(String alternativeUrl) {
        this.alternativeUrl = alternativeUrl;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public TermDetail(String category, String langCode, String term, String url) {
        this.langCode = langCode;
        this.term = term;
        this.termModified = StringMatcherUtil.decripted(term);
        //term.replace("_", " ");;
        this.links.add(url);
    }

    public TermDetail(String category,String langCode, String term) {
        this.langCode = langCode;
        this.term = term;
        this.termModified = StringMatcherUtil.decripted(term);
        this.category = category;
    }
    
    public TermDetail(TermDetail termDetail,String url,String alternativeUrl) {
        this.category=termDetail.getCategory();
        this.langCode = termDetail.getLangCode();
        this.term = termDetail.getTerm();
        this.termModified = StringMatcherUtil.decripted(term);
        this.url = url;
        this.alternativeUrl = alternativeUrl;
    }

    public String getTerm() {
        return term;
    }

    public String getLangCode() {
        return langCode;
    }

    public Set<String> getLinks() {
        return links;
    }

    public String getCategory() {
        return category;
    }

    public String getUrl() {
        return url;
    }

    public String getAlternativeUrl() {
        return alternativeUrl;
    }

    @Override
    public String toString() {
        return "TermDetail{" + "term=" + term + ", langCode=" + langCode + ", links=" + links + '}';
    }

    public String getTermModified() {
        return termModified;
    }

}
