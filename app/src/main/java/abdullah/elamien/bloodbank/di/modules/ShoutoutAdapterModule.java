package abdullah.elamien.bloodbank.di.modules;

import android.content.Context;

import abdullah.elamien.bloodbank.adapters.ShoutoutAdapter;
import abdullah.elamien.bloodbank.di.scopes.ActivityScope;
import dagger.Module;
import dagger.Provides;

/**
 * Created by AbdullahAtta on 6/18/2019.
 */
@Module
public class ShoutoutAdapterModule {
    private Context mContext;

    public ShoutoutAdapterModule(Context context) {
        this.mContext = context;
    }

    @Provides
    @ActivityScope
    ShoutoutAdapter provideShoutoutAdapter() {
        return new ShoutoutAdapter(mContext);
    }
}
