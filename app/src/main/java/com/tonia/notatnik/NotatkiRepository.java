package com.tonia.notatnik;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class NotatkiRepository {
    private NotatkiDao notatkiDao;
    private LiveData<List<Notatka>> notatki;

    public NotatkiRepository(Application application) {
        NotatkiDatabase db = NotatkiDatabase.getDatabase(application);
        notatkiDao = db.notatkiDao();
        notatki = notatkiDao.getAllData();
    }

    public LiveData<List<Notatka>> getAllData() {
        return notatki;
    }

    public int count() {
        int result = 0;
        try {
            result = new CountAsyncTask(notatkiDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class CountAsyncTask extends AsyncTask<Void, Void, Integer> {
        private NotatkiDao notatkiDao;

        public CountAsyncTask(NotatkiDao dao) {
            notatkiDao = dao;
        }

        @Override
        protected Integer doInBackground(final Void... params) {
            return notatkiDao.count();
        }
    }

    Long insert(Notatka notatka) {
        Long id = null;
        try {
            id = new InsertAsyncTask(notatkiDao).execute(notatka).get();
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void delete(Notatka notatka) { new DeleteAsyncTask(notatkiDao).execute(notatka); }

    public void update(Notatka notatka) { new UpdateAsyncTask(notatkiDao).execute(notatka); }

    private static class InsertAsyncTask extends AsyncTask<Notatka, Void, Long> {
        private NotatkiDao notatkiDao;

        public InsertAsyncTask(NotatkiDao dao) {
            notatkiDao = dao;
        }

        @Override
        protected Long doInBackground(final Notatka... params) {
            return notatkiDao.insert(params[0]);
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Notatka, Void, Void> {
        private NotatkiDao notatkiDao;

        public DeleteAsyncTask(NotatkiDao dao) { notatkiDao = dao; }

        @Override
        protected Void doInBackground(final Notatka... params) {
            notatkiDao.delete(params[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Notatka, Void, Void> {
        private NotatkiDao notatkiDao;

        public UpdateAsyncTask(NotatkiDao dao) { notatkiDao = dao; }

        @Override
        protected Void doInBackground(final Notatka... params) {
            notatkiDao.update(params[0]);
            return null;
        }
    }
}
