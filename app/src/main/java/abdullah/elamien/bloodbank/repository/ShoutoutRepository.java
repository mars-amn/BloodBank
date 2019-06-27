package abdullah.elamien.bloodbank.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.eventbus.ShoutoutCreationEvent;
import abdullah.elamien.bloodbank.models.Shoutout;
import abdullah.elamien.bloodbank.utils.Constants;

/**
 * Created by AbdullahAtta on 6/18/2019.
 */
public class ShoutoutRepository {
    private static final String TAG = "ShoutoutRepository";
    private FirebaseFirestore mFirestore;
    private MutableLiveData<List<Shoutout>> mShoutouts;


    @Inject
    public ShoutoutRepository(FirebaseFirestore firebaseFirestore) {
        mFirestore = firebaseFirestore;
        if (mShoutouts == null) {
            mShoutouts = new MutableLiveData<>();
            loadShoutouts();
        }
    }

    public void createShoutout(Map<String, Object> shoutout) {
        mFirestore.collection(Constants.SHOUTOUTS_COLLECTION_NAME)
                .document()
                .set(shoutout)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        postEvent("success");
                    } else {
                        postEvent("fail");
                        logger(task.getException().getMessage());
                    }
                });
    }

    private void postEvent(String event) {
        EventBus.getDefault().post(new ShoutoutCreationEvent(event));
    }

    private void loadShoutouts() {
        mFirestore.collection(Constants.SHOUTOUTS_COLLECTION_NAME)
                .orderBy(Constants.SHOUTOUT_TIME, Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        logger("listening failed!");
                        return;
                    }
                    List<Shoutout> shoutoutsList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc :
                            queryDocumentSnapshots) {
                        shoutoutsList.add(doc.toObject(Shoutout.class));
                    }
                    mShoutouts.setValue(shoutoutsList);
                });
    }

    public MutableLiveData<List<Shoutout>> getShoutouts() {
        return mShoutouts;
    }

    private void logger(String s) {
        Log.d(TAG, "logger: " + s);
    }
}
