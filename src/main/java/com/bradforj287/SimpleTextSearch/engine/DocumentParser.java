package com.bradforj287.SimpleTextSearch.engine;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by brad on 6/7/15.
 */
public class DocumentParser {

    private ConcurrentHashMap<String, String> stringPool;
    private boolean useStringPool;
    private boolean parseHtml;

    public DocumentParser(boolean useStringPool, boolean parseHtml) {
        this.useStringPool = useStringPool;
        this.parseHtml = parseHtml;
        if (useStringPool) {
            stringPool = new ConcurrentHashMap<>();
        }
    }

    public ParsedDocument parse(String txt,Integer id) {
        List<DocumentTerm> documentTerms = rawTextToTermList(txt);


        ParsedDocument document = new ParsedDocument(documentTerms,id);
        return document;
    }

    private List<DocumentTerm> rawTextToTermList(String rawText) {
        String text = rawText;

        if (StringUtils.isEmpty(text)) {
            return new ArrayList<DocumentTerm>();
        }

        if (parseHtml) {
            // strip HTML
            text = Jsoup.parse(text).text();
        }

        if (text == null) {
            text = "";
        }

        // to lower case
        text = text.toLowerCase();

        // iterate over parsed terms
        List<String> terms = TextParseUtils.tokenize(text);

        List<DocumentTerm> retVal = new ArrayList<>();
        int pos = 0;
        for (String str : terms) {
            String stemmedTerm = TextParseUtils.stemWord(str);

            // remove stop words
            if (StopWordHelper.isStopWord(stemmedTerm)) {
                continue;
            }

            String strToUse = stemmedTerm;

            if (useStringPool) {
                if (!stringPool.containsKey(stemmedTerm)) {
                    stringPool.put(stemmedTerm, stemmedTerm);
                }
                strToUse = stringPool.get(stemmedTerm);
            }

            retVal.add(new DocumentTerm(strToUse, pos));
            pos++;
        }

        return retVal;
    }
}
