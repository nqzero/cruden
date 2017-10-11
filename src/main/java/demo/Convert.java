/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
