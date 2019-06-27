package abdullah.elamien.bloodbank.di.components;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import abdullah.elamien.bloodbank.di.modules.FirebaseModule;
import abdullah.elamien.bloodbank.di.modules.PreferenceUtilsModule;
import abdullah.elamien.bloodbank.di.modules.ViewModelModule;
import abdullah.elamien.bloodbank.di.scopes.ApplicationScope;
import abdullah.elamien.bloodbank.utils.PreferenceUtils;
import abdullah.elamien.bloodbank.viewmodels.factory.ViewModelFactory;
import dagger.Component;

/**
 * Created by AbdullahAtta on 6/19/2019.
 */
@ApplicationScope
@Component(modules = {FirebaseModule.class, ViewModelModule.class, PreferenceUtilsModule.class})
public interface AppComponent {
    FirebaseFirestore getFirestore();

    FirebaseAuth getFirebaseAuth();

    FirebaseUser getFirebaseUser();

    FirebaseMessaging getFirebaseMessaging();

    ViewModelFactory getViewModelFactory();

    PreferenceUtils getPreferenceUtils();
}
