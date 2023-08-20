package com.meichinijiuchiquba.secumchat.contacts;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Holding a text view for label
 */
public class TextLabelViewHolder extends RecyclerView.ViewHolder {
    TextView label;

    public TextLabelViewHolder(View itemView) {
        super(itemView);
        label = (TextView) itemView;
    }
}
