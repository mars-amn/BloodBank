package abdullah.elamien.bloodbank.ui

import abdullah.elamien.bloodbank.R
import abdullah.elamien.bloodbank.databinding.ActivitySplashBinding
import abdullah.elamien.bloodbank.utils.Constants
import abdullah.elamien.bloodbank.utils.PreferenceUtils
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.ext.android.inject


class SplashActivity : AppCompatActivity() {

    private val mFirebaseMessaging by inject<FirebaseMessaging>()
    private val mPreferenceUtils by inject<PreferenceUtils>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFullScreen()
        DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        subscribeToNotification()
        if (isUserFirstLaunch) {
            startWelcomeActivity()
        } else {
            startRegisterActivity()
        }
    }

    private val isUserFirstLaunch: Boolean
        get() {
            val preferences = getSharedPreferences(Constants.FIRST_LAUNCH_NAME, Context.MODE_PRIVATE)
            return !preferences.getBoolean(Constants.FIRST_LAUNCH_KEY, false)
        }


    private fun subscribeToNotification() {
        if (mPreferenceUtils.isNotificationsEnabled(this)) {
            mFirebaseMessaging.subscribeToTopic(Constants.SHOUTOUTS_TOPIC_NAME)
        } else {
            mFirebaseMessaging.unsubscribeFromTopic(Constants.SHOUTOUTS_TOPIC_NAME)
        }
    }

    private fun setupFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun startRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
