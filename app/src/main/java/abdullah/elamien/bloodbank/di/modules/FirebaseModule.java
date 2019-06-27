package abdullah.elamien.bloodbank.di.modules;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import dagger.Module;
import dagger.Provides;

/**
 * Created by AbdullahAtta on 6/18/2019.
 */
@Module
public class FirebaseModule {
    @Provides
    static FirebaseFirestore provideFirebaseFirestore() {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    static FirebaseUser provideUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Provides
    static FirebaseMessaging getFirebaseMessaging() {
        return FirebaseMessaging.getInstance();
    }

    @Provides
    static FirebaseAuth provideAuth() {
        return FirebaseAuth.getInstance();
    }
}
