package com.talkdesk.tdx.nlp.platform.entities.rest;

import java.util.HashSet;
import java.util.Set;

public enum POS {
    ADJ("AFX", "JJ", "JJR", "JJS"),
    ADP("IN", "RP"),
    ADV("RBR", "RBS", "WRB"),
    CONJ("CC"),
    DET("DT","PDT", "PRP$", "WDT", "WP$"),
    INTJ("UH"),
    NOUN("NN", "NNS"),
    NUM("CD"),
    PART("POS", "TO"),
    PRON("EX", "PRP", "WP"),
    PROPN("NNP", "NNPS"),
    PUNCT("``", "''", ",", "-LRB-", "-RRB-", ".", ":", "HYPH", "NFP"),
    SPACE("SP", "_SP"),
    SYM("$", "SYM"),
    VERB("MD", "VB", "VBG", "VBN", "VBP", "VBZ"),
    X("ADD", "FW", "GW", "LS", "NIL", "XX");

    private Set<String> tags = new HashSet<>();

    private POS(String... associatedTags) {
        for (String tag : associatedTags) {
            this.tags.add(tag);
        }
    }

    public boolean acceptsTag(String tag) {
        return tags.contains(tag);
    }

    public static POS fromTag(String tag) {
        for (POS pos : POS.values()) {
            if (pos.acceptsTag(tag)) {
                return pos;
            }
        }

        throw new IllegalArgumentException(String.format("Tag %s does not match any known POS", tag));
    }
}
