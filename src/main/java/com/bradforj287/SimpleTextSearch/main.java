package com.bradforj287.SimpleTextSearch;

import com.bradforj287.SimpleTextSearch.engine.DocumentParser;
import com.bradforj287.SimpleTextSearch.engine.InvertedIndex;
import com.bradforj287.SimpleTextSearch.engine.ParsedDocument;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

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

        ArrayList<String> documentList = new ArrayList<>();
        Map<Integer, String> idToBody = new HashMap<>();

        for (int i = 0; i < nList.getLength(); i++) {

            Node n = nList.item(i);

            String body = n.getAttributes().getNamedItem("Body").toString();
            Integer id = i;

            documentList.add(body);
            idToBody.put(id, body);
        }

        InvertedIndex index = InvertedIndex.buildIndex(documentList);

        String searchTerm = "world";
        ArrayList<Set<ParsedDocument>> batch = index.search(searchTerm);

        int num = 0;
        for (Set<ParsedDocument> set : batch){
            num += set.size();
            int count = 0;
            for (ParsedDocument pd : set) {
                Integer id = pd.getUniqueId();
                System.out.println(id);
                System.out.println(documentList.get(id));
                System.out.println("------------------------------------------------------");
                if (++count==4) break;
            }
        }
        System.out.println("number of hits: " + num);

        System.exit(0);

    }
}
