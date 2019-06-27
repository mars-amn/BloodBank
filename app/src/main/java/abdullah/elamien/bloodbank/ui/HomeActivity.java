package abdullah.elamien.bloodbank.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nightonke.boommenu.BoomButtons.HamButton;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.R;
import abdullah.elamien.bloodbank.adapters.ShoutoutAdapter;
import abdullah.elamien.bloodbank.databinding.ActivityHomeBinding;
import abdullah.elamien.bloodbank.di.components.DaggerHomeComponent;
import abdullah.elamien.bloodbank.di.components.HomeComponent;
import abdullah.elamien.bloodbank.di.modules.ShoutoutAdapterModule;
import abdullah.elamien.bloodbank.utils.ConfigHelper;
import abdullah.elamien.bloodbank.utils.PreferenceUtils;
import abdullah.elamien.bloodbank.viewmodels.ShoutoutsViewModel;
import abdullah.elamien.bloodbank.viewmodels.factory.ViewModelFactory;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Inject
    ViewModelFactory mModelFactory;
    @Inject
    ShoutoutAdapter mShoutoutsAdapter;
    @Inject
    FirebaseUser mUser;
    @Inject
    FirebaseAuth mAuth;
    ActivityHomeBinding mHomeBinding;
    private float lastTranslate = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguageFromPreference();
        mHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initDagger();
        setupViews();
        setupShoutoutsRecyclerView();
        buildShoutoutBoomMenu();
        buildSearchBoomMenu();
        buildCreateDonorApplicationBoomMenu();
        accessHeaderView();
        registerSharedPreferenceListener();
    }

    private void initDagger() {
        HomeComponent mComponent = DaggerHomeComponent.builder()
                .shoutoutAdapterModule(new ShoutoutAdapterModule(this))
                .appComponent(((App) getApplication()).getAppComponent())
                .build();
        mComponent.injectHomeActivity(this);
    }

    private void setLanguageFromPreference() {
        ConfigHelper.setLanguage(getResources(), PreferenceUtils.getLanguageCode(this));
    }

    private void setupShoutoutsRecyclerView() {
        hideRecyclerView();
        ShoutoutsViewModel mShoutoutsViewModel = ViewModelProviders.of(this, mModelFactory).get(ShoutoutsViewModel.class);
        mShoutoutsViewModel.getShoutouts().observe(this, shoutouts -> {
            mHomeBinding.shoutoutsRecyclerView.setAdapter(mShoutoutsAdapter);
            mShoutoutsAdapter.addShoutouts(shoutouts);
            showRecyclerView();
        });
    }

    private void accessHeaderView() {
        View headerView = mHomeBinding.navigationView.getHeaderView(0);
        CircleImageView userImage = headerView.findViewById(R.id.profileHeaderImage);
        TextView userName = headerView.findViewById(R.id.profileHeaderUserName);
        TextView userEmail = headerView.findViewById(R.id.profileHeaderUserEmail);
        Glide.with(this).load(mUser.getPhotoUrl()).into(userImage);
        userName.setText(mUser.getDisplayName());
        userEmail.setText(mUser.getEmail());

    }

    private void setupViews() {
        ActionBarDrawerToggle toggle = getActionBarDrawerToggle();
        mHomeBinding.drawerLayout.addDrawerListener(toggle);
        mHomeBinding.navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
        mHomeBinding.toolbar.setNavigationIcon(R.drawable.ic_nav_icon);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
    }

    private ActionBarDrawerToggle getActionBarDrawerToggle() {
        mHomeBinding.navigationView.setItemIconTintList(null);
        setSupportActionBar(mHomeBinding.toolbar);
        return new ActionBarDrawerToggle(this, mHomeBinding.drawerLayout,
                mHomeBinding.toolbar,
                R.string.navigation_open,
                R.string.navigation_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                if (PreferenceUtils.getLanguageCode(HomeActivity.this).toLowerCase()
                        .equals(getString(R.string.arabic_language_value).toLowerCase())) {
                    slideOffset *= -1;
                }

                float moveFactor = (mHomeBinding.drawerLayout.getWidth() * slideOffset);
                TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
                anim.setDuration(0);
                anim.setFillAfter(true);
                mHomeBinding.holder.startAnimation(anim);
                lastTranslate = moveFactor;
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (mHomeBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void buildCreateDonorApplicationBoomMenu() {
        HamButton.Builder builder = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_blood_donation)
                .normalTextRes(R.string.become_a_donor_button_label)
                .normalColorRes(R.color.secondaryColor)
                .subNormalTextRes(R.string.join_donors_sub_text)
                .listener(index -> startDonateActivity());
        mHomeBinding.bmb.addBuilder(builder);
    }

    private void buildSearchBoomMenu() {
        HamButton.Builder builder = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_search)
                .normalTextRes(R.string.search_donors_label)
                .normalColorRes(R.color.secondaryColor)
                .subNormalTextRes(R.string.search_donors_sub_text)
                .listener(index -> startSearchActivity());
        mHomeBinding.bmb.addBuilder(builder);
    }

    private void buildShoutoutBoomMenu() {
        HamButton.Builder builder = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_shoutout)
                .normalTextRes(R.string.create_shoutout_label)
                .normalColorRes(R.color.secondaryColor)
                .subNormalTextRes(R.string.shoutout_sub_text)
                .listener(index -> startShoutoutCreationActivity());
        mHomeBinding.bmb.addBuilder(builder);
    }

    private void startShoutoutCreationActivity() {
        Intent intent = new Intent(this, ShoutoutCreationActivity.class);
        startActivity(intent);
    }

    private void startSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }


    private void startDonateActivity() {
        Intent intent = new Intent(this, DonateActivity.class);
        startActivity(intent);
    }

    private void hideRecyclerView() {
        mHomeBinding.shoutoutsRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showRecyclerView() {
        mHomeBinding.shoutoutsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.createShoutoutMenuItem:
                startShoutoutCreationActivity();
                return true;
            case R.id.becomeADonorMenuItem:
                startDonateActivity();
                return true;
            case R.id.searchForDonorsMenuItem:
                startSearchActivity();
                return true;
            case R.id.settingsMenuItem:
                startSettingsActivity();
                return true;
            case R.id.aboutMenuItem:
                startAboutActivity();
                return true;
            case R.id.logoutMenuItem:
                logoutUser();
                return true;
            default:
                return false;
        }
    }

    private void logoutUser() {
        mAuth.signOut();
        startLoginActivity();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.language_key))) {
            String language = sharedPreferences.getString(key, getString(R.string.arabic_language_value));
            ConfigHelper.setLanguage(getResources(), language);
            recreate();
        }
    }

    private void registerSharedPreferenceListener() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterSharedPreferenceListener();
    }

    private void unregisterSharedPreferenceListener() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
