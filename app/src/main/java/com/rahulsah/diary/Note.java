package com.rahulsah.diary;

import com.orm.SugarRecord;

/**
 * Created by ShivamLekhi on 3/30/16.
 */
public class Note extends SugarRecord {
    Long id;
    String title;
    String entry;


    public Note() {}

    public Note(String title, String entry) {
        this.title = title;
        this.entry = entry;
    }

    public Long getid() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getEntry() {
        return entry;
    }
}
