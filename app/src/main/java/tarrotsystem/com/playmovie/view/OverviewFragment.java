package tarrotsystem.com.playmovie.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import tarrotsystem.com.playmovie.R;
import tarrotsystem.com.playmovie.utilities.JSONObjectUtil;
import tarrotsystem.com.playmovie.utilities.NetworkUtils;
import tarrotsystem.com.playmovie.utilities.Utils;

import static tarrotsystem.com.playmovie.utilities.NetworkUtils.TAG_MOVIES;

/**
 * Created by HP-HP on 19-05-2016.
 */
public class OverviewFragment extends Fragment {

    private RatingBar mRatingBar;
    private JSONObjectUtil.Movies movies;

    TextView releaseDate;
    TextView ratings;
    TextView overview;

    public static OverviewFragment newInstance(JSONObjectUtil.Movies movies){
        if (movies == null) {
            throw new IllegalArgumentException("The Movies Data can not be null");
        }
        Bundle args = new Bundle();
        args.putParcelable(TAG_MOVIES, movies);

        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movies = getArguments().getParcelable(TAG_MOVIES);
        releaseDate = (TextView) view.findViewById(R.id.movieReleaseDate);
        overview = (TextView) view.findViewById(R.id.movieOverview);
        ratings = (TextView) view.findViewById(R.id.movieRating);

        mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        inflateData();
    }


    private void inflateData() {
        releaseDate.setText(Utils.formatReleaseDate(movies.getRelease_date()));
        float rating = (float)Math.round(Double.parseDouble(movies.getVote_average()) * 10) / 10;
        ratings.setText(movies.getVote_average()+"/10");
        mRatingBar.setRating(rating);
        overview.setText(movies.getOverview());
    }
}
