package www.charles.gochat.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mainToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsPagerAdapter myTabsPagerAdapter;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingBar = new ProgressDialog(this);
        /**setting up the toolbar
         * Then setting the toolbar to the
         * actionbar
         */
        mainToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("goChAt");

        //Tabs for mainActivity
        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        myTabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        /**
         * Setting an adapter for the view pager
         */
        myViewPager.setAdapter(myTabsPagerAdapter);
        /**
         * Setting the tablayout with the viewpager
         */
        myTabLayout = (TabLayout) findViewById(R.id.mainTabs);
        myTabLayout.setupWithViewPager(myViewPager);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        //Getting the current user of the app
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //cheking if the user is not logged in so that we send them to the startPageActivity
        if (currentUser == null)
        {
            /**If the user is not logged in then we'll send them to the log out
             * this method does not allow users who are not logged in to access the MainActivity
             */
            LogoutUser();

        }
    }

    private void LogoutUser()
    {
        Intent startPageIntent = new Intent(MainActivity.this, StartPageActivity.class);
        //setting a validation to enable the user not to go back to the MainActivity if they press the back button
        startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startPageIntent);
    }

    //This method is for setting the main_menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    //This method will get the items in the main_menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
       super.onOptionsItemSelected(item);
       /*
       *checking if the user has clicked the logout button
       * if the user wants to log out then
       * we log the out from firebase authentication
       * then send them back to the StartPageActivity.
       * The user can log in again
       * or a new user can register
        */
       if (item.getItemId() == R.id.main_logout_btn)
       { //Logging out the user
           loadingBar.setTitle("Loging out");
           loadingBar.setMessage("Please wait...");
           loadingBar.show();
           mAuth.signOut();
           //sending the user back to the StartPageActivity
           LogoutUser();
       }
       loadingBar.dismiss();

       if (item.getItemId() == R.id.main_account_settings_btn)
       {
           Intent profileSettingsIntent = new Intent(MainActivity.this, UserProfileSettingsActivity.class);
           startActivity(profileSettingsIntent);
       }

       return true;


    }
}
