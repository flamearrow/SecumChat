package com.shanjingtech.secumchat.net;

import android.os.AsyncTask;
import android.util.Log;

import com.shanjingtech.secumchat.util.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.webrtc.PeerConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


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
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost("https://service.xirsys.com/ice");
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("room", Constants.XIR_ROOM));
        data.add(new BasicNameValuePair("application", Constants.XIR_APPLICATION));
        data.add(new BasicNameValuePair("domain", Constants.XIR_DOMAIN));
        data.add(new BasicNameValuePair("ident", Constants.XIR_USER));
        data.add(new BasicNameValuePair("secret", Constants.XIR_SECRET));
        data.add(new BasicNameValuePair("secure", "1"));
        //Encoding POST data
        try {
            request.setEntity(new UrlEncodedFormEntity(data));
            HttpResponse response = httpClient.execute(request);
            // write response to log
            Log.d("Http Post Response:", response.toString());

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent(), "UTF-8"));
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
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
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

