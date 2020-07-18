/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.core.sparql;

import citec.core.termbase.TermInfo;
import java.util.List;

/**
 *
 * @author elahi (a) Adding some triples to a graph. The snippet describes two
 */
public class SparqlGenerator implements SparqlEndpoint {

    private final String sparqlQuery;

    public SparqlGenerator(List<TermInfo> termInfos, String prefix,String ontoNotation) {
        String tripples = this.generateTripples(termInfos, ontoNotation);
        sparqlQuery = insertSparql(prefix, tripples);
    }

    private String insertSparql(String prefix, String tripples) {
        String sparqlQuery
                = prefix
                + "\n"
                + "INSERT DATA"
                + "\n"
                + "{"
                + tripples
                + "}";

        return sparqlQuery;
    }

    public String generateTripples(List<TermInfo> termInfos, String ontoNotation) {
        String content = "\n";
        for (int i = 0; i < termInfos.size(); i++) {
            TermInfo termInfo = termInfos.get(i);
            String tripple = this.getTripple(termInfo,ontoNotation);
            if (i == termInfos.size() - 1) {
                tripple = tripple + ".";
            } else {
                tripple = tripple + ";";
            }
            content += tripple + "\n";
        }
        return content;
    }

    public String getSparqlQuery() {
        return sparqlQuery;
    }

    private String getTripple() {
        return "<http://example/book3> dc:title \"A new book\"";
    }

    private String getTripple(TermInfo termInfo, String ontologyNotation) {
        //return termInfo.getTermUrl() + "\n" + "     "+ontologyNotation + " " + termInfo.getLinks().get(linkTerminologyName);
         return termInfo.getTermUrl() + " "+ontologyNotation + " " + termInfo.getLinks().iterator().next();
    }

}
