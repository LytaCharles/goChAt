package www.charles.gochat.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Getting the current user of the app
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //cheking if the user is not logged in so that we send them to the startPageActivity
        if (currentUser == null){

            Intent startPageIntent = new Intent(MainActivity.this, StartPageActivity.class);
            //setting a validation to enable the user not to go back to the MainActivity if they press the back button
            startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startPageIntent);

        }
    }
}
