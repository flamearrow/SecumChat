package com.meichinijiuchiquba.secumchat;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import android.view.ViewStub;

import com.meichinijiuchiquba.secumchat.contacts.ContactsActivity;

/**
 * Activities with a configurable tab at bottom of screen.
 */

public abstract class SecumTabbedActivity extends SecumBaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.secum_tabbed_activity);
        setContentView(R.layout.secum_tabbed_activity_no_discover);
        ViewStub contentSub = (ViewStub) findViewById(R.id.content);
        contentSub.setLayoutResource(getContentResId());
        contentSub.inflate();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }


    @LayoutRes
    abstract protected int getContentResId();

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(getMyName());
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
                }
                else if (itemId == R.id.menu_discover) {
                    Intent i = new Intent(SecumTabbedActivity.this, SecumChatActivity
                            .class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);

                }
                else if (itemId == R.id.menu_contacts) {
                    startActivity(new Intent(SecumTabbedActivity.this, ContactsActivity.class));
                }
            });
        }
        return true;
    }

    @IdRes
    protected abstract int getNavigationMenuItemId();
}
