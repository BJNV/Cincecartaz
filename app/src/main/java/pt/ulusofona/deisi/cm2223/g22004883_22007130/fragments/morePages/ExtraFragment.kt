package pt.ulusofona.deisi.cm2223.g22004883_22007130.fragments.morePages

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import pt.ulusofona.deisi.cm2223.g22004883_22007130.MainActivity
import pt.ulusofona.deisi.cm2223.g22004883_22007130.NavigationManager
import pt.ulusofona.deisi.cm2223.g22004883_22007130.R
import pt.ulusofona.deisi.cm2223.g22004883_22007130.adapters.AutoCompleteAdapter
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRepository
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRoom
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.PhotosHandler
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.ActivityMainBinding
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.FragmentExtraBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class ExtraFragment : Fragment() {

    private lateinit var binding: FragmentExtraBinding
    //private val movieNames = MovieRoom.getAllMovieTitles()
    private val repository = MovieRepository.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_extra, container, false)
        binding = FragmentExtraBinding.bind(view)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        searchBar()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        try {
            binding.ytScreen.release()//esta linha e necessaria para cancelar o yt player no fim de vida do fragmento
        } catch (_: Exception) {
        }
        (activity as MainActivity).binding.bottomNavigator.visibility = View.VISIBLE
        (activity as MainActivity).binding.bottomAppBar.visibility = View.VISIBLE
        (activity as MainActivity).binding.fab.visibility = View.VISIBLE
        // Remove the current fragment from the container
        parentFragmentManager.beginTransaction().remove(this).commitNow()
        // Recreate the fragment and add it to the container
        MainActivity.push(R.id.more_pages)
        NavigationManager.goToExtraPage(parentFragmentManager)
    }

    /*Trata da logica da barra de pesquisa
    * se um filme nao existir na BD ele esconde os detalhes, se existir mostra*/
    fun searchBar() {
        var movieRoom: MovieRoom?
        //adapter para a lista de filmes automatica

        val adapter = AutoCompleteAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            emptyList(),
            repository
        )
        binding.searchBar?.setAdapter(adapter)
        binding.searchBar?.setOnItemClickListener() { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as MovieRoom
            binding.searchBar?.setText(selectedItem.title)
        }
        binding.searchBar?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.movieDetailsLayout?.isVisible = false
        binding.searchButton?.setOnClickListener {
            repository.getMovieByTitle(binding.searchBar?.text.toString()) {
                CoroutineScope(Dispatchers.Main).launch {
                    if (it.isSuccess) {
                        searchButtonPressed(it.getOrNull())
                    }
                }
            }
        }
    }

    private fun searchButtonPressed(movie: MovieRoom?) {
        if (movie == null /*|| validateTitle(movie) == null*/) {
            binding.movieDetailsLayout?.isVisible = false
            return
        }
        context?.let { it1 ->
            binding.posterView?.let { it2 ->
                Glide.with(it1).load(movie.getPoster()).into(
                    it2
                )
            }
        }
        showDetails(movie)
        binding.movieDetailsLayout?.isVisible = true
    }

    /*private fun validateTitle(movieRoom: MovieRoom?): MovieRoom? {
        val title = movieRoom?.title
        for (m in MovieRoom.movieRooms) {
            if (m.title == title) {
                break
            }
        }
        if (movieRoom == null || movieRoom.getPlot() == "Filme para dar um espacao na lista de filmes") {
            return null
        }
        return movieRoom
    }*/

    private fun showPoster(movieRoom: MovieRoom?){
        if(movieRoom == null) {
            return
        }
        if(movieRoom.getDownloadedPoster().isEmpty()) {
            context?.let {
                binding.posterView?.let { it1 ->
                    Glide.with(it).load(movieRoom.getPoster()).override(300, 300).into(
                        it1
                    )
                }
            }
        }else{
            val imageBitmap = PhotosHandler.stringToBitmap(movieRoom.getDownloadedPoster())
            binding.posterView?.let { Glide.with(requireContext()).load(imageBitmap).into(it) }
        }
    }

    fun showDetails(movieRoom: MovieRoom?) {
        /*context?.let {
            binding.posterView.let { it1 ->
                if (it1 != null) {
                    Glide.with(it).load(movie?.getPoster()).into(
                        it1
                    )
                }
            }
        }*/
        showPoster(movieRoom)
        binding.titleView?.text = movieRoom?.title
        binding.releasedDateView?.text = movieRoom?.getRealesedDate()
        binding.durationView?.text = movieRoom?.getDuration()
        binding.ratingView?.text = movieRoom?.getImdbRating()
        binding.votesView?.text = movieRoom?.getImdbVotes()
        showGenre(movieRoom)
        binding.plotView?.text = movieRoom?.getPlot()
        binding.directorView?.text = movieRoom?.getDirectors()
        binding.actorView?.text = movieRoom?.getActors()
        binding.awardView?.text = movieRoom?.getAwards()
        binding.linkView?.text = "https://www.imdb.com/title/${movieRoom?.getImdbID()}"
        movieRoom?.title?.let { youtubeHandler(it) }
    }

    fun showGenre(movieRoom: MovieRoom?) {
        val genres: List<String> = movieRoom?.getGenre()?.split(", ") ?: listOf()
        if (genres.size == 1) {
            binding.genre1Frame?.isVisible = false
            binding.genre2View?.text = genres[0]
            binding.genre3Frame?.isVisible = false
        }
        if (genres.size == 2) {
            binding.genre1View?.text = genres[0]
            binding.genre2View?.text = genres[1]
            binding.genre3Frame?.isVisible = false
        }
        if (genres.size == 3)  {
            binding.genre1View?.text = genres[0]
            binding.genre2View?.text = genres[1]
            binding.genre3View?.text = genres[2]
        }
    }


    /*Funcao que trata de obter o id do trailer do filme do youtube
    * Ele envia um pedido HTTP, por exemplo o seguinte:
    * https://youtube.googleapis.com/youtube/v3/search?q=Carsmovietrailer&part=id&maxResults=1&key=AIzaSyAwFwwYqFMNMkO5yJDw_ExrcrV1AmtWnDM
    * iremos obter uma lista de videos em que a lista so tem 1 elemento, logo iremos obter apenas
    * o trailer do filme que inserimos, é possivel que isto falhe e retorne um video que não seja o
    * trailer do filme, mas isso é muito raro de acontecer tinha que ser com um filme muito pouco
    * conhecido ou então um filme que nem tem trailer.
    * Com aquele link obtemos alguma informacao no formato json de um video com base na nossa pesquisa,
    * e depois apenas retiramos do json o id do video para o colocar no leitor de youtube*/
    fun youtubeHandler(movieTitle: String) = CoroutineScope(Dispatchers.IO).launch {
        val apiKey = "AIzaSyAwFwwYqFMNMkO5yJDw_ExrcrV1AmtWnDM" //api key youtube
        val searchQuery = movieTitle + "movietrailer"
        val videoId = searchVideoId(searchQuery, apiKey)
            ?: "erro" //se nao encontrar o video, é muito raro não encontrar
        /*inicia o leitor, se der e carrega o video
        * se n conseguir iniciar simplesmente carrega o video */
        try {
            binding.ytScreen.initialize(
                object : AbstractYouTubePlayerListener() {

                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(videoId, 0f)
                    }
                },
                IFramePlayerOptions.Builder().controls(1).build()
            )
        } catch (e: Exception) {
            binding.ytScreen.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            })
        }
    }

    suspend fun searchVideoId(searchQuery: String, apiKey: String): String? {
        val baseUrl = "https://youtube.googleapis.com/youtube/v3/search"
        val query = "q=${
            withContext(Dispatchers.IO) {
                URLEncoder.encode(searchQuery, "UTF-8")
            }
        }&part=id&maxResults=1&key=$apiKey"
        val url = URL("$baseUrl?$query")
        val connection = withContext(Dispatchers.IO) {
            url.openConnection()
        } as HttpURLConnection
        connection.requestMethod = "GET"

        val inputStream = connection.inputStream
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val response = StringBuilder()
        var inputLine: String?
        while (withContext(Dispatchers.IO) {
                bufferedReader.readLine()
            }.also { inputLine = it } != null) {
            response.append(inputLine)
        }
        withContext(Dispatchers.IO) {
            bufferedReader.close()
        }

        connection.disconnect()

        // Parse the JSON response and retrieve the video ID
        val jsonObject = JSONObject(response.toString())
        val items = jsonObject.getJSONArray("items")
        if (items.length() == 0) {
            return null
        }
        val videoId = items.getJSONObject(0).getJSONObject("id").getString("videoId")
        MainActivity.trailerId = videoId
        return videoId
    }

    /*Quando o dispositivo roda ele entra nesta funcao, quando inicia o fragment tambem, mas
    não interessa por causa das condicoes da funcao*/
    override fun onResume() {
        super.onResume()
        val rotation = (requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
        if(MainActivity.trailerId!="" && (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)) {
            (activity as MainActivity).binding.bottomNavigator.visibility = View.GONE
            (activity as MainActivity).binding.bottomAppBar.visibility = View.GONE
            (activity as MainActivity).binding.fab.visibility = View.GONE
            try {
                binding.ytScreen.initialize(
                    object : AbstractYouTubePlayerListener() {

                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.cueVideo(MainActivity.trailerId, 0f)
                        }
                    },
                    IFramePlayerOptions.Builder().controls(1).build()
                )
            } catch (e: Exception) {
                binding.ytScreen.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                    override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(MainActivity.trailerId, 0f)
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            binding.ytScreen.release()//esta linha e necessaria para cancelar o yt player no fim de vida do fragmento
        } catch (_: Exception) {
        }
    }

}