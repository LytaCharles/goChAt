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

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private EditText userLoginEmail;
    private EditText userLoginPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        userLoginEmail = (EditText)findViewById(R.id.et_userLoginEmail);
        userLoginPassword = (EditText)findViewById(R.id.et_userLoginPassword);
        btn_login = (Button)findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /**
                 * Extracting the user input and doing the verrification
                 * before  giving  them access to the main activity
                 */
                String email = userLoginEmail.getText().toString();
                String password = userLoginPassword.getText().toString();

                //This method will veryfy the user input
                LoginUserAccount(email, password);
            }
        });
    }

    private void LoginUserAccount(String email, String password)
    {
        /**
         * Checking the validations to ensure that the user did not leave any space blank.
         * If the user left any edit text blank (email or password)
         * then the toast will send them back a message
         */
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter email address",
                           Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter password",
                    Toast.LENGTH_SHORT).show();
        }
        /**
         * if the user has entered the details then we'll proceed to the verification
         */
        else {
            loadingBar.setTitle("Loging in");
            loadingBar.setMessage("Please wait...");
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {//checking if the login task is successfull
                            if (task.isSuccessful())
                            {
                                Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainActivityIntent);

                            }//IF the login is not successfull we send back a toast to the user
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Incorrect email or password",
                                                Toast.LENGTH_SHORT).show();
                            }
                            loadingBar.dismiss();

                        }
                    });
        }

    }
}
