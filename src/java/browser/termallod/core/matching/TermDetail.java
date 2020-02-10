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
    private String langcode = null;
    private Set<String> links = new HashSet<String>();

    public TermDetail(String term, String langcode) {
        this.term = term;
        this.langcode = langcode;
    }

    TermDetail(String term, Set<String> urls) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getLangcode() {
        return langcode;
    }

    public String getTerm() {
        return term;
    }

}
