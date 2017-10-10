package com.bradforj287.SimpleTextSearch;

import com.bradforj287.SimpleTextSearch.engine.InvertedIndex;
import com.google.common.base.Stopwatch;
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

        List<Document> documentList = new ArrayList<>();
        Map<String, String> idToBody = new HashMap<>();

        for (int i = 0; i < nList.getLength(); i++) {

            Node n = nList.item(i);

            String body = n.getAttributes().getNamedItem("Body").toString();
            String id = n.getAttributes().getNamedItem("Id").toString();

            Document document = new Document(body, id);
            documentList.add(document);

            idToBody.put(id, body);
        }

        InvertedIndex index = SearchIndexFactory.buildIndex(documentList);

        String searchTerm = "beer";
        SearchResultBatch batch = index.search(searchTerm, 3);

        System.out.println("printing results for term: " + searchTerm);
        for (SearchResult result : batch.getSearchResults()) {
            System.out.println(result.getUniqueIdentifier());
            System.out.println(idToBody.get(result.getUniqueIdentifier().toString()));
        }

        System.exit(0);

    }
}
