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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerUserName;
    private EditText registeruserEmailAddress;
    private EditText registerUserPassword;
    private Button bt_createAccount;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference storeUserDefaultDataReference;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
              final   String userName = registerUserName.getText().toString();
                String userEmail = registeruserEmailAddress.getText().toString();
                String userPassword = registerUserPassword.getText().toString();

                RegisterAccount(userName, userEmail, userPassword);

            }
        });
    }

    private void RegisterAccount(final String userName, String userEmail, String userPassword) {

        /**checking if the user have left any field blank
         * in the edit text
         * while submitting details
         * to create the account
         */

        if (TextUtils.isEmpty(userName))
        {
            Toast.makeText(RegisterActivity.this, "Field 'Name' cannot be empty",
                           Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(userEmail))
        {
            Toast.makeText(RegisterActivity.this, "Field 'Email' cannot be empty",
                           Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(userPassword))
        {
            Toast.makeText(RegisterActivity.this, "Field 'Password' cannot be empty",
                           Toast.LENGTH_LONG).show();
        }
        else

        {//This progressDialog communicates to the user when the account is being created
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait...");
            loadingBar.show();
            //creating the user account with email and password
            mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            //checking if the user has been succcesfully registered before allowing them to access the main activity
                            if (task.isSuccessful())
                            {
                                /**if the user has entered the details correctly then we need to store the user data inside our database first
                                 * Creating an a reference to the Firebase database
                                 * then create a child name to our database reference
                                 */
                                //getting the unique user id
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference()
                                                                                 .child("goChat_Users")
                                                                                 .child(currentUserId);
                                //Storing the user data in our database
                                storeUserDefaultDataReference.child("user_name").setValue(userName);
                                storeUserDefaultDataReference.child("user_status").setValue("Hey there, I'm using goChAt.Let's connect!");
                                storeUserDefaultDataReference.child("user_image").setValue("default_profile_image");
                                storeUserDefaultDataReference.child("user_thumb_image").setValue("default_thumb_image")
                                                             .addOnCompleteListener(new OnCompleteListener<Void>()
                                                             {
                                                                 @Override
                                                                 public void onComplete(@NonNull Task<Void> task)
                                                                 {
                                                                     /**
                                                                      * checking if the account has been created sucessfully
                                                                      * Display a toast to the user of the app to show that the account has been created
                                                                      * Then send the user to the MainActivity
                                                                      */

                                                                     if (task.isSuccessful())
                                                                     {
                                                                         Toast.makeText(RegisterActivity.this, "Account Created Successfully",
                                                                                 Toast.LENGTH_SHORT).show();

                                                                         Intent mainActivityIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                                                         //Setting a validation to ensure that the user does not go back to the Registration Activity
                                                                         mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                         startActivity(mainActivityIntent);

                                                                     }

                                                                 }
                                                             });

                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this, "Unknown error occured.Please try again",
                                               Toast.LENGTH_SHORT).show();
                            }
                            loadingBar.dismiss();

                        }
                    });
        }
    }
}
