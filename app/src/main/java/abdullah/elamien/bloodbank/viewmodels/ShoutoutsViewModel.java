package abdullah.elamien.bloodbank.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.models.Shoutout;
import abdullah.elamien.bloodbank.repository.ShoutoutRepository;

/**
 * Created by AbdullahAtta on 6/18/2019.
 */
public class ShoutoutsViewModel extends ViewModel {

    private ShoutoutRepository mRepository;

    @Inject
    public ShoutoutsViewModel(ShoutoutRepository repository) {
        this.mRepository = repository;
    }

    public void createShoutout(Map<String, Object> shoutout) {
        mRepository.createShoutout(shoutout);
    }

    public LiveData<List<Shoutout>> getShoutouts() {
        return mRepository.getShoutouts();
    }

}
