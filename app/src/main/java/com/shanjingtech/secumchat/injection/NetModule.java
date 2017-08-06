package com.shanjingtech.secumchat.injection;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shanjingtech.secumchat.net.SecumAPI;
import com.shanjingtech.secumchat.util.Constants;
import com.shanjingtech.secumchat.util.SecumDebug;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provide Net related injections
 */

@Module
public class NetModule {
    private static final String TAG = "NetModule";
    private final String apiBaseUrl;

    public NetModule(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

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
    Authenticator provideAuthenticator(final SharedPreferences sharedPreferences) {
        return new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credential;
                if (shouldUseBasicCredential(response)) {
                    credential = Credentials.basic(SecumAPI.USER_NAME, SecumAPI.PASSWORD);
                } else {
                    if (SecumDebug.isDebugMode(sharedPreferences)) {
                        // if it's debug mode, hardcode 11 or 22's credential
                        switch (SecumDebug.getCurrentDebugUser(sharedPreferences)) {
                            case SecumDebug.USER_11:
                                credential = "Bearer " + SecumDebug.TOKEN_11;
                                break;
                            case SecumDebug.USER_22:
                                credential = "Bearer " + SecumDebug.TOKEN_22;
                                break;
                            default:
                                credential = "mlgb";
                                Log.d(TAG, "No debug user credential found");
                        }
                    } else {
                        String bearer = sharedPreferences.getString(Constants
                                .SHARED_PREF_ACCESS_TOKEN, "mlgb");
                        credential = "Bearer " + bearer;
                        Log.d(TAG, "Bearer: " + bearer);
                    }
                }
                // TODO: when token expires, fail fast
                return response.request().newBuilder()
                        .header("Authorization", credential)
                        .build();
            }
        };
    }

    /**
     * Check if we should use basic credential(-u"user:password") or oauth token
     * For register/getAccessCode/getAccessToken, use basic
     * For others(ping/getMatch/endMatch/updateUser), use oauth
     *
     * @param response
     * @return
     */
    private boolean shouldUseBasicCredential(Response response) {
        String url = response.request().url().toString();
        // basic register/getAccessCode/getAccessToken
        return url.endsWith(Constants.PATH_REGISTER_USER) || url.endsWith(Constants
                .PATH_GET_ACCESS_CODE) || url.endsWith(Constants.PATH_GET_ACCESS_TOKEN);
        // (otherwise) oauth ping/getMatch/endMatch/updateUser
    }

    /**
     * handle the case when server response is empty
     */
    class NullOnEmptyConverterFactory extends Converter.Factory {
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation
                [] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit
                    .nextResponseBodyConverter(this, type, annotations);
            return new Converter<ResponseBody, Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException {
                    if (body.contentLength() == 0) return null;
                    return delegate.convert(body);
                }
            };
        }
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        // might not need client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    public SecumAPI provideSecumAPI(Retrofit retrofit) {
        return retrofit.create(SecumAPI.class);
    }

    @Provides
    @Singleton
    public CurrentUserProvider provideCurrentUserProvider() {
        return new CurrentUserProvider();
    }

}
