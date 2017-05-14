package tarrotsystem.com.playmovie.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import tarrotsystem.com.playmovie.R;
import tarrotsystem.com.playmovie.adapter.MovieAdapter;
import tarrotsystem.com.playmovie.utilities.Utils;
import tarrotsystem.com.playmovie.utilities.JSONObjectUtil;
import tarrotsystem.com.playmovie.utilities.NetworkUtils;

import static tarrotsystem.com.playmovie.database.MovieContract.FavoriteEntry.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickListener,
        LoaderManager.LoaderCallbacks<List<JSONObjectUtil.Movies>>{

  /*  @BindView(R.id.toolbar)*/
    Toolbar mToolBar;

    /*@BindView(R.id.progressBar)
    ProgressBar mProgressBar;
*/
    @BindView(R.id.linearLayout)
    LinearLayout container;


    //@BindView(R.id.rv_list)
    RecyclerView movie_list;

   // @BindView(R.id.empty_list)
    TextView empty_view;

    private Snackbar snackbar;
    private MovieAdapter movieAdapter;
    private static int SPAN_COUNT = 2;
    private final int LOADER_ID = 1101;
    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        empty_view = (TextView) findViewById(R.id.empty_list);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        movie_list = (RecyclerView) findViewById(R.id.rv_list) ;
        setSupportActionBar(mToolBar);
        getSupportActionBar().setElevation(3.0f);

        movieAdapter = new MovieAdapter(this,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
        int spacing = Utils.dpToPx(5, this); // 50px
        boolean includeEdge = false;

        movie_list.addItemDecoration(new GridSpacingItemDecoration(SPAN_COUNT, spacing, includeEdge));
        movie_list.setLayoutManager(gridLayoutManager);
        movie_list.setHasFixedSize(true);
        movie_list.setAdapter(movieAdapter);

        if (savedInstanceState==null) reLoadMovies(Utils.getSortOrder(this));

    }

    private void reLoadMovies(String sortType){
        empty_view.setVisibility(View.GONE);
        movie_list.setVisibility(View.VISIBLE);
        changeToolbarTitle(sortType);

        Bundle bundle = new Bundle();
        bundle.putCharSequenceArray(getString(R.string.PARAM), new String[]{sortType, getString(R.string.api_key)});
        getSupportLoaderManager().initLoader(LOADER_ID,bundle,this);
    }

    private void loadMovies(String sortType) {
        empty_view.setVisibility(View.GONE);
        movie_list.setVisibility(View.VISIBLE);
        changeToolbarTitle(sortType);

        Bundle bundle = new Bundle();
        bundle.putCharSequenceArray(getString(R.string.PARAM), new String[]{sortType, getString(R.string.api_key)});
        getSupportLoaderManager().restartLoader(LOADER_ID,bundle,this);
    }

    private void changeToolbarTitle(String sortType) {
        if(sortType.equals(getString(R.string.popular)))
            getSupportActionBar().setTitle(getString(R.string.title_popular_movies));
        else if(sortType.equals(getString(R.string.top_rated)))
            getSupportActionBar().setTitle(getString(R.string.title_top_rated));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if(!Utils.getSortOrder(this).equals(getString(R.string.popular)))
            menu.findItem(R.id.menu_sort_by_ratings).setChecked(true);
        return true;
    }


    private Loader<String> getLoader(){
        loaderManager = getSupportLoaderManager();
        return loaderManager.getLoader(LOADER_ID);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_sort_by_popular:
                if (item.isChecked()) item.setChecked(true);
                else
                    item.setChecked(true);
                    Utils.setSortOrder(this,getString(R.string.popular));
                    loadMovies(getResources().getString(R.string.popular));
                return true;
            case R.id.menu_sort_by_ratings:
                if (item.isChecked()) item.setChecked(true);
                else item.setChecked(true);
                Utils.setSortOrder(this,getString(R.string.top_rated));
                loadMovies(getResources().getString(R.string.top_rated));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onItemClicked(JSONObjectUtil.Movies response) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("MOVIE",response);
        startActivity(intent);
    }

    @Override
    public Loader<List<JSONObjectUtil.Movies>> onCreateLoader(int id, Bundle args) {
        final String params[] = args.getStringArray(getString(R.string.PARAM));
        return new AsyncTaskLoader<List<JSONObjectUtil.Movies>>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                //mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }
            @Override
            public List<JSONObjectUtil.Movies> loadInBackground() {
                if (params.length == 0) {
                    return null;
                }

                String sortorder = params[0];
                String apikey = params[1];
                URL movieRequestURL = NetworkUtils.buildUrl(sortorder,apikey);

                try {
                    if(!sortorder.equals(getString(R.string.favorite))){
                        String movieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
                        List<JSONObjectUtil.Movies> MovieJSONResponse = new JSONObjectUtil().getMovieListFromJSonResponse( movieResponse);
                        return MovieJSONResponse;
                    }
                    else{
                        Cursor cursor = getContentResolver().query(CONTENT_URI,null,null,null,null);
                        List<JSONObjectUtil.Movies> MovieJSONResponse = new JSONObjectUtil().getMovieListFromCursor( cursor);
                        return MovieJSONResponse;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<JSONObjectUtil.Movies>> loader, List<JSONObjectUtil.Movies> moviedata) {
        //mProgressBar.setVisibility(View.INVISIBLE);
        if (moviedata != null) {
            movieAdapter.setMovieData(moviedata);
        } else {
            empty_view.setVisibility(View.VISIBLE);
            movie_list.setVisibility(View.GONE);
            showSnackBar();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<JSONObjectUtil.Movies>> loader) {

    }

    private void showSnackBar(){
        snackbar = Snackbar
                .make(container,"Check your Internet Connection.",Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY",new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        loadMovies(Utils.getSortOrder(MainActivity.this));
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadMovies(Utils.getSortOrder(MainActivity.this));
    }
}
