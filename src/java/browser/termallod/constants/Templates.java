/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.constants;

import java.io.File;

/**
 *
 * @author elahi
 */
public interface Templates {

    public static String TEMPLATE_LOCATION = "src/java/browser/termallod/template/";
    public static String MAIN_PAGE_TEMPLATE_IATE = "iateTemplate.html";
    public static String MAIN_PAGE_TEMPLATE_GENTERM_EN = "gentermTemplateEn.html";
    public static String MAIN_PAGE_TEMPLATE_GENTERM_NL = "gentermTemplateNl.html";
    public static File TERM_PAGE_TEMPLATE = new File(TEMPLATE_LOCATION + "termDefination.html");
    public static File AUTO_COMPLETION_TEMPLATE = new File(TEMPLATE_LOCATION + "autocompletionTemplate.js");

}
