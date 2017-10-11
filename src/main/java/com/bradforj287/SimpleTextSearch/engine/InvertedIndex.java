package com.bradforj287.SimpleTextSearch.engine;


import java.util.*;

/**
 * Created by brad on 6/6/15.
 */
public class InvertedIndex {
    private HashMap<String,ArrayList<Integer>> index = new HashMap<>();
    private DocumentParser searchTermParser = new DocumentParser(false, false);

    public InvertedIndex(List<String> corpus) {
        DocumentParser parser = new DocumentParser(true,true);
        // build the reverse index, ie from word to list of documents
        for (int id=0; id < corpus.size(); id++) {
            String page = corpus.get(id);
            ArrayList<String> doc = parser.parse(page);
            for (String word : doc) {
                if (!index.containsKey(word)) index.put(word, new ArrayList());
                index.get(word).add(id);
            }
        }
    }




    public ArrayList<ArrayList<Integer>> search(String searchTerm) {
        ArrayList<String> search = searchTermParser.parse(searchTerm);
        ArrayList<ArrayList<Integer>> results = new ArrayList<>();
        for (String word : search)
            if (index.containsKey(word))
                results.add(index.get(word));
        return results;
    }


}
