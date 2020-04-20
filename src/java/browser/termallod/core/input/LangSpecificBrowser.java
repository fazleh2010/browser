/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.input;

import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.store.Directory;

/**
 *
 * @author elahi
 */
public class LangSpecificBrowser {

    private Map<String, String> termUrls = new HashMap<String, String>();
    private String langCode = null;
    private Directory index;

    public LangSpecificBrowser(String langCode, Map<String, String> termUrls) {
        this.langCode = langCode;
        this.termUrls = termUrls;
    }

    public void setIndex(Directory index) {
        this.index=index;
    }

    public Map<String, String> getTermUrls() {
        return termUrls;
    }
    
    public String getTermUrls(String term) {
        return termUrls.get(term);
    }

    public String getLangCode() {
        return langCode;
    }

    public Directory getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "LangSpecificBrowser{" + "termUrls=" + termUrls + ", langCode=" + langCode + ", index=" + index + '}';
    }

    

}
