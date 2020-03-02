/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.matching;

import static browser.termallod.constants.FileAndCategory.IATE;
import browser.termallod.utils.StringMatcherUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class TermDetail {

    private final String term ;
    private final String termModified;
    private final String category;
    private final String langCode;
    private Map<String,String> termLinks = new HashMap<String,String>();
    private Boolean alternativeFlag;
    private String alternativeUrl = null;
    private  String url;

     public TermDetail(String category,String langCode, String term) {
        this.category = category;
        this.langCode = langCode;
        this.term = term;
        this.termModified = StringMatcherUtil.decripted(term);
    }
    
     public TermDetail(String category,String langCode, String term,String url) {
         this(category,langCode,term);
         this.url=url;
    }
     
    public TermDetail(String category,String langCode, String term, String url, String alternativeUrl) {
        this(category,langCode, term, url);
        this.alternativeUrl=alternativeUrl;
    }


    public TermDetail(String category,String langCode, String term, String url, String givenCategory,String givenUrl) {
        this(category,langCode,term,url,url);
        this.termLinks.put(category, url);
        this.termLinks.put(givenCategory, givenUrl);
    }
   

   

   
    
    /*public TermDetail(String categoryName,String langCode, String term,String url, String alternativeUrl) {
        this(categoryName,langCode,term);
        this.url = url;
        this.alternativeUrl = alternativeUrl;
    }*/
    public TermDetail(TermDetail termDetail,String url,String alternativeUrl) {
        this(termDetail.getCategory(),termDetail.getLangCode(),termDetail.getTerm(),url,alternativeUrl);
    }

    public String getTerm() {
        return term;
    }
    
     public void setAlternativeUrl(String alternativeUrl) {
        this.alternativeUrl = alternativeUrl;
    }
    
    public String getLangCode() {
        return langCode;
    }


    public String getCategory() {
        return category;
    }

    public String getUrl() {
        return url;
    }

    public String getAlternativeUrl() {
        return alternativeUrl;
    }

    public String getTermModified() {
        return termModified;
    }

    public String getTermLinks(String givenCategory) {
        for(String categoryName:termLinks.keySet()){
            if(!categoryName.equals(givenCategory)){
                return termLinks.get(categoryName);
            }
        }
        return termLinks.get(this.category);
    }

    @Override
    public String toString() {
        return "TermDetail{" + "term=" + term + ", termModified=" + termModified + ", category=" + category + ", langCode=" + langCode + ", termLinks=" + termLinks + ", alternativeFlag=" + alternativeFlag + ", alternativeUrl=" + alternativeUrl + ", url=" + url + '}';
    }

    public String getOtherCategory(String categoryType) {
       for(String categoryName:termLinks.keySet()){
            if(!categoryName.equals(this.category)){
                return categoryName;
            }
        }
        return this.category;
    }

    public String getUrl(String otherTerminology) {
       return this.termLinks.get(otherTerminology);
    }

    public String getAlternativeUrl(String otherTerminology) {
         return this.termLinks.get(otherTerminology);
    }

}
