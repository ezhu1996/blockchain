package com.example.blockchain

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var accountInfo: SharedPreferences
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    lateinit var navView: BottomNavigationView
    lateinit var editor: SharedPreferences.Editor
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()



        if (mAuth.currentUser != null) { // if they are already logged in
            navView = findViewById(R.id.nav_view_logged_in)
            navController = findNavController(R.id.nav_host_fragment_logged_in)
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_market, R.id.navigation_swap, R.id.navigation_mywallet
                )
            )
            editor.putBoolean("loggedIn", true)
        } else { // they are not logged in
            navView = findViewById(R.id.nav_view_logged_out)
            navController = findNavController(R.id.nav_host_fragment_logged_out)
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_market, R.id.navigation_swap, R.id.navigation_wallet
                )
            )
            editor.putBoolean("loggedIn", false)
        }
        editor.apply()
        // load proper nav bar
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
