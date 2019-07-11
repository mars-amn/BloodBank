package abdullah.elamien.bloodbank.utils

import android.content.res.Resources
import java.util.*

object ConfigHelper {
    fun setLanguage(resources: Resources, languageCode: String) {
        val displayMetrics = resources.displayMetrics
        val configuration = resources.configuration
        configuration.setLocale(Locale(languageCode.toLowerCase()))
        resources.updateConfiguration(configuration, displayMetrics)
    }
}
