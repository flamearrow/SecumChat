package com.shanjingtech.secumchat.injection;

import com.shanjingtech.secumchat.SecumBaseActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(SecumBaseActivity activity);
}
