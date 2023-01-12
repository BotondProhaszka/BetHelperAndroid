package hu.proha.bethelper.data

import android.os.Parcel
import android.os.Parcelable

data class Match(val homeTeam: Team, val awayTeam: Team, val score: Score) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable<Team>(Team::class.java.classLoader)!!,
        parcel.readParcelable<Team>(Team::class.java.classLoader)!!,
        parcel.readParcelable<Score>(Score::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(homeTeam, flags)
        parcel.writeParcelable(awayTeam, flags)
        parcel.writeParcelable(score, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Match> {
        override fun createFromParcel(parcel: Parcel): Match {
            return Match(parcel)
        }

        override fun newArray(size: Int): Array<Match?> {
            return arrayOfNulls(size)
        }
    }
}

