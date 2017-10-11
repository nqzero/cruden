package demo;

import com.nqzero.SimpleTextSearch.InvertedIndex;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Demo {
    
    public static void main(String[] args) throws Exception {
        ArrayList<String> docs = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("doc/Posts.txt"));
        for(String line; (line = br.readLine()) != null; )
            docs.add(line);

        InvertedIndex index = new InvertedIndex(docs);

        String searchTerm = "world";
        ArrayList<Integer> batch = index.search(searchTerm);
        for (int ii=0; ii < 4; ii++)
            System.out.println(docs.get(batch.get(ii)));

        System.out.println("number of hits: " + batch.size());
        
    }
}
