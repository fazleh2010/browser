/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod;

import browser.termallod.datamanager.DataManager;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import browser.termallod.api.PopulateListofTerms;
import browser.termallod.datamanager.LanguageTermPage;
import browser.termallod.datamanager.Ntriple;
import browser.termallod.utils.FileRelatedUtils;
import browser.termallod.utils.Partition;

/**
 *
 * @author elahi
 */
public class BrowserHtml implements Constants {

    private static String PATH = "src/java/tbx2rdf/utils/virtuoso/";
    private static File inputFile = new File(PATH + "listOfTermsFinal.html");
    private static File configFile = new File(PATH + "/data/" + "language.conf");
    private static File termFile = new File(PATH + "/data/" + "AB_1.txt");
    private static File outputFile = new File(PATH + "output.html");
    private static String TermPathSetB = PATH + "/data/setB/";

    public static void main(String[] args) {
        File[] categories = FileRelatedUtils.getFiles(TermPathSetB, ".ntriple");
        for (File category : categories) {
            Ntriple ntriple = new Ntriple(TermPathSetB + category.getName());
            DataManager dataManager = new DataManager(configFile, ntriple.getLangSortedTerms());
            for (String language : dataManager.getLanguages()) {
                //String alphabet = dataManager.getAlphabets(language)[0];
                LanguageTermPage languageTermPage=dataManager.getLangTerms(language);
                Partition partition=languageTermPage.getPartition();
                for(Integer page=0;page<partition.size();page++){
                    File outputFile = new File(PATH + "/html/" + page +category.getName().replace(".ntriple", "") +"_"+ language+"_" + ".html");
                    List<String> terms=partition.get(page);
                    HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(inputFile);
                    Document oldDocument = htmlReaderWriter.getInputDocument();
                    HtmlModify modifyHtml = new HtmlModify(oldDocument, eng, dataManager, terms);
                    htmlReaderWriter.writeHtml(modifyHtml.getNewDocument(), outputFile);
                }
 
            }

           
        }

        /*String alphabet = dataManager.getAlphabets(eng)[0];
        TreeMap<Integer, List<String>> pageTerms = dataManager.getLangTerms(eng);
        for (Integer page : pageTerms.keySet()) {
            File outputFile = new File(PATH + "/html/" + "output" + "_" + page + ".html");
            List<String> terms = pageTerms.get(page);
            HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(inputFile);
            Document oldDocument = htmlReaderWriter.getInputDocument();
            HtmlModify modifyHtml = new HtmlModify(oldDocument, eng, dataManager, terms);
            htmlReaderWriter.writeHtml(modifyHtml.getNewDocument(), outputFile);
        }*/
    }
    
                  
                
                    /*System.out.println(page);
                    TreeSet<String> terms = pageTerms.get(page);
                    System.out.println(terms);*/
                    /*File outputFile = new File(PATH + "/html/" + "output" + "_" + page + ".html");
                   
                    HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter(inputFile);
                    Document oldDocument = htmlReaderWriter.getInputDocument();
                    HtmlModify modifyHtml = new HtmlModify(oldDocument, eng, dataManager, terms);
                    htmlReaderWriter.writeHtml(modifyHtml.getNewDocument(), outputFile);*/
                  
                

}
