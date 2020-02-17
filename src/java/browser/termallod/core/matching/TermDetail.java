/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.matching;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class TermDetail {

    private String term = null;
    private String category = null;
    private String langCode = null;
    private Set<String> links = new HashSet<String>();

    public TermDetail(String category,String langCode, String term, String givenUrl, String url) {
        this.category=category;
        this.langCode = langCode;
        this.term = term;
        this.links.add(givenUrl);
        this.links.add(url);
    }

    public TermDetail(String category,String langCode, String term, String url) {
        this.langCode = langCode;
        this.term = term.replace("_", " ");
        this.links.add(url);
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

    @Override
    public String toString() {
        return "TermDetail{" + "term=" + term + ", langCode=" + langCode + ", links=" + links + '}';
    }

}
