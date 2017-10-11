package com.bradforj287.SimpleTextSearch.engine;

import java.util.ArrayList;

/**
 * Created by brad on 6/6/15.
 */
public class ParsedDocument {
    private ArrayList<String> uniqueWords = new ArrayList();

    public ParsedDocument(ArrayList<String> terms) {
        uniqueWords = terms;
    }





    public ArrayList<String> getUniqueWords() {
        return uniqueWords;
    }

}
