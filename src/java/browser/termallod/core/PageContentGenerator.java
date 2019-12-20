/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import browser.termallod.utils.Partition;
import java.util.ArrayList;

/**
 *
 * @author elahi
 */
public class PageContentGenerator {

    private HashMap<String, List<AlphabetTermPage>> langPages = new HashMap<String, List<AlphabetTermPage>>();
    private HashMap<String, List<String>> alpahbetTermsExists = new HashMap<String,  List<String>>();
    public Integer numberofElementEachPage = 100;

    public PageContentGenerator(Ntriple ntriple) throws Exception {
        TreeMap<String, TreeMap<String, List<String>>> langSortedTerms = ntriple.getLangSortedTerms();
        if (!langSortedTerms.isEmpty()) {
            this.langPages = this.preparePageTerms(langSortedTerms);
        } else {
            throw new Exception("No list of terms found!!!");
        }
        this.display();
    }

    private HashMap<String, List<AlphabetTermPage>> preparePageTerms(TreeMap<String, TreeMap<String, List<String>>> langSortedTerms) {
        HashMap<String, List<AlphabetTermPage>> langTerms = new HashMap<String, List<AlphabetTermPage>>();
        for (String language : langSortedTerms.keySet()) {
            TreeMap<String, List<String>> alpahbetTerms = langSortedTerms.get(language);
            List<AlphabetTermPage> alphabetTermPageList = new ArrayList<AlphabetTermPage>();
            List<String> alphabetListExists = new ArrayList<String>();
            for (String alphabetPair : alpahbetTerms.keySet()) {
                List<String> termList = alpahbetTerms.get(alphabetPair);
                Collections.sort(termList);
                Partition<String> partition = Partition.ofSize(termList, this.numberofElementEachPage);
                AlphabetTermPage alphabetTermPage = new AlphabetTermPage(alphabetPair, partition);
                alphabetTermPageList.add(alphabetTermPage);
                alphabetListExists.add(alphabetPair);
            }
            this.alpahbetTermsExists.put(language, alphabetListExists);
            langTerms.put(language, alphabetTermPageList);
        }
        return langTerms;
    }

    public List<AlphabetTermPage> getLangPages(String language) {
        if (langPages.containsKey(language)) {
            return langPages.get(language);
        }
        return new ArrayList<AlphabetTermPage>();
    }

    public List<String> getAlpahbetTermsExists(String language) {
        if (alpahbetTermsExists.containsKey(language)) {
            return alpahbetTermsExists.get(language);
        }
        return new ArrayList<String>();
    }

    public Set<String> getLanguages() {
        return langPages.keySet();
    }

    /*private HashMap<String, LanguageTermPage> preparePageTerms(TreeMap<String, List<String>> langSortedTerms) {
        HashMap<String, LanguageTermPage> langTerms = new HashMap<String, LanguageTermPage>();
        for (String language : langSortedTerms.keySet()) {
            List<String> termList = langSortedTerms.get(language);
            Collections.sort(termList);
            Partition<String> partition = Partition.ofSize(termList, this.numberofElementEachPage);
            LanguageTermPage languageTermPage = new LanguageTermPage(language, partition);
            langTerms.put(language, languageTermPage);
        }
        return langTerms;
    }*/
 /*public List<AlphabetTermPage> getLangTerms(String language) {
        if (langPages.containsKey(language)) {
            return langPages.get(language);
        }
        return new ArrayList<AlphabetTermPage>();
    }*/

 /*private void alphabetSeperation(TreeMap<String, List<String>> langSortedTerms) {
        for (String language : langSortedTerms.keySet()) {
            System.out.println(language);
            List<String> terms = langSortedTerms.get(language);
            for (String term : terms) {
                
               System.out.println(term);
            }
        }
    }*/
    private void display() {
        for (String language : this.langPages.keySet()) {
            List<AlphabetTermPage> pages = langPages.get(language);
            for (AlphabetTermPage page : pages) {
                System.out.print(page);
            }

        }
    }

}
