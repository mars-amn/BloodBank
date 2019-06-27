package abdullah.elamien.bloodbank.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.R;


public class PreferenceUtils {
    private Context mContext;

    @Inject
    public PreferenceUtils(Context mContext) {
        this.mContext = mContext;
    }

    public static String getLanguageCode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.language_key), context.getString(R.string.arabic_language_value));
    }

    public boolean isNotificationsEnabled(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getBoolean(context.getString(R.string.notification_key), true);
    }
}
