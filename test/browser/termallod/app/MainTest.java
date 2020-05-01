/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.app;

import browser.termallod.api.DataBaseTemp;
import browser.termallod.app.*;
import browser.termallod.core.Taskimpl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import browser.termallod.constants.FileAndLocationConst;
import browser.termallod.core.MergingTermInfo;
import browser.termallod.core.html.HtmlParameters;
import browser.termallod.utils.FileRelatedUtils;
import java.io.IOException;
import java.net.URI;
import org.junit.Test;

/**
 *
 * @author elahi
 */
public class MainTest {

    public static FileAndLocationConst constants;
    public static Set<String> browserSet;
    private static Set<String> lang = new TreeSet<String>();
    private static String BASE_PATH = "test/resources/data/";
    private Boolean alternativeFlag = true;
    private Boolean textFileModifyFlag = true;
    private Boolean listOfTemPageFlag = true;
    private Boolean termPageFlag = true;
    private static Set<String> browsersToRun=new HashSet<String>();
    public String location = "test/resources/data/iate/txt/";
    private static String CONFIG_PATH = "src/java/resources/data/conf/";
    public DataBaseTemp dataBaseTemp = new DataBaseTemp("test/resources/data/conf/");
    
    
    public static Map<String, String> languageMapper = new HashMap<String, String>() {
        {
            put("en", "English");
            //put("nl", "Dutch");
            //currently dutch does not work...
            /* put("bg", "Bulgarian");
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
            put("lt", "Lithuanian");
            put("lv", "Latvian");
            put("mt", "Maltese");
            put("nl", "Dutch");
            put("ro", "Romanian");
            put("sk", "Slovak");
            put("sl", "Slovenian");
            put("sv", "Swedish");*/
        }
    };

    public MainTest() throws Exception {
        lang = new TreeSet<String>(languageMapper.keySet());
        constants = new FileAndLocationConst(BASE_PATH,"","");
        browserSet = new HashSet<String>(Arrays.asList(constants.IATE));
    }

    @Test
    public void testCreatingTxtFiles() throws Exception {
        
        HtmlParameters htmlCreateParameters=null;
        MergingTermInfo subjectFieldMerging=null;
         
         
        //tasks = new Taskimpl(constants.getLANGUAGE_CONFIG_FILE(), browserSet, constants, alternativeFlag, dataBaseTemp,CONFIG_PATH);
        //tasks.matchTerminologies(constants.GENTERM, constants.IATE);
         //tasks.saveDataIntoFiles(browserSet);
        
        //tasks.readDataFromSavedFiles();
        
       

        
         /*htmlCreateParameters=new HtmlParameters( false, true,  true, true);
         subjectFieldMerging=new MergingTermInfo(constants.alphabetFileName,constants.conceptFileName,
                                                     constants.subjectFileName,constants.cannonical,
                                                     constants.sense,constants.subjectDetail);
        //tasks = new Taskimpl(constants.LANGUAGE_CONFIG_FILE, browserSet, alternativeFlag);
        tasks = new Taskimpl(constants.getLANGUAGE_CONFIG_FILE(), browserSet,constants, alternativeFlag);
        tasks.matchTerminologies(constants.GENTERM, constants.IATE);
        //testMatching();
        browsersToRun=new HashSet<String>(Arrays.asList(constants.IATE));
        tasks.createHtmlFromSavedFiles(constants, browsersToRun, lang, htmlCreateParameters,subjectFieldMerging);*/
       
        System.out.println("Processing finished!!!");
                 
    }
    // run before comit..................to clean all folder

   
}
