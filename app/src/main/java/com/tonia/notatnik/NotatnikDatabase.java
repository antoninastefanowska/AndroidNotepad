package com.tonia.notatnik;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Notatka.class, Kategoria.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverters.class})
public abstract class NotatnikDatabase extends RoomDatabase {
    private static volatile NotatnikDatabase instance;

    public abstract NotatkiDao notatkiDao();
    public abstract KategorieDao kategorieDao();
    public static NotatnikDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (NotatnikDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), NotatnikDatabase.class, "notatnik").addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    getDatabase(context).kategorieDao().insert(stworzKategorie());
                                }
                            });
                        }
                    }).build();
                }
            }
        }
        return instance;
    }

    private static List<Kategoria> stworzKategorie() {
        List<Kategoria> kategorie = new ArrayList<Kategoria>();
        kategorie.add(new Kategoria(0, "Brak"));
        kategorie.add(new Kategoria(1, "Osobiste"));
        kategorie.add(new Kategoria(2, "Praca"));
        kategorie.add(new Kategoria(3, "Finanse"));
        kategorie.add(new Kategoria(4, "Plany"));
        return kategorie;
    }
}
