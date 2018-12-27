package com.tonia.notatnik;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface KategorieDao {
    @Query("SELECT * FROM kategoria")
    LiveData<List<Kategoria>> getKategorie();

    @Query("SELECT * FROM kategoria WHERE id = :id")
    Kategoria getKategoria(long id);

    @Insert
    void insert(List<Kategoria> kategorie);
}
