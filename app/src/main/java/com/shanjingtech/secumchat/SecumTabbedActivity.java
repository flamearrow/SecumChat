package com.shanjingtech.secumchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.ViewStub;

import com.shanjingtech.secumchat.contacts.ContactsActivity;

/**
 * Activities with a configurable tab at bottom of screen.
 */

public abstract class SecumTabbedActivity extends SecumBaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secum_tabbed_activity);
        ViewStub contentSub = (ViewStub) findViewById(R.id.content);
        contentSub.setLayoutResource(getContentResId());
        contentSub.inflate();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        setTitle(getMyName());
    }


    @LayoutRes
    abstract protected int getContentResId();

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(getNavigationMenuItemId());
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
            bottomNavigationView.post(() -> {
                if (itemId == R.id.menu_conversation) {
                    startActivity(new Intent(SecumTabbedActivity.this,
                            ConversationPreviewActivity.class));
                } else if (itemId == R.id.menu_discover) {
                    startActivity(new Intent(SecumTabbedActivity.this, SecumChatActivity
                            .class));

                } else if (itemId == R.id.menu_contacts) {
                    startActivity(new Intent(SecumTabbedActivity.this, ContactsActivity.class));
                }
            });
        }
        return true;
    }

    @IdRes
    protected abstract int getNavigationMenuItemId();
}
