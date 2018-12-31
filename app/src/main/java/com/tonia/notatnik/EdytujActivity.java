package com.tonia.notatnik;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpanWatcher;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.tonia.notatnik.databinding.ActivityEdytujBinding;

import java.util.ArrayList;
import java.util.List;

public class EdytujActivity extends AppCompatActivity {
    private final static int ZABLOKUJ = 0, ODBLOKUJ = 1, USUN_BLOKADE = 2;
    private Notatka notatka;
    private EditTextSelectable poleTekst;
    private Spinner poleKategoria;
    private ActivityEdytujBinding binding;
    private KategorieViewModel kategorieViewModel;
    private List<Kategoria> kategorie;
    private KategorieAdapter kategorieAdapter;
    private AlertDialog ostrzezenie;
    private SpanWatcher spanWatcher;
    private Button przyciskPogrubienie, przyciskKursywa, przyciskPodkreslenie;

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

        poleTekst = (EditTextSelectable)findViewById(R.id.etTekst);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notatka.setTekst(Html.toHtml(poleTekst.getText()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
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

        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(EdytujActivity.this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.odrzuc_btn));
        alertDialogBuilder.setMessage(getResources().getString(R.string.czy_odrzucic_alert));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.tak), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent powrot = new Intent();
                setResult(RESULT_CANCELED, powrot);
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.nie), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        ostrzezenie = alertDialogBuilder.create();

        przyciskPogrubienie = (Button)findViewById(R.id.btPogrubienie);
        przyciskKursywa = (Button)findViewById(R.id.btKursywa);
        przyciskPodkreslenie = (Button)findViewById(R.id.btPodkreslenie);
        poleTekst.setButtons(przyciskPogrubienie, przyciskKursywa, przyciskPodkreslenie);
    }

    @Override
    public void onBackPressed() {
        ostrzezenie.show();
    }

    public void btZapisz_onClick(View view) {
        Intent powrot = new Intent();
        powrot.putExtra("notatka", notatka);
        setResult(RESULT_OK, powrot);
        finish();
    }

    public void btOdrzuc_onClick(View view) {
        ostrzezenie.show();
    }

    public void btZablokuj_onClick(View view) {
        if (Build.VERSION.SDK_INT < 23) {
            Toast.makeText(this, R.string.stara_wersja_msg, Toast.LENGTH_SHORT).show();
        }
        else {
            Intent blokada = new Intent(EdytujActivity.this, BlokadaActivity.class);
            startActivityForResult(blokada, ZABLOKUJ);
        }
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
