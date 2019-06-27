package abdullah.elamien.bloodbank.di.components;

import abdullah.elamien.bloodbank.di.modules.ShoutoutAdapterModule;
import abdullah.elamien.bloodbank.di.scopes.ActivityScope;
import abdullah.elamien.bloodbank.ui.HomeActivity;
import dagger.Component;

/**
 * Created by AbdullahAtta on 6/18/2019.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = {
        ShoutoutAdapterModule.class})
public interface HomeComponent {
    void injectHomeActivity(HomeActivity activity);
}
