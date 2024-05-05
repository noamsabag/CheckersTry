package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.LinearLayout
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
import com.example.checkerstry.classes.GameData
import com.example.checkerstry.classes.OnlineGameData
import com.example.checkerstry.classes.Piece
import com.example.checkerstry.classes.Player
import com.example.checkerstry.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var passAndPlay: CardView
    lateinit var onlineGame: CardView
    lateinit var gameAnalysis: CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        passAndPlay = findViewById(R.id.passAndPlay)
        onlineGame = findViewById(R.id.onlineGame)
        gameAnalysis = findViewById(R.id.analysys)
        passAndPlay.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
        onlineGame.setOnClickListener {

            val intent = Intent(this, RoomsActivity::class.java)
            startActivity(intent)
        }
        gameAnalysis.setOnClickListener{
            GameData.setGameId("-Nx7FuNpkOU1tbBwYe7x")
            val intent = Intent(this, GameHistoryActivity::class.java)
            try {
                startActivity(intent)
            }
            catch (e: Exception)
            {
                val r = e.message
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}