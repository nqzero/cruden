package com.bradforj287.SimpleTextSearch;

import com.bradforj287.SimpleTextSearch.engine.InvertedIndex;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;
import org.jsoup.Jsoup;

/**
 * Created by brad on 6/10/15.
 */
class main {

    public static void main(String args[]) throws Exception {

        File fXmlFile = new File("t1/Posts.xml");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

        NodeList nList = doc.getElementsByTagName("row");

        ArrayList<String> docs = new ArrayList<>();

        for (int i = 0; i < nList.getLength(); i++) {

            Node n = nList.item(i);

            String body = n.getAttributes().getNamedItem("Body").toString();
            String text = Jsoup.parse(body).text();

            docs.add(text);
        }

        InvertedIndex index = new InvertedIndex(docs);

        String searchTerm = "world";
        ArrayList<ArrayList<Integer>> batch = index.search(searchTerm);

        int num = 0;
        for (ArrayList<Integer> set : batch){
            num += set.size();
            int count = 0;
            if (false)
            for (Integer id : set) {
                System.out.println(id);
                System.out.println(docs.get(id));
                System.out.println("------------------------------------------------------");
                if (++count==4) break;
            }
        }
        System.out.println("number of hits: " + num);

        System.exit(0);

    }
}
