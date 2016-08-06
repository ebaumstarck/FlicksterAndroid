package com.codepath.flickster;

import com.google.android.youtube.player.YouTubePlayer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by emma_baumstarck on 8/5/16.
 */
public class TrailerPlayer {

    static final String TRAILER_URL = "https://api.themoviedb.org/3/movie/%d/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    public static void playTrailer(final YouTubePlayer youTubePlayer, final int movieId) {
        AsyncHttpClient trailerClient = new AsyncHttpClient();
        trailerClient.get(String.format(TRAILER_URL, movieId), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // find the first YouTube trailer
                try {
                    JSONArray youtubeVideos = response.getJSONArray("youtube");
                    for (int i = 0; i < youtubeVideos.length(); i++) {
                        JSONObject video = youtubeVideos.getJSONObject(i);
                        if (video.getString("type").equals("Trailer")) {
                            youTubePlayer.cueVideo(video.getString("source"));
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
