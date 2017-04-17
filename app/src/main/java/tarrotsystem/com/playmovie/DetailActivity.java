package tarrotsystem.com.playmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import pttextview.widget.PTTextView;
import tarrotsystem.com.playmovie.utilities.JSONObjectUtil;
import tarrotsystem.com.playmovie.utilities.NetworkUtils;
import tarrotsystem.com.playmovie.utilities.GenUtils;

/**
 * Created by DOTECH on 15/04/2017.
 */


public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.movie_title) PTTextView title;
    @BindView(R.id.genre) PTTextView genre;
    @BindView(R.id.release_date) TextView releaseDate;
    @BindView(R.id.ratings) TextView ratings;
    @BindView(R.id.overview) TextView overview;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.poster_img) ImageView posterImage;
    @BindView(R.id.backdrop) ImageView backDrop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
    }

    private void initView() {
        Intent intent=getIntent();

        if (intent.hasExtra(getString(R.string.movie))){
            JSONObjectUtil.JSONResponse response = intent.getParcelableExtra(getString(R.string.movie));

            genre.setText(GenUtils.genreName(response.getGenre_ids()));
            title.setText(response.getOriginal_title());


            releaseDate.setText(GenUtils.formatReleaseDate(response.getRelease_date()));
            ratings.setText(response.getVote_average());
            overview.setText(response.getOverview());



            String imageUrl = NetworkUtils.POSTER_BASE_URL + response.getPoster_path();
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .into(posterImage);

            String backDropUrl = NetworkUtils.BACKDROP_BASE_URL + response.getBackdrop_path();
            Glide.with(this)
                    .load(backDropUrl)
                    .centerCrop()
                    .into(backDrop);


        }

    }


}
