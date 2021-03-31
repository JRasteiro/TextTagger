package com.talkdesk.tdx.nlp.platform.entities.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.talkdesk.tdx.nlp.platform.parsers.PosParser;

public class TaggedWord {

    @JsonProperty
    private String text;

    @JsonProperty
    @JsonDeserialize(using = PosParser.class)
    private POS tag;

    public TaggedWord() {
    }

    public TaggedWord(String text, POS tag) {
        this.text = text;
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public POS getTag() {
        return tag;
    }

    public void setTag(POS tag) {
        this.tag = tag;
    }
}
