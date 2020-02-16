package pl.mrozok.core.infrastructure

import pl.mrozok.core.infrastructure.Sensor.Type
import java.util.*

data class AccelerometerReading(
    override val accuracy: Reading.Accuracy = Reading.Accuracy.HIGH,
    override val takenAt: Date,
    val data: FloatArray
) : Reading {

    override val sensorName: String = Type.ACCELEROMETER
    override val dataDescription: String = "x:${x()}, y:${y()}, z:${z()}"

    fun x(): Float = data[0]

    fun y(): Float = data[1]

    fun z(): Float = data[2]

}
