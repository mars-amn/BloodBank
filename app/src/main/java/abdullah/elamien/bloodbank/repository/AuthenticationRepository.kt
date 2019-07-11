package abdullah.elamien.bloodbank.repository

import abdullah.elamien.bloodbank.eventbus.AuthenticationEvent
import abdullah.elamien.bloodbank.models.AuthModel
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import org.greenrobot.eventbus.EventBus
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by AbdullahAtta on 6/24/2019.
 */
class AuthenticationRepository : KoinComponent {
    private val mAuth by inject<FirebaseAuth>()

    fun loginUser(model: AuthModel) {
        mAuth.signInWithEmailAndPassword(model.email!!, model.password!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        postEvent("success")
                    } else {
                        postEvent("fail")
                        logger(task.exception!!.toString())
                    }
                }
    }

    fun registerFacebookUser(credential: AuthCredential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        postEvent("success")
                    } else {
                        postEvent("fail")
                        logger(task.exception!!.toString())
                    }
                }
    }

    fun registerGoogleUser(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account!!)
        } catch (e: ApiException) {
            postEvent("fail")
            logger(e.toString())
        }

    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        postEvent("success")
                    } else {
                        logger(task.exception!!.toString())
                        postEvent("fail")
                    }
                }
    }

    fun registerNewUser(authModel: AuthModel) {
        mAuth.createUserWithEmailAndPassword(authModel.email!!, authModel.password!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        addNameToUser(authModel.name)
                    } else {
                        postEvent("fail")
                        logger(task.exception!!.toString())
                    }
                }
    }

    private fun addNameToUser(name: String?) {
        val user = FirebaseAuth.getInstance().currentUser
        val updates = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        user!!.updateProfile(updates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        postEvent("success")
                    } else {
                        postEvent("fail")
                        logger(task.exception!!.toString())
                    }
                }
    }

    private fun postEvent(event: String) {
        EventBus.getDefault().post(AuthenticationEvent(event))
    }

    private fun logger(msg: String) {
        Log.d(TAG, "logger: $msg")
    }

    companion object {
        private const val TAG = "AuthenticationRepositor"
    }


}
