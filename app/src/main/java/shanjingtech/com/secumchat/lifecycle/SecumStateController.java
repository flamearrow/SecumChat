package shanjingtech.com.secumchat.lifecycle;

import android.os.AsyncTask;
import android.os.Handler;

import shanjingtech.com.secumchat.model.GetMatchRequest;
import shanjingtech.com.secumchat.model.GetMatchResponse;

/**
 * Created by flamearrow on 2/26/17.
 */

public class SecumStateController {
    public enum State {
        WAITING, // wait for server to give me a match
        DIALING, // I dial the other, call dial(callee)
        RECEIVING, // I'm about to receive the dial
        CHATTING // chatting
    }

    private final static int POLLING_DELAY_IN_SEC = 5;
    private Handler handler;
    private GetMatchRequest getMatchRequest;
    private GetMatchResponse getMatchResponse;
    private StateListener listener;
    private Runnable servicePoller = new Runnable() {
        @Override
        public void run() {
            new GetMatchTask().execute();
        }
    };

    public SecumStateController(StateListener listener) {
        getMatchRequest = new GetMatchRequest();
        handler = new Handler();
        this.listener = listener;
    }

    public void switchState(State state) {
        listener.onEnteringState(state);
        switch (state) {
            case WAITING: {
                // start a GetMatchTask in 10 secs
//                handler.postDelayed(servicePoller, POLLING_DELAY_IN_SEC * 10000);
                return;
            }
            case DIALING: {

                return;
            }
            case RECEIVING: {
                return;
            }
            case CHATTING: {
                return;
            }
        }
    }

    class GetMatchTask extends AsyncTask<Void, String, GetMatchResponse> {
        @Override
        protected GetMatchResponse doInBackground(Void... params) {
            // TODO: call getMatch and return the match result
            GetMatchResponse response = new GetMatchResponse();
            response.success = true;
            return response;
        }

        @Override
        protected void onPostExecute(GetMatchResponse response) {
            super.onPostExecute(response);
            // if get match, stop polling,
            if (response.success) {
                if (response.shouldDial()) {
                    switchState(SecumStateController.State.DIALING);
                } else {
                    switchState(SecumStateController.State.RECEIVING);
                }
            }
            // if no match, keep polling
            handler.postDelayed(servicePoller, POLLING_DELAY_IN_SEC * 10000);
        }
    }

    public interface StateListener {
        void onEnteringState(State state);
    }
}