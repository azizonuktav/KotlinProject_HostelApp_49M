package com.example.hostelapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_view_row_hostels.view.*

class HostelMainFeedRecyclerAdapter(private val hostelNameArray: ArrayList<String>, private val hostelInformationArray : ArrayList<String>, private val hostelImageArray : ArrayList<String>) : RecyclerView.Adapter<HostelMainFeedRecyclerAdapter.PostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_view_row_hostels,parent,false)
        return PostHolder(view)
    }

    override fun getItemCount(): Int {
        return hostelNameArray.size

    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.recyclerNameText?.text = hostelNameArray[position]
        holder.recyclerInformationText?.text = hostelInformationArray[position]
        Picasso.get().load(hostelImageArray[position]).into(holder.recyclerImageView)
        holder.id = hostelNameArray[position]
        holder.commentButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(it.context, HostelFeed::class.java)
            intent.putExtra("hostelid", holder.id)
            println(holder.id)
            it.context.startActivity(intent)
        } )

    }


    class PostHolder(view : View) : RecyclerView.ViewHolder(view) {

        //View Holder class

        var recyclerNameText : TextView? = null
        var recyclerInformationText : TextView? = null
        var recyclerImageView : ImageView? = null
        var commentButton = itemView.button4
        var id : String? = null

        init {
            recyclerNameText = view.findViewById(R.id.recyclerNameText)
            recyclerInformationText = view.findViewById(R.id.recyclerInformationText)
            recyclerImageView = view.findViewById(R.id.recyclerImageView)


        }

    }
}