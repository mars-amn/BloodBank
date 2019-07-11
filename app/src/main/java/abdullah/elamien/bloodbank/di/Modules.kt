package abdullah.elamien.bloodbank.di

import abdullah.elamien.bloodbank.repository.AuthenticationRepository
import abdullah.elamien.bloodbank.repository.DonateRepository
import abdullah.elamien.bloodbank.repository.SearchRepository
import abdullah.elamien.bloodbank.repository.ShoutoutRepository
import abdullah.elamien.bloodbank.utils.PreferenceUtils
import abdullah.elamien.bloodbank.viewmodels.AuthenticationViewModel
import abdullah.elamien.bloodbank.viewmodels.DonateViewModel
import abdullah.elamien.bloodbank.viewmodels.SearchViewModel
import abdullah.elamien.bloodbank.viewmodels.ShoutoutsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by AbdullahAtta on 7/11/2019.
 */
val firebaseModule = module {

    factory { FirebaseAuth.getInstance().currentUser }
    single { FirebaseMessaging.getInstance() }
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
}

val viewModelModule = module {

    factory { AuthenticationRepository() }
    viewModel { AuthenticationViewModel() }

    factory { DonateRepository() }
    viewModel { DonateViewModel() }

    factory { SearchRepository() }
    viewModel { SearchViewModel() }

    factory { ShoutoutRepository() }
    viewModel { ShoutoutsViewModel() }
}

val preferenceModule = module {
    single { PreferenceUtils() }
}

