package abdullah.elamien.bloodbank.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import abdullah.elamien.bloodbank.databinding.ActivityLoginBinding;
import abdullah.elamien.bloodbank.di.components.ActivityComponent;
import abdullah.elamien.bloodbank.di.components.DaggerActivityComponent;
import abdullah.elamien.bloodbank.eventbus.AuthenticationEvent;
import abdullah.elamien.bloodbank.models.AuthModel;
import abdullah.elamien.bloodbank.viewmodels.AuthenticationViewModel;
import abdullah.elamien.bloodbank.viewmodels.factory.ViewModelFactory;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 2019;

    @Inject
    FirebaseAuth mAuth;
    @Inject
    ViewModelFactory mViewModelFactory;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityLoginBinding mBinding;
    private AuthenticationViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mBinding.setHandlers(this);
        initDagger();
        initViewModel();
        setupToolbar();
        setupNewMembershipLabel();
        setupFacebookLogin();
        setupGoogleLogin();
        registerEventBus();
    }

    private void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(AuthenticationViewModel.class);
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
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(getString(R.string.login_activity_label));
        }
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_nav_back_white);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterEventBus();
    }

    private void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    private void setupGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_api_key))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setupFacebookLogin() {
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
                showError(getString(R.string.error_msg));
            }
        });
    }

    private void registerFacebookUser(AccessToken token) {
        mViewModel.registerFacebookUser(FacebookAuthProvider.getCredential(token.getToken()));
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

    @Subscribe
    public void onEvent(AuthenticationEvent event) {
        hideProgressBar();
        if (event.getAuthenticationEventMessage().toLowerCase().equals("success")) {
            startHomeActivity();
        } else if (event.getAuthenticationEventMessage().toLowerCase().equals("fail")) {
            showError(getString(R.string.error_msg));
        }
    }

    private void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void setupNewMembershipLabel() {
        SpannableString spannableString = new SpannableString(getString(R.string.new_membership_label));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startRegisterActivity();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        spannableString.setSpan(clickableSpan, 23, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.newMembershipLabel.setText(spannableString);
        mBinding.newMembershipLabel.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }


    public void onLoginButtonClick(View view) {
        if (allInputsAreValid()) {
            loginUser();
        } else {
            showError(getString(R.string.error_msg));
        }
    }

    private void loginUser() {
        showProgressBar();
        mViewModel.loginExistingUser(getAuthModel());
    }

    private AuthModel getAuthModel() {
        return new AuthModel(mBinding.loginEmailEditText.getText().toString(),
                mBinding.loginPasswordEditText.getText().toString());
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean allInputsAreValid() {
        String email = mBinding.loginEmailEditText.getText().toString().trim();
        String password = mBinding.loginPasswordEditText.getText().toString();

        if (isTextNotEmpty(email)
                && isEmailValid(email)
                && isTextNotEmpty(password)) {
            return true;
        } else {
            showError("Please type in valid inputs");
            return false;
        }
    }

    public boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isTextNotEmpty(String input) {
        return !TextUtils.isEmpty(input);
    }

    public void onSinInWithGoogleClick(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void showProgressBar() {
        mBinding.loginGroup.setVisibility(View.INVISIBLE);
        mBinding.loadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mBinding.loadingIndicator.setVisibility(View.GONE);
        mBinding.loginGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startRegisterActivity();
        }
        return super.onOptionsItemSelected(item);
    }

}
