package com.example.martin.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
    private static final String MOVIES_FRAGMENT_TAG = "MF_TAG";
    private String mSortOrder; // Used to store the current sort order

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize mSortOrder to whatever is currently stored in settings
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSortOrder = preferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popularity));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MoviesFragment(), MOVIES_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get the current sort order
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currSortOrder = preferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popularity));

        // Check whether the sort order has changed
        if (!mSortOrder.equals(currSortOrder)) {
            MoviesFragment moviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentByTag(MOVIES_FRAGMENT_TAG);
            mSortOrder = currSortOrder; // Update mSortOrder
            moviesFragment.onSortOrderChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
