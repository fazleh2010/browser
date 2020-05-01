/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.constants;

import static browser.termallod.app.Main.constants;
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

    private final String BASE_PATH;
    private final String OUTPUT_PATH;
    private final String INPUT_PATH;
    private String GENTERM_PATH;
    private File LANGUAGE_CONFIG_FILE;


    public FileAndLocationConst(String BASE_PATH,String INPUT_PATH,String OUTPUT_PATH) {
        this.BASE_PATH = BASE_PATH;
        this.INPUT_PATH=INPUT_PATH;
        this.OUTPUT_PATH=OUTPUT_PATH;
        this.GENTERM_PATH = BASE_PATH + File.separator;
        this.LANGUAGE_CONFIG_FILE = new File(BASE_PATH + "conf/" + "language.conf");
    }
    
    public String getOUTPUT_PATH(String category) {
        return OUTPUT_PATH+CATEGORY_ONTOLOGIES.get(category)+File.separator;
    }
    public String getOUTPUT_PATH() {
        return OUTPUT_PATH;
    }

    public String getINPUT_PATH() {
        return INPUT_PATH;
    }
    public String getINPUT_RDF_PATH(String category) {
        return INPUT_PATH +category+File.separator+ RDF_PATH;
    }
     public String getINPUT_TXT_PATH(String category) {
        return INPUT_PATH +category+File.separator+ constants.TEXT_PATH;
    }

    public String getGENTERM_PATH() {
        return GENTERM_PATH;
    }

    public File getLANGUAGE_CONFIG_FILE() {
        return LANGUAGE_CONFIG_FILE;
    }

    public String getBASE_PATH() {
        return BASE_PATH;
    }
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
    public static String AUTO_COMPLETION_TEMPLATE_LOCATION = TEMPLATE_LOCATION + "css/";

    public String SOLAR = "solar";
    public String ATC = "atc";
    public String DISEASES = "diseases";
    public String INTAGLIO = "intaglio";
    public String WASTEMANAGEMENT = "wastemanagement";
    public String IATE = "iate";
    public String GENTERM = "genterm";
    public Set<String> BROWSER_GROUPS = new HashSet<String>(Arrays.asList(GENTERM, IATE));
    public String SERVER_URL = "http://webtentacle1.techfak.uni-bielefeld.de/";

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

    
    public Map<String, String> CATEGORY_TERM_URL = new HashMap<String, String>() {
        {
            put(ATC, SERVER_URL + "tbx2rdf_atc/data/");
            put(SOLAR, SERVER_URL + "tbx2rdf_solar/data/");
            put(INTAGLIO, SERVER_URL + "tbx2rdf_intaglio/data/");
            put(WASTEMANAGEMENT, SERVER_URL + "tbx2rdf_wastemanagement/data/");
            put(DISEASES, SERVER_URL + "tbx2rdf_diseases/data/");
            put(IATE, SERVER_URL + "tbx2rdf_iate/data/");
        }
    };

    public Map<String, List<String>> BROWSER_CATEGORIES = new HashMap<String, List<String>>() {
        {
            put(GENTERM, new ArrayList<String>(Arrays.asList(ATC, SOLAR, INTAGLIO, WASTEMANAGEMENT, DISEASES)));
            put(IATE, new ArrayList<String>(Arrays.asList(IATE)));
        }
    };

    public Map<String, String> BROWSER_URL = new HashMap<String, String>() {
        {
            put(GENTERM, "http://www.cvt.ugent.be/genterm.htm");
            put(IATE, "https://iate.europa.eu/");
        }
    };

}
