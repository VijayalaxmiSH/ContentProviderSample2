package com.vijayalaxmi.contentprovidersample2.data;

import android.content.ContentValues;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = Book.TABLE_NAME, indices = {@Index(name = "book_id", value = "book_id", unique = true)})
public class Book {

    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = BaseColumns._ID;

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_BOOK_ID = "book_id";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;

    @ColumnInfo(name = COLUMN_NAME)
    public String bookName;

    @ColumnInfo(index =  true,  name = COLUMN_BOOK_ID)
    public String bookId;

    @NonNull
    public static Book fromContentValues(@Nullable ContentValues values) {
        final Book book = new Book();
        if (values != null && values.containsKey(COLUMN_ID)) {
            book.id = values.getAsLong(COLUMN_ID);
        }
        if (values != null && values.containsKey(COLUMN_NAME)) {
            book.bookName = values.getAsString(COLUMN_NAME);
        }
        if (values != null && values.containsKey(COLUMN_BOOK_ID)) {
            book.bookId = values.getAsString(COLUMN_BOOK_ID);
        }
        return book;
    }
}
