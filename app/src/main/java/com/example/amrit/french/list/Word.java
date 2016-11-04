package com.example.amrit.french.list;

/**
 * Created by Amrit on 26-10-2016.
 */

public class Word {
    String id, french, meaning;

    public Word(String id, String french, String meaning) {
        this.id = id;
        this.french = french;
        this.meaning = meaning;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrench() {
        return french;
    }

    public void setFrench(String french) {
        this.french = french;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
