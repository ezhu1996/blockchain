package com.example.blockchain.ui.wallet


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.blockchain.MainActivity
import com.example.blockchain.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginFragment : Fragment() {

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var userEmail: EditText? = null
    private var userPassWord: EditText? = null
    private var loginBtn: Button? = null
    private var progressBar: ProgressBar? = null
    private var mAuth: FirebaseAuth? = null


    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView: View? = inflater.inflate(R.layout.fragment_login, container, false)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Email")
        mAuth = FirebaseAuth.getInstance()
        userEmail = rootView?.findViewById(R.id.username)
        userPassWord = rootView?.findViewById(R.id.password)
        loginBtn = rootView?.findViewById(R.id.login)
        loginBtn?.setOnClickListener {
            loginUserAccount()
        }


        return rootView
    }


    private fun loginUserAccount() {
        progressBar?.visibility = View.VISIBLE
        val email = userEmail!!.text.toString()
        val password = userPassWord!!.text.toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(activity, "Please enter email...", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(activity, "Please enter password...", Toast.LENGTH_LONG).show()
            return
        }

        val accountInfo: SharedPreferences =
            activity!!.getSharedPreferences("accountInfo", Context.MODE_PRIVATE)


        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // success
                Toast.makeText(activity, "Login successful!", Toast.LENGTH_LONG).show()
                progressBar?.visibility = View.GONE

                // read in firebase items and put into sharedpreferences
                val editor: SharedPreferences.Editor = accountInfo.edit()
                editor.putBoolean("loggedIn", true)
                editor.apply()

                // go back to main activity
                activity!!.startActivity(Intent(activity!!, MainActivity::class.java))
            } else {
                // failure
                Toast.makeText(activity, "Wrong email and password combination.", Toast.LENGTH_LONG)
                    .show()
                progressBar?.visibility = View.GONE
            }
        }

    }


}