package com.example.sale1996.mvvc_pattern_app_java;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//ovako se u androidu pisu nazivi tabele za sql-lite bazu
@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String description;

    private int priority;

    /* *
    ukoliko zelimo neko polje da ignorisemo (da ne stavljamo u bazu) jednostavno stavimo @Ignore iznad anotaciju..
     */

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
