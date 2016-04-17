package com.rahulsah.diary;

import com.orm.SugarRecord;

/**
 * Created by ShivamLekhi on 3/30/16.
 */
public class Event extends SugarRecord {
    Long id;
    String dte;
    String Title;

    public Event() {}

    public Event(String dte, String Title) {
        this.dte = dte;
        this.Title = Title;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return Title;
    }

    public String getDate() {
        return dte;
    }
}
