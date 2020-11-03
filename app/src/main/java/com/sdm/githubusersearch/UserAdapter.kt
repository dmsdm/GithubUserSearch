package com.sdm.githubusersearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdm.githubusersearch.repository.User

class UserAdapter(
    private val callback: ((User, ImageView) -> Unit)?
) : ListAdapter<User, UserAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.login == newItem.login
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.avatarUrl == newItem.avatarUrl
        }
    }
) {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.imageView)
        private val text: TextView = view.findViewById(R.id.textView)
        init {
            itemView.setOnClickListener {
                callback?.invoke(currentList[adapterPosition], image)
            }
        }
        fun bind(position: Int) {
            Glide.with(itemView).load(currentList[position].avatarUrl).into(image)
            text.text = currentList[position].login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }
}
