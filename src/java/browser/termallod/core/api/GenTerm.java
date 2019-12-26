/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.api;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author elahi
 */
public interface GenTerm {

    public String solar = "solar";
    public String ate = "ate";
    public String diseases = "diseases";
    public String intaglio = "intaglio";
    public String wastemanagement = "wastemanagement";
    public Map<String, String> categoryOntologyMapper = new HashMap<String, String>() {
        {
            put(ate, "tbx2rdf_atc");
            put(solar, "tbx2rdf_solarenergy");
            put(intaglio, "tbx2rdf_intaglio");
            put(wastemanagement, "tbx2rdf_wastemanagement");
            put(diseases, "tbx2rdf_diseases");
        }
    };

}
