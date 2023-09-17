package pt.ulusofona.deisi.cm2223.g22004883_22007130

import android.app.Application
import okhttp3.OkHttpClient
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieDatabase
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieOkHttp
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRepository
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRoomModel
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.Cinema

class MovieApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Cinema.readJsonFile(assets)
        MovieRepository.init(remote = initMovieOkHttp(), local = initMovieRoom(), context = this)
    }

    private fun initMovieOkHttp(): MovieOkHttp {
        return MovieOkHttp("https://www.omdbapi.com/?", "bc3b940e&s", "3e3c7e2c", OkHttpClient())
    }

    private fun initMovieRoom(): MovieRoomModel {
        return MovieRoomModel(MovieDatabase.getInstance(this).movieDao())
    }

}