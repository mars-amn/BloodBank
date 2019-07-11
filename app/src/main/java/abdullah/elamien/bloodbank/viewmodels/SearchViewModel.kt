package abdullah.elamien.bloodbank.viewmodels

import abdullah.elamien.bloodbank.models.Donor
import abdullah.elamien.bloodbank.repository.SearchRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by AbdullahAtta on 6/20/2019.
 */
class SearchViewModel : ViewModel(), KoinComponent {
    private val mRepository: SearchRepository by inject()

    fun getSearchResult(city: String, bloodType: String): LiveData<List<Donor>> {
        return mRepository.searchForDonors(city, bloodType)
    }
}
