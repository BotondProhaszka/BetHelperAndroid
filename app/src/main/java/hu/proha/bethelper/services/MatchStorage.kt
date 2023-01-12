package hu.proha.bethelper.services

import android.content.Context
import com.google.gson.Gson

class MatchStorage(private val context: Context) {

    private val fileName = "matches.json"

    fun saveMatches(matches: List<hu.proha.bethelper.data.Match>) {
        val json = Gson().toJson(matches)
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }

    fun loadMatches(): ArrayList<hu.proha.bethelper.data.Match> {
        val json = context.openFileInput(fileName).bufferedReader().use {
            it.readText()
        }
        return ArrayList(Gson().fromJson(json, Array<hu.proha.bethelper.data.Match>::class.java).toList())
    }
}
