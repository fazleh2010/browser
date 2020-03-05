/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.matching;

import browser.termallod.core.term.TermDetail;
import static browser.termallod.constants.FileAndCategory.IATE;
import static browser.termallod.constants.FileAndCategory.SOLAR;
import browser.termallod.constants.Languages;
import browser.termallod.core.Browser;
import browser.termallod.core.LangSpecificBrowser;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author elahi integrate wordnet and genterm..
 */
public class MatchingTerminologies implements Languages {

    private Map<String, Browser> inputBrowsers = new HashMap<String, Browser>();
    private Map<String, Map<String, Integer>> langFrequency = new HashMap<String, Map<String, Integer>>();

    private static Map<String,Map<String, List<TermDetail>>> langTermDetails = new HashMap<String,Map<String, List<TermDetail>>>();

    public MatchingTerminologies() throws Exception {

    }

    /*public void setDummyData() {
        termDetails = new HashMap<String, List<TermDetail>>();
        List<TermDetail> termDetails = new ArrayList<TermDetail>();
        String url = "https://terms.tdwg.org/wiki/Original";
        String alterurl = "https://terms.tdwg.org/wiki/skos:Alternative";

        termDetails.add(new TermDetail(IATE, "en", "term_1", url, alterurl));
        termDetails.add(new TermDetail(IATE, "en", "term_2", url, alterurl));
        termDetails.add(new TermDetail(IATE, "en", "term_3", url, alterurl));
        termDetails.add(new TermDetail(IATE, "en", "term_4", url, alterurl));
        termDetails.add(new TermDetail(IATE, "en", "term_5", url, alterurl));
        termDetails.put(IATE, termDetails);
    }*/
    //this is an important function temporarily closed..
    public MatchingTerminologies(Map<String, Browser> inputBrowsers, String matchCategory) throws Exception {
        if (inputBrowsers.isEmpty()) {
            throw new Exception("No browser data found for matching terms!!");
        } else {
            this.inputBrowsers = inputBrowsers;
            for (String langCode : languageMapper.keySet()) {
                matchBrowsers(matchCategory, langCode);
            }
            for (String langCode : this.langFrequency.keySet()) {
                Map<String, Integer> fequency = this.langFrequency.get(langCode);
                if (!fequency.isEmpty()) {
                    System.out.println(langCode + "..." + fequency);
                }
            }
        }

    }

    /*public MatchingTerminologies(Map<String, Browser> inputBrowsers, String givenCategory) throws Exception {
        this.inputBrowsers = inputBrowsers;
        if (!this.inputBrowsers.isEmpty()) {
            this.getTerms(givenCategory);
        } else {
            throw new Exception("No browser data found for creating index!!");
        }
    }**/

 /*public MatchingTerminologies() {
         TermDetail termDetail=new TermDetail(ATC,"eng","test", "http");
         List<TermDetail> termDetails=new  ArrayList<TermDetail>();
         termDetails.add(termDetail);
         termDetails.put(termDetail.getLangCode(), termDetails);
    }*/
    
    //important for close for some time..
    /*private void getTerms(String givenCategory) throws Exception {
        for (String category : inputBrowsers.keySet()) {
            Browser browsers = inputBrowsers.get(category);
            if (category.equals(givenCategory)) {
                Map<String, LangSpecificBrowser> langTerms = browsers.getLangTermUrls();
                for (String langCode : langTerms.keySet()) {
                    LangSpecificBrowser langSpecificBrowser = langTerms.get(langCode);
                    List<TermDetail> termDetails = new ArrayList<TermDetail>();
                    for (String term : langSpecificBrowser.getTermUrls().keySet()) {
                        String url = langSpecificBrowser.getTermUrls().get(term);
                        TermDetail termDetail = new TermDetail(givenCategory, langCode, term, url);
                        termDetails.add(termDetail);
                        System.out.println(termDetails.toString());
                    }
                    MatchingTerminologies.termDetails.put(langCode, termDetails);
                }

            }

        }

    }*/

    public void matchBrowsers(String givenCategory, String langCode) throws Exception {
        Map<String, Integer> fequency = new HashMap<String, Integer>();
        Map<String, List<TermDetail>> termDetails=new HashMap<String, List<TermDetail>>();
        Browser givenBrowser = inputBrowsers.get(givenCategory);
        LangSpecificBrowser givenLangSpecificBrowser = null;
        LangSpecificBrowser otherLangSpecificBrowser = null;
        Boolean flag = false;
        if (!givenBrowser.istLanCodeExisit(langCode)) {
            return;
        } else {
            givenLangSpecificBrowser = givenBrowser.getLangTermUrls(langCode);
        }

        //LangSpecificBrowser givenLangSpecificBrowser = inputBrowsers.get(givenCategory).getLangTermUrls(givenlangCode);
        for (String category : inputBrowsers.keySet()) {
            Browser browsers = inputBrowsers.get(category);
            if (!category.equals(givenCategory)) {
                if (browsers.istLanCodeExisit(langCode)) {
                    otherLangSpecificBrowser = browsers.getLangTermUrls(langCode);
                } else {
                    continue;
                }

                Set<String> answer = match(givenLangSpecificBrowser.getTermUrls().keySet(), otherLangSpecificBrowser.getTermUrls().keySet());
                String key = category + "_" + givenCategory + "_" + langCode;
                Integer value = answer.size();
                fequency.put(key, value);
                System.out.println(key + " " + value);
                //List<String> terms = new ArrayList<String>();
                List<TermDetail> termsDetails = new ArrayList<TermDetail>();
                for (String term : answer) {
                     System.out.println(term);
                    String givenUrl = givenLangSpecificBrowser.getTermUrls(term);
                    String url = otherLangSpecificBrowser.getTermUrls(term);
                    termsDetails = this.getMatchedTermsDetail(category, langCode, term, url, givenCategory, givenUrl);
                    //terms.add(termDetail.getTermModified());
                    termDetails.put(term, termsDetails);
                    
                    //System.out.println(termDetail);
                }
                //Collections.sort(terms);
                /*for(String term:terms){
                     System.out.println(term);
                 }*/
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            }

        }
        langTermDetails.put(langCode, termDetails);
        langFrequency.put(langCode, fequency);

    }

    private Set<String> match(LangSpecificBrowser browser1, LangSpecificBrowser browser2) {
        System.out.println(browser1.getTermUrls().keySet());
        System.out.println(browser2.getTermUrls().keySet());
        return match(browser1.getTermUrls().keySet(), browser2.getTermUrls().keySet());

        /*for (String term : answer) {
            Set<String> urls = new HashSet<String>();
            for (String browser : BROWSER_CATEGORIES.keySet()) {
                String url = browser1.getTermUrls(browser);
                urls.add(url);
            }
            TermDetail termDetail = new TermDetail(term, urls);
        }*/
    }

    private static Set<String> match(Set<String> set1, Set<String> set2) {
        return Sets.intersection(set1, set2);
    }

    public static Map<String, Map<String, List<TermDetail>>> getLangTermDetails() {
        return langTermDetails;
    }

    public static Map<String, List<TermDetail>> getCategroyTerms(String langCode) {
        return langTermDetails.get(langCode);
    }

    public static List<TermDetail> getTermDetails(String langCode,String term) {
        Map<String, List<TermDetail>> termDetails=getCategroyTerms(langCode);
        if (termDetails.containsKey(term)) {
            return termDetails.get(term);
        }
        return new ArrayList<TermDetail>();
    }

    private List<TermDetail> getMatchedTermsDetail(String category, String langCode, String term, String url, String givenCategory, String givenUrl) {
        List<TermDetail> termsDetails = new ArrayList<TermDetail>();
        TermDetail termDetail = new TermDetail(category, langCode, term, url, givenCategory, givenUrl);
        termsDetails.add(termDetail);
        return termsDetails;
    }

}
