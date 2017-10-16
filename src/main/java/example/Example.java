package example;

import com.nqzero.cruden.InvertedIndex;
import static com.nqzero.cruden.InvertedIndex.count;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public class Example {
    ArrayList<String> docs;
    long time;
    long time() { return time = new Date().getTime(); }

    public Example() { docs = new ArrayList(); }
    public Example(ArrayList<String> docs) {
        this.docs = docs;
    }
    
    public static void main(String[] args) throws Exception {
        Example example = new Example();
        example.read();
        example.run();
    }
    public Example read() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("doc/Posts.txt"));
            for(String line; (line = br.readLine()) != null; )
                docs.add(line);
        }
        catch (Exception ex) { throw new RuntimeException(ex); }
        return this;
    }
    
    void run() {
        InvertedIndex index = new InvertedIndex(true).add(0,docs);
        query(index,"world taste",1);
        query(index,"hops",1);
        query(index,"co2 hops",1);
        query(index,"darker warmer lighter",1);
        query(index,"dark warm light",1);
        printStop(index,500);
        System.out.println("---------------------------------------------------------------------");
        printOccur(index,200);

        time();
        int last = 30, step = last/3;
        for (int jj=0; jj <= last; jj++)
            loop(jj,jj/step);
    }
    
    void loop(int kdoc,int scan) {
        InvertedIndex index = new InvertedIndex().add(0,docs);

        // sprinkle a little salt to try to keep the jit honest
        index.add(docs.size(),docs.get(kdoc)+" loving the flavour");

        String searchTerm = "world";
        int batch = index.search(searchTerm).size();

        // fixme - the order of scan effects the JIT, so doing the fast version first
        //   no good way to characterize overall performance which would be a mix of the 2
        double delta = (-time + time())/1000.0;
        int total = scan > 0 ? countSquared(index,scan < 2):0;

        double d2 = (-time + time())/1000.0;
        System.out.format("%5.2f +%5.2f sec %s, number of hits: %4d --> %d\n",
                delta,d2,index.stats(),batch,total);
    }
    
    int countSquared(InvertedIndex index,boolean exact) {
        int total = 0;
        for (String doc : docs)
            for (String word : doc.split(" "))
                total += index.search(word,exact).size();
        return total;
    }
    
    void query(InvertedIndex index,String query,int max) {
        ArrayList<Integer> res = index.search(query);
        int num = Math.min(max,res.size());
        System.out.format("%40s --> %4d%s",query,res.size(),num==0 ? "\n":":");
        for (int ii=0; ii < num; ii++)
            System.out.println("\t"+docs.get(res.get(ii)));
    }
    public static void printStop(InvertedIndex index,int num) {
        foreach(num,
                sort(index.copyStop(),(x1,x2)
                        -> x2.getValue().val-x1.getValue().val).iterator(),
                item
                        -> System.out.format("%40s -> %5d\n",item.getKey(),item.getValue().val));
    }
    public static void printOccur(InvertedIndex index,Integer num) {
        foreach(num,
                sort(index.copyIndex(),(x1,x2)
                        -> count(x2.getValue())-count(x1.getValue())).iterator(),
                item
                        -> System.out.format("%40s -> %5d\n",item.getKey(),count(item.getValue())));
    }

    public static <KK,VV> ArrayList<Map.Entry<KK,VV>> sort(Map<KK,VV> map,Comparator<? super Map.Entry<KK,VV>> cmp) {
        ArrayList<Map.Entry<KK,VV>> array = new ArrayList<>(map.entrySet());
        if (cmp!=null) array.sort(cmp);
        return array;
    }
    public static <TT> void foreach(Integer max,Iterator<TT> iter,Consumer<TT> action) {
        if (max==null) max = Integer.MAX_VALUE;
        for (int ii=0; ii < max && iter.hasNext(); ii++)
            action.accept(iter.next());
    }
    
}
