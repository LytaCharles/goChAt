package www.charles.gochat.com;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
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
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PIC_REQUEST = 2;
    private StorageReference storeUserProfileImageRef;
    private FirebaseStorage firebaseStorage;
    private Uri imageUri;
    private String  image;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);

        loadingBar = new ProgressDialog(this);

        /**
         * creating a reference to the firebase auth
         * this will enable us to get the unique id that was given to the user during authentication
         * the key is important in retrieving back the user data
         * we get the key by creating a string type varriable and attach it to the child
         */
        mAuth = FirebaseAuth.getInstance();
        String onlineCurrentUser = mAuth.getCurrentUser().getUid();
        firebaseStorage = FirebaseStorage.getInstance();

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
                 *
                 */


                /**
                 * Setting the data in the
                 *  String image = dataSnapshot.child("user_image").getValue().toString();
                 *                 Picasso.get().load(image).into(sttngsDisplayUserProfileImage);
                 */



            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
        getUserDataReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded( DataSnapshot dataSnapshot,  String image)
            {



            }

            @Override
            public void onChildChanged( DataSnapshot dataSnapshot,  String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
         //Enabling the user ta take a photo using the camera
         sttngsFavCamera.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 pictureTakerAction();

             }
         });

    }

    private void pictureTakerAction() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, TAKE_PIC_REQUEST);
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
                    imageUri = data.getData();
                     uploadUserProfile();

                }
                else {
                    if (resultCode == RESULT_CANCELED)
                    {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        }

        //SETTING UP THE CAMERA
        if (requestCode == TAKE_PIC_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                if (data != null)
                {
                    Bitmap capturedImage = (Bitmap)data.getExtras().get("data");
                    sttngsDisplayUserProfileImage.setImageBitmap(capturedImage);

                }
                else if (resultCode == RESULT_CANCELED)
                {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void uploadUserProfile() {

        if (imageUri != null)
        {
            loadingBar.setTitle("Uploading Image");
            loadingBar.show();
            storeUserProfileImageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {

                                    loadingBar.setProgress(0);

                                }
                            }, 5000);
                            Toast.makeText(UserProfileSettingsActivity.this, "Image uploaded successfully",
                                    Toast.LENGTH_SHORT).show();
                            Picasso.get().load(image).into(sttngsDisplayUserProfileImage);


                            loadingBar.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            loadingBar.dismiss();
                            Toast.makeText(UserProfileSettingsActivity.this, "Failed to upload image" + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                        {

                            double uploadingUserImage = (100.0 *  taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            loadingBar.setMessage("Uploaded" + (int) uploadingUserImage +  "%");

                        }
                    })
                     .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                         @Override
                         public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                             if ( task.isSuccessful())
                             {
                                String downloadUrl;
                                downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
                                getUserDataReference.child("user_image").setValue(downloadUrl);

                             }
                         }
                     });

        }

    }
}
