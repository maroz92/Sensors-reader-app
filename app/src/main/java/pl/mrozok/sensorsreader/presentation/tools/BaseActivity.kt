package pl.mrozok.sensorsreader.presentation.tools

import androidx.appcompat.app.AppCompatActivity
import pl.mrozok.sensorsreader.SensorsReadingsApp
import pl.mrozok.sensorsreader.di.RootComponent

abstract class BaseActivity : AppCompatActivity() {

    protected fun rootComponent(): RootComponent = (application as SensorsReadingsApp).rootComponent

}