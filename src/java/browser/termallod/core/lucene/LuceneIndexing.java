/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.lucene;

/**
 *
 * @author elahi
 */

import browser.termallod.core.input.LangSpecificBrowser;
import browser.termallod.core.input.Browser;
import browser.termallod.core.input.TermallodBrowser;
import browser.termallod.core.api.LuceneTermSearch;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

//https://github.com/macluq/helloLucene
//https://mygeekjourney.com/programming-notes/apache-lucene-how-to-sort-results-by-alphabetical-order/
public class LuceneIndexing implements LuceneTermSearch {

    private StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
    private Map<String, Browser> indexedBrowsers = new HashMap<String, Browser>();

    public LuceneIndexing(TermallodBrowser browsers) throws IOException, ParseException, Exception {
        Map<String, Browser> inputBrowsers = browsers.getBrowserInfos();

        if (!inputBrowsers.isEmpty()) {
            this.indexedBrowsers = createIndexForEachCategory(inputBrowsers);
        } else {
            throw new Exception("No browser data found for creating index!!");
        }

    }

    private Map<String, Browser> createIndexForEachCategory(Map<String, Browser> input) throws IOException, ParseException, Exception {
        Integer countIndex = 0;
        Map<String, Browser> output = new TreeMap<String, Browser>();
        for (String category : input.keySet()) {
            Browser browsers = input.get(category);
            Map<String, LangSpecificBrowser> langTermUrls = new TreeMap<String, LangSpecificBrowser>();
            for (String langCode :browsers.getLangTermUrls().keySet()) {
                LangSpecificBrowser land = browsers.getLangTermUrls().get(langCode);
                Map<String, String> datas = new HashMap<String, String>();
                for (String term : land.getTermUrls().keySet()) {
                    datas.put((countIndex++).toString(), term);
                }
                Directory index = loadData(datas);
                land.setIndex(index);
                langTermUrls.put(langCode, land);
                
            }
             Browser indexedBrowsers = new Browser(browsers,langTermUrls);
             output.put(category, indexedBrowsers);
        }
        return output;
    }

    @Override
    public List<String> search(String category, String langCode, String searchQuery) throws IOException, ParseException, Exception {
        Browser browserInfo = indexedBrowsers.get(category);
        LangSpecificBrowser langSpecificBrowser = browserInfo.getLangTermUrls(langCode);
        System.out.println(langSpecificBrowser.getTermUrls());
        Directory index = langSpecificBrowser.getIndex();
        Query query = createQuery(analyzer, searchQuery);
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        Pair<IndexSearcher, ScoreDoc[]> pair = getScore(searcher, query);
        List<String> terms = scoreGenerator(pair.getKey(), pair.getValue());
        System.out.println("result:" + terms.toString());
        reader.close();
        return terms;
    }

    private Directory loadData(Map<String, String> datas) throws IOException, ParseException {
        Directory index = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        IndexWriter w = new IndexWriter(index, config);

        for (String key : datas.keySet()) {
            String value = datas.get(key);
            addDoc(w, value, key);
        }
        w.close();
        return index;

    }

    private Query createQuery(StandardAnalyzer analyzer, String querystr) throws IOException, ParseException {
        Query query = null;
        try {
            query = new QueryParser(Version.LUCENE_40, "title", analyzer).parse(querystr);
        } catch (org.apache.lucene.queryparser.classic.ParseException e) {
            e.printStackTrace();
        }

        return query;
    }

    private Pair<IndexSearcher, ScoreDoc[]> getScore(IndexSearcher searcher, Query query) throws IOException, ParseException {
        int hitsPerPage = 10;
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        searcher.search(query, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        return new MutablePair<IndexSearcher, ScoreDoc[]>(searcher, hits);
    }

    private List<String> scoreGenerator(IndexSearcher searcher, ScoreDoc[] hits) throws IOException, ParseException {
        List<String> termList = new ArrayList<String>();
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            String foundStr = (i + 1) + ". " + d.get("isbn") + "\t" + d.get("title");
            termList.add(foundStr);
        }

        return termList;
    }

    private void addDoc(IndexWriter w, String title, String isbn) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }

    /*Map<String, String> datas = new HashMap<String, String>();
        datas.put("1", "Lucene in Action");
        datas.put("2", "Lucene for Dummies");
        datas.put("3", "Managing Gigabytes");
        datas.put("4", "The Art of Computer Science");
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
        Directory index = luceneJavaImpl.loadData(analyzer, datas);
        Query query = luceneJavaImpl.query(analyzer, querystr);
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        Pair<IndexSearcher, ScoreDoc[]> pair = luceneJavaImpl.getScore(searcher, query);
        List<String> terms = luceneJavaImpl.search(pair.getKey(), pair.getValue());
        System.out.println(terms.toString());
        reader.close();*/

    
}
