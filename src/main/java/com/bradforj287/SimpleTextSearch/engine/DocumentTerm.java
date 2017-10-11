package com.bradforj287.SimpleTextSearch.engine;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by brad on 6/6/15.
 */
public class DocumentTerm {

    private String word;
    private int positionInDoc;

    public DocumentTerm(String word, int positionInDoc) {
        Preconditions.checkArgument(!StringUtils.isEmpty(word));
        this.word = word;
        this.positionInDoc = positionInDoc;
    }

    public String getWord() {
        return word;
    }


}
