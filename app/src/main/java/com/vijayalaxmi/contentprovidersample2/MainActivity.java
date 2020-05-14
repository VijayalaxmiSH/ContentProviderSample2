package com.vijayalaxmi.contentprovidersample2;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vijayalaxmi.contentprovidersample2.data.Book;
import com.vijayalaxmi.contentprovidersample2.provider.LocalLibraryContentProvider;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int LOADER_BOOKS = 1;
    private BookListAdapter mBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RecyclerView list = findViewById(R.id.book_list_rv);
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));
        mBookAdapter = new BookListAdapter(this, null);
        list.setAdapter(mBookAdapter);
        findViewById(R.id.fab).setOnClickListener(this);
        LoaderManager.getInstance(this).initLoader(LOADER_BOOKS, null, mLoaderCallbacks);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                @NonNull
                public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                    return new CursorLoader(getApplicationContext(),
                            LocalLibraryContentProvider.URI_BOOK,
                            new String[]{Book.COLUMN_ID, Book.COLUMN_NAME, Book.COLUMN_BOOK_ID},
                            null, null, null);
                }

                @Override
                public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                    Log.d("VIJI", "Data fetched");
                    mBookAdapter.swapCursor(data);
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                }

            };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            navigateToAddBookScreen();
        }
    }

    private void navigateToAddBookScreen() {
        startActivity(AddBookActivity.getIntent(this));
    }
}
