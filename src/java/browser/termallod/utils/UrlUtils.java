/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.utils;

import browser.termallod.constants.FileAndCategory;
import browser.termallod.constants.Languages;
import browser.termallod.core.LanguageAlphabetPro;
import browser.termallod.core.api.LanguageManager;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;


/**
 *
 * @author elahi
 */
public class UrlUtils implements FileAndCategory,Languages {

    public static String encode(String input_text) {
        return URLUTF8Encoder.encode(input_text);
    }

    public static String decode(String encoded_text) {
        return UTF8URL.unescape(encoded_text);
    }

    public static void main(String[] args) throws IOException, Exception {
        //System.out.println(encode("¥"));
        //System.out.println(decode("%c2%a5"));
        String encodeText = getEncodedUrl("¥_¥");
        String decodeTest = getDecodedUrl(encodeText);
        System.out.println(encodeText);
        System.out.println(decodeTest);

        LanguageManager languageManager = new LanguageAlphabetPro(LANGUAGE_CONFIG_FILE);
        TreeMap<String, String> langBox = new TreeMap<String, String>();

        for (String langCode : languageManager.getLangAlphabetPairSorted().keySet()) {
            List<String> pairs = languageManager.getLangAlphabetPairSorted(langCode);
            String pair = pairs.get(0);
            String pairEncode = getEncodedUrl(pair);
            String pairDecode = getDecodedUrl(pairEncode);
            String Language = languageMapper.get(langCode);
            String browserUrl = "browser" + "_" + langCode + "_" + pairEncode + "_" + "1" + ".html";
            browserUrl = "<td><a href=" + "\"" + browserUrl + "\"" + ">" + Language + "</a></td>";
            langBox.put(Language, browserUrl);

            //String urlDe="browser"+"_"+langCode+"_"+pairDecode+"_"+"1"+".html";
            // System.out.println("encode:"+urlDe);
        }
        Integer index = 0;
        Integer groupIndex=0;
        String str="";
        String groupStr="";
        for (String language : langBox.keySet()) {
            index++;
           
            String line = langBox.get(language) +  "\n"+"<br>";
            str+=line;
            if (index >= 7) {
              groupStr+=" <tr>"+ str+"</tr>";
              groupIndex++;
              index=0;
               str="";
               
            }
           

            //String urlDe="browser"+"_"+langCode+"_"+pairDecode+"_"+"1"+".html";
            // System.out.println("encode:"+urlDe);
        }
       groupStr="<tbody>"+groupStr+" </tbody>";
        System.out.println(groupStr+ "\n");

       

    }

    public static String getEncodedUrl(String pair) {
        if (!pair.contains("_")) {
            return encode(pair);
        } else {
            String[] info = pair.split("_");
            pair = encode(info[0]) + "_" + encode(info[1]);
        }

        return pair;
    }

    public static String getDecodedUrl(String pair) {
        String[] info = pair.split("_");
        return decode(info[0]) + "_" + decode(info[1]);
    }

}
