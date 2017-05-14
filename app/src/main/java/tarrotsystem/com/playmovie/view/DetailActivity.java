package tarrotsystem.com.playmovie.view;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import pttextview.widget.PTTextView;
import tarrotsystem.com.playmovie.R;
import tarrotsystem.com.playmovie.adapter.TabLayoutAdapter;
import tarrotsystem.com.playmovie.database.MovieContract;
import tarrotsystem.com.playmovie.database.MovieDBHelper;
import tarrotsystem.com.playmovie.utilities.JSONObjectUtil;
import tarrotsystem.com.playmovie.utilities.NetworkUtils;
import tarrotsystem.com.playmovie.utilities.Utils;
import tarrotsystem.com.playmovie.utilities.ViewUtil;

/**
 * Created by DOTECH on 15/04/2017.
 */


public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.movie_title)
    PTTextView title;

    @BindView(R.id.genre)
    PTTextView genre;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.poster_img)
    ImageView posterImage;

    @BindView(R.id.backdrop)
    ImageView backDrop;
    @BindView(R.id.favButton)
    FloatingActionButton mFavoriteButton;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private TabLayoutAdapter mTabLayoutAdapter;

    private boolean isFavoriteChanged = false;

    private JSONObjectUtil.Movies movies;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        inflateData();
        setUpTabLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void inflateData() {
        Intent intent=getIntent();

        if (intent.hasExtra(getString(R.string.movie))){
             movies = intent.getParcelableExtra(getString(R.string.movie));

           // genre.setText(Utils.genreName(movies.getGenre_ids()));
            title.setText(movies.getOriginal_title());


            String imageUrl = NetworkUtils.POSTER_BASE_URL + movies.getPoster_path();
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .into(posterImage);

            String backDropUrl = NetworkUtils.BACKDROP_BASE_URL + movies.getBackdrop_path();
            Glide.with(this)
                    .load(backDropUrl)
                    .centerCrop()
                    .into(backDrop);

            if (movies.isFavorite())mFavoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_favorite_bold));



            mFavoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (movies.isFavorite()){
                        getContentResolver().delete(MovieContract.FavoriteEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movies.getId())).build(), null, null);

                        ViewUtil.showToast(getResources().getString(R.string.removed_favorite),v.getContext());
                        mFavoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_favorite));
                        movies.setFavorite(false);


                    }else {
                        ContentValues values = MovieDBHelper.getMovieContentValues(movies);
                        getContentResolver().insert(MovieContract.FavoriteEntry.CONTENT_URI, values);

                        ViewUtil.showToast(getResources().getString(R.string.added_favorite),v.getContext());
                        mFavoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_favorite_bold));
                        movies.setFavorite(true);
                    }
                }
            });
        }
    }

    private void setUpTabLayout(){
        mTabLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager(), movies);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mTabLayoutAdapter);
        tabLayout.setTabTextColors(getResources().getColor(R.color.colorLightGrey), getResources().getColor(R.color.colorAccent));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        tabLayout.setSelectedTabIndicatorHeight(Utils.dpToPx(3,getApplicationContext()));
        tabLayout.setupWithViewPager(mViewPager);
    }


}
