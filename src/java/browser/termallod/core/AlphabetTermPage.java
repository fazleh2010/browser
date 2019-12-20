/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core;

import browser.termallod.utils.Partition;

/**
 *
 * @author elahi
 */
public class AlphabetTermPage {

    private Partition<String> partition = null;
    private Integer numberOfPages=null;
    private String alpahbetPair=null;

    public AlphabetTermPage( String alpahbetPair, Partition<String> partition) {
        this.alpahbetPair=alpahbetPair.toUpperCase();
        this.partition=partition;
        this.numberOfPages=partition.size();
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

    @Override
    public String toString() {
        return "AlphabetTermPage{" + "partition=" + partition + ", numberOfPages=" + numberOfPages + ", alpahbetPair=" + alpahbetPair + '}';
    }
    

}
