package com.example.lostandfoundapp.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AdvertDao {

    // Insert a new advert
    @Insert
    void insert(Advert advert);

    // Get all adverts (for listing)
    @Query("SELECT * FROM adverts")
    List<Advert> getAllAdverts();

    // Delete a specific advert
    @Delete
    void delete(Advert advert);
}
