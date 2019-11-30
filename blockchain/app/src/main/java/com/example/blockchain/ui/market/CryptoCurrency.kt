package com.example.blockchain.ui.market


class CryptoCurrency(
    private val currencyName: String,
    private val currencyValue: String,
    private val currencySymbol: String,
    private val change24Hours: String

    ) {
    fun getCurrencyName(): String {
        return currencyName
    }

    fun getCurrencyValue(): String {
        return currencyValue
    }

    fun getCurrencySymbol(): String {
        return currencySymbol
    }

    fun getChange24Hours(): String {
        return change24Hours
    }
}