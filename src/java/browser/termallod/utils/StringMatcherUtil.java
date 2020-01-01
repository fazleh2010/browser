/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.utils;

/**
 *
 * @author elahi
 */
public class StringMatcherUtil {

    public static String encripted(String term) {
        return term.replaceAll("\\s+", "_");
    }

    public static String decripted(String term) {
        return term.replaceAll("\\_", " ");
    }

}
