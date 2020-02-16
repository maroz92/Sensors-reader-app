package pl.mrozok.core.domain

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pl.mrozok.core.domain.SensorsMonitorModule.Configuration
import pl.mrozok.core.infrastructure.Reading
import pl.mrozok.core.infrastructure.ReadingsPublisher
import pl.mrozok.core.infrastructure.Sensor
import pl.mrozok.core.testingtools.MockedSensor

class SensorsMonitorServiceTest {

    private val readingsPublisher: ReadingsPublisher = mock()
    private lateinit var systemUnderTest: SensorsMonitorModule

    @Test
    fun `should monitoring given sensor`() {
        val sensor: Sensor = mock()
        givenSensorMonitorModule(sensor)

        systemUnderTest.start {}

        verify(sensor).startMonitoring(any())
    }

    @Test
    fun `should stop monitoring given sensor`() {
        val sensor: Sensor = mock()
        givenSensorMonitorModule(sensor)

        systemUnderTest.start {}
        systemUnderTest.stop()

        verify(sensor).stopMonitoring()
    }

    @Test(expected = SensorsMonitorException::class)
    fun `should throw error if sensor couldn't start`() {
        val sensor: Sensor = mock()
        whenever(sensor.startMonitoring(any())).thenThrow(SensorsMonitorException::class.java)
        givenSensorMonitorModule(sensor)

        systemUnderTest.start {}
    }

    @Test
    fun `should record occurred events`() {
        val sensor = MockedSensor()
        givenSensorMonitorModule(sensor)

        systemUnderTest.start {}
        val reading: Reading = mock()
        sensor.simulateReadingCaptured(reading)

        val readings = systemUnderTest.readings()

        assertThat(readings).contains(reading)
    }

    @Test
    fun `should publish recorded readings`() {
        val sensor = MockedSensor()
        givenSensorMonitorModule(sensor)

        systemUnderTest.start {}

        val firstReading: Reading = mock()
        sensor.simulateReadingCaptured(firstReading)
        val secondReading: Reading = mock()
        sensor.simulateReadingCaptured(secondReading)

        systemUnderTest.publishReadings()

        verify(readingsPublisher).publish(listOf(firstReading, secondReading))
    }

    @Test
    fun `should clear recorded readings`() {
        val sensor = MockedSensor()
        givenSensorMonitorModule(sensor)

        systemUnderTest.start {}

        val firstReading: Reading = mock()
        sensor.simulateReadingCaptured(firstReading)

        systemUnderTest.clearReadings()
        systemUnderTest.publishReadings()

        verify(readingsPublisher).publish(emptyList())
    }

    private fun givenSensorMonitorModule(sensor: Sensor) {
        systemUnderTest =
            SensorsMonitorModule.create(Configuration(readingsPublisher, arrayOf(sensor)))
    }

}
