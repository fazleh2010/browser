/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.api;

import browser.termallod.constants.FileAndLocationConst;
import browser.termallod.core.Browser;
import browser.termallod.core.SubjectFieldMerging;
import browser.termallod.core.html.HtmlParameters;
import browser.termallod.core.matching.MatchingTerminologies;
import browser.termallod.core.term.TermDetail;
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

    public  void readDataFromSavedFiles() throws IOException, Exception;

    public  void  readDataFromSavedFiles(String givenBrowser) throws IOException, Exception;

    public void createHtmlFromSavedFiles(FileAndLocationConst constants, Set<String> browserSet, Set<String> lang,HtmlParameters htmlCreateParameters) throws Exception, IOException;

    public void createHtmlFromSavedFiles(FileAndLocationConst constants, Set<String> browserSet, Set<String> lang, HtmlParameters htmlCreateParameters, SubjectFieldMerging merging) throws Exception, IOException;

    public void createIndexing() throws IOException, ParseException, Exception;

    public void createIndexing(String browser) throws IOException, ParseException, Exception;

    public List<String> search(String category, String langCode, String searchQuery) throws IOException, ParseException, Exception;

    public void createJavaScriptForAutoComp(String category) throws IOException, Exception;

    public void generateScript() throws IOException, Exception;

    public void generateScript(String category) throws IOException, Exception;

    public Set<TermDetail> matchTerminologies(String firstTerminology, String secondTerminology) throws IOException, Exception;

    //term page seperate creation...
    //public void createTermDetailHtmlPage(String browser,Set<String>givenLangs) throws IOException, Exception;
    
    public Map<String, Browser> getBrowsersInfor();
    
    //add decline page seperate creation..
    //public void createAddDeclineHtmlPage(String category,String lang,TermDetail termdetail,Set<String>givenLangs);
}
