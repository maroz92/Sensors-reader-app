package pl.mrozok.core.infrastructure

import pl.mrozok.core.domain.SensorsMonitorException

interface Sensor {

    object Type {
        const val ACCELEROMETER = "Accelerometer"
        const val SCREEN_TOUCH = "ScreenTouch"
    }

    @Throws(SensorsMonitorException::class)
    fun startMonitoring(readingConsumer: (Reading) -> Unit)

    fun stopMonitoring()

}
