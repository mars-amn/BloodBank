package abdullah.elamien.bloodbank.ui

import abdullah.elamien.bloodbank.R
import abdullah.elamien.bloodbank.databinding.ActivityRegisterBinding
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
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
import com.google.firebase.auth.FirebaseAuth
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.android.ext.android.inject

class RegisterActivity : AppCompatActivity() {


    private val mAuth by inject<FirebaseAuth>()
    private val mAuthViewModel by inject<AuthenticationViewModel>()


    private var mCallbackManager: CallbackManager? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mBinding: ActivityRegisterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        mBinding!!.handlers = this

        if (mAuth.currentUser != null) {
            startHomeActivity()
        } else {
            registerEventBus()
            setupToolbar()
            setupExistedMembershipLabel()
            setupFacebookRegister()
            setupGoogleClient()
        }
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

    private fun setupToolbar() {
        setSupportActionBar(mBinding!!.toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = getString(R.string.register_activity_label)
        }
    }

    private fun setupGoogleClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_api_key))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }


    private fun setupFacebookRegister() {
        mCallbackManager = CallbackManager.Factory.create()
        mBinding!!.facebookRegister.setReadPermissions("email", "public_profile")
        mBinding!!.facebookRegister.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                registerFacebookUser(loginResult.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebookReg" + error.message)
                Toast.makeText(this@RegisterActivity, R.string.error_msg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun registerFacebookUser(token: AccessToken) {
        mAuthViewModel.registerFacebookUser(FacebookAuthProvider.getCredential(token.token))
    }

    fun onGoogleSignInClick(view: View) {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager!!.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            showProgressBar()
            mAuthViewModel.registerGoogleUser(GoogleSignIn.getSignedInAccountFromIntent(data))
        }
    }

    private fun setupExistedMembershipLabel() {
        val spannableString = SpannableString(getString(R.string.existed_membership_label))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startLoginActivity()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(clickableSpan, 17, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding!!.newMembershipLabel.text = spannableString
        mBinding!!.newMembershipLabel.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


    fun onRegisterButtonClick(view: View) {
        if (inputsAreValid()) {
            registerNewUser()
        } else {
            showError(getString(R.string.error_msg))
        }
    }

    private fun registerNewUser() {
        showProgressBar()
        val model = authModel
        mAuthViewModel.registerNewUser(model)
    }

    private val authModel: AuthModel
        get() = AuthModel(mBinding!!.registerNameEditText.text!!.toString(),
                mBinding!!.registerEmailEditText.text!!.toString(),
                mBinding!!.registerPasswordConfirmEditText.text!!.toString())

    @Subscribe
    fun onEvent(event: AuthenticationEvent) {
        hideProgressBar()
        if (event.authenticationEventMessage.toLowerCase() == "success") {
            startHomeActivity()
        } else if (event.authenticationEventMessage.toLowerCase() == "fail") {
            showError(getString(R.string.error_msg))
        }
    }


    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun inputsAreValid(): Boolean {
        val name = mBinding!!.registerNameEditText.text!!.toString()
        val email = mBinding!!.registerEmailEditText.text!!.toString().trim { it <= ' ' }
        val password = mBinding!!.registerPasswordEditText.text!!.toString()
        val passwordConfirm = mBinding!!.registerPasswordConfirmEditText.text!!.toString()
        return if (isTextNotEmpty(name)
                && isTextNotEmpty(email)
                && isEmailValid(email)
                && isTextNotEmpty(password)
                && isTextNotEmpty(passwordConfirm)
                && password == passwordConfirm) {
            true
        } else {
            showError("Please make sure you inserted all the information correctly")
            false
        }
    }

    private fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isTextNotEmpty(input: String): Boolean {
        return !TextUtils.isEmpty(input)
    }

    private fun showProgressBar() {
        mBinding!!.registerGroup.visibility = View.INVISIBLE
        mBinding!!.loadingIndicator.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        mBinding!!.loadingIndicator.visibility = View.GONE
        mBinding!!.registerGroup.visibility = View.VISIBLE
    }

    companion object {

        private const val RC_SIGN_IN = 2019
        private const val TAG = "RegisterActivity"

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}
