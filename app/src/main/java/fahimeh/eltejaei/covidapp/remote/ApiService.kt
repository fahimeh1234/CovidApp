package fahimeh.eltejaei.covidapp.remote

import fahimeh.eltejaei.covidapp.model.ResponseSummary
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/summary")
    fun getSummery(): Call<ResponseSummary>
}