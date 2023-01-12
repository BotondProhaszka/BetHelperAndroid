package hu.proha.bethelper.services

import android.util.Log
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

    suspend fun getMatchesByCountry(@Header("X-Auth-Token") apiKey: String,
                            @Query("country") country: String): Call<MatchResponse>

    @GET("matches")
    suspend fun getMatchesByCountryWithDate(
        @Query("status") status: String = "FINISHED",
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String,
        @Query("country") country: String
    ): Response<MatchResponse>
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
        Log.d("DEBUG", response.toString())
        return response.matches
    }


    suspend fun getMatchesByCountry(country: String, callback: (List<Match>?) -> Unit) {
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
    //"2022-12-01T00:00:00Z" HU
    suspend fun getMatchesByCountryWithDate(
        country: String,
        dateFrom: String,
        dateTo: String
    ): List<Match> {
        val response = service.getMatchesByCountryWithDate(dateFrom = dateFrom, dateTo = dateTo, country = country)
        Log.d("DEBUG", response.toString())
        return if (response.isSuccessful) {
            response.body()?.matches ?: emptyList()
        } else {
            throw Exception("Error occurred while fetching data")
        }
    }
}
