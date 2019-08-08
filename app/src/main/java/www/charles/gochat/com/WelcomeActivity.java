package www.charles.gochat.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        Thread thread = new Thread(){

            @Override
            public void run() {
                try {
                    sleep(6000);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
                    Intent mainActivityIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                    //setting a validation to enable the user not to go back to the WelcomeActivity if they press the back button
                    mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainActivityIntent);
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
