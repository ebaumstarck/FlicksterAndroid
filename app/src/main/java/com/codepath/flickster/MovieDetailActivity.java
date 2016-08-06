package com.codepath.flickster;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.codepath.flickster.databinding.ActivityMovieDetailBinding;
import com.codepath.flickster.models.Movie;
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

    ActivityMovieDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        binding.ratingBar.setClickable(false);

        binding.setMovie(new Movie(
                getIntent().getIntExtra(MOVIE_ID_KEY, -1),
                null,
                null,
                getIntent().getStringExtra(MOVIE_TITLE_KEY),
                getIntent().getStringExtra(MOVIE_OVERVIEW_KEY),
                getIntent().getStringExtra(MOVIE_RELEASE_DATE_KEY),
                (float) (5.0 / 6.0 * getIntent().getDoubleExtra(MOVIE_RATING_KEY, 0.0))));

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
