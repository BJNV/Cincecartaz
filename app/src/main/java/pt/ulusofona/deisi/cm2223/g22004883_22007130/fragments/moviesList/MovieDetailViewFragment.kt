package pt.ulusofona.deisi.cm2223.g22004883_22007130.fragments.moviesList

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22004883_22007130.R
import pt.ulusofona.deisi.cm2223.g22004883_22007130.adapters.PhotosListAdapter
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieDatabase
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRepository
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRoom
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.PhotosHandler
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.FragmentMovieDetailViewBinding

private const val ARG_IMDBID = "param1"

class MovieDetailViewFragment : Fragment() {
    private var imdbID: String? = null
    private lateinit var binding: FragmentMovieDetailViewBinding
    private val adapter = PhotosListAdapter()
    private val repository = MovieRepository.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imdbID = it.getString(ARG_IMDBID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_detail_view, container, false)
        binding = FragmentMovieDetailViewBinding.bind(view)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        imdbID?.let {
            repository.getMovieByImdbID(it){ movie ->
                if (movie.isSuccess) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val movie = movie.getOrNull()
                        addDetails(movie)
                    }
                }
            }
        }
    }

    fun addDetails(movie: MovieRoom?) {
        //context?.let { Glide.with(it).load(movie?.getPoster()).into(binding.posterView) }
        showPoster(movie)
        if (movie?.title!!.length < 14) {
            binding.titleView.textSize = 20F
            binding.titleView.text = movie?.title
        } else {
            binding.titleView.textSize = 16F
            binding.titleView.text = movie?.title
        }

        binding.releasedDateView.text = movie?.getRealesedDate()
        binding.durationView.text = movie?.getDuration()
        binding.ratingView.text = movie?.getImdbRating()
        binding.votesView.text = movie?.getImdbVotes()
        showGenre(movie)
        binding.plotView.text = movie?.getPlot()
        binding.directorView.text = movie?.getDirectors()
        binding.actorView.text = movie?.getActors()
        binding.linkView.text = "https://www.imdb.com/title/$imdbID"

        binding.cinemaNameView.text = movie?.getCinemaName()
        binding.seenDateView.text = movie?.getUserSeenDate()
        binding.userRatingView.text = movie?.getUserRating()
        showObservations(movie)

        showPhotos(movie)
        binding.rvPhotosList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPhotosList.adapter = adapter
        val listMoviesString = movie?.getUserPhoto()
        val list = mutableListOf<String>()
        listMoviesString?.forEach { list.add(it) }
        adapter.updateItems(list)
    }

    private fun showPoster(movieRoom: MovieRoom?) {
        if (movieRoom == null) {
            return
        }
        if (movieRoom.getDownloadedPoster().isEmpty()) {
            context?.let {
                Glide.with(it).load(movieRoom.getPoster()).override(300, 300)
                    .into(binding.posterView)
            }
        } else {
            val imageBitmap = PhotosHandler.stringToBitmap(movieRoom.getDownloadedPoster())
            Glide.with(requireContext()).load(imageBitmap).into(binding.posterView)
        }
    }

    private fun showGenre(movieRoom: MovieRoom?) {
        val genres: List<String> = movieRoom?.getGenre()?.split(", ") ?: listOf()
        if (genres.size == 1) {
            binding.genre1Frame.isVisible = false
            binding.genre2View.text = genres[0]
            binding.genre3Frame.isVisible = false
        } else if (genres.size == 2) {
            binding.genre1View.text = genres[0]
            binding.genre2View.text = genres[1]
            binding.genre3Frame.isVisible = false
        } else {
            binding.genre1View.text = genres[0]
            binding.genre2View.text = genres[1]
            binding.genre3View.text = genres[2]
        }
    }

    private fun showObservations(movieRoom: MovieRoom?) {
        if (movieRoom?.getUserObservations() != "") {
            binding.userObservationsView.text = movieRoom?.getUserObservations()
        } else {
            binding.userObservationsFrame.isVisible = false
        }
    }

    private fun showPhotos(movieRoom: MovieRoom?) {
        if (movieRoom?.getUserPhoto() == null) {
            binding.userPhotoFrame.isVisible = false
        }
    }

    companion object {
        @JvmStatic
        fun newInstanceWithID(imdbID: String) =
            MovieDetailViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMDBID, imdbID)
                }
            }
    }
}