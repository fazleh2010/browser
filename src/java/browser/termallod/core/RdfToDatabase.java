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
import browser.termallod.api.DataBaseTemp;
import browser.termallod.api.IATE;
import browser.termallod.core.term.TermInfo;
import browser.termallod.api.LanguageManager;
import static browser.termallod.app.Main.constants;
import browser.termallod.utils.DataBaseTests;
import browser.termallod.utils.NameExtraction;
import browser.termallod.utils.FileRelatedUtils;
import browser.termallod.utils.MySQLAccess;
import browser.termallod.utils.StringMatcherUtil;
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

public class RdfToDatabase implements IATE {

    private String MODEL_TYPE;
    private LanguageManager languageInfo;
    private DataBaseTemp dataBaseTemp;

    public RdfToDatabase(String browser, DataBaseTemp dataBaseTemp, LanguageManager languageInfo, String MODEL_TYPE, String MODEL_EXTENSION) throws Exception {
        this.MODEL_TYPE = MODEL_TYPE;
        this.languageInfo = languageInfo;
        this.dataBaseTemp = dataBaseTemp;
        String rdfDir = constants.getINPUT_RDF_PATH(browser);
        String txtDir = constants.getINPUT_TXT_PATH(browser);

        /*ResultSet first_results = getResult(tbx2rdf_iate_endpoint, tbx2rdf_iate__query);
         ResultSet sec_results = getResult(dbpedia_endpoint, dbpedia_query);*/
        //ResultSet first_results = getResult(tbx2rdftest, tbx2rdf_iate__query);
        //mySQLAccess.createTermTable("iate_en_A_B");
        //mySQLAccess.createTermTable("atc_en_A_B");
        //mySQLAccess.createLinkingTable("linking_terms");
        //mySQLAccess.deleteTable("en_A_B_term");
        //mySQLAccess.deleteTable("linking_terms");
        //mySQLAccess.insertDataTermTable("en_A_B_term");
        //mySQLAccess.insertDataLinkTable("linking_terms");

        File[] turtleFiles = FileRelatedUtils.getFiles(rdfDir, MODEL_EXTENSION);

        if (turtleFiles.length > 0) {
            for (File categoryFile : turtleFiles) {
                String categoryName = NameExtraction.getCategoryName(rdfDir, categoryFile, MODEL_EXTENSION);
                String fileNameOrUri = rdfDir + categoryFile.getName();
                 //MySQLAccess mySQLAccess = new MySQLAccess();
                this.extractInformation(fileNameOrUri,  categoryName);
                break;
            }
        } else {
            throw new Exception("No rdf file to process!!!");
        }

    }

    // dont change List to set. then the sorting breaks;
    // temporarliy closed.
    private void extractInformation(String fileNameOrUri, String categoryName) throws Exception {
       
        TreeMap<String, TreeMap<String, List<TermInfo>>> langTerms = new TreeMap<String, TreeMap<String, List<TermInfo>>>();
        Map<String, String> langSensList = new TreeMap<String, String>();
        Map<String, String> urlCanonicalForm = new TreeMap<String, String>();
        Map<String, String> urlSense = new TreeMap<String, String>();
        Map<String, TreeMap<String, String>> reliabilityCode = new TreeMap<String, TreeMap<String, String>>();
        Map<String, TreeMap<String, String>> administrativeStatus = new TreeMap<String, TreeMap<String, String>>();
        Model model = ModelFactory.createDefaultModel();
        InputStream is = FileManager.get().open(fileNameOrUri);
        String idSubjectID = "";

        if (is != null) {
            model.read(is, null, MODEL_TYPE);
            StmtIterator stmtIterator = model.listStatements();
            while (stmtIterator.hasNext()) {
                Statement statement = stmtIterator.nextStatement();
                Triple triple = statement.asTriple();

                if (triple.toString().contains(HTTP_IATE)) {
                    if (triple.toString().contains(RELIABILITY_CODE)) {
                        reliabilityCode = getreliabilityCode(triple, reliabilityCode);
                    }

                }
                if (triple.toString().contains(HTTP_IATE)) {
                    if (triple.toString().contains(ADMINISTRATIVE_STATUS)) {
                        administrativeStatus = getAdministrativeStatus(triple, administrativeStatus);
                    }

                }

                if (triple.toString().contains(IATE_ID)) {

                    if (triple.toString().contains(HTTP_SUBJECT_FIELD)) {
                        idSubjectID = this.getSubjectField(triple, idSubjectID);
                    }
                    if (triple.toString().contains(SENSE)) {
                        langSensList = this.getSense(triple, langSensList);
                    }

                } else {
                    if (triple.toString().contains("CanonicalForm") && !triple.getObject().toString().contains(LANGUAGE_SEPERATE_SYMBOLE)) {
                        urlCanonicalForm = this.getCanonicalForm(triple, urlCanonicalForm, "CanonicalForm");
                    }
                    if (triple.toString().contains("Sense")) {
                        urlSense = this.getCanonicalForm(triple, urlSense, "Sense");
                    }
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
        
        //MySQLAccess.insertDataTermTable(langTerms,categoryName);

        /*FileRelatedUtils.writeFile(idSubjectID, dataSaveDir + File.separator + dataBaseTemp.getSubjectFileName());
        FileRelatedUtils.writeFile(langTerms, dataSaveDir + categoryName);
        FileRelatedUtils.writeLangFile2(langSensList, dataSaveDir, dataBaseTemp.getSENSE());
        FileRelatedUtils.writeLangFile(reliabilityCode, dataSaveDir, dataBaseTemp.getRELIABILITY_CODE());
        FileRelatedUtils.writeLangFile(administrativeStatus, dataSaveDir, dataBaseTemp.getADMINISTRATIVE_STATUS());*/

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
        System.out.println(statement);
        String string = statement.toString();
        String[] infos = string.split(" ");
        List<String> wordList = Arrays.asList(infos);
        String id = null, checkField = null, senseField = null, OrgSenseField = null, language = null;
        for (String http : wordList) {
            if (http.contains(IATE_ID)) {
                id = http.trim();
            }
            if (http.contains(this.SENSE)) {
                checkField = http.trim();
                OrgSenseField = checkField;
                checkField = StringMatcherUtil.modifyUrl(checkField);
                checkField = checkField.substring(0, checkField.lastIndexOf('#'));
                senseField = checkField;
            }

        }
        if (IATE_ID != null && senseField != null) {
            id = StringMatcherUtil.modifyId(id);
            language = StringMatcherUtil.getLanguage(senseField);
            if (!languageInfo.isLanguageExist(language)) {
                return langSensList;
            }

            if (langSensList.containsKey(language)) {
                String idSense = langSensList.get(language);
                OrgSenseField = StringMatcherUtil.modifyUrl(OrgSenseField);
                String line = id + " = " + OrgSenseField;
                idSense += line + "\n";
                langSensList.put(language, idSense);
            } else {
                String idSense = "";
                OrgSenseField = StringMatcherUtil.modifyUrl(OrgSenseField);
                String line = id + " = " + OrgSenseField;
                idSense += line + "\n";
                langSensList.put(language, idSense);
            }

        }
        return langSensList;
    }

    private String getSubjectField(Triple statement, String idSubjectID) throws Exception {
        //System.out.println(statement);
        String string = statement.toString();
        String[] infos = string.split(" ");
        List<String> wordList = Arrays.asList(infos);
        String id = null, checkField = null, language = null;
        for (String http : wordList) {
            if (http.contains(IATE_ID)) {
                id = http.trim();
            }
            if (http.contains(HTTP_SUBJECT_FIELD)) {
                checkField = http.trim();
            }
            //System.out.println(url+" "+senseField);
        }
        if (IATE_ID != null && checkField != null) {
            if (checkField.contains(this.HTTP_SUBJECT_FIELD)) {
                //System.out.println(url+".."+checkField);
                id = StringMatcherUtil.modifyId(id);
                checkField = StringMatcherUtil.modifySubject(checkField);
                String line = id + " = " + checkField;
                idSubjectID += line + "\n";
            }

        }
        return idSubjectID;
    }

    private Map<String, String> getCanonicalForm(Triple statement, Map<String, String> idSubjectFieldID, String match) throws Exception {
        //System.out.println(statement);
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
            checkField = StringMatcherUtil.modifyId(checkField);
            idSubjectFieldID.put(checkField, url);

        }
        return idSubjectFieldID;
    }

    private Map<String, TreeMap<String, String>> getreliabilityCode(Triple statement, Map<String, TreeMap<String, String>> langReliabiltyList) throws Exception {
        //System.out.println(statement);
        String string = statement.toString();
        String[] infos = string.split(" ");
        List<String> wordList = Arrays.asList(infos);
        String url = null, checkField = null, language = null, orgUrl = null;
        for (String http : wordList) {
            if (http.contains(HTTP)) {
                if (http.contains(HTTP_IATE)) {
                    orgUrl = http.trim();
                    url = orgUrl;
                    url = StringMatcherUtil.modifyUrl(url);
                    language = StringMatcherUtil.getLanguage(url);
                } else if (http.contains("integer")) {
                    checkField = http.trim();
                    checkField = StringMatcherUtil.modifyReliabiltyCode(checkField);
                }
            }
        }
        if (orgUrl != null && checkField != null) {
            url = StringMatcherUtil.modifyUrl(orgUrl);

            if (langReliabiltyList.containsKey(language)) {
                TreeMap<String, String> urlReliabilityCode = langReliabiltyList.get(language);
                urlReliabilityCode.put(url, checkField);
                langReliabiltyList.put(language, urlReliabilityCode);
            } else {
                TreeMap<String, String> urlReliabilityCode = new TreeMap<String, String>();
                urlReliabilityCode.put(url, checkField);
                langReliabiltyList.put(language, urlReliabilityCode);
            }

        }

        return langReliabiltyList;
    }

    private Map<String, TreeMap<String, String>> getAdministrativeStatus(Triple statement, Map<String, TreeMap<String, String>> lang) throws Exception {
        System.out.println(statement);
        String string = statement.toString();
        String[] infos = string.split(" ");
        TreeMap<String, String> urlAdministrative = new TreeMap<String, String>();
        List<String> wordList = Arrays.asList(infos);
        String url = null, checkField = null, language = null, orgUrl = null;
        for (String http : wordList) {
            if (http.contains(HTTP)) {
                if (http.contains(HTTP_IATE)) {
                    orgUrl = http.trim();
                    url = orgUrl;

                    language = StringMatcherUtil.getLanguage(url);

                } else if (http.contains("@" + HTTP)) {
                    // checkField = http.trim();
                } else if (http.contains(HTTP)) {
                    checkField = http.trim();
                    checkField = StringMatcherUtil.modifyAdministrativeStatus(checkField);
                }
            }
        }
        if (orgUrl != null && checkField != null) {
            url = StringMatcherUtil.modifyUrl(orgUrl);

            if (lang.containsKey(language)) {
                urlAdministrative = lang.get(language);
                urlAdministrative.put(url, checkField);
                lang.put(language, urlAdministrative);
            } else {
                urlAdministrative.put(url, checkField);
                lang.put(language, urlAdministrative);
            }

        }
        return lang;
    }

}
