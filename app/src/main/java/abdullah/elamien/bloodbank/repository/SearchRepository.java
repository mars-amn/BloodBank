package abdullah.elamien.bloodbank.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.eventbus.SearchEvent;
import abdullah.elamien.bloodbank.models.Donor;
import abdullah.elamien.bloodbank.utils.Constants;

/**
 * Created by AbdullahAtta on 6/20/2019.
 */
public class SearchRepository {

    private FirebaseFirestore mFirestore;
    private MutableLiveData<List<Donor>> mDonors;

    @Inject
    public SearchRepository(FirebaseFirestore mFirestore) {
        this.mFirestore = mFirestore;
    }

    public LiveData<List<Donor>> searchForDonors(String city, String bloodType) {
        mDonors = new MutableLiveData<>();
        CollectionReference reference = mFirestore.collection(Constants.DONORS_COLLECTION_NAME);
        reference.whereEqualTo(Constants.DONORS_USER_ADDRESS,
                city).whereEqualTo(Constants.DONORS_USER_BLOOD_TYPE, bloodType)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Donor> donors = new ArrayList<>();
                for (QueryDocumentSnapshot doc :
                        task.getResult()) {
                    donors.add(doc.toObject(Donor.class));
                }
                mDonors.setValue(donors);
            } else {
                postEvent("fail");
            }
        });
        return mDonors;
    }

    private void postEvent(String event) {
        EventBus.getDefault().post(new SearchEvent(event));
    }
}
