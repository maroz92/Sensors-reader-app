package pl.mrozok.sensorsreader.infrastructure.api

import pl.mrozok.sensorsreader.infrastructure.ReadingDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SensorsAPI {
    @POST("post")
    fun postReadings(@Body readings: List<ReadingDTO>): Call<Any>
}
