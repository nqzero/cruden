package com.nqzero.SimpleTextSearch;


import java.util.*;

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
    

    public InvertedIndex(List<String> corpus) {
        // build the reverse index, ie from word to list of documents
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
    static class Ibox {
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

    public static ArrayList<String> parse(String text) {
        if (text==null || text.isEmpty())
            return new ArrayList<>();

        text = text.toLowerCase();
        List<String> terms = TextParseUtils.tokenize(text);

        ArrayList<String> unique = new ArrayList<>();
        HashSet<String> unseen = new HashSet();
        for (String str : terms) {
            String stem = TextParseUtils.stemWord(str);
            if (! StopWordHelper.isStopWord(stem) && unseen.add(stem))
                unique.add(stem);
        }

        return unique;
    }
    public static void main(String[] args) {
        String doc = "don't hello world the quick brown. fox jumped/over the lazy-dog. light lighter warm warmer dark darker";
        ArrayList<String> list = new InvertedIndex(new ArrayList()).parse(doc);
        for (String word : list)
            System.out.println(word);
    }
}
