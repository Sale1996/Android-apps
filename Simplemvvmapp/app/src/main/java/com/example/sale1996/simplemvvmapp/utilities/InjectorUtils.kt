package com.example.sale1996.simplemvvmapp.utilities

import com.example.sale1996.simplemvvmapp.data.FakeDatabase
import com.example.sale1996.simplemvvmapp.data.QuoteRepository
import com.example.sale1996.simplemvvmapp.ui.quotes.QuotesViewModelFactory

/*
*
* Ovo je fajl u kome se nalaze svi injektori za depedency injection u ovom projektu..
*
* */
object InjectorUtils {

    fun provideQuotesViewModelFactory(): QuotesViewModelFactory {
        //dakle sav depedency koji nam treba je ubacen u ovoj liniji koda...
        val quoteRepository = QuoteRepository.getInstance(FakeDatabase.getInstance().quoteDao)
        return QuotesViewModelFactory(quoteRepository)
    }
}