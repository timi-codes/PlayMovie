package tarrotsystem.com.playmovie.utilities;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by DOTECH on 15/04/2017.
 */

public class NetworkUtils {

    private final static String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static String POSTER_BASE_URL = IMAGE_BASE_URL+"w185/";
    public static String BACKDROP_BASE_URL=IMAGE_BASE_URL+"w780/";
    private static String PARAM_QUERY = "api_key";


    /**
     * Builds the URL used to query TMDB.
     *
     * @param path The keyword that will be queried for.
     * @return The URL to use to query the TMDB.
     */
    public static URL buildUrl(String path, String apiKey) {
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendEncodedPath(path)
                .appendQueryParameter(PARAM_QUERY,apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        // set the connection timeout to 5 seconds and the read timeout to 10 seconds
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(10000);


        try {
            // get a stream to read data from
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
