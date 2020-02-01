/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.utils;

import browser.termallod.constants.FileAndCategory;
import browser.termallod.constants.Templates;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author elahi
 */
public class MakeListOfTerm implements Templates, FileAndCategory {

    public static void main(String[] argv) throws IOException {

        Set<String> lang = new HashSet<String>();
        lang.add("en");

        for (String browser : categorySet) {
            List<String> categories = FileAndCategory.categoryBrowser.get(browser);
            String source = FileRelatedUtils.getSourcePath(PATH, browser);
            for (String category : categories) {
                String ontologyName = categoryOntologyMapper.get(category);
                List<File> files = FileRelatedUtils.getFiles(source + textPath, ontologyName, ".txt");
                Map<String, List<File>> languageFiles = FileRelatedUtils.getLanguageFiles(files, ".txt");
                List<String> termList = new ArrayList<String>();
                for (String langCode : languageFiles.keySet()) {
                    if (lang.contains(langCode)) {
                        List<File> temFiles = languageFiles.get(langCode);
                        for (File file : files) {
                            Integer index = 0;
                            Properties props = FileRelatedUtils.getPropertyHash(file);
                            Set<String> termSet = props.stringPropertyNames();
                            termList.addAll(termSet);
                        }
                        Collections.sort(termList);
                        String str = getTerms(termList);
                        System.out.println(str);
                        /*File templatefile=FileRelatedUtils.getFile(AUTO_COMPLETION_TEMPLATE_LOCATION,category, langCode,"js");
                        String outputFile=templatefile.getName().replace(JAVA_SCHRIPT_EXTENSION, "") + "_" + "en" + JAVA_SCHRIPT_EXTENSION;
                         */
                        //createAutoCompletionTemplate(templatefile,str,outputFile);
                    }
                }
            }

        }

        /*Properties props = FileRelatedUtils.getPropertyHash(new File(source + textPath + "tbx2rdf_iate_en_A_B.txt"));
        Set<String> termSet = props.stringPropertyNames();
        List<String> termList = new ArrayList<String>(termSet);
        Collections.sort(termList);
        String str = "var countries = [";
        for (int i = 0; i < termList.size(); i++) {
            String term = termList.get(i);
            term = term.trim();
            term = quote(term);
            if (i == termList.size()) {
                str += "\"" + term + "\"" + "];" + "\n";
            } else {
                str += "\"" + term + "\"" + "," + "\n";
            }
            index++;
            //if(index==5)
            //    break;

        }
        str += "\"" + "z" + "\"" + "];" + "\n";
        //System.out.println(str);
        createAutoCompletionTemplate(str);*/
    }

    private static String getTerms(List<String> termList) throws IOException {
        String str = "var countries = [";
       
            for (int i = 0; i < termList.size(); i++) {
                String term = termList.get(i);
                term = term.trim();
                term = quote(term);
                if (i == termList.size()) {
                    str += "\"" + term + "\"" + "];" + "\n";
                } else {
                    str += "\"" + term + "\"" + "," + "\n";
                }
            }
            str += "\"" + "z" + "\"" + "];" + "\n";
            
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
        return text;
    }

    private static void createAutoCompletionTemplate(File templatefileName, String str, String outputFileName) throws FileNotFoundException, IOException {
        InputStream input = new FileInputStream(templatefileName);
        String line = IOUtils.toString(input, "UTF-8");
        str += line + "\n";
        FileRelatedUtils.stringToFile_DeleteIf_Exists(str, outputFileName);

    }

}
