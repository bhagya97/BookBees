package com.example.finalsample1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.io.SerializablePermission;
import java.util.ArrayList;
import java.util.List;

public class CustomGridViewAdapter extends BaseAdapter implements Serializable {


    List<UserDetails> userDetailsListAdapter =new ArrayList<>();
    private Context mContext;

    // Constructor

    public CustomGridViewAdapter(List<UserDetails> userDetailsListAdapter, Context mContext) {
        this.userDetailsListAdapter = userDetailsListAdapter;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return userDetailsListAdapter.size();
    }

    @Override
    public Object getItem(int position) {
        return userDetailsListAdapter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ViewHolderItem viewHolder;


        if (convertView == null) {

            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_grid, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.profileName = (TextView) convertView.findViewById(R.id.profileName);
            viewHolder.location = (TextView) convertView.findViewById(R.id.location);
            viewHolder.imageViewItem = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.messageBtn = (Button) convertView.findViewById(R.id.messageBtn);
            convertView.setTag(viewHolder);


        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        // object item based on the position
        final UserDetails userDetails=(UserDetails) this.getItem(position
        );
        // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
        viewHolder.profileName.setText(userDetails.getProfileName());
        viewHolder.location.setText(userDetails.getProfileLocation());
        viewHolder.profileName.setTag(position);
        viewHolder.imageViewItem.setImageBitmap(userDetails.getDecodedByte());
        viewHolder.messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String profileName = userDetails.getProfileName();
                System.out.println("check pro:"+profileName);
                Intent i = new Intent(mContext,ChatActivity.class);
                i.putExtra("user_name",profileName);
                i.putExtra("user_id",userDetails.getUserUid());
                mContext.startActivity(i);
            }
        });

        return convertView;

    }



    // our ViewHolder.
// caches our TextView
    static class ViewHolderItem {
        TextView profileName;
        TextView location;
        ImageView imageViewItem;
        Button messageBtn;
    }


}
