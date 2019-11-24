package com.example.blockchain.ui.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.blockchain.R

class WalletFragment : Fragment() {


    private var registerBtn: Button? = null
    private var loginBtn: Button? = null


    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView: View? = inflater.inflate(R.layout.fragment_wallet, container, false)
        registerBtn = rootView?.findViewById(R.id.registerScreen)
        loginBtn = rootView?.findViewById(R.id.loginScreen)

        registerBtn?.setOnClickListener {

            //            val intent = Intent(this@WalletFragment, RegistrationActivity::class.java)
//            startActivity(intent)
        }

        loginBtn?.setOnClickListener {
            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(this.id, LoginFragment())
            transaction.commit()

            //            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            //            startActivity(intent)
        }
        return rootView


    }
}