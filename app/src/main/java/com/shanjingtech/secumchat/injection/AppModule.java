package com.shanjingtech.secumchat.injection;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.shanjingtech.secumchat.db.MessageDAO;
import com.shanjingtech.secumchat.db.SecumDBConstants;
import com.shanjingtech.secumchat.db.SecumDatabase;
import com.shanjingtech.secumchat.net.SecumAPI;
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
    PushyInitializer providesPushyInitializer(Application application, SecumAPI secumAPI) {
        return new PushyInitializer(application, secumAPI);
    }

    @Provides
    @Singleton
    SecumDatabase providesSecumDatabase(Application application) {
        return Room.databaseBuilder(application.getApplicationContext(), SecumDatabase.class,
                SecumDBConstants.DB_NAME).build();
    }

    @Provides
    @Singleton
    MessageDAO providesMessageDAO(SecumDatabase secumDatabase) {
        return secumDatabase.messageDAO();
    }

}