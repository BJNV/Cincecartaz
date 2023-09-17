package pt.ulusofona.deisi.cm2223.g22004883_22007130.data

import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MovieOkHttp(
    private val baseUrl: String,
    private val apiKeyPrimaryKey: String,
    private val apiKeySecondaryKey: String,
    private val client: OkHttpClient
) : MovieFunctions() {

    override fun getAllMovies(onFinished: (Result<List<MovieRoom>>) -> Unit) {
        Log.e("APP", "web service is not able to get all movies")
    }

    override fun getMovieByTitle(movieTitle: String, onFinished: (Result<MovieRoom?>) -> Unit) {
        Log.d("movie title",movieTitle)
        val searchTitleQuery = "t="
        val request: Request =
            Request.Builder()
                .url("$baseUrl$searchTitleQuery$movieTitle&apikey=$apiKeyPrimaryKey")//.addHeader("Authorization", "Bearer $apiKeyPrimaryKey")
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFinished(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    onFinished(Result.failure(IOException("Unexpected Code $response")))
                } else {
                    val body = response.body?.string()
                    Log.d("response",body.toString())
                    if (body != null) {
                        val jsonObject = JSONObject(body)
                        if(jsonObject.getString("Response") == "False") {
                            onFinished(Result.failure(Exception("Nome Inválido")))
                            return
                        }
                        val movie = MovieRoom(
                            jsonObject.getString("Title"),
                            jsonObject.getString("Year"),
                            jsonObject.getString("Runtime"),
                            jsonObject.getString("Genre"),
                            jsonObject.getString("Poster"),
                            jsonObject.getString("Plot"),
                            jsonObject.getString("imdbRating"),
                            jsonObject.getString("imdbVotes"),
                            jsonObject.getString("imdbID"),
                            jsonObject.getString("Actors"),
                            jsonObject.getString("Director"),
                            jsonObject.getString("Awards"),
                            "",
                            "",
                            "",
                            "",
                            mutableListOf<String>(),
                            ""
                        )
                        onFinished(Result.success(movie))
                    }
                }
            }
        })
    }

    override fun getMovieByImdbID(imdbID: String, onFinished: (Result<MovieRoom>) -> Unit) {
        Log.d("movie title",imdbID)
        val imdbIDQuery = "i="
        val request: Request =
            Request.Builder()
                .url("$baseUrl$imdbIDQuery$imdbID&apikey=$apiKeySecondaryKey")//.addHeader("Authorization", "Bearer $apiKeyPrimaryKey")
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFinished(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    onFinished(Result.failure(IOException("Unexpected Code $response")))
                } else {
                    val body = response.body?.string()
                    if (body != null) {
                        val jsonObject = JSONObject(body)

                        val movie = MovieRoom(
                            jsonObject.getString("Title") ?: "N/A",
                            jsonObject.getString("Year") ?: "N/A",
                            jsonObject.getString("Runtime") ?: "N/A",
                            jsonObject.getString("Genre") ?: "N/A",
                            jsonObject.getString("Poster"),
                            jsonObject.getString("Plot") ?: "N/A",
                            jsonObject.getString("imdbRating") ?: "N/A",
                            jsonObject.getString("imdbVotes") ?: "N/A",
                            jsonObject.getString("imdbID") ?: "N/A",
                            jsonObject.getString("Actors") ?: "N/A",
                            jsonObject.getString("Director") ?: "N/A",
                            jsonObject.getString("Awards") ?: "N/A",
                            "",
                            "",
                            "",
                            "",
                            mutableListOf<String>(),
                            ""
                        )
                        onFinished(Result.success(movie))
                    }
                }
            }
        })
    }

    override fun insertMovieInDatabase(movie: MovieRoom) {
        Log.e("APP", "web service is not able to insert movies")
    }

    override fun deleteMovieByTitle(movieName: String) {
        Log.e("APP", "web service is not able to delete movies")
    }

    override fun getMoviesSeenByUser(onFinished: (List<MovieRoom>) -> Unit) {
        Log.e("APP", "web service does not store movies seen by user")
    }


    override fun getMatchingSuggestions(searchTerm: String): List<MovieRoom> {
        imdbIDList.clear()
        // Make API call to get matching suggestions and return the results as a List<String>
        if (searchTerm.length < 3) {
            return emptyList()
        }
        val apiKey = "bc3b940e"

        val url = URL("http://www.omdbapi.com/?apikey=$apiKey&s=$searchTerm")
        val connection = url.openConnection() as HttpURLConnection
        Log.d("link",url.toString())
        connection.requestMethod = "GET"

        val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
        var inputLine: String?
        val response = StringBuffer()

        while (bufferedReader.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
        }
        bufferedReader.close()

        val jsonObject = JSONObject(response.toString())
        val searchResults = jsonObject.getJSONArray("Search")
        val listApi = mutableListOf<MovieRoom>()
        for (i in 0 until searchResults.length()) {
            val movie = searchResults.getJSONObject(i)
            val movieTitle = movie.getString("Title")
            val movieID = movie.getString("imdbID")
            val poster = movie.getString("Poster")
            listApi.add(
                MovieRoom(
                    movieTitle,
                    "",
                    "",
                    "",
                    poster,
                    "",
                    "",
                    "",
                    movieID,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    mutableListOf<String>(),
                    ""
                )
            )
            imdbIDList.add(movieID)
        }
        return listApi
    }

    companion object {
        val imdbIDList: MutableList<String> = mutableListOf()

        fun getImdbList(): List<String> {
            return imdbIDList.takeLast(10)
        }
    }

}