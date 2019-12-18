/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.datamanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import browser.termallod.Constants;
import browser.termallod.utils.Partition;

/**
 *
 * @author elahi
 */
public class DataManager implements Constants {

    private HashMap<String, LanguageTermPage> langTerms = new HashMap<String, LanguageTermPage>();
    private Set<String> languages = new HashSet<String>();
    private Language languageInfo;
    public Integer numberofElementEachPage = 100;

    public DataManager(File languageFile, TreeMap<String, List<String>> langSortedTerms) {
        this.languageInfo = new Language(languageFile);
        this.langTerms = this.preparePageTerms(langSortedTerms);
        this.languages = langTerms.keySet();
        //String[] alphebetPair = languageInfo.getLangAlphabet(language);

    }

    private HashMap<String, LanguageTermPage> preparePageTerms(TreeMap<String, List<String>> langSortedTerms) {
        HashMap<String, LanguageTermPage> langTerms = new HashMap<String, LanguageTermPage>();

        for (String language : langSortedTerms.keySet()) {
            List<String> termList = langSortedTerms.get(language);
            Collections.sort(termList);
            Partition<String> partition = Partition.ofSize(termList, this.numberofElementEachPage);
            LanguageTermPage languageTermPage = new LanguageTermPage(language, partition);
            langTerms.put(language, languageTermPage);
        }
        return langTerms;
    }

    public Language getLanguageInfo() {
        return languageInfo;
    }

    public HashMap<String, LanguageTermPage> getLangTerms() {
        return langTerms;
    }

    public Set<String> getLanguages() {
        return languages;
    }

    public Integer getNumberofElementEachPage() {
        return numberofElementEachPage;
    }

    public LanguageTermPage getLangTerms(String language) {
        if (langTerms.containsKey(language)) {
            return langTerms.get(language);
        }
        return null;
    }

    public String[] getAlphabets(String language) {
        return languageInfo.getLangAlphabet(language);
    }

    /*private TreeMap<Integer, List<String>> getPageTerms(File termFile) {
        TreeMap<Integer, List<String>> pageTerms = new TreeMap<Integer, List<String>>();
        Integer count = 0;

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(termFile));
            String line = reader.readLine();
            List<String> terms = new ArrayList<String>();
            while (line != null) {
                line = reader.readLine();
                terms.add(line);
                if (terms.size() == this.numberofElementEachPage) {
                    count++;
                    pageTerms.put(count, terms);
                    terms = new ArrayList<String>();
                }

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageTerms;
    }

    private void preparePageTerms(TreeMap<String, TreeSet<String>> langSortedTerms) {
        for (String language : langSortedTerms.keySet()) {
            Integer page = 0;
            TreeMap<Integer, TreeSet<String>> pageTerms = new TreeMap<Integer, TreeSet<String>>();
            TreeSet<String> terms = langSortedTerms.get(language);
            if (terms.size() <= this.numberofElementEachPage) {
                pageTerms.put(page++, terms);
            } else {
                TreeSet<String> indexedTerms = new TreeSet<String>();
                for (String term : terms) {
                    indexedTerms.add(term);
                    if (terms.size() == this.numberofElementEachPage) {
                        page++;
                        pageTerms.put(page, indexedTerms);
                        indexedTerms = new TreeSet<String>();
                    }

                }
            }

            this.langTerms.put(language, pageTerms);
        }

        System.out.println(langTerms.toString());

    }*/
}
