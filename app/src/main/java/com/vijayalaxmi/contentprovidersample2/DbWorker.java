package com.vijayalaxmi.contentprovidersample2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.vijayalaxmi.contentprovidersample2.data.Book;
import com.vijayalaxmi.contentprovidersample2.provider.LocalLibraryContentProvider;

import java.util.Objects;

public class DbWorker extends Worker {

    private Context context;

    public DbWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        String bookName =
                getInputData().getString(Constants.KEY_BOOK_NAME);
        String bookId =
                getInputData().getString(Constants.KEY_BOOK_ID);
        try {
            context.getContentResolver().insert(LocalLibraryContentProvider.URI_BOOK, getContentValues(bookName, bookId));
        } catch (SQLiteConstraintException e) {
            return Result.failure();
        }
        return Result.success();
    }

    private ContentValues getContentValues(String bookName, String bookId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Book.COLUMN_NAME, Objects.requireNonNull(bookName));
        contentValues.put(Book.COLUMN_BOOK_ID, Objects.requireNonNull(bookId));
        return contentValues;
    }
}
