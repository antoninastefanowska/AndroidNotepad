package com.tonia.notatnik;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotatkiController implements Callback<List<Notatka>> {
    private static final String BASE_URL = "http://my-json-server.typicode.com/toniaa/BazaNotatek/";
    private NotatkiAdapter notatkiAdapter;

    public NotatkiController(NotatkiAdapter notatkiAdapter) {
        this.notatkiAdapter = notatkiAdapter;
    }

    public void start() {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        NotatkiAPI notatkiAPI = retrofit.create(NotatkiAPI.class);
        Call<List<Notatka>> call = notatkiAPI.loadNotatki();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Notatka>> call, Response<List<Notatka>> response) {
        if (response.isSuccessful()) {
            notatkiAdapter.setData(response.body());
            notatkiAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailure(Call<List<Notatka>> call, Throwable t) {
        t.printStackTrace();
    }
}
