package com.meichinijiuchiquba.secumchat.injection;

import android.app.Application;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.room.Room;
import android.content.Context;

import com.meichinijiuchiquba.secumchat.db.MessageDAO;
import com.meichinijiuchiquba.secumchat.db.SecumDBConstants;
import com.meichinijiuchiquba.secumchat.db.SecumDatabase;
import com.meichinijiuchiquba.secumchat.db.UserDAO;
import com.meichinijiuchiquba.secumchat.lifecycle.SecumNetDBLifecycleObserver;
import com.meichinijiuchiquba.secumchat.net.SecumAPI;
import com.meichinijiuchiquba.secumchat.net.SecumNetDBSynchronizer;
import com.meichinijiuchiquba.secumchat.pushy.PushyInitializer;

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