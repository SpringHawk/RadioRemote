package martinzaubitzer.android.radioremote.networking

import io.reactivex.Observable
import martinzaubitzer.android.radioremote.networking.requestObjects.SearchRequestObject
import martinzaubitzer.android.radioremote.networking.responseObjects.Station
import retrofit2.http.Body
import retrofit2.http.POST

interface SearchRequest {
    @POST("search?token=" + ApiKey.API_KEY)
    fun getSearchResult(@Body body: SearchRequestObject): Observable<List<Station>>
}