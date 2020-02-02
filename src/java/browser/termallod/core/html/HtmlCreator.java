/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.html;

import browser.termallod.constants.FileAndCategory;
import browser.termallod.constants.Templates;
import browser.termallod.core.AlphabetTermPage;
import browser.termallod.core.CategoryInfo;
import browser.termallod.core.PageContentGenerator;
import browser.termallod.utils.FileRelatedUtils;
import browser.termallod.utils.NameExtraction;
import browser.termallod.utils.Partition;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.jsoup.nodes.Document;

/**
 *
 * @author elahi
 */
public class HtmlCreator implements FileAndCategory,Templates {

    private Set<String> lang = new HashSet<String>();
    private String PATH;

    public HtmlCreator(String PATH, Set<String> lang) {
        this.PATH = PATH;
        this.lang = lang;

    }

    public void createHtmlForEachCategory(List<String> browsers, String source, String MODEL_EXTENSION, String browser) throws Exception {
        for (String categoryBrowser : browsers) {
            String ontologyName = categoryOntologyMapper.get(categoryBrowser);
            List<File> files = FileRelatedUtils.getFiles(source + textPath, ontologyName, MODEL_EXTENSION);
            TreeMap<String, CategoryInfo> langSortedTerms = new TreeMap<String, CategoryInfo>();
            String categoryName = null;
            Map<String, List<File>> languageFiles = FileRelatedUtils.getLanguageFiles(files, MODEL_EXTENSION);
            for (String langCode : languageFiles.keySet()) {
                if (lang.contains(langCode)) {
                    List<File> temFiles = languageFiles.get(langCode);
                    CategoryInfo category = new CategoryInfo(source, langCode, temFiles, MODEL_EXTENSION);
                    langSortedTerms.put(category.getLangCode(), category);
                    categoryName = category.getCategoryName();
                    category.print(category.getLangSortedTerms());
                }
            }
            if (!langSortedTerms.isEmpty()) {
                if (categoryBrowser.contains(iate)) {
                    createHtmlForEachLanguage(langSortedTerms, categoryName, browser);
                } else {
                    createHtmlForEachLanguage(langSortedTerms, categoryName, browser);
                }
            }

        }
    }

    private void createHtmlForEachLanguage(TreeMap<String, CategoryInfo> langSortedTerms, String categoryName, String browser) throws Exception {
        PageContentGenerator pageContentGenerator = new PageContentGenerator(langSortedTerms);
        for (String language : pageContentGenerator.getLanguages()) {
            List<AlphabetTermPage> alphabetTermPageList = pageContentGenerator.getLangPages(language);
            for (AlphabetTermPage alphabetTermPage : alphabetTermPageList) {
                File MAIN_PAGE_TEMPLATE = getTemplate(categoryName, language,".html");
                createHtmlForEachAlphabetPair(categoryName, MAIN_PAGE_TEMPLATE, language, alphabetTermPage, pageContentGenerator);
            }
        }
    }

    private void createHtmlForEachAlphabetPair(String categoryName, File templateFile, String language, AlphabetTermPage alphabetTermPage, PageContentGenerator pageContentGenerator) throws Exception {
        Partition partition = alphabetTermPage.getPartition();
        for (Integer page = 0; page < partition.size(); page++) {
            Integer currentPageNumber = page + 1;
            List<String> terms = partition.get(page);
            HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(templateFile);
            Document templateHtml = htmlReaderWriter.getInputDocument();
            HtmlModifier htmlPage = new HtmlModifier(PATH, templateHtml, language, alphabetTermPage, terms, categoryName, pageContentGenerator, currentPageNumber);
            htmlReaderWriter.writeHtml(htmlPage.getGeneratedHtmlPage(), htmlPage.getHtmlFileName());
        }

    }

    private File getTemplate(String categoryName, String langCode,String extension) throws Exception {
            return new File(TEMPLATE_LOCATION + categoryName+"_"+langCode+extension);
    }


}
