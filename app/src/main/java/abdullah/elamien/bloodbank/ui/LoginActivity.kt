package abdullah.elamien.bloodbank.ui

import abdullah.elamien.bloodbank.R
import abdullah.elamien.bloodbank.databinding.ActivityLoginBinding
import abdullah.elamien.bloodbank.eventbus.AuthenticationEvent
import abdullah.elamien.bloodbank.models.AuthModel
import abdullah.elamien.bloodbank.viewmodels.AuthenticationViewModel
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private val mAuthViewModel by inject<AuthenticationViewModel>()
    private lateinit var mCallbackManager: CallbackManager
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding.handlers = this
        setupToolbar()
        setupNewMembershipLabel()
        setupFacebookLogin()
        setupGoogleLogin()
        registerEventBus()
    }

    private fun registerEventBus() {
        EventBus.getDefault().register(this)
    }

    private fun setupToolbar() {
        setSupportActionBar(mBinding.toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.title = getString(R.string.login_activity_label)
        }
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_nav_back_white)
    }

    override fun onStop() {
        super.onStop()
        unregisterEventBus()
    }

    private fun unregisterEventBus() {
        EventBus.getDefault().unregister(this)
    }

    private fun setupGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_api_key))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setupFacebookLogin() {
        mCallbackManager = CallbackManager.Factory.create()
        mBinding.facebookRegister.setReadPermissions("email", "public_profile")
        mBinding.facebookRegister.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                registerFacebookUser(loginResult.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {
                showError(getString(R.string.error_msg))
            }
        })
    }

    private fun registerFacebookUser(token: AccessToken) {
        mAuthViewModel.registerFacebookUser(FacebookAuthProvider.getCredential(token.token))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            showProgressBar()
            mAuthViewModel.registerGoogleUser(GoogleSignIn.getSignedInAccountFromIntent(data))
        }
    }

    @Subscribe
    fun onEvent(event: AuthenticationEvent) {
        hideProgressBar()
        if (event.authenticationEventMessage.toLowerCase() == "success") {
            startHomeActivity()
        } else if (event.authenticationEventMessage.toLowerCase() == "fail") {
            showError(getString(R.string.error_msg))
        }
    }

    private fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun setupNewMembershipLabel() {
        val spannableString = SpannableString(getString(R.string.new_membership_label))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startRegisterActivity()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(clickableSpan, 23, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.newMembershipLabel.text = spannableString
        mBinding.newMembershipLabel.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun startRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }


    fun onLoginButtonClick(view: View) {
        if (allInputsAreValid()) {
            loginUser()
        } else {
            showError(getString(R.string.error_msg))
        }
    }

    private fun loginUser() {
        showProgressBar()
        mAuthViewModel.loginExistingUser(authModel)
    }

    private val authModel: AuthModel
        get() = AuthModel(mBinding.loginEmailEditText.text!!.toString(),
                mBinding.loginPasswordEditText.text!!.toString())

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun allInputsAreValid(): Boolean {
        val email = mBinding.loginEmailEditText.text!!.toString().trim { it <= ' ' }
        val password = mBinding.loginPasswordEditText.text!!.toString()

        return if (isTextNotEmpty(email)
                && isEmailValid(email)
                && isTextNotEmpty(password)) {
            true
        } else {
            showError("Please type in valid inputs")
            false
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isTextNotEmpty(input: String): Boolean {
        return !TextUtils.isEmpty(input)
    }

    fun onSinInWithGoogleClick(view: View) {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun showProgressBar() {
        mBinding.loginGroup.visibility = View.INVISIBLE
        mBinding.loadingIndicator.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        mBinding.loadingIndicator.visibility = View.GONE
        mBinding.loginGroup.visibility = View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> startRegisterActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private const val RC_SIGN_IN = 2019
    }

}
