package de.fhoeborn.android.sampleapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity implements ListFragment.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onComicSelected(int id) {
        DetailFragment fragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.frag_detail);

        if (fragment != null && fragment.isInLayout()) {
            fragment.setId(id);
        } else {
            Intent intent = new Intent();
            intent.setClass(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_ID, id);
            startActivity(intent);
        }
    }
}
