package com.example.blockchain.ui.swap

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.blockchain.R
import java.io.FileNotFoundException
import java.net.URL

class SwapFragment : Fragment() {

    private lateinit var selectedAddressTitle: TextView
    private lateinit var rootView: View
    private lateinit var accountInfo: SharedPreferences
    private lateinit var selectedAddress: String
    private lateinit var swapAmount: EditText
    private lateinit var selectedAddressAmount: String
    private lateinit var currenciesList: MutableList<Currency>
    private lateinit var myListView: ListView
    private lateinit var refreshButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_swap, container, false)
        initialize()

        // event listener for new swap inquiry
        swapAmount.addTextChangedListener {
            var inquiry = swapAmount.text.toString()
            if (inquiry.isNotEmpty()) {
                currenciesList.clear()
                if (inquiry[inquiry.length - 1] == '.') inquiry =
                    inquiry.substring(0, inquiry.length - 1)
                val newInquiryThread = createThread(inquiry)
                newInquiryThread.start()
                newInquiryThread.join()
            }
        }

        // event listener for refresh
        refreshButton.setOnClickListener {
            refreshToCurrentAddressAmount()
        }

        return rootView
    }

    private fun refreshToCurrentAddressAmount() {
        swapAmount.setText(selectedAddressAmount)
    }

    @SuppressLint("SetTextI18n")
    private fun initialize() {
        refreshButton = rootView.findViewById(R.id.refresh)
        currenciesList = ArrayList()
        myListView = rootView.findViewById(R.id.currencies)
        selectedAddressTitle = rootView.findViewById(R.id.addressTitle)
        accountInfo = activity!!.getSharedPreferences("accountInfo", Context.MODE_PRIVATE)
        selectedAddress = accountInfo.getString("selectedAddress", "").toString()
        swapAmount = rootView.findViewById(R.id.amountBTC)
        if (selectedAddress != "") {
            selectedAddressTitle.text = "Current Selected Address: $selectedAddress"

            val myAmountThread = Thread {
                val amount =
                    URL("https://blockchain.info/q/addressbalance/$selectedAddress").readText()
                (context as FragmentActivity).runOnUiThread {
                    selectedAddressAmount = (amount.toFloat() / 100000000.0).toString()
                    swapAmount.setText(selectedAddressAmount)
                }
            }

            myAmountThread.start()
            myAmountThread.join()
        }

        // convert btc to other currencies using current value of swapAmount
        val currencyThread = createThread(swapAmount.text.toString())
        currencyThread.start()
        currencyThread.join()
    }

    private fun createThread(inquiry: String): Thread {
        return Thread {
            try {
                var currenciesString =
                    URL("https://blockchain.info/ticker").readText()
                currenciesString =
                    currenciesString.substring(1, currenciesString.length - 1).replace(" ", "")
                val currencies = currenciesString.split("\n")
                (context as FragmentActivity).runOnUiThread {
                    // bootleg json parser
                    for (currency in currencies) {
                        if (!TextUtils.isEmpty(currency)) {
                            val currencyName = currency.substring(1, 4)
                            val currencyValue = currency.substring(13, currency.indexOf(","))
                            val currencySymbol = currency.substring(
                                currency.lastIndexOf(":") + 2, currency.lastIndexOf("\"")
                            )
                            currenciesList.add(
                                Currency(
                                    currencyName,
                                    currencyValue,
                                    currencySymbol
                                )
                            )
                            val currencyListAdapter =
                                CurrencyList(activity!!, currenciesList, inquiry)
                            //attaching adapter to the listview
                            myListView.adapter = currencyListAdapter
                        }
                    }
                }
            } catch (e: FileNotFoundException) {

            }
        }
    }
}