package de.fhoeborn.android.sampleapplication;


import android.app.Fragment;

public class OttoFragment extends Fragment {
    @Override
    public void onStart() {
        super.onStart();

        BaseApp.bus.register(this);
    }

    @Override
    public void onStop() {
        BaseApp.bus.unregister(this);

        super.onStop();
    }
}
