package com.example.diario_final

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.mediation.Adapter

class RecyclerAdapter(private val mList: ArrayList<ItemsViewModel>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]

        holder.itemVideo.setVideoPath(ItemsViewModel.video)
        holder.itemNome.text = ItemsViewModel.textnome
        holder.itemData.text = ItemsViewModel.textdata
        holder.itemVideo.start()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val itemVideo: VideoView
        var itemNome: TextView
        var itemData: TextView


        init {
            itemVideo = itemView.findViewById(R.id.videoView)
            itemNome = itemView.findViewById(R.id.textViewNome)
            itemData = itemView.findViewById(R.id.textViewData)
        }

    }
}