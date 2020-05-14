package com.vijayalaxmi.contentprovidersample2.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vijayalaxmi.contentprovidersample2.BuildConfig;
import com.vijayalaxmi.contentprovidersample2.data.Book;
import com.vijayalaxmi.contentprovidersample2.data.LocalLibraryDao;
import com.vijayalaxmi.contentprovidersample2.data.LocalLibraryDatabase;

import java.util.ArrayList;
import java.util.concurrent.Callable;


public class LocalLibraryContentProvider extends ContentProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";

    public static final Uri URI_BOOK = Uri.parse(
            "content://" + AUTHORITY + "/" + Book.TABLE_NAME);

    private static final int CODE_BOOK_DIR = 1;

    private static final int CODE_BOOK_ITEM = 2;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, Book.TABLE_NAME, CODE_BOOK_DIR);
        MATCHER.addURI(AUTHORITY, Book.TABLE_NAME + "/*", CODE_BOOK_ITEM);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
            @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_BOOK_DIR || code == CODE_BOOK_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            LocalLibraryDao book = LocalLibraryDatabase.getInstance(context).book();
            final Cursor cursor;
            if (code == CODE_BOOK_DIR) {
                cursor = book.selectAll();
            } else {
                cursor = book.selectById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_BOOK_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + Book.TABLE_NAME;
            case CODE_BOOK_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + Book.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case CODE_BOOK_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = LocalLibraryDatabase.getInstance(context).book()
                        .insert(Book.fromContentValues(values));
                Log.d("DB-ANAND", "Data inserted  : " + id);
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_BOOK_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_BOOK_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_BOOK_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = LocalLibraryDatabase.getInstance(context).book()
                        .deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_BOOK_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_BOOK_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final Book book = Book.fromContentValues(values);
                book.id = ContentUris.parseId(uri);
                final int count = LocalLibraryDatabase.getInstance(context).book()
                        .update(book);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @SuppressWarnings("RedundantThrows") /* This gets propagated up from the Callable */
    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(
            @NonNull final ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final Context context = getContext();
        if (context == null) {
            return new ContentProviderResult[0];
        }
        final LocalLibraryDatabase database = LocalLibraryDatabase.getInstance(context);
        return database.runInTransaction(new Callable<ContentProviderResult[]>() {
            @Override
            public ContentProviderResult[] call() throws OperationApplicationException {
                return LocalLibraryContentProvider.super.applyBatch(operations);
            }
        });
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] valuesArray) {
        switch (MATCHER.match(uri)) {
            case CODE_BOOK_DIR:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final LocalLibraryDatabase database = LocalLibraryDatabase.getInstance(context);
                final Book[] books = new Book[valuesArray.length];
                for (int i = 0; i < valuesArray.length; i++) {
                    books[i] = Book.fromContentValues(valuesArray[i]);
                }
                return database.book().insertAll(books).length;
            case CODE_BOOK_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

}
