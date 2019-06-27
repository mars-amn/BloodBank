package abdullah.elamien.bloodbank.di.modules;

import android.content.Context;

import abdullah.elamien.bloodbank.utils.PreferenceUtils;
import dagger.Module;
import dagger.Provides;

/**
 * Created by AbdullahAtta on 6/23/2019.
 */
@Module
public class PreferenceUtilsModule {

    private Context mContext;

    public PreferenceUtilsModule(Context mContext) {
        this.mContext = mContext;
    }

    @Provides
    PreferenceUtils providePreferenceUtils() {
        return new PreferenceUtils(mContext);
    }
}
