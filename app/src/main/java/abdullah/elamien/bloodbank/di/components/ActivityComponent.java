package abdullah.elamien.bloodbank.di.components;

import abdullah.elamien.bloodbank.di.scopes.ActivityScope;
import abdullah.elamien.bloodbank.ui.LoginActivity;
import abdullah.elamien.bloodbank.ui.RegisterActivity;
import abdullah.elamien.bloodbank.ui.SettingsActivity;
import abdullah.elamien.bloodbank.ui.WelcomeActivity;
import dagger.Component;

/**
 * Created by AbdullahAtta on 6/23/2019.
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(SettingsActivity activity);

    void inject(WelcomeActivity welcomeActivity);

    void inject(RegisterActivity registerActivity);

    void inject(LoginActivity loginActivity);
}
