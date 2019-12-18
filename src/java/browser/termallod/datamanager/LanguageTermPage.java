/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.datamanager;

import browser.termallod.utils.Partition;

/**
 *
 * @author elahi
 */
public class LanguageTermPage {

    private String language = null;
    private Partition<String> partition = null;
    private Integer numberOfPages=null;

    public LanguageTermPage( String language, Partition<String> partition) {
        this.language=language;
        this.partition=partition;
        this.numberOfPages=partition.size();
    }

    public String getLanguage() {
        return language;
    }

    public Partition<String> getPartition() {
        return partition;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }
    

}
