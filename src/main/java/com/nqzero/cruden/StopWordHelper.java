package com.nqzero.cruden;


import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by brad on 7/12/15.
 */
public class StopWordHelper {
    //for now only english
    private static HashSet<String> stopwords = getStopWords();

    private static HashSet<String> getStopWords() {

        HashSet<String> retVal = new HashSet<>();
        ClassLoader classLoader = StopWordHelper.class.getClassLoader();

        InputStream stream = classLoader.getResourceAsStream("stopwords/en.txt");
        try (Scanner scanner = new Scanner(stream)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line == null) {
                    continue;
                }
                line = line.trim();
                line = line.toLowerCase();
                if (line.isEmpty()) {
                    continue;
                }
                retVal.add(line);
            }
        }
        return retVal;
    }

    public static boolean isStopWord(String txt) {
        if (txt == null || txt.isEmpty())
            return true;
        return stopwords.contains(txt);
    }

}
