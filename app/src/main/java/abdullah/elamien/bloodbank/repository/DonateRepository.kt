package abdullah.elamien.bloodbank.repository

import abdullah.elamien.bloodbank.eventbus.DonateEvent
import abdullah.elamien.bloodbank.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import org.greenrobot.eventbus.EventBus
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by AbdullahAtta on 6/19/2019.
 */
class DonateRepository : KoinComponent {
    private val mFirestore by inject<FirebaseFirestore>()

    fun checkIfUserDonatedAndApplyIfNot(userId: String, user: Map<String, Any>) {
        val reference = mFirestore.collection(Constants.DONORS_COLLECTION_NAME)
        reference.whereEqualTo(Constants.DONORS_USER_UID, userId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val snapshots = task.result
                        if (snapshots!!.isEmpty) {
                            applyNewDonor(user)
                        } else {
                            postEvent("user exists")
                        }
                    }
                }
    }

    private fun applyNewDonor(user: Map<String, Any>) {
        mFirestore.collection(Constants.DONORS_COLLECTION_NAME)
                .document()
                .set(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        postEvent("success")
                    } else {
                        postEvent("fail")
                    }
                }
    }

    private fun postEvent(event: String) {
        EventBus.getDefault().post(DonateEvent(event))
    }
}
