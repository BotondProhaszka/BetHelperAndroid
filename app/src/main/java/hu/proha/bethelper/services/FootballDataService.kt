package hu.proha.bethelper.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FootballDataService {
    @Headers("X-Auth-Token: YOUR_API_KEY")
    @GET("competitions/CL/matches")
    suspend fun getMatches(@Query("status") status: String): MatchResponse
}

data class MatchResponse(val matches: List<Match>)
data class Match(val homeTeam: Team, val awayTeam: Team, val score: Score)
data class Team(val name: String)
data class Score(val fullTime: ScoreResult)
data class ScoreResult(val homeTeam: Int, val awayTeam: Int)

class FootballDataRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://api.football-data.org/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(FootballDataService::class.java)

    suspend fun getMatches(): List<Match> {
        val response = service.getMatches("FINISHED")
        return response.matches
    }
}
