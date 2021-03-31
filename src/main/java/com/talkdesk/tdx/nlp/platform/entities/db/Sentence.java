package com.talkdesk.tdx.nlp.platform.entities.db;

public class Sentence {

    private long id;
    private String client;
    private String text;

    public Sentence(String client, String text) {
        this.client = client;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
