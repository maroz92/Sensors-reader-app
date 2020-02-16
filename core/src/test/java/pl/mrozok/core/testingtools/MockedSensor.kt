package pl.mrozok.core.testingtools

import pl.mrozok.core.infrastructure.Reading
import pl.mrozok.core.infrastructure.Sensor

class MockedSensor : Sensor {
    private lateinit var readingConsumer: (Reading) -> Unit

    override fun startMonitoring(readingConsumer: (Reading) -> Unit) {
        this.readingConsumer = readingConsumer
    }

    override fun stopMonitoring() {
        // not needed in mock implementation
    }

    fun simulateReadingCaptured(reading: Reading) {
        readingConsumer.invoke(reading)
    }
}
