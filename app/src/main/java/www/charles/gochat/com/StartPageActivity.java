 package www.charles.gochat.com;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class StartPageActivity extends AppCompatActivity {

    private Button btn_AlreadyHaveAccount;
    private Button btn_CreateNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        btn_AlreadyHaveAccount = (Button)findViewById(R.id.already_have_account_bt);
        btn_AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivityIntent = new Intent(StartPageActivity.this, LoginActivity.class);
                startActivity(loginActivityIntent);
            }
        });
        btn_CreateNewAccount = (Button)findViewById(R.id.Create_account_bt);
        btn_CreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivityIntent = new Intent(StartPageActivity.this, RegisterActivity.class);
                startActivity(registerActivityIntent);
            }
        });
    }
}
