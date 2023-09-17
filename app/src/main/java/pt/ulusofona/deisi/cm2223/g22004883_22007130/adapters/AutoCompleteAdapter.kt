package pt.ulusofona.deisi.cm2223.g22004883_22007130.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import pt.ulusofona.deisi.cm2223.g22004883_22007130.R
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieOkHttp
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRepository
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRoom
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.AutocompleteLayoutBinding
import java.lang.Exception

class AutoCompleteAdapter(context: Context, resource: Int, data: List<MovieRoom>, val repository: MovieRepository): ArrayAdapter<MovieRoom>(context, resource, data), Filterable {

    var filteredData = data

    override fun getCount(): Int {
        return filteredData.size
    }

    override fun getItem(position: Int): MovieRoom {
        Log.d("ERRO", filteredData.toString())
        return filteredData[position]
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(characters: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                try {
                    if (characters != null) {
                        val suggestions = repository.getMatchingSuggestions(characters.toString())
                        filterResults.values = suggestions
                        filterResults.count = suggestions.size
                    }
                } catch (e:Exception) {
                    Log.d("Exception", e.toString())
                }
                return filterResults
            }

            override fun publishResults(characters: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    filteredData = results.values as List<MovieRoom>
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.autocomplete_layout, parent, false)
        }
        val binding =AutocompleteLayoutBinding.bind(view!!)
        //val textViewName = view?.findViewById<TextView>(R.id.title)
        //val poster = view?.findViewById<ImageView>(R.id.poster)
        //textViewName?.text = filteredData[position].title
        binding.title.text = filteredData[position].title
        if (filteredData[position].getPoster() == "N/A") {
            Glide.with(context).load(R.drawable.no_poster).override(300,300).into(binding.poster)
            /*poster?.let {
                Glide.with(context).load(R.drawable.no_poster).override(300, 300)
                    .into(it)
            }*/
        } else {
            Glide.with(context).load(filteredData[position].getPoster()).override(300,300).into(binding.poster)
            /*poster?.let {
                Glide.with(context).load(filteredData[position].getPoster()).override(300, 300)
                    .into(it)
            }*/
        }
        return binding.root
    }

}