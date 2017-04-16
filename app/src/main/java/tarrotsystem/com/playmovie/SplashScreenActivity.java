package tarrotsystem.com.playmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by DOTECH on 14/04/2017.
 */

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Starts MainActivity after app is ready
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
