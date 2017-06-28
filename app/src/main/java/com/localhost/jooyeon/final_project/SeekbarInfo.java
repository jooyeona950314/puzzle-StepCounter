package com.localhost.jooyeon.final_project;

/**
 * Created by asmwj on 2017-06-17.
 */

public class SeekbarInfo {
    String date;
    String uri;
    String kg;

    SeekbarInfo(String date, String uri, String kg){
        this.date = date;
        this.uri = uri;
        this.kg = kg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getKg() {
        return kg;
    }

    public void setKg(String kg) {
        this.kg = kg;
    }
}