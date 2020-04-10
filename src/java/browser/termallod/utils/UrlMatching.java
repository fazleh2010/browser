/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.utils;

import browser.termallod.core.SubjectFieldMerging;
import browser.termallod.core.term.TermDetail;
import browser.termallod.core.term.TermInfo;

/**
 *
 * @author elahi
 */
public class UrlMatching {

    private String IATE_URL = "http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_iate/data/iate/";
    private String hashString = "#";
    private SubjectFieldMerging merging;
    private Boolean mergingResult = false;
    private TermInfo termInfo = null;

    public UrlMatching(SubjectFieldMerging merging, String termUrl) {
        this.merging = merging;
        this.termInfo = this.getTermInformation(termUrl);
    }

    private TermInfo getTermInformation(String termUrl) {
        termUrl = TermDetail.getAlternativeUrl(termUrl, false);
        termUrl = termUrl.replace(IATE_URL, "");
        if (termUrl.contains((hashString))) {
            termUrl = termUrl.substring(0, termUrl.lastIndexOf('#'));
        }
        System.out.println(termUrl);

        for (String givenUrlOrg : this.merging.getUrlInfo().keySet()) {
            String givenUrl = givenUrlOrg.substring(0, givenUrlOrg.lastIndexOf('#'));
            if (givenUrl.equals(termUrl)) {
                return this.merging.getUrlInfo().get(givenUrlOrg);
            }

        }
        return null;
        //return this.merging.getUrlInfo(url);
    }

    public Boolean getMergingResult() {
        return mergingResult;
    }

    public TermInfo getTermInfo() {
        return termInfo;
    }

}
