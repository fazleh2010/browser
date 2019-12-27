/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.constants;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author elahi
 */
public interface FilePathAndConstant {

    public static String PATH = "src/java/browser/termallod/data/";
    public static String dataPath = "data";
    public static String TEMPLATE_LOCATION = "src/java/browser/termallod/template/";
    public static File MAIN_PAGE_TEMPLATE = new File(TEMPLATE_LOCATION + "listOfTermsFinal.html");
    public static File TERM_PAGE_TEMPLATE = new File(TEMPLATE_LOCATION + "termDefination.html");
    public static File configFile = new File(PATH + "conf/" + "language.conf");
    public static String GENTERM_PATH = PATH + "genterm/";
    public static String NTRIPLE_EXTENSION = ".ntriple";
    public static String TURTLE_EXTENSION = ".ttl";
    public static String N_TRIPLE = "N-TRIPLE";
    public static String LIST_OF_TERMS_PAGE_LOCATION = PATH + "html/";
    public static String LOCALHOST_URL_LIST_OF_TERMS_PAGE = "http://localhost/";
    //public static String LOCALHOST_URL_LIST_OF_TERMS_PAGE = "https://webtentacle1.techfak.uni-bielefeld.de/";
    public static String HTML_EXTENSION = ".html";
    public static Integer INITIAL_PAGE = 1;
    public String UNDERSCORE = "_";

    public String solar = "solar";
    public String ate = "ate";
    public String diseases = "diseases";
    public String intaglio = "intaglio";
    public String wastemanagement = "wastemanagement";
    public Map<String, String> categoryOntologyMapper = new HashMap<String, String>() {
        {
            put(ate, "tbx2rdf_atc");
            put(solar, "tbx2rdf_solarenergy");
            put(intaglio, "tbx2rdf_intaglio");
            put(wastemanagement, "tbx2rdf_wastemanagement");
            put(diseases, "tbx2rdf_diseases");
        }
    };

    public Map<String, String> languageMapper = new HashMap<String, String>() {
        {
            put("en", "English");
            put("nl", "Dutch");
        }
    };

}
