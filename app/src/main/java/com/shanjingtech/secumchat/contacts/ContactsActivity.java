package com.shanjingtech.secumchat.contacts;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.SecumTabbedActivity;
import com.shanjingtech.secumchat.model.Contact;
import com.shanjingtech.secumchat.model.ListContactsRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Show your contacts.
 */

public class ContactsActivity extends SecumTabbedActivity {
    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;

    private static final String TAG = "CONTACTSACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeRecyclerView();
        requestContacts();
    }

    @Override
    protected int getContentResId() {
        return R.layout.contacts_activity;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.menu_contacts;
    }

    private void requestContacts() {
        secumAPI.listContacts(new ListContactsRequest()).enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                contactsAdapter.updateActiveContacts(response.body());
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Log.d(TAG, "Contacts fetch failure");
            }
        });

    }

    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.contacts_recycler);
        contactsAdapter = new ContactsAdapter(recyclerView);
        recyclerView.setAdapter(contactsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
