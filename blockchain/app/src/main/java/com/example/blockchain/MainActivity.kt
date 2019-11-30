package com.example.blockchain

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var accountInfo: SharedPreferences
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var navView: BottomNavigationView
    private lateinit var editor: SharedPreferences.Editor
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()

        if (mAuth.currentUser != null) { // if they are already logged in
            // update shared preferences
            editor.putBoolean("loggedIn", true)
            editor.apply()


            // hide the logged out nav bar
            findViewById<View>(R.id.nav_host_fragment_logged_out).visibility = View.GONE
            findViewById<BottomNavigationView>(R.id.nav_view_logged_out).visibility = View.GONE
            findViewById<BottomNavigationView>(R.id.nav_view_logged_in).visibility = View.VISIBLE

            // set up the logged in nav bar
            navView = findViewById(R.id.nav_view_logged_in)
            navController = findNavController(R.id.nav_host_fragment_logged_in)
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_market, R.id.navigation_swap, R.id.navigation_mywallet
                )
            )
        } else { // they are not logged in
            // update shared preferences
            editor.putBoolean("loggedIn", false)
            editor.apply()

            // hide the logged in nav bar
            findViewById<View>(R.id.nav_host_fragment_logged_in).visibility = View.GONE
            findViewById<BottomNavigationView>(R.id.nav_view_logged_in).visibility = View.GONE
            findViewById<BottomNavigationView>(R.id.nav_view_logged_out).visibility = View.VISIBLE

            // set up the logged out nav bar
            navView = findViewById(R.id.nav_view_logged_out)
            navController = findNavController(R.id.nav_host_fragment_logged_out)
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_market, R.id.navigation_swap, R.id.navigation_wallet
                )
            )
        }
        //load proper nav bar
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    @SuppressLint("CommitPrefEdits")
    private fun initialize() {
        accountInfo = getSharedPreferences("accountInfo", Context.MODE_PRIVATE)
        mAuth = FirebaseAuth.getInstance()
        editor = accountInfo.edit()
    }
}
