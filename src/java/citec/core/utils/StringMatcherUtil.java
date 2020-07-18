/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.core.utils;

import static citec.core.termbase.TermInfo.HASH_SYMBOLE;

/**
 *
 * @author elahi
 */
public class StringMatcherUtil {

    private static Integer orginalIndex = 0;
    private static Integer alternativeIndex = 1;
    
    public static void main(String args []){
        String language=getLanguage("http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_atc/data/atc/fatigue-EN");
        System.out.println(language);
    }

    public static String encripted(String term) {
        return term.replaceAll("\\s+", "_");
    }

    public static String decripted(String term) {
        return term.replaceAll("\\_", " ");
    }

    public static String getUrl(String checkField) {
        checkField = checkField.substring(0, checkField.lastIndexOf('#'));
        return checkField;
    }

    public static String getAlternativeUrl(String value, Boolean alternativeUrlFlag) {
        if (value.contains("=")) {
            String[] urls = value.split("=");
            String orgUrl = urls[orginalIndex];
            String alterUrl = urls[alternativeIndex];
            if (alternativeUrlFlag) {
                value = alterUrl;
            } else {
                value = orgUrl;
            }
        }
        return value;
    }

    public static String getLanguage(String subject) {
        int index = subject.lastIndexOf('/');
        String lastString = subject.substring(index + 1);
        
         boolean isSubjectFound = lastString.indexOf("-") != -1 ? true : false;
        if (isSubjectFound) {
            String[] info = lastString.split("-");
            return info[1].toLowerCase();
        }
       return null;
    }

}
