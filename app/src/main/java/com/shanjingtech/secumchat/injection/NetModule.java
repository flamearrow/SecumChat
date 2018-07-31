package com.shanjingtech.secumchat.injection;

import android.app.Application;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shanjingtech.pnwebrtc.PnRTCClient;
import com.shanjingtech.pnwebrtc.PnRTCListener;
import com.shanjingtech.pnwebrtc.PnSignalingParams;
import com.shanjingtech.secumchat.db.Message;
import com.shanjingtech.secumchat.db.MessageDAO;
import com.shanjingtech.secumchat.lifecycle.PnRTCClientLifecycleObserver;
import com.shanjingtech.secumchat.message.CurrentPeerUserNameProvider;
import com.shanjingtech.secumchat.net.SecumAPI;
import com.shanjingtech.secumchat.net.XirSysRequest;
import com.shanjingtech.secumchat.util.Constants;
import com.shanjingtech.secumchat.util.SecumDebug;

import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
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

    // TODO: load the certificate file and add the trusted managers
    // https://developer.android.com/training/articles/security-ssl#SelfSigned
    // https://stackoverflow.com/questions/29273387/certpathvalidatorexception-trust-anchor-for
    // -certificate-path-not-found-retro
//    @Provides
//    @Singleton
//    SSLSocketFactory provideSSLSocketFactory() throws CertificateException, IOException,
//            NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
//        CertificateFactory cf = CertificateFactory.getInstance("X.509");
//        InputStream cert = context.getResources().openRawResource(R.raw.my_cert);
//        Certificate ca;
//        try {
//            ca = cf.generateCertificate(cert);
//        } finally {
//            cert.close();
//        }
//
//        // creating a KeyStore containing our trusted CAs
//        String keyStoreType = KeyStore.getDefaultType();
//        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//        keyStore.load(null, null);
//        keyStore.setCertificateEntry("ca", ca);
//
//        // creating a TrustManager that trusts the CAs in our KeyStore
//        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//        tmf.init(keyStore);
//
//        // creating an SSLSocketFactory that uses our TrustManager
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//        sslContext.init(null, tmf.getTrustManagers(), null);
//
//        return sslContext.getSocketFactory();
//    }

    // TODO: This returns a trustmanager that disabled certification, should NOT be used
    // https://stackoverflow.com/questions/2893819/accept-servers-self-signed-ssl-certificate-in
    // -java-client
    @Provides
    @Singleton
    SSLSocketFactory provideDisablingSSLSocketFactory() {
        TrustManager[] trustAllCertsTrustManagers = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertsTrustManagers, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        assert sslContext != null;
        return sslContext.getSocketFactory();

    }

//    @Provides
//    @Singleton
//    X509TrustManager provideX509TrustManager() {
//        // TODO: wtf should I return here
//        return null;
//    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache,
                                     Authenticator authenticator,
                                     SharedPreferences sharedPreferences
            , SSLSocketFactory unSafeSocketFactory
//                                     X509TrustManager x509TrustManager
    ) {
        // Interceptor modifies outgoing request and touches incoming response
        // chain.proceed() call is blocking and waits the response
        return new OkHttpClient.Builder().cache(cache).authenticator(authenticator)
                .addInterceptor(chain -> {
                    Response response = chain.proceed(addHeaderToRequest(chain.request(),
                            sharedPreferences));
                    return response;
                })
                .sslSocketFactory(unSafeSocketFactory)
                .build();
    }


    private Request addHeaderToRequest(Request request, SharedPreferences sharedPreferences) {
        String credential;
        if (shouldUseBasicCredential(request.url().toString())) {
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
        return request.newBuilder()
                .header("Authorization", credential)
                .build();
    }

    @Provides
    @Singleton
    Authenticator provideAuthenticator(final SharedPreferences sharedPreferences) {
        return (route, response) ->
                addHeaderToRequest(response.request(), sharedPreferences);
    }

    /**
     * Check if we should use basic credential(-u"user:password") or oauth token
     * For register/getAccessCode/getAccessToken, use basic
     * For others(ping/getMatch/endMatch/updateUser), use oauth
     *
     * @param url
     * @return
     */
    private boolean shouldUseBasicCredential(String url) {
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
            return (Converter<ResponseBody, Object>) body -> {
                if (body.contentLength() == 0) return null;
                return delegate.convert(body);
            };
        }
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        // might not need client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(new Converter.Factory() {
                    @Override
                    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                                            Annotation[]
                                                                                    annotations,
                                                                            Retrofit retrofit) {
                        final Converter<ResponseBody, ?> delegate = retrofit
                                .nextResponseBodyConverter(this, type, annotations);
                        return (Converter<ResponseBody, Object>) body -> {
                            if (body.contentLength() == 0) return null;
                            return delegate.convert(body);
                        };
                    }
                })
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
    public PnRTCClient providePnRTCClient(
            Application application,
            CurrentUserProvider currentUserProvider,
            MessageDAO messageDAO,
            CurrentPeerUserNameProvider currentPeerUserNameProvider) {
        // First, we initiate the PeerConnectionFactory with our application context and some
        // options.
        PeerConnectionFactory.initializeAndroidGlobals(
                application.getApplicationContext(),  // Context
                true,  // Audio Enabled
                true,  // Video Enabled
                true,  // Hardware Acceleration Enabled
                null); // Render EGL Context

        String currentUserName = currentUserProvider.getUser().getUsername();
        PnRTCClient pnRTCClient;

        List<PeerConnection.IceServer> servers = XirSysRequest.getIceServers();
        if (!servers.isEmpty()) {
            pnRTCClient = new PnRTCClient(Constants.PUB_KEY, Constants.SUB_KEY, Constants.SEC_KEY,
                    currentUserName, new PnSignalingParams(servers));
        } else {
            // TODO: revert to use default signal params
            pnRTCClient = new PnRTCClient(Constants.PUB_KEY, Constants.SUB_KEY, Constants.SEC_KEY,
                    currentUserName);
        }

        // register global message receiver
        pnRTCClient.attachRTCListener(new PnRTCListener() {
            @Override
            public void onMessage(String content, String from, String groupId, long time) {
                String ownerName = currentUserProvider.getUser().getUsername();
                Message message =
                        new Message.Builder()
                                .setFrom(from).setTo(ownerName).setOwnerName(ownerName)
                                .setTime(time).setGroupId(groupId).setContent(content)
                                .setRead(currentPeerUserNameProvider.isPeerUserNameEqualTo(from))
                                .build();
                messageDAO.insertMessage(message);
            }
        });

        //TODO: consider move this to a single provides method and inject it from SecumApplication
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new PnRTCClientLifecycleObserver
                (pnRTCClient, currentUserProvider));

        return pnRTCClient;
    }

    @Provides
    @Singleton
    public CurrentPeerUserNameProvider provideCurrentPeerNameProvider() {
        return new CurrentPeerUserNameProvider();
    }

}
