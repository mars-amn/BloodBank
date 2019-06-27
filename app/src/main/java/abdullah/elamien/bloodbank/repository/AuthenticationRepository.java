package abdullah.elamien.bloodbank.repository;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.eventbus.AuthenticationEvent;
import abdullah.elamien.bloodbank.models.AuthModel;

/**
 * Created by AbdullahAtta on 6/24/2019.
 */
public class AuthenticationRepository {
    private static final String TAG = "AuthenticationRepositor";
    private FirebaseAuth mAuth;

    @Inject
    public AuthenticationRepository(FirebaseAuth auth) {
        this.mAuth = auth;
    }

    public void loginUser(AuthModel model) {
        mAuth.signInWithEmailAndPassword(model.getEmail(), model.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        postEvent("success");
                    } else {
                        postEvent("fail");
                        logger(task.getException().toString());
                    }
                });
    }

    public void registerFacebookUser(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        postEvent("success");
                    } else {
                        postEvent("fail");
                        logger(task.getException().toString());
                    }
                });
    }

    public void registerGoogleUser(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            postEvent("fail");
            logger(e.toString());
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        postEvent("success");
                    } else {
                        logger(task.getException().toString());
                        postEvent("fail");
                    }
                });
    }

    public void registerNewUser(AuthModel authModel) {
        mAuth.createUserWithEmailAndPassword(authModel.getEmail(), authModel.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        addNameToUser(authModel.getName());
                    } else {
                        postEvent("fail");
                        logger(task.getException().toString());
                    }
                });
    }

    private void addNameToUser(String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest updates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
        user.updateProfile(updates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        postEvent("success");
                    } else {
                        postEvent("fail");
                        logger(task.getException().toString());
                    }
                });
    }

    private void postEvent(String event) {
        EventBus.getDefault().post(new AuthenticationEvent(event));
    }

    private void logger(String msg) {
        Log.d(TAG, "logger: " + msg);
    }


}
