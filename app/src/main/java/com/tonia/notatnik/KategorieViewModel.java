package com.tonia.notatnik;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class KategorieViewModel extends AndroidViewModel {
    private KategorieRepository kategorieRepository;
    private LiveData<List<Kategoria>> kategorie;

    public KategorieViewModel(@NonNull Application application) {
        super(application);
        kategorieRepository = new KategorieRepository(application);
        kategorie = kategorieRepository.getKategorie();
    }

    public LiveData<List<Kategoria>> getKategorie() { return kategorie; }

    public LiveData<Kategoria> getKategoria(long id) { return kategorieRepository.getKategoria(id); }
}
