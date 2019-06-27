package abdullah.elamien.bloodbank.di.components;

import abdullah.elamien.bloodbank.di.scopes.ActivityScope;
import abdullah.elamien.bloodbank.ui.ShoutoutCreationActivity;
import dagger.Component;

/**
 * Created by AbdullahAtta on 6/19/2019.
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ShoutoutCreationComponent {
    void inject(ShoutoutCreationActivity activity);
}
