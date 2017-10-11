package com.bradforj287.SimpleTextSearch.engine;

import com.bradforj287.SimpleTextSearch.*;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;

import java.util.*;

/**
 * Created by brad on 6/6/15.
 */
public class InvertedIndex {
    private ImmutableMap<String, DocumentPostingCollection> termToPostings;
    private DocumentParser searchTermParser;

    public InvertedIndex(List<ParsedDocument> corpus) {
        init(corpus);
        searchTermParser = new DocumentParser(false, false);
    }

    private void init(List<ParsedDocument> corpus) {
        // build term -> posting map
        Map<String, DocumentPostingCollection> termToPostingsMap = new HashMap<>();
        for (ParsedDocument document : corpus) {
            for (DocumentTerm documentTerm : document.getDocumentTerms()) {
                final String word = documentTerm.getWord();
                if (!termToPostingsMap.containsKey(word))
                    termToPostingsMap.put(word, new DocumentPostingCollection());

                termToPostingsMap.get(word).add(document);
            }
        }
        termToPostings = ImmutableMap.copyOf(termToPostingsMap);
    }



    private ArrayList<Set<ParsedDocument>> getRelevantDocuments(ParsedDocument searchDoc) {
        ArrayList<Set<ParsedDocument>> retVal = new ArrayList<>();
        for (String word : searchDoc.getUniqueWords()) {
            if (termToPostings.containsKey(word)) {
                retVal.add(termToPostings.get(word).getUniqueDocuments());
            }
        }
        return retVal;
    }

    public ArrayList<Set<ParsedDocument>> search(String searchTerm, int maxResults) {
        ParsedDocument searchDocument = searchTermParser.parseDocument(new Document(searchTerm, new Object()));
        ArrayList<Set<ParsedDocument>> documentsToScanSet = getRelevantDocuments(searchDocument);
        return documentsToScanSet;
    }

}
