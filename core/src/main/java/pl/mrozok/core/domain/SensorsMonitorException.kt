package pl.mrozok.core.domain

class SensorsMonitorException(val reason: Reason) : Throwable() {

    sealed class Reason(val details: String) {
        object Unknown : Reason("")
        object CannotReachHost : Reason("Couldn't reach host")
        object GenericWebAPIError : Reason("Unknown api error")
        class SensorNotAvailable(sensorName: String) : Reason("Couldn't find sensor $sensorName")
    }

}
