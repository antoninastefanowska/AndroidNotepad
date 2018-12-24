package com.tonia.notatnik;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {Notatka.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverters.class})
public abstract class NotatkiDatabase extends RoomDatabase {
    private static volatile NotatkiDatabase instance;

    public abstract NotatkiDao notatkiDao();
    public static NotatkiDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (NotatkiDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), NotatkiDatabase.class, "notatnik").build();
                }
            }
        }
        return instance;
    }
}
