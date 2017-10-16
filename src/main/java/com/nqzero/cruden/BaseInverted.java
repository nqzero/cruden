package com.nqzero.cruden;

import com.nqzero.cruden.InvertedIndex.Counts;
import static com.nqzero.cruden.InvertedIndex.join;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class BaseInverted {
    HashMap<String,Counts> index = new HashMap<>();
    Analyzer analyzer = new StandardAnalyzer();

    public void add(int id,String page) {
        ArrayList<String> doc = parse(page);
        for (String word : doc) {
            Counts vals = index.get(word);
            if (vals==null)
                index.put(word,vals = new Counts());
            vals.add(id);
        }
    }
    public Counts search(String term,boolean exact) {
        return exact ? searchExact(term):search(term);
    }
    public Counts searchExact(String term) {
        Counts result = index.get(term);
        return result==null ? new Counts():result;
    }
    public Counts search(String terms) {
        ArrayList<String> search = parse(terms);
        Counts [] data = new Counts[search.size()];

        for (int ii=0; ii < search.size(); ii++)
            data[ii] = index.get(search.get(ii));
        Counts result = join(data);
        return result==null ? new Counts():result;
    }
    public ArrayList<String> parse(String text) {
        if (text==null || text.isEmpty())
            return new ArrayList<>();

        List<String> terms = tokenize(text);

        ArrayList<String> unique = new ArrayList<>();
        HashSet<String> unseen = new HashSet();
        for (String stem : terms)
            if (! stop(stem) && unseen.add(stem))
                unique.add(stem);
        return unique;
    }
    ArrayList<String> tokenize(String doc) {
        ArrayList<String> words = new ArrayList();
        try (TokenStream raw = analyzer.tokenStream(null,doc)) {
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

    boolean stop(String word) { return false; }
    public String stats() { return ""; }
}
