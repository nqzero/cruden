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
        Map<String,HashSet<ParsedDocument>> map = new HashMap<>();
        for (ParsedDocument doc : corpus) {
            for (String word : doc.getUniqueWords()) {
                if (!map.containsKey(word)) map.put(word, new HashSet());
                map.get(word).add(doc);
            }
        }
        termToPostings = ImmutableMap.copyOf(map);
    }



    private ArrayList<Set<ParsedDocument>> getRelevantDocuments(ParsedDocument searchDoc) {
        ArrayList<Set<ParsedDocument>> retVal = new ArrayList<>();
        for (String word : searchDoc.getUniqueWords())
            if (termToPostings.containsKey(word))
                retVal.add(termToPostings.get(word));
        return retVal;
    }

    public ArrayList<Set<ParsedDocument>> search(String searchTerm, int maxResults) {
        ParsedDocument searchDocument = searchTermParser.parse(searchTerm,0);
        ArrayList<Set<ParsedDocument>> documentsToScanSet = getRelevantDocuments(searchDocument);
        return documentsToScanSet;
    }
    public static InvertedIndex buildIndex(ArrayList<String> docs) {

        DocumentParser parser = new DocumentParser(true,true);

        ArrayList<ParsedDocument> corpus = new ArrayList<>();
        for (int ii=0; ii < docs.size(); ii++)
            corpus.add(parser.parse(docs.get(ii),ii));
        
        InvertedIndex invertedIndex = new InvertedIndex(corpus);

        return invertedIndex;
    }

}
