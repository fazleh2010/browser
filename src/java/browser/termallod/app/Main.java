/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.app;

import browser.termallod.app.process.CreateAlphabetFiles;
import browser.termallod.core.html.HtmlCreator;
import browser.termallod.api.LanguageManager;
import browser.termallod.core.LanguageAlphabetPro;
import browser.termallod.utils.FileRelatedUtils;
import browser.termallod.core.sparql.CurlSparqlQuery;
import browser.termallod.core.sparql.SparqlEndpoint;
import browser.termallod.core.termbase.Termbase;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Mohammad Fazleh Elahi
 */
public class Main implements SparqlEndpoint {

    private static String BASE_PATH = "src/java/resources/data/";
    private static String OUTPUT_PATH = BASE_PATH + "/output/";
    private static String INPUT_PATH = BASE_PATH + "/input/";
    private static String CONFIG_PATH = BASE_PATH + "/conf/";
    private static String TEMPLATE_PATH = BASE_PATH + "/template/";
    private String htmlString = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "    <head>\n"
            + "        <title>Example</title>\n"
            + "    </head>\n"
            + "    <body>\n"
            + "        <p>This is an example of a simple HTML page with one paragraph.</p>\n"
            + "    </body>\n"
            + "</html>";

    private static LanguageManager languageInfo;

    public static void main(String[] args) throws Exception {

        String myTermTableName = "myTerminology", otherTermTableName = "otherTerminology", matchedTermTable = "link";
        String myTermSparqlEndpoint = null, list = null;
        String ListOfTermPage = "ListOfTerms", TermPage = "TermPage";

        System.out.println("called");
        System.out.println("arguments: " + args.length);
        if (args.length > 0) {
            myTermSparqlEndpoint = args[0];
            System.out.println("SparqlEndpoint: " + myTermSparqlEndpoint);
        } else {
            myTermSparqlEndpoint = endpoint_solar;
            System.err.println("no sparql endpoint in arguments");
        }
        if (args.length > 1) {
            OUTPUT_PATH = args[1];
            System.out.println("output folder: " + OUTPUT_PATH);
        } else {
            System.err.println("no output folder in arguments");

        }
        if (args.length > 2) {
            list = args[2];
            System.out.println("output folder: " + list);
        } else {
        }

        languageInfo = new LanguageAlphabetPro(new File(BASE_PATH + "/conf/" + "language.conf"));
        System.out.println("reading terms");
        Termbase myTerminology = new CurlSparqlQuery(myTermSparqlEndpoint, query_writtenRep, myTermTableName).getTermbase();
        System.out.println("saving terms");
        CreateAlphabetFiles alphabetFiles = new CreateAlphabetFiles(languageInfo, myTerminology);
        cleanDirectory();
        System.out.println("saving files");
        FileRelatedUtils.writeFile(alphabetFiles.getLangTerms(), INPUT_PATH);
        System.out.println("creating html");
        HtmlCreator htmlCreator = new HtmlCreator(INPUT_PATH, alphabetFiles.getLangTerms().keySet(), TEMPLATE_PATH, OUTPUT_PATH, ListOfTermPage);
        System.out.println("Processing iate finished!!!");

    }

    private static void cleanDirectory() throws IOException {
        FileRelatedUtils.deleteDirectory(INPUT_PATH);
        FileRelatedUtils.createDirectory(INPUT_PATH);
        FileRelatedUtils.deleteDirectory(OUTPUT_PATH);
        FileRelatedUtils.createDirectory(OUTPUT_PATH);
    }

}
