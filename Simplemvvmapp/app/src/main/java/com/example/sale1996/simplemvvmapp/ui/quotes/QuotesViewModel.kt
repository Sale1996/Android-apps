package com.example.sale1996.simplemvvmapp.ui.quotes

import androidx.lifecycle.ViewModel
import com.example.sale1996.simplemvvmapp.data.Quote
import com.example.sale1996.simplemvvmapp.data.QuoteRepository

class QuotesViewModel(private val quoteRepository: QuoteRepository) : ViewModel() {

    fun getQuotes() = quoteRepository.getQuotes()

    fun addQuote(quote : Quote) = quoteRepository.addQuote(quote)

}