package tarrotsystem.com.playmovie.view;


import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import tarrotsystem.com.playmovie.R;
import tarrotsystem.com.playmovie.adapter.ReviewsAdapter;
import tarrotsystem.com.playmovie.adapter.VideosAdapter;
import tarrotsystem.com.playmovie.model.MovieReviews;
import tarrotsystem.com.playmovie.model.Trailer;
import tarrotsystem.com.playmovie.utilities.JSONObjectUtil;
import tarrotsystem.com.playmovie.utilities.NetworkUtils;
import tarrotsystem.com.playmovie.utilities.Utils;

import static tarrotsystem.com.playmovie.utilities.NetworkUtils.TAG_MOVIES;


public class ReviewsFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<MovieReviews>> {

    //@BindView(R.id.videoView)
    RecyclerView recyclerView;


    private LinearLayoutManager linearLayoutManager;
    private ReviewsAdapter videosAdapter;
    private List<MovieReviews> movieReviews = new ArrayList<>();
    private JSONObjectUtil.Movies movies;
    private final int REVIEW_ID = 202;
    private Bundle savedInstanceState;
    private View noReviewsView;




    public static ReviewsFragment newInstance(JSONObjectUtil.Movies movies){
        if (movies == null) {
            throw new IllegalArgumentException("The Movies Data can not be null");
        }
        Bundle args = new Bundle();
        args.putParcelable(TAG_MOVIES, movies);

        ReviewsFragment fragment = new ReviewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        movies = getArguments().getParcelable(TAG_MOVIES);
        this.savedInstanceState = savedInstanceState;
        loadMovieReview();
    }


    private void loadMovieReview() {
        Bundle bundle = new Bundle();
        bundle.putCharSequenceArray(getString(R.string.review), new String[]{Integer.toString(movies.getId()),getString(R.string.review)});
        if(savedInstanceState == null)
            this.getLoaderManager().initLoader(REVIEW_ID,bundle,this);
        else
            getLoaderManager().restartLoader(REVIEW_ID,bundle,this);
    }


    @Override
    public void onResume() {
        super.onResume();
        loadMovieReview();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reviews, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int columnCount = 3;

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        int spacing = Utils.dpToPx(5, getActivity()); // 50px
        recyclerView.setLayoutManager(linearLayoutManager);

        initAdapter(movieReviews);
    }

    private void initAdapter(List<MovieReviews> movieVideos) {
        videosAdapter = new ReviewsAdapter(movieVideos,getContext());
        recyclerView.setAdapter(videosAdapter);
    }


    @Override
    public android.support.v4.content.Loader<List<MovieReviews>> onCreateLoader(int id, Bundle args) {
        final String params [] = args.getStringArray(getString(R.string.review));

        return new AsyncTaskLoader<List<MovieReviews>>(getContext()) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                //mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }
            @Override
            public List<MovieReviews> loadInBackground() {
                if (params.length == 0) {
                    return null;
                }
                List<MovieReviews> jsonResponse = null;
                String movieid = params[0];
                String type = params[1];
                URL movieRequestURL = NetworkUtils.buildVideoReviewUrl(movieid,type,getString(R.string.api_key));
                    String movieResponse = null;
                    try {
                        movieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonResponse = new MovieReviews().getReviewListFromJSonResponse(movieResponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                return jsonResponse;
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<MovieReviews>> loader, List<MovieReviews> data) {
        //mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data!=null){
            videosAdapter = new ReviewsAdapter(data,getContext());
        }else{
            showNoReviews(true);
        }

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<MovieReviews>> loader) {

    }

    private void showNoReviews(boolean value){

        int noReviewsVisibility = value? View.VISIBLE : View.GONE;
        noReviewsView.setVisibility(noReviewsVisibility);

        int recyclerViewVisibility = value? View.GONE : View.VISIBLE;
        recyclerView.setVisibility(recyclerViewVisibility);
    }
}
