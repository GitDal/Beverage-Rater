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

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import dk.au.mad21spring.appproject.group6.constants.InstanceStateExtras;
import dk.au.mad21spring.appproject.group6.fragments.ListFragment;
import dk.au.mad21spring.appproject.group6.fragments.request.RequestFragment;
import dk.au.mad21spring.appproject.group6.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int AUTH_ACTIVITY = 1001;

    //Fragment Tags
    private static final String BEVERAGE_LIST_FRAG = "beverage_list_fragment";
    private static final String BEVERAGE_DETAILS_FRAG = "beverage_details_fragment";
    private static final String REQUESTS_FRAG = "requests_fragment";
    private static final String PROFILE_FRAG = "profile_fragment";

    //Fragments
    private ListFragment beverageListFragment;
    private RequestFragment requestFragment;

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
        Intent authIntent = new Intent(this, AuthActivity.class);
        startActivityForResult(authIntent, AUTH_ACTIVITY);
    }

    private void initializeFragments() {
        beverageListFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(BEVERAGE_LIST_FRAG);
        if (beverageListFragment == null) {
            beverageListFragment = ListFragment.newInstance();
        }
        requestFragment = (RequestFragment) getSupportFragmentManager().findFragmentByTag(REQUESTS_FRAG);
        if (requestFragment == null) {
            requestFragment = RequestFragment.newInstance();
        }
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
                        .replace(R.id.mainFragmentContainter, beverageListFragment, BEVERAGE_LIST_FRAG)
                        .commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFragmentContainter, requestFragment, REQUESTS_FRAG)
                        .commit();
                break;
            case 2:
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
                    invalidateOptionsMenu();                        //To update action-bar with new username (onPrepareOptionsMenu gets called again)
                    mainTabs.selectTab(mainTabs.getTabAt(0)); // Update tab to display default tab
                    handleTabPosition(0);
                    vm.UpdateCurrentUser();
                    return;
                }
            }
        }

        // Force user to log in or sign up to use app
        if (auth.getCurrentUser() == null) {
            goToSignIn();
        }
    }
}
