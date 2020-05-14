package com.vijayalaxmi.contentprovidersample2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.vijayalaxmi.contentprovidersample2.data.Book;

public class BookListAdapter extends CursorRecyclerViewAdapter<BookListAdapter.ViewHolder> {


    public BookListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder((LayoutInflater.from(parent.getContext()).inflate(
                R.layout.book_list_item, parent, false)));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.bookName.setText(cursor.getString(
                cursor.getColumnIndexOrThrow(Book.COLUMN_NAME)));
        holder.bookId.setText(cursor.getString(
                cursor.getColumnIndexOrThrow(Book.COLUMN_BOOK_ID)));
    }

    public void add(Cursor data) {
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final AppCompatTextView bookName;
        final AppCompatTextView bookId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.book_name);
            bookId = itemView.findViewById(R.id.book_id);
        }
    }
}
