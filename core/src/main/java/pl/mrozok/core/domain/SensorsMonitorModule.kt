package pl.mrozok.core.domain

import pl.mrozok.core.infrastructure.Reading
import pl.mrozok.core.infrastructure.ReadingsPublisher
import pl.mrozok.core.infrastructure.Sensor

interface SensorsMonitorModule {

    companion object {
        fun create(configuration: Configuration): SensorsMonitorModule =
            SensorsMonitorService(configuration.readingsPublisher, configuration.sensors)
    }

    @Throws(SensorsMonitorException::class)
    fun start(onNewReadingAction: () -> Unit)

    @Throws(SensorsMonitorException::class)
    fun stop()

    fun readings(): List<Reading>

    @Throws(SensorsMonitorException::class)
    fun publishReadings()

    fun clearReadings()

    data class Configuration(
        val readingsPublisher: ReadingsPublisher,
        val sensors: Array<Sensor>
    )

}
