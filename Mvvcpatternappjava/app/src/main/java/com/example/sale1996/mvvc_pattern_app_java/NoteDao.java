package com.example.sale1996.mvvc_pattern_app_java;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/*
* Ovo notiramo kao interfejs ili abstraktnu klasu, jer zelimo samo da naglasimo
* koje metode zelimo da imamo i onda da prepustimo Room-u da nam on sam njih implementira
* */

@Dao
public interface NoteDao {

    //kada kliknemo CTRL+B odlazimo u javinu impelemtaciju ovih anotacija, gde vidimo da mozemo
    //kao argumente funkcije mozemo unostiti razlicite kombinacije ulaza
    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    /*
    * Zasto je dobar ROOM?
    *
    * Jedan od razloga jeste sto nam nece dozvoliti da buildamo aplikaciju ukoliko
    * ima neka "type" greska u kodu.. ukoliko npr unesemo leksicku gresku prilikom formiranja
    * sparq upita, ili probamo da unesemo pogresan naziv tabele (na osnovu entiteta, on provaljuje
    * koji nazivi postoje)..
    * */

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    //Ovde vidimo da kao povratnu vrednost mozemo vratiti LiveData.. sto znaci kada se podatak
    //promeni u bazi, da ce nas program to uhvatiti i azurirati se
    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    LiveData<List<Note>> getAllNotes();

}
