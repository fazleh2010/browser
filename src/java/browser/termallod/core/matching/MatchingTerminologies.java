/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.matching;

import static browser.termallod.constants.FileAndCategory.ATC;
import static browser.termallod.constants.FileAndCategory.IATE;
import browser.termallod.core.Browser;
import browser.termallod.core.LangSpecificBrowser;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author elahi integrate wordnet and genterm..
 */
public class MatchingTerminologies {

    private Map<String, Browser> inputBrowsers = new HashMap<String, Browser>();
    private Map<String, List<TermDetail>> categroyTerms=new HashMap<String, List<TermDetail>>();
    

    public MatchingTerminologies(Map<String, Browser> inputBrowsers) throws Exception {
        if (!inputBrowsers.isEmpty()) {
            matchBrowsers(inputBrowsers, IATE, "en");
        } else {
            throw new Exception("No browser data found for creating index!!");
        }

    }

    public MatchingTerminologies(Map<String, Browser> inputBrowsers, String givenCategory) throws Exception {
        this.inputBrowsers = inputBrowsers;
        if (!this.inputBrowsers.isEmpty()) {
            this.getTerms(givenCategory);
        } else {
            throw new Exception("No browser data found for creating index!!");
        }
    }

    public MatchingTerminologies() {
         TermDetail termDetail=new TermDetail(ATC,"eng","test", "http");
         List<TermDetail> termDetails=new  ArrayList<TermDetail>();
         termDetails.add(termDetail);
         categroyTerms.put(termDetail.getLangCode(), termDetails);
    }

    private void getTerms(String givenCategory) throws Exception {
        for (String category : inputBrowsers.keySet()) {
            Browser browsers = inputBrowsers.get(category);
            if (category.equals(givenCategory)) {
                Map<String, LangSpecificBrowser> langTerms = browsers.getLangTermUrls();
                for (String langCode : langTerms.keySet()) {
                    LangSpecificBrowser langSpecificBrowser = langTerms.get(langCode);
                    List<TermDetail> termDetails = new ArrayList<TermDetail>();
                    for (String term : langSpecificBrowser.getTermUrls().keySet()) {
                        String url = langSpecificBrowser.getTermUrls().get(term);
                        TermDetail termDetail = new TermDetail(givenCategory,langCode, term, url);
                        termDetails.add(termDetail);
                        System.out.println(termDetails.toString());
                    }
                    categroyTerms.put(langCode, termDetails);
                }

            }

        }
        
    }

    public void matchBrowsers(Map<String, Browser> input, String givenCategory, String langCode) throws Exception {

        LangSpecificBrowser givenLangSpecificBrowser = input.get(givenCategory).getLangTermUrls(langCode);

        for (String category : input.keySet()) {
            Browser browsers = input.get(category);
            if (!category.equals(givenCategory)) {
                LangSpecificBrowser langSpecificBrowser = browsers.getLangTermUrls(langCode);
                Set<String> answer = match(givenLangSpecificBrowser.getTermUrls().keySet(), langSpecificBrowser.getTermUrls().keySet());
                System.out.println(category + "..." + givenCategory);
                for (String term : answer) {
                    String givenUrl = givenLangSpecificBrowser.getTermUrls(term);
                    String url = langSpecificBrowser.getTermUrls(term);
                    TermDetail termDetail = new TermDetail(langCode, term, givenUrl, url);
                    System.out.println(termDetail);
                }
                break;
            }

        }

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

    public Map<String, List<TermDetail>> getCategroyTerms() {
        return categroyTerms;
    }
    public  List<TermDetail> getCategroyTerms(TermDetail termdetail) throws Exception {
        if(categroyTerms.isEmpty())
            throw new Exception("List is emplty!!");
        return categroyTerms.get(termdetail.getLangCode());
    }

}
