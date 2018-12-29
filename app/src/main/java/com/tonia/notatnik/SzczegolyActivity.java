package com.tonia.notatnik;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.tonia.notatnik.databinding.ActivitySzczegolyBinding;

public class SzczegolyActivity extends AppCompatActivity {
    private static final int ODBLOKUJ = 0;
    private ActivitySzczegolyBinding binding;
    private Notatka notatka;
    private KategorieViewModel kategorieViewModel;
    private TextView poleKategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_szczegoly);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_szczegoly);

        notatka = (Notatka)getIntent().getSerializableExtra("notatka");
        if (notatka.getZablokowana()) {
            Intent blokada = new Intent(SzczegolyActivity.this, BlokadaActivity.class);
            startActivityForResult(blokada, ODBLOKUJ);
        }
        else {
            binding.setNotatka(notatka);
        }

        poleKategoria = (TextView)findViewById(R.id.tvKategoria);

        kategorieViewModel = ViewModelProviders.of(this).get(KategorieViewModel.class);
        kategorieViewModel.getKategoria(notatka.getKategoriaId()).observe(this, new Observer<Kategoria>() {
            @Override
            public void onChanged(@Nullable final Kategoria kategoria) {
                poleKategoria.setText(kategoria.getNazwa());
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ODBLOKUJ:
                    binding.setNotatka(notatka);
                    binding.executePendingBindings();
                    break;
            }
        }
        else if (requestCode == ODBLOKUJ) {
            Intent powrot = new Intent();
            setResult(RESULT_CANCELED, powrot);
            finish();
        }
    }
}
