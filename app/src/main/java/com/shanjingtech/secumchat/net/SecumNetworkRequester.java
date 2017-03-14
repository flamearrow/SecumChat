package com.shanjingtech.secumchat.net;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.shanjingtech.secumchat.SecumApplication;
import com.shanjingtech.secumchat.log.EndMatchSentFactory;
import com.shanjingtech.secumchat.log.GetMatchSentFactory;
import com.shanjingtech.secumchat.log.GetMatchSuccessFactory;
import com.shanjingtech.secumchat.model.EndMatch;
import com.shanjingtech.secumchat.model.EndMatchRequest;
import com.shanjingtech.secumchat.model.GetMatch;
import com.shanjingtech.secumchat.model.GetMatchRequest;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Requester for GetMatch and EndMatch.
 * When {@link #initializeMatch()} is called, a EndMatch is sent first, upon returning
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

    public SecumNetworkRequester(Activity activity, String myName, SecumNetworkRequesterCallbacks
            callbacks) {
        ((SecumApplication) activity.getApplication()).getNetComponet().inject(this);
        this.myName = myName;
        this.callbacks = callbacks;
        handler = new Handler();
    }

    public void initializeMatch() {
        handler.postDelayed(endMatchRunnable, GET_MATCH_DELAY);
    }

    public void cancellAll() {
        handler.removeCallbacks(getMatchRunnable);
        handler.removeCallbacks(endMatchRunnable);
    }

    private void postMatchRequest() {
        handler.postDelayed(getMatchRunnable, GET_MATCH_DELAY);
    }

    private Runnable getMatchRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(SecumAPI.TAG, "Posting GetMatch(" + myName + ")");
            answers.logCustom(getMatchSentFactory.create(myName));
            secumAPI.getMatch(new GetMatchRequest(myName)).enqueue(new Callback<GetMatch>() {
                @Override
                public void onResponse(Call<GetMatch> call, Response<GetMatch> response) {
                    GetMatch getMatch = response.body();
                    answers.logCustom(getMatchSuccessFactory.create(getMatch.getCaller(),
                            getMatch.getCallee()));
                    if (getMatch.isSuccess()) {
                        Log.d(SecumAPI.TAG, "GetMatch(" + myName + ") Success, caller: " + getMatch
                                .getCaller());
                        if (getMatch.isCaller()) {
                            callbacks.onGetMatchSucceed(getMatch, true);
                        } else if (getMatch.isCallee()) {
                            callbacks.onGetMatchSucceed(getMatch, false);
                            Log.d(SecumAPI.TAG, "GetMatch(" + myName + ")  Success, callee: " +
                                    getMatch.getCallee() + ", posting another get match");
                            // keep posting
                            postMatchRequest();
                        }
                    } else {
                        callbacks.onGetMatchFailed(getMatch);
                        Log.d(SecumAPI.TAG, "GetMatch(" + myName + ")  failed, posting another " +
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
            secumAPI.endMatch(new EndMatchRequest(myName)).enqueue(
                    new Callback<EndMatch>() {
                        @Override
                        public void onResponse(Call<EndMatch> call, Response<EndMatch> response) {
                            EndMatch endMatch = response.body();
                            if (endMatch.isSuccess()) {
                                callbacks.onEndMatchSucceed();
                                Log.d(SecumAPI.TAG, "EndMatch(" + myName + ") Success, posting " +
                                        "GetMatch");
                                postMatchRequest();
                            } else {
                                callbacks.onEndMatchFailed();
                                Log.d(SecumAPI.TAG, "EndMatch(" + myName + ") failed, reposting " +
                                        "EndMatch");
                                initializeMatch();
                            }
                        }

                        @Override
                        public void onFailure(Call<EndMatch> call, Throwable t) {
                            callbacks.onEndMatchFailed();
                            initializeMatch();
                        }
                    }
            );
        }
    };
}
