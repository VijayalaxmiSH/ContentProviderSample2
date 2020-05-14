package com.vijayalaxmi.contentprovidersample2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.Objects;


public class AddBookActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatEditText mBookNameEt;
    private AppCompatEditText mBookIdEt;

    public static Intent getIntent(Context context) {
        return new Intent(context, AddBookActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book_activity);
        mBookNameEt = findViewById(R.id.book_name_et);
        mBookIdEt = findViewById(R.id.book_id_et);
        findViewById(R.id.add_btn).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_btn) {
            saveBookToDataBase();
        }
    }

    private void saveBookToDataBase() {
        if (TextUtils.isEmpty(mBookNameEt.getText())) {

        } else if (TextUtils.isEmpty(mBookIdEt.getText())) {

        } else {
            Data contentData = new Data.Builder()
                    .putString(Constants.KEY_BOOK_NAME, Objects.requireNonNull(mBookNameEt.getText()).toString())
                    .putString(Constants.KEY_BOOK_ID, Objects.requireNonNull(mBookIdEt.getText()).toString())
                    .build();
            OneTimeWorkRequest addWork =
                    new OneTimeWorkRequest.Builder(DbWorker.class)
                            .setInputData(contentData)
                            .build();
            WorkManager.getInstance(AddBookActivity.this).beginWith(addWork).enqueue();
            onBackPressed();
        }
    }
}
