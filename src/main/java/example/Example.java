package example;

import com.nqzero.SimpleTextSearch.InvertedIndex;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;

public class Example {
    
    public static void main(String[] args) throws Exception {
        ArrayList<String> docs = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("doc/Posts.txt"));
        for(String line; (line = br.readLine()) != null; )
            docs.add(line);

        long time = new Date().getTime();
        int last = 10;
        for (int jj=0; jj <= last; jj++) {
            InvertedIndex index = new InvertedIndex(docs);
            index.add(docs.size(),docs.get(jj)+" loving the flavour");
            String searchTerm = "world";
            ArrayList<Integer> batch = index.search(searchTerm);
            for (int ii=0; jj==0 && ii < 4; ii++)
                System.out.println(docs.get(batch.get(ii)));
            
            double delta = (-time + (time=new Date().getTime()))/1000.0;
            int total = 0;
            if (jj > last-5)
                for (String doc : docs)
                    for (String word : doc.split(" "))
                        total += index.search(word).size();

            double d2 = (-time + (time=new Date().getTime()))/1000.0;
            System.out.format("%5.2f +%5.2f sec %s, number of hits: %4d --> %d\n",
                    delta,d2,index.stats(),batch.size(),total);
        }
    }
}
