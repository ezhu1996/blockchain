package com.example.blockchain.ui.wallet


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.blockchain.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginFragment : Fragment() {

    private lateinit var walletViewModel: WalletViewModel
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



        userEmail = rootView?.findViewById(R.id.username)
        userPassWord = rootView?.findViewById(R.id.password)
        loginBtn = rootView?.findViewById(R.id.login)
        loginBtn?.setOnClickListener {
            if (rootView?.id == R.id.login) {
                loginUserAccount()
            }
        }

        mAuth


        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loginBtn!!.setOnClickListener { loginUserAccount() }

    }

    private fun loginUserAccount() {
        progressBar?.visibility = View.VISIBLE
        val email = userEmail?.text.toString()
        val password = userPassWord?.text.toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(activity, "Please enter email...", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(activity, "Please enter password...", Toast.LENGTH_LONG).show()
            return
        }

        /* mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
             if (task.isSuccessful) {
                 Toast.makeText(activity, "Login successful!", Toast.LENGTH_LONG).show()
                 progressBar?.visibility = View.GONE
                 val intent = Intent(this@LoginFragment, MarketFragment::class.java)
                 intent.putExtra(userEmail, mAuth?.currentUser?.email);
                 startActivity(intent)
             }
         }*/

    }


}