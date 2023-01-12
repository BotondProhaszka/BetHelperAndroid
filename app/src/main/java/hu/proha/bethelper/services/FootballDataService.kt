package hu.proha.bethelper.services

import hu.proha.bethelper.data.Match
import hu.proha.bethelper.data.MatchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface FootballDataService {
    @Headers("X-Auth-Token: e97208bf1c2e45f981e12544883432c7")
    @GET("competitions/CL/matches")
    suspend fun getMatches(@Query("status") status: String): MatchResponse

    fun getMatchesByCountry(@Header("X-Auth-Token") apiKey: String,
                            @Query("country") country: String): Call<MatchResponse>

}


class FootballDataRepository {

    private val apiKey = "e97208bf1c2e45f981e12544883432c7"

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://api.football-data.org/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(FootballDataService::class.java)

    suspend fun getMatches(): List<Match> {
        val response = service.getMatches("FINISHED")
        return response.matches
    }


    fun getMatchesByCountry(country: String, callback: (List<Match>?) -> Unit) {
        val call = service.getMatchesByCountry(apiKey,country)
        call.enqueue(object : Callback<MatchResponse> {
            override fun onResponse(call: Call<MatchResponse>, response: Response<MatchResponse>) {
                if (response.isSuccessful) {
                    val matches = response.body()?.matches
                    callback(matches)
                } else {
                    callback(null)
                }
            }
            override fun onFailure(call: Call<MatchResponse>, t: Throwable) {
                callback(null)
            }
        })
    }
}
