package com.bradforj287.SimpleTextSearch;

import com.bradforj287.SimpleTextSearch.engine.Corpus;
import com.bradforj287.SimpleTextSearch.engine.DocumentParser;
import com.bradforj287.SimpleTextSearch.engine.InvertedIndex;
import com.bradforj287.SimpleTextSearch.engine.ParsedDocument;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by brad on 6/6/15.
 */
public class SearchIndexFactory {

    private SearchIndexFactory() {

    }


    public static InvertedIndex buildIndex(Collection<Document> documents) {

        DocumentParser parser = new DocumentParser(true,true);

        ArrayList<ParsedDocument> list = new ArrayList<>();
        for (Document doc : documents)
            list.add(parser.parseDocument(doc));
        
        Corpus corpus = new Corpus(list);
        InvertedIndex invertedIndex = new InvertedIndex(corpus);

        return invertedIndex;
    }
}
