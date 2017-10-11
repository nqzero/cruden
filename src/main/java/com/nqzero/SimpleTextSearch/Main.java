package com.nqzero.SimpleTextSearch;

import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;
import org.jsoup.Jsoup;

/**
 * Created by brad on 6/10/15.
 */
class Main {

    public static void main(String args[]) throws Exception {

        File fXmlFile = new File("t1/Posts.xml");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("row");
        ArrayList<String> docs = new ArrayList<>();

        for (int ii = 0; ii < nList.getLength(); ii++) {
            String body = nList.item(ii).getAttributes().getNamedItem("Body").getTextContent();
            String text = Jsoup.parse(body).text();
            docs.add(text);
        }

        InvertedIndex index = new InvertedIndex(docs);

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

        System.exit(0);

    }
}
