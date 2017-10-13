package com.nqzero.SimpleTextSearch;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashSet;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * Created by brad on 6/6/15.
 */
public class TextParseUtils {
    Analyzer analyzer = new StandardAnalyzer();

    ArrayList<String> tokenize(String doc) {
        ArrayList<String> words = new ArrayList();
        try (TokenStream raw = analyzer.tokenStream("myfield",doc)) {
            KStemFilter ts = new KStemFilter(raw);
            CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
            ts.reset();
            while (ts.incrementToken())
                // fixme - strip and split at non-word chars
                words.add(term.toString());
            ts.end();
        }
        catch (IOException ex) {}
        return words;
    }
    

    public static void main(String[] args) {
        String adj = "warm warmer light lighter ";
        String doc = "don't hello world the quick brown. fox jumped/over the lazy-dog";
        for (String word : new TextParseUtils().tokenize(adj+doc))
                System.out.println(word);
    }
    

}
