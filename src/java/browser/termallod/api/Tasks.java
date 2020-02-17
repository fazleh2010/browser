/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.api;

import browser.termallod.core.input.Browser;
import browser.termallod.core.matching.MatchingTerminologies;
import browser.termallod.core.matching.TermDetail;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author elahi
 */
public interface Tasks {

    public void saveDataIntoFiles(Set<String> browserSet) throws Exception, IOException;

    public void saveDataIntoFiles(Set<String> browserSet, String browser) throws Exception, IOException;

    public void readDataFromSavedFiles() throws IOException, Exception;

    public void readDataFromSavedFiles(String givenBrowser) throws IOException, Exception;

    public void createHtmlFromSavedFiles(List<String> categorySet, String MODEL_EXTENSION, Set<String> browserSet, Set<String> lang) throws Exception, IOException;

    public void createIndexing() throws IOException, ParseException, Exception;

    public void createIndexing(String browser) throws IOException, ParseException, Exception;

    public List<String> search(String category, String langCode, String searchQuery) throws IOException, ParseException, Exception;

    public void prepareGroundForJs() throws IOException, Exception;

    public void generateScript() throws IOException, Exception;

    public void generateScript(String category) throws IOException, Exception;

    public Set<TermDetail> matchBrowsers() throws IOException, Exception;

    public void createTermDetailHtmlPage(String browser,Set<String>givenLangs) throws IOException, Exception;
    
    public Map<String, Browser> getBrowsersInfor();
    
    public void createAddDeclineHtmlPage(String category,String lang,TermDetail termdetail,Set<String>givenLangs);
}
