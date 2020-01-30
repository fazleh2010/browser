/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.utils;

import browser.termallod.constants.FilePathAndConstant;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author elahi
 */
public class MakeListOfTerm implements FilePathAndConstant {

    public static void main(String[] argv) throws IOException {
        String source = FileRelatedUtils.getSourcePath(PATH, iate);
        Integer index = 0;

        Properties props = FileRelatedUtils.getPropertyHash(new File(source + textPath + "tbx2rdf_iate_en_A_B.txt"));
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
        createAutoCompletionTemplate(str);

    }

    private static void createAutoCompletionTemplate(String str) throws FileNotFoundException, IOException {
        InputStream input = new FileInputStream(AUTO_COMPLETION_TEMPLATE);
        String line = IOUtils.toString(input, "UTF-8");
        str += line + "\n";
        System.out.println(TEMPLATE_LOCATION + AUTO_COMPLETION_TEMPLATE.getName().replace(JAVA_SCHRIPT_EXTENSION, "") + "_" + "en" + JAVA_SCHRIPT_EXTENSION);
        FileRelatedUtils.stringToFile_DeleteIf_Exists(str, TEMPLATE_LOCATION + AUTO_COMPLETION_TEMPLATE.getName().replace(JAVA_SCHRIPT_EXTENSION, "") + "_" + "en" + JAVA_SCHRIPT_EXTENSION);

    }

    public static String quote(String text) {
        if (text.contains("\"")) {
            return text.replaceAll("\"", "");
        }
        if (text.contains("\'")) {
            return text.replaceAll("\'", "");
        }
        return text;
    }

}
