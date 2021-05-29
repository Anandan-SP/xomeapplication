package com.xomeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.xomeapp.FlickerGridAdapter.MyViewHolder

class FlickerGridAdapter(
    private var myContext: Context,
    private var aFlickerInfoList: ArrayList<FlickerPojo>
) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_inflate_flicker_list_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val aString =
            "https://farm" + aFlickerInfoList.get(position).farm + ".static.flickr.com/" + aFlickerInfoList.get(
                position
            ).server + "/" +
                    aFlickerInfoList.get(position).id + "_" + aFlickerInfoList.get(position).secret + ".jpg"

        Picasso.with(myContext)
            .load(aString)
            .placeholder(R.color.bg_lightcolor)
            .into(holder.aImageView);
    }

    override fun getItemCount(): Int {
        return aFlickerInfoList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var aImageView: ImageView =
            itemView.findViewById<View>(R.id.layout_inflate_flicker_list_item_IMG_photo) as ImageView
    }
}