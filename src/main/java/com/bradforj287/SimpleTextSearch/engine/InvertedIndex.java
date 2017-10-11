package com.bradforj287.SimpleTextSearch.engine;


import java.util.*;

/**
 * Created by brad on 6/6/15.
 */
public class InvertedIndex {
    private HashMap<String,ArrayList<Integer>> index = new HashMap<>();

    public InvertedIndex(List<String> corpus) {
        // build the reverse index, ie from word to list of documents
        for (int id=0; id < corpus.size(); id++) {
            String page = corpus.get(id);
            ArrayList<String> doc = parse(page);
            for (String word : doc) {
                if (!index.containsKey(word)) index.put(word, new ArrayList());
                index.get(word).add(id);
            }
        }
    }

    public ArrayList<ArrayList<Integer>> search(String searchTerm) {
        ArrayList<String> search = parse(searchTerm);
        ArrayList<ArrayList<Integer>> results = new ArrayList<>();
        for (String word : search)
            if (index.containsKey(word))
                results.add(index.get(word));
        return results;
    }

    public static ArrayList<String> parse(String text) {
        if (text==null || text.isEmpty())
            return new ArrayList<>();

        text = text.toLowerCase();
        List<String> terms = TextParseUtils.tokenize(text);

        ArrayList<String> unique = new ArrayList<>();
        HashSet<String> unseen = new HashSet();
        for (String str : terms) {
            String stem = TextParseUtils.stemWord(str);
            if (! StopWordHelper.isStopWord(stem) && unseen.add(stem))
                unique.add(stem);
        }

        return unique;
    }
}
