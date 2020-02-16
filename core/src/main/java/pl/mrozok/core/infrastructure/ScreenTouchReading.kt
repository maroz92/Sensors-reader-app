package pl.mrozok.core.infrastructure

import pl.mrozok.core.infrastructure.Sensor.Type
import java.util.*

class ScreenTouchReading(
    override val takenAt: Date,
    val x: Float,
    val y: Float
) : Reading {

    override val sensorName: String = Type.SCREEN_TOUCH
    override val accuracy: Reading.Accuracy = Reading.Accuracy.HIGH
    override val dataDescription: String = "x:$x, y:$y"

}
