/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import browser.termallod.utils.FileRelatedUtils;
import browser.termallod.utils.NameExtraction;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class CategoryInfo {

    private String browser = null;
    private String langCode = null;
    private String categoryName = null;
    private Map<String, File> pairFile = new TreeMap<String, File>();
    private TreeMap<String, List<String>> langSortedTerms = new TreeMap<String, List<String>>();

    public CategoryInfo(String browser, String langCode, List<File> files, String model_extension) throws IOException, IOException, IOException, IOException, IOException {
        this.browser = browser;
        this.langCode = langCode;
        for (File file : files) {
            this.categoryName = NameExtraction.getCategoryName(browser, file, model_extension);
            String pair = NameExtraction.getPairName(file, categoryName, langCode, model_extension);
            this.pairFile.put(pair, file);
            this.getValuesFromTextFile(file, pair);
        }
        this.print(langSortedTerms);

    }

    private void getValuesFromTextFile(File propFile, String pair) throws FileNotFoundException, IOException {
        Properties props = FileRelatedUtils.getPropertyHash(propFile);
        Set<String> termSet = props.stringPropertyNames();
        List<String> termList = new ArrayList<String>(termSet);
        Collections.sort(termList);
        //System.out.println(termList.toString());
        langSortedTerms.put(pair, termList);

    }

    public String getBrowser() {
        return browser;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public TreeMap<String, List<String>> getLangSortedTerms() {
        return langSortedTerms;
    }

    public String getLangCode() {
        return langCode;
    }

    public File getPairFile(String pair) {
        return pairFile.get(pair);
    }

    public void print(TreeMap<String, List<String>> langSortedTerms) {
        for (String pair : langSortedTerms.keySet()) {
            String line = pair + "\n";
            List<String> terms = langSortedTerms.get(pair);
            System.out.println(line + terms.toString());
        }
    }

}
