package abdullah.elamien.bloodbank.viewmodels;


import androidx.lifecycle.ViewModel;

import java.util.Map;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.repository.DonateRepository;

/**
 * Created by AbdullahAtta on 6/19/2019.
 */
public class DonateViewModel extends ViewModel {
    private DonateRepository mRepository;

    @Inject
    public DonateViewModel(DonateRepository mRepository) {
        this.mRepository = mRepository;
    }

    public void applyNewDonor(Map<String, Object> donor, String userId) {
        mRepository.checkIfUserDonatedAndApplyIfNot(userId, donor);
    }

}
