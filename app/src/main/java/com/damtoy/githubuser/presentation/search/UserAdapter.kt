package com.damtoy.githubuser.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.damtoy.githubuser.R
import com.damtoy.githubuser.databinding.ItemUserBinding
import com.damtoy.githubuser.domain.model.User
import com.damtoy.githubuser.presentation.util.loadAvatar

class UserAdapter(
    private val onUserClick: (User) -> Unit,
    private val onFavoriteClick: (User) -> Unit
) : ListAdapter<User, UserAdapter.UserViewHolder>(
    UserDiffCallback
) {

    inner class UserViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {

            binding.tvLogin.text = user.login

            binding.ivAvatar.loadAvatar(user.avatarUrl)

            binding.root.setOnClickListener {
                onUserClick(user)
            }

            binding.btnFavorite.setImageResource(
                if (user.isFavorite) {
                    R.drawable.ic_favorite
                } else {
                    R.drawable.ic_favorite_border
                }
            )

            binding.btnFavorite.setOnClickListener {
                onFavoriteClick(user)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        return UserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}

private object UserDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(
        oldItem: User,
        newItem: User
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: User,
        newItem: User
    ): Boolean {
        return oldItem == newItem
    }
}