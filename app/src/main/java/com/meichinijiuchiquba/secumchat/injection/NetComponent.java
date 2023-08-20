package com.meichinijiuchiquba.secumchat.injection;

import com.meichinijiuchiquba.secumchat.DebugActivity;
import com.meichinijiuchiquba.secumchat.LegacyLoginActivity;
import com.meichinijiuchiquba.secumchat.SecumBaseActivity;
import com.meichinijiuchiquba.secumchat.SecumChatActivity;
import com.meichinijiuchiquba.secumchat.message.SecumMessageActivity;
import com.meichinijiuchiquba.secumchat.net.SecumNetworkRequester;
import com.meichinijiuchiquba.secumchat.viewModels.SecumDBViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class, FabricModule.class, FirebaseModule.class})
public interface NetComponent {
    void inject(SecumBaseActivity activity);

    void inject(SecumChatActivity activity);

    void inject(SecumMessageActivity activity);

    void inject(SecumNetworkRequester networkRequester);

    void inject(LegacyLoginActivity legacyLoginActivity);

    void inject(DebugActivity debugActivity);

    void inject(SecumDBViewModel secumDBViewModel);
}
