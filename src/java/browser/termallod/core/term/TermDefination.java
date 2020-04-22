/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.term;

import java.net.URI;
import java.util.List;

/**
 *
 * @author elahi
 */
public class TermDefination {

    private static String SENSE = "Sense";
    //private static String REFERENCE_LINK = "CanonicalForm";
    private static String CANONICALFORM = "CanonicalForm";

    private String term = null;
    private String language = null;
    private URI termUri = null;
    private URI senseUri = null;
    //private URI referenceUri = null;
    private URI canonicalFormUri = null;

    public TermDefination(List<URI> links) {
        for (URI uri : links) {
            if (uri.toString().contains(SENSE)) {
                this.senseUri = uri;
                //System.out.println(uri);
            }
            if (uri.toString().contains(CANONICALFORM)) {
                this.canonicalFormUri = uri;
                //System.out.println(uri);
            }

        }

    }

    public String getTerm() {
        return term;
    }

    public String getLanguage() {
        return language;
    }

    public URI getTermUri() {
        return termUri;
    }

    public URI getSenseUri() {
        return senseUri;
    }


    public URI getCanonicalFormUri() {
        return canonicalFormUri;
    }

   
}
