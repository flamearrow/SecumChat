package com.shanjingtech.secumchat.injection;

import com.shanjingtech.secumchat.DebugActivity;
import com.shanjingtech.secumchat.LegacyLoginActivity;
import com.shanjingtech.secumchat.SecumBaseActivity;
import com.shanjingtech.secumchat.SecumChatActivity;
import com.shanjingtech.secumchat.net.SecumNetworkRequester;
import com.shanjingtech.secumchat.viewModels.SecumDBViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class, FabricModule.class, FirebaseModule.class})
public interface NetComponent {
    void inject(SecumBaseActivity activity);

    void inject(SecumChatActivity activity);

    void inject(SecumNetworkRequester networkRequester);

    void inject(LegacyLoginActivity legacyLoginActivity);

    void inject(DebugActivity debugActivity);

    void inject(SecumDBViewModel secumDBViewModel);
}
