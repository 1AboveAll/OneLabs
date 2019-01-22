package com.himanshurawat.onelabs.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.himanshurawat.onelabs.R
import com.himanshurawat.onelabs.pojo.Person
import org.jetbrains.anko.find

class PersonAdapter(private val context: Context,
                    private val dataList: MutableList<Person>,
                    private val listener: OnPersonClickListener) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        return PersonViewHolder(LayoutInflater.from(context).inflate(R.layout.person_recycler_view_item,parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val pos = holder.adapterPosition
        val item = dataList[pos]

        holder.name.text = item.name
        Glide.with(context).
            load(R.drawable.person_icon_grey).
            apply(RequestOptions().circleCrop()).
            into(holder.profileImage)

        holder.itemView.setOnClickListener{
            listener.onPersonClick(item)
        }

    }


    class PersonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name = itemView.find<TextView>(R.id.person_recycler_view_item_name_text_view)
        val profileImage = itemView.find<ImageView>(R.id.person_recycler_view_item_profile_image_view)

    }


    interface OnPersonClickListener{
        fun onPersonClick(person: Person)
    }
}