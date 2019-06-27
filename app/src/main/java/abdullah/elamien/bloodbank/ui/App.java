package abdullah.elamien.bloodbank.ui;

import android.app.Application;

import abdullah.elamien.bloodbank.di.components.AppComponent;
import abdullah.elamien.bloodbank.di.components.DaggerAppComponent;
import abdullah.elamien.bloodbank.di.modules.PreferenceUtilsModule;

/**
 * Created by AbdullahAtta on 6/19/2019.
 */
public class App extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder().preferenceUtilsModule(new PreferenceUtilsModule(getApplicationContext())).build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
