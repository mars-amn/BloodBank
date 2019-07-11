package abdullah.elamien.bloodbank.repository

import abdullah.elamien.bloodbank.eventbus.SearchEvent
import abdullah.elamien.bloodbank.models.Donor
import abdullah.elamien.bloodbank.utils.Constants
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import org.greenrobot.eventbus.EventBus
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

/**
 * Created by AbdullahAtta on 6/20/2019.
 */
class SearchRepository : KoinComponent {
    private val mFirestore by inject<FirebaseFirestore>()
    private var mDonors: MutableLiveData<List<Donor>>? = null

    fun searchForDonors(city: String, bloodType: String): LiveData<List<Donor>> {
        mDonors = MutableLiveData()
        val reference = mFirestore.collection(Constants.DONORS_COLLECTION_NAME)
        reference.whereEqualTo(Constants.DONORS_USER_ADDRESS,
                city).whereEqualTo(Constants.DONORS_USER_BLOOD_TYPE, bloodType)
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val donors = ArrayList<Donor>()
                        for (doc in task.result!!) {
                            donors.add(doc.toObject(Donor::class.java))
                        }
                        mDonors!!.setValue(donors)
                    } else {
                        postEvent("fail")
                    }
                }
        return mDonors as MutableLiveData<List<Donor>>
    }

    private fun postEvent(event: String) {
        EventBus.getDefault().post(SearchEvent(event))
    }
}
