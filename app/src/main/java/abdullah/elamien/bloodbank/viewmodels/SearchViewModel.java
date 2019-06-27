package abdullah.elamien.bloodbank.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.models.Donor;
import abdullah.elamien.bloodbank.repository.SearchRepository;

/**
 * Created by AbdullahAtta on 6/20/2019.
 */
public class SearchViewModel extends ViewModel {
    private SearchRepository mRepository;

    @Inject
    public SearchViewModel(SearchRepository mRepository) {
        this.mRepository = mRepository;
    }

    public LiveData<List<Donor>> getSearchResult(String city, String bloodType) {
        return mRepository.searchForDonors(city, bloodType);
    }
}
