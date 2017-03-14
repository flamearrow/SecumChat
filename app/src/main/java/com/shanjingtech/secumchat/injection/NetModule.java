package com.shanjingtech.secumchat.injection;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shanjingtech.secumchat.net.SecumAPI;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provide Net related injections
 */

@Module
public class NetModule {
    private final String apiBaseUrl;

    public NetModule(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    // Application reference must come from AppModule.class
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, Authenticator authenticator) {
        return new OkHttpClient.Builder().cache(cache).authenticator(authenticator).build();
    }

    @Provides
    @Singleton
    Authenticator provideAuthenticator() {
        return new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                System.out.println("Authenticating for response: " + response);
                System.out.println("Challenges: " + response.challenges());
                String credential = Credentials.basic(SecumAPI.USER_NAME, SecumAPI.PASSWORD);
                return response.request().newBuilder()
                        .header("Authorization", credential)
                        .build();
            }
        };
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        // might not need client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    public SecumAPI provideSecumAPI(Retrofit retrofit) {
        return retrofit.create(SecumAPI.class);
    }

}
