/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.api;

import browser.termallod.core.AlphabetTermPage;
import browser.termallod.core.PageContentGenerator;
import java.io.File;

/**
 *
 * @author elahi
 */
public interface HtmlConverter {

    public static String PATH = "src/java/browser/termallod/";
    public static String LIST_OF_TERMS_PAGE_LOCATION = PATH + "html/";
    public static String TERM_DEFINATION_LOCATION = LIST_OF_TERMS_PAGE_LOCATION+"definition";
    public static String LOCALHOST_URL = "http://localhost/";
    public static String HTML_EXTENSION =".html";
    public String UNDERSCORE = "_";

    public void convertToHtml(String PATH, String categoryName, File templateFile, String language, AlphabetTermPage alphabetTermPage, PageContentGenerator pageContentGenerator) throws Exception;

}
