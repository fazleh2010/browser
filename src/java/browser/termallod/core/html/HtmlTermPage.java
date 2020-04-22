/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.html;

import static browser.termallod.api.HtmlStringConts.divClassEnd;
import static browser.termallod.api.HtmlStringConts.divClassStr;
import static browser.termallod.api.IATE.SUBJECT_FIELD;
import browser.termallod.constants.FileAndLocationConst;
import static browser.termallod.constants.Languages.languageMapper;
import static browser.termallod.core.html.HtmlListOfTerms.termAlterUrl;
import browser.termallod.core.matching.MatchingTerminologies;
import browser.termallod.core.term.TermDetail;
import browser.termallod.core.term.TermInfo;
import browser.termallod.utils.StringMatcherUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author elahi
 */
public class HtmlTermPage extends HtmlPageAbstract {

    private Document templateHtml;
    private TermDetail termDetail;
    private String url;
    private String termFileName;

    public HtmlTermPage(HtmlParameters htmlCreateParameters, OntologyInfo info, HtmlReaderWriter htmlReaderWriter, FileAndLocationConst constants) {
        super(htmlCreateParameters, info, htmlReaderWriter, constants);
    }

    public void test(Document templateHtml, TermDetail termDetail, String url, String termFileName) {
        this.templateHtml = templateHtml;
        this.termDetail = termDetail;
        this.url = url;
        this.termFileName = termFileName;
        try {
            this.createTermPage();
        } catch (Exception ex) {
            Logger.getLogger(HtmlTermPage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public TermDetail createTerms(TermDetail termDetail, Integer index, File htmlFileName) throws Exception {
        Document generatedHtmlPage = null;

        Document termTemplate = info.getTermPageTemplate(constants.TEMPLATE_LOCATION, ".html");
        if (htmlCreateParameters.getAlternativeFlag()) {
             String url = htmlFileName.getName().replace(".html", "");
             url = url + "_" + "term" + "_" + index + ".html";
             termDetail.setAlternativeUrl(url);
            

            if (htmlCreateParameters.getTermPageFlag()) {
                String term = termDetail.getTerm();
                String urls = info.getAlphabetTermPage().getProps().getProperty(term);
                url = StringMatcherUtil.getAlternativeUrl(urls, htmlCreateParameters.getAlternativeFlag());
                
                File TermhtmlFileName = new File(constants.getBASE_PATH() + info.getOntologyFileName() + "/" + url);
                test(termTemplate, termDetail, url, url);
                generatedHtmlPage = this.getTemplateHtml();
                super.htmlReaderWriter.writeHtml(generatedHtmlPage, TermhtmlFileName);
            }
        }

        return termDetail;
    }

    public String getTermLi(TermDetail termDetail) {
        String term = termDetail.getTerm();
        String title = "title=" + '"' + term + " definition" + '"';
        String url = super.info.getAlphabetTermPage().getProps().getProperty(term);
        String alterUrl = termDetail.getAlternativeUrl();
        String assignUrl = null;

        if (!htmlCreateParameters.getAlternativeFlag()) {
            assignUrl = url;
        } else {
            assignUrl = alterUrl;
        }

        term = termDetail.getTermModified();
        String a = "<a href=" + assignUrl + " " + title + ">" + term + "</a>";
        String li = "\n<li>" + a + "</li>\n";
        termAlterUrl.put(termDetail.getTerm(), url + "=" + alterUrl);

        return li;
    }

    private void createTermPage() throws Exception {
        String langDetail = languageMapper.get(info.getLanguage());
        Element body = templateHtml.body();
        Element divTerm = body.getElementsByClass("webtop-g").get(0);
        String term = termDetail.getTermModified();

        //<a class="academic" href="https://www.oxfordlearnersdictionaries.com/wordlist/english/academic/">
        String classStr = "<a class=" + "\"" + "academic" + "\"" + " href=" + "\"" + "https://www.oxfordlearnersdictionaries.com/wordlist/english/academic/" + "\"" + ">";
        //</a><span class="z"> </span>
        String spanStr = "</a><span class=" + "\"" + "z" + "\"" + "> </span>";
        //<h2 class="h">abandon</h2>
        String wordStr = "<h2 class=" + "\"" + "h" + "\"" + ">" + term + "</h2>";
        //<span class="z"> </span>
        String extraStr = "<span class=" + "\"" + "z" + "\"" + ">" + "</span>";

        String str = classStr + spanStr + wordStr + extraStr; //language;//+titleStr+langStr;
        divTerm.append(str);

        Element divLang = body.getElementsByClass("top-g").get(0);
        String langDiv = "<span class=" + "\"" + "collapse" + "\"" + " title=" + "\"" + langDetail + "\"" + ">";
        langDiv += "<span class=" + "\"" + "heading" + "\"" + ">" + langDetail + "</span></span>";
        divLang.append(langDiv);

        List<String> divStrS = new ArrayList<String>();
        List<TermDetail> matchedTerms = MatchingTerminologies.getTermDetails(info.getLanguage(), termDetail.getTerm());

        divStrS = createTermInfo(matchedTerms, term, url);

        if (!divStrS.isEmpty()) {
            Integer index = 0;
            List<Element> divTerms = body.getElementsByClass("panel panel-default");
            for (Element divLinkTerm : divTerms) {
                String divStr = divStrS.get(index);
                divLinkTerm.append(divStr);
                index++;
                if (index == divStrS.size()) {
                    break;
                }

            }
        }

    }

    private List<String> createTermInfo(List<TermDetail> matchedTerms, String term, String url) throws Exception {
        List<String> divStrS = new ArrayList<String>();
        String subjectFieldTr = "", ReferenceTr = "", languageTr = "", reliabilityCodeTr = "", administrativeTr = "", subjectID = "";;
        String posTr = "", numberTr = "", genderTr = "", definitionTr = "", hypernymTr = "", hyponymTr = "", variantTr = "", synonymTr = "";

        TermInfo termInfo = this.getTermInformation(url);
        /*if (termInfo != null) {
           // System.out.println(term + " " + termFileName + " " + termInfo.getSubjectId() + termInfo.getTermID());
        }*/

        String langValueStr = languageMapper.get(info.getLanguage());
        languageTr = getTr(getProperty("Language"), getValueNew(langValueStr));

        if (termInfo != null) {

            if (termInfo.getReliabilityCode() != null) {
                reliabilityCodeTr = getTr(getProperty("Reliability Code:"), getValueNew(termInfo.getReliabilityCode()));
            }
            if (termInfo.getAdministrativeStatus() != null) {
                administrativeTr = getTr(getProperty("Administrative Status:"), getValueNew(termInfo.getAdministrativeStatus()));
            }
            if (termInfo.getSubjectId() != null) {
                String subjectFieldPro = " " + SUBJECT_FIELD + ":";
                if (termInfo.getSubjectId().length() != 0) {
                    subjectID = "(" + termInfo.getSubjectId() + ")";
                } else {
                    subjectID = "";
                }
                subjectFieldTr = getTr(getProperty(subjectFieldPro), getValueNew(subjectID + termInfo.getSubjectDescription()));
            }
            if (termInfo.getTermID() != null) {
                ReferenceTr = getTr(getProperty("Reference:"), getValueNew(termInfo.getTermID()));
            }
            if (termInfo.getPOST() != null) {
                posTr = getTr(getProperty("POS:"), getValueNew(termInfo.getPOST()));
            }
            if (termInfo.getNumber() != null) {
                numberTr = getTr(getProperty("Number:"), getValueNew(termInfo.getNumber()));
            }
            if (termInfo.getGender() != null) {
                genderTr = getTr(getProperty("Gender:"), getValueNew(termInfo.getGender()));
            }
            if (termInfo.getDefinition() != null) {
                definitionTr = getTr(getProperty("Definition:"), getValueNew(termInfo.getDefinition()));
            }
            if (termInfo.getHypernym() != null) {
                hypernymTr = getTr(getProperty("Hypernym:"), getValueNew(termInfo.getHypernym()));
            }
            if (termInfo.getHyponym() != null) {
                hyponymTr = getTr(getProperty("Hyponym:"), getValueNew(termInfo.getHyponym()));
            }
            if (termInfo.getVariant() != null) {
                variantTr = getTr(getProperty("Variant:"), getValueNew(termInfo.getVariant()));
            }
            if (termInfo.getSynonym() != null) {
                synonymTr = getTr(getProperty("Synonym:"), getValueNew(termInfo.getSynonym()));
            }
        }

        String table = this.getTable(this.getTbody(languageTr + definitionTr + reliabilityCodeTr + administrativeTr + subjectFieldTr + ReferenceTr
                + posTr + numberTr + genderTr + hypernymTr + hyponymTr + variantTr + synonymTr));
        String divStr = table;
        divStrS.add(divStr);

        divStrS = this.generateTermLink(matchedTerms, divStrS);

        return divStrS;
    }
    
     private List<String> generateTermLink(List<TermDetail> matchedTerms, List<String> divStrS) {

        for (TermDetail termDetail : matchedTerms) {
            String otherTerminology = termDetail.getOtherCategory(info.getCategoryType());
           
            //String langValueStr = languageMapper.get(language);
            String spanTerminologyName = otherTerminology;
            String spanTerminologyUrl = constants.BROWSER_URL.get(spanTerminologyName);

            String term = termDetail.getTerm();
            String url = null;
            if (!super.htmlCreateParameters.getAlternativeFlag()) {
                url = termDetail.getUrl(otherTerminology);
            } else {
                url = termDetail.getAlternativeUrl(otherTerminology);
                if (constants.CATEGORY_TERM_URL.containsKey(otherTerminology)) {
                    url = constants.CATEGORY_TERM_URL.get(otherTerminology) + url;
                }
            }
            
            System.out.println(term+" "+url+" "+otherTerminology+" "+spanTerminologyUrl+" Iate:"+termFileName);

            //temporary closed
            String panelHeadingStart = divClassStr + this.getWithinQuote("panel-heading") + ">"
                    + "<a href=" + this.getWithinQuote(url) + " class="
                    + this.getWithinQuote("rdf_link") + ">" + term + "</a>" + divClassEnd;

            panelHeadingStart = "<h3>Links to other terminologies</h3>";
            String panelHeadingEnd = "</div>";
            String thirdTr = getTr(getProperty(spanTerminologyUrl, otherTerminology), getValue(url, url, term));
            //firstTr = "";

            /*String termValue = this.getValueNew(this.getSpanProp(spanPropUrl1, spanPropUrl2, spanPropStr) + this.getSpanValue(spanTerminologyUrl, spanTerminologyName));
            String subjectFieldTr = getTr(getProperty(langTermUrl, langTermStr), termValue);
            subjectFieldTr = "";

            String languageTr = getTr(getProperty(matchPropUrl, matchPropStr), getValueNew(matchValueUrl1, matchValueUrl2, matchValueStr));
            languageTr = "";*/
            String table = this.getTable(this.getTbody(thirdTr));

            /*String yesNoButtonDiv = getAcceptDenyButton();
            //yes no button is closed for time being.
            yesNoButtonDiv = "";*/
            String divStr = panelHeadingStart + table + panelHeadingEnd;
            divStrS.add(divStr);

        }
        return divStrS;
    }

    private TermInfo getTermInformation(String termUrl) {
        return super.info.getAlphabetTermPage().getTermInfo(termUrl);

    }

    /*private TermInfo getTermInformation(String termUrl) {
        UrlMatching urlMatching = new UrlMatching(merging, termUrl);
        return urlMatching.getTermInfo();
    }*/
    private String getAcceptDenyButton() {
        String yesNoButtonDiv
                = "<div class=" + this.getWithinQuote("w3-container") + ">"
                + "<div class=" + this.getWithinQuote("w3-container w3-center") + ">"
                + "<div class=" + this.getWithinQuote("w3-section") + ">"
                + "<button class=" + this.getWithinQuote("w3-button w3-green") + ">Accept</button>"
                + "<button class=" + this.getWithinQuote("w3-button w3-red") + ">Decline</button>"
                + "</div>"
                + " </div>"
                + " </div>";
        return yesNoButtonDiv;
    }

    private String getValueNew(String str) {
        String tdEnd = "</td>";
        String tdRdfValueStart = "<td" + ">";
        String langValue = tdRdfValueStart + str + "</a>" + tdEnd;
        return langValue;
    }

    private String getProperty(String str) {
        String tdPropStart = "<td " + ">";
        String tdEnd = "</td>";
        String langProp = tdPropStart + str + "</a>" + tdEnd;
        return langProp;
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

    private Document getTermLinkPageTemplate(String extension) {
        File templateFile = new File(super.constants.TEMPLATE_LOCATION + info.getOntologyFileName() + "_" + info.getLanguage() + "_" + "term" + "_" + "add" + extension);
        HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(templateFile);
        return htmlReaderWriter.getInputDocument();

    }

    public Document getTemplateHtml() {
        return templateHtml;
    }

   

}
