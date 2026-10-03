package com.damtoy.githubuser.presentation.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.damtoy.githubuser.R
import com.damtoy.githubuser.databinding.FragmentUserDetailBinding
import com.damtoy.githubuser.presentation.util.loadAvatar
import com.damtoy.githubuser.presentation.util.setTextOrGone
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.findNavController
@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(
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

        binding.btnBack.setOnClickListener {
            val handled = findNavController().navigateUp()
            if (!handled) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        // Di dalam onViewCreated
        binding.btnBack.setColorFilter(
            com.google.android.material.color.MaterialColors.getColor(
                binding.btnBack,
                com.google.android.material.R.attr.colorOnSurface
            )
        )

        binding.btnRetry.setOnClickListener {
            viewModel.load()
        }

        binding.fabFavorite.setOnClickListener {
            val state = viewModel.uiState.value

            if (state is DetailUiState.Success) {
                viewModel.toggleFavorite(state.detail)
            }
        }

        viewModel.uiState.observe(
            viewLifecycleOwner
        ) { state ->
            render(state)
        }
    }

    private fun render(state: DetailUiState) = with(binding) {

        progress.isVisible = state is DetailUiState.Loading
        contentView.isVisible = state is DetailUiState.Success
        errorContainer.isVisible = state is DetailUiState.Error

        when (state) {

            is DetailUiState.Success -> {
                val user = state.detail

                ivAvatar.loadAvatar(user.avatarUrl)

                tvName.text = user.name ?: user.login

                tvLogin.text = getString(
                    R.string.login_format,
                    user.login
                )

                tvBio.setTextOrGone(user.bio)

                tvCompany.setTextOrGone(
                    user.company?.let {
                        getString(
                            R.string.company_format,
                            it
                        )
                    }
                )

                tvLocation.setTextOrGone(
                    user.location?.let {
                        getString(
                            R.string.location_format,
                            it
                        )
                    }
                )

                tvBlog.setTextOrGone(
                    user.blog?.let {
                        getString(
                            R.string.blog_format,
                            it
                        )
                    }
                )

                tvRepos.text = user.publicRepos.toString()

                tvFollowers.text = user.followers.toString()

                tvFollowing.text = user.following.toString()

                btnOpenGithub.setOnClickListener {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(user.htmlUrl)
                        )
                    )
                }

                fabFavorite.setImageResource(
                    if (user.isFavorite) {
                        R.drawable.ic_favorite
                    } else {
                        R.drawable.ic_favorite_border
                    }
                )

                fabFavorite.isVisible = true
            }

            is DetailUiState.Error -> {
                fabFavorite.isVisible = false
            }

            DetailUiState.Loading -> {
                fabFavorite.isVisible = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}