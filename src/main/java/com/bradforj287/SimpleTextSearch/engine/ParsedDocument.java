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
    private HashSet<String> uniqueWords;
    private Integer uniqueId;

    public ParsedDocument(List<DocumentTerm> documentTerms,Integer uniqueId) {
        Preconditions.checkNotNull(uniqueId);
        Preconditions.checkNotNull(documentTerms);
        this.uniqueId = uniqueId;
        HashMap<String, Integer> wordFrequency = new HashMap<>();

        for (DocumentTerm t : documentTerms) {
            String word = t.getWord();
            if (!wordFrequency.containsKey(word))
                wordFrequency.put(word, 0);

            int count = wordFrequency.get(word);
            wordFrequency.put(word, count + 1);
        }

        uniqueWords = getUniqueWordsHashSet(documentTerms);
    }





    public Set<String> getUniqueWords() {
        return uniqueWords;
    }

    private HashSet<String> getUniqueWordsHashSet(List<DocumentTerm> terms) {

        HashSet<String> w = new HashSet<>();
        for (DocumentTerm t : terms) {
            w.add(t.getWord());
        }
        return w;
    }

    public Integer getUniqueId() {
        return uniqueId;
    }
}
