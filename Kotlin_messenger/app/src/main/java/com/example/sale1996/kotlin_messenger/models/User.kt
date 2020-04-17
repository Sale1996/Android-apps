package com.example.sale1996.kotlin_messenger.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
* Klasa iznad ima anotaciju @Parcelize jer se u jednom momentu preko intenta
* prebacivao ceo objekat umesto njegovi atributi ponaosob, ovo je losa praksa,
* jer konvertovanje uzima dosta resursa.
*
* Prazan konstruktor pored konstruktora sa parametrima je napravljen zbog firebase biblioteke
* Prilikom fetchovanja podataka sa firebase njemu je neophodan konstruktor bez parametra kako bi
* omogucio automatsko konvertovanje podataka u objekat koji smo mi naveli
*
* */
@Parcelize
class User(val uid: String, val username: String, val profileImageUrl: String): Parcelable{

    constructor(): this("","","")
}