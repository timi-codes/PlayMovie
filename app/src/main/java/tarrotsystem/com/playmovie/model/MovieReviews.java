package tarrotsystem.com.playmovie.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP-HP on 27-03-2016.
 */
public class MovieReviews implements Parcelable {

    private String id;
    private String author;
    private String content;
    private String url;
    private List<MovieReviews>parsedReview;
    private MovieReviews reviewresponse;
    private static final String RESULT = "results";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);

    }

    //creator
    public static final Creator CREATOR = new Creator() {

        public MovieReviews createFromParcel(Parcel in) {
            return new MovieReviews(in);
        }

        public MovieReviews[] newArray(int size) {
            return new MovieReviews[size];
        }

    };

    public MovieReviews(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public MovieReviews(){

    }

    public List<MovieReviews> getReviewListFromJSonResponse(String jsonResponse) throws JSONException {
        JSONObject object = new JSONObject(jsonResponse);
        JSONArray data = object.getJSONArray(RESULT);
        parsedReview = new ArrayList<>();
        for(int i= 0; i<data.length(); i++){
            JSONObject reviewdata = (JSONObject)data.get(i);
            reviewresponse = new MovieReviews();

            reviewresponse.setAuthor(reviewdata.getString(AUTHOR));
            reviewresponse.setContent(reviewdata.getString(CONTENT));
            parsedReview.add(reviewresponse);
        }
        return  parsedReview;
    }
}
