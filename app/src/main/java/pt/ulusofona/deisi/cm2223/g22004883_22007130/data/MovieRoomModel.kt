package pt.ulusofona.deisi.cm2223.g22004883_22007130.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieRoomModel(private val model: MovieDao): MovieFunctions() {

    override fun insertMovieInDatabase(movie: MovieRoom) {
        CoroutineScope(Dispatchers.IO).launch {
            if (model.getMovieByImdbID(movie.getImdbID())?.getImdbID() != null || model.getMovieByImdbID(movie.getImdbID())?.getImdbID() == movie.getImdbID()) {
                model.deleteMovieByImdbID(movie.getImdbID())
                model.insert(movie)
                CoroutineScope(Dispatchers.Main).launch {
                    Log.i("Inserir filme", "${movie.title}, ${movie.getReleasedYear()}, rating de utilizador = ${movie.getUserRating()}")
                }
            } else {
                Log.i("name",movie.title )
                Log.i("year",movie.getReleasedYear() )
                Log.i("RATING",movie.getUserRating() )
                Log.i("PHOTOS",movie.getUserPhoto().toString() ?: "NO photos")
                model.insert(movie)
                CoroutineScope(Dispatchers.Main).launch {
                    Log.i("Inserir filme", "${movie.title}, ${movie.getReleasedYear()}, rating de utilizador = ${movie.getUserRating()}")
                }
            }
        }
    }

    override fun deleteMovieByTitle(movieName:String) {
        CoroutineScope(Dispatchers.IO).launch {
            model.deleteMovieByTitle(movieName)
        }
    }

    override fun getAllMovies(onFinished: (Result<List<MovieRoom>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val movies = model.getAll()
            onFinished(Result.success(movies))
        }
    }

    override fun getMovieByTitle(movieTitle: String, onFinished: (Result<MovieRoom?>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            onFinished(Result.success(model.getMovieByTitle(movieTitle)))
        }
    }


    override fun getMovieByImdbID(imdbID: String, onFinished: (Result<MovieRoom>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val movie = model.getMovieByImdbID(imdbID)
            onFinished(Result.success(movie))
        }
    }

    override fun getMoviesSeenByUser(onFinished: (List<MovieRoom>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            onFinished(model.getMoviesSeenByUser())
        }
    }

    override fun getMatchingSuggestions(searchTerm: String): List<MovieRoom> {
        val movies = model.getAll()
        val suggestions = mutableListOf<MovieRoom>()
        for (movie in movies) {
            if (suggestions.size >= 10) {
                break
            }
            if (movie.title.startsWith(searchTerm, true)) {
                suggestions.add(movie)
            }
        }
        return suggestions
    }
}