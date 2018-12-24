package com.tonia.notatnik;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.tonia.notatnik.databinding.ActivityEdytujBinding;

public class EdytujActivity extends AppCompatActivity {
    private Notatka notatka;
    private EditText poleTytul, poleAutor, poleTekst;
    private Spinner poleKategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edytuj);

        notatka = (Notatka)getIntent().getSerializableExtra("notatka");
        ActivityEdytujBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edytuj);
        binding.setNotatka(notatka);

        poleTytul = (EditText)findViewById(R.id.etTytul);
        poleAutor = (EditText)findViewById(R.id.etAutor);
        poleTekst = (EditText)findViewById(R.id.etTekst);
        poleKategoria = (Spinner)findViewById(R.id.spKategoria);

        poleTytul.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
    }

    public void btZapisz_onClick(View view) {
        Intent powrot = new Intent();
        powrot.putExtra("notatka", notatka);
        setResult(RESULT_OK, powrot);
        finish();
    }

    public void btOdrzuc_onClick(View view) {
        Intent powrot = new Intent();
        setResult(RESULT_CANCELED, powrot);
        finish();
    }
}
