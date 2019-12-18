/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tbx2rdf.utils.virtuoso.datamanager;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import tbx2rdf.utils.virtuoso.Constants;
import tbx2rdf.utils.virtuoso.utils.Mathmatices;

/**
 *
 * @author elahi
 */
public class Language implements Constants {

    private HashMap<String, String[]> langAlphabet = new HashMap<String, String[]>();
    private String SLASH_SEPERATOR= "-" ;

    public Language(File file) {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(file));
            Set<String> keys = props.stringPropertyNames();
            for (String key : keys) {
                String[] pickedAlphebets = getAlphebets(props.getProperty(key));
                langAlphabet.put(key, pickedAlphebets);
            }

        } catch (Exception ex) {
            Logger.getLogger(Language.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String[] getAlphebets(String input) {
        Integer index = 0;
        String[] alphebets = input.split(" ");
        String[] pickedNumbers = new String[alphebets.length / 2];
        if (alphebets.length > 1) {
            for (int i = 1; i < alphebets.length; i = i + 2) {
                pickedNumbers[i / 2] = alphebets[i - 1] + SLASH_SEPERATOR + alphebets[i];
                index++;
            }
        }
        if (Mathmatices.isOdd(alphebets)) {
            pickedNumbers[pickedNumbers.length - 1] = alphebets[alphebets.length - 1];
        }
        return pickedNumbers;
    }


    private void display(String alphebets[]) {
        for (String element : alphebets) {
            System.out.println(element);
        }
    }

    public String[] getLangAlphabet(String language) {
        if (langAlphabet.containsKey(language)) {
            return langAlphabet.get(language);
        }
        return null;
    }

}
