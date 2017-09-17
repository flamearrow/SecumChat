package com.shanjingtech.secumchat.message;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.ui.CircleImageView;

public class TextMessageViewHolder extends RecyclerView.ViewHolder {

    TextView txtTime;
    TextView txtContent;
    CircleImageView profilePicImage;

    public TextMessageViewHolder(View itemView) {
        super(itemView);
        txtTime = (TextView) itemView.findViewById(R.id.txt_time);
        txtContent = (TextView) itemView.findViewById(R.id.txt_content);
        profilePicImage = (CircleImageView) itemView.findViewById(R.id.img_user_image);
    }

}
