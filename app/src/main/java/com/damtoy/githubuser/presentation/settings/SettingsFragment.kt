package com.damtoy.githubuser.presentation.settings

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.damtoy.githubuser.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val themePreferences by lazy {
        requireContext().getSharedPreferences(
            "theme_preferences",
            Context.MODE_PRIVATE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(
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

        setupTheme()
        setupExitButton()
    }

    private fun setupTheme() {
        // Default ke MODE_NIGHT_FOLLOW_SYSTEM jika pengguna belum pernah memilih
        val savedMode = themePreferences.getInt(
            "theme_mode",
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )

        // Cek apakah tampilan HP saat ini sedang menggunakan Mode Gelap (baik dari sistem atau manual)
        val isDarkModeActive = when (savedMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> true
            AppCompatDelegate.MODE_NIGHT_NO -> false
            else -> { // MODE_NIGHT_FOLLOW_SYSTEM
                val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                currentNightMode == Configuration.UI_MODE_NIGHT_YES
            }
        }

        // Atur posisi switch sesuai kondisi aktif saat ini
        binding.switchDarkMode.isChecked = isDarkModeActive

        binding.switchDarkMode.setOnCheckedChangeListener { _, checked ->
            val newMode = if (checked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

            themePreferences
                .edit()
                .putInt("theme_mode", newMode)
                .apply()

            AppCompatDelegate.setDefaultNightMode(newMode)
        }
    }

    private fun setupExitButton() {
        binding.btnExit.setOnClickListener {
            requireActivity().finishAffinity()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}