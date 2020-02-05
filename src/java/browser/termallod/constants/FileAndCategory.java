/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elahi
 */
public interface FileAndCategory {

    public static String BASE_PATH = "src/java/resources/data/";
    public static String DATA_PATH = "data/";
    public static String TEXT_PATH = "text/";
    public static String RDF_PATH = "rdf/";
    public static String GENTERM_PATH=BASE_PATH+File.separator;
    public static File LANGUAGE_CONFIG_FILE = new File(BASE_PATH+"conf/" +"language.conf");
    public static String NTRIPLE_EXTENSION = ".ntriple";
    public static String TURTLE_EXTENSION = ".ttl";
    public static String JAVA_SCHRIPT_EXTENSION = ".js";
    public static String TEXT_EXTENSION = ".txt";
    public static String N_TRIPLE = "N-TRIPLE";
    public static String TURTLE = "TURTLE";
    public static String TEXT = "TEXT";
    //public static String LOCALHOST_URL_LIST_OF_TERMS_PAGE = "http://localhost/";
    public static String LOCALHOST_URL_LIST_OF_TERMS_PAGE = "";
     public static String TEMPLATE_LOCATION = "src/java/resources/atemplate/";
    public static File TERM_PAGE_TEMPLATE = new File(TEMPLATE_LOCATION + "termDefination.html");
    public static String AUTO_COMPLETION_TEMPLATE_LOCATION = TEMPLATE_LOCATION +"css/";


    public String SOLAR = "solar";
    public String ATC = "atc";
    public String DISEASES = "diseases";
    public String INTAGLIO = "intaglio";
    public String WASTEMANAGEMENT = "wastemanagement";
    public String IATE = "iate";
    public String GENTERM = "genterm";
    public List<String> BROWSER_GROUPS = new ArrayList<String>(Arrays.asList(GENTERM, IATE));

    public Map<String, String> CATEGORY_ONTOLOGIES = new HashMap<String, String>() {
        {
            put(ATC, "tbx2rdf_atc");
            put(SOLAR, "tbx2rdf_solar");
            put(INTAGLIO, "tbx2rdf_intaglio");
            put(WASTEMANAGEMENT, "tbx2rdf_wastemanagement");
            put(DISEASES, "tbx2rdf_diseases");
            put(IATE, "tbx2rdf_iate");
        }
    };

    public Map<String, List<String>> BROWSER_CATEGORIES = new HashMap<String, List<String>>() {
        {
            put(GENTERM, new ArrayList<String>(Arrays.asList(ATC, SOLAR, INTAGLIO, WASTEMANAGEMENT, DISEASES)));
            put(IATE, new ArrayList<String>(Arrays.asList(IATE)));
        }
    };

}
