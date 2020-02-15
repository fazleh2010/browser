/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.utils;

import browser.termallod.constants.FileAndCategory;
import browser.termallod.core.input.TermallodBrowser;
import browser.termallod.core.input.Browser;
import browser.termallod.core.input.LangSpecificBrowser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
/**
 *
 * @author elahi
 */
public class GeneralCompScriptGen implements FileAndCategory {

    private final Map<String, Browser> inputBrowsers;
    private final File templateFile;

    public GeneralCompScriptGen(Map<String, Browser> inputBrowsers, File templateFile) throws Exception {
        this.inputBrowsers = inputBrowsers;
        this.templateFile = templateFile;
        if (!templateFile.exists()) {
            throw new Exception(" no template find found for autocompletion!!");
        }
        if (inputBrowsers.isEmpty()) {
            throw new Exception(" No data found for creating java script!!");
        }
    }

   
    public void generateScript() throws IOException, Exception {
        for (String category : inputBrowsers.keySet()) {
            generateScript(category);
        }
    }


    public void generateScript(String category) throws IOException, Exception {
        Browser generalBrowser = inputBrowsers.get(category);
        String ontologyName = TermallodBrowser.getOntologyName(category);
        for (String langCode : generalBrowser.getLangTermUrls().keySet()) {
            LangSpecificBrowser langSpecificBrowser = generalBrowser.getLangTermUrls().get(langCode);
            Map<String, String> allkeysValues = langSpecificBrowser.getTermUrls();
            String str = getTerms(allkeysValues);
            //System.out.println(str);
            //File templateFile = new File(AUTO_COMPLETION_TEMPLATE_LOCATION + "autoComp" + ".js");
            String outputFileName = AUTO_COMPLETION_TEMPLATE_LOCATION + ontologyName + "_" + langCode + ".js";

            System.out.println(outputFileName);
            createAutoCompletionTemplate(templateFile, str, outputFileName);
        }
    }

    /*private void generateScript() throws IOException, Exception {
        for (String category : browsers.getBrowserInfos().keySet()) {
            generateScript(category);
        }
    }

    private void generateScript(String category) throws IOException, Exception {
        GeneralBrowser generalBrowser = browsers.getBrowserInfos().get(category);
        String ontologyName = Browsers.getOntologyName(category);
        for (String langCode : generalBrowser.getLangTermUrls().keySet()) {
            LangSpecificBrowser langSpecificBrowser = generalBrowser.getLangTermUrls().get(langCode);
            Map<String, String> allkeysValues = langSpecificBrowser.getTermUrls();
            String str = getTerms(allkeysValues);
            System.out.println(str);
            File templateFile = new File(AUTO_COMPLETION_TEMPLATE_LOCATION + "autoComp" + ".js");
            String outputFileName = AUTO_COMPLETION_TEMPLATE_LOCATION + ontologyName + "_" + langCode + ".js";

            if (!templateFile.exists()) {
                throw new Exception(" no template find found for autocompletion!!");
            }

            System.out.println(outputFileName);
            createAutoCompletionTemplate(templateFile, str, outputFileName);
        }

    }*/
    private static String getTerms(Map<String, String> allKeysValues) throws IOException {
        String str = "window.termUrls = new Map();" + "\n";
        str += "";
        List<String> termList = new ArrayList<String>(allKeysValues.keySet());
        Collections.sort(termList);

        for (int i = 0; i < termList.size(); i++) {
            String term = termList.get(i);
            term = term.trim();
            String value = allKeysValues.get(term);
            term = quote(term);
            if (i == termList.size()) {
                str += "\"" + term + "\"" + "];" + "\n";
            } else {
                str += "termUrls.set(" + "\"" + term + "\"" + "," + "\"" + value + "\"" + ");" + "\n";
            }
        }
        str += "\n";

        return str;
    }

    private static String quote(String text) {
        if (text.contains("\"")) {
            return text.replaceAll("\"", "");
        }
        if (text.contains("\'")) {
            return text.replaceAll("\'", "");
        }
        if (text.contains("\\[")) {
            return text.replaceAll("[", "");
        }
        if (text.contains("\\]")) {
            return text.replaceAll("]", "");
        }
        if (text.contains("\\,")) {
            return text.replaceAll(",", "");
        }
        if (text.contains("_")) {
            return text.replaceAll("_", " ");
        }

        return text;
    }

    private static void createAutoCompletionTemplate(File templatefileName, String str, String outputFile) throws FileNotFoundException, IOException {
        InputStream input = new FileInputStream(templatefileName);
        String line = IOUtils.toString(input, "UTF-8");
        str += line + "\n";
        FileRelatedUtils.stringToFile_DeleteIf_Exists(str, outputFile);

    }

}
