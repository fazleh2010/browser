/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template alphabetFile, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import browser.termallod.api.DataBaseTemp;
import browser.termallod.core.html.HtmlParameters;
import browser.termallod.core.term.TermInfo;
import browser.termallod.utils.FileRelatedUtils;
import browser.termallod.utils.Partition;
import java.io.File;
import java.util.Properties;

/**
 *
 * @author elahi
 */
public class AlphabetTermPage {

    private Partition<String> partition = null;
    private Integer numberOfPages = null;
    private Integer emptyTerm = null;
    private String alpahbetPair = null;
    private File alphabetFile = null;
    private Properties props = null;
    private Integer numericalValueOfPair = 0;
    private MergingTermInfo mergingTermInfo;

    public AlphabetTermPage(String language, String alpahbetPair, File file, Partition<String> partition, Integer numericalValueOfPair, DataBaseTemp dataBaseTemp, HtmlParameters htmlParameters) throws Exception {
        this.alpahbetPair = alpahbetPair;
        this.partition = partition;
        this.numberOfPages = partition.size();
        this.numericalValueOfPair = numericalValueOfPair;
        this.alphabetFile = file;
        this.props = FileRelatedUtils.getPropertyHash(this.alphabetFile);
        if(!htmlParameters.getTextFileModifyFlag())
           this.mergingTermInfo = new MergingTermInfo(alphabetFile, language, dataBaseTemp, htmlParameters.getAlternativeFlag());
    }

    /*public String getUrl(String term) {
        //Properties props;
        String url = null;
        try {
            props = FileRelatedUtils.getPropertyHash(this.alphabetFile);
            url = props.getProperty(term);
        } catch (IOException ex) {
            Logger.getLogger(AlphabetTermPage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return url;
    }*/
    public String getUrl(String term) {
        return props.getProperty(term);
    }

    public Partition<String> getPartition() {
        return partition;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public String getAlpahbetPair() {
        return alpahbetPair;
    }

    public Integer getEmptyTerm() {
        return emptyTerm;
    }

    public Integer getNumericalValueOfPair() {
        return numericalValueOfPair;
    }

    public Properties getProps() {
        return props;
    }

    public TermInfo getTermInfo(String url) {
        return mergingTermInfo.getUrlInfo().get(url);
    }

    @Override
    public String toString() {
        return "AlphabetTermPage{" + "numberOfPages=" + numberOfPages + ", alpahbetPair=" + alpahbetPair + ", numericalValueOfPair=" + numericalValueOfPair + '}';
    }

}
