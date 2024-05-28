package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.checkerstry.classes.FirebaseAuthHelper
import com.example.checkerstry.databinding.ActivityMainBinding

/**
 * Main activity class that acts as the primary UI controller for the application.
 * It sets up a navigation drawer and handles navigation to different fragments based on user interaction.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup for the navigation drawer
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Ensure the home fragment is displayed when the activity is created for the first time
        if (savedInstanceState == null) {
            changeFragment(R.id.nav_home)
            binding.navView.setCheckedItem(R.id.nav_home)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Navigation item selection handling
        binding.navView.setNavigationItemSelectedListener {
            changeFragment(it.itemId)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    /**
     * Handles navigation based on the navigation drawer's item selection.
     * Starts new activities or replaces fragments in the container.
     * @param itemId The menu item ID that was selected.
     */
    private fun changeFragment(itemId: Int) {
        var fragment: Fragment? = null
        when(itemId) {
            R.id.nav_play_online -> fragment = OnlineGameFragment()
            R.id.nav_play_offline -> fragment = OfflineGameFragment()
            R.id.nav_home -> fragment = MainMenuFragment()
            R.id.nav_profile_page -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.nav_logout -> onBackPressed()
        }
        fragment?.let {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, it)
                commit()
            }
        }
    }

    /**
     * Handles ActionBar item selections, particularly the home button that controls the drawer.
     * @param item The menu item that was selected.
     * @return true if handled by the drawer toggle, otherwise false.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Overrides the back button to display a logout confirmation dialog.
     */
    override fun onBackPressed() {
        val dialog = AlertDialog.Builder(this)
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { _, _ ->
                FirebaseAuthHelper.logOut()
                super.onBackPressed()
            }
            .setNegativeButton("No", null)
            .create()

        dialog.show()
    }
}
