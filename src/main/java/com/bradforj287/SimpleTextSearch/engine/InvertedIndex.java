package com.bradforj287.SimpleTextSearch.engine;

import com.bradforj287.SimpleTextSearch.*;
import com.google.common.collect.ImmutableMap;

import java.util.*;

/**
 * Created by brad on 6/6/15.
 */
public class InvertedIndex {
    private ImmutableMap<String,HashSet<ParsedDocument>> termToPostings;
    private DocumentParser searchTermParser = new DocumentParser(false, false);

    public InvertedIndex(List<ParsedDocument> corpus) {
        // build term -> posting map
        Map<String,HashSet<ParsedDocument>> termToPostingsMap = new HashMap<>();
        for (ParsedDocument document : corpus) {
            for (DocumentTerm term : document.getDocumentTerms()) {
                final String word = term.getWord();
                if (!termToPostingsMap.containsKey(word))
                    termToPostingsMap.put(word, new HashSet());

                termToPostingsMap.get(word).add(document);
            }
        }
        termToPostings = ImmutableMap.copyOf(termToPostingsMap);
    }



    private ArrayList<Set<ParsedDocument>> getRelevantDocuments(ParsedDocument searchDoc) {
        ArrayList<Set<ParsedDocument>> retVal = new ArrayList<>();
        for (String word : searchDoc.getUniqueWords())
            if (termToPostings.containsKey(word))
                retVal.add(termToPostings.get(word));
        return retVal;
    }

    public ArrayList<Set<ParsedDocument>> search(String searchTerm, int maxResults) {
        ParsedDocument searchDocument = searchTermParser.parseDocument(searchTerm,new Object());
        ArrayList<Set<ParsedDocument>> documentsToScanSet = getRelevantDocuments(searchDocument);
        return documentsToScanSet;
    }

}
