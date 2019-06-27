package abdullah.elamien.bloodbank.ui;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import abdullah.elamien.bloodbank.R;


/**
 * Created by AbdullahAtta on 2/16/2019.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
    }
}
