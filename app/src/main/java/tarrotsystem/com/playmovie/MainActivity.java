package tarrotsystem.com.playmovie;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
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

import tarrotsystem.com.playmovie.utilities.GenUtils;
import tarrotsystem.com.playmovie.utilities.JSONObjectUtil;
import tarrotsystem.com.playmovie.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickListener{

    private ProgressBar mProgressBar;
    private LinearLayout container;
    private Snackbar snackbar;
    private RecyclerView movie_list;
    private TextView empty_view;
    private MovieAdapter movieAdapter;
    private static int SPAN_COUNT = 2;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(mToolBar);
        getSupportActionBar().setElevation(3.0f);

        container = (LinearLayout) findViewById(R.id.linearLayout) ;



        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        empty_view = (TextView) findViewById(R.id.empty_list);
        movie_list = (RecyclerView) findViewById(R.id.rv_list);

        movieAdapter = new MovieAdapter(this,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT);

        movie_list.setLayoutManager(gridLayoutManager);
        movie_list.setHasFixedSize(true);
        movie_list.setAdapter(movieAdapter);

        //Uses the set preference
        loadMovies(GenUtils.getSortOrder(this));
    }

    private void loadMovies(String sortType) {
        empty_view.setVisibility(View.GONE);
        movie_list.setVisibility(View.VISIBLE);
        changeToolbarTitle(sortType);
        new FetchMovies().execute(sortType);
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
        if(!GenUtils.getSortOrder(this).equals(getString(R.string.popular)))
            menu.findItem(R.id.menu_sort_by_ratings).setChecked(true);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_sort_by_popular:
                if (item.isChecked()) item.setChecked(true);
                else
                    item.setChecked(true);
                    GenUtils.setSortOrder(this,getString(R.string.popular));
                    loadMovies(getResources().getString(R.string.popular));
                return true;
            case R.id.menu_sort_by_ratings:
                if (item.isChecked()) item.setChecked(true);
                else item.setChecked(true);
                GenUtils.setSortOrder(this,getString(R.string.top_rated));
                loadMovies(getResources().getString(R.string.top_rated));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onItemClicked(JSONObjectUtil.JSONResponse response) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("MOVIE",response);
        startActivity(intent);
    }

    public class FetchMovies extends AsyncTask<String, Void, List<JSONObjectUtil.JSONResponse>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<JSONObjectUtil.JSONResponse> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String sortorder = params[0];
            String apiKey = getResources().getString(R.string.api_key);
            URL movieRequestURL = NetworkUtils.buildUrl(sortorder,apiKey);

            try {
                String movieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
                List<JSONObjectUtil.JSONResponse> jsonResponse = new JSONObjectUtil().getMovieListFromJSonResponse(movieResponse);
                Log.d("MainActivity",movieResponse);
                return jsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<JSONObjectUtil.JSONResponse> moviedata) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (moviedata != null) {
                movieAdapter.setMovieData(moviedata);
            } else {
                empty_view.setVisibility(View.VISIBLE);
                movie_list.setVisibility(View.GONE);
                showSnackBar();
            }
        }

    }

    private void showSnackBar(){
        snackbar = Snackbar
                .make(container,"Check your Internet Connection.",Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY",new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        loadMovies(GenUtils.getSortOrder(MainActivity.this));
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

}
