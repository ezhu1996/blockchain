package com.example.blockchain.ui.swap

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.blockchain.R
import java.text.NumberFormat
import java.util.*


class CurrencyList(
    private val contxt: Context,
    private var currencies: MutableList<Currency>,
    private val amountBTC: String
) :
    ArrayAdapter<Currency>(contxt, R.layout.currency_list, currencies) {


    @SuppressLint("SetTextI18n", "ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listViewItem = LayoutInflater.from(contxt).inflate(R.layout.currency_list, null, false)

        val textViewCurrency = listViewItem.findViewById<View>(R.id.textViewCurrency) as TextView
        val textViewCurrencyAmount =
            listViewItem.findViewById<View>(R.id.textViewCurrencyAmount) as TextView

        val currency = currencies[position]
        val currencyName = currency.getCurrencyName()
        val currencyValue = currency.getCurrencyValue()
        val currencySymbol = currency.getCurrencySymbol()


        textViewCurrency.text = currencyName
        textViewCurrencyAmount.text = currencySymbol + NumberFormat.getNumberInstance(Locale.US).format(amountBTC.toFloat() * currencyValue.toFloat())
        return listViewItem
    }
}