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
public interface FilePathAndConstant {

    public static String PATH = "src/java/browser/termallod/data/";
    public static String dataPath = "data/";
    public static String textPath = "text/";
    public static String rdfPath = "rdf/";
    public static String TEMPLATE_LOCATION = "src/java/browser/termallod/template/";
    public static File MAIN_PAGE_TEMPLATE_IATE = new File(TEMPLATE_LOCATION + "iateTemplate.html");
    public static File MAIN_PAGE_TEMPLATE_GENTERM = new File(TEMPLATE_LOCATION + "gentermTemplate.html");

    public static File TERM_PAGE_TEMPLATE = new File(TEMPLATE_LOCATION + "termDefination.html");
    public static File configFile = new File(PATH + "conf/" + "language.conf");
    public static File AUTO_COMPLETION_TEMPLATE = new File(TEMPLATE_LOCATION + "autocompletionTemplate.js");
    //public static String GENTERM_PATH = PATH + "genterm/";
    public static String IATE_PATH = PATH + "iate/";
    public static String GENTERM_PATH = PATH + "genterm/";
    public static String NTRIPLE_EXTENSION = ".ntriple";
    public static String TURTLE_EXTENSION = ".ttl";
    public static String JAVA_SCHRIPT_EXTENSION = ".js";
    public static String TEXT_EXTENSION = ".txt";
    public static String N_TRIPLE = "N-TRIPLE";
    public static String TURTLE = "TURTLE";
    public static String TEXT = "TEXT";
    public static String LIST_OF_TERMS_PAGE_LOCATION = PATH + "html/";
    //public static String LOCALHOST_URL_LIST_OF_TERMS_PAGE = "http://localhost/";
    public static String LOCALHOST_URL_LIST_OF_TERMS_PAGE = "";
    //public static String LOCALHOST_URL_LIST_OF_TERMS_PAGE = "https://webtentacle1.techfak.uni-bielefeld.de/";
    public static String HTML_EXTENSION = ".html";
    public static Integer INITIAL_PAGE = 1;
    public String UNDERSCORE = "_";
    public String browser="browser";

    public String solar = "solar";
    public String ate = "ate";
    public String diseases = "diseases";
    public String intaglio = "intaglio";
    public String wastemanagement = "wastemanagement";
    public String iate = "iate";
    public String genterm = "genterm";
    public List<String> categorySet = new ArrayList<String>(Arrays.asList(genterm,iate));

    public Map<String, String> categoryOntologyMapper = new HashMap<String, String>() {
        {
            put(ate, "tbx2rdf_atc");
            put(solar, "tbx2rdf_solarenergy");
            put(intaglio, "tbx2rdf_intaglio");
            put(wastemanagement, "tbx2rdf_wastemanagement");
            put(diseases, "tbx2rdf_diseases");
            put(iate, "tbx2rdf_iate");
        }
    };
    
    public Map<String, List<String>> categoryBrowser = new HashMap<String, List<String>>() {
        {
            put(genterm, new ArrayList<String>(Arrays.asList(ate,solar,intaglio,wastemanagement,diseases)));
            put(iate, new ArrayList<String>(Arrays.asList(iate)));
        }
    };


    public Map<String, String> languageMapper = new HashMap<String, String>() {
        {
            put("bg", "Bulgarian");
            put("cs", "Czech");
            put("da", "Danish");
            put("de", "German");
            put("el", "Greek");
            put("en", "English");
            put("es", "German");
            put("et", "Estonian");
            put("fi", "Finnish");
            put("fr", "French");
            put("ga", "Irish");
            put("hr", "Croatian");
            put("hu", "Hungarian");
            put("it", "Italian");
            put("It", "Lithuanian");
            put("lv", "Latvian");
            put("mt", "Maltese");
            put("nl", "Dutch");
            put("ro", "Romanian");
            put("sk", "Slovak");
            put("sl", "Slovenian");
            put("sv", "Swedish");
        }
    };
    
      /*<td><a href="browser_bg_%d0%90_%d0%91_1.html">Bulgarian</a></td> 
            <td><a href="browser_cs_A_Á_1.html">Czech</a></td> 
            <td><a href="browser_da_A_B_1.html">Danish</a></td> 
            <td><a href="browser_de_A_B_1.html">German</a></td> 
            <td><a href="browser_el_A_B_1.html">Greek</a></td> 
            <td><a href="browser_en_A_B_1.html">English</a></td> 
            <td><a href="browser_es_A_B_1.html">Spanish</a></td> 
            <td><a href="browser_et_A_B_1.html">Estonian</a></td> 
           </tr> 
           <tr> 
            <td><a href="browser_fi_A_B_1.html">Finnish</a></td> 
            <td><a href="browser_fr_A_B_1.html">French</a></td> 
            <td><a href="browser_ga_A_B_1.html">Irish</a></td> 
            <td><a href="browser_hr_A_B_1.html">Croatian</a></td> 
            <td><a href="browser_hu_A_Á_1.html">Hungarian</a></td> 
            <td><a href="browser_it_A_B_1.html">Italian</a></td> 
            <td><a href="browser_It_A_Ą_1.html">Lithuanian</a></td> 
            <td><a href="browser_lv_A_Ā_1.html">Latvian</a></td> 
    
     <td><a href="browser_mt_A_B_1.html">Maltese</a></td> 
            <td><a href="browser_nl_A_B_1.html">Dutch</a></td> 
            <td><a href="browser_ro_A_Ă_1.html">Romanian</a></td> 
            <td><a href="browser_sk_A_Á_1.html">Slovak</a></td> 
            <td><a href="browser_sl_A_B_1.html">Slovenian</a></td> 
            <td><a href="browser_sv_A_B_1.html">Swedish</a></td> 
           </tr> 
    
    
    */

}
