package com.shanjingtech.secumchat;

import android.app.Application;

import com.shanjingtech.secumchat.injection.AppModule;
import com.shanjingtech.secumchat.injection.DaggerNetComponent;
import com.shanjingtech.secumchat.injection.NetComponent;
import com.shanjingtech.secumchat.injection.NetModule;
import com.shanjingtech.secumchat.net.SecumAPI;

/**
 * Provide app-wise controls
 */

public class SecumApplication extends Application {
    private NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        netComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(SecumAPI.BASE_URL))
                .build();
    }

    public NetComponent getNetComponet() {
        return netComponent;
    }
}
