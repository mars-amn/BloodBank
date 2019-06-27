package abdullah.elamien.bloodbank.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.R;
import abdullah.elamien.bloodbank.databinding.ActivityShoutoutCreationBinding;
import abdullah.elamien.bloodbank.di.components.DaggerShoutoutCreationComponent;
import abdullah.elamien.bloodbank.di.components.ShoutoutCreationComponent;
import abdullah.elamien.bloodbank.eventbus.ShoutoutCreationEvent;
import abdullah.elamien.bloodbank.utils.Constants;
import abdullah.elamien.bloodbank.viewmodels.ShoutoutsViewModel;
import abdullah.elamien.bloodbank.viewmodels.factory.ViewModelFactory;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ShoutoutCreationActivity extends AppCompatActivity {

    @Inject
    FirebaseAuth mAuth;
    @Inject
    FirebaseUser mUser;
    @Inject
    ViewModelFactory mModelFactory;
    private ActivityShoutoutCreationBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_shoutout_creation);
        mBinding.setHandlers(this);
        initDagger();
        setupToolbar();
        loadUserImage();
        registerEvenBusListener();
    }

    private void initDagger() {
        ShoutoutCreationComponent component = DaggerShoutoutCreationComponent.builder()
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
            actionBar.setTitle(getString(R.string.shoutout_activity_label));
        }
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_nav_back);
    }

    private void loadUserImage() {
        Glide.with(this).load(mUser.getPhotoUrl()).into(mBinding.userProfileImage);
    }


    private void registerEvenBusListener() {
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterEventBustListener();
    }

    private void unregisterEventBustListener() {
        EventBus.getDefault().unregister(this);
    }

    public void onCreateShoutoutClick(View view) {
        if (inputsAreValid()) {
            showLoadingIndicator();
            createNewShoutout();
        }
    }

    private boolean inputsAreValid() {
        if (isTextNotEmpty(getShoutoutUserName()) &&
                isTextNotEmpty(getShoutoutUserNumber()) &&
                isTextNotEmpty(getShoutoutAddress()) &&
                isTextNotEmpty(getShoutoutUserDescription()) &&
                isShoutoutAddressChipSelected() &&
                isShoutoutBloodTypeChipSelected()) {
            return true;
        } else {
            showError("Please make sure you filled the application!");
            return false;
        }
    }

    private void showLoadingIndicator() {
        mBinding.shoutoutCreationGroup.setVisibility(View.INVISIBLE);
        mBinding.loadingIndicator.setVisibility(View.VISIBLE);
    }


    private void createNewShoutout() {
        ShoutoutsViewModel viewModel = ViewModelProviders.of(this, mModelFactory).get(ShoutoutsViewModel.class);
        viewModel.createShoutout(getNewShoutoutDetails());
    }

    private Map<String, Object> getNewShoutoutDetails() {
        Map<String, Object> shoutout = new HashMap<>();
        shoutout.put(Constants.SHOUTOUT_USER_EMAIL, getUserEmail());
        shoutout.put(Constants.SHOUTOUT_USER_NAME, getShoutoutUserName());
        shoutout.put(Constants.SHOUTOUT_USER_PHONE, getShoutoutUserNumber());
        shoutout.put(Constants.SHOUTOUT_USER_SPECIFIC_ADDRESS, getShoutoutAddress());
        shoutout.put(Constants.SHOUTOUT_USER_DESCRIPTION, getShoutoutUserDescription());
        shoutout.put(Constants.SHOUTOUT_USER_NEEDED_ADDRESS, getChipAddress());
        shoutout.put(Constants.SHOUTOUT_USER_BLOOD_TYPE, getBloodType());
        shoutout.put(Constants.SHOUTOUT_USER_IMAGE_URL, getImageUrl());
        shoutout.put(Constants.SHOUTOUT_USER_ID, getUserId());
        shoutout.put(Constants.SHOUTOUT_TIME, new Date());
        return shoutout;
    }

    @Subscribe
    public void onEvent(ShoutoutCreationEvent event) {
        hideLoadingIndicator();
        if (event.getShoutoutCreationMessage().toLowerCase().equals("success")) {
            buildSuccessSweetDialog();
        } else if (event.getShoutoutCreationMessage().toLowerCase().equals("fail")) {
            showError("Something went wrong!");
        }
    }

    private void hideLoadingIndicator() {
        mBinding.loadingIndicator.setVisibility(View.INVISIBLE);
        mBinding.shoutoutCreationGroup.setVisibility(View.VISIBLE);
    }

    private void buildSuccessSweetDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(getString(R.string.shoutout_creation_successfully_title))
                .setContentText(getString(R.string.shoutout_creation_success_msg))
                .setConfirmClickListener(sweetAlertDialog -> {
                    NavUtils.navigateUpFromSameTask(ShoutoutCreationActivity.this);
                    finish();
                })
                .show();
    }

    private String getUserId() {
        return mUser.getUid();
    }

    private String getImageUrl() {
        if (mUser.getPhotoUrl() == null) {
            return "";
        } else {
            return mUser.getPhotoUrl().toString();
        }
    }

    private String getUserEmail() {
        return mUser.getEmail();
    }


    private boolean isShoutoutBloodTypeChipSelected() {
        return mBinding.bloodTypeChipGroup.getCheckedChipId() != View.NO_ID;
    }

    private void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean isShoutoutAddressChipSelected() {
        return mBinding.addressChipGroup.getCheckedChipId() != View.NO_ID;
    }

    private String getBloodType() {
        switch (mBinding.bloodTypeChipGroup.getCheckedChipId()) {
            case R.id.aPlusChip:
                return getString(R.string.a_plus_blood_type_chip_label);
            case R.id.aMinusChip:
                return getString(R.string.a_minus_blood_type_chip_label);
            case R.id.bPlusChip:
                return getString(R.string.b_plus_blood_type_chip_label);
            case R.id.bMinusChip:
                return getString(R.string.b_minus_blood_type_chip_label);
            case R.id.oPlusChip:
                return getString(R.string.o_plus_blood_type_chip_label);
            case R.id.oMinusChip:
                return getString(R.string.o_minus_blood_type_chip_label);
            case R.id.abPlusChip:
                return getString(R.string.ab_plus_blood_type_chip_label);
            case R.id.abMinusChip:
                return getString(R.string.ab_minus_blood_type_chip_label);
            case R.id.allBloodTypesChip:
                return getString(R.string.all_blood_type_chip_label);
            default:
                return "Not available";
        }
    }

    private String getChipAddress() {
        switch (mBinding.addressChipGroup.getCheckedChipId()) {
            case R.id.arishChip:
                return getString(R.string.alarish_chip_label);
            case R.id.bearAlAbdChip:
                return getString(R.string.bear_al_abd_chip_label);
            case R.id.nekhelChip:
                return getString(R.string.nekhel_chip_label);
            case R.id.sheikhZouidChip:
                return getString(R.string.sheikh_zould_chip_label);
            case R.id.rafahChip:
                return getString(R.string.rafah_chip_label);
            case R.id.hasanaChip:
                return getString(R.string.hasana_chip_label);
            default:
                return "Not available";
        }
    }

    private String getShoutoutUserDescription() {
        return mBinding.shoutoutUserDescription.getText().toString();
    }

    private String getShoutoutAddress() {
        return mBinding.shoutoutUserAddress.getText().toString();
    }

    private String getShoutoutUserNumber() {
        return mBinding.shoutoutUserPhone.getText().toString();
    }

    private String getShoutoutUserName() {
        return mBinding.shoutoutUserName.getText().toString();
    }

    private boolean isTextNotEmpty(String input) {
        return !TextUtils.isEmpty(input);
    }
}
