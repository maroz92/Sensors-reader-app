package pl.mrozok.core.domain

import pl.mrozok.core.infrastructure.Reading
import pl.mrozok.core.infrastructure.Sensor

abstract class BaseSensor : Sensor {

    private var readingConsumer: (Reading) -> Unit = {}

    protected fun publishReading(reading: Reading) {
        readingConsumer.invoke(reading)
    }

    @Throws(SensorsMonitorException::class)
    override fun startMonitoring(readingConsumer: (Reading) -> Unit) {
        this.readingConsumer = readingConsumer
    }

}
