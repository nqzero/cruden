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

        ArrayList<Document> documentList = new ArrayList<>();
        Map<Integer, String> idToBody = new HashMap<>();

        for (int i = 0; i < nList.getLength(); i++) {

            Node n = nList.item(i);

            String body = n.getAttributes().getNamedItem("Body").toString();
            Integer id = i;

            Document document = new Document(body, id);
            documentList.add(document);

            idToBody.put(id, body);
        }

        InvertedIndex index = buildIndex(documentList);

        String searchTerm = "world";
        ArrayList<Set<ParsedDocument>> batch = index.search(searchTerm, 3);

        for (Set<ParsedDocument> set : batch){
            int count = 0;
            for (ParsedDocument pd : set) {
                Object id = pd.getUniqueId();
                System.out.println(id);
                System.out.println(idToBody.get(id));
                System.out.println("------------------------------------------------------");
                if (++count==4) break;
            }
        }        

        System.exit(0);

    }
    public static InvertedIndex buildIndex(ArrayList<Document> documents) {

        DocumentParser parser = new DocumentParser(true,true);

        ArrayList<ParsedDocument> corpus = new ArrayList<>();
        for (Document doc : documents)
            corpus.add(parser.parseDocument(doc.getRawText(),doc.getUniqueIdentifier()));
        
        InvertedIndex invertedIndex = new InvertedIndex(corpus);

        return invertedIndex;
    }
}
