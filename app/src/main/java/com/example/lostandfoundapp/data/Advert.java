package com.example.lostandfoundapp.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "adverts")
public class Advert {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String type;         // "Lost" or "Found"
    public String name;
    public String phone;
    public String description;
    public String date;
    public String location;

    public Advert(String type, String name, String phone, String description, String date, String location) {
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
    }
}
