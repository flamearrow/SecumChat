package com.shanjingtech.secumchat.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanjingtech.secumchat.R;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    public ImageView avatar;
    public TextView name;

    /**
     * inflated from {@link R.layout.contact_item}
     */
    public ContactViewHolder(View itemView) {
        super(itemView);
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        name = (TextView) itemView.findViewById(R.id.name);
    }


}
