/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

/**
 *
 * @author elahi
 */
import browser.termallod.api.LanguageManager;
import browser.termallod.utils.NameExtraction;
import browser.termallod.utils.FileRelatedUtils;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class RdfReader {

    private String MODEL_TYPE;
    private String LANGUAGE_SEPERATE_SYMBOLE = "@";
    private LanguageManager languageInfo;

    public RdfReader(String rdfDir, LanguageManager languageInfo, String MODEL_TYPE, String MODEL_EXTENSION, String dataSaveDir) throws Exception {
        this.MODEL_TYPE = MODEL_TYPE;
        this.languageInfo = languageInfo;
        File[] files = FileRelatedUtils.getFiles(rdfDir, MODEL_EXTENSION);
        for (File categoryFile : files) {
            String categoryName = NameExtraction.getCategoryName(rdfDir, categoryFile, MODEL_EXTENSION);
            String fileNameOrUri = rdfDir + categoryFile.getName();
            //temporarily stoped for testing
            this.readTermsAndLanguages(fileNameOrUri, dataSaveDir + categoryName);
        }

    }

    private List<File> readTermsAndLanguages(String fileNameOrUri, String dataSaveDir) throws Exception {
        TreeMap<String, TreeMap<String, List<TermInfo>>> langTerms = getLanguageAndTerms(fileNameOrUri);
        return FileRelatedUtils.writeFile(langTerms, dataSaveDir);
    }

    // dont change List to set. then the sorting breaks;
    private TreeMap<String, TreeMap<String, List<TermInfo>>> getLanguageAndTerms(String fileNameOrUri) throws Exception {
        TreeMap<String, TreeMap<String, List<TermInfo>>> langTerms = new TreeMap<String, TreeMap<String, List<TermInfo>>>();
        Model model = ModelFactory.createDefaultModel();
        InputStream is = FileManager.get().open(fileNameOrUri);
        if (is != null) {
            model.read(is, null, MODEL_TYPE);
            StmtIterator stmtIterator = model.listStatements();
            while (stmtIterator.hasNext()) {
                Triple triple = stmtIterator.nextStatement().asTriple();
                if (triple.getObject().toString().contains("@")) {
                    String language = triple.getObject().getLiteralLanguage().toLowerCase().trim();
                    TermInfo term = new TermInfo(triple);
                    if (!languageInfo.isLanguageExist(language)) {
                        continue;
                    }
                    if (langTerms.containsKey(language)) {
                        langTerms = ifElementExist(language, term, langTerms);

                    } else {
                        langTerms = ifElementNotExist(language, term, langTerms);
                    }
                }

            }

        } else {
            //System.err.println("cannot read " + fileNameOrUri);;
        }
        return langTerms;
    }

    // dont change List to set. then the sorting breaks;
    private TreeMap<String, TreeMap<String, List<TermInfo>>> getLanguageAndTermsTest(String fileNameOrUri) throws Exception {
        TreeMap<String, TreeMap<String, List<TermInfo>>> langTerms = new TreeMap<String, TreeMap<String, List<TermInfo>>>();
        Model model = ModelFactory.createDefaultModel();
        InputStream is = FileManager.get().open(fileNameOrUri);
        if (is != null) {
            model.read(is, null, MODEL_TYPE);
            List<RDFNode> rdfNodes = model.listObjects().toList();
            for (RDFNode rdfNode : rdfNodes) {
                //testrdfNode(rdfNode);
                System.out.println("----------------------RDF node START--------------------------");
                System.out.println(rdfNode.toString());
                System.out.println("----------------------RDF node END--------------------------");

            }

        } else {
            //System.err.println("cannot read " + fileNameOrUri);;
        }
        return langTerms;
    }

    // dont change List to set. then the sorting breaks;
    private TreeMap<String, TreeMap<String, List<TermInfo>>> getLanguageAndTermsTest2(String fileNameOrUri) throws Exception {
        TreeMap<String, TreeMap<String, List<TermInfo>>> langTerms = new TreeMap<String, TreeMap<String, List<TermInfo>>>();
        Model model = ModelFactory.createDefaultModel();
        InputStream is = FileManager.get().open(fileNameOrUri);

        if (is != null) {
            model.read(is, null, MODEL_TYPE);

            String queryString = "select distinct ?Concept where {[] a ?Concept} LIMIT 100";

            /*String queryString
                    = "PREFIX foaf: <http://blog.planetrdf.com/> "
                    + "SELECT ?url "
                    + "WHERE {"
                    + "      ?contributor foaf:name \"Jon Foobar\" . "
                    + "      ?contributor foaf:weblog ?url . "
                    + "      }";
             */
            Query query = QueryFactory.create(queryString);

            // Execute the query and obtain results
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet results = qe.execSelect();

            // Output query results    
            ResultSetFormatter.out(System.out, results, query);

            // Important ‑ free up resources used running the query
            qe.close();

        } else {
            //System.err.println("cannot read " + fileNameOrUri);;
        }

        return langTerms;
    }

    /*private TreeMap<String, TreeMap<String, List<TermInfo>>> ifElementNotExist(String language, TermInfo term, TreeMap<String, TreeMap<String, List<TermInfo>>> langTerms) {
        String pair;
        try {
        pair = this.getAlphabetPair(language, term.getTermString());
        TreeMap<String, List<TermInfo>> alpahbetTerms = new TreeMap<String, List<TermInfo>>();
        List<TermInfo> terms = new ArrayList<TermInfo>();
        terms.add(term);
        alpahbetTerms.put(pair, terms);
        langTerms.put(language, alpahbetTerms);
        } catch (NullPointerException e) {
            System.out.println("Null pointer:" + language + " " + term);

        }
        return langTerms;
    }*/
    private TreeMap<String, TreeMap<String, List<TermInfo>>> ifElementNotExist(String language, TermInfo term, TreeMap<String, TreeMap<String, List<TermInfo>>> langTerms) {
        String pair;
        try {
            pair = this.getAlphabetPair(language, term.getTermString());
            TreeMap<String, List<TermInfo>> alpahbetTerms = new TreeMap<String, List<TermInfo>>();
            List<TermInfo> terms = new ArrayList<TermInfo>();
            terms.add(term);
            alpahbetTerms.put(pair, terms);
            langTerms.put(language, alpahbetTerms);
        } catch (NullPointerException e) {
            System.out.println("Null pointer:" + language + " " + term);

        }
        return langTerms;
    }

    private TreeMap<String, TreeMap<String, List<TermInfo>>> ifElementExist(String language, TermInfo term, TreeMap<String, TreeMap<String, List<TermInfo>>> langTerms) {
        String pair;
        pair = this.getAlphabetPair(language, term.getTermString());
        TreeMap<String, List<TermInfo>> alpahbetTerms = langTerms.get(language);
        try {
            if (alpahbetTerms.containsKey(pair)) {
                List<TermInfo> terms = alpahbetTerms.get(pair);
                terms.add(term);
                alpahbetTerms.put(pair, terms);
                langTerms.put(language, alpahbetTerms);
            } else {
                List<TermInfo> terms = new ArrayList<TermInfo>();
                terms.add(term);
                alpahbetTerms.put(pair, terms);
                langTerms.put(language, alpahbetTerms);
            }
        } catch (NullPointerException e) {
            System.out.println("Null pointer:" + language + " " + term);

        }
        return langTerms;
    }

    private String getAlphabetPair(String language, String term) {
        HashMap<String, String> alphabetPairs;
        try {
            alphabetPairs = this.languageInfo.getLangAlphabetHash(language);
            term = term.trim();
            String letter = term.substring(0, 1);
            if (alphabetPairs.containsKey(letter)) {
                String pair = alphabetPairs.get(letter);
                return pair;
            }
        } catch (Exception ex) {
            //System.out.println("No alphebet found for the lanague" + language);
        }

        return null;
    }
}
