/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.lucene;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elahi
 */
public interface LuceneTermSearch {

  public List<String> search(String category,String langCode, String searchQuery) throws IOException, ParseException, Exception ;
   
}
