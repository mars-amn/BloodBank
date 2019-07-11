package abdullah.elamien.bloodbank.ui

import abdullah.elamien.bloodbank.R
import abdullah.elamien.bloodbank.databinding.ActivityShoutoutCreationBinding
import abdullah.elamien.bloodbank.eventbus.ShoutoutCreationEvent
import abdullah.elamien.bloodbank.utils.Constants
import abdullah.elamien.bloodbank.viewmodels.ShoutoutsViewModel
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.android.ext.android.inject
import java.util.*

class ShoutoutCreationActivity : AppCompatActivity() {

    private val mUser by inject<FirebaseUser>()
    private val shoutoutViewModel by inject<ShoutoutsViewModel>()
    private var mBinding: ActivityShoutoutCreationBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_shoutout_creation)
        mBinding!!.handlers = this
        setupToolbar()
        loadUserImage()
        registerEvenBusListener()
    }

    private fun setupToolbar() {
        setSupportActionBar(mBinding!!.toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.title = getString(R.string.shoutout_activity_label)
        }
        mBinding!!.toolbar.setNavigationIcon(R.drawable.ic_nav_back)
    }

    private fun loadUserImage() {
        Glide.with(this).load(mUser.photoUrl).into(mBinding!!.userProfileImage)
    }


    private fun registerEvenBusListener() {
        EventBus.getDefault().register(this)
    }


    override fun onStop() {
        super.onStop()
        unregisterEventBustListener()
    }

    private fun unregisterEventBustListener() {
        EventBus.getDefault().unregister(this)
    }

    fun onCreateShoutoutClick(view: View) {
        if (inputsAreValid()) {
            showLoadingIndicator()
            createNewShoutout()
        }
    }

    private fun inputsAreValid(): Boolean {
        return if (isTextNotEmpty(shoutoutUserName) &&
                isTextNotEmpty(shoutoutUserNumber) &&
                isTextNotEmpty(shoutoutAddress) &&
                isTextNotEmpty(shoutoutUserDescription) &&
                isShoutoutAddressChipSelected &&
                isShoutoutBloodTypeChipSelected) {
            true
        } else {
            showError("Please make sure you filled the application!")
            false
        }
    }

    private fun showLoadingIndicator() {
        mBinding!!.shoutoutCreationGroup.visibility = View.INVISIBLE
        mBinding!!.loadingIndicator.visibility = View.VISIBLE
    }


    private fun createNewShoutout() {
        shoutoutViewModel.createShoutout(newShoutoutDetails)
    }

    private val newShoutoutDetails: Map<String, Any>
        get() {
            val shoutout = HashMap<String, Any>()
            shoutout[Constants.SHOUTOUT_USER_EMAIL] = userEmail!!
            shoutout[Constants.SHOUTOUT_USER_NAME] = shoutoutUserName
            shoutout[Constants.SHOUTOUT_USER_PHONE] = shoutoutUserNumber
            shoutout[Constants.SHOUTOUT_USER_SPECIFIC_ADDRESS] = shoutoutAddress
            shoutout[Constants.SHOUTOUT_USER_DESCRIPTION] = shoutoutUserDescription
            shoutout[Constants.SHOUTOUT_USER_NEEDED_ADDRESS] = chipAddress
            shoutout[Constants.SHOUTOUT_USER_BLOOD_TYPE] = bloodType
            shoutout[Constants.SHOUTOUT_USER_IMAGE_URL] = imageUrl
            shoutout[Constants.SHOUTOUT_USER_ID] = userId
            shoutout[Constants.SHOUTOUT_TIME] = Date()
            return shoutout
        }

    private val userId: String
        get() = mUser.uid

    private val imageUrl: String
        get() = if (mUser.photoUrl == null) {
            ""
        } else {
            mUser.photoUrl!!.toString()
        }

    private val userEmail: String?
        get() = mUser.email


    private val isShoutoutBloodTypeChipSelected: Boolean
        get() = mBinding!!.bloodTypeChipGroup.checkedChipId != View.NO_ID

    private val isShoutoutAddressChipSelected: Boolean
        get() = mBinding!!.addressChipGroup.checkedChipId != View.NO_ID

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
                R.id.allBloodTypesChip -> getString(R.string.all_blood_type_chip_label)
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

    private val shoutoutUserDescription: String
        get() = mBinding!!.shoutoutUserDescription.text.toString()

    private val shoutoutAddress: String
        get() = mBinding!!.shoutoutUserAddress.text.toString()

    private val shoutoutUserNumber: String
        get() = mBinding!!.shoutoutUserPhone.text.toString()

    private val shoutoutUserName: String
        get() = mBinding!!.shoutoutUserName.text.toString()

    @Subscribe
    fun onEvent(event: ShoutoutCreationEvent) {
        hideLoadingIndicator()
        if (event.shoutoutCreationMessage.toLowerCase() == "success") {
            buildSuccessSweetDialog()
        } else if (event.shoutoutCreationMessage.toLowerCase() == "fail") {
            showError("Something went wrong!")
        }
    }

    private fun hideLoadingIndicator() {
        mBinding!!.loadingIndicator.visibility = View.INVISIBLE
        mBinding!!.shoutoutCreationGroup.visibility = View.VISIBLE
    }

    private fun buildSuccessSweetDialog() {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(getString(R.string.shoutout_creation_successfully_title))
                .setContentText(getString(R.string.shoutout_creation_success_msg))
                .setConfirmClickListener {
                    NavUtils.navigateUpFromSameTask(this@ShoutoutCreationActivity)
                    finish()
                }
                .show()
    }

    private fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun isTextNotEmpty(input: String): Boolean {
        return !TextUtils.isEmpty(input)
    }
}
