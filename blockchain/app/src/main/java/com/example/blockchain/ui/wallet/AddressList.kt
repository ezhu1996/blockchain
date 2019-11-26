package com.example.blockchain.ui.wallet

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.blockchain.R


class AddressList(private val contxt: Context, internal var addresses: List<String>) : ArrayAdapter<String>(contxt, R.layout.address_list, addresses) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listViewItem = LayoutInflater.from(contxt).inflate(R.layout.address_list, null, false)

        val textViewAddress = listViewItem.findViewById<View>(R.id.textViewAddress) as TextView
        val textViewAmount = listViewItem.findViewById<View>(R.id.textViewAmount) as TextView

        val address = addresses[position]
        textViewAddress.text = address

        // call blockchain API here
        textViewAmount.text = 0.toString()//author.authorCountry

        return listViewItem
    }

}