/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.constants;

import browser.termallod.core.AlphabetTermPage;
import browser.termallod.core.PageContentGenerator;
import browser.termallod.core.term.TermDetail;
import java.io.File;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author elahi
 */
public interface HtmlPage {
    
    public static String browser="browser";
    public static String HTML_EXTENSION = ".html";
    public String UNDERSCORE = "_";
    public static String LOCALHOST_URL_LIST_OF_TERMS_PAGE = "";
    public static Integer INITIAL_PAGE = 1;
    public void createTerms(Element body, List<TermDetail> terms, String alphebetPair, Integer emptyTerm,File htmlFileName)throws Exception;

}
