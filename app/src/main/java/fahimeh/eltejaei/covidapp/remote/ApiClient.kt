package fahimeh.eltejaei.covidapp.remote

import fahimeh.eltejaei.covidapp.baseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    fun getClient(): ApiService = Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build().create(ApiService::class.java)
}