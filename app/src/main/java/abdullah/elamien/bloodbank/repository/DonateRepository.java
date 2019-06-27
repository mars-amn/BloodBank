package abdullah.elamien.bloodbank.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.eventbus.DonateEvent;
import abdullah.elamien.bloodbank.utils.Constants;

/**
 * Created by AbdullahAtta on 6/19/2019.
 */
public class DonateRepository {

    private FirebaseFirestore mFirestore;

    @Inject
    public DonateRepository(FirebaseFirestore mFirestore) {
        this.mFirestore = mFirestore;
    }

    public void checkIfUserDonatedAndApplyIfNot(String userId, Map<String, Object> user) {
        CollectionReference reference = mFirestore.collection(Constants.DONORS_COLLECTION_NAME);
        reference.whereEqualTo(Constants.DONORS_USER_UID, userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshots = task.getResult();
                        if (snapshots.isEmpty()) {
                            applyNewDonor(user);
                        } else {
                            postEvent("user exists");
                        }
                    }
                });
    }

    private void applyNewDonor(Map<String, Object> user) {
        mFirestore.collection(Constants.DONORS_COLLECTION_NAME)
                .document()
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        postEvent("success");
                    } else {
                        postEvent("fail");
                    }
                });
    }

    private void postEvent(String event) {
        EventBus.getDefault().post(new DonateEvent(event));
    }
}
