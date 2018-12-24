package com.tonia.notatnik;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NotatkiAPI {
    @GET("notatki/")
    Call<List<Notatka>> loadNotatki();
}
