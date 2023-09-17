package pt.ulusofona.deisi.cm2223.g22004883_22007130.fragments.moviesList

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22004883_22007130.MainActivity
import pt.ulusofona.deisi.cm2223.g22004883_22007130.NavigationManager
import pt.ulusofona.deisi.cm2223.g22004883_22007130.adapters.MoviesListAdapter
import pt.ulusofona.deisi.cm2223.g22004883_22007130.R
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieDatabase
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRepository
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRoom
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.FragmentMoviesListBinding

class MoviesListFragment : Fragment() {

    private lateinit var binding: FragmentMoviesListBinding
    private val adapter = MoviesListAdapter(::onClickItem, ::onClickGoogleMapImage)
    private val repository = MovieRepository.getInstance()
    private var container :ViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.container = container
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)
        binding = FragmentMoviesListBinding.bind(view)
        return binding.root
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Remove the current fragment from the container
        parentFragmentManager.beginTransaction().remove(this).commitNow()
        // Recreate the fragment and add it to the container
        MainActivity.push(R.id.movies)
        NavigationManager.goToMoviesListFragment(parentFragmentManager)
    }

    override fun onStart() {
        super.onStart()
        binding.rvMoviesList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMoviesList.adapter = adapter
        //adapter.updateItems(MovieRoom.getMoviesSortedByDate())
        updateList()
        //adapter.updateItems(Movie.movies)
    }


    private fun onClickItem(imdbID: String) {
        MainActivity.push(R.id.movies)
        NavigationManager.goToMovieDetailFragment(parentFragmentManager, imdbID)
    }

    private fun onClickGoogleMapImage(cinemaId: String) {
        MainActivity.push(R.id.more_pages)
        (requireActivity() as MainActivity).binding.bottomNavigator.menu.findItem(MainActivity.fragementsInBottomNavigatorMenuStack.last()).isChecked =
            true //highlight navigator item
        NavigationManager.goToMapWithCinemaIdFragment(parentFragmentManager, cinemaId)
    }

    fun updateList() {
        repository.getAllMovies() {
            CoroutineScope(Dispatchers.Main).launch {
                it.getOrNull()?.let { movies ->
                    adapter.updateItems(/*movies*/MovieRoom.getMoviesSortedByDate(movies))
                }
            }
        }
    }

}