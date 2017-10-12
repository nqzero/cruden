# SimpleTextSearch Overview
A lightweight and easy to use full text search implementation for Java. For data sets that can fit entirely in memory. Useful for situations where traditional search engines are overkill and overly complicated.

based on https://github.com/bradforj287/SimpleTextSearch

###Several assumptions are made in SimpleTextSearch:
* It is assumed your data can fit in memory. The Index is stored entirely in memory with nothing written to disk
* The Index itself is immutable. There is no support for automatic re-indexing of documents. Build a new index.
* Only the english language is supported (as of now) 
* This is only an Index and there is no sharding support. If you want sharding, you'd have to build it yourself. 
* Only freeform text searches are supported. No advanced search operators.

###Key Features:
* Inverted Index
* Word Stemming (snowball stemmer)
* Stop words
* String tokenizer (Stanford NLP)
 
### Example

see Demo.java for a real example ...
```
        ArrayList<String> documents = new ArrayList<>();
        documents.add("the quick brown fox");
        documents.add("in pursuit of love");
        documents.add("jumped over the lazy dog");
        documents.add("a battle of wits and possession");

        TextSearchIndex index = new InvertedIndex(documents);

        String searchTerm = "jump";

        ArrayList<Integer> batch = index.search(searchTerm);
```


### Sample Text Document

test/Posts.txt

this is based on https://archive.org/download/stackexchange/beer.stackexchange.com.7z

this was generated via the demo project / subdir, in particular Convert
(extract the Posts.xml via 7z from the archive)

licensed under CC by-sa/3.0
per https://archive.org/details/stackexchange

it's used here only as a medium sized sample to run the code against

# License
the license specified in LICENSE.txt (MIT) applies to all files in this repository.  


### Changes relative to the original
most of the features of the original have been stripped out.
all that's left are the preprocessing steps
