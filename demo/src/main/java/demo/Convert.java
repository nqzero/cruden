// copyright 2017 nqzero - licensed under the terms of the MIT license

package demo;

import java.io.File;
import java.io.FileWriter;

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
