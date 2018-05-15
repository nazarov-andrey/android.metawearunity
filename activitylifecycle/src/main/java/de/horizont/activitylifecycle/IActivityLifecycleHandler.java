package de.horizont.activitylifecycle;

import android.os.Bundle;

public interface IActivityLifecycleHandler
{
    void onCreate(Bundle savedInstanceState);
    void onStart();
    void onRestart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
}
