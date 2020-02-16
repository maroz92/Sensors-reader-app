package pl.mrozok.core.presentation

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import org.junit.Test
import pl.mrozok.core.domain.SensorsMonitorModule

class ReadingsPresenterTest {

    private val sensorsMonitor: SensorsMonitorModule = mock()
    private lateinit var systemUnderTest: ReadingsPresenter

    @Test
    fun `should start listening`() {
        givenReadingsPresenter(sensorsMonitor)

        systemUnderTest.startListening()

        verify(sensorsMonitor).start(any())
    }

    @Test
    fun `should stop listening`() {
        givenReadingsPresenter(sensorsMonitor)

        systemUnderTest.stopListening()

        verify(sensorsMonitor).stop()
    }

    @Test
    fun `should publish readings`() {
        givenReadingsPresenter(sensorsMonitor)

        systemUnderTest.sendToBackend()

        verify(sensorsMonitor).publishReadings()
    }

    @Test
    fun `should clear readings`() {
        givenReadingsPresenter(sensorsMonitor)

        systemUnderTest.clearReadings()

        verify(sensorsMonitor).clearReadings()
    }

    private fun givenReadingsPresenter(sensorsMonitor: SensorsMonitorModule) {
        systemUnderTest = ReadingsPresenter(sensorsMonitor, Dispatchers.Unconfined)
    }

}
