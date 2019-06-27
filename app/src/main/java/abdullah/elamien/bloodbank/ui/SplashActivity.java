package abdullah.elamien.bloodbank.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.R;
import abdullah.elamien.bloodbank.databinding.ActivitySplashBinding;
import abdullah.elamien.bloodbank.di.components.DaggerSplashComponent;
import abdullah.elamien.bloodbank.di.components.SplashComponent;
import abdullah.elamien.bloodbank.utils.Constants;
import abdullah.elamien.bloodbank.utils.PreferenceUtils;


public class SplashActivity extends AppCompatActivity {

    @Inject
    FirebaseAuth mAuth;
    @Inject
    FirebaseMessaging mFirebaseMessaging;
    @Inject
    PreferenceUtils mPreferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFullScreen();
        ActivitySplashBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initDagger();
        subscribeToNotification();
        if (isUserFirstLaunch()) {
            startWelcomeActivity();
        } else {
            startRegisterActivity();
        }
    }


    private void initDagger() {
        SplashComponent component = DaggerSplashComponent.builder()
                .appComponent(((App) getApplication()).getAppComponent())
                .build();
        component.inject(this);
    }

    private void subscribeToNotification() {
        if (mPreferenceUtils.isNotificationsEnabled(this)) {
            mFirebaseMessaging.subscribeToTopic(Constants.SHOUTOUTS_TOPIC_NAME);
        } else {
            mFirebaseMessaging.unsubscribeFromTopic(Constants.SHOUTOUTS_TOPIC_NAME);
        }
    }

    private void setupFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isUserFirstLaunch() {
        SharedPreferences preferences = getSharedPreferences(Constants.FIRST_LAUNCH_NAME, MODE_PRIVATE);
        return !preferences.getBoolean(Constants.FIRST_LAUNCH_KEY, false);
    }

    private void startWelcomeActivity() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
