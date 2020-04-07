package com.example.sale1996.simplemvvmapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FakeQuoteDao {
    private val quoteList = mutableListOf<Quote>()
    //mutable zato sto mozemo kasnije da ih menjamo
    private val quotes = MutableLiveData<List<Quote>>()

    //i kada budemo imali repository onda cemo observati quotes sa repository

    init {
        //ovo instanciramo samo da odma ubacimo vrednost..
        quotes.value = quoteList
    }

    fun addQuote(quote: Quote){
        quoteList.add(quote)
        //i kada se ovo izmeni onda ce se obavestiti svi koji osluskuju ovaj objekat
        quotes.value = quoteList
    }

    fun getQuotes() = quotes as LiveData<List<Quote>>

}