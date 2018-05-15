package de.horizont.activitylifecycle;

import android.os.Bundle;

import java.util.ArrayList;

public class UnityPlayerActivity extends com.unity3d.player.UnityPlayerActivity
{
    protected ArrayList<IActivityLifecycleHandler> lifecycleHandlers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (IActivityLifecycleHandler listener : lifecycleHandlers) {
            listener.onCreate(savedInstanceState);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        for (IActivityLifecycleHandler listener : lifecycleHandlers) {
            listener.onStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        for (IActivityLifecycleHandler listener : lifecycleHandlers) {
            listener.onRestart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (IActivityLifecycleHandler listener : lifecycleHandlers) {
            listener.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (IActivityLifecycleHandler listener : lifecycleHandlers) {
            listener.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (IActivityLifecycleHandler listener : lifecycleHandlers) {
            listener.onStop();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        for (IActivityLifecycleHandler listener : lifecycleHandlers) {
            listener.onDestroy();
        }
    }
}
