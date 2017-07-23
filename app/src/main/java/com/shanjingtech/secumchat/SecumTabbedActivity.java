package com.shanjingtech.secumchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;

import com.shanjingtech.secumchat.contacts.ContactsActivity;

/**
 * Created by flamearrow on 7/23/17.
 */

public abstract class SecumTabbedActivity extends SecumBaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            item.setChecked(item.getItemId() == getNavigationMenuItemId());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // remove flicker
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(final @NonNull MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId != getNavigationMenuItemId()) {
            bottomNavigationView.post(new Runnable() {
                @Override
                public void run() {
                    if (itemId == R.id.menu_conversation) {
//                    startActivity(new Intent(SecumTabbedActivity.this, SecumChatActivity.class));
                    } else if (itemId == R.id.menu_discover) {
                        startActivity(new Intent(SecumTabbedActivity.this, SecumChatActivity
                                .class));

                    } else if (itemId == R.id.menu_contacts) {
                        startActivity(new Intent(SecumTabbedActivity.this, ContactsActivity.class));
                    }
//                    finish();
                }
            });
        }
        return true;
    }

    @LayoutRes
    protected abstract int getLayoutID();

    @IdRes
    protected abstract int getNavigationMenuItemId();
}
