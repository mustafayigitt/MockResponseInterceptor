package com.mustafayigit.mockresponseinterceptor.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mustafayigit.mockresponseinterceptor.data.model.NewsModel
import com.mustafayigit.mockresponseinterceptor.databinding.AdapterItemNewsBinding
import com.mustafayigit.mockresponseinterceptor.manager.ImageManager

class NewsAdapter(
    private inline val onItemClick: (NewsModel) -> Unit
) : ListAdapter<NewsModel, NewsAdapter.NewsViewHolder>(NewsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = AdapterItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding).also {holder ->
            binding.root.setOnClickListener {
                val item = getItem(holder.bindingAdapterPosition) ?: return@setOnClickListener
                onItemClick(item)
            }
        }
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NewsViewHolder(
        private val binding: AdapterItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(newsModel: NewsModel) {
            with(binding) {
                newsModel.coverImage?.let {
                    ImageManager.loadImage(imgCover, it)
                }
                txtTitle.text = newsModel.title
            }
        }
    }
}

class NewsDiffUtil : DiffUtil.ItemCallback<NewsModel>() {
    override fun areItemsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
        return oldItem == newItem
    }
}