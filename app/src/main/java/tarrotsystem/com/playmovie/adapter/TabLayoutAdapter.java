package tarrotsystem.com.playmovie.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tarrotsystem.com.playmovie.utilities.JSONObjectUtil;
import tarrotsystem.com.playmovie.view.OverviewFragment;

/**
 * Created by DOTECH on 12/05/2017.
 */

public class TabLayoutAdapter extends FragmentStatePagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"Overview", "Videos", "Reviews"};
    private FragmentManager fm;
    private JSONObjectUtil.Movies movies;

    public TabLayoutAdapter(FragmentManager fm, JSONObjectUtil.Movies movies) {
        super(fm);
        this.fm=fm;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return OverviewFragment.newInstance(movies);
            case 1:
                return OverviewFragment.newInstance(movies);
            case 2:
                return OverviewFragment.newInstance(movies);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
