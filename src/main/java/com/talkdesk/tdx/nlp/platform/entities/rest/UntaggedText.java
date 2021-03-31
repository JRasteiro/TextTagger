package com.talkdesk.tdx.nlp.platform.entities.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UntaggedText {

    private String text;

    private final String model = "en";

    @JsonProperty("collapse_punctuation")
    private final int collapsePunctuation = 0;

    @JsonProperty("collapse_phrases")
    private final int collapsePhrases = 1;

    public UntaggedText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
