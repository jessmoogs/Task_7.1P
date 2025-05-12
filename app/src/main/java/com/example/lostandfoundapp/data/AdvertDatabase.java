package com.example.lostandfoundapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Advert.class}, version = 1)
public abstract class AdvertDatabase extends RoomDatabase {

    private static AdvertDatabase instance;

    public abstract AdvertDao advertDao();

    public static synchronized AdvertDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AdvertDatabase.class, "advert_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()  // ✅ You can remove this in production
                    .build();
        }
        return instance;
    }
}
