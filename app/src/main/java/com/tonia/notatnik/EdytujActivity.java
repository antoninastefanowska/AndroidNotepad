package com.tonia.notatnik;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.tonia.notatnik.databinding.ActivityEdytujBinding;

import java.util.ArrayList;
import java.util.List;

public class EdytujActivity extends AppCompatActivity {
    private final static int ZABLOKUJ = 0, ODBLOKUJ = 1, USUN_BLOKADE = 2;
    private Notatka notatka;
    private Button przyciskZapisz, przyciskOdrzuc;
    private EditText poleTytul, poleAutor, poleTekst;
    private Spinner poleKategoria;
    private ActivityEdytujBinding binding;
    private KategorieViewModel kategorieViewModel;
    private List<Kategoria> kategorie;
    private KategorieAdapter kategorieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edytuj);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edytuj);

        notatka = (Notatka)getIntent().getSerializableExtra("notatka");
        if (notatka.getZablokowana()) {
            Intent blokada = new Intent(EdytujActivity.this, BlokadaActivity.class);
            startActivityForResult(blokada, ODBLOKUJ);
        }
        else {
            binding.setNotatka(notatka);
        }

        poleKategoria = (Spinner)findViewById(R.id.spKategoria);
        kategorie = new ArrayList<Kategoria>();
        kategorieAdapter = new KategorieAdapter(this, android.R.layout.simple_spinner_item, kategorie);
        poleKategoria.setAdapter(kategorieAdapter);

        kategorieViewModel = ViewModelProviders.of(this).get(KategorieViewModel.class);
        kategorieViewModel.getKategorie().observe(this, new Observer<List<Kategoria>>() {
            @Override
            public void onChanged(@Nullable final List<Kategoria> zaladowaneKategorie) {
                kategorie = zaladowaneKategorie;
                kategorieAdapter.setData(kategorie);
                poleKategoria.setSelection((int)notatka.getKategoriaId());
            }
        });

        przyciskZapisz = (Button)findViewById(R.id.btZapisz);
        przyciskOdrzuc = (Button)findViewById(R.id.btAnuluj);
        poleTytul = (EditText)findViewById(R.id.etTytul);
        poleAutor = (EditText)findViewById(R.id.etAutor);
        poleTekst = (EditText)findViewById(R.id.etTekst);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                zmiana();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        poleTytul.addTextChangedListener(textWatcher);
        poleAutor.addTextChangedListener(textWatcher);
        poleTekst.addTextChangedListener(textWatcher);

        poleKategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Kategoria kategoria = (Kategoria)poleKategoria.getSelectedItem();
                notatka.setKategoriaId(kategoria.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void btZapisz_onClick(View view) {
        Intent powrot = new Intent();
        notatka.setTekst(Html.toHtml(poleTekst.getText(), 0));
        powrot.putExtra("notatka", notatka);
        setResult(RESULT_OK, powrot);
        finish();
    }

    public void btOdrzuc_onClick(View view) {
        Intent powrot = new Intent();
        setResult(RESULT_CANCELED, powrot);
        finish();
    }

    private void zmiana() {
        przyciskZapisz.setEnabled(true);
        przyciskOdrzuc.setEnabled(true);
    }

    public void btZablokuj_onClick(View view) {
        Intent blokada = new Intent(EdytujActivity.this, BlokadaActivity.class);
        startActivityForResult(blokada, ZABLOKUJ);
    }

    public void btOdblokuj_onClick(View view) {
        Intent blokada = new Intent(EdytujActivity.this, BlokadaActivity.class);
        startActivityForResult(blokada, USUN_BLOKADE);
    }

    public void btPogrubienie_onClick(View view) {
        SpannableString tekst = new SpannableString(poleTekst.getText());
        int nastepny, pogrubione = 0;
        for (int i = poleTekst.getSelectionStart(); i < poleTekst.getSelectionEnd(); i = nastepny) {
            nastepny = tekst.nextSpanTransition(i, poleTekst.getSelectionEnd(), StyleSpan.class);
            StyleSpan[] styleSpans = tekst.getSpans(i, nastepny, StyleSpan.class);
            for (StyleSpan styleSpan : styleSpans) {
                if (styleSpan.getStyle() == Typeface.BOLD) {
                    pogrubione++;
                    tekst.removeSpan(styleSpan);
                }
            }
        }

        if (pogrubione == 0) {
            tekst.setSpan(new StyleSpan(Typeface.BOLD), poleTekst.getSelectionStart(), poleTekst.getSelectionEnd(), 0);
        }
        poleTekst.setText(tekst);
    }

    public void btKursywa_onClick(View view) {
        SpannableString tekst = new SpannableString(poleTekst.getText());
        int nastepny, pochylone = 0;
        for (int i = poleTekst.getSelectionStart(); i < poleTekst.getSelectionEnd(); i = nastepny) {
            nastepny = tekst.nextSpanTransition(i, poleTekst.getSelectionEnd(), StyleSpan.class);
            StyleSpan[] styleSpans = tekst.getSpans(i, nastepny, StyleSpan.class);
            for (StyleSpan styleSpan : styleSpans) {
                if (styleSpan.getStyle() == Typeface.ITALIC) {
                    pochylone++;
                    tekst.removeSpan(styleSpan);
                }
            }
        }
        if (pochylone == 0) {
            tekst.setSpan(new StyleSpan(Typeface.ITALIC), poleTekst.getSelectionStart(), poleTekst.getSelectionEnd(), 0);
        }
        poleTekst.setText(tekst);
    }

    public void btPodkreslenie_onClick(View view) {
        SpannableString tekst = new SpannableString(poleTekst.getText());
        int nastepny, podkreslone = 0;
        for (int i = poleTekst.getSelectionStart(); i < poleTekst.getSelectionEnd(); i = nastepny) {
            nastepny = tekst.nextSpanTransition(i, poleTekst.getSelectionEnd(), StyleSpan.class);
            UnderlineSpan[] underlineSpans = tekst.getSpans(i, nastepny, UnderlineSpan.class);
            for (UnderlineSpan underlineSpan : underlineSpans) {
                podkreslone++;
                tekst.removeSpan(underlineSpan);
            }
        }
        if (podkreslone == 0) {
            tekst.setSpan(new UnderlineSpan(), poleTekst.getSelectionStart(), poleTekst.getSelectionEnd(), 0);
        }
        poleTekst.setText(tekst);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ZABLOKUJ:
                    notatka.setZablokowana(true);
                    binding.setNotatka(notatka);
                    binding.executePendingBindings();
                    break;
                case ODBLOKUJ:
                    binding.setNotatka(notatka);
                    binding.executePendingBindings();
                    break;
                case USUN_BLOKADE:
                    notatka.setZablokowana(false);
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
