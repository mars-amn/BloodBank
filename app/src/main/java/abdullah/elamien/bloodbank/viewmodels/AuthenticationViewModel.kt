package abdullah.elamien.bloodbank.viewmodels

import abdullah.elamien.bloodbank.models.AuthModel
import abdullah.elamien.bloodbank.repository.AuthenticationRepository
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by AbdullahAtta on 6/24/2019.
 */
class AuthenticationViewModel : ViewModel(), KoinComponent {
    private val mRepository: AuthenticationRepository by inject()

    fun loginExistingUser(model: AuthModel) {
        mRepository.loginUser(model)
    }

    fun registerFacebookUser(credential: AuthCredential) {
        mRepository.registerFacebookUser(credential)
    }

    fun registerGoogleUser(task: Task<GoogleSignInAccount>) {
        mRepository.registerGoogleUser(task)
    }

    fun registerNewUser(authModel: AuthModel) {
        mRepository.registerNewUser(authModel)
    }
}
