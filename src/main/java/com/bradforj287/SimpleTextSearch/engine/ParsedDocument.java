package com.bradforj287.SimpleTextSearch.engine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by brad on 6/6/15.
 */
public class ParsedDocument {
    private HashSet<String> uniqueWords = new HashSet<>();

    public ParsedDocument(List<String> terms) {
        for (String term : terms)
            uniqueWords.add(term);
    }





    public Set<String> getUniqueWords() {
        return uniqueWords;
    }

}
