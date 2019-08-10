package www.charles.gochat.com;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfileSettingsActivity extends AppCompatActivity {
    private CircleImageView sttngsDisplayUserProfileImage;
    private TextView sttngsDisplayUserName;
    private TextView sttngsDisplayUserStatus;
    private Button sttngsBtnChangeUserProfile;
    private Button sttngsBtnUpdateUserStatus;
    private FloatingActionButton sttngsFavCamera;

    private DatabaseReference getUserDataReference;

    private FirebaseAuth mAuth;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storeUserProfileImageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);

        /**
         * creating a reference to the firebase auth
         * this will enable us to get the unique id that was given to the user during authentication
         * the key is important in retrieving back the user data
         * we get the key by creating a string type varriable and attach it to the child
         */
        mAuth = FirebaseAuth.getInstance();
        String onlineCurrentUser = mAuth.getCurrentUser().getUid();

        /**
         * Creating a reference to the database
         * this refernce is for retrieving back the user data
         * add the child here to get the user id
         */
        getUserDataReference = FirebaseDatabase.getInstance().getReference().child("goChat_Users")
                                               .child(onlineCurrentUser);
        /**
         * Creating a reference to the database storage for the user profile image
         * also create a folder to store the images
         */
        storeUserProfileImageRef = FirebaseStorage.getInstance().getReference().child("profile_images");

        sttngsDisplayUserProfileImage = (CircleImageView)findViewById(R.id.settingsUserProfileImage_id);
        sttngsDisplayUserName = (TextView)findViewById(R.id.settingsUserName);
       sttngsDisplayUserStatus = (TextView)findViewById(R.id.settingsUserStatusDisplay);
       sttngsBtnChangeUserProfile = (Button)findViewById(R.id.btn_settingsChangeProfile);
       sttngsBtnUpdateUserStatus = (Button)findViewById(R.id.btn_settingsUpdateStatus);
       sttngsFavCamera = (FloatingActionButton)findViewById(R.id.settingsFavCamera);

        /**
         * Retriving the user data from the database by using the refernce 'getUserDataReference'
         */
        getUserDataReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                /**
                 * getting the data using the dataSnapshot oblect
                 * this object allow us to get all the user data from the database
                 * String username = dataSnapshot.child("user_name").getValue().toString();
                 *                 String userStatus = dataSnapshot.child("user_status").getValue().toString();
                 */


                /**
                 * Setting the data in the app
                 */

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
        /**
         * This code is for enabling the user to select a photo from the gallery
         * then set the photo as a profile photo
         */
         sttngsBtnChangeUserProfile.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View v)
             {
                 Intent galaryIntent = new Intent();
                 galaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                 galaryIntent.setType("image/*");
                 startActivityForResult(galaryIntent, PICK_IMAGE_REQUEST);

             }
         });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                if (data!=null)
                {
                    Uri imageUri = data.getData();
                    /**
                     * Setting the crop feature to the image
                     */
                    CropImage.activity()
                             .setGuidelines(CropImageView.Guidelines.ON)
                             .setAspectRatio(1,1)
                             .start(this);

                }
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
                {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK)
                    {
                        Uri resultUri = result.getUri();
                        /**
                         * Creating a reference to enble the user to send the image to the database
                         * if the image has been cropped successfully, it is stored in the resultUri varriable
                         * give the photo a storage name using the unique user id
                         */
                        String userId = mAuth.getCurrentUser().getUid();
                        StorageReference filePath = storeUserProfileImageRef.child(userId + "jpn");
                        //putting the croped photo into our firebase dtabase
                        filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                            {//Checking if the thoto has been successfully sent to the firebase database
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(UserProfileSettingsActivity.this, "Uploading profile to the server...", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(UserProfileSettingsActivity.this, "Error occurred. Please try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
                    {
                        Exception errors = result.getError();
                    }
                }
            }
        }
    }
}
