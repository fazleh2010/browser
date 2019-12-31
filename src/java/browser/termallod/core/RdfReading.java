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
import browser.termallod.constants.FilePathAndConstant;
import browser.termallod.core.api.LanguageManager;
import browser.termallod.utils.NameExtraction;
import browser.termallod.utils.FileRelatedUtils;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.FileManager;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


public class RdfReading implements FilePathAndConstant {

    private String MODEL_TYPE;
    private String LANGUAGE_SEPERATE_SYMBOLE = "@";
    private LanguageManager languageInfo;

    public RdfReading(String rdfDir, LanguageManager languageInfo, String MODEL_TYPE, String MODEL_EXTENSION, String dataSaveDir) throws Exception {
        this.MODEL_TYPE = MODEL_TYPE;
        this.languageInfo = languageInfo;
        File[] files = FileRelatedUtils.getFiles(rdfDir, MODEL_EXTENSION);
        for (File categoryFile : files) {
            String categoryName = NameExtraction.getCategoryName(rdfDir, categoryFile, MODEL_EXTENSION);
            String fileNameOrUri = rdfDir + categoryFile.getName();
            this.readTermsAndLanguages(fileNameOrUri, dataSaveDir + categoryName);
        }

    }

    private List<File> readTermsAndLanguages(String fileNameOrUri, String dataSaveDir) throws Exception {
        TreeMap<String, TreeMap<String, List<String>>> langTerms = getLanguageAndTerms(fileNameOrUri);
        return FileRelatedUtils.writeFile(langTerms, dataSaveDir);
    }

    // dont change List to set. then the sorting breaks;
    private TreeMap<String, TreeMap<String, List<String>>> getLanguageAndTerms(String fileNameOrUri) throws Exception {
        TreeMap<String, TreeMap<String, List<String>>> langTerms = new TreeMap<String, TreeMap<String, List<String>>>();
        Model model = ModelFactory.createDefaultModel();
        InputStream is = FileManager.get().open(fileNameOrUri);
        if (is != null) {
            model.read(is, null, MODEL_TYPE);
            //model.write(System.out, "N-TRIPLE");
            List<RDFNode> rdfNodes = model.listObjects().toList();
            for (RDFNode rdfNode : rdfNodes) {
                //testrdfNode(rdfNode);
                if (rdfNode.isLiteral()) {
                    if (rdfNode.toString().toLowerCase().contains(LANGUAGE_SEPERATE_SYMBOLE)) {
                        String[] infor = rdfNode.toString().split(LANGUAGE_SEPERATE_SYMBOLE);;
                        String language = infor[1].toLowerCase();
                        String term = infor[0].toLowerCase();
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
            }

        } else {
            System.err.println("cannot read " + fileNameOrUri);;
        }
        return langTerms;
    }

    private TreeMap<String, TreeMap<String, List<String>>> ifElementNotExist(String language, String term, TreeMap<String, TreeMap<String, List<String>>> langTerms) {
        String pair;
        pair = this.getAlphabetPair(language, term);
        TreeMap<String, List<String>> alpahbetTerms = new TreeMap<String, List<String>>();
        List<String> terms = new ArrayList<String>();
        terms.add(term);
        alpahbetTerms.put(pair, terms);
        langTerms.put(language, alpahbetTerms);
        return langTerms;
    }

    private TreeMap<String, TreeMap<String, List<String>>> ifElementExist(String language, String term, TreeMap<String, TreeMap<String, List<String>>> langTerms) {
        String pair;
        pair = this.getAlphabetPair(language, term);
        TreeMap<String, List<String>> alpahbetTerms = langTerms.get(language);
        try {
            if (alpahbetTerms.containsKey(pair)) {
                List<String> terms = alpahbetTerms.get(pair);
                terms.add(term);
                alpahbetTerms.put(pair, terms);
                langTerms.put(language, alpahbetTerms);
            } else {
                List<String> terms = new ArrayList<String>();
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
            System.out.println("No alphebet found for the lanague" + language);
        }

        return null;
    }


    /*private void testrdfNode(RDFNode rdfNode) {
        if (rdfNode.isResource()) {
            Resource resource = rdfNode.asResource();
            String nl = "http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_atc/data/atc/";
            if (resource.toString().contains(nl) && !resource.toString().contains("TransacGrp")
                    && !resource.toString().contains("#")) {
                System.out.println(resource);
            }
            if (resource.toString().contains(nl) && !resource.toString().contains("TransacGrp")
                    && resource.toString().contains("#")) {
                System.out.println(resource);
            }
          
        }
    }*/
}
