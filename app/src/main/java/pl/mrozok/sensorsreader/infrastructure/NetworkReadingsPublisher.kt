package pl.mrozok.sensorsreader.infrastructure

import android.util.Log
import com.google.gson.Gson
import pl.mrozok.core.domain.SensorsMonitorException
import pl.mrozok.core.domain.SensorsMonitorException.Reason
import pl.mrozok.core.infrastructure.Reading
import pl.mrozok.core.infrastructure.ReadingsPublisher
import pl.mrozok.sensorsreader.infrastructure.api.SensorsAPI
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkReadingsPublisher(
    private val sensorsAPI: SensorsAPI,
    private val gson: Gson,
    private val file: File
) : ReadingsPublisher {

    override fun publish(readings: List<Reading>) {
        writeToFile(readings)
        postToApi(readings)
    }

    private fun postToApi(readings: List<Reading>) {
        try {
            val response = sensorsAPI.postReadings(mapReadings(readings)).execute()
            if (response.isSuccessful.not()) {
                throw SensorsMonitorException(Reason.GenericWebAPIError)
            }
        } catch (exception: SocketTimeoutException) {
            throw SensorsMonitorException(Reason.CannotReachHost)
        } catch (exception: UnknownHostException) {
            throw SensorsMonitorException(Reason.CannotReachHost)
        }
    }

    private fun mapReadings(readings: List<Reading>): List<ReadingDTO> = readings.map { reading ->
        reading.run {
            ReadingDTO(
                accuracy.toString(),
                takenAt,
                sensorName,
                dataDescription
            )
        }
    }

    private fun writeToFile(readings: List<Reading>) {
        try {
            if (file.exists().not())
                file.createNewFile()
            val writer = FileWriter(file)
            readings.map { item -> gson.toJson(item) }
                .forEach { item -> writer.appendln(item) }
            writer.close()
        } catch (e: IOException) {
            Log.w("NetworkReadingsPublishe", "couldn't write readings to file, exception: $e")
        }
    }

}
