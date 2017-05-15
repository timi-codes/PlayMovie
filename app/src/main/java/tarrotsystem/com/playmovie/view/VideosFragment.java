package tarrotsystem.com.playmovie.view;


import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import tarrotsystem.com.playmovie.adapter.VideosAdapter;
import tarrotsystem.com.playmovie.model.Trailer;
import tarrotsystem.com.playmovie.utilities.JSONObjectUtil;
import tarrotsystem.com.playmovie.utilities.NetworkUtils;
import tarrotsystem.com.playmovie.utilities.Utils;

import static tarrotsystem.com.playmovie.utilities.NetworkUtils.TAG_MOVIES;


public class VideosFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Trailer>>,VideosAdapter.Callbacks {

    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private VideosAdapter videosAdapter;
    private List<Trailer> mVideos = new ArrayList<>();
    private JSONObjectUtil.Movies movies;
    private final int TRAILER_ID = 202;
    private Bundle savedInstanceState;



    public static VideosFragment newInstance(JSONObjectUtil.Movies movies){
        if (movies == null) {
            throw new IllegalArgumentException("The Movies Data can not be null");
        }
        Bundle args = new Bundle();
        args.putParcelable(TAG_MOVIES, movies);

        VideosFragment fragment = new VideosFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        movies = getArguments().getParcelable(TAG_MOVIES);
        this.savedInstanceState = savedInstanceState;
        loadMovieTrailer();
    }


    private void loadMovieTrailer() {
        Bundle bundle = new Bundle();
        bundle.putCharSequenceArray(getString(R.string.trailer), new String[]{Integer.toString(movies.getId()),getString(R.string.video)});
        if(savedInstanceState == null)
            this.getLoaderManager().initLoader(TRAILER_ID,bundle,this);
        else
            getLoaderManager().restartLoader(TRAILER_ID,bundle,this);
    }


    @Override
    public void onResume() {
        super.onResume();
        loadMovieTrailer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_videos, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int columnCount = 3;

        recyclerView = (RecyclerView)view.findViewById(R.id.videoView);
        gridLayoutManager = new GridLayoutManager(getActivity(), columnCount);
        int spacing = Utils.dpToPx(5, getActivity()); // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(columnCount, spacing, includeEdge));
        recyclerView.setLayoutManager(gridLayoutManager);

        initAdapter(mVideos);
    }

    private void initAdapter(List<Trailer> movieVideos) {
        videosAdapter = new VideosAdapter(getContext());
        videosAdapter.setCallbacks(this);
        recyclerView.setAdapter(videosAdapter);
    }

    @Override
    public android.support.v4.content.Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
        final String params [] = args.getStringArray(getString(R.string.trailer));

        return new AsyncTaskLoader<List<Trailer>>(getContext()) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }
            @Override
            public List<Trailer> loadInBackground() {
                if (params.length == 0) {
                    return null;
                }
                List<Trailer> jsonResponse = null;
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
                        jsonResponse = new Trailer().getTrailerListFromJSonResponse(movieResponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                return jsonResponse;
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Trailer>> loader, List<Trailer> data) {
        Log.d("VIDEO",data.get(0).getKey());

        videosAdapter.setVideolistData(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Trailer>> loader) {

    }

    @Override
    public void onVideoClick(Trailer movieVideos) {
        inflateVideoPlayer(movieVideos.getKey());

    }

    private void inflateVideoPlayer(String videoKey) {
        int startTimeMillis = 0;
        boolean autoplay = true;
        boolean lightboxMode = false;

        Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                getActivity(), Utils.Google_Key, videoKey, startTimeMillis, autoplay, lightboxMode);

        if (intent != null) {
            if (canResolveIntent(intent)) {
                startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
            } else {
                // Could not resolve the intent - must need to install or update the YouTube API service.
                YouTubeInitializationResult.SERVICE_MISSING
                        .getErrorDialog(getActivity(), REQ_RESOLVE_SERVICE_MISSING).show();
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != getActivity().RESULT_OK) {
            YouTubeInitializationResult errorReason =
                    YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(getActivity(), 0).show();
            } else {
                String errorMessage = getResources().getString(R.string.player_error) + errorReason.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }
}
