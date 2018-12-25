package com.tonia.notatnik;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Filtr extends BaseObservable implements Serializable {
    private boolean tytulWarunek;
    private boolean tytulDokladny;
    private boolean tytulZawiera;
    private String tytul;

    private boolean autorWarunek;
    private String autor;

    private boolean slowaKluczoweWarunek;
    private boolean slowaKluczoweWszystkie;
    private boolean slowaKluczoweJakiekolwiek;
    private String slowaKluczowe;

    private boolean kategoriaWarunek;
    private String kategoria;

    private boolean dataUtworzeniaWarunek;
    private boolean dataUtworzeniaOdWarunek;
    private boolean dataUtworzeniaDoWarunek;
    private Date dataUtworzeniaOd;
    private Date dataUtworzeniaDo;

    private boolean dataModyfikacjiWarunek;
    private boolean dataModyfikacjiOdWarunek;
    private boolean dataModyfikacjiDoWarunek;
    private Date dataModyfikacjiOd;
    private Date dataModyfikacjiDo;

    private boolean wyroznienieWarunek;
    private boolean wyroznienie;

    public Filtr() {
        dataUtworzeniaOd = dataUtworzeniaDo = dataModyfikacjiOd = dataModyfikacjiDo = Calendar.getInstance().getTime();
        tytulDokladny = true;
        slowaKluczoweWszystkie = true;
        wyroznienie = true;
    }

    @Bindable
    public boolean getTytulWarunek() { return tytulWarunek; }

    @Bindable
    public void setTytulWarunek(boolean tytulWarunek) {
        this.tytulWarunek = tytulWarunek;
        notifyPropertyChanged(BR.tytulWarunek);
    }

    @Bindable
    public boolean getTytulDokladny() { return tytulDokladny; }

    @Bindable
    public void setTytulDokladny(boolean tytulDokladny) { this.tytulDokladny = tytulDokladny; }

    @Bindable
    public boolean getTytulZawiera() { return tytulZawiera; }

    @Bindable
    public void setTytulZawiera(boolean tytulZawiera) { this.tytulZawiera = tytulZawiera; }

    @Bindable
    public String getTytul() { return tytul; }

    @Bindable
    public void setTytul(String tytul) { this.tytul = tytul; }

    @Bindable
    public boolean getAutorWarunek() { return autorWarunek; }

    @Bindable
    public void setAutorWarunek(boolean autorWarunek) {
        this.autorWarunek = autorWarunek;
        notifyPropertyChanged(BR.autorWarunek);
    }

    @Bindable
    public String getAutor() { return autor; }

    @Bindable
    public void setAutor(String autor) { this.autor = autor; }

    @Bindable
    public boolean getSlowaKluczoweWarunek() { return slowaKluczoweWarunek; }

    @Bindable
    public void setSlowaKluczoweWarunek(boolean slowaKluczoweWarunek) {
        this.slowaKluczoweWarunek = slowaKluczoweWarunek;
        notifyPropertyChanged(BR.slowaKluczoweWarunek);
    }

    @Bindable
    public boolean getSlowaKluczoweWszystkie() { return slowaKluczoweWszystkie; }

    @Bindable
    public void setSlowaKluczoweWszystkie(boolean slowaKluczoweWszystkie) { this.slowaKluczoweWszystkie = slowaKluczoweWszystkie; }

    @Bindable
    public boolean getSlowaKluczoweJakiekolwiek() { return slowaKluczoweJakiekolwiek; }

    @Bindable
    public void setSlowaKluczoweJakiekolwiek(boolean slowaKluczoweJakiekolwiek) { this.slowaKluczoweJakiekolwiek = slowaKluczoweJakiekolwiek; }

    @Bindable
    public String getSlowaKluczowe() { return slowaKluczowe; }

    @Bindable
    public void setSlowaKluczowe(String slowaKluczowe) { this.slowaKluczowe = slowaKluczowe; }

    @Bindable
    public boolean getKategoriaWarunek() { return kategoriaWarunek; }

    @Bindable
    public void setKategoriaWarunek(boolean kategoriaWarunek) {
        this.kategoriaWarunek = kategoriaWarunek;
        notifyPropertyChanged(BR.kategoriaWarunek);
    }

    @Bindable
    public String getKategoria() { return kategoria; }

    @Bindable
    public void setKategoria(String kategoria) { this.kategoria = kategoria; }

    @Bindable
    public boolean getDataUtworzeniaWarunek() { return dataUtworzeniaWarunek; }

    @Bindable
    public void setDataUtworzeniaWarunek(boolean dataUtworzeniaWarunek) {
        this.dataUtworzeniaWarunek = dataUtworzeniaWarunek;
        notifyPropertyChanged(BR.dataUtworzeniaWarunek);
    }

    @Bindable
    public boolean getDataUtworzeniaOdWarunek() { return dataUtworzeniaOdWarunek; }

    @Bindable
    public void setDataUtworzeniaOdWarunek(boolean dataUtworzeniaOdWarunek) {
        this.dataUtworzeniaOdWarunek = dataUtworzeniaOdWarunek;
        notifyPropertyChanged(BR.dataUtworzeniaOdWarunek);
    }

    @Bindable
    public boolean getDataUtworzeniaDoWarunek() { return dataUtworzeniaDoWarunek; }

    @Bindable
    public void setDataUtworzeniaDoWarunek(boolean dataUtworzeniaDoWarunek) {
        this.dataUtworzeniaDoWarunek = dataUtworzeniaDoWarunek;
        notifyPropertyChanged(BR.dataUtworzeniaDoWarunek);
    }

    @Bindable
    public long getDataUtworzeniaOd() { return DateConverters.toTimestamp(dataUtworzeniaOd); }

    @Bindable
    public void setDataUtworzeniaOd(long dataUtworzeniaOd) { this.dataUtworzeniaOd = DateConverters.toDate(dataUtworzeniaOd); }

    @Bindable
    public long getDataUtworzeniaDo() { return DateConverters.toTimestamp(dataUtworzeniaDo); }

    @Bindable
    public void setDataUtworzeniaDo(long dataUtworzeniaDo) { this.dataUtworzeniaDo = DateConverters.toDate(dataUtworzeniaDo); }

    @Bindable
    public boolean getDataModyfikacjiWarunek() { return dataModyfikacjiWarunek; }

    @Bindable
    public void setDataModyfikacjiWarunek(boolean dataModyfikacjiWarunek) {
        this.dataModyfikacjiWarunek = dataModyfikacjiWarunek;
        notifyPropertyChanged(BR.dataModyfikacjiWarunek);
    }

    @Bindable
    public boolean getDataModyfikacjiOdWarunek() { return dataModyfikacjiOdWarunek; }

    @Bindable
    public void setDataModyfikacjiOdWarunek(boolean dataModyfikacjiOdWarunek) {
        this.dataModyfikacjiOdWarunek = dataModyfikacjiOdWarunek;
        notifyPropertyChanged(BR.dataModyfikacjiOdWarunek);
    }

    @Bindable
    public boolean getDataModyfikacjiDoWarunek() { return dataModyfikacjiDoWarunek; }

    @Bindable
    public void setDataModyfikacjiDoWarunek(boolean dataModyfikacjiDoWarunek) {
        this.dataModyfikacjiDoWarunek = dataModyfikacjiDoWarunek;
        notifyPropertyChanged(BR.dataModyfikacjiDoWarunek);
    }

    @Bindable
    public long getDataModyfikacjiOd() { return DateConverters.toTimestamp(dataModyfikacjiOd); }

    @Bindable
    public void setDataModyfikacjiOd(long dataModyfikacjiOd) { this.dataModyfikacjiOd = DateConverters.toDate(dataModyfikacjiOd); }

    @Bindable
    public long getDataModyfikacjiDo() { return DateConverters.toTimestamp(dataModyfikacjiDo); }

    @Bindable
    public void setDataModyfikacjiDo(long dataModyfikacjiDo) { this.dataModyfikacjiDo = DateConverters.toDate(dataModyfikacjiDo); }

    @Bindable
    public boolean getWyroznienieWarunek() { return wyroznienieWarunek; }

    @Bindable
    public void setWyroznienieWarunek(boolean wyroznienieWarunek) {
        this.wyroznienieWarunek = wyroznienieWarunek;
        notifyPropertyChanged(BR.wyroznienieWarunek);
    }

    @Bindable
    public boolean getWyroznienie() { return wyroznienie; }

    @Bindable
    public void setWyroznienie(boolean wyroznienie) { this.wyroznienie = wyroznienie; }

    public boolean czyPasuje(Notatka notatka) {
        if (tytulWarunek) {
            if (tytulDokladny) {
                if (!tytul.equals(notatka.getTytul())) return false;
            }
            else if (tytulZawiera) {
                if (!notatka.getTytul().toLowerCase().contains(tytul.toLowerCase())) return false;
            }
        }

        if (autorWarunek)
            if (autor != notatka.getAutor()) return false;

        if (slowaKluczoweWarunek) {
            String[] slowa = slowaKluczowe.split(" ");
            String tekst = notatka.getTekst().toLowerCase();
            if (slowaKluczoweWszystkie) {
                for (String slowo : slowa)
                    if (!tekst.contains(slowo)) return false;
            }
            else if (slowaKluczoweJakiekolwiek) {
                int i;
                for (i = 0; i < slowa.length; i++)
                    if (tekst.contains(slowa[i])) break;
                if (i == slowa.length) return false;
            }
        }

        if (kategoriaWarunek)
            if (kategoria != notatka.getKategoria()) return false;

        if (dataUtworzeniaWarunek) {
            if (dataUtworzeniaOdWarunek)
                if (dataUtworzeniaOd.after(notatka.getDataUtworzenia())) return false;

            if (dataUtworzeniaDoWarunek)
                if (dataUtworzeniaDo.before(notatka.getDataUtworzenia())) return false;
        }

        if (dataModyfikacjiWarunek) {
            if (dataModyfikacjiOdWarunek)
                if (dataModyfikacjiOd.after(notatka.getDataModyfikacji())) return false;

            if (dataModyfikacjiDoWarunek)
                if (dataModyfikacjiDo.before(notatka.getDataModyfikacji())) return false;
        }

        if (wyroznienieWarunek)
            if (wyroznienie != notatka.getWyroznienie()) return false;

        return true;
    }
}
