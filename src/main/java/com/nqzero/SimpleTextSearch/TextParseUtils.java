package com.nqzero.SimpleTextSearch;

import java.io.IOException;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.HashSet;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.tartarus.snowball.ext.EnglishStemmer;

/**
 * Created by brad on 6/6/15.
 */
public class TextParseUtils {
    public static boolean dbg = false;
    static HashSet<String> map = new HashSet();


    public static String stemWord(String word) {
        EnglishStemmer stemmer = new EnglishStemmer();
        stemmer.setCurrent(word);
        stemmer.stem();
        return stemmer.getCurrent();
    }
    
    static ArrayList<String> tokenize(String raw) {
        ArrayList<String> words = new ArrayList();
        Analyzer analyzer = new StandardAnalyzer();
        try (TokenStream ts = analyzer.tokenStream("myfield",new StringReader(raw))) {
            ts.reset();
            while (ts.incrementToken()) {
                String term = ts.getAttribute(CharTermAttribute.class).toString();
                // fixme - strip and split at non-word chars
                words.add(term);
            }
            ts.end();
        }
        catch (IOException ex) {}
        return words;
    }
    

    public static void main(String[] args) {
        for (String word:"warm warmer light lighter".split(" "))
            System.out.println(stemWord(word));
        String doc = "don't hello world the quick brown. fox jumped/over the lazy-dog";
        for (String word : tokenize(doc))
                System.out.println(word);
    }
    

}
