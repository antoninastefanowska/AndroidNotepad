package com.tonia.notatnik;

import com.tonia.notatnik.Notatka;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NotatkiAPI {
    @GET("notatki/")
    Call<List<Notatka>> loadNotatki();
}
