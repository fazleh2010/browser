/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.matching;

import browser.termallod.core.input.LangSpecificBrowser;
import browser.termallod.utils.TbxUtil;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author elahi integrate wordnet and genterm..
 */
public class MatchTerminologies {

    private static Set<String> categories = new HashSet<String>();

    public static void main(String[] args) throws IOException, Exception {
        Map<String, String> termUrls1 = new HashMap<String, String>();
        Map<String, String> termUrls2 = new HashMap<String, String>();
        LangSpecificBrowser langSpecificBrowser1 = new LangSpecificBrowser("en", termUrls1);
        LangSpecificBrowser langSpecificBrowser2 = new LangSpecificBrowser("en", termUrls2);
        match(langSpecificBrowser1, langSpecificBrowser2);
    }

    private static void match(LangSpecificBrowser langSpecificBrowser1, LangSpecificBrowser langSpecificBrowser2) {
        Set<String> answer = match(langSpecificBrowser1.getTermUrls().keySet(), langSpecificBrowser2.getTermUrls().keySet());
        System.out.println(answer);
        for (String term : answer) {
            Set<String> urls=new HashSet<String>();
            for (String browser : categories) {
                String url = langSpecificBrowser1.getTermUrls(browser);
                urls.add(url);
            }
            TermDetail termDetail=new TermDetail(term,urls);
        }
    }

    private static Set<String> match(Set<String> set1, Set<String> set2) {
        return Sets.intersection(set1, set2);
    }

}
