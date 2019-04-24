package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class LeaderboardManager {

    private static final String BASE_URL = "https://crleaderboard-9970.restdb.io/rest/leaderboard";

    private String key;
    private RequestQueue queue;

    private ArrayList<Pair<String, Integer>> recentScores;
    private boolean isBusy;

    Context context;

    LeaderboardManager(Context context) {
        this.context = context;

        try {
            InputStream inputStream = context.getAssets().open("leaderboard-key");
            byte[] buf = new byte[inputStream.available()];
            inputStream.read(buf);
            inputStream.close();
            key = new String(buf);
        } catch (IOException ignored) {
            Log.e("CR-ERRORS", "Unable to find leaderboard private key");
        }

        queue = Volley.newRequestQueue(context);

        recentScores = null;
        isBusy = false;
    }

    private void updateScores() {
        recentScores = null;
        isBusy = true;

        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray json = null;
                        try {
                            json = new JSONArray(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        recentScores = new ArrayList<>();
                        for (int i = 0; i < json.length(); i++) {
                            try {
                                JSONObject object = json.getJSONObject(i);
                                recentScores.add(new Pair<>(object.getString("name"), object.getInt("score")));
                            } catch (JSONException ignored) { }
                        }
                        isBusy = false;
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isBusy = false;
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("x-apikey", key);
                headers.put("cache-control", "no-cache");
                return headers;
            }
        };

        queue.add(request);
    }

    public void postScore(final int score) {
        StringRequest request = new StringRequest(
                Request.Method.POST, BASE_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
        {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return ("{\"name\":\"Anonymous\",\"score\":\"" + Integer.toString(score) + "\"}").getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("content-type", "application/json");
                headers.put("x-apikey", key);
                headers.put("cache-control", "no-cache");
                return headers;
            }
        };

        queue.add(request);
    }

    public ArrayList<Pair<String, Integer>> getScores() {
        if (isBusy) {
            return null;
        } else {
            if (recentScores != null) {
                return recentScores;
            } else {
                updateScores();
                return null;
            }
        }
    }

}
