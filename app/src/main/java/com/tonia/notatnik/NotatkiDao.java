package com.tonia.notatnik;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.tonia.notatnik.Notatka;

import java.util.List;

@Dao
public interface NotatkiDao {
    @Query("SELECT * FROM notatka")
    LiveData<List<Notatka>> getAllData();

    @Query("SELECT COUNT(*) FROM notatka")
    int count();

    @Insert
    long insert(Notatka notatka);

    @Delete
    void delete(Notatka notatka);

    @Update
    void update(Notatka notatka);
}
