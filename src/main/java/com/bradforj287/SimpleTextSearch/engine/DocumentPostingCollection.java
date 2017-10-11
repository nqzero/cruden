package com.bradforj287.SimpleTextSearch.engine;

import java.util.HashSet;

/**
 * Created by brad on 6/7/15.
 */
public class DocumentPostingCollection {

    private HashSet<ParsedDocument> docset = new HashSet<>();

    public void add(ParsedDocument doc) {
        docset.add(doc);
    }

    public HashSet<ParsedDocument> getUniqueDocuments() {
        return docset;
    }
}
