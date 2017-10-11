package com.bradforj287.SimpleTextSearch.engine;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by brad on 6/6/15.
 */
public class ParsedDocument {
    private HashSet<String> uniqueWords = new HashSet<>();
    private Integer uniqueId;

    public ParsedDocument(List<DocumentTerm> terms,Integer uniqueId) {
        Preconditions.checkNotNull(uniqueId);
        Preconditions.checkNotNull(terms);
        this.uniqueId = uniqueId;
        for (DocumentTerm term : terms)
            uniqueWords.add(term.getWord());
    }





    public Set<String> getUniqueWords() {
        return uniqueWords;
    }

    public Integer getUniqueId() {
        return uniqueId;
    }
}
