package com.urani.favoriteplaces.ui.main.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.urani.favoriteplaces.R
import com.urani.favoriteplaces.database.entities.Place
import com.urani.favoriteplaces.databinding.PlaceItemBinding
import com.urani.favoriteplaces.utils.Utils
import kotlinx.android.synthetic.main.place_item.view.*
import java.util.*

class PlacesAdapter(
    val context: Context,
    private var placesList: MutableList<Place>,
    private val callback: (Pair<Place, Boolean>) -> Unit
) : RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder>() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutInflater: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        layoutInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {
        val binding: PlaceItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.place_item, parent, false)

        return PlacesViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return placesList.size
    }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        val placeItem = placesList[position]

        val savedImageURI = Uri.parse(placeItem.imagePath)
        holder.binding.imageView.setImageURI(savedImageURI)

        holder.itemView.setOnClickListener {
            callback.invoke(Pair(placeItem, false))
        }

        holder.itemView.btnEdit.setOnClickListener {
            callback.invoke(Pair(placeItem, true))
        }
    }

    class PlacesViewHolder(itemView: PlaceItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: PlaceItemBinding = itemView

    }

    fun onAddPlaces(list: MutableList<Place>) {
        this.placesList = list
        notifyDataSetChanged()
    }


}