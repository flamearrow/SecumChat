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
 * Adapts contacts.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    private List<Contact> contacts;

    private RecyclerView recyclerView;

    public ContactsAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void updateContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.contact_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                String currentUserName = contacts.get(itemPosition).getContact_username();
//                Toast.makeText(parent.getContext(), currentUserName, Toast.LENGTH_LONG).show();
                Context context = parent.getContext();
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(Constants.CURRENT_USER_NAME, currentUserName);
                context.startActivity(intent);
            }
        });
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.name.setText(contact.getContact_nickname());
    }

    @Override
    public int getItemCount() {
        return contacts == null ? 0 : contacts.size();
    }
}
