package pt.ulusofona.deisi.cm2223.g22004883_22007130.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.PhotosHandler
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.ItemPhotoBinding


class PhotosListAdapter(private var items: List<String> = listOf()) :
    RecyclerView.Adapter<PhotosListAdapter.PhotosListViewHolder>() {

    class PhotosListViewHolder(val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root)

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosListViewHolder {
        context = parent.context
        return PhotosListViewHolder(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotosListViewHolder, position: Int) {
        val item = items[position]
        val photoBitmap = PhotosHandler.stringToBitmap(item)
        Glide.with(context).load(photoBitmap).into(holder.binding.photoView)
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(items: List<String>) {
        this.items = items
        notifyDataSetChanged()
    }
}
