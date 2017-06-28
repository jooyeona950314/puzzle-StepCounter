package com.localhost.jooyeon.final_project;

/**
 * Created by asmwj on 2017-06-16.
 */

public class CurrentUser {
    String current_kg;
    String date;
    String walk_cnt;

    public CurrentUser(String date, String kg, String walk_cnt){
        this.current_kg = kg;
        this.date = date;
        this.walk_cnt = walk_cnt;
    }

    public String getCurrent_kg() {
        return current_kg;
    }

    public void setCurrent_kg(String current_kg) {
        this.current_kg = current_kg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWalk_cnt() {
        return walk_cnt;
    }

    public void setWalk_cnt(String walk_cnt) {
        this.walk_cnt = walk_cnt;
    }


}
