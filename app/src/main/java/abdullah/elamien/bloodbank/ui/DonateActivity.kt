package abdullah.elamien.bloodbank.ui

import abdullah.elamien.bloodbank.R
import abdullah.elamien.bloodbank.databinding.ActivityDonateBinding
import abdullah.elamien.bloodbank.eventbus.DonateEvent
import abdullah.elamien.bloodbank.utils.Constants
import abdullah.elamien.bloodbank.viewmodels.DonateViewModel
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseUser
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.android.ext.android.inject
import java.util.*

class DonateActivity : AppCompatActivity() {
    private val mUser by inject<FirebaseUser>()
    private val mDonateViewModel by inject<DonateViewModel>()
    private lateinit var mBinding: ActivityDonateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_donate)
        mBinding.handlers = this
        setupToolbar()
        registerEventBus()
    }

    private fun setupToolbar() {
        setSupportActionBar(mBinding.toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.title = getString(R.string.donate_activity_label)
        }
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_nav_back)
    }


    private fun registerEventBus() {
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        unRegisterEventBus()
    }

    private fun unRegisterEventBus() {
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onEvent(event: DonateEvent) {
        hideLoadingIndicator()
        when {
            event.donateEventMessage.toLowerCase() == "success" -> buildSuccessSweetDialog()
            event.donateEventMessage.toLowerCase() == "user exists" -> showExistingDonorDialog()
            event.donateEventMessage.toLowerCase() == "fail" -> showError("Something went wrong!")
        }
    }

    fun onBecomeADonorButtonClick(view: View) {
        if (inputsAreValid()) {
            showLoadingIndicator()
            apply()
        }
    }

    private fun showLoadingIndicator() {
        mBinding.group.visibility = View.INVISIBLE
        mBinding.loadingIndicator.visibility = View.VISIBLE
    }

    private fun apply() {
        mDonateViewModel.applyNewDonor(donorInfo, mUser.uid)
    }

    private fun showExistingDonorDialog() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getString(R.string.existed_donor_dialog_title_msg))
                .setContentText(getString(R.string.existed_donor_dialog_text_msg))
                .setConfirmClickListener { getBackToHomeActivity() }
                .show()
    }

    private fun buildSuccessSweetDialog() {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(getString(R.string.becoming_a_donor_greeting_title))
                .setContentText(getString(R.string.becoming_a_donor_greeting_msg))
                .setConfirmClickListener { sweetAlertDialog -> getBackToHomeActivity() }
                .show()
    }

    private fun getBackToHomeActivity() {
        NavUtils.navigateUpFromSameTask(this@DonateActivity)
        finish()
    }

    private fun inputsAreValid(): Boolean {
        return if (isTextNotEmpty(donorName)
                && isTextNotEmpty(donorAge.toString())
                && isDonorAgeValid
                && isTextNotEmpty(donorPhone)
                && isTextNotEmpty(donorBio)
                && isAddressChipSelected
                && isBloodTypeChipSelected) {
            true
        } else {
            showError("Please fill the inputs, make sure you're 18 years old or older.")
            false
        }
    }

    private val donorInfo: Map<String, Any>
        get() {
            val donorInfo = HashMap<String, Any>()
            donorInfo[Constants.DONORS_USER_EMAIL] = mUser.email!!
            donorInfo[Constants.DONORS_USER_IMAGE] = imageUrl
            donorInfo[Constants.DONORS_USER_NAME] = donorName
            donorInfo[Constants.DONORS_USER_PHONE] = donorPhone
            donorInfo[Constants.DONORS_USER_AGE] = donorAge
            donorInfo[Constants.DONORS_USER_BIO] = donorBio
            donorInfo[Constants.DONORS_USER_ADDRESS] = chipAddress
            donorInfo[Constants.DONORS_USER_BLOOD_TYPE] = bloodType
            donorInfo[Constants.DONORS_USER_UID] = mUser.uid
            return donorInfo
        }


    private val imageUrl: String
        get() = if (mUser.photoUrl == null) {
            ""
        } else {
            mUser.photoUrl!!.toString()
        }

    private val isDonorAgeValid: Boolean
        get() = !(donorAge <= 17 || donorAge > 100)


    private val bloodType: String
        get() {
            return when (mBinding.bloodTypeChipGroup.checkedChipId) {
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
            return when (mBinding.addressChipGroup.checkedChipId) {
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
        get() = mBinding.bloodTypeChipGroup.checkedChipId != View.NO_ID


    private val isAddressChipSelected: Boolean
        get() = mBinding.addressChipGroup.checkedChipId != View.NO_ID

    private val donorBio: String
        get() = mBinding.donorBioEditText.text.toString()

    private val donorPhone: String
        get() = mBinding.donorPhoneEditText.text.toString()

    private val donorName: String
        get() = mBinding.donorNameEditText.text.toString()

    private val donorAge: Int
        get() {
            return try {
                Integer.parseInt(mBinding.donorAgeEditText.text.toString())
            } catch (e: NumberFormatException) {
                Log.d(TAG, e.localizedMessage)
                1
            }

        }

    private fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun isTextNotEmpty(input: String): Boolean {
        return !TextUtils.isEmpty(input)
    }

    private fun hideLoadingIndicator() {
        mBinding.loadingIndicator.visibility = View.INVISIBLE
        mBinding.group.visibility = View.VISIBLE
    }

    companion object {

        private const val TAG = "DonateActivity"
    }
}
