package pl.mrozok.core.presentation

interface ReadingsScreen {

    interface Presenter {
        fun startListening()
        fun stopListening()
        fun applyFilter(sensorName: String)
        fun clearReadings()
        fun sendToBackend()
        fun cancelSending()
        fun attachUi(ui: UI)
    }

    interface UI {
        fun updateUI(viewModel: ViewModel)
    }

    sealed class ViewModel {
        data class Readings(val data: List<String>) : ViewModel()
        object ReadingsPosted : ViewModel()
        data class Error(val exception: Throwable) : ViewModel()
    }

}
