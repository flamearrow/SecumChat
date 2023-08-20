package com.meichinijiuchiquba.secumchat.net;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.meichinijiuchiquba.secumchat.SecumApplication;
import com.meichinijiuchiquba.secumchat.log.EndMatchSentFactory;
import com.meichinijiuchiquba.secumchat.log.GetMatchSentFactory;
import com.meichinijiuchiquba.secumchat.log.GetMatchSuccessFactory;
import com.meichinijiuchiquba.secumchat.model.EndMatch;
import com.meichinijiuchiquba.secumchat.model.GetMatch;
import com.meichinijiuchiquba.secumchat.model.GetMatchRequest;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Requester for GetMatch and EndMatch.
 * When {@link #startMatch()} is called, a EndMatch is sent first, upon returning
 * if succeed, start looping for GetMatch:
 * call {@link #postMatchRequest()}, send GetMatch, upon returning
 * * if match success
 * ** if is caller, notify callbacks
 * ** if is callee, notify callbacks, continue posting GetMatch(looping)
 * * if match failed
 * ** continue posting GetMatch(looping)
 * * if request failed
 * ** continue posting GetMatch(looping)
 * * if failed, retry sending EndMatch
 */
public class SecumNetworkRequester {
    public interface SecumNetworkRequesterCallbacks {
        void onGetMatchSucceed(GetMatch getMatch, boolean isCaller);

        void onGetMatchFailed(@Nullable GetMatch getMatch);

        void onEndMatchSucceed();

        void onEndMatchFailed();
    }

    private static final int GET_MATCH_DELAY = 2000;

    private String myName;
    private Handler handler;
    private SecumNetworkRequesterCallbacks callbacks;
    @Inject
    SecumAPI secumAPI;
    @Inject
    Answers answers;
    @Inject
    GetMatchSentFactory getMatchSentFactory;
    @Inject
    EndMatchSentFactory endMatchSentFactory;
    @Inject
    GetMatchSuccessFactory getMatchSuccessFactory;

    private LocationManager locationManager;
    private Activity activity;


    public SecumNetworkRequester(Activity activity, String myName, SecumNetworkRequesterCallbacks
            callbacks) {
        ((SecumApplication) activity.getApplication()).getNetComponet().inject(this);
        this.myName = myName;
        this.callbacks = callbacks;
        handler = new Handler();
        this.activity = activity;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Start polling server for getMatch
     */
    public void startMatch() {
        handler.removeCallbacks(bestEffortEndMatchRunnable);
        handler.postDelayed(endMatchRunnable, GET_MATCH_DELAY);
    }

    public void stopMatch() {
        cancellAll();
        endMatch();
    }

    /**
     * Cancel all pending getMatch and endMatch
     */
    public void cancellAll() {
        handler.removeCallbacks(getMatchRunnable);
        handler.removeCallbacks(endMatchRunnable);
    }

    /**
     * Notify server to stop sending me getMatch
     */
    public void endMatch() {
        handler.post(bestEffortEndMatchRunnable);
    }

    private void postMatchRequest() {
        handler.postDelayed(getMatchRunnable, GET_MATCH_DELAY);
    }

    private Runnable bestEffortEndMatchRunnable = new Runnable() {
        @Override
        public void run() {
            secumAPI.endMatch().enqueue(
                    new Callback<EndMatch>() {
                        @Override
                        public void onResponse(Call<EndMatch> call, Response<EndMatch> response) {
                            EndMatch endMatch = response.body();
                            if (endMatch != null && endMatch.isSuccess()) {
                                Log.d(SecumAPI.TAG, "Single EndMatch(" + myName + ") Success.");
                            } else {
                                Log.d(SecumAPI.TAG, "Single EndMatch(" + myName + ") failed");
                            }
                        }

                        @Override
                        public void onFailure(Call<EndMatch> call, Throwable t) {
                            Log.d(SecumAPI.TAG, "Single EndMatch(" + myName + ") failed");
                        }
                    }
            );
        }
    };

    private Runnable getMatchRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(SecumAPI.TAG, "Posting GetMatch(" + myName + ")");
            answers.logCustom(getMatchSentFactory.create(myName));
            Location location;
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission
                    .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                    .checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                location = null;
            } else {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            }

            GetMatchRequest getMatchRequest;
            if (location != null) {
                getMatchRequest = new GetMatchRequest.Builder().setLat("" + location.getLatitude())
                        .setLng("" + location.getLongitude()).build();
            } else {
                getMatchRequest = new GetMatchRequest.Builder().build();
            }
            secumAPI.getMatch(getMatchRequest).
                    enqueue(new Callback<GetMatch>() {
                        @Override
                        public void onResponse(Call<GetMatch> call, Response<GetMatch> response) {
                            GetMatch getMatch = response.body();
                            if (getMatch != null && getMatch.isSuccess()) {
                                answers.logCustom(getMatchSuccessFactory.create(getMatch
                                                .getCaller(),
                                        getMatch.getCallee()));
                                Log.d(SecumAPI.TAG, "GetMatch(" + myName + ") Success, caller: "
                                        + getMatch
                                        .getCaller());
                                if (getMatch.isCaller()) {
                                    callbacks.onGetMatchSucceed(getMatch, true);
                                } else if (getMatch.isCallee()) {
                                    callbacks.onGetMatchSucceed(getMatch, false);
                                    Log.d(SecumAPI.TAG, "GetMatch(" + myName + ")  Success, " +
                                            "callee: " +
                                            getMatch.getCallee() + ", posting another get match");
                                    // keep posting
                                    postMatchRequest();
                                }
                            } else {
                                callbacks.onGetMatchFailed(getMatch);
                                Log.d(SecumAPI.TAG, "GetMatch(" + myName + ")  failed, posting " +
                                        "another " +
                                        "get match");
                                postMatchRequest();
                            }
                        }

                        @Override
                        public void onFailure(Call<GetMatch> call, Throwable t) {
                            callbacks.onGetMatchFailed(null);
                            postMatchRequest();
                        }
                    });
        }
    };

    private Runnable endMatchRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(SecumAPI.TAG, "Posting EndMatch(" + myName + ")");
            answers.logCustom(endMatchSentFactory.create(myName));
            secumAPI.endMatch().enqueue(
                    new Callback<EndMatch>() {
                        @Override
                        public void onResponse(Call<EndMatch> call, Response<EndMatch> response) {
                            EndMatch endMatch = response.body();
                            if (endMatch != null && endMatch.isSuccess()) {
                                callbacks.onEndMatchSucceed();
                                Log.d(SecumAPI.TAG, "EndMatch(" + myName + ") Success, posting " +
                                        "GetMatch");
                                postMatchRequest();
                            } else {
                                callbacks.onEndMatchFailed();
                                Log.d(SecumAPI.TAG, "EndMatch(" + myName + ") failed, reposting " +
                                        "EndMatch");
                                startMatch();
                            }
                        }

                        @Override
                        public void onFailure(Call<EndMatch> call, Throwable t) {
                            callbacks.onEndMatchFailed();
                            startMatch();
                        }
                    }
            );
        }
    };
}
