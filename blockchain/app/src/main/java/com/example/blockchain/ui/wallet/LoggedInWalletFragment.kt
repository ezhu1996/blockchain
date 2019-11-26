package com.example.blockchain.ui.wallet


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.blockchain.MainActivity
import com.example.blockchain.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class LoggedInWalletFragment : Fragment() {


    private var signoutBtn: Button? = null
    private var mAuth: FirebaseAuth? = null

    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView: View? = inflater.inflate(R.layout.fragment_logged_in_wallet, container, false)

        mAuth = FirebaseAuth.getInstance()
        signoutBtn = rootView?.findViewById(R.id.signout)
        signoutBtn!!.setOnClickListener {
            mAuth!!.signOut()
            val accountInfo: SharedPreferences =
                activity!!.getSharedPreferences("accountInfo", Context.MODE_PRIVATE)

            // read in firebase items and put into sharedpreferences
            val editor: SharedPreferences.Editor = accountInfo.edit()
            editor.putBoolean("loggedIn", false)
            editor.apply()

            // go back to main activity
            activity!!.startActivity(Intent(activity!!, MainActivity::class.java))
        }



        return rootView


    }
}