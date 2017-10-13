package com.nqzero.SimpleTextSearch;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import org.tartarus.snowball.ext.englishStemmer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import opennlp.tools.tokenize.WhitespaceTokenizer;

/**
 * Created by brad on 6/6/15.
 */
public class TextParseUtils {
    public static boolean dbg = false;
    static HashSet<String> map = new HashSet();


    public static String stemWord(String word) {
        englishStemmer stemmer = new englishStemmer();
        stemmer.setCurrent(word);
        stemmer.stem();
        return stemmer.getCurrent();
    }

    public static List<String> tokenize(String rawText) {
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

    public static void main(String[] args) {
        for (String word : tokenizer.tokenize("don't hello world the quick brown. fox jumped/over the lazy-dog"))
                System.out.println(word);
    }
    

}
