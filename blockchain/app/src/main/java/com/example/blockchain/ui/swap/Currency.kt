package com.example.blockchain.ui.swap

class Currency(
    private val currencyName: String,
    private val currencyValue: String,
    private val currencySymbol: String
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
}