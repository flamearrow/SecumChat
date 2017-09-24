package com.shanjingtech.secumchat.injection;

import android.app.Application;
import android.content.Context;

import com.shanjingtech.secumchat.pushy.PushyInitializer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provide Application
 */

@Module
public class AppModule {
    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context providesApplicationContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    PushyInitializer providesPushyInitializer(Application application) {
        return new PushyInitializer(application);
    }
}