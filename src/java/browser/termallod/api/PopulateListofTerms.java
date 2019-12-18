/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.api;

import java.util.List;
import org.jsoup.nodes.Document;
import browser.termallod.datamanager.DataManager;

/**
 *
 * @author elahi
 */
public interface PopulateListofTerms {
    public Document changeBody(Document oldDocument,String language);
    
    
}
