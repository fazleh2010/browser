/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import browser.termallod.core.api.HtmlPage;

/**
 *
 * @author elahi
 */
public class HtmlPageGenerator implements HtmlPage {

    private final Document generatedHtmlPage;
    private final Integer currentPageNumber;
    private final String language;
    private final String ontologyFileName;
    private  String categoryType="";
    private File htmlFileName = null;

    public HtmlPageGenerator(Document templateHtml, String language, AlphabetTermPage alphabetTermPage, List<String> terms, String categoryName, PageContentGenerator pageContentGenerator, Integer currentPageNumber) throws Exception {
        this.currentPageNumber = currentPageNumber;
        this.language = language;
        this.ontologyFileName = categoryName;
        String []ontology=ontologyFileName.split("_");
        this.categoryType=ontology [1];
        this.generatedHtmlPage = this.generateHtmlFromTemplate(templateHtml, terms, pageContentGenerator, alphabetTermPage);
        this.htmlFileName = outputFileName(currentPageNumber, alphabetTermPage);
    }

    private Document generateHtmlFromTemplate(Document templateHtml, List<String> terms, PageContentGenerator pageContentGenerator, AlphabetTermPage alphabetTermPage) throws Exception {
        Element body = templateHtml.body();
        String alphebetPair = alphabetTermPage.getAlpahbetPair();
        Integer numberofPages = alphabetTermPage.getNumberOfPages();
        //currently not
        Integer emptyTerm = alphabetTermPage.getEmptyTerm();
        this.createLangSelectBox(body, pageContentGenerator);
        createAlphabet(body, alphebetPair, pageContentGenerator);
        createTerms(body, terms, alphebetPair, emptyTerm,alphabetTermPage);

        /*Element divCurrentPageUpper = body.getElementsByClass("activepageUpper").get(0);
        this.assignCurrentPageNumber(divCurrentPageUpper);
         Element divCurrentPageLower = body.getElementsByClass("activepageLower");
        this.assignCurrentPageNumber(divCurrentPageLower);*/
        createUperPageNumber(body, alphebetPair, numberofPages);
        createLowerPageNumber(body, alphebetPair, numberofPages);
        return templateHtml;
    }

    @Override
    public void createLangSelectBox(Element body, PageContentGenerator pageContentGenerator) throws Exception {
        Element divLanguage = body.getElementsByClass("langauge selection box").get(0);
        String options = "<ul class=" + "\"" + "language-list" + "\"" + ">";
        for (String language : pageContentGenerator.getLanguages()) {
            if (languageMapper.containsKey(language)) {
                String languageDetail = languageMapper.get(language);
                String pair = pageContentGenerator.getLanguageInitpage(language);
                pair = getAlphabetFileName(pair, language);
                String url = LOCALHOST_URL_LIST_OF_TERMS_PAGE + this.ontologyFileName+"/"+""+pair;
                String option = "<li>&#8227; <a href=" + "\"" + url + "\"" + ">" + languageDetail + "</a></li>";
                options += option;
            }
        }
        options = options + "</ul>";
        String form = "<form>" + options + "</form>";
        divLanguage.append(form);
    }


    @Override
    public String createAlphabet(Element body, String alphebetPair, PageContentGenerator pageContentGenerator) throws Exception {
        Element divAlphabet = body.getElementsByClass("currentpage").get(0);
        divAlphabet.append("<span>" + alphebetPair + "</span>");
        List<String> alphabetPairsExists = pageContentGenerator.getAlpahbetTermsExists(language);
        for (String pair : alphabetPairsExists) {
            if (!pair.contains(alphebetPair)) {
                String alphabetFileName = getAlphabetFileName(pair, language);
                //String alphabetFileName = categoryName + UNDERSCORE + language + UNDERSCORE + pair + UNDERSCORE + "1" + HTML_EXTENSION;
                String li = getAlphebetLi(pair, alphabetFileName);
                divAlphabet.append(li);
            }

        }
        return alphebetPair;
    }

    @Override
    public void createUperPageNumber(Element body, String alphebetPair, Integer numberofPages) {
        Element divPageUper = body.getElementsByClass("paging_links inner").get(0);
        List<String> pageUperliS = getPageLi(alphebetPair, numberofPages);
        if (pageUperliS.isEmpty()) {
            return;
        }
        for (String li : pageUperliS) {
            divPageUper.append(li);
        }
    }

    @Override
    public void createLowerPageNumber(Element body, String alphebetPair, Integer numberofPages) {
        Element divPageDown = body.getElementsByClass("paging_links inner_down").get(0);
        List<String> liS = getPageLi(alphebetPair, numberofPages);
        if (liS.isEmpty()) {
            return;
        }
        for (String li : liS) {
            divPageDown.append(li);
        }
    }

    @Override
    public void createTerms(Element body, List<String> terms, String alphebetPair, Integer emptyTerm,AlphabetTermPage alphabetTermPage) {
        Element divTerm = body.getElementsByClass("result-list1 wordlist-oxford3000 list-plain").get(0);
        for (String term : terms) {
            String liString = getTermLi(alphebetPair, term,alphabetTermPage);
            divTerm.append(liString);
            /*File termFile =new File(termFileLocation(term));
            HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(TERM_PAGE_TEMPLATE);
            Document templateHtml = htmlReaderWriter.getInputDocument();
            htmlReaderWriter.writeHtml(templateHtml, termFile);*/
        }
        
        /*for (Integer index=0;index<emptyTerm;index++) {
            String liString = "";
            divTerm.append(liString);
        }*/

    }

    private String getTermLi(String alphebetPair, String term,AlphabetTermPage alphabetTermPage) {
        //<li><a href="https://www.oxfordlearnersdictionaries.com/definition/english/abandon_1" title="abandon definition">abandon</a> </li>
        String title = "title=" + '"' + term + " definition" + '"';
        //real version
        //String url = this.path+"/"+DEFINITION+"/" +language+"/" +alphebetPair +"/" +term + "_1";
        String url =generateTermUrl(term);
        //String url = LOCALHOST_URL + "termDefination.php";
        //System.out.println(url);
        String a = "<a href=" + url + " " + title + ">" + term + "</a>";
        String li = "\n<li>" + a + "</li>\n";
        return li;
    }

    private String getAlphebetLi(String alphabet, String alphabetFileName) {
        //Elements divAlphabet = body.getElementsByClass("side-selector__left");
        //Element content = body.getElementById("entries-selector");
        /*String ontologyLocation="";
        if(categoryOntologyMapper.containsKey(this.categoryName)){
            ontologyLocation=categoryOntologyMapper.get(categoryName);
        }*/
        String url = LOCALHOST_URL_LIST_OF_TERMS_PAGE + this.ontologyFileName+"/"+alphabetFileName;
        String a = "<a href=" + url + ">" + alphabet + "</a>";
        String li = "<li>" + a + "</li>";
        
        return li;
    }

    private List<String> getPageLi(String pair, Integer pages) {
        //Elements divAlphabet = body.getElementsByClass("side-selector__left");
        //Element content = body.getElementById("entries-selector");
        List<String> liS = new ArrayList<String>();
        String pageUrl = null;
        String li = "";
        /*"<span>" + this.currentPageNumber + "</span>";
        liS.add(li);*/
        if (pages == 1) {
            return new ArrayList<String>();
        }
        for (Integer page = 0; page < pages; page++) {
            Integer pageNumber = (page + 1);
            pageUrl = LOCALHOST_URL_LIST_OF_TERMS_PAGE +ontologyFileName +File.separator +this.categoryType+UNDERSCORE + language + UNDERSCORE + pair + UNDERSCORE + pageNumber + HTML_EXTENSION;
            String a = "<a href=" + pageUrl + ">" + pageNumber + "</a>";
            li = "\n<li>" + a + "</li>\n";
            liS.add(li);
        }
        return liS;
    }

    private void assignCurrentPageNumber(Element divCurrentPage) {
        divCurrentPage.append("<span>" + this.currentPageNumber + "</span> </li>");

    }

    private String assignHighLightPageNumber(String pageUrl) {
        String a = "<a href=" + pageUrl + ">" + "&gt;" + "</a>";
        return "\n<li>" + a + "</li>\n";

    }
    
    private String getAlphabetFileName(String intialFileName, String langCode) {
        intialFileName = categoryType + UNDERSCORE + langCode + UNDERSCORE + intialFileName + UNDERSCORE + INITIAL_PAGE + HTML_EXTENSION;
        return intialFileName;
    }

    private File outputFileName(Integer page, AlphabetTermPage alphabetTermPage) {
        String outputfileString = this.categoryType + "_" + language + "_";
        outputfileString =PATH+this.ontologyFileName +"/"+ outputfileString + alphabetTermPage.getAlpahbetPair() + "_" + page + HTML_EXTENSION;
        File outputFile = new File(outputfileString);
        return outputFile;
    }

    private String generateTermUrl(String term) {
        String outputfileString =  LOCALHOST_URL_LIST_OF_TERMS_PAGE  + this.generateTermFileName()  +this.termFileExtension(term);
        return outputfileString;
    }
    private String generateTermFileName() {
        String outputfileString = ontologyFileName +"/"+ "data"+"/"+this.categoryType+"/";
        return outputfileString;
    }
    
    private String termFileExtension(String term) {
        return term.trim().replace(" ", "+")+"-" + language.toUpperCase()+HTML_EXTENSION;
    }
    private String termFileLocation(String term) {
        return PATH+this.generateTermFileName()+this.termFileExtension(term) ;
    }

    public Document getGeneratedHtmlPage() {
        return generatedHtmlPage;
    }

    public File getHtmlFileName() {
        return htmlFileName;
    }

    /*<select name="forma" onchange="location = this.options[this.selectedIndex].value;">
                                                <option value="" selected="selected">A-B</option>
                                                <option value="https://www.oxfordlearnersdictionaries.com/wordlist/english/oxford3000/Oxford3000_C-D/">C-D</option>
                                                <option value="https://www.oxfordlearnersdictionaries.com/wordlist/english/oxford3000/Oxford3000_E-G/">E-G</option>
                                                <option value="https://www.oxfordlearnersdictionaries.com/wordlist/english/oxford3000/Oxford3000_H-K/">H-K</option>
                                                <option value="https://www.oxfordlearnersdictionaries.com/wordlist/english/oxford3000/Oxford3000_L-N/">L-N</option>
                                                <option value="https://www.oxfordlearnersdictionaries.com/wordlist/english/oxford3000/Oxford3000_O-P/">O-P</option>
                                                <option value="https://www.oxfordlearnersdictionaries.com/wordlist/english/oxford3000/Oxford3000_Q-R/">Q-R</option>
                                                <option value="https://www.oxfordlearnersdictionaries.com/wordlist/english/oxford3000/Oxford3000_S/">S</option>
                                                <option value="https://www.oxfordlearnersdictionaries.com/wordlist/english/oxford3000/Oxford3000_T/">T</option>
                                                <option value="https://www.oxfordlearnersdictionaries.com/wordlist/english/oxford3000/Oxford3000_U-Z/">U-Z</option>
                                            </select>
    
    
            <select name="forma" onchange="location = this.options[this.selectedIndex].value;">
<option value="http://localhost/atc_en_A_B_1.html">English</option>

<option value="http://localhost/atc_nl_A_B_1.html">Dutch</option>
</select>

    
     */
 /*<li>&#8227; <a href="" > Inbox</a></li>
        <li>&#8227; <a href="" > Compose</a></li>
        <li>&#8227; <a href="" > Reports</a></li>
        <li>&#8227; <a href="" > Preferences</a></li>
        <li>&#8227; <a href="" > logout</a></li>
     */
 /* @Override
    public void createLangSelectBox(Element body, PageContentGenerator pageContentGenerator) throws Exception {
       Element divLanguage = body.getElementsByClass("langauge selection box").get(0);
        String options = "";
        String option = "";
        //String url=HtmlPage.LOCALHOST_URL;
        Integer index=0;
        for (String language : pageContentGenerator.getLanguages()) {
            if (languageMapper.containsKey(language)) {
                String languageDetail = languageMapper.get(language);
                String pair = pageContentGenerator.getLanguageInitpage(language);
                pair = getAlphabetFileName(pair, language);
                String url = LOCALHOST_URL + pair;
                if(index==0){
                   option = "\n" + "<option value=" + "\""+ "\"" + " selected=" + "\""+ "selected"+"\"" + languageDetail + "</option>" + "\n";
                   index++;
                 }
                option = "\n" + "<option value=" + "\""+url +"\"" +">"+languageDetail + "</option>" + "\n";
                options += option;
            }

        }

        //<form name="store" id="store" method="post" action="" id="FORM_ID" >
        //<select id="mySelect" onchange="myFunction()">
        String selection =  "\n" + "<select name=" + "\"" + "forma" + "\"" +" onchange=" + "\"" + "location = this.options[this.selectedIndex].value;" + "\"" + ">";
        selection =selection+options+"</select>";
        String form = "<form>" + selection + "</form>";
        divLanguage.append(form);
        System.out.println(form);

    }*/
 /*@Override
    public void createLangSelectBox(Element body, PageContentGenerator pageContentGenerator) throws Exception {
       Element divLanguage = body.getElementsByClass("langauge selection box").get(0);
        String options = "";
        String option = "";
        //String url=HtmlPage.LOCALHOST_URL;
        Integer index=0;
        for (String language : pageContentGenerator.getLanguages()) {
            if (languageMapper.containsKey(language)) {
                String languageDetail = languageMapper.get(language);
                String pair = pageContentGenerator.getLanguageInitpage(language);
                pair = getAlphabetFileName(pair, language);
                String url = LOCALHOST_URL + pair;
                if(index==0){
                   option = "\n" + "<option value=" + "\""+ "\"" + " selected=" + "\""+ "selected"+"\"" + languageDetail + "</option>" + "\n";
                   index++;
                 }
                option = "\n" + "<option value=" + "\""+url +"\"" +">"+languageDetail + "</option>" + "\n";
                options += option;
            }

        }

        //<form name="store" id="store" method="post" action="" id="FORM_ID" >
        //<select id="mySelect" onchange="myFunction()">
        String selection =  "\n" + "<select name=" + "\"" + "forma" + "\"" +" onchange=" + "\"" + "location = this.options[this.selectedIndex].value;" + "\"" + ">";
        selection =selection+options+"</select>";
        String form = "<form>" + selection + "</form>";
        divLanguage.append(form);
        System.out.println(form);

    }*/

    
}
