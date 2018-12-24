package com.tonia.notatnik;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class NotatkiViewModel extends AndroidViewModel {
    private NotatkiRepository notatkiRepository;
    private LiveData<List<Notatka>> notatki;

    public NotatkiViewModel(@NonNull Application application) {
        super(application);
        notatkiRepository = new NotatkiRepository(application);
        notatki = notatkiRepository.getAllData();
    }

    public LiveData<List<Notatka>> getAllData() {
        return notatki;
    }

    public int count() {
        return notatkiRepository.count();
    }

    public void insert(Notatka notatka) {
        notatkiRepository.insert(notatka);
    }

    public void delete(Notatka notatka) { notatkiRepository.delete(notatka); }

    public void update(Notatka notatka) { notatkiRepository.update(notatka); }
}
