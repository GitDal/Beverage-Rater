package dk.au.mad21spring.appproject.group6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    TabLayout mainTabs;

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

        initializeFragments();
        setupUI();
    }

    private void setupUI() {
        mainTabs = findViewById(R.id.main_tabs);

        mainTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment_containter, beverageListFragment, BEVERAGE_LIST_FRAG)
                                .commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment_containter, requestFragment, REQUESTS_FRAG)
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

    private void initializeFragments() {
        beverageListFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(BEVERAGE_LIST_FRAG);
        if(beverageListFragment == null){
            beverageListFragment = ListFragment.newInstance("", "");
        }
        requestFragment = (RequestFragment) getSupportFragmentManager().findFragmentByTag(REQUESTS_FRAG);
        if(requestFragment==null){
            requestFragment = RequestFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_fragment_containter, beverageListFragment, BEVERAGE_LIST_FRAG)
                .add(R.id.main_fragment_containter, requestFragment, REQUESTS_FRAG)
                .replace(R.id.main_fragment_containter, beverageListFragment, BEVERAGE_LIST_FRAG)
                .commit();
    }
}