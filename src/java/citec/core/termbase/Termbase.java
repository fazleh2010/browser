/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.core.termbase;

import citec.core.utils.FileUrlUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author elahi
 */
public class Termbase {

    private final String termbaseName;
    private Map<String, TermInfo> terms = new HashMap<String, TermInfo>();

    public Termbase(String termbaseName, Map<String, TermInfo> terms) throws IOException {
        this.termbaseName = termbaseName;
        this.terms =terms;
    }

    public Map<String, TermInfo> getTerms() {
        return terms;
    }

    public String getTermbaseName() {
        return termbaseName;
    }

    
    public void display() {
        for(String term:terms.keySet()){
            TermInfo terminfo=terms.get(term);
            System.out.println(terminfo);
            
        }
    }

}
