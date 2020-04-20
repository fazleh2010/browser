/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elahi
 */
public class TermBase {

    public static final String ONTOLOGY_NOTATION = "tbx2rdf";

    public String getBrowserFromOntologyName(String category_ontology) {
        Genterm genterm = new Genterm();
        Iate iate = new Iate();
        if (genterm.getBrowserFromOntologyName(category_ontology)) {
            return genterm.getMAIN_BROWSER_NAME();
        }
        if (iate.getBrowserFromOntologyName(category_ontology)) {
            return iate.getMAIN_BROWSER_NAME();
        }
        return null;
    }

    public String getBrowserFromCategoryName(String categoryName) {
        Genterm genterm = new Genterm();
        Iate iate = new Iate();
        if (genterm.getBrowserFromCategoryName(categoryName)) {
            return genterm.getMAIN_BROWSER_NAME();
        }
        if (iate.getBrowserFromCategoryName(categoryName)) {
            return iate.getMAIN_BROWSER_NAME();
        }
        return null;
    }

    public Genterm getGenterm() {
        return new Genterm();
    }

    public Iate getIate() {
        return new Iate();
    }

    public class Genterm {

        public final String MAIN_BROWSER_NAME = "genterm";

        public final String ATC = "atc";
        public final String DISEASES = "diseases";
        public final String INTAGLIO = "intaglio";
        public final String SOLAR = "solar";
        public final String WASTEMANAGEMENT = "wastemanagement";

        public final String ATC_ONTOLOGY = ONTOLOGY_NOTATION + "_" + ATC;
        public final String DISEASES_ONTOLOGY = ONTOLOGY_NOTATION + "_" + DISEASES;
        public final String INTAGLIO_ONTOLOGY = ONTOLOGY_NOTATION + "_" + INTAGLIO;
        public final String SOLAR_ONTOLOGY = ONTOLOGY_NOTATION + "_" + SOLAR;
        public final String WASTEMANAGEMENT_ONTOLOGY = ONTOLOGY_NOTATION + "_" + WASTEMANAGEMENT;

        public final Map<String, String> CATEGORY_ONTOLOGIES = new HashMap<String, String>() {
            {
                put(ATC, ATC_ONTOLOGY);
                put(SOLAR, SOLAR_ONTOLOGY);
                put(INTAGLIO, INTAGLIO_ONTOLOGY);
                put(WASTEMANAGEMENT, WASTEMANAGEMENT_ONTOLOGY);
                put(DISEASES, DISEASES_ONTOLOGY);
            }
        };

        public String getMAIN_BROWSER_NAME() {
            return MAIN_BROWSER_NAME;
        }

        public Map<String, String> getCATEGORY_ONTOLOGIES() {
            return CATEGORY_ONTOLOGIES;
        }

        public Boolean getBrowserFromOntologyName(String ontology_category) {
            if (CATEGORY_ONTOLOGIES.containsValue(ontology_category)) {
                return true;
            }
            return false;
        }

        public Boolean getBrowserFromCategoryName(String ontology_category) {
            if (CATEGORY_ONTOLOGIES.containsKey(ontology_category)) {
                return true;
            }
            return false;
        }

    }

    public class Iate {

        public String MAIN_BROWSER_NAME = "iate";
        public String IATE = "iate";

        public final String IATE_ONTOLOGY = ONTOLOGY_NOTATION + "_" + IATE;

        public final Map<String, String> CATEGORY_ONTOLOGIES = new HashMap<String, String>() {
            {
                put(IATE, IATE_ONTOLOGY);
            }
        };

        public String getMAIN_BROWSER_NAME() {
            return MAIN_BROWSER_NAME;
        }

        public Boolean getBrowserFromOntologyName(String ontology_category) {
            if (CATEGORY_ONTOLOGIES.containsValue(ontology_category)) {
                return true;
            }
            return false;
        }

        public Boolean getBrowserFromCategoryName(String ontology_category) {
            if (CATEGORY_ONTOLOGIES.containsKey(ontology_category)) {
                return true;
            }
            return false;
        }

    }
}
