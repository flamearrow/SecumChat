package com.shanjingtech.secumchat.contacts;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shanjingtech.secumchat.ProfileActivity;
import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.SecumTabbedActivity;
import com.shanjingtech.secumchat.model.ContactInfos;
import com.shanjingtech.secumchat.model.ContactInfosValue;
import com.shanjingtech.secumchat.model.ContactRequest;
import com.shanjingtech.secumchat.model.PendingRequests;
import com.shanjingtech.secumchat.util.BotUtils;
import com.shanjingtech.secumchat.viewModels.ContactsViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Show your contacts.
 */

public class ContactsActivity extends SecumTabbedActivity {
    private RecyclerView recyclerView;
    private BotsAdapter contactsAdapter;
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
//                        contactsAdapter.updateContacts(contacts));

                break;
            case CONTACTS_TYPE_PENDING:
                secumAPI.listPendingRequests().enqueue(new Callback<PendingRequests>() {
                    @Override
                    public void onResponse(Call<PendingRequests> call, Response<PendingRequests> response) {
                        PendingRequests pendingRequests = response.body();
                        Log.d("BGLM", "pending requests: " + pendingRequests);
                        contactsAdapter.updateContacts(pendingRequests.incomingContactRequests);
                    }

                    @Override
                    public void onFailure(Call<PendingRequests> call, Throwable t) {
                        Log.d("BGLM", "getting requests error" + t);
                    }
                });

                break;
            case CONTACTS_TYPE_REQUESTED:
                secumAPI.listPendingRequests().enqueue(new Callback<PendingRequests>() {
                    @Override
                    public void onResponse(Call<PendingRequests> call, Response<PendingRequests> response) {
                        PendingRequests pendingRequests = response.body();
                        Log.d("BGLM", "pending requests: " + pendingRequests);
                        contactsAdapter.updateContacts(pendingRequests.outgoingContactRequests);
                    }

                    @Override
                    public void onFailure(Call<PendingRequests> call, Throwable t) {
                        Log.d("BGLM", "getting requests error" + t);
                    }
                });
                break;
            case CONTACTS_TYPE_CONTACTS:
            default:
                secumAPI.listContacts().enqueue(new Callback<ContactInfos>() {
                    @Override
                    public void onResponse(Call<ContactInfos> call, Response<ContactInfos> response) {
                        Log.d("BGLM", "getting contacts success" + response);
                        ContactInfos contactInfos = response.body();
                        List<ContactInfosValue> contactInfosValues = contactInfos.contactInfosValues;
                        if (contactInfosValues == null) {
                            Log.d("BGLM", "contact infos value is " + null);
                            contactInfosValues = new ArrayList<>();
                        }

                        // filter dupped Contacts
                        Set<String> idsDedupped = new HashSet<>();
                        List<ContactInfosValue> deduppedValues = new ArrayList<>();

                        for (ContactInfosValue contactInfo : contactInfosValues) {
                            if (idsDedupped.add(contactInfo.user.userId)) {
                                deduppedValues.add(contactInfo);
                            }
                        }


                        List<ContactRequest> adaptedContactRequests = new LinkedList<>();
                        for (ContactInfosValue value : deduppedValues) {
                            // only adding bot
                            if (BotUtils.BOT_IDS.contains(value.user.userId)) {
                                Log.d("BGLM", "adding bot with nickname" + value.user.getNickname());
                                adaptedContactRequests.add(new ContactRequest(value.user));
                            }
                        }
                        contactsAdapter.updateContacts(adaptedContactRequests);
                    }

                    @Override
                    public void onFailure(Call<ContactInfos> call, Throwable t) {

                    }
                });
//                        contactsViewModel.getActiveContactsOwnedBy(getMyName()).observe(this, contacts ->
//                                contactsAdapter.updateContacts(contacts));
                break;
        }

    }

    private void initializeRecyclerView() {
        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);

        recyclerView = findViewById(R.id.recycler_view);
        contactsType = getIntent().getStringExtra(CONTACTS_TYPE) == null ?
                CONTACTS_TYPE_CONTACTS : getIntent().getStringExtra(CONTACTS_TYPE);
        contactsAdapter = new BotsAdapter(this, recyclerView, secumAPI);
        recyclerView.setAdapter(contactsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
