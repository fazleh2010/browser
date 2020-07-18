/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.core;

import citec.core.sparql.SparqlEndpoint;
import citec.core.termbase.Termbase;
import citec.core.mysql.MySQLAccess;
import citec.core.sparql.CurlSparqlQuery;
import citec.core.sparql.SparqlGenerator;
import citec.core.termbase.TermInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class Main implements SparqlEndpoint {

    private static String path = "src/main/resources/";
    private static Integer limitOfTerms = -1;
    private static MySQLAccess mySQLAccess;

    public static void main(String[] args) throws Exception {

        Integer index = 1;

        String myTermTableName = "myTerminology", otherTermTableName = "otherTerminology", matchedTermTable = "link";
        String myTermSparqlEndpoint = null, otherTermSparqlEndpoint = null;

        System.out.println("called");
        System.out.println("arguments: " + args.length);
        if (args.length > 0) {
            myTermSparqlEndpoint = args[0];
            System.out.println("endpoint 1: " + args[0]);
        } else {
            System.err.println("no first endpoint in arguments");
            if (index == 1) {
                myTermSparqlEndpoint = endpoint_atc;
            }
        }
        if (args.length > 1) {
            otherTermSparqlEndpoint = args[1];
            System.out.println("endpoint 2: " + args[1]);
        } else {
            System.err.println("no second endpoint in arguments");
            otherTermSparqlEndpoint = endpoint_intaglio;
        }

        mySQLAccess = new MySQLAccess();
        
        System.out.println(myTermSparqlEndpoint);
         System.out.println(otherTermSparqlEndpoint);

        //my terminology
        /*System.out.println("Adding my terminology!!" + myTermSparqlEndpoint);
        Termbase myTerminology = new CurlSparqlQuery(myTermSparqlEndpoint, query_writtenRep, myTermTableName).getTermbase();
        addToDataBase(myTermTableName, myTerminology, limitOfTerms);

        //Link terminology
        System.out.println("Adding my other terminology!!" + otherTermTableName);
        Termbase otherTerminology = new CurlSparqlQuery(otherTermSparqlEndpoint, query_writtenRep, otherTermTableName).getTermbase();
        addToDataBase(otherTermTableName, otherTerminology, limitOfTerms);

        System.out.println("creating linking table!!");
        matchWithDataBase(myTermTableName, otherTerminology, matchedTermTable);

        System.out.println("inserting linked terminologies in host terminology!!");
        List<TermInfo> termList = matchWithDataBase(myTermTableName, otherTerminology, matchedTermTable);
        TermInfo.display(termList);
        /*SparqlGenerator sparqlGenerator = new SparqlGenerator(termList, ontolex_prefix, ontolex_owl_sameAs);
        CurlSparqlQuery curlSparqlQuery=new CurlSparqlQuery(myTermSparqlEndpoint,sparqlGenerator.getSparqlQuery());*/

        mySQLAccess.close();
        //System.out.println("MY terminology !!" + myTermSparqlEndpoint);
        //System.out.println("Other terminology!!" + otherTermSparqlEndpoint);
    }

    private static Boolean addToDataBase(String myTermTableName, Termbase myTerminology, Integer limitOfTerms) {
        try {
            mySQLAccess.deleteTable(myTermTableName);
            mySQLAccess.createTermTable(myTermTableName);
            mySQLAccess.insertDataTermTable(myTermTableName, myTerminology, limitOfTerms);
            mySQLAccess.readTermTable(myTermTableName);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;

    }

    private static List<TermInfo> matchWithDataBase(String myTermTable, Termbase otherTerminology, String matchedTermTable) {
        Integer index = 0;
        List<TermInfo> termInfos = new ArrayList<TermInfo>();
        try {
            mySQLAccess.deleteTable(matchedTermTable);
            mySQLAccess.createLinkingTable(matchedTermTable);
            index = mySQLAccess.insertDataTermTable(myTermTable, otherTerminology, matchedTermTable);
            termInfos = mySQLAccess.readMatchedTermTable(matchedTermTable);
            //display(matchedTermTable);
            System.out.println(matchedTermTable + "  number of matched found:  " + index);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return termInfos;

    }

    /*private static Termbase getTermBaseFromTxtFiles(String termBaseName, String path, String extension) throws Exception {
        //System.out.println(termBaseName+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        File[] myTerminologyfiles = FileUrlUtils.getFiles(path, extension);
        Map<String, TermInfo> allkeysValues = new HashMap<String, TermInfo>();
        for (File file : myTerminologyfiles) {
            //System.out.println(file.getAbsolutePath());
            Map<String, TermInfo> terms = new HashMap<String, TermInfo>();
            terms = FileUrlUtils.getHashFromFile(file);
            allkeysValues.putAll(terms);
        }
        Termbase termbase = new Termbase(termBaseName, allkeysValues);
        return termbase;
    }*/
}
