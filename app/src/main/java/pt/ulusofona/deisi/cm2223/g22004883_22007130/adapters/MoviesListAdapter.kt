package pt.ulusofona.deisi.cm2223.g22004883_22007130.adapters

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pt.ulusofona.deisi.cm2223.g22004883_22007130.R
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRoom
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.ItemMovieBinding

class MoviesListAdapter(
    private val onClickTile: (String) -> Unit,
    private val onClickMap: (String) -> Unit,
    private var items: List<MovieRoom> = listOf()
) :
    RecyclerView.Adapter<MoviesListAdapter.MoviesListViewHolder>() {

    class MoviesListViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListViewHolder {
        context = parent.context
        return MoviesListViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MoviesListViewHolder, position: Int) {
        val item: MovieRoom = items[position]
        holder.itemView.setOnClickListener { onClickTile(item.getImdbID()) }//entrar nos detalhes
        holder.binding.googleMapId?.setOnClickListener { onClickMap(item.getCinemaID()) }//entrar no mapa, usado na lista horizontal
        val actorsDirectorsTextSize = getSize()
        if (item.getPlot() == "Filme para dar um espacao na lista de filmes") {
            //insertListSpace(holder)
        } else {
            if(item.getPoster() == "N/A") {
                Glide.with(context).load(R.drawable.no_poster).override(300, 300)
                    .into(holder.binding.movieImage)
            } else{
                Glide.with(context).load(item.getPoster()).override(300, 300)
                    .into(holder.binding.movieImage)
            }
            holder.binding.titleYear.text = item.getTitleYear()
            holder.binding.genre.text = MovieRoom.limitTextSize(item.getGenre(), actorsDirectorsTextSize)
            holder.binding.directors.text = MovieRoom.limitTextSize(item.getDirectors(), actorsDirectorsTextSize)
            holder.binding.actors.text = MovieRoom.limitTextSize(item.getActors(), actorsDirectorsTextSize)
            holder.binding.rating.text = item.getImdbRating()
            holder.binding.votes.text = item.getImdbVotes()

            holder.binding.userRatingView?.text = item.getUserRating()// usado na lista horizontal
        }
    }

    /*Para nao dar bug com a nossa bottom bar navigator inserimos este espaço para nao ficar nenhum
    * item da lista de baixo da barra*/
    private fun insertListSpace(holder: MoviesListViewHolder) {
        holder.binding.card.isVisible = false
        holder.binding.titleYear.text = ""
        holder.binding.genre.text = ""
        holder.binding.directors.text = ""
        holder.binding.actors.text = ""
        holder.binding.rating.text = ""
        holder.binding.votes.text = ""
    }


    /*Se tivermos o ecra na orientacao vertical ele diminui o tamanho dos atores e dos diretores, para
    * nao sair do ecra, e se tiver horizontal aumenta o tamanho limite
    * Exemplo: portrait -> Sylvester Sta...
    *          landscape -> Sylvester Stallone*/
    private fun getSize(): Int {
        return if(context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            60
        } else {
            30
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(items: List<MovieRoom>) {
        this.items = items
        notifyDataSetChanged()
    }
}