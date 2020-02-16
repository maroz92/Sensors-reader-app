package pl.mrozok.sensorsreader.infrastructure

import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import pl.mrozok.core.domain.BaseSensor
import pl.mrozok.core.domain.SensorsMonitorException
import pl.mrozok.core.domain.SensorsMonitorException.Reason.SensorNotAvailable
import pl.mrozok.core.infrastructure.AccelerometerReading
import pl.mrozok.core.infrastructure.Reading
import pl.mrozok.core.infrastructure.Sensor
import java.util.*

class AccelerometerSensor(private val sensorManager: SensorManager) : BaseSensor() {

    private var lastReadingData: FloatArray = FloatArray(3) { 0F }

    private val sensorEventListener: SensorEventListener = object : SensorEventListener {

        override fun onAccuracyChanged(sensor: android.hardware.Sensor, accuracy: Int) {
            // not needed since accuracy can be accessed via SensorEvent
        }

        override fun onSensorChanged(event: SensorEvent) {
            if (lastReadingData.contentEquals(event.values).not()) {
                publishReading(mapToReading(event))
            }
            lastReadingData = event.values.copyOf()
        }
    }

    private fun mapToReading(event: SensorEvent): Reading =
        AccelerometerReading(mapAccuracy(event.accuracy), Date(), event.values)

    private fun mapAccuracy(accuracy: Int): Reading.Accuracy = when (accuracy) {
        SensorManager.SENSOR_STATUS_ACCURACY_LOW -> Reading.Accuracy.LOW
        SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> Reading.Accuracy.MEDIUM
        SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> Reading.Accuracy.HIGH
        SensorManager.SENSOR_STATUS_UNRELIABLE -> Reading.Accuracy.UNRELIABLE
        else -> Reading.Accuracy.UNKNOWN
    }

    @Throws(SensorsMonitorException::class)
    override fun startMonitoring(readingConsumer: (Reading) -> Unit) {
        super.startMonitoring(readingConsumer)
        val sensor = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER)
        sensor ?: throw SensorsMonitorException(SensorNotAvailable(Sensor.Type.ACCELEROMETER))
        sensorManager.registerListener(
            sensorEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun stopMonitoring() {
        sensorManager.unregisterListener(sensorEventListener)
    }

}
