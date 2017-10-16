package com.nqzero.SimpleTextSearch;


import java.io.IOException;
import java.util.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * Created by brad on 6/6/15.
 */
public class InvertedIndex {
    private HashMap<String,Counts> index = new HashMap<>();
    public int numdocs = 0;
    public int numtotal = 0;
    public int numdummy = 0;
    public int nummax = 0;
    static private boolean usemax = true;
    static private boolean usestop = true;
    public boolean printmax = false;
    Analyzer analyzer = new StandardAnalyzer();
    private boolean countStops;

    public static class Counts extends ArrayList<Integer> {
        int count;
    }

    public InvertedIndex() {}
    public InvertedIndex(List<String> corpus) { this(corpus,false); }
    public InvertedIndex(List<String> corpus,boolean $countStops) {
        countStops = $countStops;
        for (int id=0; id < corpus.size(); id++)
            add(id,corpus.get(id));
    }

    public static int count(Counts v1) {
        return v1.count==0 ? v1.size():v1.size();
    }
    public void add(int id,String page) {
        numdocs++;
        int max = (numdocs >> 3) + 200;
        ArrayList<String> doc = parse(page);
        for (String word : doc) {
            numtotal++;
            Counts vals = index.get(word);
            if (vals==null)
                index.put(word,vals = new Counts());
            if (vals.count > 0) {
                nummax++;
                vals.count++;
            }
            else {
                if (usemax & vals.size() > max) {
                    if (printmax) System.out.format("trim: %10s ... %4d of %4d\n",word,vals.size(),numdocs);
                    vals.count = vals.size();
                    nummax += vals.size();
                    numdummy++;
                    vals.clear();
                }
                else
                    vals.add(id);
            }
        }
    }

    public String stats() {
        return String.format("stats: %4d, %4d, %4d, %4d",numdocs,numtotal,numdummy,nummax);
    }

    public static <TT extends Counts> TT join(TT ... lists) {
        ArrayList<Integer> valid = new ArrayList();
        for (int ii=0; ii < lists.length; ii++)
            if (lists[ii] != null && lists[ii].count==0)
                valid.add(ii);
        if (valid.isEmpty())
             return null;
        TT result = lists[valid.get(0)];
        if (valid.size()==1)
            return result;
        HashMap<Integer,Ibox> found = new HashMap();
        int ii = 0, last = valid.size()-1;
        for (Integer index : lists[valid.get(ii)])
            found.put(index,new Ibox(1));
        for (ii=1; ii < last; ii++)
            for (Integer index : lists[valid.get(ii)]) {
                Ibox box = found.get(index);
                if (box != null) box.val++;
            }
        result.clear();
        for (Integer index : lists[valid.get(last)]) {
            Ibox box = found.get(index);
            if (box != null && box.val==last)
                result.add(index);
        }
        return result;
    }

    public static class Ibox {
        public int val;
        public Ibox() {};
        public Ibox(int $val) { val = $val; };
    }

    public Counts search(String term,boolean exact) {
        return exact ? searchExact(term):search(term);
    }
    public Counts searchExact(String term) {
        Counts result = index.get(term);
        return result==null ? new Counts():result;
    }
    public Counts search(String searchTerm) {
        ArrayList<String> search = parse(searchTerm);
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
    boolean stop(String word) {
        if (! usestop) return false;
        boolean stop = StopWordHelper.isStopWord(word);
        if (countStops & stop) {
            Ibox box = stopMap.get(word);
            if (box==null)
                stopMap.put(word,box = new Ibox());
            box.val++;
        }
        return stop;
    }
    private HashMap<String,Ibox> stopMap = new HashMap<>();
    public HashMap<String,Counts> copyIndex() {
        return new HashMap<>(index);
    }
    public HashMap<String,Ibox> copyStop() {
        return new HashMap<>(stopMap);
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

    public static void main(String[] args) {
        String doc = "don't hello world the quick brown. fox jumped/over the lazy-dog. light lighter warm warmer dark darker";
        ArrayList<String> list = new InvertedIndex(new ArrayList()).parse(doc);
        for (String word : list)
            System.out.println(word);
        String adj = "warm warmer light lighter ";
        for (String word : new InvertedIndex().tokenize(adj+doc))
                System.out.println(word);
    }
}
