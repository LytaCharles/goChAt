package www.charles.gochat.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerUserName;
    private EditText registeruserEmailAddress;
    private EditText registerUserPassword;
    private Button bt_createAccount;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        registeruserEmailAddress = (EditText) findViewById(R.id.et_registerenterEmail);
        registerUserName = (EditText) findViewById(R.id.et_registeruserName);
        registerUserPassword = (EditText) findViewById(R.id.et_registerpassword);
        bt_createAccount = (Button) findViewById(R.id.btn_createAccount);

        bt_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting the user registration details
                String userName = registerUserName.getText().toString();
                String userEmail = registeruserEmailAddress.getText().toString();
                String userPassword = registerUserPassword.getText().toString();

                RegisterAccount(userName, userEmail, userPassword);

            }
        });
    }

    private void RegisterAccount(String userName, String userEmail, String userPassword) {

        /**checking if the user have left any field blank
         * in the edit text
         * while submitting details
         * to create the account
         */

        if (TextUtils.isEmpty(userName))
        {
            Toast.makeText(RegisterActivity.this, "Field 'Name' cannot be empty", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(userEmail))
        {
            Toast.makeText(RegisterActivity.this, "Field 'Email' cannot be empty", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(userPassword))
        {
            Toast.makeText(RegisterActivity.this, "Field 'Password' cannot be empty", Toast.LENGTH_LONG).show();
        }
        else

        {
            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("Please wait...");
            loadingBar.show();
            //creating the user account with email and password
            mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            //checking if the user has been succcesfully registered before allowing them to access the main activity
                            if (task.isSuccessful())
                            {
                                Intent mainActivityIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainActivityIntent);
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this, "Unknown error occured.Please try again", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }
}
