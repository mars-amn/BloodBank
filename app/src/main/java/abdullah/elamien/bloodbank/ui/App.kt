package abdullah.elamien.bloodbank.ui


import abdullah.elamien.bloodbank.di.firebaseModule
import abdullah.elamien.bloodbank.di.preferenceModule
import abdullah.elamien.bloodbank.di.viewModelModule
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Created by AbdullahAtta on 6/19/2019.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(listOf(
                    firebaseModule,
                    viewModelModule,
                    preferenceModule))
        }
    }
}

