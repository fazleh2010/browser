/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.input;

import java.util.Map;
import java.util.TreeMap;
import org.apache.lucene.store.Directory;

/**
 *
 * @author elahi
 */
public class Browser {

    private final String browser;
    private final String category;
    private Map<String, LangSpecificBrowser> langTermUrls = new TreeMap<String, LangSpecificBrowser>();

    public Browser(String browser, String category) {
        this.browser = browser;
        this.category = category;
    }

    public Browser(Browser browserInfo, Map<String, LangSpecificBrowser> langTermUrls) {
        this.browser = browserInfo.getBrowser();
        this.category = browserInfo.getCategory();
        this.langTermUrls = browserInfo.getLangTermUrls();
    }
   
    public void setLangTermUrls(String langCode, Map<String, String> termUrls) {
        langTermUrls.put(langCode, new LangSpecificBrowser(langCode, termUrls));

    }

    public void setIndex(String langCode, Directory index) {
        LangSpecificBrowser modifyLangBrowser = langTermUrls.get(langCode);
        modifyLangBrowser.setIndex(index);
        langTermUrls.put(browser, modifyLangBrowser);

    }

    public String getBrowser() {
        return browser;
    }

    public String getCategory() {
        return category;
    }

    public Map<String, LangSpecificBrowser> getLangTermUrls() {
        return langTermUrls;
    }

    public LangSpecificBrowser getLangTermUrls(String langCode) {
        return langTermUrls.get(langCode);
    }
}
