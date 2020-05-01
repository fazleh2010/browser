/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.app;

import browser.termallod.constants.FileAndLocationConst;
import browser.termallod.utils.FileRelatedUtils;
import browser.termallod.utils.GentermAutoGen;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author elahi
 */
public class GentermAutoCompTest {

    public static FileAndLocationConst constants;
    public static Set<String> browserSet;
    private static Set<String> lang = new TreeSet<String>();
    private static String BASE_PATH = "test/resources/data/iate/txt/";
    private Map<String, String> allkeysValues = new HashMap<String, String>();
    private String ontologyName = "tbx2rdf_iate";
    private String langCode = "en";

    @Test
    public void testAutoCompletion() throws IOException, Exception {
        String fileName = BASE_PATH + ontologyName + ".txt";
        Map<String, String> allkeysValues = FileRelatedUtils.getHash(fileName);
        String str = GentermAutoGen.getTerms(allkeysValues);
        //System.out.println(str);
        File templateFile = new File(constants.AUTO_COMPLETION_TEMPLATE_LOCATION + "autoComp" + ".js");
        String outputFileName = BASE_PATH + ontologyName + "_" + langCode + ".js";
        System.out.println(outputFileName);
        if (!templateFile.exists()) {
            throw new Exception(" no template find found for autocompletion!!");
        }
        //System.out.println(outputFileName);
        GentermAutoGen.createAutoCompletionTemplate(templateFile, str, outputFileName);

    }

}
