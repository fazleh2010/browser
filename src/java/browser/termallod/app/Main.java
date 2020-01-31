/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.app;

import browser.termallod.constants.FileAndCategory;
import browser.termallod.core.LanguageAlphabetPro;
import browser.termallod.core.RdfReader;
import browser.termallod.core.api.LanguageManager;
import java.io.File;
import browser.termallod.utils.FileRelatedUtils;
import java.io.IOException;
import java.util.List;
import browser.termallod.core.html.HtmlCreator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author elahi
 */
public class Main  implements FileAndCategory{

    public static Set<String> browserSet = new HashSet<String>(Arrays.asList(genterm));
    private static Set<String> lang = new TreeSet<String>();
    public static Map<String, String> languageMapper = new HashMap<String, String>() {
        {
            put("en", "English");
            put("nl", "Dutch");
        }
    };

    public static void main(String[] args) throws Exception {
        lang = new TreeSet<String>(languageMapper.keySet());
        inputLoader();
        //process(categorySet, TEXT_EXTENSION);
    }

    private static void inputLoader() throws Exception, IOException {
        LanguageManager languageManager = new LanguageAlphabetPro(configFile);
        FileRelatedUtils.cleanDirectory(categorySet, PATH, textPath);
        FileRelatedUtils.cleanDirectory(categoryOntologyMapper, PATH, dataPath);
        for (String browser : categorySet) {
            if (browserSet.contains(browser)) {
                String source = FileRelatedUtils.getSourcePath(PATH, browser);
                File[] files = FileRelatedUtils.getFiles(source, TURTLE_EXTENSION);
                String inputDir = source + rdfPath;
                String outputDir = source + textPath;
                new RdfReader(inputDir, languageManager, TURTLE, TURTLE_EXTENSION, outputDir);
            }
        }
    }

    private static void process(List<String> categorySet, String MODEL_EXTENSION) throws Exception, IOException {
        FileRelatedUtils.cleanDirectory(categoryOntologyMapper, PATH, dataPath);
        for (String browser : categorySet) {
            if (browserSet.contains(browser)) {
                String source = FileRelatedUtils.getSourcePath(PATH, browser);
                List<String> categoties = categoryBrowser.get(browser);
                HtmlCreator htmlCreator = new HtmlCreator(PATH,lang);
                htmlCreator.createHtmlForEachCategory(categoties, source, MODEL_EXTENSION, browser);
            }
        }
    }

}
