/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.utils;

import browser.termallod.core.TermInfo;
import browser.termallod.utils.NameExtraction;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 *
 * @author elahi
 */
public class FileRelatedUtils {

    public static File[] getFiles(String fileDir, String ntriple) throws Exception{
        
        File dir = new File(fileDir);
        FileFilter fileFilter = new WildcardFileFilter("*" + ntriple);
        File[] files = dir.listFiles(fileFilter);
        return files;

    }

    public static List<File> getFiles(String fileDir, String category, String extension) {

        String[] files = new File(fileDir).list();
        List<File> selectedFiles = new ArrayList<File>();
        for (String fileName : files) {
            if (fileName.contains(category) && fileName.contains(extension)) {
                selectedFiles.add(new File(fileDir + fileName));
            }
        }

        return selectedFiles;

    }
    
    public static List<File> getFiles(String fileDir, String category, String language,String extension) {

        String[] files = new File(fileDir).list();
        List<File> selectedFiles = new ArrayList<File>();
        for (String fileName : files) {
            if (fileName.contains(category) && fileName.contains(language)&& fileName.contains(extension)) {
                selectedFiles.add(new File(fileDir + fileName));
            }
        }

        return selectedFiles;

    }
    
    public static File getFile(String fileDir, String category, String language,String extension) {
        String[] files = new File(fileDir).list();
        File selectedFile = null;
        for (String fileName : files) {
            if (fileName.contains(category) && fileName.contains(language)&& fileName.contains(extension)) {
                 selectedFile =new File(fileDir + fileName);
            }
        }
        return selectedFile;
    }
    
    
    

    /*public static List<File> writeFile(TreeMap<String, TreeMap<String, List<String>>> langSortedTerms, String path) throws IOException {
        List<File> files = new ArrayList<File>();
        for (String language : langSortedTerms.keySet()) {
            String str = "";
            String fileName = path + "_" + language +".txt";
            files.add(new File(fileName));
            TreeMap<String, List<String>> alphabetPairTerms = langSortedTerms.get(language);
            for (String pair : alphabetPairTerms.keySet()) {
                List<String> terms = alphabetPairTerms.get(pair);
                String line = pair + " = " + terms.toString().replace("[", "");
                line = line.replace("]", "");
                str += line + "\n";
            }
            stringToFile(str, fileName);
        }
        return files;
    }*/
    public static List<File> writeFile(TreeMap<String, TreeMap<String, List<TermInfo>>> langSortedTerms, String path) throws IOException {
        List<File> files = new ArrayList<File>();
        for (String language : langSortedTerms.keySet()) {
            String str = "";
            TreeMap<String, List<TermInfo>> alphabetPairTerms = langSortedTerms.get(language);
            for (String pair : alphabetPairTerms.keySet()) {
                String fileName = path + "_" + language + "_" + pair + ".txt";
                List<TermInfo> terms = alphabetPairTerms.get(pair);
                str = "";
                for (TermInfo term : terms) {
                    String line = term.getTermString() + " = " + term.getTermUrl();
                    str += line + "\n";
                }
                stringToFile_ApendIf_Exists(str, fileName);
            }

        }
        return files;
    }

    public static void stringToFile_ApendIf_Exists(String str, String fileName)
            throws IOException {
        if (new File(fileName).exists()) {
            appendStringInFile(str, fileName);
            return;
        } else {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.close();
        }

    }

    public static void stringToFile_DeleteIf_Exists(String str, String fileName)
            throws IOException {
        /*File file = new File(fileName);
        if (file.exists()) {
            file.deleteOnExit();
        }*/
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.close();

    }

    public static Properties getPropertyHash(File propFile) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(propFile);
        BufferedReader br = new BufferedReader(fr);
        Properties props = new Properties();
        props.load(new InputStreamReader(new FileInputStream(propFile), "UTF-8"));
        return props;
    }

    public static void appendStringInFile(String textToAppend, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
        writer.write(textToAppend);
        writer.close();
    }

    public static void cleanDirectory(List<String> categorySet, String PATH, String TEXT_DIR) throws IOException {
        //deleting all generated term filkes
        for (String browser : categorySet) {
            String sourceTextDir = getSourcePath(PATH, browser) + TEXT_DIR;
            FileRelatedUtils.deleteDirectory(sourceTextDir);
            FileRelatedUtils.createDirectory(sourceTextDir);
        }
    }

    public static void cleanDirectory(Map<String, String> categoryOntologyMapper, String PATH, String dataPath) throws IOException {

        //deleting all html files previous files
        for (String key : categoryOntologyMapper.keySet()) {
            key = categoryOntologyMapper.get(key);
            String mainDir = PATH + key;
            String[] infor = key.split("_");
            String termDir = PATH + key + File.separator + dataPath + infor[1];
            FileRelatedUtils.deleteDirectory(mainDir);
            //FileRelatedUtils.deleteDirectory(termDir);
            FileRelatedUtils.createDirectory(mainDir);
            //FileRelatedUtils.createDirectory(termDir);
        }

    }
    
    public static Map<String, List<File>> getLanguageFiles(List<File> inputfiles, String model_extension) {
        Map<String, List<File>> languageFiles = new HashMap<String, List<File>>();
        for (File file : inputfiles) {
            String langCode = NameExtraction.getLanCode(file, model_extension);
            if (languageFiles.containsKey(langCode)) {
                List<File> files = languageFiles.get(langCode);
                files.add(file);
                languageFiles.put(langCode, files);
            } else {
                List<File> files = new ArrayList<File>();
                files.add(file);
                languageFiles.put(langCode, files);
            }

        }
        return languageFiles;
    }

    public static void deleteDirectory(String dir) throws IOException {
        FileUtils.deleteDirectory(new File(dir));
    }

    public static void createDirectory(String dir) throws IOException {
        Files.createDirectories(Paths.get(dir));
    }

    public static String getSourcePath(String PATH, String browser) {
        String source = PATH + browser + File.separator;
        return source;
    }

}
