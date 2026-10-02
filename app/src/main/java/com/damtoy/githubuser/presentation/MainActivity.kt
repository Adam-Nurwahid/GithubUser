package com.damtoy.githubuser.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.damtoy.githubuser.R
import com.damtoy.githubuser.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val themePreferences by lazy {
        getSharedPreferences("theme_preferences", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        applySavedTheme()

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host)
                    as NavHostFragment

        navController = navHost.navController

        setupActionBarWithNavController(navController)

        binding.bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.searchFragment -> {
                    navController.navigate(
                        R.id.searchFragment
                    )
                    true
                }

                R.id.favoriteFragment -> {
                    navController.navigate(
                        R.id.favoriteFragment
                    )
                    true
                }

                R.id.settingsFragment -> {
                    navController.navigate(
                        R.id.settingsFragment
                    )
                    true
                }

                else -> false
            }
        }
        navController.addOnDestinationChangedListener {
                _,
                destination,
                _ ->

            binding.bottomNavigation.isVisible =
                destination.id != R.id.userDetailFragment
        }
    }

    private fun applySavedTheme() {

        val savedTheme = themePreferences.getInt(
            "theme_mode",
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )

        AppCompatDelegate.setDefaultNightMode(savedTheme)
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

        updateThemeMenuTitle(menu)

        return true
    }

    override fun onOptionsItemSelected(
        item: android.view.MenuItem
    ): Boolean {

        return when (item.itemId) {

            R.id.action_theme -> {
                toggleTheme()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleTheme() {

        val currentMode = AppCompatDelegate.getDefaultNightMode()

        val newMode =
            if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.MODE_NIGHT_NO
            } else {
                AppCompatDelegate.MODE_NIGHT_YES
            }

        themePreferences
            .edit()
            .putInt("theme_mode", newMode)
            .apply()

        AppCompatDelegate.setDefaultNightMode(newMode)
    }

    private fun updateThemeMenuTitle(menu: android.view.Menu) {

        val themeItem = menu.findItem(R.id.action_theme)

        themeItem.title =
            if (AppCompatDelegate.getDefaultNightMode()
                == AppCompatDelegate.MODE_NIGHT_YES
            ) {
                getString(R.string.theme_light)
            } else {
                getString(R.string.theme_dark)
            }
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp() || super.onSupportNavigateUp()
}