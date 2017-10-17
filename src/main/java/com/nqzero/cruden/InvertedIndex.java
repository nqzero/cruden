// copyright 2017 nqzero - licensed under the terms of the MIT license

package com.nqzero.cruden;


import java.util.*;

public class InvertedIndex extends BaseInverted {
    public int numdocs = 0;
    public int numtotal = 0;
    public int numdummy = 0;
    public int nummax = 0;
    static private boolean usemax = true;
    static private boolean usestop = true;
    public boolean printmax = false;
    private boolean countStops;

    public static class Counts extends ArrayList<Integer> {
        int count;
    }
    public static int count(Counts v1) {
        return v1.count==0 ? v1.size():v1.count;
    }

    public InvertedIndex() {}
    public InvertedIndex(boolean countStops) { this.countStops = countStops; }

    public static <TT extends BaseInverted> TT add(TT invert,int first,List<String> corpus) {
        for (int id=0; id < corpus.size(); id++)
            invert.add(first+id,corpus.get(id));
        return invert;
    }
    public InvertedIndex add(int first,List<String> corpus) {
        return add(this,first,corpus);
    }

    public void add(Integer id,String page) {
        if (id==null) id = numdocs;
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
    public static void main(String[] args) {
        String doc = "don't hello world the quick brown. fox jumped/over the lazy-dog. light lighter warm warmer dark darker";
        ArrayList<String> list = new InvertedIndex().parse(doc);
        for (String word : list)
            System.out.println(word);
        String adj = "warm warmer light lighter ";
        for (String word : new InvertedIndex().tokenize(adj+doc))
                System.out.println(word);
    }
}
