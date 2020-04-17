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
import browser.termallod.core.term.TermInfo;
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
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class RdfReaderNew {

    private String termID = "IATE-";
    private String senseID = "Sense";
    private String subjectID = "http://tbx2rdf.lider-project.eu/data/iate/subjectField/";
    private String MODEL_TYPE;
    private String LANGUAGE_SEPERATE_SYMBOLE = "@";
    private String administrativeStatus = "administrativeStatus";
    private String reliabilityCode = "reliabilityCode";
    private String HTTP = "http";
    private String HTTP_IATE = "http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_iate/data/iate/";
    private LanguageManager languageInfo;

    public RdfReaderNew(String rdfDir, LanguageManager languageInfo, String MODEL_TYPE, String MODEL_EXTENSION, String dataSaveDir) throws Exception {
        this.MODEL_TYPE = MODEL_TYPE;
        this.languageInfo = languageInfo;
        File[] files = FileRelatedUtils.getFiles(rdfDir, MODEL_EXTENSION);
        for (File categoryFile : files) {
            String categoryName = NameExtraction.getCategoryName(rdfDir, categoryFile, MODEL_EXTENSION);
            String fileNameOrUri = rdfDir + categoryFile.getName();
            this.extractInformation(fileNameOrUri, dataSaveDir, categoryName);

        }

    }

    // dont change List to set. then the sorting breaks;
    // temporarliy closed.
    private void extractInformation(String fileNameOrUri, String dataSaveDir, String categoryName) throws Exception {
        TreeMap<String, TreeMap<String, List<TermInfo>>> langTerms = new TreeMap<String, TreeMap<String, List<TermInfo>>>();
        Map<String, String> langSensList = new TreeMap<String, String>();
        //Map<String, String> idSubjectFieldID = new TreeMap<String, String>();
        Map<String, String> urlCanonicalForm = new TreeMap<String, String>();
        Map<String, String> urlSense = new TreeMap<String, String>();
        Map<String, String> urlReliabilityCode = new TreeMap<String, String>();
        Map<String, String> urlAdministrative = new TreeMap<String, String>();
        Model model = ModelFactory.createDefaultModel();
        InputStream is = FileManager.get().open(fileNameOrUri);
        String idSubjectID = "";

        if (is != null) {
            model.read(is, null, MODEL_TYPE);
            StmtIterator stmtIterator = model.listStatements();
            while (stmtIterator.hasNext()) {
                Statement statement = stmtIterator.nextStatement();
                Triple triple = statement.asTriple();
                //System.out.println(triple.toString());
                /*if (triple.toString().contains("canonicalForm") ||  triple.toString().contains(subjectID)) {
                        System.out.println(triple.toString());
                    }*/
 /*if (!triple.toString().contains("canonicalForm") ||  !triple.toString().contains(senseID)||!triple.toString().contains(termID)) {
                        System.out.println(triple.toString());
                    }*/

                //"http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_iate/data/iate/ablakl%C3%A9h%C3%A9s-hu"
                if (triple.toString().contains(HTTP_IATE)) {
                    if (triple.toString().contains(reliabilityCode)) {
                        urlReliabilityCode = getreliabilityCode(triple, urlReliabilityCode);
                    }

                }
                if (triple.toString().contains(HTTP_IATE)) {
                    if (triple.toString().contains(administrativeStatus)) {
                        urlAdministrative = getAdministrativeStatus(triple, urlAdministrative);
                    }

                }

                if (triple.toString().contains(termID)) {

                    if (triple.toString().contains(subjectID)) {
                        idSubjectID = this.getSubjectField(triple, idSubjectID);
                    }
                    if (triple.toString().contains(senseID)) {
                        langSensList = this.getSense(triple, langSensList);
                    }
                   
                } else {
                    /*if (triple.toString().contains("CanonicalForm") && !triple.getObject().toString().contains(LANGUAGE_SEPERATE_SYMBOLE)) {
                        urlCanonicalForm = this.getCanonicalForm(triple, urlCanonicalForm, "CanonicalForm");
                    }
                    if (triple.toString().contains("Sense")) {
                        urlSense = this.getCanonicalForm(triple, urlSense, "Sense");
                    }*/
                }

                if (triple.getObject().toString().contains(LANGUAGE_SEPERATE_SYMBOLE)) {
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

        FileRelatedUtils.writeFile(idSubjectID, dataSaveDir + File.separator + "subject.txt");
        //FileRelatedUtils.writeFileNew(idSubjectFieldID, dataSaveDir + File.separator + "subject.txt");
        FileRelatedUtils.writeFile(langTerms, dataSaveDir + categoryName);
        FileRelatedUtils.writeLangFile2(langSensList, dataSaveDir);
        //FileRelatedUtils.writeLangFile(langSensList, dataSaveDir);
        System.out.println(urlCanonicalForm.size());
        //FileRelatedUtils.writeFileNew(urlCanonicalForm, dataSaveDir + File.separator + "canonicalForm.txt");
        System.out.println(urlSense.size());
        //FileRelatedUtils.writeFileNew(urlSense, dataSaveDir + File.separator + "sense.txt");
        FileRelatedUtils.writeFileNew(urlReliabilityCode, dataSaveDir + File.separator + "reliabilityCode.txt");

    }

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

    private Map<String, String> getSense(Triple statement, Map<String, String> langSensList) throws Exception {
        String string = statement.toString();
        String[] infos = string.split(" ");
        List<String> wordList = Arrays.asList(infos);
        String id = null, checkField = null, senseField = null, OrgSenseField = null, language = null;
        //System.out.println(statement.toString());
        for (String http : wordList) {
            if (http.contains(termID)) {
                id = http.trim();
            }
            if (http.contains(this.senseID)) {
                checkField = http.trim();
                OrgSenseField = checkField;
                checkField = modifyUrl(checkField);
                checkField = checkField.substring(0, checkField.lastIndexOf('#'));
                //String firstLetter = checkField.substring(0, 1);
                //if (firstLetter.equals("a") || firstLetter.equals("b")) {
                senseField = checkField;
                //}

            }

        }
        if (termID != null && senseField != null) {

            //checkField = checkField.substring(0, checkField.lastIndexOf('#'));
            //checkField=checkField.replace("HTTP://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_iate/data/iate/", "");
            id = this.modifyId(id);
            //id=url.replace("HTTP://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_iate/data/iate/", "");
            language = this.getLanguage(senseField);
            if (!languageInfo.isLanguageExist(language)) {
                return langSensList;
            }

            if (langSensList.containsKey(language)) {
                String idSense = langSensList.get(language);
                OrgSenseField = this.modifyUrl(OrgSenseField);
                String line = id + "=" + OrgSenseField;
                idSense += line + "\n";
                langSensList.put(language, idSense);
            } else {
                String idSense = "";
                OrgSenseField = modifyUrl(OrgSenseField);
                String line = id + "=" + OrgSenseField;
                idSense += line + "\n";
                langSensList.put(language, idSense);
            }
            //System.out.println(url+".."+checkField);

        }
        return langSensList;
    }

    private String getSubjectField(Triple statement, String idSubjectID) throws Exception {
        String string = statement.toString();
        String[] infos = string.split(" ");
        List<String> wordList = Arrays.asList(infos);
        String id = null, checkField = null, language = null;
        for (String http : wordList) {
            if (http.contains(termID)) {
                id = http.trim();
            }
            if (http.contains(subjectID)) {
                checkField = http.trim();
            }
            //System.out.println(url+" "+senseField);
        }
        if (termID != null && checkField != null) {
            if (checkField.contains(this.subjectID)) {
                //System.out.println(url+".."+checkField);
                id = this.modifyId(id);
                checkField = this.modifySubject(checkField);
                String line = id + "=" + checkField;
                idSubjectID += line + "\n";
            }

        }
        return idSubjectID;
    }

    private Map<String, String> getCanonicalForm(Triple statement, Map<String, String> idSubjectFieldID, String match) throws Exception {
        String string = statement.toString();
        String[] infos = string.split(" ");
        List<String> wordList = Arrays.asList(infos);
        String url = null, checkField = null, language = null;
        for (String http : wordList) {
            if (!http.contains("ontolex")) {
                if (http.contains(match)) {
                    checkField = http.trim();
                } else {
                    url = http.trim();
                    /*language = getLanguage(url);
                    if (!language.contains("en")) {
                        return idSubjectFieldID;
                    }*/
                }

            }

        }
        if (url != null && checkField != null) {

            //System.out.println(url+".."+checkField);
            //id=this.modifyId(url);
            //checkField=this.modifySubject(checkField);
            // System.out.println(url+".."+checkField);
            checkField = this.modifyId(checkField);
            idSubjectFieldID.put(checkField, url);

        }
        return idSubjectFieldID;
    }

    private Map<String, String> getreliabilityCode(Triple statement, Map<String, String> urlMap) throws Exception {
        String string = statement.toString();
        String[] infos = string.split(" ");
        List<String> wordList = Arrays.asList(infos);
        String url = null, checkField = null, language = null, orgUrl = null;
        for (String http : wordList) {
            if (http.contains(HTTP)) {
                if (http.contains(HTTP_IATE)) {
                    orgUrl = http.trim();
                    url = orgUrl;
                    url = modifyUrl(url);
                    language = this.getLanguage(url);
                    if (!language.contains("en")) {
                        return urlMap;
                    }
                } else if (http.contains("integer")) {
                    checkField = http.trim();
                }
            }
        }
        if (orgUrl != null && checkField != null) {
            urlMap.put(orgUrl, checkField);
            System.out.println(statement.toString());
        }
        return urlMap;
    }

    private Map<String, String> getAdministrativeStatus(Triple statement, Map<String, String> urlMap) throws Exception {
        String string = statement.toString();
        String[] infos = string.split(" ");
        List<String> wordList = Arrays.asList(infos);
        String url = null, checkField = null, language = null, orgUrl = null;
        for (String http : wordList) {
            if (http.contains(HTTP)) {
                if (http.contains(HTTP_IATE)) {
                    orgUrl = http.trim();
                    url = orgUrl;
                    url = modifyUrl(url);
                    language = this.getLanguage(url);
                    if (!language.contains("en")) {
                        return urlMap;
                    }
                } else if (http.contains("@" + HTTP)) {
                    // checkField = http.trim();
                } else if (http.contains(HTTP)) {
                    checkField = http.trim();
                }
            }
        }
        if (orgUrl != null && checkField != null) {
            urlMap.put(orgUrl, checkField);
            System.out.println(statement.toString());
        }
        return urlMap;
    }

    private String getLanguage(String checkField) throws Exception {
        String http = "http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_iate/data/iate/";
        checkField = checkField.replace(http, "");
        //checkField = checkField.substring(0, checkField.lastIndexOf('#'));        
        String[] info = checkField.split("-");
        String language = info[1];
        return language;
    }

    private String modifyId(String id) {
        id = id.replace("http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_iate/data/iate/", "");
        //id=id.replace(termID, "");
        return id;
    }

    private String modifyUrl(String url) {
        url = url.replace("http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_iate/data/iate/", "");
        //url=url.toLowerCase();
        return url;
    }

    private String modifySubject(String checkField) {
        return checkField = checkField.replace(subjectID, "");
    }

}
