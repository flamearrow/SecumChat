package com.shanjingtech.secumchat.net;

import android.os.AsyncTask;
import android.util.Log;

import com.shanjingtech.secumchat.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.webrtc.PeerConnection;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Request XirSys for STUN and TURN servers.
 * TODO: use OkHttp
 */
public class XirSysRequest extends AsyncTask<Void, Void, List<PeerConnection.IceServer>> {
    public static List<PeerConnection.IceServer> getIceServers() {
        List<PeerConnection.IceServer> servers = new ArrayList<>();
        try {
            servers = new XirSysRequest().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return servers;
    }

    public List<PeerConnection.IceServer> doInBackground(Void... params) {
        List<PeerConnection.IceServer> servers = new ArrayList<PeerConnection.IceServer>();
        OkHttpClient client = new OkHttpClient.Builder().build();

        // Create a form body builder
        FormBody.Builder formBuilder = new FormBody.Builder();

        // Add name-value pairs to the form body
        formBuilder.add("room", Constants.XIR_ROOM);
        formBuilder.add("application", Constants.XIR_APPLICATION);
        formBuilder.add("domain", Constants.XIR_DOMAIN);
        formBuilder.add("ident", Constants.XIR_USER);
        formBuilder.add("secret", Constants.XIR_SECRET);
        formBuilder.add("secure", "1");

        // Build the form body
        RequestBody requestBody = formBuilder.build();


        // Create a request object with the desired URL
        Request request = new Request.Builder()
                .url("https://service.xirsys.com/ice")
                .post(requestBody)
                .build();

        try {
            // Send the request and retrieve the response
            Response response = client.newCall(request).execute();

            // Get the response body as a string
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                Log.d("BGLM", "got response: " + responseBody.string());
                BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody.byteStream(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null; ) {
                    builder.append(line).append("\n");
                }
                JSONTokener tokener = new JSONTokener(builder.toString());
                JSONObject json = new JSONObject(tokener);
                if (json.isNull("e")) {
                    JSONArray iceServers = json.getJSONObject("d").getJSONArray("iceServers");
                    for (int i = 0; i < iceServers.length(); i++) {
                        JSONObject srv = iceServers.getJSONObject(i);
                        PeerConnection.IceServer is;
                        if (srv.has("username"))
                            is = new PeerConnection.IceServer(srv.getString("url"),
                                    srv.getString("username"), srv.getString("credential"));
                        else
                            is = new PeerConnection.IceServer(srv.getString("url"));
                        servers.add(is);
                    }
                } else {
                    Log.d("BGLM", "XirSys error" + json);
                }
                // Make sure to close the response body
                responseBody.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Log.i("XIRSYS", "Servers: " + servers.toString());
        return servers;
    }


}

/*
function get_xirsys_servers() {
    var servers;
    $.ajax({
        type: 'POST',
        url: 'https://service.xirsys.com/ice',
        data: {
            room: 'default',
            application: 'default',
            domain: 'kevingleason.me',
            ident: 'gleasonk',
            secret: 'b9066b5e-1f75-11e5-866a-c400956a1e19',
            secure: 1,
        },
        success: function(res) {
	        console.log(res);
            res = JSON.parse(res);
            if (!res.e) servers = res.d.iceServers;
        },
        async: false
    });
    return servers;
}
 */

