package pt.ulusofona.deisi.cm2223.g22004883_22007130.data

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22004883_22007130.R
import java.util.*
import kotlin.collections.HashMap

@Entity(tableName = "movies")
data class MovieRoom(
    // Variaveis da API
    val title: String,
    private val releasedDate: String,
    private val duration: String,
    private val genre: String,
    private val poster: String,
    private val plot: String,
    private val imdbRating: String,
    private val imdbVotes: String,
    @PrimaryKey private val imdbID: String,
    private val actors: String,
    private val directors: String,
    private val awards: String,
    private var downloadedPoster: String,

    // Variavel do JSON
    private var cinemaID: String,

    // Variaveis do Registo
    private var userRating: String,
    private var userSeenDate: String,
    private var userPhoto: List<String>?,
    private var userObservations: String
) {



    companion object {
        fun getAllMoviesRegisteredByUser(movies: List<MovieRoom>): MutableList<MovieRoom> {
            val moviesRegisteredByUser = mutableListOf<MovieRoom>()
            movies.forEach { if (it.userSeenDate.isNotEmpty()) moviesRegisteredByUser.add(it) }
            return moviesRegisteredByUser
        }

        fun getMoviesSortedByDate(movies: List<MovieRoom>): MutableList<MovieRoom> {
            val moviesSortedByDate = getAllMoviesRegisteredByUser(movies)
            //val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.UK)
            moviesSortedByDate.sortByDescending { /*sdf.parse(it.userSeenDate)*/it.userSeenDate }
            //moviesSortedByDate.add(movies.last())
            return moviesSortedByDate
        }

        fun getMostSeenActorDirectorGenre(field: String, movies: List<MovieRoom>): String {
            val map: HashMap<String, Int> = HashMap()
            val movies = MovieRoom.getAllMoviesRegisteredByUser(movies)
            movies.forEach { movie ->
                var text: List<String> = emptyList()
                when (field) {
                    "actors" -> text = movie.actors.split(", ")
                    "directors" -> text = movie.directors.split(", ")
                    "genre" -> text = movie.genre.split(", ")
                    "cinema" -> text = listOf(movie.getCinemaName())
                    else -> emptyList<String>()
                }
                text.forEach {
                    if (map.containsKey(it) && map[it] != null) {
                        var counter = map[it]
                        counter = counter!! + 1
                        map[it] = counter
                    } else {
                        map[it] = 1
                    }
                }
            }
            var mostSeen = ""
            var counter2: List<Int> = ArrayList(map.values)
            counter2 = counter2.sorted()
            for (i in map) {
                if (i.value == counter2.last()) {
                    mostSeen = i.key
                    break
                }
            }
            return mostSeen
        }

        fun limitTextSize(text: String?, size: Int): String? {
            var textResized = text
            if (text?.length!! > size) {
                textResized = text.substring(0, size)
                textResized += "..."
            }
            return textResized
        }

        fun getLastSeenMovie(moviesList: List<MovieRoom>): MovieRoom? {
            val movies = getMoviesSortedByDate(moviesList)
            if (movies.size == 0) return null
            return movies.first()
        }

        fun getTotalDurationsOfMoviesSeenByUser(moviesList: List<MovieRoom>): String {
            val movies = getAllMoviesRegisteredByUser(moviesList)
            var totalDuration = 0
            movies.forEach {
                if (it.duration == "N/A") return@forEach //isto é igual a um break
                val d = it.duration.split(" min")
                val movieDuration = d[0].toInt()
                totalDuration += movieDuration
            }
            return "$totalDuration min."
        }

        fun getTotalMoviesSeenByUser(moviesList: List<MovieRoom>): String {
            val movies = getAllMoviesRegisteredByUser(moviesList)
            return movies.size.toString()
        }
    }


    //fun getTitle(): String = title
    fun getRealesedDate(): String = releasedDate
    fun getDuration(): String = duration
    fun getReleasedDate(): String = releasedDate
    fun getReleasedYear(): String = getOnlyYearReleaseDate()
    fun getGenre(): String = genre
    fun getPoster(): String = poster
    fun getPlot(): String = plot
    fun getImdbRating(): String = imdbRating
    fun getImdbVotes(): String = imdbVotes
    fun getImdbID(): String = imdbID
    fun getActors(): String = actors
    fun getDirectors(): String = directors
    fun getAwards(): String = awards
    fun getDownloadedPoster(): String = downloadedPoster
    fun getCinemaID(): String = cinemaID
    fun getUserRating(): String = userRating
    fun getUserSeenDate(): String = userSeenDate
    fun getUserPhoto(): List<String>? = userPhoto
    fun getUserObservations(): String = userObservations

    // Função para concatenar Titulo e Ano
    fun getTitleYear(): String {
        return "${this.title} (${this.getReleasedYear()})"
    }

    // Função para mandar só o ano
    fun getOnlyYearReleaseDate(): String {
        val date: List<String> = this.releasedDate.split("/")
        return date[0]
    }

    fun getCinema(): Cinema? {
        Cinema.cinemas.forEach { if (it.getID() == cinemaID) return it }
        return null
    }

    fun getCinemaName(): String {
        return getCinema()?.getName() ?: "No Cinema"
    }

    fun setCinemaID(cinemaID: String) {
        this.cinemaID = cinemaID
    }

    fun setUserRating(rating: String) {
        this.userRating = rating
    }

    fun setUserSeenDate(seenDate: String) {
        this.userSeenDate = seenDate
    }

    fun setUserPhoto(photos: List<String>) {
        this.userPhoto = photos
    }

    fun setUserObservations(observations: String) {
        this.userObservations = observations
    }

    fun getRatingMarker(context: Context): String {
        val rating = userRating.toDouble()
        return when (rating) {
            in 1.0..2.9 -> context.getString(R.string.very_weak)
            in 3.0..4.9 -> context.getString(R.string.weak)
            in 5.0..6.9 -> context.getString(R.string.medium)
            in 7.0..8.9 -> context.getString(R.string.good)
            in 9.0..10.0 -> context.getString(R.string.excellent)
            else -> ""
        }
    }

}

class PhotosTypeConverter {
    @TypeConverter
    fun fromString(value: String?): List<String?> {
        val listType = object : TypeToken<List<String?>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String?>): String? {
        return Gson().toJson(list)
    }
}