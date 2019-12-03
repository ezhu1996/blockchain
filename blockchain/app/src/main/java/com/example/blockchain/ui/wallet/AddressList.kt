package com.example.blockchain.ui.wallet

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.blockchain.R
import java.io.FileNotFoundException
import java.net.URL


class AddressList(
    private val contxt: Context,
    private var addresses: MutableList<String>,
    private var selected: String
) :
    ArrayAdapter<String>(contxt, R.layout.address_list, addresses) {


    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listViewItem = LayoutInflater.from(contxt).inflate(R.layout.address_list, null, false)

        val textViewAddress = listViewItem.findViewById<View>(R.id.textViewAddress) as TextView
        val textViewAmount = listViewItem.findViewById<View>(R.id.textViewAmount) as TextView

        val address = addresses[position]

        // call blockchain API here
        Thread {
            try {
                val amount =
                    URL("https://blockchain.info/q/addressbalance/$address").readText()
                (context as FragmentActivity).runOnUiThread {
                    if (address == selected) {
                        listViewItem.setBackgroundColor(Color.parseColor("#303437"))
                    }
                    textViewAmount.text = (amount.toFloat() / 100000000.0).toString()
                    textViewAddress.text = address
                }
            } catch (e: FileNotFoundException) {
                (context as FragmentActivity).runOnUiThread {
                    addresses.remove(address)
                    Toast.makeText(
                        context as FragmentActivity,
                        "Invalid Address: $address!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }.start()
        return listViewItem
    }
}