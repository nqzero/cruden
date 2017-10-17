// copyright 2015 bradforj287 - licensed under the terms of the MIT license

package com.nqzero.SimpleTextSearch;

import com.nqzero.cruden.InvertedIndex;
import java.util.ArrayList;
import org.junit.Test;

public class DocumentParserTest {

    @Test
    public void testParseHtml() {
        String raw = "Body=\"<p><img src=\"http://i.stack.imgur.com/M64qK.png\" alt=\"animal king takeover\"></p>\n" +
                "\n" +
                "<p>In this image we see six flags behind the animal king.\n" +
                "I think I recognize some of them:\n" +
                "First - Argentina;\n" +
                "Third - Bulgaria;\n" +
                "Fifth - Sweden;\n" +
                "Sixth - Austria;</p>\n" +
                "\n" +
                "<p>Am I correct, and which countries' flags are the second and fourth?</p>\n" +
                "\"";

        ArrayList<String> terms = new InvertedIndex().parse(raw);

        boolean foundPunctuation = false;
        for (String term : terms) {
            if (term.equals(";")) {
                foundPunctuation = true;
                break;
            }
        }

        assert (foundPunctuation == false);
    }


}
