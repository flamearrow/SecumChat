package com.shanjingtech.secumchat;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.stetho.Stetho;
import com.shanjingtech.secumchat.injection.AppModule;
import com.shanjingtech.secumchat.injection.DaggerNetComponent;
import com.shanjingtech.secumchat.injection.FabricModule;
import com.shanjingtech.secumchat.injection.NetComponent;
import com.shanjingtech.secumchat.injection.NetModule;
import com.shanjingtech.secumchat.net.SecumAPI;

import io.fabric.sdk.android.Fabric;

/**
 * Provide app-wise controls
 */

public class SecumApplication extends Application {
    private NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new Answers());
        netComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(SecumAPI.BASE_URL))
                .fabricModule(new FabricModule())
                .build();
        Stetho.initializeWithDefaults(this);

    }

    public NetComponent getNetComponet() {
        return netComponent;
    }
}
