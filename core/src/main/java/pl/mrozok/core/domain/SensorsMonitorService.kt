package pl.mrozok.core.domain

import pl.mrozok.core.infrastructure.Reading
import pl.mrozok.core.infrastructure.ReadingsPublisher
import pl.mrozok.core.infrastructure.Sensor

internal class SensorsMonitorService(
    private val readingsPublisher: ReadingsPublisher,
    private val sensors: Array<Sensor>
) : SensorsMonitorModule {

    private var onNewReadingAction: (() -> Unit)? = null

    private val readings: MutableList<Reading> = mutableListOf()
    private val readingsConsumer: (Reading) -> Unit = { reading ->
        readings.add(reading)
        onNewReadingAction?.invoke()
    }

    override fun start(onNewReadingAction: () -> Unit) {
        this.onNewReadingAction = onNewReadingAction
        sensors.forEach { sensor ->
            sensor.startMonitoring(readingsConsumer)
        }
    }

    override fun stop() {
        sensors.forEach { sensor ->
            sensor.stopMonitoring()
        }
    }

    override fun publishReadings() {
        readingsPublisher.publish(readings)
    }

    override fun clearReadings() {
        readings.clear()
    }

    override fun readings(): List<Reading> = readings
}
