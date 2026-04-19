package com.erc.canopysentinalg

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.erc.canopysentinalg.databinding.ActivityMainBinding
import com.erc.canopysentinalg.ui.auth.AuthActivity
import com.erc.canopysentinalg.ui.viewmodel.AuthViewModel
import com.erc.canopysentinalg.ui.viewmodel.ForestViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val authViewModel: AuthViewModel by viewModels()
    private val forestViewModel: ForestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAuthentication()
        setupToolbar()
        setupNavigation()
        setupSystemBars()
    }

    // ✅ FIXED AUTH CHECK (no more guest loop)
    private fun checkAuthentication() {
        authViewModel.currentUser.observe(this) { user ->
            if (user == null) {
                navigateToAuth()
            }
        }
    }

    private fun navigateToAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_map,
                R.id.navigation_analytics,
                R.id.navigation_alerts,
                R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun setupSystemBars() {
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        val controller = ViewCompat.getWindowInsetsController(window.decorView)
        controller?.show(WindowInsetsCompat.Type.systemBars())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_country_selector -> true
            R.id.menu_refresh -> {
                forestViewModel.refreshData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
