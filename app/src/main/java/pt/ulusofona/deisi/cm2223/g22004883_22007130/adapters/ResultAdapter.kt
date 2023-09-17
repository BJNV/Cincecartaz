package pt.ulusofona.deisi.cm2223.g22004883_22007130.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pt.ulusofona.deisi.cm2223.g22004883_22007130.R
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRoom
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.ItemMarkerLayoutBinding

class ResultAdapter(private var items: List<MovieRoom>, private val onClickTile: (String) -> Unit) :
    RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    class ResultViewHolder(val binding: ItemMarkerLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        context = parent.context
        return ResultViewHolder(
            ItemMarkerLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val item = items[position]
        setBackgroundColor(holder, position)
        //holder.binding.movieRating.text = item.getRatingMarker()
        //holder.binding.titleYear.text = item.getTitleYear()
        holder.itemView.setOnClickListener { onClickTile(item.getImdbID()) }
    }

    fun setBackgroundColor(holder: ResultViewHolder, position: Int) {
        val item = items[position]
        holder.binding.movieRating.text = item.getRatingMarker(context)
        holder.binding.titleYear.text = MovieRoom.limitTextSize(item.getTitleYear(),20)
        if (item.getRatingMarker(context) == context.getString(R.string.very_weak)) {
            holder.binding.movieRating.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
        }
        if (item.getRatingMarker(context) == context.getString(R.string.weak)) {
            holder.binding.movieRating.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
        }
        if (item.getRatingMarker(context) == context.getString(R.string.medium)) {
            holder.binding.movieRating.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
        }
        if (item.getRatingMarker(context) == context.getString(R.string.good)) {
            holder.binding.movieRating.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        }
        if (item.getRatingMarker(context) == context.getString(R.string.excellent)) {
            holder.binding.movieRating.setBackgroundColor(ContextCompat.getColor(context, R.color.blue))
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(items: List<MovieRoom>) {
        this.items = items
        notifyDataSetChanged()
    }
}