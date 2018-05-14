package com.shanjingtech.secumchat.contacts;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.shanjingtech.secumchat.ProfileActivity;
import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.SecumTabbedActivity;
import com.shanjingtech.secumchat.viewModels.ContactsViewModel;

/**
 * Show your contacts.
 */

public class ContactsActivity extends SecumTabbedActivity {
    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;
    private String contactsType;
    private ContactsViewModel contactsViewModel;

    public static final String CONTACTS_TYPE = "CONTACTS_TYPE";
    public static final String CONTACTS_TYPE_CONTACTS = "CONTACTS";
    public static final String CONTACTS_TYPE_REQUESTED = "REQUESTED";
    public static final String CONTACTS_TYPE_PENDING = "PENDING";
    public static final String CONTACTS_TYPE_BLOCKED = "BLOCKED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        secumNetDBSynchronizer.syncUserDBbyPreview(getMyName());
        initializeRecyclerView();
        observeContacts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        contactsAdapter.notifyDataSetChanged();
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
        return R.layout.fullscreen_recyclerview_container;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.menu_contacts;
    }

    private void observeContacts() {
        switch (contactsType) {
            case CONTACTS_TYPE_BLOCKED:
                contactsViewModel.getBlockedContactsOwnedBy(getMyName()).observe(this, contacts ->
                        contactsAdapter.updateContacts(contacts));
                break;
            case CONTACTS_TYPE_PENDING:
                contactsViewModel.getPendingContactsOwnedBy(getMyName()).observe(this, contacts ->
                        contactsAdapter.updateContacts(contacts));
                break;
            case CONTACTS_TYPE_REQUESTED:
                contactsViewModel.getRequestedContactsOwnedBy(getMyName()).observe(this, contacts ->
                        contactsAdapter.updateContacts(contacts));
                break;
            case CONTACTS_TYPE_CONTACTS:
            default:
                contactsViewModel.getActiveContactsOwnedBy(getMyName()).observe(this, contacts ->
                        contactsAdapter.updateContacts(contacts));
                break;
        }
    }

    private void initializeRecyclerView() {
        contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel.class);

        recyclerView = findViewById(R.id.recycler_view);
        contactsType = getIntent().getStringExtra(CONTACTS_TYPE) == null ?
                CONTACTS_TYPE_CONTACTS : getIntent().getStringExtra(CONTACTS_TYPE);
        contactsAdapter = new ContactsAdapter(recyclerView, contactsType, secumAPI);
        recyclerView.setAdapter(contactsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
