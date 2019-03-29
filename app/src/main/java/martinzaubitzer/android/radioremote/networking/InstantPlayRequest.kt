package martinzaubitzer.android.radioremote.networking

import retrofit2.http.GET
import retrofit2.http.Path

interface InstantPlayRequest {
    @GET("/{PARAMETER}")
    fun playStation(@Path("command") stationUrl: String)
}