package pt.ulusofona.deisi.cm2223.g22004883_22007130.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import pt.ulusofona.deisi.cm2223.g22004883_22007130.MainActivity
import pt.ulusofona.deisi.cm2223.g22004883_22007130.NavigationManager
import pt.ulusofona.deisi.cm2223.g22004883_22007130.R
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRoom
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.FragmentMapBinding
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.MarkerLayoutBinding

class CustomInfoWindowAdapter(private val context: Context, private val result: List<MovieRoom>, private val fragment: FragmentManager) :
    GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        val infoWindowView = LayoutInflater.from(context).inflate(R.layout.marker_layout, null)
        //val rv = infoWindowView.findViewById<RecyclerView>(R.id.marker_rv)
        //rv.layoutManager = LinearLayoutManager(context)
        val binding : MarkerLayoutBinding = MarkerLayoutBinding.bind(infoWindowView)
        binding.markerRv.layoutManager = LinearLayoutManager(context)
        val adapter = ResultAdapter(result, ::onClickItem)
        infoWindowView.setOnClickListener {
            Log.d("clickei no rv","")
        }
        adapter.updateItems(result)
        binding.markerRv.adapter = adapter
        return infoWindowView
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    private fun onClickItem(imdbID: String) {
        MainActivity.push(R.id.movies)
        NavigationManager.goToMovieDetailFragment(fragment, imdbID)
    }
}

