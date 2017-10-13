package example;

import com.nqzero.SimpleTextSearch.InvertedIndex;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;

public class Example {
    ArrayList<String> docs;
    long time;
    long time() { return time = new Date().getTime(); }

    public Example(ArrayList<String> docs) {
        this.docs = docs;
    }
    
    public static void main(String[] args) throws Exception {
        ArrayList<String> docs = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("doc/Posts.txt"));
        for(String line; (line = br.readLine()) != null; )
            docs.add(line);

        Example main = new Example(docs);
        main.run();
    }
    
    void run() {
        InvertedIndex index = new InvertedIndex(docs);
        query(index,"world taste",1);
        query(index,"hops",1);
        query(index,"co2 hops",1);
        query(index,"darker warmer lighter",1);

        time();
        int last = 10;
        for (int jj=0; jj <= last; jj++)
            loop(jj,jj > last-5);
    }
    
    void loop(int kdoc,boolean scan) {
        InvertedIndex index = new InvertedIndex(docs);

        // sprinkle a little salt to try to keep the jit honest
        index.add(docs.size(),docs.get(kdoc)+" loving the flavour");

        String searchTerm = "world";
        int batch = index.search(searchTerm).size();

        double delta = (-time + time())/1000.0;
        int total = scan ? countSquared(index):0;

        double d2 = (-time + time())/1000.0;
        System.out.format("%5.2f +%5.2f sec %s, number of hits: %4d --> %d\n",
                delta,d2,index.stats(),batch,total);
    }
    
    int countSquared(InvertedIndex index) {
        int total = 0;
        for (String doc : docs)
            for (String word : doc.split(" "))
                total += index.search(word).size();
        return total;
    }
    
    void query(InvertedIndex index,String query,int max) {
        ArrayList<Integer> res = index.search(query);
        int num = Math.min(max,res.size());
        System.out.format("%40s --> %4d%s",query,res.size(),num==0 ? "\n":":");
        for (int ii=0; ii < num; ii++)
            System.out.println("\t"+docs.get(res.get(ii)));
    }
}
