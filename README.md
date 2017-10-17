# Cruden Inverted Index

Cruden is a lightweight and easy to use in-memory inverted index for fulltext search for Java

### features:

* Inverted Index (in memory HashMap)
* tokenizer (lucene)
* optional word stemming (lucene KStemmer)
* optional stop words (english only)
* optional word occurrence based filtering
* BaseInverted is a bare-minimum implementation/API to allow prototyping
* InvertedIndex includes the optional features and statistics for tuning
* multiple search terms are treated as an intersection, ie they imply the AND operator
* documents are referred to by an integer key (auto increment if null)
* writing to the index is not thread safe, but multiple readers may be used
* it's fast
 
### Example

see Example.java for a real example ...
```
        InvertedIndex index = new InvertedIndex();
        index.add(0,"the quick brown fox");
        index.add(1,"in pursuit of love");
        index.add(2,"jumped over the lazy dog");
        index.add(3,"a battle of wits and possession");

        ArrayList<Integer> batch = index.search("jump");
        // --> [2]
```


### Sample Text Document

doc/Posts.txt

this is based on https://archive.org/download/stackexchange/beer.stackexchange.com.7z

this was generated via the demo project / subdir, in particular Convert
(first extract the Posts.xml via 7z from the archive)

licensed under CC by-sa/3.0
per https://archive.org/details/stackexchange

it's used here only as a medium sized sample to run the code against

### License
the license specified in LICENSE.txt (MIT) applies to all files in this repository

- this document is copyright 2015 bradforj287, 2017 nqzero
- stopwords/en.txt is copyright 2015 bradforj287
- other documents are as indicated, otherwise nqzero 2017


### Changes relative to the original

Cruden was inspired by and initially based upon https://github.com/bradforj287/SimpleTextSearch
many of the features of the original have been stripped out.
in particular, the inverted index is from scratch and based on the lucene tokenizer instead of stanford nlp



### About the name

Alexander Cruden singlehandedly created a concordance, ie an annotated inverted index, for the bible in the 1730s.
this seemed like an appropriate name for an in-memory inverted index, and at the time of creation
no obvious uses of the name in the java ecosystem were found, ie maven central or github

### Motivation

the goal is a bare-minimum API to allow prototyping a fiber-based system using [kilim](https://github.com/nqzero/kilim),
quasar and [db4j](https://github.com/nqzero/db4j). unfortunately, porting lucene to such a system seems difficult,
ie lucene doesn't provide an async adapter




