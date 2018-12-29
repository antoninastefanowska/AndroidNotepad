package com.tonia.notatnik;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class NotatkiRepository {
    private NotatkiDao notatkiDao;
    private LiveData<List<Notatka>> notatki;

    public NotatkiRepository(Application application) {
        NotatnikDatabase db = NotatnikDatabase.getDatabase(application);
        notatkiDao = db.notatkiDao();
        notatki = notatkiDao.getNotatki();
    }

    public LiveData<List<Notatka>> getNotatki() {
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

    public LiveData<List<Notatka>> search(String query) {
        LiveData<List<Notatka>> result = null;
        try {
            result = new SearchAsyncTask(notatkiDao).execute(query).get();
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Long insert(Notatka notatka) {
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

    private static class SearchAsyncTask extends AsyncTask<String, Void, LiveData<List<Notatka>>> {
        private NotatkiDao notatkiDao;

        public SearchAsyncTask(NotatkiDao dao) { notatkiDao = dao; }

        @Override
        protected LiveData<List<Notatka>> doInBackground(String... params) {
            return notatkiDao.search(new SimpleSQLiteQuery(params[0]));
        }
    }

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
