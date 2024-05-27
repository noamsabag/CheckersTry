package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.checkerstry.classes.GameData
import com.example.checkerstry.classes.OnlineGameData
import com.example.checkerstry.classes.Piece
import com.example.checkerstry.classes.Player
import com.example.checkerstry.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    lateinit var passAndPlay: CardView
    lateinit var onlineGame: CardView
    lateinit var gameAnalysis: CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null)
        {
            changeFragment(R.id.nav_home)
            binding.navView.setCheckedItem(R.id.nav_home)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            changeFragment(it.itemId)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val defultFragment = MainMenuFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, defultFragment)
            commit()
        }

    }

    fun changeFragment(itemId: Int)
    {
        var fragment: Fragment? = null
        when(itemId)
        {
            R.id.nav_play_online -> fragment = OnlineGameFragment()
            R.id.nav_play_offline -> fragment = OfflineGameFragment()
            R.id.nav_home -> fragment = MainMenuFragment()
            R.id.nav_profile_page -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.nav_logout -> onBackPressed()
        }
        if (fragment == null) return

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val dialog = AlertDialog.Builder(this)
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton("no") { _, _ -> }
            .create()

        dialog.show()
    }


}