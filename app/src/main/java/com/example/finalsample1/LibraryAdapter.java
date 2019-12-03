package com.example.finalsample1;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;


public class LibraryAdapter extends BaseAdapter {

    private Context mContext;

    ArrayList<Bitmap> retrievedImage = Profile.getBookBitmap();

    public ArrayList<Bitmap> bookArray = retrievedImage;

    public LibraryAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public int getCount() {
        return bookArray.size();
    }

    @Override
    public Object getItem(int position) {
        return bookArray.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView Book = new ImageButton(mContext);
        Book.setImageBitmap(bookArray.get(position));
        Book.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Book.setLayoutParams(new GridView.LayoutParams(300, 300));
        return Book;
    }
}
