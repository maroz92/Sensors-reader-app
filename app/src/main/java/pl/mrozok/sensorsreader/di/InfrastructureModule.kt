package pl.mrozok.sensorsreader.di

import android.content.Context
import android.hardware.SensorManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import pl.mrozok.core.infrastructure.ReadingsPublisher
import pl.mrozok.sensorsreader.infrastructure.NetworkReadingsPublisher
import pl.mrozok.sensorsreader.infrastructure.api.RetrofitInstance
import pl.mrozok.sensorsreader.infrastructure.api.SensorsAPI
import retrofit2.Retrofit
import java.io.File
import javax.inject.Singleton

@Module
class InfrastructureModule(private val context: Context) {

    @Singleton
    @Provides
    fun readingsPublisher(
        sensorsAPI: SensorsAPI,
        gson: Gson,
        file: File
    ): ReadingsPublisher =
        NetworkReadingsPublisher(sensorsAPI, gson, file)

    @Singleton
    @Provides
    fun retrofit(): Retrofit = RetrofitInstance.provide()

    @Provides
    fun sensorsApi(retrofit: Retrofit): SensorsAPI =
        retrofit.create(SensorsAPI::class.java)

    @Provides
    fun sensorManager(): SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    @Provides
    fun logFile(): File =
        File(context.getExternalFilesDir(null), "readings.log")

    @Provides
    fun gson(): Gson = Gson()

}
