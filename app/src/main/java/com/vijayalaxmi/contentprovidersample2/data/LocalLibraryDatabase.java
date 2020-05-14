package com.vijayalaxmi.contentprovidersample2.data;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Book.class}, version = 1)
public abstract class LocalLibraryDatabase extends RoomDatabase {

    public abstract LocalLibraryDao book();

    private static LocalLibraryDatabase sInstance;

    public static synchronized LocalLibraryDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), LocalLibraryDatabase.class, "ex")
                    .build();
        }
        return sInstance;
    }


    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                LocalLibraryDatabase.class).build();
    }



}
