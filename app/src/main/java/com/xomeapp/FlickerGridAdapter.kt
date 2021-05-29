package com.xomeapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.xomeapp.FlickerGridAdapter.MyViewHolder

class FlickerGridAdapter(
    private var context: Context,
    private var emailIds: ArrayList<FlickerPojo>
) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_inflate_flicker_list_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val aString =
            "https://farm" + emailIds.get(position).farm + ".static.flickr.com/" + emailIds.get(
                position
            ).server + "/" +
                    emailIds.get(position).id + "_" + emailIds.get(position).secret + ".jpg"

        Picasso.with(context)
            .load(aString)
            .placeholder(R.drawable.bg_placeholder)
            .into(holder.imageView);
    }

    override fun getItemCount(): Int {
        return emailIds.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView =
            itemView.findViewById<View>(R.id.layout_inflate_flicker_list_item_IMG_photo) as ImageView
    }
}