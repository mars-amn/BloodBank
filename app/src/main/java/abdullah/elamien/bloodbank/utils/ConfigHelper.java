package abdullah.elamien.bloodbank.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public class ConfigHelper {
    public static void setLanguage(Resources resources, String languageCode) {
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        android.content.res.Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(languageCode.toLowerCase()));
        resources.updateConfiguration(configuration, displayMetrics);
    }
}
