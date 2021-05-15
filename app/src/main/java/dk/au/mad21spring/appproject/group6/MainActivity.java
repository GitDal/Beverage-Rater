package dk.au.mad21spring.appproject.group6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import dk.au.mad21spring.appproject.group6.constants.InstanceStateExtras;
import dk.au.mad21spring.appproject.group6.fragments.ProfileFragment;
import dk.au.mad21spring.appproject.group6.fragments.wrapper.WrapperFragment;
import dk.au.mad21spring.appproject.group6.fragments.request.RequestFragment;
import dk.au.mad21spring.appproject.group6.services.NotificationService;
import dk.au.mad21spring.appproject.group6.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int AUTH_ACTIVITY = 1001;

    //Fragment Tags
    private static final String WRAPPER_FRAG = "wrapper_fragment";
    private static final String REQUESTS_FRAG = "requests_fragment";
    private static final String PROFILE_FRAG = "profile_fragment";

    //Fragment
    private WrapperFragment wrapperFragment;
    private RequestFragment requestFragment;
    private ProfileFragment profileFragment;

    //State
    MainActivityViewModel vm;

    //UI
    TabLayout mainTabs;
    FirebaseAuth auth;
    Toolbar appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vm = new ViewModelProvider(this).get(MainActivityViewModel.class);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            goToSignIn();
        } else {
            vm.UpdateCurrentUser();
            startNotificationService();
        }

        initializeFragments();
        setupUI();

        if(savedInstanceState != null) {
            int tabPosition = savedInstanceState.getInt(InstanceStateExtras.TAB_POSITION, 0);
            Log.d(TAG, "onCreate: Received savedInstanceState: tabPosition = " + tabPosition);
            mainTabs.selectTab(mainTabs.getTabAt(tabPosition));
        }
        handleTabPosition(mainTabs.getSelectedTabPosition());
    }

    private void goToSignIn() {
        stopNotificationService();
        Intent authIntent = new Intent(this, AuthActivity.class);
        startActivityForResult(authIntent, AUTH_ACTIVITY);
    }

    private void initializeFragments() {
        wrapperFragment = (WrapperFragment) getSupportFragmentManager().findFragmentByTag(WRAPPER_FRAG);
        if (wrapperFragment == null) {
            wrapperFragment = WrapperFragment.newInstance();
        }

        requestFragment = (RequestFragment) getSupportFragmentManager().findFragmentByTag(REQUESTS_FRAG);
        if (requestFragment == null) {
            requestFragment = RequestFragment.newInstance();
        }

        profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag(PROFILE_FRAG);
        if (profileFragment == null) {
            profileFragment = ProfileFragment.newInstance();
        }
    }

    private void reInitializeFragments() {
        wrapperFragment = WrapperFragment.newInstance();
        requestFragment = RequestFragment.newInstance();
        profileFragment = ProfileFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragmentContainter, wrapperFragment, WRAPPER_FRAG)
                .commit();
    }

    private void setupUI() {
        mainTabs = findViewById(R.id.mainTabs);
        appbar = findViewById(R.id.mainAppbar);
        setSupportActionBar(appbar);

        mainTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                handleTabPosition(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Go to original fragment for the selected tab
            }
        });
    }

    private void handleTabPosition(int position) {
        switch (position) {
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFragmentContainter, wrapperFragment, WRAPPER_FRAG)
                        .commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFragmentContainter, requestFragment, REQUESTS_FRAG)
                        .commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFragmentContainter, profileFragment, PROFILE_FRAG)
                        .commit();
                break;
            default:
                Log.d(TAG, "Unhandled switch case");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem userNameItem = menu.findItem(R.id.appBarUserNameText);

        if(auth.getCurrentUser() != null) {
            String username = auth.getCurrentUser().getEmail();
            TextView usernameTextView = (TextView) userNameItem.getActionView();

            usernameTextView.setText(username);
            usernameTextView.setTextColor(Color.BLACK);
            usernameTextView.setTypeface(null, Typeface.BOLD);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appbarActionSignOut:
                vm.RemoveCurrentUser();
                auth.signOut();
                goToSignIn();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int currentTabPosition = mainTabs.getSelectedTabPosition();
        Log.d(TAG, "onSaveInstanceState: Saving tabPosition = " + currentTabPosition);
        outState.putInt(InstanceStateExtras.TAB_POSITION, currentTabPosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTH_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                if(auth.getCurrentUser() != null){
                    vm.UpdateCurrentUser();                         //resolve whether or not user is admin
                    invalidateOptionsMenu();                        //To update action-bar with new username (onPrepareOptionsMenu gets called again)
                    reInitializeFragments();
                    mainTabs.selectTab(mainTabs.getTabAt(0)); // Update tab to display default tab
                    startNotificationService();                     // Start notification service
                    return;
                }
            }
        }

        // Force user to log in or sign up to use app
        if (auth.getCurrentUser() == null) {
            goToSignIn();
        }
    }

    @Override
    public void onBackPressed() {
        if(!wrapperFragment.handlesOnBackPressed()){
            super.onBackPressed();
        }
    }

    private void startNotificationService() {
        Log.d(TAG, "startNotificationService");
        Intent startServiceIntent = new Intent(this, NotificationService.class);
        startService(startServiceIntent);
    }

    private void stopNotificationService() {
        Log.d(TAG, "stopNotificationService");
        Intent startServiceIntent = new Intent(this, NotificationService.class);
        stopService(startServiceIntent);
    }
}
