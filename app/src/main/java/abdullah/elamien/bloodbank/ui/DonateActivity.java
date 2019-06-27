package abdullah.elamien.bloodbank.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.R;
import abdullah.elamien.bloodbank.databinding.ActivityDonateBinding;
import abdullah.elamien.bloodbank.di.components.DaggerDonateComponent;
import abdullah.elamien.bloodbank.di.components.DonateComponent;
import abdullah.elamien.bloodbank.eventbus.DonateEvent;
import abdullah.elamien.bloodbank.utils.Constants;
import abdullah.elamien.bloodbank.viewmodels.DonateViewModel;
import abdullah.elamien.bloodbank.viewmodels.factory.ViewModelFactory;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class DonateActivity extends AppCompatActivity {

    private static final String TAG = "DonateActivity";
    @Inject
    FirebaseFirestore mFirestore;
    @Inject
    FirebaseAuth mAuth;
    @Inject
    FirebaseUser mUser;
    @Inject
    ViewModelFactory mViewModelFactory;
    private ActivityDonateBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_donate);
        mBinding.setHandlers(this);
        initDagger();
        setupToolbar();
        registerEventBus();
    }

    private void initDagger() {
        DonateComponent component = DaggerDonateComponent.builder()
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
            actionBar.setTitle(getString(R.string.donate_activity_label));
        }
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_nav_back);
    }


    private void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unRegisterEventBus();
    }

    private void unRegisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(DonateEvent event) {
        hideLoadingIndicator();
        if (event.getDonateEventMessage().toLowerCase().equals("success")) {
            buildSuccessSweetDialog();
        } else if (event.getDonateEventMessage().toLowerCase().equals("user exists")) {
            showExistingDonorDialog();
        } else if (event.getDonateEventMessage().toLowerCase().equals("fail")) {
            showError("Something went wrong!");
        }
    }

    public void onBecomeADonorButtonClick(View view) {
        if (inputsAreValid()) {
            showLoadingIndicator();
            apply();
        }
    }

    private void showLoadingIndicator() {
        mBinding.group.setVisibility(View.INVISIBLE);
        mBinding.loadingIndicator.setVisibility(View.VISIBLE);
    }

    private void apply() {
        DonateViewModel viewModel = ViewModelProviders.of(this, mViewModelFactory).get(DonateViewModel.class);
        viewModel.applyNewDonor(getDonorInfo(), mUser.getUid());
    }

    private void showExistingDonorDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getString(R.string.existed_donor_dialog_title_msg))
                .setContentText(getString(R.string.existed_donor_dialog_text_msg))
                .setConfirmClickListener(sweetAlertDialog -> getBackToHomeActivity())
                .show();
    }

    private Map<String, Object> getDonorInfo() {
        Map<String, Object> donorInfo = new HashMap<>();
        donorInfo.put(Constants.DONORS_USER_EMAIL, mUser.getEmail());
        donorInfo.put(Constants.DONORS_USER_IMAGE, getImageUrl());
        donorInfo.put(Constants.DONORS_USER_NAME, getDonorName());
        donorInfo.put(Constants.DONORS_USER_PHONE, getDonorPhone());
        donorInfo.put(Constants.DONORS_USER_AGE, getDonorAge());
        donorInfo.put(Constants.DONORS_USER_BIO, getDonorBio());
        donorInfo.put(Constants.DONORS_USER_ADDRESS, getChipAddress());
        donorInfo.put(Constants.DONORS_USER_BLOOD_TYPE, getBloodType());
        donorInfo.put(Constants.DONORS_USER_UID, mUser.getUid());
        return donorInfo;
    }

    private void buildSuccessSweetDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(getString(R.string.becoming_a_donor_greeting_title))
                .setContentText(getString(R.string.becoming_a_donor_greeting_msg))
                .setConfirmClickListener(sweetAlertDialog -> getBackToHomeActivity())
                .show();
    }

    private void getBackToHomeActivity() {
        NavUtils.navigateUpFromSameTask(DonateActivity.this);
        finish();
    }


    private String getImageUrl() {
        if (mUser.getPhotoUrl() == null) {
            return "";
        } else {
            return mUser.getPhotoUrl().toString();
        }
    }

    private boolean inputsAreValid() {
        if (isTextNotEmpty(getDonorName())
                && isTextNotEmpty(String.valueOf(getDonorAge()))
                && isDonorAgeValid()
                && isTextNotEmpty(getDonorPhone())
                && isTextNotEmpty(getDonorBio())
                && isAddressChipSelected()
                && isBloodTypeChipSelected()) {
            return true;
        } else {
            showError("Please fill the inputs, make sure you're 18 years old or older.");
            return false;
        }
    }

    private boolean isDonorAgeValid() {
        return !(getDonorAge() <= 17 || getDonorAge() > 100);
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


    private void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean isBloodTypeChipSelected() {
        return mBinding.bloodTypeChipGroup.getCheckedChipId() != View.NO_ID;
    }


    private boolean isAddressChipSelected() {
        return mBinding.addressChipGroup.getCheckedChipId() != View.NO_ID;
    }

    private String getDonorBio() {
        return mBinding.donorBioEditText.getText().toString();
    }

    private String getDonorPhone() {
        return mBinding.donorPhoneEditText.getText().toString();
    }

    private String getDonorName() {
        return mBinding.donorNameEditText.getText().toString();
    }

    private int getDonorAge() {
        try {
            return Integer.parseInt(mBinding.donorAgeEditText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, e.getLocalizedMessage());
            return 1;
        }
    }

    private boolean isTextNotEmpty(String input) {
        return !TextUtils.isEmpty(input);
    }

    private void hideLoadingIndicator() {
        mBinding.loadingIndicator.setVisibility(View.INVISIBLE);
        mBinding.group.setVisibility(View.VISIBLE);
    }
}
