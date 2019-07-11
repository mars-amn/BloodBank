package abdullah.elamien.bloodbank.ui

import abdullah.elamien.bloodbank.R
import abdullah.elamien.bloodbank.databinding.ActivityAboutBinding
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

class AboutActivity : AppCompatActivity() {
    private var mBinding: ActivityAboutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about)
        mBinding!!.handlers = this
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(mBinding!!.toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.title = getString(R.string.about_activity_label)
        }
        mBinding!!.toolbar.setNavigationIcon(R.drawable.ic_nav_back)
    }

    fun onFacebookButtonClick(view: View) {
        openFacebookPage()
    }

    private fun openFacebookPage() {
        val intent = facebookIntent
        startActivity(intent)
    }

    private val facebookIntent: Intent
        get() {
            try {
                if (facebookApplicationInfo.enabled) {
                    val facebookUri = Uri.parse("fb://facewebmodal/f?href=$FACEBOOK_PAGE_URL")
                    return Intent(Intent.ACTION_VIEW, facebookUri)
                }
            } catch (e: PackageManager.NameNotFoundException) {
                logger(e.message)
            }

            return Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_PAGE_URL))
        }

    private val facebookApplicationInfo: ApplicationInfo
        @Throws(PackageManager.NameNotFoundException::class)
        get() = packageManager.getApplicationInfo("com.facebook.katana", 0)

    private fun logger(message: String?) {
        Log.d(this.javaClass.simpleName, message)
    }

    companion object {
        private const val FACEBOOK_PAGE_URL = "https://www.facebook.com/"
    }
}
