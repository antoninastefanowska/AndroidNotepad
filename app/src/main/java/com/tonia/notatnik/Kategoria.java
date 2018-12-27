package com.tonia.notatnik;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Color;

import java.io.Serializable;

@Entity(tableName = "kategoria")
public class Kategoria implements Serializable {
    @PrimaryKey
    private long id;

    @ColumnInfo(name = "nazwa")
    private String nazwa;

    @Ignore
    private Color kolor;

    public Kategoria()
    {
        this.id = 0;
        this.nazwa = "Brak";
    }

    public Kategoria(long id, String nazwa)
    {
        this.id = id;
        this.nazwa = nazwa;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getNazwa() { return nazwa; }

    public void setNazwa(String nazwa) { this.nazwa = nazwa; }

    public Color getKolor() { return kolor; }

    public void setKolor(Color kolor) { this.kolor = kolor; }

    public String toString() { return nazwa; }
}
