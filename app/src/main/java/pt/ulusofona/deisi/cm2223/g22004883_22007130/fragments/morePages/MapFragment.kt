package pt.ulusofona.deisi.cm2223.g22004883_22007130.fragments.morePages

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22004883_22007130.*
import pt.ulusofona.deisi.cm2223.g22004883_22007130.adapters.ResultAdapter
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.Cinema
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRepository
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRoom
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.FragmentMapBinding
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.MarkerLayoutBinding
import java.util.*


private const val ARG_MOVIE_ID = "param1"


class MapFragment : Fragment(), OnLocationChangedListener {

    private var movieID: String? = null
    private lateinit var binding: FragmentMapBinding
    private var map: GoogleMap? = null
    private lateinit var geocoder: Geocoder
    private val repository = MovieRepository.getInstance()
    val cinemaMovies = HashMap<String, MutableList<MovieRoom>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieID = it.getString(ARG_MOVIE_ID)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        cinemaMovies.clear()
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        binding = FragmentMapBinding.bind(view)
        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync { map ->
            pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation.registerListener(this)
            this.map = map
            map.isMyLocationEnabled = true
            placeMarkers()
            placeCamera()
            map.setOnMarkerClickListener { marker ->
                val tag = marker.tag as? String
                if (tag.equals("Only one movie")) {
                    val cinemas = Cinema.cinemas
                    var selectedCinema: Cinema? = null
                    cinemas.forEach {
                        if (it.getLatitude() == marker.position.latitude.toString() && it.getLongitude() == marker.position.longitude.toString()) {
                            selectedCinema = it
                        }
                    }
                    val movie = cinemaMovies.get(selectedCinema?.getID())?.get(0)
                    MainActivity.push(R.id.movies)
                    movie?.getImdbID()?.let {
                        NavigationManager.goToMovieDetailFragment(
                            parentFragmentManager,
                            it
                        )
                    }
                } else {
                    //marker.showInfoWindow()
                    val cinemas = Cinema.cinemas
                    var selectedCinema: Cinema? = null
                    cinemas.forEach {
                        if (it.getLatitude() == marker.position.latitude.toString() && it.getLongitude() == marker.position.longitude.toString()) {
                            selectedCinema = it
                        }
                    }
                    val movies = cinemaMovies.get(selectedCinema?.getID())
                    if (movies != null) {
                        val dialog = MyDialogFragment(movies.toList())
                        dialog.show(requireFragmentManager(), "Lista")
                    }
                }
                false
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.hideBtn.setOnClickListener {
            binding.infoLayout.isVisible = false
            binding.infoIcon.isVisible = true
        }
        binding.infoIcon.setOnClickListener {
            binding.infoIcon.isVisible = false
            binding.infoLayout.isVisible = true
        }
    }




    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation.unregisterListener()
    }

    fun placeCamera() {
        var latitude = 38.722252
        var longitude = -9.139335
        var zoom = 10f
        if(movieID != null) {
            Cinema.cinemas.forEach { if(it.getID() == movieID) {
                latitude = it.getLatitude().toDouble()
                longitude = it.getLongitude().toDouble()
                zoom = 12f
            } }
        }
        placeCamera(latitude,longitude,zoom)
    }

    private fun placeCityName(latitude: Double, longitude: Double) {
        val addresses = geocoder.getFromLocation(latitude, longitude, 5)
        val location = addresses?.first {
            it.locality != null && it.locality.isNotEmpty()
        }
        //binding.tvCityName.text = location?.locality
    }


    // Atualiza e faz zoom no mapa de acordo com a localização
    private fun placeCamera(latitude: Double, longitude: Double,zoom:Float) {
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(latitude, longitude))
            .zoom(zoom)
            .build()

        map?.animateCamera(
            CameraUpdateFactory.newCameraPosition(cameraPosition)
        )
    }

    override fun onLocationChanged(latitude: Double, longitude: Double) {
        //placeCamera(latitude, longitude)
        //placeCityName(latitude, longitude)
    }

    private fun placeMarkers() {
        repository.getMoviesSeenByUser { movies ->
            CoroutineScope(Dispatchers.Main).launch {
                movies.forEach {
                    if (cinemaMovies.containsKey(it.getCinemaID())) {
                        val list = cinemaMovies.get(it.getCinemaID())
                        list?.add(it)
                        cinemaMovies.put(it.getCinemaID(), list!!)
                    } else {
                        cinemaMovies.put(it.getCinemaID(), mutableListOf(it))
                    }
                }
                getMarkers()
            }
        }
    }

    private fun placeMarker(latitude: Double, longitude: Double, color: Float,tag:String) {
        val marker = map?.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.defaultMarker(color))
        )
        marker?.tag = tag
    }

    private fun getMarkers() {
        val cinemas = Cinema.cinemas
        cinemas.forEach {
            if (cinemaMovies.containsKey(it.getID())) {
                val movies = cinemaMovies.get(it.getID())
                if (movies?.size == 1) {
                    placeMarker(
                        it.getLatitude().toDouble(),
                        it.getLongitude().toDouble(),
                        getRating(movies[0]),
                        "Only one movie"
                    )
                } else {
                    if (movies != null) {
                        placeMarker(
                            it.getLatitude().toDouble(),
                            it.getLongitude().toDouble(),
                            BitmapDescriptorFactory.HUE_VIOLET,
                            "more than 1 movie"
                        )
                    }
                }
            }
        }
    }

    private fun getRating(movie: MovieRoom): Float {
        val rating = movie.getUserRating().toDouble()
        return when {
            rating in 1.0..2.9 -> BitmapDescriptorFactory.HUE_RED
            rating in 3.0..4.9 -> BitmapDescriptorFactory.HUE_ORANGE
            rating in 5.0..6.9 -> BitmapDescriptorFactory.HUE_YELLOW
            rating in 7.0..8.9 -> BitmapDescriptorFactory.HUE_GREEN
            rating in 9.0..10.0 -> BitmapDescriptorFactory.HUE_AZURE
            else -> BitmapDescriptorFactory.HUE_VIOLET
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(movieId: String) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MOVIE_ID, movieId)
                }
            }
    }
}

class MyDialogFragment(private val movies : List<MovieRoom>) : DialogFragment() {
    private lateinit var binding: MarkerLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate your custom layout for the dialog
        val view = inflater.inflate(R.layout.marker_layout, container, false)
        binding = MarkerLayoutBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        binding.markerRv.layoutManager = LinearLayoutManager(requireContext())
        binding.markerRv.adapter = ResultAdapter(movies, ::onClickItem)
    }

    private fun onClickItem(imdbID: String) {
        dialog?.dismiss()
        MainActivity.push(R.id.movies)
        NavigationManager.goToMovieDetailFragment(parentFragmentManager, imdbID)
    }
}