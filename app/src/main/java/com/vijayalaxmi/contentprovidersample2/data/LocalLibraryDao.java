package com.vijayalaxmi.contentprovidersample2.data;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LocalLibraryDao {

    @Query("SELECT COUNT(*) FROM " + Book.TABLE_NAME)
    int count();

    @Insert
    long insert(Book book);

    @Insert
    long[] insertAll(Book[] books);


    @Query("SELECT * FROM " + Book.TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT * FROM " + Book.TABLE_NAME + " WHERE " + Book.COLUMN_ID + " = :id")
    Cursor selectById(long id);


    @Query("DELETE FROM " + Book.TABLE_NAME + " WHERE " + Book.COLUMN_ID + " = :id")
    int deleteById(long id);


    @Update
    int update(Book book);

}
