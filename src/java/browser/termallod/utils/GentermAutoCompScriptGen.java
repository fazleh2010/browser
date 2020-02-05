/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.utils;

import browser.termallod.constants.FileAndCategory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author elahi
 */
public class GentermAutoCompScriptGen implements FileAndCategory {

    public static void main(String[] argv) throws IOException, Exception {
        for (String browser : BROWSER_GROUPS) {
            List<String> categories = FileAndCategory.BROWSER_CATEGORIES.get(browser);
            String source = FileRelatedUtils.getSourcePath(BASE_PATH, browser);
            for (String category : categories) {
                 String ontologyName = CATEGORY_ONTOLOGIES.get(category);
                List<File> files = FileRelatedUtils.getFiles(source + TEXT_PATH, ontologyName, ".txt");
                Map<String, List<File>> languageFiles = FileRelatedUtils.getLanguageFiles(files, ".txt");
                for (String langCode : languageFiles.keySet()) {
                   
                        List<File> temFiles = languageFiles.get(langCode);
                        if (temFiles.isEmpty()) {
                            throw new Exception("No files are found to process!!");
                        }
                        Map<String, String> allkeysValues = new HashMap<String, String>();
                        for (File file : files) {
                            Properties props = FileRelatedUtils.getPropertyHash(file);
                            Map<String, String> tempHash = (Map) props;
                            allkeysValues.putAll(tempHash);
                        }
                        String str = getTerms(allkeysValues);
                        //System.out.println(str);
                        File templateFile = new File(AUTO_COMPLETION_TEMPLATE_LOCATION + "autoComp" + ".js");
                        String outputFileName = AUTO_COMPLETION_TEMPLATE_LOCATION + ontologyName + "_" + langCode + ".js";

                        if (!templateFile.exists()) {
                            throw new Exception(" no template find found for autocompletion!!");
                        } 
                        
                        System.out.println(outputFileName);
                        createAutoCompletionTemplate(templateFile, str, outputFileName);
                    
                }
            }

        }

    }

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

    public static String quote(String text) {
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

        return text;
    }

    private static void createAutoCompletionTemplate(File templatefileName, String str, String outputFile) throws FileNotFoundException, IOException {
        InputStream input = new FileInputStream(templatefileName);
        String line = IOUtils.toString(input, "UTF-8");
        str += line + "\n";
        FileRelatedUtils.stringToFile_DeleteIf_Exists(str, outputFile);

    }

}
