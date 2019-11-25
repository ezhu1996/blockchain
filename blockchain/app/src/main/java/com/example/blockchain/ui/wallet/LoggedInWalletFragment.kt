package com.example.blockchain.ui.wallet


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.blockchain.R

class LoggedInWalletFragment : Fragment() {


    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView: View? = inflater.inflate(R.layout.fragment_logged_in_wallet, container, false)


        return rootView


    }
}