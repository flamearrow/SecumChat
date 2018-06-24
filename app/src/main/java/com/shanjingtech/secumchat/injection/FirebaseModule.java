package com.shanjingtech.secumchat.injection;

import com.google.firebase.storage.FirebaseStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {
    private static final String FIREBASE_PROFILE_IMAGE_STOREAGE = "gs://miao-android.appspot.com";
    public static final String FIREBASE_PROFILE_IMAGE_STOREAGE_BASE_PATH = "profile_images/";

    @Provides
    @Singleton
    FirebaseStorage provideFirebaseStorage() {
        return FirebaseStorage.getInstance(FIREBASE_PROFILE_IMAGE_STOREAGE);
    }
}
