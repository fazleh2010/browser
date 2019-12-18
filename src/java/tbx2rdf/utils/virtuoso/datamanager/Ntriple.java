/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tbx2rdf.utils.virtuoso.datamanager;

/**
 *
 * @author elahi
 */
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Ntriple {

    private static String N_TRIPLE = "N-TRIPLE";
    private static String langSepSymbol = "@";

    private TreeMap<String, List<String>> langSortedTerms = new TreeMap<String, List<String>>();

    public Ntriple(String PATH) {
        langSortedTerms = this.readTermsAndLanguages(PATH);
    }

    private TreeMap<String, List<String>> readTermsAndLanguages(String fileNameOrUri) {
        TreeMap<String, List<String>> langTerms = new TreeMap<String, List<String>>();
        Model model = ModelFactory.createDefaultModel();
        InputStream is = FileManager.get().open(fileNameOrUri);
        if (is != null) {
            model.read(is, null, N_TRIPLE);
            //model.write(System.out, "N-TRIPLE");
            List<RDFNode> rdfNodes = model.listObjects().toList();
            for (RDFNode rdfNode : rdfNodes) {
                if (rdfNode.isLiteral()) {
                    if (rdfNode.toString().toLowerCase().contains(langSepSymbol)) {
                        String[] infor = rdfNode.toString().split(langSepSymbol);;
                        String language = infor[1].toLowerCase();
                        String term = infor[0].toLowerCase();
                        if (langTerms.containsKey(language)) {
                            List<String> terms = langTerms.get(language);
                            terms.add(term);
                            langTerms.put(language, terms);

                        } else {
                            List<String> terms = new ArrayList<String>();
                            terms.add(term);
                            langTerms.put(language, terms);
                        }
                    }
                }
            }

        } else {
            System.err.println("cannot read " + fileNameOrUri);;
        }
        return langTerms;
    }

    public TreeMap<String, List<String>> getLangSortedTerms() {
        return langSortedTerms;
    }

}
