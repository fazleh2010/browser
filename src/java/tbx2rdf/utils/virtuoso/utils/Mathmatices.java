/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tbx2rdf.utils.virtuoso.utils;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 * @author elahi
 */
public class Mathmatices {

    public static boolean isOdd(String alphebets[]) {
        int x;

        return ifOdd(alphebets.length);
    }

    /*public static List<Object> getNumberofPages(List<String> list, Integer numberElementsEachPage) {
        double value = list.size() / numberElementsEachPage;
        Integer numberOfPages= (int)Math.ceil(value);
        TreeMap<Integer, List<String>> pageTerms=new TreeMap<Integer, List<String>>();
        List<Object> firstNElementsList = list.stream().limit(numberElementsEachPage).collect(Collectors.toList());
        return firstNElementsList;
    }*/

    private static boolean ifOdd(Integer length) {
        int x;
        x = length;
        if (x % 2 == 0) {
            return false;
        } else {
            return true;
        }
    }

}
