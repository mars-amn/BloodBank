package abdullah.elamien.bloodbank.ui;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import abdullah.elamien.bloodbank.R;
import abdullah.elamien.bloodbank.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {
    private static final String FACEBOOK_PAGE_URL = "https://www.facebook.com/";
    private ActivityAboutBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        mBinding.setHandlers(this);
        setupToolbar();
    }

    private void setupToolbar() {
        setSupportActionBar(mBinding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(getString(R.string.about_activity_label));
        }
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_nav_back);
    }

    public void onFacebookButtonClick(View view) {
        openFacebookPage();
    }

    private void openFacebookPage() {
        Intent intent = getFacebookIntent();
        startActivity(intent);
    }

    private Intent getFacebookIntent() {
        try {
            if (getFacebookApplicationInfo().enabled) {
                Uri facebookUri = Uri.parse("fb://facewebmodal/f?href=" + FACEBOOK_PAGE_URL);
                return new Intent(Intent.ACTION_VIEW, facebookUri);
            }
        } catch (PackageManager.NameNotFoundException e) {
            logger(e.getMessage());
        }
        return new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_PAGE_URL));
    }

    private void logger(String message) {
        Log.d(this.getClass().getSimpleName(), message);
    }

    private ApplicationInfo getFacebookApplicationInfo() throws PackageManager.NameNotFoundException {
        return getPackageManager().getApplicationInfo("com.facebook.katana", 0);
    }
}
