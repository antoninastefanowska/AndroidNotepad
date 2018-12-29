package com.tonia.notatnik;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class KategorieRepository {
    private KategorieDao kategorieDao;
    private LiveData<List<Kategoria>> kategorie;

    public KategorieRepository(Application application) {
        NotatnikDatabase db = NotatnikDatabase.getDatabase(application);
        kategorieDao = db.kategorieDao();
        kategorie = kategorieDao.getKategorie();
    }

    public LiveData<List<Kategoria>> getKategorie() { return kategorie; }

    public LiveData<Kategoria> getKategoria(long id) {
        LiveData<Kategoria> kategoria = null;
        try {
            kategoria = new GetKategoriaAsyncTask(kategorieDao).execute(id).get();
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return kategoria;
    }

    private static class GetKategoriaAsyncTask extends AsyncTask<Long, Void, LiveData<Kategoria>> {
        private KategorieDao kategorieDao;

        public GetKategoriaAsyncTask(KategorieDao kategorieDao) { this.kategorieDao = kategorieDao; }

        @Override
        protected LiveData<Kategoria> doInBackground(final Long... params) {
            return kategorieDao.getKategoria(params[0]);
        }
    }
}
