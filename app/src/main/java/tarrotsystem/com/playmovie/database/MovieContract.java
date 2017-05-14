package tarrotsystem.com.playmovie.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by DOTECH on 13/05/2017.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.tarrotsystem.playmovie";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String FAVOURITE_PATH = "favorite";

    public final static class FavoriteEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(FAVOURITE_PATH)
                .build();

        //table name
        public static final String TABLE_NAME = "favorite";
        //columns
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTEAVERAGE = "voteAverage";
        public static final String COLUMN_RELEASEDATE = "releasedate";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_BACKDROP = "backdrop";

        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
          ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + FAVOURITE_PATH;

        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + FAVOURITE_PATH;

    }
}
