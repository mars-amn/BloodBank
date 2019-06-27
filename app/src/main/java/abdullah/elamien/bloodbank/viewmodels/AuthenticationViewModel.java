package abdullah.elamien.bloodbank.viewmodels;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.models.AuthModel;
import abdullah.elamien.bloodbank.repository.AuthenticationRepository;

/**
 * Created by AbdullahAtta on 6/24/2019.
 */
public class AuthenticationViewModel extends ViewModel {

    private AuthenticationRepository mRepository;

    @Inject
    public AuthenticationViewModel(AuthenticationRepository mRepository) {
        this.mRepository = mRepository;
    }

    public void loginExistingUser(AuthModel model) {
        mRepository.loginUser(model);
    }

    public void registerFacebookUser(AuthCredential credential) {
        mRepository.registerFacebookUser(credential);
    }

    public void registerGoogleUser(Task<GoogleSignInAccount> task) {
        mRepository.registerGoogleUser(task);
    }

    public void registerNewUser(AuthModel authModel) {
        mRepository.registerNewUser(authModel);
    }
}
