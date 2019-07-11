package abdullah.elamien.bloodbank.viewmodels

import abdullah.elamien.bloodbank.models.Shoutout
import abdullah.elamien.bloodbank.repository.ShoutoutRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by AbdullahAtta on 6/18/2019.
 */
class ShoutoutsViewModel : ViewModel(), KoinComponent {
    private val mRepository: ShoutoutRepository by inject()

    val shoutouts: LiveData<List<Shoutout>>?
        get() = mRepository.shoutouts

    fun createShoutout(shoutout: Map<String, Any>) {
        mRepository.createShoutout(shoutout)
    }

}
