package abdullah.elamien.bloodbank.ui

import abdullah.elamien.bloodbank.R
import abdullah.elamien.bloodbank.adapters.SearchAdapter
import abdullah.elamien.bloodbank.databinding.ActivitySearchBinding
import abdullah.elamien.bloodbank.eventbus.SearchEvent
import abdullah.elamien.bloodbank.models.Donor
import abdullah.elamien.bloodbank.viewmodels.SearchViewModel
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.android.ext.android.inject

class SearchActivity : AppCompatActivity() {

    private val mSearchViewModel by inject<SearchViewModel>()
    private var mBinding: ActivitySearchBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        mBinding!!.handlers = this
        setupToolbar()
        registerEventBus()
    }

    private fun setupToolbar() {
        setSupportActionBar(mBinding!!.toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.title = getString(R.string.search_activity_label)
        }
        mBinding!!.toolbar.setNavigationIcon(R.drawable.ic_nav_back)
    }


    private fun registerEventBus() {
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        unregisterEventBus()
    }

    private fun unregisterEventBus() {
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onEvent(event: SearchEvent) {
        if (event.searchEventMessage.toLowerCase() == "fail") {
            showError(getString(R.string.error_msg))
        }
    }

    private fun showError(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }


    fun onSearchDonorsButtonClick(view: View) {
        if (isChipsSelected) {
            searchForDonors()
        }
    }

    private fun searchForDonors() {
        mSearchViewModel.getSearchResult(chipAddress, bloodType).observe(this, Observer<List<Donor>> { this.showSearchResults(it) })
    }

    private val isChipsSelected: Boolean
        get() = isAddressChipSelected && isBloodTypeChipSelected

    private val bloodType: String
        get() {
            return when (mBinding!!.bloodTypeChipGroup.checkedChipId) {
                R.id.aPlusChip -> getString(R.string.a_plus_blood_type_chip_label)
                R.id.aMinusChip -> getString(R.string.a_minus_blood_type_chip_label)
                R.id.bPlusChip -> getString(R.string.b_plus_blood_type_chip_label)
                R.id.bMinusChip -> getString(R.string.b_minus_blood_type_chip_label)
                R.id.oPlusChip -> getString(R.string.o_plus_blood_type_chip_label)
                R.id.oMinusChip -> getString(R.string.o_minus_blood_type_chip_label)
                R.id.abPlusChip -> getString(R.string.ab_plus_blood_type_chip_label)
                R.id.abMinusChip -> getString(R.string.ab_minus_blood_type_chip_label)
                else -> "Not available"
            }
        }

    private val chipAddress: String
        get() {
            return when (mBinding!!.addressChipGroup.checkedChipId) {
                R.id.arishChip -> getString(R.string.alarish_chip_label)
                R.id.bearAlAbdChip -> getString(R.string.bear_al_abd_chip_label)
                R.id.nekhelChip -> getString(R.string.nekhel_chip_label)
                R.id.sheikhZouidChip -> getString(R.string.sheikh_zould_chip_label)
                R.id.rafahChip -> getString(R.string.rafah_chip_label)
                R.id.hasanaChip -> getString(R.string.hasana_chip_label)
                else -> "Not available"
            }
        }

    private val isBloodTypeChipSelected: Boolean
        get() = mBinding!!.bloodTypeChipGroup.checkedChipId != View.NO_ID


    private val isAddressChipSelected: Boolean
        get() = mBinding!!.addressChipGroup.checkedChipId != View.NO_ID

    private fun showSearchResults(donors: List<Donor>) {
        val adapter = SearchAdapter(this)
        adapter.setDonors(donors)
        mBinding!!.searchDonorsRecyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> NavUtils.navigateUpFromSameTask(this)
        }
        return super.onOptionsItemSelected(item)
    }
}
