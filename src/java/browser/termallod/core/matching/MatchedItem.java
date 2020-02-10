/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.matching;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author elahi
 */
public class MatchedItem extends TermDetail{
    

    private Map<String, String> categoryTerm = new HashMap<String, String>();
    private Map<String, String> categorySparql = new HashMap<String, String>();
    
    public MatchedItem(String langCode, String term){
        super(langCode,term);
        
    }

    public void setCategoryTerm(String category, String url) {
        this.categoryTerm.put(category, url);
    }

    public void setCategorySparql(String term, String sparql) {
        this.categorySparql.put(term, sparql);
    }

}
