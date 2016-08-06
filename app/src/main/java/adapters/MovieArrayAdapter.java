package adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by emma_baumstarck on 8/2/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {
    private static class UnpopularViewHolder {
        ImageView image;
        TextView title;
        TextView releaseDate;
        TextView overview;
    }

    private static class PopularViewHolder {
        ImageView image;
        ImageView overlay;
        TextView title;
        TextView releaseDate;
        TextView overview;
    }

    private final int POPULAR_VIEW = 0;
    private final int UNPOPULAR_VIEW = 1;

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isPopular() ? POPULAR_VIEW : UNPOPULAR_VIEW;
    }

    private View getInflatedLayoutForType(int type) {
        if (type == POPULAR_VIEW) {
            return LayoutInflater.from(getContext()).inflate(R.layout.popular_movie, null);
        } else if (type == UNPOPULAR_VIEW) {
            return LayoutInflater.from(getContext()).inflate(R.layout.unpopular_movie, null);
        } else {
            return null;
        }
    }

    private View getUnpopularView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        UnpopularViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new UnpopularViewHolder();
            LayoutInflater infater = LayoutInflater.from(getContext());
            convertView = infater.inflate(R.layout.unpopular_movie, parent, false);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.idMovieImage);
            viewHolder.title = (TextView) convertView.findViewById(R.id.movieTitle);
            viewHolder.releaseDate = (TextView) convertView.findViewById(R.id.movieReleaseDate);
            viewHolder.overview = (TextView) convertView.findViewById(R.id.movieOverview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UnpopularViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(movie.getOriginalTitle());
        viewHolder.releaseDate.setText("Release Date: " + movie.getReleaseDate());
        viewHolder.overview.setText(movie.getOverview());
        viewHolder.image.setImageResource(0);
        Picasso.with(getContext()).load(movie.getPosterPath()).fit().centerCrop()
                .transform(new RoundedCornersTransformation(20, 20))
                .placeholder(R.drawable.load)
                .error(R.drawable.error)
                .into(viewHolder.image);
        return convertView;
    }

    private View getPopularView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        PopularViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new PopularViewHolder();
            LayoutInflater infater = LayoutInflater.from(getContext());
            convertView = infater.inflate(R.layout.popular_movie, parent, false);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.idMovieImage);
            viewHolder.overlay = (ImageView) convertView.findViewById(R.id.movieOverlay);
            viewHolder.title = (TextView) convertView.findViewById(R.id.movieTitle);
            viewHolder.releaseDate = (TextView) convertView.findViewById(R.id.movieReleaseDate);
            viewHolder.overview = (TextView) convertView.findViewById(R.id.movieOverview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PopularViewHolder) convertView.getTag();
        }

        viewHolder.image.setImageResource(0);
        viewHolder.overlay.setImageResource(0);

        int orientation = getContext().getResources().getConfiguration().orientation;
        int width, height, overlaySize, overlayTopMargin;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // fill whole view
            width = parent.getWidth();
            height = (int)(parent.getWidth() * 0.75);
            overlaySize = (int)(width * 0.3333);
            overlayTopMargin = (int)((height - overlaySize) * 0.29);
        } else {
            // fill most of view
            width = (int)(parent.getWidth() * 0.6);
            height = (int)(parent.getWidth() * 0.75);
            overlaySize = (int)(width * 0.3333);
            overlayTopMargin = (int)((height - overlaySize) * 0.12);
        }

        Picasso.with(getContext()).load(movie.getBackdropPath())
                .resize(width, height)
                .centerInside()
                .transform(new RoundedCornersTransformation(20, 20))
                .placeholder(R.drawable.load)
                .error(R.drawable.error)
                .into(viewHolder.image);

        Picasso.with(getContext()).load("https://whohubapp.com/images/video-play-xxl.png")
                .resize(overlaySize, overlaySize)
                .into(viewHolder.overlay);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(
                (int)((width - overlaySize) * 0.5),
                overlayTopMargin,
                0,
                0);
        viewHolder.overlay.setLayoutParams(layoutParams);

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewHolder.title.setText("");
            viewHolder.releaseDate.setText("");
            viewHolder.overview.setText("");
        } else {
            viewHolder.title.setText(movie.getOriginalTitle());
            viewHolder.releaseDate.setText("Release Date: " + movie.getReleaseDate());
            viewHolder.overview.setText(movie.getOverview());
        }

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case POPULAR_VIEW:
                return getPopularView(position, convertView, parent);
            case UNPOPULAR_VIEW:
                return getUnpopularView(position, convertView, parent);
            default:
                return null;
        }
    }
}
