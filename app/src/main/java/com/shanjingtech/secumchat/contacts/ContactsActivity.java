package com.shanjingtech.secumchat.contacts;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.shanjingtech.secumchat.ProfileActivity;
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
    private static final String CONTACTS_TYPE = "CONTACTS_TYPE";
    private static final String CONTACTS_TYPE_CONTACTS = "CONTACTS";
    private static final String CONTACTS_TYPE_REQUSTED = "REQUESTED";
    private static final String CONTACTS_TYPE_PENDING = "PENDING";
    private static final String CONTACTS_TYPE_BLOCKED = "BLOCKED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeRecyclerView();
        requestContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(this, ProfileActivity.class)));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchView.setIconified(true);
                MenuItemCompat.collapseActionView(searchItem);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
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
