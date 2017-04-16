package tarrotsystem.com.playmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import pttextview.widget.PTTextView;
import tarrotsystem.com.playmovie.utilities.NetworkUtils;
import tarrotsystem.com.playmovie.utilities.GenUtils;

/**
 * Created by DOTECH on 15/04/2017.
 */


public class DetailActivity extends AppCompatActivity {

    private PTTextView title;
    private PTTextView genre;
    private TextView releaseDate;
    private TextView elapseTime;
    private TextView ratings;
    private TextView overview;

    private Toolbar toolbar;


    private ImageView posterImage,backDrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        title = (PTTextView)findViewById ( R.id.movie_title);
        genre = (PTTextView)findViewById ( R.id.genre);
        releaseDate = (TextView)findViewById ( R.id.release_date);
        elapseTime = (TextView)findViewById ( R.id.elapse_time);
        overview = (TextView)findViewById ( R.id.overview);

        ratings = (TextView)findViewById ( R.id.ratings);
        posterImage = (ImageView)findViewById ( R.id.poster_img);
        backDrop = (ImageView)findViewById ( R.id.backdrop);

        initView();

    }

    private void initView() {
        Intent intent=getIntent();
        String movieTitle,movieReleaseDate,movieRatings,overView;

        if (intent.hasExtra(getString(R.string.movieposter))){
            movieTitle = intent.getStringExtra(getString(R.string.movietitle));
            movieReleaseDate = intent.getStringExtra(getString(R.string.moviereleasedate));
            movieRatings = intent.getStringExtra(getString(R.string.movierating));
            overView = intent.getStringExtra(getString(R.string.overviews));

            int[] genreiD = intent.getIntArrayExtra(getString(R.string.moviegenre));
            genre.setText(GenUtils.genreName(genreiD));
            title.setText(movieTitle);


            releaseDate.setText(GenUtils.formatReleaseDate(movieReleaseDate));
            ratings.setText(movieRatings);
            overview.setText(overView);



            String imageUrl = NetworkUtils.POSTER_BASE_URL + intent.getStringExtra(getString(R.string.movieposter));
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .into(posterImage);

            String backDropUrl = NetworkUtils.BACKDROP_BASE_URL + intent.getStringExtra(getString(R.string.backdrop));
            Glide.with(this)
                    .load(backDropUrl)
                    .centerCrop()
                    .into(backDrop);


        }

    }


}
