package tarrotsystem.com.playmovie.utilities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DOTECH on 15/04/2017.
 */

public class JSONObjectUtil {
    private static final String RESULT = "results";
    private static final String ID = "id";
    private static final String BACKDROP = "backdrop_path";
    private static final String GENEREID = "genre_ids";
    private static final String ORIGINALTITLE = "original_title";
    private static final String OVERVIEW = "overview";
    private static final String POSTERPATH = "poster_path";
    private static final String RELEASEDATE = "release_date";
    private static final String VOTEAVERAGE = "vote_average";

    private List<JSONResponse> parsedMovies;
    private JSONResponse response;

    public  List<JSONResponse> getMovieListFromJSonResponse(String jsonResponse) throws JSONException {
        JSONObject object = new JSONObject(jsonResponse);
        JSONArray data = object.getJSONArray(RESULT);
        parsedMovies = new ArrayList<>();
        for(int i= 0; i<data.length(); i++){
            JSONObject moviedata = (JSONObject)data.get(i);
            response = new JSONResponse();

            response.setId(moviedata.getInt(ID));
            response.setBackdrop_path(moviedata.getString(BACKDROP));
            response.setOriginal_title(moviedata.getString(ORIGINALTITLE));
            response.setOverview(moviedata.getString(OVERVIEW));
            response.setPoster_path(moviedata.getString(POSTERPATH));
            response.setRelease_date(moviedata.getString(RELEASEDATE));
            response.setVote_average(moviedata.getString(VOTEAVERAGE));



            JSONArray genre = moviedata.getJSONArray(GENEREID);
            int [] temp = new int[genre.length()];
            for(int j=0; j<genre.length();j++){
                temp[j] = genre.getInt(j);
            }

            response.setGenre_ids(temp);

            parsedMovies.add(response);

        }

        return  parsedMovies;
    }

    @SuppressWarnings("serial")

    public static class JSONResponse implements Parcelable {
        private String original_title;
        private String poster_path;
        private String overview;
        private String vote_average;
        private String release_date;
        private int id;
        private int [] genre_ids;
        private String backdrop_path;



        public String getOriginal_title() {
            return original_title;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getVote_average() {
            return vote_average;
        }

        public void setVote_average(String vote_average) {
            this.vote_average = vote_average;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        public int[] getGenre_ids() {
            return genre_ids;
        }

        public void setGenre_ids(int[] genre_ids) {
            this.genre_ids = genre_ids;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public void setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.original_title);
            dest.writeString(this.poster_path);
            dest.writeString(this.overview);
            dest.writeString(this.vote_average);
            dest.writeString(this.release_date);
            dest.writeInt(this.id);
            dest.writeIntArray(this.genre_ids);
            dest.writeString(this.backdrop_path);
        }

        public JSONResponse() {
        }

        public JSONResponse(Parcel in) {
            this.original_title = in.readString();
            this.poster_path = in.readString();
            this.overview = in.readString();
            this.vote_average = in.readString();
            this.release_date = in.readString();
            this.id = in.readInt();
            this.genre_ids = in.createIntArray();
            this.backdrop_path = in.readString();
        }

        public static final Parcelable.Creator<JSONResponse> CREATOR = new Parcelable.Creator<JSONResponse>() {
            @Override
            public JSONResponse createFromParcel(Parcel source) {
                return new JSONResponse(source);
            }

            @Override
            public JSONResponse[] newArray(int size) {
                return new JSONResponse[size];
            }
        };
    }

}
