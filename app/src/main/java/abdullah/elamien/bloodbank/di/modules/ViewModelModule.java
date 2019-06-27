package abdullah.elamien.bloodbank.di.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import abdullah.elamien.bloodbank.repository.AuthenticationRepository;
import abdullah.elamien.bloodbank.repository.DonateRepository;
import abdullah.elamien.bloodbank.repository.SearchRepository;
import abdullah.elamien.bloodbank.repository.ShoutoutRepository;
import abdullah.elamien.bloodbank.viewmodels.AuthenticationViewModel;
import abdullah.elamien.bloodbank.viewmodels.DonateViewModel;
import abdullah.elamien.bloodbank.viewmodels.SearchViewModel;
import abdullah.elamien.bloodbank.viewmodels.ShoutoutsViewModel;
import abdullah.elamien.bloodbank.viewmodels.factory.ViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

/**
 * Created by AbdullahAtta on 6/18/2019.
 */
@Module
public abstract class ViewModelModule {
    @Provides
    static ShoutoutRepository provideShoutoutRepository(FirebaseFirestore firebaseFirestore) {
        return new ShoutoutRepository(firebaseFirestore);
    }

    @Provides
    static DonateRepository provideDonateRepository(FirebaseFirestore firebaseFirestore) {
        return new DonateRepository(firebaseFirestore);
    }

    @Provides
    static SearchRepository provideSearchRepository(FirebaseFirestore firebaseFirestore) {
        return new SearchRepository(firebaseFirestore);
    }

    @Provides
    static AuthenticationRepository provideAuthenticationRepository(FirebaseAuth firebaseAuth) {
        return new AuthenticationRepository(firebaseAuth);
    }

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(ShoutoutsViewModel.class)
    abstract ViewModel provideShoutoutsViewModel(ShoutoutsViewModel shoutoutsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DonateViewModel.class)
    abstract ViewModel bindsDonateViewModel(DonateViewModel donateViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    abstract ViewModel bindsSearchViewModel(SearchViewModel searchViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticationViewModel.class)
    abstract ViewModel providesAuthenticationViewModel(AuthenticationViewModel authenticationViewModel);
}
