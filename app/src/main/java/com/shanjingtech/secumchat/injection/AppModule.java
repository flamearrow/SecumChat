package com.shanjingtech.secumchat.injection;

import android.app.Application;

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
}
