package abdullah.elamien.bloodbank.di.components;

import abdullah.elamien.bloodbank.di.modules.SearchAdapterModule;
import abdullah.elamien.bloodbank.di.scopes.ActivityScope;
import abdullah.elamien.bloodbank.ui.SearchActivity;
import dagger.Component;

/**
 * Created by AbdullahAtta on 6/20/2019.
 */

@ActivityScope
@Component(dependencies = AppComponent.class, modules = {SearchAdapterModule.class})
public interface SearchComponent {
    void inject(SearchActivity searchActivity);
}
