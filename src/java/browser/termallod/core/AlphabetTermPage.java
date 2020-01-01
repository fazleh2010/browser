/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import browser.termallod.utils.FileRelatedUtils;
import browser.termallod.utils.Partition;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class AlphabetTermPage {

    private Partition<String> partition = null;
    private Integer numberOfPages = null;
    private Integer emptyTerm = null;
    private String alpahbetPair = null;
    private File file = null;

    public AlphabetTermPage(String alpahbetPair, File file, Partition<String> partition) {
        this.alpahbetPair = alpahbetPair;
        this.partition = partition;
        this.numberOfPages = partition.size();
        this.file = file;
        /*if(numberOfPages<100){
            emptyTerm=100-numberOfPages;
        }*/
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

    public String getUrl(String term) {
        Properties props;
        String url =null;
        try {
            props = FileRelatedUtils.getPropertyHash(this.file);
            url = props.getProperty(term);
        } catch (IOException ex) {
            Logger.getLogger(AlphabetTermPage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return url;
    }

}
