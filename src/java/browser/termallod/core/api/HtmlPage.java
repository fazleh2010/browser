/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.api;

import browser.termallod.core.AlphabetTermPage;
import browser.termallod.core.PageContentGenerator;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.jsoup.nodes.Element;

/**
 *
 * @author elahi
 */
public interface HtmlPage {

    public static String PATH = "src/java/browser/termallod/data/";
    public static String LIST_OF_TERMS_PAGE_LOCATION = PATH + "html/";
    public static String TERM_DEFINATION_LOCATION = LIST_OF_TERMS_PAGE_LOCATION + "definition";
    public static String LOCALHOST_URL_LIST_OF_TERMS_PAGE = "http://localhost/";
    //public static String LOCALHOST_URL_LIST_OF_TERMS_PAGE = "https://webtentacle1.techfak.uni-bielefeld.de/";
    
    public static String HTML_EXTENSION = ".html";
    public static Integer INITIAL_PAGE = 1;
    public String UNDERSCORE = "_";
    public Map<String, String> languageMapper = new HashMap<String, String>() {
        {
            put("en", "English");
            put("nl", "Dutch");
        }
    };
    

    public String createAlphabet(Element body, String alphebetPair, PageContentGenerator pageContentGenerator) throws Exception;

    public void createLangSelectBox(Element body, PageContentGenerator pageContentGenerator) throws Exception;

    public void createUperPageNumber(Element body, String alphebetPair, Integer numberofPages);

    public void createLowerPageNumber(Element body, String alphebetPair, Integer numberofPages);

    public void createTerms(Element body, List<String> terms, String alphebetPair, Integer emptyTerm,AlphabetTermPage alphabetTermPage);

}
