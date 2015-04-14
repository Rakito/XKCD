package de.fhoeborn.android.sampleapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class DetailActivity extends ActionBarActivity {
    public static final String EXTRA_ID = "EXTRA_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt(EXTRA_ID);

        Fragment fragment = getFragmentManager().findFragmentById(R.id.frag_detail);

        if (!(fragment instanceof DetailFragment)) {
            throw new IllegalStateException("Wrong fragment attached!");
        }

        ((DetailFragment) fragment).setId(id);
    }
}
