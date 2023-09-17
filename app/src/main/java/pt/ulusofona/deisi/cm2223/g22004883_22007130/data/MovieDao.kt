package pt.ulusofona.deisi.cm2223.g22004883_22007130.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {

    @Insert
    fun insert(movie: MovieRoom)

    @Query("DELETE FROM movies WHERE title = :movieTitle")
    fun deleteMovieByTitle(movieTitle: String)

    @Query("DELETE FROM movies WHERE imdbID = :imdbID")
    fun deleteMovieByImdbID(imdbID: String)

    @Query("SELECT * FROM movies")
    fun getAll(): List<MovieRoom>

    @Query("SELECT * FROM movies WHERE title = :movieTitle")
    fun getMovieByTitle(movieTitle: String): MovieRoom?

    @Query("SELECT * FROM movies WHERE imdbID = :movieImdbID")
    fun getMovieByImdbID(movieImdbID: String): MovieRoom

    @Query("SELECT * FROM movies WHERE userSeenDate IS NOT ''")
    fun getMoviesSeenByUser(): List<MovieRoom>

}