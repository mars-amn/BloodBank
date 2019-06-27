package abdullah.elamien.bloodbank.di.components;

import abdullah.elamien.bloodbank.di.scopes.ActivityScope;
import abdullah.elamien.bloodbank.ui.SplashActivity;
import dagger.Component;

/**
 * Created by AbdullahAtta on 6/23/2019.
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface SplashComponent {
    void inject(SplashActivity activity);
}
