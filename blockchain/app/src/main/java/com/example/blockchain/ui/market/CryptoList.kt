package com.example.blockchain.ui.market

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.blockchain.R


class CryptoList(
    private val contxt: Context,
    private var cryptos: MutableList<CryptoCurrency>
) :
    ArrayAdapter<CryptoCurrency>(contxt, R.layout.cryptocurrency_list, cryptos) {


    @SuppressLint("SetTextI18n", "ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listViewItem =
            LayoutInflater.from(contxt).inflate(R.layout.cryptocurrency_list, null, false)

        val textViewCryptoCurrencyName =
            listViewItem.findViewById<View>(R.id.textViewCryptoCurrencyName) as TextView
        val textViewCryptoCurrencySymbol =
            listViewItem.findViewById<View>(R.id.textViewCryptoCurrencySymbol) as TextView
        val textViewCryptoCurrencyAmount =
            listViewItem.findViewById<View>(R.id.textViewCryptoCurrencyAmount) as TextView
        val textViewCryptoCurrencyChange =
            listViewItem.findViewById<View>(R.id.textViewCryptoCurrencyChange) as TextView

        val cryptoCurrency = cryptos[position]
        val cryptoCurrencyName = cryptoCurrency.getCurrencyName()
        val cryptoCurrencySymbol = cryptoCurrency.getCurrencySymbol()
        val cryptoCurrencyAmount = cryptoCurrency.getCurrencyValue()
        val cryptoCurrencyChange = cryptoCurrency.getChange24Hours()
        textViewCryptoCurrencyAmount.setTextColor(Color.parseColor("#edeeef"))
        textViewCryptoCurrencySymbol.setTextColor(Color.parseColor("#a9a9a9"))
        textViewCryptoCurrencyName.setTextColor(Color.parseColor("#edeeef"))


        textViewCryptoCurrencyName.text = cryptoCurrencyName
        textViewCryptoCurrencySymbol.text = cryptoCurrencySymbol
        textViewCryptoCurrencyAmount.text =
            "$" + String.format("%.2f", cryptoCurrencyAmount.toDouble()).toDouble().toString()
        textViewCryptoCurrencyChange.text =
            String.format("%.3f", cryptoCurrencyChange.toDouble()).toDouble().toString() + "%"
        if (textViewCryptoCurrencyChange.text[0] == '-') {
            textViewCryptoCurrencyChange.setTextColor(Color.parseColor("#d23f31"))
        } else {
            textViewCryptoCurrencyChange.text = "+" + textViewCryptoCurrencyChange.text
            textViewCryptoCurrencyChange.setTextColor(Color.parseColor("#0f9d58"))
        }
        return listViewItem
    }
}