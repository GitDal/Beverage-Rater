package dk.au.mad21spring.appproject.group6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    TextView helloTest;
    TabLayout mainTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloTest = findViewById(R.id.main_hello_test);
        mainTabs = findViewById(R.id.main_tabs);

        mainTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        helloTest.setText("Hello List");
                        break;
                    case 1:
                        helloTest.setText("Hello Request");
                        break;
                    case 2:
                        helloTest.setText("Hello Profile");
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
}