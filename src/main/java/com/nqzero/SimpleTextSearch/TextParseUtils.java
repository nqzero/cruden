package com.nqzero.SimpleTextSearch;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import org.tartarus.snowball.ext.englishStemmer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

        PTBTokenizer<CoreLabel> ptbt =
                new PTBTokenizer<>(new StringReader(rawText),new CoreLabelTokenFactory(), "untokenizable=noneDelete");
        while (ptbt.hasNext()) {
            CoreLabel label = ptbt.next();
            String str = label.toString();
            if (str == null) {
                continue;
            }

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

}
