/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.utils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

/**
 *
 * @author elahi
 */
public class DataBaseTests implements SparqlEndpoint {

    public static void main(String[] args) throws Exception {
        DataBaseTests main = new DataBaseTests();
        /*ResultSet first_results = getResult(tbx2rdf_iate_endpoint, tbx2rdf_iate__query);
         ResultSet sec_results = getResult(dbpedia_endpoint, dbpedia_query);*/
        //ResultSet first_results = getResult(tbx2rdftest, tbx2rdf_iate__query);
        MySQLAccess mySQLAccess = new MySQLAccess();
        //mySQLAccess.createTermTable("iate_en_A_B");
        //mySQLAccess.createTermTable("atc_en_A_B");
        //mySQLAccess.createLinkingTable("linking_terms");
        //mySQLAccess.deleteTable("en_A_B_term");
        //mySQLAccess.deleteTable("linking_terms");
        //mySQLAccess.insertDataTermTable("en_A_B_term");
        //mySQLAccess.insertDataLinkTable("linking_terms");
    }

    private static ResultSet getResult(String sparql_endpoint, String sparql_query) {
        Query query = QueryFactory.create(sparql_query); //s2 = the query above
        QueryExecution qExe = QueryExecutionFactory.sparqlService(sparql_endpoint, query);
        ResultSet results = qExe.execSelect();
        ResultSetFormatter.out(System.out, results, query);
        return results;
    }

}
