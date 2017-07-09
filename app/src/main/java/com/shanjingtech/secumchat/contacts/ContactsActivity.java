package com.shanjingtech.secumchat.contacts;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.SecumBaseActivity;
import com.shanjingtech.secumchat.model.Contact;
import com.shanjingtech.secumchat.model.ListContactsRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Show your contacts.
 */

public class ContactsActivity extends SecumBaseActivity {
    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;

    private static final String TAG = "CONTACTSACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity);
        initializeRecyclerView();
        requestContacts();
    }

    private void requestContacts() {
        secumAPI.listContacts(new ListContactsRequest()).enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                contactsAdapter.updateContacts(response.body());
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Log.d(TAG, "Contacts fetch failure");
            }
        });

    }

    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.contacts_recycler);
        contactsAdapter = new ContactsAdapter();
        recyclerView.setAdapter(contactsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
