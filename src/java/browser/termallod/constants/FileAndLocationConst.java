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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class FileAndLocationConst {

    private  String BASE_PATH;
    private  String GENTERM_PATH;
    private  File LANGUAGE_CONFIG_FILE;

    public String getGENTERM_PATH() {
        return GENTERM_PATH;
    }

    public File getLANGUAGE_CONFIG_FILE() {
        return LANGUAGE_CONFIG_FILE;
    }

    public String getBASE_PATH() {
        return BASE_PATH;
    }

    public FileAndLocationConst(String BASE_PATH) {
        this.BASE_PATH = BASE_PATH;
        this.GENTERM_PATH=BASE_PATH+File.separator;
        this.LANGUAGE_CONFIG_FILE = new File(BASE_PATH+"conf/" +"language.conf");
    }
    public static String DATA_PATH = "data/";
    public static String TEXT_PATH = "txt/";
    public static String RDF_PATH = "rdf/";
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
    public Set<String> BROWSER_GROUPS = new HashSet<String>(Arrays.asList(GENTERM, IATE));
    public  String IATE_URL = "http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_iate/data/iate/";

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
    
    public String URL="http://webtentacle1.techfak.uni-bielefeld.de/";
    public Map<String, String> CATEGORY_TERM_URL = new HashMap<String, String>() {
        {
            put(ATC, URL+"tbx2rdf_atc/data/");
            put(SOLAR, URL+"tbx2rdf_solar/data/");
            put(INTAGLIO, URL+"tbx2rdf_intaglio/data/");
            put(WASTEMANAGEMENT, URL+"tbx2rdf_wastemanagement/data/");
            put(DISEASES, URL+"tbx2rdf_diseases/data/");
            put(IATE, URL+"tbx2rdf_iate/data/");
        }
    };
    

    public Map<String, List<String>> BROWSER_CATEGORIES = new HashMap<String, List<String>>() {
        {
            put(GENTERM, new ArrayList<String>(Arrays.asList(ATC, SOLAR, INTAGLIO, WASTEMANAGEMENT, DISEASES)));
            put(IATE, new ArrayList<String>(Arrays.asList(IATE)));
        }
    };
    
    public Map<String, String> BROWSER_URL = new HashMap<String,String>() {
        {
            put(GENTERM,"http://www.cvt.ugent.be/genterm.htm");
            put(IATE,"https://iate.europa.eu/");
        }
    };
    
   

}
