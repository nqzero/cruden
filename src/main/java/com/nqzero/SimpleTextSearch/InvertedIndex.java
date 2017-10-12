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
    public ArrayList<Integer> join(ArrayList<Integer> ... list) {
        // fixme - actually join
        return list.length==0 || list[0]==null ? new ArrayList<>():list[0];
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
}
