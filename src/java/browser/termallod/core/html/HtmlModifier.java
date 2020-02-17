/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.html;

import browser.termallod.api.HtmlStringConts;
import static browser.termallod.constants.FileAndCategory.CATEGORY_ONTOLOGIES;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import browser.termallod.constants.HtmlPage;
import browser.termallod.constants.Languages;
import browser.termallod.core.AlphabetTermPage;
import browser.termallod.core.PageContentGenerator;
import browser.termallod.core.matching.TermDetail;
import browser.termallod.utils.StringMatcherUtil;

/**
 *
 * @author elahi
 */
public class HtmlModifier implements HtmlPage, Languages, HtmlStringConts {

    private Document generatedHtmlPage;
    private Integer currentPageNumber;
    private Integer maximumNumberOfPages = 4;
    private String language;
    private String ontologyFileName;
    private String categoryType = "";
    private File htmlFileName = null;
    private AlphabetTermPage alphabetTermPage;

    public HtmlModifier(String PATH, Document templateHtml, String language, AlphabetTermPage alphabetTermPage, List<String> terms, String categoryName, PageContentGenerator pageContentGenerator, Integer currentPageNumber) throws Exception {
        this.currentPageNumber = currentPageNumber;
        this.language = language;
        this.ontologyFileName = categoryName;
        this.alphabetTermPage = alphabetTermPage;
        String[] ontology = ontologyFileName.split("_");
        this.categoryType = ontology[1];
        this.generatedHtmlPage = this.generateHtmlFromTemplate(templateHtml, terms, pageContentGenerator, alphabetTermPage);
        this.htmlFileName = new File(PATH + this.ontologyFileName + "/" + createFileNameUnicode(language, currentPageNumber));
    }

    public HtmlModifier(String PATH, Document templateHtml, TermDetail termDetail, String categoryName) throws Exception {
        this.language = termDetail.getLangCode();
        this.ontologyFileName = CATEGORY_ONTOLOGIES.get(categoryName);
        this.generatedHtmlPage = this.generateHtmlFromTemplate(templateHtml, termDetail.getTerm());
        System.out.println(generatedHtmlPage.toString());
        this.htmlFileName = new File(PATH + this.ontologyFileName + "/" + termDetail.getTerm() + ".html");
    }

    public HtmlModifier(String PATH, Document templateHtml, TermDetail givenTermDetail, List<TermDetail> termDetails, String category) throws Exception {
        this.language = givenTermDetail.getLangCode();
        this.ontologyFileName = CATEGORY_ONTOLOGIES.get(category);
        this.generatedHtmlPage = this.generateHtmlFromTemplate(templateHtml, termDetails);
        this.htmlFileName = new File(PATH + this.ontologyFileName + "/" + givenTermDetail.getTerm() + "_add" + ".html");
    }

    private Document generateHtmlFromTemplate(Document templateHtml, List<String> terms, PageContentGenerator pageContentGenerator, AlphabetTermPage alphabetTermPage) throws Exception {
        Element body = templateHtml.body();
        String alphebetPair = alphabetTermPage.getAlpahbetPair();
        Integer numberofPages = alphabetTermPage.getNumberOfPages();
        //currently not
        Integer emptyTerm = alphabetTermPage.getEmptyTerm();
        //this part of code is used to automatically generated language selection box
        //currently it is hard coded in HTML template
        /*if (!this.categoryType.contains(iate)) {
            this.createLangSelectBox(body, pageContentGenerator);
        }*/

        createAlphabet(body, alphebetPair, pageContentGenerator);
        createTerms(body, terms, alphebetPair, emptyTerm, alphabetTermPage);

        /*Element divCurrentPageUpper = body.getElementsByClass("activepageUpper").get(0);
        this.assignCurrentPageNumber(divCurrentPageUpper);
         Element divCurrentPageLower = body.getElementsByClass("activepageLower");
        this.assignCurrentPageNumber(divCurrentPageLower);*/
        //createUperPageNumber(body, alphebetPair, numberofPages);
        //upper page number
        createPageNumber(body, "paging_links inner", alphebetPair, numberofPages);
        //lower page number
        createPageNumber(body, "paging_links inner_down", alphebetPair, numberofPages);
        return templateHtml;
    }

    private Document generateHtmlFromTemplate(Document templateHtml, String term) throws Exception {

        String langDetail = languageMapper.get(language);
        Element body = templateHtml.body();
        Element divTerm = body.getElementsByClass("webtop-g").get(0);
        //<a class="academic" href="https://www.oxfordlearnersdictionaries.com/wordlist/english/academic/">
        String classStr = "<a class=" + "\"" + "academic" + "\"" + " href=" + "\"" + "https://www.oxfordlearnersdictionaries.com/wordlist/english/academic/" + "\"" + ">";
        //</a><span class="z"> </span>
        String spanStr = "</a><span class=" + "\"" + "z" + "\"" + "> </span>";
        //<h2 class="h">abandon</h2>
        String wordStr = "<h2 class=" + "\"" + "h" + "\"" + ">" + term + "</h2>";
        //<span class="z"> </span>
        String extraStr = "<span class=" + "\"" + "z" + "\"" + ">" + "</span>";

        String str = classStr + spanStr + wordStr + extraStr + language;//+titleStr+langStr;
        divTerm.append(str);

        Element divLang = body.getElementsByClass("top-g").get(0);
        String langDiv = "<span class=" + "\"" + "collapse" + "\"" + " title=" + "\"" + langDetail + "\"" + ">";
        langDiv += "<span class=" + "\"" + "heading" + "\"" + ">" + langDetail + "</span></span>";
        divLang.append(langDiv);

        Element multiLingualDiv = body.getElementsByClass("entry").get(0);
        //<li><a href="https://www.oxfordlearnersdictionaries.com/definition/english/abandon_1" title="abandon definition">abandon</a> </li>
        String title = "title=" + '"' + "term" + " definition" + '"';
        //real version
        //String url = this.path+"/"+DEFINITION+"/" +language+"/" +alphebetPair +"/" +term + "_1";
        String url = "http: term url";
        //String url = LOCALHOST_URL + "termDefination.php";
        //System.out.println(url);
        String a = "<a href=" + url + " " + title + ">" + term + "</a>";
        String li = "\n<li>" + a + "</li>\n";
        multiLingualDiv.append(li);

        //System.out.println(multiLingualDiv.toString());
        return templateHtml;
    }

    private Document generateHtmlFromTemplate(Document templateHtml, List<TermDetail> termDetail) throws Exception {

        String langDetail = languageMapper.get(language);
        String term = "term";
        
        
        String panelHeadingStart = divClassStr + this.getWithinQuote("panel-heading") + ">" + "<a href=" + this.getWithinQuote("/data/iate/test+tubes-en") + " class=" + this.getWithinQuote("rdf_link") + ">" + term + "</a>" + divClassEnd;
        String panelHeadingEnd="</div>";
        String firstTr = getTr(getProperty(langPropUrl, langPropStr), getValue(langValueUrl1, langValueUrl2, langValueStr));
        String termValue = this.getValue(this.getSpanProp(spanPropUrl1, spanPropUrl2, spanPropStr) + this.getSpanValue(spanValueUrl, spanValueStr));
        String secondTr = getTr(getProperty(langTermUrl, langTermStr), termValue);
        String thirdTr = getTr(getProperty(matchPropUrl, matchPropStr), getValue(matchValueUrl1, matchValueUrl2, matchValueStr));
        String table = this.getTable(this.getTbody(firstTr + secondTr + thirdTr));
        
         String yesNoButtonDiv= getAcceptDenyButton();
        
        
        String divStr=panelHeadingStart+table+ yesNoButtonDiv+panelHeadingEnd;
        
        Element body = templateHtml.body();
        List<Element> divTerms = body.getElementsByClass("panel panel-default");
        for(Element divTerm:divTerms){
            divTerm.append(divStr);
            System.out.println(divTerm);
        }
        /*Element divterm=divTerms.get(0);
        divterm.append(div);
        System.out.println(div);*/
        
       
             
        
       /*<div class="panel panel-default">
                
                <div class="w3-container">
                    <div class="w3-container w3-center">
                        <div class="w3-section">
                            <button class="w3-button w3-green">Accept</button>
                            <button class="w3-button w3-red">Decline</button>
                        </div>
                    </div>
                </div>                    
            </div>*/
        
        /*for (Element divterm : divTerms) {
             divterm.append(panelHeading);
        }*/

     
        /*<div class="panel-heading"><a href="/data/iate/test+tubes-en" class="rdf_link">test+tubes-en</a></div>
                <table class="panel-body rdf_embedded_table">
                    <tbody>
                        <tr>
                            <td class="rdf_prop">
                                <a href="http://www.w3.org/ns/lemon/ontolex#language" class="rdf_link">Language</a>
                            </td>
                            <td class="rdf_value rdf_first_value">
                                <a href="http://www.lexvo.org/page/iso639-3/eng" property="http://www.w3.org/ns/lemon/ontolex#language" class="rdf_link rdf_prop">Eng</a>
                            </td>
                        </tr>
                        <tr>
                            <td class="rdf_prop">
                                <a href="http://tbx2rdf.lider-project.eu/tbx#reliabilityCode" class="rdf_link">Terminology</a>
                            </td>
                            <td class="rdf_value rdf_first_value">
                                <span property="http://tbx2rdf.lider-project.eu/tbx#reliabilityCode" datatype="http://www.w3.org/2001/XMLSchema#integer">3</span>
                                <span class="pull-right rdf_datatype"><a href="http://www.w3.org/2001/XMLSchema#integer" class="rdf_link">iate</a></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="rdf_prop">
                                <a href="http://www.lexinfo.net/ontology/2.0/lexinfo#termType" class="rdf_link">Match type</a>
                            </td>
                            <td class="rdf_value rdf_first_value">
                                <a href="http://www.lexinfo.net/ontology/2.0/lexinfo#fullForm" property="http://www.lexinfo.net/ontology/2.0/lexinfo#termType" class="rdf_link rdf_prop">extact match</a>
                            </td>
                        </tr>
                    </tbody>
                </table> */
        return templateHtml;
    }

    private String getAcceptDenyButton() {
        String yesNoButtonDiv=
                "<div class="+this.getWithinQuote("w3-container")+">"
                +"<div class="+this.getWithinQuote("w3-container w3-center")+">"
                +"<div class="+this.getWithinQuote("w3-section")+">"
                +"<button class="+this.getWithinQuote("w3-button w3-green")+">Accept</button>"
                +"<button class="+this.getWithinQuote("w3-button w3-red")+">Decline</button>"
                +"</div>"
                +" </div>"
                +" </div>";
        return yesNoButtonDiv;
    }

    private String getValue(String url1, String url2, String str) {
        String tdEnd = "</td>";
        String tdRdfValueStart = "<td class=" + this.getWithinQuote("rdf_value rdf_first_value") + ">";
        String langValue = tdRdfValueStart + "<a href=" + this.getWithinQuote(url1) + " property=" + this.getWithinQuote(url2) + " class=" + this.getWithinQuote("rdf_link rdf_prop") + ">" + str + "</a>" + tdEnd;
        return langValue;
    }

    private String getProperty(String url, String str) {
        String tdPropStart = "<td class=" + this.getWithinQuote("rdf_prop") + ">";
        String tdEnd = "</td>";
        String langProp = tdPropStart + " <a href=" + this.getWithinQuote(url) + " class=" + this.getWithinQuote("rdf_link") + ">" + str + "</a>" + tdEnd;
        return langProp;
    }

    @Override
    public void createLangSelectBox(Element body, PageContentGenerator pageContentGenerator) throws Exception {
        Element divLanguage = body.getElementsByClass("langauge selection box").get(0);
        String options = "<ul class=" + "\"" + "language-list" + "\"" + ">";
        for (String languageCode : pageContentGenerator.getLanguages()) {
            if (languageMapper.containsKey(languageCode)) {
                String languageDetail = languageMapper.get(languageCode);
                String pair = pageContentGenerator.getLanguageInitpage(languageCode);
                String url = this.createUrlLink(languageCode, INITIAL_PAGE);
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
        List<AlphabetTermPage> alphabetPairs = pageContentGenerator.getLangPages(language);
        for (AlphabetTermPage alphabetTermPage : alphabetPairs) {
            if (!alphabetTermPage.getAlpahbetPair().contains(alphebetPair)) {
                String li = getAlphebetLi(INITIAL_PAGE, alphabetTermPage);
                divAlphabet.append(li);
            }

        }
        return alphebetPair;
    }

    @Override
    public void createPageNumber(Element body, String elementName, String alphebetPair, Integer numberofPages) {
        //Element divPageDown = body.getElementsByClass("paging_links inner_down").get(0);
        Element divPage = body.getElementsByClass(elementName).get(0);

        List<String> liS = getPageLi(alphebetPair, numberofPages, alphabetTermPage);
        if (liS.isEmpty()) {
            return;
        }
        for (String li : liS) {
            divPage.append(li);
        }
    }

    @Override
    public void createTerms(Element body, List<String> terms, String alphebetPair, Integer emptyTerm, AlphabetTermPage alphabetTermPage) {
        Element divTerm = body.getElementsByClass("result-list1 wordlist-oxford3000 list-plain").get(0);
        for (String term : terms) {
            String liString = getTermLi(alphebetPair, term, alphabetTermPage);
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

    private String getTermLi(String alphebetPair, String term, AlphabetTermPage alphabetTermPage) {
        //<li><a href="https://www.oxfordlearnersdictionaries.com/definition/english/abandon_1" title="abandon definition">abandon</a> </li>
        String title = "title=" + '"' + term + " definition" + '"';
        //real version
        //String url = this.path+"/"+DEFINITION+"/" +language+"/" +alphebetPair +"/" +term + "_1";
        String url = generateTermUrl(term, alphabetTermPage);
        term = StringMatcherUtil.decripted(term);
        System.out.println(term + "..." + url);
        //String url = LOCALHOST_URL + "termDefination.php";
        //System.out.println(url);
        String a = "<a href=" + url + " " + title + ">" + term + "</a>";
        String li = "\n<li>" + a + "</li>\n";
        return li;
    }

    private String getAlphebetLi(Integer pageNumber, AlphabetTermPage alphabetTermPage) {
        //there is an error in Hungarian langauge link in HTML template, 
        //since all static htmls are already generated so the problem is now solved by hardcoded.
        //extreme bad solution but quick solution. 

        //Elements divAlphabet = body.getElementsByClass("side-selector__left");
        //Element content = body.getElementById("entries-selector");
        /*String ontologyLocation="";
        if(categoryOntologyMapper.containsKey(this.categoryName)){
            ontologyLocation=categoryOntologyMapper.get(categoryName);
        }*/
        String url = this.createUrlLink(language, pageNumber, alphabetTermPage);
        //String url = LOCALHOST_URL_LIST_OF_TERMS_PAGE + alphabetFileName;

        if (language.contains("hu") && alphabetTermPage.getNumericalValueOfPair() == 1) {
            url = "browser_hu_A_1_1.html";
        }

        String a = "<a href=" + url + ">" + alphabetTermPage.getAlpahbetPair() + "</a>";
        String li = "\n" + "<li>" + a + "</li>" + "\n";
        return li;
    }

    private List<String> getPageLi(String pair, Integer pages, AlphabetTermPage alphabetTermPage) {
        //Elements divAlphabet = body.getElementsByClass("side-selector__left");
        //Element content = body.getElementById("entries-selector");
        List<String> liS = new ArrayList<String>();
        String pageUrl = null;
        String li = "";
        /*"<span>" + this.currentPageNumber + "</span>";
        liS.add(li);*/

        //there is an error in Hungarian langauge link in HTML template, 
        //since all static htmls are already generated so the problem is now solved by hardcoded.
        //extreme bad solution but quick solution. 
        if (pages == 1) {
            return new ArrayList<String>();
        }
        if (this.currentPageNumber > INITIAL_PAGE) {
            pageUrl = createUrlLink(language, currentPageNumber - 1);
            if (language.contains("hu") && this.currentPageNumber == 2) {
                pageUrl = "browser_hu_A_1_1.html";
            }
            String a = "<a href=" + pageUrl + ">" + "Previous" + "</a>";
            li = "\n<li>" + a + "</li>\n";
            liS.add(li);
        }
        Integer index = 0;
        for (Integer page = this.currentPageNumber; page < pages; page++) {
            Integer pageNumber = (page + 1);
            pageUrl = createUrlLink(language, pageNumber);
            if (language.contains("hu") && pageNumber == 1) {
                pageUrl = "browser_hu_A_1_1.html";
            }

            String a = "<a href=" + pageUrl + ">" + pageNumber + "</a>";
            li = "\n<li>" + a + "</li>\n";
            liS.add(li);
            if (index > this.maximumNumberOfPages && (pageNumber + 1) < pages) {
                pageUrl = createUrlLink(language, pageNumber + 1);
                a = "<a href=" + pageUrl + ">" + "Next" + "</a>";
                li = "\n<li>" + a + "</li>\n";
                liS.add(li);
                break;
            }

            index++;

        }

        return liS;
    }

    private String createUrlLink(String languageCode, Integer pageNumber) {
        return LOCALHOST_URL_LIST_OF_TERMS_PAGE + this.createFileNameUnicode(languageCode, pageNumber);
    }

    private String createUrlLink(String languageCode, Integer pageNumber, AlphabetTermPage alphabetTermPage) {
        return LOCALHOST_URL_LIST_OF_TERMS_PAGE + this.createFileNameWithPairNumber(languageCode, pageNumber, alphabetTermPage);
    }

    /*private String createFileNameUnicode(String languageCode, Integer pageNumber) {
        String pair=alphabetTermPage.getAlpahbetPair();
        pair = UrlUtils.getEncodedUrl(pair);
        return browser + UNDERSCORE + languageCode + UNDERSCORE + pair + UNDERSCORE + pageNumber + HTML_EXTENSION;
    }*/
    private String createFileNameUnicode(String languageCode, Integer pageNumber) {
        String pair = getPairValue(alphabetTermPage);
        return browser + UNDERSCORE + languageCode + UNDERSCORE + pair.toString() + UNDERSCORE + pageNumber + HTML_EXTENSION;
    }

    private String createFileNameWithPairNumber(String languageCode, Integer pageNumber, AlphabetTermPage alphabetTermPage) {
        String pair = getPairValue(alphabetTermPage);
        return browser + UNDERSCORE + languageCode + UNDERSCORE + pair.toString() + UNDERSCORE + pageNumber + HTML_EXTENSION;
    }

    private String getPairValue(AlphabetTermPage alphabetTermPage) {
        String pair;
        if (language.equals("en")) {
            pair = alphabetTermPage.getAlpahbetPair();
        } else {
            pair = alphabetTermPage.getNumericalValueOfPair().toString();
        }
        return pair;
    }

    private void assignCurrentPageNumber(Element divCurrentPage) {
        divCurrentPage.append("<span>" + this.currentPageNumber + "</span> </li>");

    }

    private String assignHighLightPageNumber(String pageUrl) {
        String a = "<a href=" + pageUrl + ">" + "&gt;" + "</a>";
        return "\n<li>" + a + "</li>\n";

    }

    /*private String generateTermUrl(String term) {
        String outputfileString = LOCALHOST_URL_LIST_OF_TERMS_PAGE + this.generateTermFileName() + this.termFileExtension(term);
        return outputfileString;
    }*/
    private String generateTermFileName() {
        String outputfileString = ontologyFileName + "/" + "data" + "/" + this.categoryType + "/";
        return outputfileString;
    }

    private String termFileExtension(String term) {
        return term.trim().replace(" ", "+") + "-" + language.toUpperCase() + HTML_EXTENSION;
    }

    private String generateTermUrl(String term, AlphabetTermPage alphabetTermPage) {
        return alphabetTermPage.getUrl(term);
    }

    /*private String termFileLocation(String term) {
        return PATH + this.generateTermFileName() + this.termFileExtension(term);
    }*/
    public Document getGeneratedHtmlPage() {
        return generatedHtmlPage;
    }

    public File getHtmlFileName() {
        return htmlFileName;
    }

    private String getWithinQuote(String term) {
        return "\"" + term + "\"";
    }

    private String getTr(String langProp, String langValue) {
        String trStart = " <tr>";
        String trEnd = "</tr>";
        return trStart + langProp + langValue + trEnd;
    }

    private String getSpanProp(String url1, String url2, String str) {
        return "<span property=" + this.getWithinQuote(url1) + " datatype=" + this.getWithinQuote(url2) + ">" + str + "</span>";
    }

    private String getSpanValue(String url, String str) {
        return "<span class=" + this.getWithinQuote("pull-right rdf_datatype") + "><a href=" + this.getWithinQuote(url) + " class=" + this.getWithinQuote("rdf_link") + ">" + str + "</a></span>";
    }

    private String getValue(String string) {
        String tdEnd = "</td>";
        String tdRdfValueStart = "<td class=" + this.getWithinQuote("rdf_value rdf_first_value") + ">";
        return tdRdfValueStart + string + tdEnd;

    }

    private String getTbody(String trs) {
        String tbodyStart = "<tbody>";
        String tbodyEnd = "</tbody>";
        return tbodyStart + trs + tbodyEnd;

    }

    private String getTable(String tbody) {
        String tableStart = "<table class=" + this.getWithinQuote("panel-body rdf_embedded_table") + ">";
        String tableEnd = "</table>";
        return tableStart + tbody + tableEnd;
    }

}
