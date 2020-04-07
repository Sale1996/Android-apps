package com.example.sale1996.simplemvvmapp.data

class QuoteRepository private constructor(private val quoteDao: FakeQuoteDao) {

    fun addQuote(quote: Quote){
        quoteDao.addQuote(quote)
    }

    fun getQuotes() = quoteDao.getQuotes()

    companion object {
        //volatile znaci da su ispisi u ovom atributu vidljivi svim nitima
        @Volatile private var instance: QuoteRepository? = null

        //ovo vraca instancu ako nije null ako jeste onda pravi novu
        fun getInstance(quoteDao: FakeQuoteDao) =
            instance ?: synchronized(this){
                //ispitujemo jos jednom da li je neka druga nit mozda instancirala, ako nije
                //pravimo nasu instancu FakeDatabase() i dodeljujemo joj vrednost
                instance?: QuoteRepository(quoteDao).also { instance = it}
            }
    }
}