package pl.mrozok.core.infrastructure

import java.util.*

interface Reading {
    val accuracy: Accuracy
    val takenAt: Date
    val sensorName: String
    val dataDescription: String

    enum class Accuracy {
        HIGH,
        MEDIUM,
        LOW,
        UNRELIABLE,
        UNKNOWN
    }

}
