package abdullah.elamien.bloodbank.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.R;
import abdullah.elamien.bloodbank.databinding.ActivityRegisterBinding;
import abdullah.elamien.bloodbank.di.components.ActivityComponent;
import abdullah.elamien.bloodbank.di.components.DaggerActivityComponent;
import abdullah.elamien.bloodbank.eventbus.AuthenticationEvent;
import abdullah.elamien.bloodbank.models.AuthModel;
import abdullah.elamien.bloodbank.viewmodels.AuthenticationViewModel;
import abdullah.elamien.bloodbank.viewmodels.factory.ViewModelFactory;

public class RegisterActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 2019;
    private static final String TAG = "RegisterActivity";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    @Inject
    FirebaseAuth mAuth;
    @Inject
    ViewModelFactory mViewModelFactory;


    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityRegisterBinding mBinding;
    private AuthenticationViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        mBinding.setHandlers(this);
        initDagger();
        if (mAuth.getCurrentUser() != null) {
            startHomeActivity();
        } else {
            initViewModel();
            registerEventBus();
            setupToolbar();
            setupExistedMembershipLabel();
            setupFacebookRegister();
            setupGoogleClient();
        }
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(AuthenticationViewModel.class);
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


    private void initDagger() {
        ActivityComponent component = DaggerActivityComponent.builder()
                .appComponent(((App) getApplication()).getAppComponent())
                .build();
        component.inject(this);
    }

    private void setupToolbar() {
        setSupportActionBar(mBinding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.register_activity_label));
        }
    }

    private void setupGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_api_key))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    private void setupFacebookRegister() {
        mCallbackManager = CallbackManager.Factory.create();
        mBinding.facebookRegister.setReadPermissions("email", "public_profile");
        mBinding.facebookRegister.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                registerFacebookUser(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebookReg" + error.getMessage());
                Toast.makeText(RegisterActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerFacebookUser(AccessToken token) {
        mViewModel.registerFacebookUser(FacebookAuthProvider.getCredential(token.getToken()));
    }

    public void onGoogleSignInClick(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            showProgressBar();
            mViewModel.registerGoogleUser(GoogleSignIn.getSignedInAccountFromIntent(data));
        }
    }

    private void setupExistedMembershipLabel() {
        SpannableString spannableString = new SpannableString(getString(R.string.existed_membership_label));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startLoginActivity();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        spannableString.setSpan(clickableSpan, 17, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.newMembershipLabel.setText(spannableString);
        mBinding.newMembershipLabel.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    public void onRegisterButtonClick(View view) {
        if (inputsAreValid()) {
            registerNewUser();
        } else {
            showError(getString(R.string.error_msg));
        }
    }

    private void registerNewUser() {
        showProgressBar();
        AuthModel model = getAuthModel();
        mViewModel.registerNewUser(model);
    }

    private AuthModel getAuthModel() {
        return new AuthModel(mBinding.registerNameEditText.getText().toString(),
                mBinding.registerEmailEditText.getText().toString(),
                mBinding.registerPasswordConfirmEditText.getText().toString());
    }

    @Subscribe
    public void onEvent(AuthenticationEvent event) {
        hideProgressBar();
        if (event.getAuthenticationEventMessage().toLowerCase().equals("success")) {
            startHomeActivity();
        } else if (event.getAuthenticationEventMessage().toLowerCase().equals("fail")) {
            showError(getString(R.string.error_msg));
        }
    }


    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean inputsAreValid() {
        String name = mBinding.registerNameEditText.getText().toString();
        String email = mBinding.registerEmailEditText.getText().toString().trim();
        String password = mBinding.registerPasswordEditText.getText().toString();
        String passwordConfirm = mBinding.registerPasswordConfirmEditText.getText().toString();
        if (isTextNotEmpty(name)
                && isTextNotEmpty(email)
                && isEmailValid(email)
                && isTextNotEmpty(password)
                && isTextNotEmpty(passwordConfirm)
                && password.equals(passwordConfirm)) {
            return true;
        } else {
            showError("Please make sure you inserted all the information correctly");
            return false;
        }
    }

    private void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isTextNotEmpty(String input) {
        return !TextUtils.isEmpty(input);
    }

    private void showProgressBar() {
        mBinding.registerGroup.setVisibility(View.INVISIBLE);
        mBinding.loadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mBinding.loadingIndicator.setVisibility(View.GONE);
        mBinding.registerGroup.setVisibility(View.VISIBLE);
    }
}
