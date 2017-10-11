package com.bradforj287.SimpleTextSearch.engine;


import java.util.*;

/**
 * Created by brad on 6/6/15.
 */
public class InvertedIndex {
    private HashMap<String,HashSet<Integer>> index = new HashMap<>();
    private DocumentParser searchTermParser = new DocumentParser(false, false);

    public InvertedIndex(List<ParsedDocument> corpus) {
        // build the reverse index, ie from word to list of documents
        for (ParsedDocument doc : corpus) {
            for (String word : doc.getUniqueWords()) {
                if (!index.containsKey(word)) index.put(word, new HashSet());
                index.get(word).add(doc.getUniqueId());
            }
        }
    }




    public ArrayList<Set<Integer>> search(String searchTerm) {
        ParsedDocument searchDoc = searchTermParser.parse(searchTerm,0);
        ArrayList<Set<Integer>> results = new ArrayList<>();
        for (String word : searchDoc.getUniqueWords())
            if (index.containsKey(word))
                results.add(index.get(word));
        return results;
    }

    public static InvertedIndex buildIndex(ArrayList<String> docs) {
        DocumentParser parser = new DocumentParser(true,true);

        ArrayList<ParsedDocument> corpus = new ArrayList<>();
        for (int ii=0; ii < docs.size(); ii++)
            corpus.add(parser.parse(docs.get(ii),ii));
        
        return new InvertedIndex(corpus);
    }

}
