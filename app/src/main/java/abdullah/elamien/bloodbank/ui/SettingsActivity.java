package abdullah.elamien.bloodbank.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.preference.PreferenceManager;

import com.google.firebase.messaging.FirebaseMessaging;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.R;
import abdullah.elamien.bloodbank.databinding.ActivitySettingsBinding;
import abdullah.elamien.bloodbank.di.components.ActivityComponent;
import abdullah.elamien.bloodbank.di.components.DaggerActivityComponent;
import abdullah.elamien.bloodbank.utils.ConfigHelper;
import abdullah.elamien.bloodbank.utils.Constants;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    FirebaseMessaging mFirebaseMessaging;
    private ActivitySettingsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        initDagger();
        registerPreferenceListener();
        setupToolbar();
    }

    private void initDagger() {
        ActivityComponent component = DaggerActivityComponent.builder()
                .appComponent(((App) getApplication()).getAppComponent())
                .build();
        component.inject(this);
    }

    private void setupToolbar() {
        setSupportActionBar(mBinding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(getString(R.string.settings_action_bar_label));
        }
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_nav_back);
    }

    private void registerPreferenceListener() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.notification_key))) {
            boolean isNotificationSelected = sharedPreferences.getBoolean(key, true);
            if (isNotificationSelected) {
                mFirebaseMessaging.subscribeToTopic(Constants.SHOUTOUTS_TOPIC_NAME);
            } else {
                mFirebaseMessaging.unsubscribeFromTopic(Constants.SHOUTOUTS_TOPIC_NAME);
            }
        } else if (key.equals(getString(R.string.language_key))) {
            String language = sharedPreferences.getString(key, getString(R.string.arabic_language_value));
            ConfigHelper.setLanguage(getResources(), language);
            recreate();

        }
    }
}
