package com.example.blockchain.ui.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.blockchain.R
import org.json.JSONObject
import java.io.FileNotFoundException
import java.net.URL

class MarketFragment : Fragment() {
    private lateinit var rootView: View
    private lateinit var cryptoList: MutableList<CryptoCurrency>
    private lateinit var myListView: ListView
    private lateinit var marketViewModel: MarketViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_market, container, false)
        initialize()
        return rootView
    }

    private fun initialize() {
        cryptoList = ArrayList()
        myListView = rootView.findViewById(R.id.cryptoCurrencies)
        val cryptoThread = Thread {
            try {
                val market =
                    JSONObject(URL("https://api.coincap.io/v2/assets").readText()).getJSONArray("data")

                (context as FragmentActivity).runOnUiThread {
                    for (i in 0 until market.length()) {
                        val crypto = JSONObject(market.get(i).toString())
                        cryptoList.add(
                            CryptoCurrency(
                                crypto.getString("name"),
                                crypto.getString("priceUsd"),
                                crypto.getString("symbol"),
                                crypto.getString("changePercent24Hr")
                            )
                        )
                        val cryptoCurrencyListAdapter =
                            CryptoList(activity!!, cryptoList)
                        //attaching adapter to the listview
                        myListView.adapter = cryptoCurrencyListAdapter


                    }
                }
            } catch (e: FileNotFoundException) {

            }
        }
        cryptoThread.start()
        cryptoThread.join()
    }
}