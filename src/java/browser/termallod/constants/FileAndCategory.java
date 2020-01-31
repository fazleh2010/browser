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

    public static String PATH = "src/java/resources/data/";
    public static String dataPath = "data/";
    public static String textPath = "text/";
    public static String rdfPath = "rdf/";
    public static String GENTERM_PATH=PATH+File.separator;
    public static File configFile = new File(PATH+"conf/" +"language.conf");
    public static String NTRIPLE_EXTENSION = ".ntriple";
    public static String TURTLE_EXTENSION = ".ttl";
    public static String JAVA_SCHRIPT_EXTENSION = ".js";
    public static String TEXT_EXTENSION = ".txt";
    public static String N_TRIPLE = "N-TRIPLE";
    public static String TURTLE = "TURTLE";
    public static String TEXT = "TEXT";
    //public static String LOCALHOST_URL_LIST_OF_TERMS_PAGE = "http://localhost/";
    public static String LOCALHOST_URL_LIST_OF_TERMS_PAGE = "";

    public String solar = "solar";
    public String ate = "ate";
    public String diseases = "diseases";
    public String intaglio = "intaglio";
    public String wastemanagement = "wastemanagement";
    public String iate = "iate";
    public String genterm = "genterm";
    public List<String> categorySet = new ArrayList<String>(Arrays.asList(genterm, iate));

    public Map<String, String> categoryOntologyMapper = new HashMap<String, String>() {
        {
            put(ate, "tbx2rdf_atc");
            put(solar, "tbx2rdf_solar");
            put(intaglio, "tbx2rdf_intaglio");
            put(wastemanagement, "tbx2rdf_wastemanagement");
            put(diseases, "tbx2rdf_diseases");
            put(iate, "tbx2rdf_iate");
        }
    };

    public Map<String, String> gentermTemplates = new HashMap<String, String>() {
        {
            put("en", "gentermTemplateEn.html");
            put("nl", "gentermTemplateNl.html");
        }
    };

    public Map<String, List<String>> categoryBrowser = new HashMap<String, List<String>>() {
        {
            put(genterm, new ArrayList<String>(Arrays.asList(ate, solar, intaglio, wastemanagement, diseases)));
            put(iate, new ArrayList<String>(Arrays.asList(iate)));
        }
    };

}
