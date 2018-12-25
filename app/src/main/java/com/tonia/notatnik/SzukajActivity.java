package com.tonia.notatnik;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tonia.notatnik.Filtr;
import com.tonia.notatnik.R;
import com.tonia.notatnik.databinding.ActivitySzukajBinding;

public class SzukajActivity extends AppCompatActivity {
    private Filtr filtr;
    private ActivitySzukajBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_szukaj);

        filtr = new Filtr();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_szukaj);
        binding.setFiltr(filtr);
    }

    public void btZatwierdz_onClick(View view) {
        Intent powrot = new Intent();
        powrot.putExtra("filtr", binding.getFiltr());
        setResult(RESULT_OK, powrot);
        finish();
    }

    public void btAnuluj_onClick(View view) {
        Intent powrot = new Intent();
        setResult(RESULT_CANCELED, powrot);
        finish();
    }
}
