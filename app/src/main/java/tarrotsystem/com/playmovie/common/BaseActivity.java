package tarrotsystem.com.playmovie.common;

import android.support.annotation.LayoutRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import tarrotsystem.com.playmovie.R;

/**
 * Created by DOTECH on 12/05/2017.
 */

public class BaseActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progressBar)ProgressBar mProgressBar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupToolbar();
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        //getSupportActionBar().setElevation(3.0f);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void changeToolbarTitle(String sortType) {
       // if(sortType.equals(getString(R.string.popular)))
          //  getSupportActionBar().setTitle(getString(R.string.title_popular_movies));
        //else if(sortType.equals(getString(R.string.top_rated)))
          //  getSupportActionBar().setTitle(getString(R.string.title_top_rated));
    }
    public void showProgressDialog(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressDialog(){
        mProgressBar.setVisibility(View.INVISIBLE);
    }

}
