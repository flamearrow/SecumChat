package com.shanjingtech.secumchat.injection;

import com.shanjingtech.secumchat.SecumBaseActivity;
import com.shanjingtech.secumchat.net.SecumNetworkRequester;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class, FabricModule.class})
public interface NetComponent {
    void inject(SecumBaseActivity activity);
    void inject(SecumNetworkRequester networkRequester);
}
