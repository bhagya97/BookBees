package com.example.finalsample1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGridViewAdapter extends BaseAdapter {


    public Integer[] mThumbIds = {
            R.drawable.common_google_signin_btn_icon_disabled,
            R.drawable.common_google_signin_btn_icon_disabled, R.drawable.common_google_signin_btn_icon_disabled,
            R.drawable.common_google_signin_btn_icon_disabled, R.drawable.common_google_signin_btn_icon_disabled,
            R.drawable.common_google_signin_btn_icon_disabled,
            R.drawable.common_google_signin_btn_icon_disabled,
            R.drawable.common_google_signin_btn_icon_disabled, R.drawable.common_google_signin_btn_icon_disabled,
            R.drawable.common_google_signin_btn_icon_disabled, R.drawable.common_google_signin_btn_icon_disabled,
            R.drawable.common_google_signin_btn_icon_disabled,
            R.drawable.common_google_signin_btn_icon_disabled,
            R.drawable.common_google_signin_btn_icon_disabled, R.drawable.common_google_signin_btn_icon_disabled,
            R.drawable.common_google_signin_btn_icon_disabled, R.drawable.common_google_signin_btn_icon_disabled,
            R.drawable.common_google_signin_btn_icon_disabled,
    };
    public String[] mThumbNames = {
            "Bank", "Facility", "Gym", "Facility", "Facility", "Construct",
            "Bank", "Facility", "Gym", "Facility", "Facility", "Construct",
            "Bank", "Facility", "Gym", "Facility", "Facility", "Construct"

    };
    private Context mContext;

    // Constructor
    public CustomGridViewAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolderItem viewHolder;


        if (convertView == null) {

            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_grid, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.textViewItem = (TextView) convertView.findViewById(R.id.textView);

            viewHolder.imageViewItem = (ImageView) convertView.findViewById(R.id.imageView);
            // store the holder with the view.
            convertView.setTag(viewHolder);


        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        // object item based on the position

        // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
        viewHolder.textViewItem.setText(mThumbNames[position]);
        viewHolder.textViewItem.setTag(position);

        viewHolder.imageViewItem.setImageResource(mThumbIds[position]);


        return convertView;

    }

    // our ViewHolder.
// caches our TextView
    static class ViewHolderItem {
        TextView textViewItem;
        ImageView imageViewItem;
    }


}
