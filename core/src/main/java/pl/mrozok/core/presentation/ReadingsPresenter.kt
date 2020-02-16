package pl.mrozok.core.presentation

import kotlinx.coroutines.*
import pl.mrozok.core.domain.SensorsMonitorException
import pl.mrozok.core.domain.SensorsMonitorModule
import pl.mrozok.core.infrastructure.Reading
import pl.mrozok.core.presentation.ReadingsScreen.ViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class ReadingsPresenter(
    private val sensorsMonitor: SensorsMonitorModule,
    private val backgroundWorkContext: CoroutineContext = Dispatchers.IO
) :
    ReadingsScreen.Presenter, CoroutineScope by MainScope() {

    private val simpleDateFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    private var shownReadings: List<Reading> = emptyList()
    private var sensorNameToFilter: String = ""
    private var pendingJob: Job? = null
    private var ui: ReadingsScreen.UI? = null

    override fun startListening() {
        sensorsMonitor.start(onNewReading())
    }

    private fun onNewReading(): () -> Unit = {
        var readings = sensorsMonitor.readings()
            .takeLast(100)
        if (sensorNameToFilter.isNotBlank())
            readings = filterReadingsBySensorName(readings, sensorNameToFilter)
        updateUiWithReadings(readings)
    }

    override fun stopListening() {
        sensorsMonitor.stop()
    }

    override fun applyFilter(sensorName: String) {
        sensorNameToFilter = sensorName
        val readings = filterReadingsBySensorName(shownReadings, sensorName)
        updateUiWithReadings(readings)
    }

    private fun filterReadingsBySensorName(readings: List<Reading>, sensorName: String): List<Reading> =
        readings.filter { reading -> sensorName == reading.sensorName }

    private fun updateUiWithReadings(readingsToShow: List<Reading>) {
        val readings = readingsToShow
            .map { reading -> "${reading.sensorName} ${formatTakenDate(reading.takenAt)}: ${reading.accuracy} ${reading.dataDescription}" }
        ui?.updateUI(ViewModel.Readings(readings))
        shownReadings = readingsToShow
    }

    private fun formatTakenDate(takenAt: Date): String = simpleDateFormat.format(takenAt)

    override fun clearReadings() {
        sensorsMonitor.clearReadings()
    }

    override fun sendToBackend() {
        pendingJob = launch(backgroundWorkContext) {
            try {
                sensorsMonitor.publishReadings()
                if (isActive.not()) return@launch
                ui?.updateUI(ViewModel.ReadingsPosted)
            } catch (exception: SensorsMonitorException) {
                if (isActive.not()) return@launch
                ui?.updateUI(ViewModel.Error(exception))
            }
        }
    }

    override fun cancelSending() {
        pendingJob ?: return
        pendingJob?.cancel()
        pendingJob = null
    }

    override fun attachUi(ui: ReadingsScreen.UI) {
        this.ui = ui
    }

}