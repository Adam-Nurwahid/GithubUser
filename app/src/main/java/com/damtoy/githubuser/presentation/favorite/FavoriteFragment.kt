package com.damtoy.githubuser.presentation.favorite


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.damtoy.githubuser.R
import com.damtoy.githubuser.databinding.FragmentFavoriteBinding
import com.damtoy.githubuser.presentation.detail.UserDetailViewModel
import com.damtoy.githubuser.presentation.search.UserAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels()

    private val adapter = UserAdapter(
        onUserClick = { user ->
            findNavController().navigate(
                R.id.action_favorite_to_detail,
                bundleOf(
                    UserDetailViewModel.ARG_USERNAME to user.login
                )
            )
        },
        onFavoriteClick = { user ->
            viewModel.toggleFavorite(user)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentFavoriteBinding.inflate(
                inflater,
                container,
                false
            )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoriteFragment.adapter
        }

        viewModel.users.observe(
            viewLifecycleOwner
        ) { users ->

            adapter.submitList(users)

            binding.tvEmpty.isVisible =
                users.isEmpty()

            binding.rvFavorites.isVisible =
                users.isNotEmpty()
        }

        viewModel.loading.observe(
            viewLifecycleOwner
        ) {
            binding.progress.isVisible = it
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavorites()
    }

    override fun onDestroyView() {
        binding.rvFavorites.adapter = null
        _binding = null
        super.onDestroyView()
    }
}