/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.input;

import browser.termallod.constants.FileAndCategory;
import static browser.termallod.constants.FileAndCategory.BASE_PATH;
import static browser.termallod.constants.FileAndCategory.BROWSER_GROUPS;
import static browser.termallod.constants.FileAndCategory.CATEGORY_ONTOLOGIES;
import static browser.termallod.constants.FileAndCategory.GENTERM;
import static browser.termallod.constants.FileAndCategory.TEXT_PATH;
import browser.termallod.utils.FileRelatedUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author elahi
 */
public class TermallodBrowser {

    private final Map<String, Browser> browsersInfor;

    public TermallodBrowser() throws Exception {
        browsersInfor = prepareData();
    }

    private Map<String, Browser> prepareData() throws IOException, Exception {
        Map<String, Browser> browserInfos = new HashMap<String, Browser>();
        for (String browser : BROWSER_GROUPS) {
            if (browser.contains(GENTERM)) {
                List<String> categories = FileAndCategory.BROWSER_CATEGORIES.get(browser);
                String source = FileRelatedUtils.getSourcePath(BASE_PATH, browser);
                for (String category : categories) {
                    String ontologyName = CATEGORY_ONTOLOGIES.get(category);
                    List<File> files = FileRelatedUtils.getFiles(source + TEXT_PATH, ontologyName, ".txt");
                    Map<String, List<File>> languageFiles = FileRelatedUtils.getLanguageFiles(files, ".txt");

                    Browser generalBrowser = new Browser(browser, category);
                    for (String langCode : languageFiles.keySet()) {

                        List<File> temFiles = languageFiles.get(langCode);
                        if (temFiles.isEmpty()) {
                            throw new Exception("No files are found to process!!");
                        }
                        Map<String, String> allkeysValues = new HashMap<String, String>();
                        for (File file : files) {
                            Properties props = FileRelatedUtils.getPropertyHash(file);
                            Map<String, String> tempHash = (Map) props;
                            allkeysValues.putAll(tempHash);
                        }
                        generalBrowser.setLangTermUrls(langCode, allkeysValues);
                    }
                    browserInfos.put(category, generalBrowser);

                }
            }
        }
        return browserInfos;
    }

    public Map<String, Browser> getBrowserInfos() {
        return browsersInfor;
    }

    public Browser getBrowserInfos(String category) {
        return browsersInfor.get(category);
    }

    public static String getOntologyName(String category) {
        return CATEGORY_ONTOLOGIES.get(category);
    }

}
