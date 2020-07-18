/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.app;

import browser.termallod.api.LanguageManager;
import browser.termallod.utils.FileRelatedUtils;
import citec.core.termbase.TermDetailNew;
import citec.core.termbase.Termbase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author Mohammad Fazleh Elahi
 */
public class AlphabetFiles {

    private Termbase myTerminology = null;
    private LanguageManager languageInfo = null;
    private TreeMap<String, TreeMap<String, List<TermDetailNew>>> langTerms = new TreeMap<String, TreeMap<String, List<TermDetailNew>>>();

    public AlphabetFiles(LanguageManager languageInfo, Termbase myTerminology) throws Exception {
        this.languageInfo = languageInfo;
        this.myTerminology = myTerminology;
        this.generate();
    }

    private void generate() throws Exception {
        for (String term : myTerminology.getTerms().keySet()) {
            TermDetailNew termDetailNew = myTerminology.getTerms().get(term);
            String language = termDetailNew.getLanguage();
            if (langTerms.containsKey(termDetailNew.getLanguage())) {
                langTerms = ifElementExist(language, termDetailNew, langTerms);
            } else {
                langTerms = ifElementNotExist(language, termDetailNew, langTerms);
            }
        }
    }

    private TreeMap<String, TreeMap<String, List<TermDetailNew>>> ifElementExist(String language, TermDetailNew term, TreeMap<String, TreeMap<String, List<TermDetailNew>>> langTerms) {
        String pair;
        pair = getAlphabetPair(language, term.getTermOrg());
        TreeMap<String, List<TermDetailNew>> alpahbetTerms = langTerms.get(language);
        try {
            if (alpahbetTerms.containsKey(pair)) {
                List<TermDetailNew> terms = alpahbetTerms.get(pair);
                terms.add(term);
                alpahbetTerms.put(pair, terms);
                langTerms.put(language, alpahbetTerms);
            } else {
                List<TermDetailNew> terms = new ArrayList<TermDetailNew>();
                terms.add(term);
                alpahbetTerms.put(pair, terms);
                langTerms.put(language, alpahbetTerms);
            }
        } catch (NullPointerException e) {
            System.out.println("Null pointer:" + language + " " + term);

        }
        return langTerms;
    }

    private TreeMap<String, TreeMap<String, List<TermDetailNew>>> ifElementNotExist(String language, TermDetailNew term, TreeMap<String, TreeMap<String, List<TermDetailNew>>> langTerms) {
        String pair;
        try {
            pair = getAlphabetPair(language, term.getTermOrg());
            TreeMap<String, List<TermDetailNew>> alpahbetTerms = new TreeMap<String, List<TermDetailNew>>();
            List<TermDetailNew> terms = new ArrayList<TermDetailNew>();
            terms.add(term);
            alpahbetTerms.put(pair, terms);
            langTerms.put(language, alpahbetTerms);
        } catch (NullPointerException e) {
            System.out.println("Null pointer:" + language + " " + term);

        }
        return langTerms;
    }

    private String getAlphabetPair(String language, String term) {
        HashMap<String, String> alphabetPairs;
        try {
            alphabetPairs = languageInfo.getLangAlphabetHash(language);
            term = term.trim();
            String letter = term.substring(0, 1);
            if (alphabetPairs.containsKey(letter)) {
                String pair = alphabetPairs.get(letter);
                return pair;
            }
        } catch (Exception ex) {
            //System.out.println("No alphebet found for the lanague" + language);
        }

        return null;
    }

    public TreeMap<String, TreeMap<String, List<TermDetailNew>>> getLangTerms() {
        return langTerms;
    }

}
