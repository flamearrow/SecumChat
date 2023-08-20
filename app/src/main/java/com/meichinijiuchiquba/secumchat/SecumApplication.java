package com.meichinijiuchiquba.secumchat;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.stetho.Stetho;
import com.meichinijiuchiquba.secumchat.injection.AppModule;
import com.meichinijiuchiquba.secumchat.injection.DaggerNetComponent;
import com.meichinijiuchiquba.secumchat.injection.FabricModule;
import com.meichinijiuchiquba.secumchat.injection.NetComponent;
import com.meichinijiuchiquba.secumchat.injection.NetModule;
import com.meichinijiuchiquba.secumchat.net.SecumAPI;

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
