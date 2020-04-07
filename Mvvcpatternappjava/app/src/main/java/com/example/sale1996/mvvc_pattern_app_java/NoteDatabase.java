package com.example.sale1996.mvvc_pattern_app_java;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

//ukoliko imamo vise entiteta, samo ih zarezom delimo u ovoj viticastoj zagradi...
@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    //synchronized ide jer zelimo da samo jedna nit moze da poziva ovo u jednom momentu
    //da ne bude da u isto vreme 2 niti pozovu pa da imamo vise od 1 instance..
    public static synchronized NoteDatabase getInstance(Context context){
            if(instance == null){
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        NoteDatabase.class, "note_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build();
                /*
                * Ovako posto ova klasa ne moze da se instancira, moramo koristiti databaseBuilder od
                * Room-a, gde mu prosledjujemo app context, klasu koju zelimo da instanciramo i koji
                * zelimo naziv da bude ovoj bazi podataka...
                *
                * fallbackToDestructiveMigration() <= sa ovim kada dobijemo novu verziju baze podataka
                * se stitimo da nam se aplikacija ne rusi u runTime-u... sa ovim kolko sam skontao
                * on brise celu bazu podataka i pravi novu svaki put kada to treba da uradi..
                * */
            }
            return instance;
    };


    /*
    * Recimo zelimo da dodamo neke podatke bazi podataka prilikom kreiranja
    * Onda moramo da dodamo "CallBack" objekat prilikom buildovanja baze.. a u tom objektu
    * mozemo da kontrolisemo DAO objekat i insertujemo nase podatke
    * */

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //Ovde pravimo novi async task, jer jelte ROOM ne dozvoljava rad sa bazom u glavnoj niti
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDatabase db){
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Naslov 1", "Opis 1", 1));
            noteDao.insert(new Note("Naslov 2", "Opis 2", 2));
            noteDao.insert(new Note("Naslov 3", "Opis 3", 3));
            return null;
        }
    }

}
