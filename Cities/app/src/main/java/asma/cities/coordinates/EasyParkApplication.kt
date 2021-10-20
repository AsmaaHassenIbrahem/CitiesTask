package asma.cities.coordinates

import android.app.Application
import asma.cities.coordinates.di.appModule
import asma.cities.coordinates.di.repoModule
import asma.cities.coordinates.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class EasyParkApplication : Application() {

    init {
        instance = this@EasyParkApplication
    }

    companion object {
        private var instance: EasyParkApplication? = null

        fun applicationContext(): EasyParkApplication {
            return instance as EasyParkApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
            startKoin {
                androidLogger(Level.ERROR) // Or any other level I guess
                androidContext(this@EasyParkApplication)
                androidFileProperties()
                modules(listOf(appModule, repoModule, viewModelModule))
            }
    }
}
