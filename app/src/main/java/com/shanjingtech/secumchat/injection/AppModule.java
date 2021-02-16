package com.shanjingtech.secumchat.injection;

import android.app.Application;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.room.Room;
import android.content.Context;

import com.shanjingtech.secumchat.db.MessageDAO;
import com.shanjingtech.secumchat.db.SecumDBConstants;
import com.shanjingtech.secumchat.db.SecumDatabase;
import com.shanjingtech.secumchat.db.UserDAO;
import com.shanjingtech.secumchat.lifecycle.SecumNetDBLifecycleObserver;
import com.shanjingtech.secumchat.net.SecumAPI;
import com.shanjingtech.secumchat.net.SecumNetDBSynchronizer;
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

    @Provides
    @Singleton
    UserDAO providesUserDAO(SecumDatabase secumDatabase) {
        return secumDatabase.userDAO();
    }

    @Provides
    @Singleton
    SecumNetDBSynchronizer providesSecumNetDBSynchronizer(SecumAPI secumAPI, MessageDAO
            messageDAO, UserDAO userDAO, CurrentUserProvider currentUserProvider) {
        SecumNetDBSynchronizer synchronizer = new SecumNetDBSynchronizer(secumAPI, userDAO,
                messageDAO, currentUserProvider);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new SecumNetDBLifecycleObserver
                (synchronizer, currentUserProvider));
        return synchronizer;
    }

}