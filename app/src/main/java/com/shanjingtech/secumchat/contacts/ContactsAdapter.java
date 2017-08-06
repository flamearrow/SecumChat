package com.shanjingtech.secumchat.contacts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanjingtech.secumchat.ProfileActivity;
import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.model.Contact;
import com.shanjingtech.secumchat.util.Constants;

import java.util.List;

/**
 * Adapts activeContacts.
 */

public class ContactsAdapter extends RecyclerView.Adapter {
    private static final int PENDING_POINTER = 0;
    private static final int CONTACTS_LABEL = 1;
    private static final int CONTACTS = 2;
    private static final int REQUESTED_POINTER = 3;
    private static final int BLOCKED_POINTER = 4;
    private List<Contact> activeContacts;

    private RecyclerView recyclerView;

    private Context context;

    public ContactsAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        context = recyclerView.getContext();
    }

    public void updateActiveContacts(List<Contact> activeContacts) {
        this.activeContacts = activeContacts;
        notifyDataSetChanged();
    }

    /**
     * 0: pending label
     * 1: contacts label
     * [2, 2+actibeSize-1]: contacts
     * 2+activeSize: requested label
     * 3+activeSize: blocked label
     */
    private int calculateViewTypeFromPosition(int position) {
        int contactsCount = activeContacts == null ? 0 : activeContacts.size();
        if (position == 0) {
            return PENDING_POINTER;
        } else if (position == 1) {
            return CONTACTS_LABEL;
        } else if (position <= contactsCount + 1) {
            return CONTACTS;
        } else if (position == 2 + contactsCount) {
            return REQUESTED_POINTER;
        } else if (position == 3 + contactsCount) {
            return BLOCKED_POINTER;
        } else return -1;
    }

    @Override
    public int getItemViewType(int position) {
        return calculateViewTypeFromPosition(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == CONTACTS_LABEL) {
            return new TextLabelViewHolder(
                    inflater.inflate(
                            R.layout.text_label_view_item, parent, false));
        } else if (viewType == CONTACTS) {
            final View view = inflater.inflate(
                    R.layout.contact_item, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // need to -2 to offset the two labels
                    int itemPosition = recyclerView.getChildLayoutPosition(view) - 2;
                    String currentUserName = activeContacts.get(itemPosition).getContact_username();
                    Context context = parent.getContext();
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra(Constants.CURRENT_USER_NAME, currentUserName);
                    context.startActivity(intent);
                }
            });
            return new ContactViewHolder(view);

        } else if (viewType == PENDING_POINTER || viewType == REQUESTED_POINTER || viewType ==
                BLOCKED_POINTER) {
            return new PointerLabelViewHolder(inflater.inflate(R.layout.pointer_label_view_item,
                    parent, false));
        } else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case CONTACTS: {
                Contact contact = activeContacts.get(position - 2);
                ((ContactViewHolder) holder).name.setText(contact.getContact_nickname());
                break;
            }
            case CONTACTS_LABEL:
                ((TextLabelViewHolder) holder).label.setText(context.getString(R.string.contacts));
                break;
            case REQUESTED_POINTER:
                ((PointerLabelViewHolder) holder).textLabel.setText(context.getString(R.string
                        .requested));
                ((PointerLabelViewHolder) holder).itemView.setOnClickListener(new View
                        .OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // pull requested
                        int i = 0;
                        i++;
                        int j = 0;
                        j++;
                    }
                });
                break;
            case BLOCKED_POINTER:
                ((PointerLabelViewHolder) holder).textLabel.setText(context.getString(R.string
                        .blocked));
                ((PointerLabelViewHolder) holder).itemView.setOnClickListener(new View
                        .OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // pull blocked
                    }
                });
                break;
            case PENDING_POINTER:
                ((PointerLabelViewHolder) holder).textLabel.setText(context.getString(R.string
                        .pending));
                ((PointerLabelViewHolder) holder).itemView.setOnClickListener(new View
                        .OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // pull pending
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 4 + (activeContacts == null ? 0 : activeContacts.size());
    }
}
