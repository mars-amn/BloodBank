package abdullah.elamien.bloodbank.ui

import abdullah.elamien.bloodbank.R
import abdullah.elamien.bloodbank.adapters.ShoutoutAdapter
import abdullah.elamien.bloodbank.databinding.ActivityHomeBinding

import abdullah.elamien.bloodbank.models.Shoutout
import abdullah.elamien.bloodbank.utils.ConfigHelper
import abdullah.elamien.bloodbank.utils.PreferenceUtils
import abdullah.elamien.bloodbank.viewmodels.ShoutoutsViewModel
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nightonke.boommenu.BoomButtons.HamButton
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private val mShoutoutsViewModel by inject<ShoutoutsViewModel>()
    private val mUser by inject<FirebaseUser>()
    private val mAuth by inject<FirebaseAuth>()
    lateinit var mHomeBinding: ActivityHomeBinding
    private var lastTranslate = 0.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguageFromPreference()
        mHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setupViews()
        setupShoutoutsRecyclerView()
        buildShoutoutBoomMenu()
        buildSearchBoomMenu()
        buildCreateDonorApplicationBoomMenu()
        accessHeaderView()
        registerSharedPreferenceListener()
    }

    private val actionBarDrawerToggle: ActionBarDrawerToggle
        get() {
            mHomeBinding.navigationView.itemIconTintList = null
            setSupportActionBar(mHomeBinding.toolbar)
            return object : ActionBarDrawerToggle(this, mHomeBinding.drawerLayout,
                    mHomeBinding.toolbar,
                    R.string.navigation_open,
                    R.string.navigation_close) {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    var slideOffset = slideOffset
                    super.onDrawerSlide(drawerView, slideOffset)

                    if (PreferenceUtils.getLanguageCode(this@HomeActivity).toLowerCase() == getString(R.string.arabic_language_value).toLowerCase()) {
                        slideOffset *= -1f
                    }

                    val moveFactor = mHomeBinding.drawerLayout.width * slideOffset
                    val anim = TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f)
                    anim.duration = 0
                    anim.fillAfter = true
                    mHomeBinding.holder.startAnimation(anim)
                    lastTranslate = moveFactor
                    super.onDrawerSlide(drawerView, slideOffset)
                }
            }
        }

    private fun setLanguageFromPreference() {
        ConfigHelper.setLanguage(resources, PreferenceUtils.getLanguageCode(this))
    }

    private fun setupShoutoutsRecyclerView() {
        hideRecyclerView()
        mShoutoutsViewModel.shoutouts?.observe(this, Observer<List<Shoutout>> { t ->
            val adapter = ShoutoutAdapter(this)
            mHomeBinding.shoutoutsRecyclerView.adapter = adapter
            adapter.addShoutouts(t)
            showRecyclerView()
        })
    }

    private fun accessHeaderView() {
        val headerView = mHomeBinding.navigationView.getHeaderView(0)
        val userImage = headerView.findViewById<CircleImageView>(R.id.profileHeaderImage)
        val userName = headerView.findViewById<TextView>(R.id.profileHeaderUserName)
        val userEmail = headerView.findViewById<TextView>(R.id.profileHeaderUserEmail)
        Glide.with(this).load(mUser.photoUrl).into(userImage)
        userName.text = mUser.displayName
        userEmail.text = mUser.email

    }

    private fun setupViews() {
        val toggle = actionBarDrawerToggle
        mHomeBinding.drawerLayout.addDrawerListener(toggle)
        mHomeBinding.navigationView.setNavigationItemSelectedListener(this)
        toggle.syncState()
        mHomeBinding.toolbar.setNavigationIcon(R.drawable.ic_nav_icon)
        val actionBar = supportActionBar
        actionBar?.setTitle(R.string.app_name)
    }

    override fun onBackPressed() {
        if (mHomeBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mHomeBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun buildCreateDonorApplicationBoomMenu() {
        val builder = HamButton.Builder()
                .normalImageRes(R.drawable.ic_blood_donation)
                .normalTextRes(R.string.become_a_donor_button_label)
                .normalColorRes(R.color.secondaryColor)
                .subNormalTextRes(R.string.join_donors_sub_text)
                .listener { index -> startDonateActivity() }
        mHomeBinding.bmb.addBuilder(builder)
    }

    private fun buildSearchBoomMenu() {
        val builder = HamButton.Builder()
                .normalImageRes(R.drawable.ic_search)
                .normalTextRes(R.string.search_donors_label)
                .normalColorRes(R.color.secondaryColor)
                .subNormalTextRes(R.string.search_donors_sub_text)
                .listener { index -> startSearchActivity() }
        mHomeBinding.bmb.addBuilder(builder)
    }

    private fun buildShoutoutBoomMenu() {
        val builder = HamButton.Builder()
                .normalImageRes(R.drawable.ic_shoutout)
                .normalTextRes(R.string.create_shoutout_label)
                .normalColorRes(R.color.secondaryColor)
                .subNormalTextRes(R.string.shoutout_sub_text)
                .listener { index -> startShoutoutCreationActivity() }
        mHomeBinding.bmb.addBuilder(builder)
    }

    private fun startShoutoutCreationActivity() {
        val intent = Intent(this, ShoutoutCreationActivity::class.java)
        startActivity(intent)
    }

    private fun startSearchActivity() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }


    private fun startDonateActivity() {
        val intent = Intent(this, DonateActivity::class.java)
        startActivity(intent)
    }

    private fun hideRecyclerView() {
        mHomeBinding.shoutoutsRecyclerView.visibility = View.INVISIBLE
    }

    private fun showRecyclerView() {
        mHomeBinding.shoutoutsRecyclerView.visibility = View.VISIBLE
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.createShoutoutMenuItem -> {
                startShoutoutCreationActivity()
                return true
            }
            R.id.becomeADonorMenuItem -> {
                startDonateActivity()
                return true
            }
            R.id.searchForDonorsMenuItem -> {
                startSearchActivity()
                return true
            }
            R.id.settingsMenuItem -> {
                startSettingsActivity()
                return true
            }
            R.id.aboutMenuItem -> {
                startAboutActivity()
                return true
            }
            R.id.logoutMenuItem -> {
                logoutUser()
                return true
            }
            else -> return false
        }
    }

    private fun logoutUser() {
        mAuth.signOut()
        startLoginActivity()
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startAboutActivity() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun startSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.language_key)) {
            val language = sharedPreferences.getString(key, getString(R.string.arabic_language_value))
            ConfigHelper.setLanguage(resources, language!!)
            recreate()
        }
    }

    private fun registerSharedPreferenceListener() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterSharedPreferenceListener()
    }

    private fun unregisterSharedPreferenceListener() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this)
    }

    companion object {

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}
