package com.damtoy.githubuser.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.damtoy.githubuser.R
import com.damtoy.githubuser.databinding.FragmentSearchBinding
import com.damtoy.githubuser.presentation.detail.UserDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private val adapter = UserAdapter { user ->
        findNavController().navigate(
            R.id.action_search_to_detail,
            bundleOf(UserDetailViewModel.ARG_USERNAME to user.login)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvUsers.adapter = adapter
        binding.etSearch.doAfterTextChanged { viewModel.onQueryChanged(it?.toString().orEmpty()) }
        binding.btnRetry.setOnClickListener { viewModel.retry() }
        viewModel.uiState.observe(viewLifecycleOwner) { render(it) }
    }

    private fun render(state: SearchUiState) = with(binding) {
        progress.isVisible = state is SearchUiState.Loading
        rvUsers.isVisible = state is SearchUiState.Success
        tvCacheBanner.isVisible = state is SearchUiState.Success && state.isFromCache
        stateContainer.isVisible = state is SearchUiState.Idle ||
                state is SearchUiState.Empty || state is SearchUiState.Error
        btnRetry.isVisible = state is SearchUiState.Error

        when (state) {
            SearchUiState.Idle -> tvMessage.setText(R.string.search_idle)
            SearchUiState.Empty -> tvMessage.setText(R.string.search_empty)
            is SearchUiState.Error -> tvMessage.setText(state.message)
            is SearchUiState.Success -> adapter.submitList(state.users)
            SearchUiState.Loading -> Unit
        }
    }

    override fun onDestroyView() {
        binding.rvUsers.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
