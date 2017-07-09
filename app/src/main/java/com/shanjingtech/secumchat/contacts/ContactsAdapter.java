package com.shanjingtech.secumchat.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.model.Contact;

import java.util.List;

/**
 * Adapts contacts.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    private List<Contact> contacts;

    public void updateContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.contact_item, null));
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
