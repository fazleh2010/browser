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
import java.util.Map;
import java.util.TreeSet;

/**
 *
 * @author elahi
 */
public class PageContentGenerator {

    private HashMap<String, List<AlphabetTermPage>> langPages = new HashMap<String, List<AlphabetTermPage>>();
    public Integer numberofElementEachPage = 100;
    private TreeSet<String> languages=new TreeSet();
    private Map<String, String> languageInitpage = new HashMap<String, String>();
    


    public PageContentGenerator( TreeMap<String, CategoryInfo> langSortedTerms) throws Exception {
        if (!langSortedTerms.isEmpty()) {
            this.langPages = this.preparePageTerms(langSortedTerms);
            this.languages=new TreeSet(langPages.keySet());
        } else {
            throw new Exception("No list of terms found!!!");
        }
        this.display();
    }

    private HashMap<String, List<AlphabetTermPage>> preparePageTerms(TreeMap<String, CategoryInfo> langSortedTerms) throws Exception {
        HashMap<String, List<AlphabetTermPage>> langTerms = new HashMap<String, List<AlphabetTermPage>>();
        for (String language : langSortedTerms.keySet()) {
            CategoryInfo categoryInfo= langSortedTerms.get(language);
            TreeMap<String, List<String>> alpahbetTerms =categoryInfo.getLangSortedTerms();
            List<AlphabetTermPage> alphabetTermPageList = new ArrayList<AlphabetTermPage>();
            if(!alpahbetTerms.isEmpty()) {
               this.languageInitpage.put(language, alpahbetTerms.keySet().iterator().next());
            }
            Integer numericalValueOfPair=1;
            for (String alphabetPair : alpahbetTerms.keySet()) {
                List<String> termSet = alpahbetTerms.get(alphabetPair);
                List<String>termList=new ArrayList<String>(termSet);
                Collections.sort(termList);
                Partition<String> partition = Partition.ofSize(termList, this.numberofElementEachPage);
                AlphabetTermPage alphabetTermPage = new AlphabetTermPage(alphabetPair,categoryInfo.getPairFile(alphabetPair), partition,numericalValueOfPair);
                alphabetTermPageList.add(alphabetTermPage);
                numericalValueOfPair++;
            }
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

    public TreeSet<String> getLanguages() {
        return this.languages;
    }

    public String getLanguageInitpage(String language) throws Exception{
        if (languageInitpage.containsKey(language)) {
            return languageInitpage.get(language);
        }
        else {
            throw new Exception("No Alphabet pair!");
        }
    }
   
    private void display() {
        for (String language : this.langPages.keySet()) {
            List<AlphabetTermPage> pages = langPages.get(language);
            for (AlphabetTermPage page : pages) {
                System.out.print(page);
            }

        }
    }

}
