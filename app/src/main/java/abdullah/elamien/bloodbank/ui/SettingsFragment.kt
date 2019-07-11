package abdullah.elamien.bloodbank.ui

import abdullah.elamien.bloodbank.R
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat


/**
 * Created by AbdullahAtta on 2/16/2019.
 */
class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
    }
}
