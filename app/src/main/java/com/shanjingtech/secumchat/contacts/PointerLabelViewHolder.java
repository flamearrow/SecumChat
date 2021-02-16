package com.shanjingtech.secumchat.contacts;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shanjingtech.secumchat.R;

/**
 * Holding a view for text and a pointer to another activity
 */

public class PointerLabelViewHolder extends RecyclerView.ViewHolder {
    TextView textLabel;
    View itemView;

    public PointerLabelViewHolder(View itemView) {
        super(itemView);
        textLabel = (TextView) itemView.findViewById(R.id.text_label);
        this.itemView = itemView;
    }
}
