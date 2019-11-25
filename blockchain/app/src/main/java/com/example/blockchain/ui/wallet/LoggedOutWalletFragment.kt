package com.example.blockchain.ui.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.blockchain.R

class LoggedOutWalletFragment : Fragment() {


    private var registerBtn: Button? = null
    private var loginBtn: Button? = null


    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView: View? = inflater.inflate(R.layout.fragment_logged_out_wallet, container, false)
        registerBtn = rootView?.findViewById(R.id.registerScreen)
        loginBtn = rootView?.findViewById(R.id.loginScreen)

        registerBtn?.setOnClickListener {
            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(this.id, RegisterFragment())
            transaction.commit()
        }

        loginBtn?.setOnClickListener {
            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(this.id, LoginFragment())
            transaction.commit()
        }
        return rootView


    }
}