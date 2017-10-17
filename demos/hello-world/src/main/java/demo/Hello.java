// copyright 2017 nqzero - licensed under the terms of the MIT license

package demo;

import com.nqzero.cruden.InvertedIndex;
import java.util.ArrayList;

public class Hello {
    public static void main(String args[]) throws Exception {
        InvertedIndex index = new InvertedIndex();
        index.add(0,"the quick brown fox");
        index.add(1,"in pursuit of love");
        index.add(2,"jumped over the lazy dog");
        index.add(3,"a battle of wits and possession");

        ArrayList<Integer> batch = index.search("jump lazy");
        for (int key : batch)
            System.out.println(key);
    }
}
