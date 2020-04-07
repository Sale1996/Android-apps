package com.example.sale1996.simplemvvmapp.data
/*
* Pravljenje singleton klase
* */
class FakeDatabase private constructor() {

    var quoteDao = FakeQuoteDao()
            private set

    companion object {
        //volatile znaci da su ispisi u ovom atributu vidljivi svim nitima
        @Volatile private var instance: FakeDatabase? = null

        //ovo vraca instancu ako nije null ako jeste onda pravi novu
        fun getInstance() =
                instance ?: synchronized(this){
                    //ispitujemo jos jednom da li je neka druga nit mozda instancirala, ako nije
                    //pravimo nasu instancu FakeDatabase() i dodeljujemo joj vrednost
                    instance?: FakeDatabase().also { instance = it}
                }
    }
}