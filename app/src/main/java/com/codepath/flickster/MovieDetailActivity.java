package com.codepath.flickster;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MovieDetailActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    public static final String MOVIE_TITLE_KEY = "title";
    public static final String MOVIE_RELEASE_DATE_KEY = "releaseDate";
    public static final String MOVIE_OVERVIEW_KEY = "overview";
    public static final String MOVIE_RATING_KEY = "rating";
    public static final String MOVIE_ID_KEY = "id";

    YouTubePlayerView youTubePlayerView;
    RatingBar ratingBar;
    TextView movieTitle;
    TextView movieReleaseDate;
    TextView movieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating((float) (5.0 / 6.0 * getIntent().getDoubleExtra(MOVIE_RATING_KEY, 0.0)));
        ratingBar.setClickable(false);

        movieTitle = (TextView) findViewById(R.id.movieTitle);
        movieReleaseDate = (TextView) findViewById(R.id.movieReleaseDate);
        movieOverview = (TextView) findViewById(R.id.movieOverview);

        movieTitle.setText(getIntent().getStringExtra(MOVIE_TITLE_KEY));
        movieReleaseDate.setText(
                "Release Date: " + getIntent().getStringExtra(MOVIE_RELEASE_DATE_KEY));
        movieOverview.setText(getIntent().getStringExtra(MOVIE_OVERVIEW_KEY));

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.player);
        youTubePlayerView.initialize(YoutubeApiCredentials.YOUTUBE_API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        int movieId = getIntent().getIntExtra(MOVIE_ID_KEY, -1);
        if (movieId != -1) {
            TrailerPlayer.playTrailer(youTubePlayer, movieId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.e("YOUTUBE", "Failed to initialize youtube");
    }
}
