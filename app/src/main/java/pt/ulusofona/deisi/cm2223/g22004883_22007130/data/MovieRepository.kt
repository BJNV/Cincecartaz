package pt.ulusofona.deisi.cm2223.g22004883_22007130.data

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.lang.IllegalStateException

class MovieRepository(
    private val context: Context,
    private val local: MovieFunctions,
    private val remote: MovieFunctions
) : MovieFunctions() {

    override fun insertMovieInDatabase(movie: MovieRoom) {
        local.insertMovieInDatabase(movie)
    }

    override fun deleteMovieByTitle(movieName: String) {
        local.deleteMovieByTitle(movieName)
    }

    override fun getAllMovies(onFinished: (Result<List<MovieRoom>>) -> Unit) {
        local.getAllMovies(onFinished)
    }

    override fun getMovieByTitle(movieTitle: String, onFinished: (Result<MovieRoom?>) -> Unit) {
        if (ConnectivityUtil.isOnline(context)) {
            Log.d("APP", "Got movies from the server")
            return remote.getMovieByTitle(movieTitle, onFinished)
        } else {
            Log.d("APP", "App is offline. Getting characters from the database")
            return local.getMovieByTitle(movieTitle, onFinished)
        }
    }

    override fun getMovieByImdbID(imdbID: String, onFinished: (Result<MovieRoom>) -> Unit) {
        local.getMovieByImdbID(imdbID, onFinished)
    }

    override fun getMoviesSeenByUser(onFinished: (List<MovieRoom>) -> Unit) {
        local.getMoviesSeenByUser(onFinished)
    }

    override fun getMatchingSuggestions(searchTerm: String): List<MovieRoom> {
        if (ConnectivityUtil.isOnline(context)) {
            Log.i("APP", "Got movies from the server")
            return remote.getMatchingSuggestions(searchTerm)
        } else {
            Log.i("APP", "App is offline. Getting characters from the database characther = $searchTerm")
            return local.getMatchingSuggestions(searchTerm)
        }
    }

    fun downloadMovies(imdbID: String) {
        if (ConnectivityUtil.isOnline(context)) {
            for (i in 0 until MovieOkHttp.getImdbList().size) {
                Log.d("filmes",MovieOkHttp.getImdbList().size.toString())
                Log.d("filme",MovieOkHttp.getImdbList().size.toString())
                remote.getMovieByImdbID(MovieOkHttp.getImdbList()[i]) { movie ->
                    if (movie.isSuccess) {
                        if(imdbID != movie.getOrNull()?.getImdbID()) {
                            movie.getOrNull()?.let { local.insertMovieInDatabase(it) }
                        }
                    } else {
                        Log.i("falha", "")
                    }
                }
            }
        }
    }

    companion object {
        private var instance: MovieRepository? = null

        fun init(local: MovieFunctions, remote: MovieFunctions, context: Context) {
            if (instance == null) {
                instance = MovieRepository(context = context, local = local, remote = remote)
            }
        }

        fun getInstance(): MovieRepository {
            if (instance == null) {
                throw IllegalStateException("Singleton Not Initialized")
            }
            return instance as MovieRepository
        }
    }

}