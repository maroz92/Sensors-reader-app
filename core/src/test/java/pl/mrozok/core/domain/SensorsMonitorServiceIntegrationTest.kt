package pl.mrozok.core.domain

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pl.mrozok.core.domain.SensorsMonitorModule.Configuration
import pl.mrozok.core.infrastructure.Reading
import pl.mrozok.core.infrastructure.ReadingsPublisher
import pl.mrozok.core.presentation.ReadingsPresenter
import pl.mrozok.core.presentation.ReadingsScreen
import pl.mrozok.core.presentation.ReadingsScreen.ViewModel
import pl.mrozok.core.testingtools.MockedSensor
import java.util.*

class SensorsMonitorServiceIntegrationTest {

    @Test
    fun `should update ui when new reading saved`() {
        val ui: ReadingsScreen.UI = mock()
        val readingsPublisher: ReadingsPublisher = mock()
        val sensor = MockedSensor()
        val sensorsMonitor =
            SensorsMonitorModule.create(Configuration(readingsPublisher, arrayOf(sensor)))
        val readingsPresenter = ReadingsPresenter(sensorsMonitor)

        readingsPresenter.attachUi(ui)
        readingsPresenter.startListening()
        sensor.simulateReadingCaptured(MockReading("one"))

        verify(ui).updateUI(ViewModel.Readings(listOf("MockReading 13:00:54.114: HIGH one")))
    }

    @Test
    fun `should display first 100 readings`() {
        val ui: ReadingsScreen.UI = mock()
        val readingsPublisher: ReadingsPublisher = mock()
        val sensor = MockedSensor()
        val sensorsMonitor =
            SensorsMonitorModule.create(Configuration(readingsPublisher, arrayOf(sensor)))
        val readingsPresenter = ReadingsPresenter(sensorsMonitor)

        readingsPresenter.attachUi(ui)
        readingsPresenter.startListening()
        (1..106).forEach { index ->
            sensor.simulateReadingCaptured(MockReading(index.toString()))
        }

        argumentCaptor<ViewModel>().run {
            verify(ui, times(106)).updateUI(capture())
            assertThat(lastValue).isInstanceOf(ViewModel.Readings::class.java)
            val readings = lastValue as ViewModel.Readings
            assertThat(readings.data).hasSize(100)
        }
    }

    @Test
    fun `should filter out new readings`() {
        val ui: ReadingsScreen.UI = mock()
        val readingsPublisher: ReadingsPublisher = mock()
        val sensor = MockedSensor()
        val sensorsMonitor =
            SensorsMonitorModule.create(Configuration(readingsPublisher, arrayOf(sensor)))
        val readingsPresenter = ReadingsPresenter(sensorsMonitor)

        readingsPresenter.attachUi(ui)
        readingsPresenter.startListening()
        readingsPresenter.applyFilter("Sensor2")
        sensor.simulateReadingCaptured(MockReading("One", "Sensor1"))

        verify(ui, times(2)).updateUI(ViewModel.Readings(emptyList()))
    }

    @Test
    fun `should filter out shown readings`() {
        val ui: ReadingsScreen.UI = mock()
        val readingsPublisher: ReadingsPublisher = mock()
        val sensor = MockedSensor()
        val sensorsMonitor =
            SensorsMonitorModule.create(Configuration(readingsPublisher, arrayOf(sensor)))
        val readingsPresenter = ReadingsPresenter(sensorsMonitor)

        readingsPresenter.attachUi(ui)
        readingsPresenter.startListening()
        sensor.simulateReadingCaptured(MockReading("One", "Sensor1"))
        sensor.simulateReadingCaptured(MockReading("Two", "Sensor2"))

        readingsPresenter.applyFilter("Sensor2")

        verify(ui).updateUI(ViewModel.Readings(listOf("Sensor2 13:00:54.114: HIGH Two")))
    }

    class MockReading(
        private val value: String,
        override val sensorName: String = "MockReading"
    ) : Reading {
        override val accuracy = Reading.Accuracy.HIGH
        override val takenAt = Date(1581681654114)
        override fun toString(): String = value
        override val dataDescription: String = value
    }

}
