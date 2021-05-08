package dk.au.mad21spring.appproject.group6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import dk.au.mad21spring.appproject.group6.fragments.ListFragment;
import dk.au.mad21spring.appproject.group6.fragments.RequestFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int AUTH_ACTIVITY = 1;

    TabLayout mainTabs;
    FirebaseAuth auth;
    Toolbar appbar;

    //Fragment Tags
    private static final String BEVERAGE_LIST_FRAG = "beverage_list_fragment";
    private static final String BEVERAGE_DETAILS_FRAG = "beverage_details_fragment";
    private static final String REQUESTS_FRAG = "requests_fragment";
    private static final String PROFILE_FRAG = "profile_fragment";

    //Fragments
    private ListFragment beverageListFragment;
    private RequestFragment requestFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        initializeFragments();
        setupUI();

        if (auth.getCurrentUser() == null) {
            goToSignIn();
        }

        // Code to get claims from current logged in user - This is only to test - Delete when no longer needed
        if (auth.getCurrentUser() != null) {
            auth.getCurrentUser().getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                @Override
                public void onSuccess(GetTokenResult result) {
                    boolean isAdmin = result.getClaims().containsKey("admin");
                    if (isAdmin) {
                        Log.d("CLAIM", "contains admin claim");
                    } else {
                        Log.d("CLAIM", "no admin claim found");
                    }

                    if(isAdmin){
                        boolean isAdmin2 = (boolean) result.getClaims().get("admin"); // This code crashes if no admin claim is on user - So do a check beforehand to see if containsKey("admin")
                        if (isAdmin) {
                            Log.d("CLAIM", "admin claim is true");
                        } else {
                            Log.d("CLAIM", "admin claim is false");
                        }
                    }
                }
            });
        }
    }

    private void goToSignIn() {
        Intent authIntent = new Intent(this, AuthActivity.class);
        startActivityForResult(authIntent, AUTH_ACTIVITY);
    }

    private void setupUI() {
        mainTabs = findViewById(R.id.mainTabs);
        appbar = findViewById(R.id.mainAppbar);
        setSupportActionBar(appbar);

        mainTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
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
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Go to original fragment for the selected tab
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appbarActionSignOut:
                auth.signOut();
                goToSignIn();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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

        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainFragmentContainter, beverageListFragment, BEVERAGE_LIST_FRAG)
                .add(R.id.mainFragmentContainter, requestFragment, REQUESTS_FRAG)
                .replace(R.id.mainFragmentContainter, beverageListFragment, BEVERAGE_LIST_FRAG)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTH_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                if(auth.getCurrentUser() != null){
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
