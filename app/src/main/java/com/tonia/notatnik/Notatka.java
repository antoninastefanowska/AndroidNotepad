package com.tonia.notatnik;

import android.arch.persistence.room.ForeignKey;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Ignore;
import android.text.Html;
import android.text.SpannableString;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

@Entity(tableName = "notatka", foreignKeys = @ForeignKey(entity = Kategoria.class, parentColumns = "id", childColumns = "kategoria_id"))
public class Notatka extends BaseObservable implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "tytul")
    private String tytul;

    @ColumnInfo(name = "autor")
    private String autor;

    @ColumnInfo(name = "tekst")
    private String tekst;

    @ColumnInfo(name = "kategoria_id")
    private long kategoriaId;

    @ColumnInfo(name = "data_utworzenia")
    private Date dataUtworzenia;

    @ColumnInfo(name = "data_modyfikacji")
    private Date dataModyfikacji;

    @ColumnInfo(name = "wyroznienie")
    private boolean wyroznienie;

    @ColumnInfo(name = "zablokowana")
    private boolean zablokowana;

    @Ignore
    private boolean zaznaczona;

    public Notatka() {
        kategoriaId = 0;
        wyroznienie = false;
        dataUtworzenia = Calendar.getInstance().getTime();
        dataModyfikacji = Calendar.getInstance().getTime();
        tekst = "";
        autor = "Anonimowy";
        tytul = "[BEZ TYTUŁU]";
        zaznaczona = false;
        zablokowana = false;
    }

    public Notatka(Notatka notatka) { przepisz(notatka); }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    @Bindable
    public String getTytul() { return tytul; }

    @Bindable
    public void setTytul(String tytul) { this.tytul = tytul; }

    @Bindable
    public String getAutor() { return autor; }

    @Bindable
    public void setAutor(String autor) { this.autor = autor; }

    @Bindable
    public String getTekst() { return tekst; }

    @Bindable
    public void setTekst(String tekst) { this.tekst = tekst; }

    @Bindable
    public long getKategoriaId() { return kategoriaId; }

    @Bindable
    public void setKategoriaId(long kategoriaId) { this.kategoriaId = kategoriaId; }

    @Bindable
    public Date getDataUtworzenia() { return dataUtworzenia; }

    public void setDataUtworzenia(Date dataUtworzenia) { this.dataUtworzenia = dataUtworzenia; }

    @Bindable
    public Date getDataModyfikacji() { return dataModyfikacji; }

    public void setDataModyfikacji(Date dataModyfikacji) { this.dataModyfikacji = dataModyfikacji; }

    @Bindable
    public boolean getWyroznienie() { return wyroznienie; }

    @Bindable
    public void setWyroznienie(boolean wyroznienie)
    {
        this.wyroznienie = wyroznienie;
        notifyPropertyChanged(BR.notatka);
    }

    public boolean getZablokowana() { return zablokowana; }

    public void setZablokowana(boolean zablokowana)
    {
        this.zablokowana = zablokowana;
        notifyPropertyChanged(BR.notatka);
    }

    public void przepisz(Notatka notatka) {
        this.tytul = notatka.tytul;
        this.autor = notatka.autor;
        this.tekst = notatka.tekst;
        this.kategoriaId = notatka.kategoriaId;
        this.wyroznienie = notatka.wyroznienie;
        this.dataUtworzenia = notatka.dataUtworzenia;
        this.dataModyfikacji = Calendar.getInstance().getTime();
        this.zablokowana = notatka.zablokowana;
    }

    public boolean getZaznaczona() { return zaznaczona; }

    public void setZaznaczona(boolean zaznaczona)
    {
        this.zaznaczona = zaznaczona;
        notifyPropertyChanged(BR.notatka);
    }

    @Override
    public String toString() {
        return tytul;
    }

    public String skroconyTekst() {
        SpannableString tekst = new SpannableString(Html.fromHtml(this.tekst, 0));
        String s = tekst.toString();
        if (s.length() >= 80)
            return s.substring(0, 80).replace("\n", " ") + "...";
        else
            return s;
    }

    public String getDataUtworzeniaString() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(dataUtworzenia);
    }

    public String getDataModyfikacjiString() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(dataModyfikacji);
    }
}
