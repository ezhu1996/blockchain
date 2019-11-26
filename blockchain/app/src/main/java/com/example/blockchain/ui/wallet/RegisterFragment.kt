package com.example.blockchain.ui.wallet


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
import androidx.fragment.app.FragmentTransaction
import com.example.blockchain.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {
    private var userEmail: EditText? = null
    private var userPassWord: EditText? = null
    private var registerBtn: Button? = null
    private var userConfirmPassword: EditText? = null
    private var progressBar: ProgressBar? = null
    private var mAuth: FirebaseAuth? = null


    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView: View? = inflater.inflate(R.layout.fragment_register, container, false)

        mAuth = FirebaseAuth.getInstance()
        userEmail = rootView?.findViewById(R.id.username)
        userPassWord = rootView?.findViewById(R.id.password)
        userConfirmPassword = rootView?.findViewById(R.id.confirmPassword)
        registerBtn = rootView?.findViewById(R.id.register)
        registerBtn?.setOnClickListener {
            registerUserAccount()
        }

        return rootView
    }


    private fun registerUserAccount() {
        progressBar?.visibility = View.VISIBLE
        val email = userEmail?.text.toString()
        val password = userPassWord?.text.toString()
        val confirmPassword = userConfirmPassword?.text.toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(activity, "Please enter email...", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(activity, "Please enter password...", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(activity, "Please confirm password...", Toast.LENGTH_LONG).show()
            return
        }
        if (password != confirmPassword) {
            Toast.makeText(activity, "Passwords do not match...", Toast.LENGTH_LONG).show()
            return
        }

        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, "Registration successful!", Toast.LENGTH_LONG).show()
                progressBar?.visibility = View.GONE
                mAuth!!.signOut()
                val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                transaction.replace(this.id, LoginFragment())
                transaction.commit()
            } else {

                Toast.makeText(
                    activity,
                    task.exception.toString(),
                    Toast.LENGTH_LONG
                ).show()
                progressBar?.visibility = View.GONE
            }
        }
    }
}