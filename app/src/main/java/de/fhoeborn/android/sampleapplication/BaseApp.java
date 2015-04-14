package de.fhoeborn.android.sampleapplication;


import android.app.Application;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BaseApp extends Application {
    public static Bus bus = new Bus(ThreadEnforcer.MAIN);
}
