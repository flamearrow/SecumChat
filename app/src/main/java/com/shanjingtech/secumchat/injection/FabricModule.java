package com.shanjingtech.secumchat.injection;

import com.crashlytics.android.answers.Answers;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FabricModule {
    @Provides
    @Singleton
    public Answers provideAnswers() {
        return Answers.getInstance();
    }
}
