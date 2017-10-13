package com.nqzero.SimpleTextSearch;

import java.io.IOException;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import opennlp.tools.tokenize.WhitespaceTokenizer;
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

    public static List<String> tokenize2(String rawText) {
        List<String> retVal = new ArrayList<>();

        for (String str : tokenizer.tokenize(rawText)) {

            // fixme - also split, eg bottom-shelf -> bottom, shelf, bottomshelf
            // fixme - allow numbers in words
            String old = str;
            str = str.replaceAll("[^a-zA-Z ]", "");

            if (dbg && !str.equals(old) && map.add(old))
                System.out.format("%40s -> %40s\n",old,str);
            
            if (str.isEmpty()) {
                continue;
            }

            retVal.add(str);
        }


        return retVal;
    }
    static WhitespaceTokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
    
    static ArrayList<String> tokenize(String raw) {
        ArrayList<String> words = new ArrayList();
        Analyzer analyzer = new StandardAnalyzer();
        try (TokenStream ts = analyzer.tokenStream("myfield",new StringReader(raw))) {
            ts.reset();
            while (ts.incrementToken()) {
                String term = ts.getAttribute(CharTermAttribute.class).toString();
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
        for (String word : tokenizer.tokenize(doc))
                System.out.println(word);
        tokenize(doc);
    }
    

}
