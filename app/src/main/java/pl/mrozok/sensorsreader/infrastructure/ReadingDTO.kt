package pl.mrozok.sensorsreader.infrastructure

import java.util.*

data class ReadingDTO(
    val accuracy: String,
    val takenAt: Date,
    val sensorName: String,
    val data: String
)
