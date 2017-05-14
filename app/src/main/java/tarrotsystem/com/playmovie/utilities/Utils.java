package tarrotsystem.com.playmovie.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.TypedValue;

import java.text.SimpleDateFormat;
import java.util.Date;

import tarrotsystem.com.playmovie.R;

/**
 * Created by DOTECH on 15/04/2017.
 */

public class Utils {
    public static final String Google_Key = "AIzaSyCUXOiFGNCO1QOzLIpj3nZaJ8zyTkpERlc";

    public static String getSortOrder(Context vcontext){
        SharedPreferences shared = vcontext.getSharedPreferences(vcontext.getString(R.string.preference),Context.MODE_PRIVATE);
        return shared.getString(vcontext.getString(R.string.sort_type),vcontext.getString(R.string.popular));
    }

    public static void setSortOrder(Context context,String sorttype){
        SharedPreferences shared = context.getSharedPreferences(context.getString(R.string.preference),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(context.getString(R.string.sort_type),sorttype);
        editor.commit();
    }

    public static String genreName(int[] id){
        // genre
        String genreStr = "";
        for (int str : id) {
            if (str==16)
                genreStr += "Animation" + " | ";
            else if(str==35)
                genreStr += "Comedy" + " | ";
            else if(str==28)
                genreStr += "Action" + " | ";
            else if(str==12)
                genreStr += "Adventure" + " | ";
            else if(str==80)
                genreStr += "Crime" + " | ";
            else if(str==99)
                genreStr += "Documentary" + " | ";
            else if(str==18)
                genreStr += "Drama" + " | ";
            else if(str==10751)
                genreStr += "Family" + " | ";
            else if(str==14)
                genreStr += "Fantansy" + " | ";
            else if(str==36)
                genreStr += "History" + " | ";
            else if(str==27)
                genreStr += "Horror" + " | ";
            else if(str==10402)
                genreStr += "Music" + " | ";
            else if(str==9648)
                genreStr += "Mystery" + " | ";
            else if(str==10749)
                genreStr += "Romance" + " | ";
            else if(str==878)
                genreStr += "Science Fiction" + " | ";
            else if(str==10770)
                genreStr += "Tv Movie" + " | ";
            else if(str==53)
                genreStr += "Thriller" + " | ";
            else if(str==10752)
                genreStr += "War" + " | ";
            else if(str==37)
                genreStr += "Western" + " | ";
        }

        return genreStr = genreStr.length() > 0 ? genreStr.substring(0,
                genreStr.length() - 2) : genreStr;
    }

    public static String formatReleaseDate(String date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = null;
        try {
            convertedDate = formatter.parse(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat postFormatter = new SimpleDateFormat("MMM dd, yyyy");
        return  postFormatter.format(convertedDate);
    }

    public static int dpToPx(float dp, Context context) {
        return dpToPx(dp, context.getResources());
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }
}
