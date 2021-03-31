package com.talkdesk.tdx.nlp.platform.entities.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaggedText {

    @JsonProperty("words")
    public List<TaggedWord> taggedTexts;

    public List<TaggedWord> getTaggedWords() {
        return taggedTexts;
    }
}
