package abdullah.elamien.bloodbank.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.R;
import abdullah.elamien.bloodbank.adapters.SearchAdapter;
import abdullah.elamien.bloodbank.databinding.ActivitySearchBinding;
import abdullah.elamien.bloodbank.di.components.DaggerSearchComponent;
import abdullah.elamien.bloodbank.di.components.SearchComponent;
import abdullah.elamien.bloodbank.di.modules.SearchAdapterModule;
import abdullah.elamien.bloodbank.eventbus.SearchEvent;
import abdullah.elamien.bloodbank.models.Donor;
import abdullah.elamien.bloodbank.viewmodels.SearchViewModel;
import abdullah.elamien.bloodbank.viewmodels.factory.ViewModelFactory;

public class SearchActivity extends AppCompatActivity {


    @Inject
    FirebaseFirestore mFirestore;
    @Inject
    SearchAdapter mSearchAdapter;
    @Inject
    ViewModelFactory mViewModelFactory;
    private ActivitySearchBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        mBinding.setHandlers(this);
        initDagger();
        setupToolbar();
        registerEventBus();
    }

    private void initDagger() {
        SearchComponent component = DaggerSearchComponent.builder()
                .searchAdapterModule(new SearchAdapterModule(this))
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
            actionBar.setTitle(getString(R.string.search_activity_label));
        }
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_nav_back);
    }


    private void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterEventBus();
    }

    private void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(SearchEvent event) {
        if (event.getSearchEventMessage().toLowerCase().equals("fail")) {
            showError(getString(R.string.error_msg));
        }
    }

    private void showError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    public void onSearchDonorsButtonClick(View view) {
        if (isChipsSelected()) {
            searchForDonors();
        }
    }

    private boolean isChipsSelected() {
        return isAddressChipSelected() && isBloodTypeChipSelected();
    }

    private void searchForDonors() {
        SearchViewModel viewModel = ViewModelProviders.of(this, mViewModelFactory).get(SearchViewModel.class);
        viewModel.getSearchResult(getChipAddress(), getBloodType()).observe(this, this::showSearchResults);
    }

    private void showSearchResults(List<Donor> donors) {
        mSearchAdapter.setDonors(donors);
        mBinding.searchDonorsRecyclerView.setAdapter(mSearchAdapter);
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

    private boolean isBloodTypeChipSelected() {
        return mBinding.bloodTypeChipGroup.getCheckedChipId() != View.NO_ID;
    }


    private boolean isAddressChipSelected() {
        return mBinding.addressChipGroup.getCheckedChipId() != View.NO_ID;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
