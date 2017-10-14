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
    private HashMap<String,ArrayList<Integer>> index = new HashMap<>();
    public int numdocs = 0;
    public int numtotal = 0;
    public int numdummy = 0;
    public int nummax = 0;
    static private ArrayList<Integer> dummy = new ArrayList<>();
    static private boolean usemax = true;
    public boolean printmax = false;
    Analyzer analyzer = new StandardAnalyzer();
    public boolean countStops = false;

    

    public InvertedIndex() {}
    public InvertedIndex(List<String> corpus) { this(corpus,false); }
    public InvertedIndex(List<String> corpus,boolean $countStops) {
        countStops = $countStops;
        for (int id=0; id < corpus.size(); id++)
            add(id,corpus.get(id));
    }

    
    public void add(int id,String page) {
        numdocs++;
        int max = (numdocs >> 3) + 200;
        ArrayList<String> doc = parse(page);
        for (String word : doc) {
            numtotal++;
            ArrayList<Integer> vals = index.get(word);
            if (vals==null)
                index.put(word,vals = new ArrayList());
            if (vals==dummy) nummax++;
            else {
                if (usemax & vals.size() > max) {
                    if (printmax) System.out.format("trim: %10s ... %4d of %4d\n",word,vals.size(),numdocs);
                    nummax += vals.size();
                    numdummy++;
                    index.put(word,dummy);
                }
                else
                    vals.add(id);
            }
        }
    }

    public String stats() {
        return String.format("stats: %4d, %4d, %4d, %4d",numdocs,numtotal,numdummy,nummax);
    }

    public ArrayList<Integer> join(ArrayList<Integer> ... lists) {
        ArrayList<Integer> valid = new ArrayList();
        for (int ii=0; ii < lists.length; ii++)
            if (lists[ii] != null & lists[ii] != dummy)
                valid.add(ii);
        if (valid.isEmpty())
             return new ArrayList<>();
        if (valid.size()==1)
            return lists[valid.get(0)];
        HashMap<Integer,Ibox> found = new HashMap();
        ArrayList<Integer> result = new ArrayList<>();
        int ii = 0, last = valid.size()-1;
        for (Integer index : lists[valid.get(ii)])
            found.put(index,new Ibox(1));
        for (ii=1; ii < last; ii++)
            for (Integer index : lists[valid.get(ii)]) {
                Ibox box = found.get(index);
                if (box != null) box.val++;
            }
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

    public ArrayList<Integer> search(String searchTerm) {
        ArrayList<String> search = parse(searchTerm);
        ArrayList<Integer> [] data = new ArrayList[search.size()];

        for (int ii=0; ii < search.size(); ii++)
            data[ii] = index.get(search.get(ii));
        return join(data);
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
    public HashMap<String,ArrayList<Integer>> copyIndex() {
        return new HashMap<>(index);
    }
    public HashMap<String,Ibox> copyStop() {
        return new HashMap<>(stopMap);
    }

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
        String doc = "don't hello world the quick brown. fox jumped/over the lazy-dog. light lighter warm warmer dark darker";
        ArrayList<String> list = new InvertedIndex(new ArrayList()).parse(doc);
        for (String word : list)
            System.out.println(word);
        String adj = "warm warmer light lighter ";
        for (String word : new InvertedIndex().tokenize(adj+doc))
                System.out.println(word);
    }
}
