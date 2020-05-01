/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.html;

import static browser.termallod.constants.HtmlPage.HTML_EXTENSION;
import static browser.termallod.constants.HtmlPage.UNDERSCORE;
import static browser.termallod.constants.HtmlPage.browser;
import browser.termallod.core.AlphabetTermPage;
import java.io.File;
import org.jsoup.nodes.Document;

/**
 *
 * @author elahi
 */
public class OntologyInfo {

    private String language;
    private String ontologyFileName;
    private String categoryType;
    private AlphabetTermPage alphabetTermPage;

    public OntologyInfo(String language, String categoryName, AlphabetTermPage alphabetTermPage) {
        this.language = language;
        this.ontologyFileName = categoryName;
        String[] ontology = ontologyFileName.split("_");
        this.categoryType = ontology[1];
        this.alphabetTermPage = alphabetTermPage;

    }

    public String getLanguage() {
        return language;
    }

    public String getOntologyFileName() {
        return ontologyFileName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public String createFileNameUnicode(Integer pageNumber,AlphabetTermPage alphabetTermPage) {
        String pair = getPairValue(alphabetTermPage);
        return browser + UNDERSCORE + this.language + UNDERSCORE + pair.toString() + UNDERSCORE + pageNumber + HTML_EXTENSION;
    }

    public Document getTermPageTemplate(String templateLocation, String extension) {
        File templateFile = new File(templateLocation + this.ontologyFileName + "_" + language + "_" + "term" + extension);
        HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(templateFile);
        return htmlReaderWriter.getInputDocument();

    }

    public AlphabetTermPage getAlphabetTermPage() {
        return alphabetTermPage;
    }

    public String getPairValue(AlphabetTermPage alphabetTermPage) {
        String pair;
        if (this.language.equals("en")) {
            pair = alphabetTermPage.getAlpahbetPair();
        } else {
            pair = alphabetTermPage.getNumericalValueOfPair().toString();
        }
        return pair;
    }

    private String termFileExtension(String term) {
        return term.trim().replace(" ", "+") + "-" + this.language.toUpperCase() + HTML_EXTENSION;
    }

    public String generateTermFileName() {
        String outputfileString = this.ontologyFileName + "/" + "data" + "/" + this.categoryType + "/";
        return outputfileString;
    }

    public String createFileNameWithPairNumber(Integer pageNumber) {
        String pair = this.getPairValue(alphabetTermPage);
        return browser + UNDERSCORE + this.language + UNDERSCORE + pair.toString() + UNDERSCORE + pageNumber + HTML_EXTENSION;
    }

    /*public File makeHtmlFileName(String base_path, Integer currentPageNumber,AlphabetTermPage alphabetTermPage) {
        return new File(base_path + this.ontologyFileName + "/" + this.createFileNameUnicode(currentPageNumber,alphabetTermPage));
    }*/
    public String creatHtmlFileName(Integer currentPageNumber,AlphabetTermPage alphabetTermPage) {
        return this.ontologyFileName + "/" + this.createFileNameUnicode(currentPageNumber,alphabetTermPage);
    }

}
