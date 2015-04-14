package de.fhoeborn.android.sampleapplication;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.squareup.otto.Subscribe;

import de.fhoeborn.android.sampleapplication.model.ComicId;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        BaseApp.bus.register(this);
    }

    @Override
    protected void onStop() {
        BaseApp.bus.unregister(this);

        super.onStop();
    }

    @Subscribe
    public void onComicSelected(ComicId id) {
        if (!isFragmentInThisActivity(getFragmentManager().findFragmentById(R.id.frag_detail))) {
            Intent intent = new Intent();
            intent.setClass(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_ID, id.getId());
            startActivity(intent);
        }
    }

    private boolean isFragmentInThisActivity(Fragment fragment) {
        return fragment != null && fragment.isInLayout();
    }


}
