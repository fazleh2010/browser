/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.html;

import browser.termallod.constants.FileAndLocationConst;
import browser.termallod.core.SubjectFieldMerging;
import browser.termallod.core.term.TermDetail;
import org.jsoup.nodes.Document;

/**
 *
 * @author elahi
 */
public class HtmlPageAbstract {

    public final SubjectFieldMerging merging;
    public final FileAndLocationConst constants;
    public final HtmlReaderWriter htmlReaderWriter;
    public final HtmlParameters htmlCreateParameters;
    public final OntologyInfo info;

    public HtmlPageAbstract(HtmlParameters htmlCreateParameters, OntologyInfo info, HtmlReaderWriter htmlReaderWriter, SubjectFieldMerging merging, FileAndLocationConst constants) {
        this.constants = constants;
        this.htmlReaderWriter = htmlReaderWriter;
        this.htmlCreateParameters = htmlCreateParameters;
        this.info = info;
        this.merging = merging;
    }

   

}
