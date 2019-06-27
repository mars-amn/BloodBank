package abdullah.elamien.bloodbank.di.modules;

import android.content.Context;

import java.util.ArrayList;

import abdullah.elamien.bloodbank.adapters.SearchAdapter;
import abdullah.elamien.bloodbank.di.scopes.ActivityScope;
import dagger.Module;
import dagger.Provides;

/**
 * Created by AbdullahAtta on 6/20/2019.
 */
@Module
public class SearchAdapterModule {
    private Context mContext;

    public SearchAdapterModule(Context mContext) {
        this.mContext = mContext;
    }

    @Provides
    @ActivityScope
    SearchAdapter provideSearchAdapter() {
        return new SearchAdapter(mContext, new ArrayList<>());
    }
}
