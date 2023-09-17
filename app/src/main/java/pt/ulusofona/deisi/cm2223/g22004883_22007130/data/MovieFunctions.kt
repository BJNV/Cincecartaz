package pt.ulusofona.deisi.cm2223.g22004883_22007130.data

abstract class MovieFunctions {

    abstract fun insertMovieInDatabase(movie: MovieRoom)

    abstract fun deleteMovieByTitle(movieName: String)

    abstract fun getAllMovies(onFinished: (Result<List<MovieRoom>>) -> Unit)

    abstract fun getMovieByTitle(movieTitle: String, onFinished: (Result<MovieRoom?>) -> Unit)

    abstract fun getMovieByImdbID(imdbID: String, onFinished: (Result<MovieRoom>) -> Unit)

    abstract fun getMoviesSeenByUser(onFinished: (List<MovieRoom>) -> Unit)

    abstract fun getMatchingSuggestions(searchTerm: String): List<MovieRoom>

}