package abdullah.elamien.bloodbank.viewmodels


import abdullah.elamien.bloodbank.repository.DonateRepository
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by AbdullahAtta on 6/19/2019.
 */
class DonateViewModel : ViewModel(), KoinComponent {
    private val mRepository: DonateRepository by inject()

    fun applyNewDonor(donor: Map<String, Any>, userId: String) {
        mRepository.checkIfUserDonatedAndApplyIfNot(userId, donor)
    }

}
