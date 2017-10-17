// copyright 2017 nqzero - licensed under the terms of the MIT license

package demo;

import java.io.File;
import java.io.FileWriter;

// convert the stackexchange Posts.xml into Posts.txt
// usage:
//   download https://archive.org/download/stackexchange/beer.stackexchange.com.7z
//   extract into directory t1 using 7z
//   run this program, eg mvn exec:java -Dexec.mainClass=demo.Convert
//   this will produce test1.txt
//   copy this to doc/Posts.txt to use it with Example.java

public class Convert {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.process();
        
        File file = new File("test1.txt");
        FileWriter fileWriter = new FileWriter(file);
        for (String line : main.docs) {
            String clean = line.replace("\n","");
            fileWriter.write(clean + "\n");
        }
        fileWriter.flush();
        fileWriter.close();
    }

}
