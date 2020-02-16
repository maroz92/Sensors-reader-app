package pl.mrozok.sensorsreader

import android.app.Application
import pl.mrozok.sensorsreader.di.GraphBuilder
import pl.mrozok.sensorsreader.di.RootComponent

class SensorsReadingsApp : Application() {

    lateinit var rootComponent: RootComponent
        private set

    override fun onCreate() {
        super.onCreate()

        rootComponent = GraphBuilder.build(this)
    }
}
