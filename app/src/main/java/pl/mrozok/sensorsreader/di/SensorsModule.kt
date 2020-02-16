package pl.mrozok.sensorsreader.di

import android.hardware.SensorManager
import android.view.ViewGroup
import dagger.Module
import dagger.Provides
import pl.mrozok.core.domain.SensorsMonitorModule
import pl.mrozok.core.infrastructure.ReadingsPublisher
import pl.mrozok.sensorsreader.infrastructure.AccelerometerSensor
import pl.mrozok.sensorsreader.infrastructure.ScreenTouchSensor

@Module
class SensorsModule(private val view: ViewGroup) {

    @Provides
    fun accelerometerSensor(sensorManager: SensorManager): AccelerometerSensor =
        AccelerometerSensor(sensorManager)

    @Provides
    fun screenTouchSensor(): ScreenTouchSensor =
        ScreenTouchSensor(view)

    @Provides
    fun sensorsMonitorModule(
        readingsPublisher: ReadingsPublisher,
        accelerometerSensor: AccelerometerSensor,
        screenTouchSensor: ScreenTouchSensor
    ): SensorsMonitorModule =
        SensorsMonitorModule.create(
            SensorsMonitorModule.Configuration(
                readingsPublisher,
                arrayOf(accelerometerSensor, screenTouchSensor)
            )
        )
}
