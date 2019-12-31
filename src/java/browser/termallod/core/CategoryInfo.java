/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import browser.termallod.utils.NameExtraction;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author elahi
 */
public class CategoryInfo {

    private String browser = null;
    private String langCode = null;
    private String categoryName = null;
    private TreeMap<String, List<String>> langSortedTerms = new TreeMap<String, List<String>>();

    public CategoryInfo(String browser, File file, String model_extension) throws IOException, IOException, IOException, IOException, IOException {
        this.browser = browser;
        this.categoryName = NameExtraction.getCategoryName(browser, file, model_extension);
        this.langCode = NameExtraction.getLanCode(file, model_extension);
        this.getValuesFromTextFile(file);
    }

    private void getValuesFromTextFile(File file) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        langSortedTerms = new TreeMap<String, List<String>>();
        while ((line = br.readLine()) != null) {
            if (line.contains("=")) {
                //System.out.println(line);
                String[] strArray = line.split("=");
                String pair = strArray[0].trim();
                List<String> newTerms = this.getTerms(strArray[1].trim());
                if (langSortedTerms.containsKey(pair)) {
                    List<String> existTerms = langSortedTerms.get(pair);
                    existTerms.addAll(newTerms);
                    langSortedTerms.put(pair, existTerms);
                } else {
                    langSortedTerms.put(pair, newTerms);
                }

            }

        }
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

    private List<String> getTerms(String line) {
        String[] myStringArray = line.split(",");
        TreeSet<String> termSet = new TreeSet(Arrays.asList(myStringArray));
        List<String> terms = new ArrayList<String>(termSet.size());
        terms.addAll(termSet);
        return terms;
    }

    public String getLangCode() {
        return langCode;
    }

    @Override
    public String toString() {
        return "CategoryInfo{" + "browser=" + browser + ", langCode=" + langCode + ", categoryName=" + categoryName + "\n"+" langSortedTerms=" + "\n"+ this.print(langSortedTerms) + '}';
    }

   private String print(TreeMap<String, List<String>> langSortedTerms) {
       String str="";
        for (String pair : langSortedTerms.keySet()) {
            String line =pair+ "\n";
            List<String> terms = langSortedTerms.get(pair);
            for (String term : terms) {
                line+=term+ "\n";
                str+=line;
            }
        }
        return str;
    }

}
