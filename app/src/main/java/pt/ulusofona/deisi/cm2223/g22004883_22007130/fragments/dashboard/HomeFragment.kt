package pt.ulusofona.deisi.cm2223.g22004883_22007130.fragments.dashboard

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22004883_22007130.MainActivity
import pt.ulusofona.deisi.cm2223.g22004883_22007130.NavigationManager
import pt.ulusofona.deisi.cm2223.g22004883_22007130.R
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRepository
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRoom
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.PhotosHandler
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val repository = MovieRepository.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Home"
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(view)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        //repository.deleteMovieByTitle("Ronaldo")
        repository.getAllMovies { movies ->
            if (movies.isSuccess) {
                CoroutineScope(Dispatchers.Main).launch {
                    val movies2 = movies.getOrNull()
                    if (movies2 == null || movies2.isEmpty()) {
                        binding.home.isVisible = false
                        binding.homeNoMovies.isVisible = true
                    } else {
                        binding.homeNoMovies.isVisible = false
                        binding.home.isVisible = true
                        movies.getOrNull()?.let { showDashboard(it) }
                    }
                }
            }
        }
    }

    private fun showDashboard(movies: List<MovieRoom>) {
        val movie: MovieRoom? = MovieRoom.getLastSeenMovie(movies) ?: return
        showPoster(movie)
        binding.lastSeenTitleYearView.text = movie?.getTitleYear()
        binding.lastSeenGenreView.text = MovieRoom.limitTextSize(movie?.getGenre(), 30)
        binding.lastSeenDirectorsView.text = MovieRoom.limitTextSize(movie?.getDirectors(), 30)
        binding.lastSeenActorsView.text = MovieRoom.limitTextSize(movie?.getActors(), 30)
        binding.lastSeenImdbRatingView.text = movie?.getImdbRating()
        binding.lastSeenImdbVotesView.text = movie?.getImdbVotes()
        binding.lastSeenDateView.text = movie?.getUserSeenDate()
        binding.lastSeenCinemaView.text = movie?.getCinemaName()
        binding.mostSeenGenreView.text = MovieRoom.getMostSeenActorDirectorGenre("genre", movies)
        binding.totalMoviesSeenByUserView.text = MovieRoom.getTotalMoviesSeenByUser(movies)
        binding.totalDurationMoviesSeenByUserView.text =
            MovieRoom.getTotalDurationsOfMoviesSeenByUser(movies)
        binding.mostWatchedActorView.text =
            MovieRoom.getMostSeenActorDirectorGenre("actors", movies)
        binding.mostWatchedDirectorView.text =
            MovieRoom.getMostSeenActorDirectorGenre("directors", movies)
        binding.mostUsedCinemaView.text = MovieRoom.getMostSeenActorDirectorGenre("cinema", movies)

        binding.homePoster.setOnClickListener {
            goToLastMoviesDetailView(movie)
        }
        binding.lastSeenPosterCard.setOnClickListener {
            goToLastMoviesDetailView(movie)
        }
    }


    private fun showPoster(movieRoom: MovieRoom?) {
        if (movieRoom == null) {
            return
        }
        if (movieRoom.getDownloadedPoster().isEmpty()) {
            context?.let {
                Glide.with(it).load(movieRoom.getPoster()).override(300, 300)
                    .into(binding.homePoster)
            }
        } else {
            val imageBitmap = PhotosHandler.stringToBitmap(movieRoom.getDownloadedPoster())
            Glide.with(requireContext()).load(imageBitmap).into(binding.homePoster)
        }
    }

    private fun goToLastMoviesDetailView(movieRoom: MovieRoom?) {
        MainActivity.push(R.id.movies)
        (requireActivity() as MainActivity).binding.bottomNavigator.menu.findItem(MainActivity.fragementsInBottomNavigatorMenuStack.last()).isChecked =
            true//highlight navigator item
        movieRoom?.getImdbID()
            ?.let { NavigationManager.goToMovieDetailFragment(parentFragmentManager, it) }
    }
}