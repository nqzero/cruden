// copyright 2015 bradforj287 - licensed under the terms of the MIT license

package demo;

import com.nqzero.cruden.InvertedIndex;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;
import org.jsoup.Jsoup;

class Main {
    ArrayList<String> docs = new ArrayList<>();

    public static void main(String args[]) throws Exception {
        new Main().process();
        System.exit(0);
    }
    
    public void process() throws Exception {

        File fXmlFile = new File("t1/Posts.xml");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("row");

        for (int ii = 0; ii < nList.getLength(); ii++) {
            String body = nList.item(ii).getAttributes().getNamedItem("Body").getTextContent();
            String text = Jsoup.parse(body).text();
            docs.add(text);
        }

        InvertedIndex index = new InvertedIndex().add(0,docs);

        String searchTerm = "world";
        ArrayList<Integer> batch = index.search(searchTerm);

        int num = batch.size();
        int count = 0;
        if (false)
            for (Integer id : batch) {
                if (count++ >= 4) break;
                System.out.println(id);
                System.out.println(docs.get(id));
                System.out.println("------------------------------------------------------");
            }
        System.out.println("number of hits: " + num);


    }
}
