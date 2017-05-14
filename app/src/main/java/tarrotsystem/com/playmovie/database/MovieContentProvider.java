package tarrotsystem.com.playmovie.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static tarrotsystem.com.playmovie.database.MovieContract.CONTENT_AUTHORITY;
import static tarrotsystem.com.playmovie.database.MovieContract.FAVOURITE_PATH;
import static tarrotsystem.com.playmovie.database.MovieContract.FavoriteEntry.TABLE_NAME;

/**
 * Created by DOTECH on 13/05/2017.
 */

public class MovieContentProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int MOVIE = 300;
    private static final int MOVIE_WITH_ID = 301;

    private SQLiteDatabase db;
    private MovieDBHelper mOpenHelper;


    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.FAVOURITE_PATH, MOVIE);
        matcher.addURI(authority, MovieContract.FAVOURITE_PATH + "/*", MOVIE_WITH_ID);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        db = mOpenHelper.getReadableDatabase();
        Cursor retCursor = null;
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIE:
                try {
                    retCursor = db.query(
                            TABLE_NAME,
                            null,
                            null,
                            null,
                            null,
                            null,
                            MovieContract.FavoriteEntry._ID);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case MOVIE_WITH_ID:
                try {
                    retCursor = db.query(TABLE_NAME,null,null,null,null,null,null);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                break;
            default:
                throw new UnsupportedOperationException("error:" + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                return MovieContract.FavoriteEntry.CONTENT_DIR_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.FavoriteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri,ContentValues values)  {
        db = mOpenHelper.getWritableDatabase();

        final int matchid = sUriMatcher.match(uri);
        Uri returnUri;
        switch (matchid){
            case MOVIE:
                long id = db.insert(TABLE_NAME,null,values);
                if(id > 0)
                    returnUri = ContentUris.withAppendedId(MovieContract.FavoriteEntry.CONTENT_URI,id);
                else
                    returnUri = null;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }


    @Override
    public int delete(Uri uri,String s,String[] strings) {
        return 0;
    }


    @Override
    public int update(Uri uri,ContentValues contentValues,String s,String[] strings) {
        return 0;
    }
}
