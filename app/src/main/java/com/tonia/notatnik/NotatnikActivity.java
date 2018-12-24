package com.tonia.notatnik;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.tonia.notatnik.databinding.ActivityNotatnikBinding;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class NotatnikActivity extends AppCompatActivity {
    public final static int DODAJ = 1, EDYTUJ = 2, SZUKAJ = 3;

    private List<Notatka> notatki;
    private List<Notatka> przefiltrowaneNotatki;
    private boolean czyPrzefiltrowane;

    public NotatkiAdapter notatkiAdapter;
    private RecyclerView notatkiRecyclerView;
    private ActivityNotatnikBinding binding;

    private NotatkiViewModel notatkiViewModel;
    private boolean czyZaladowane;

    private NotatkiController notatkiController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notatnik);

        notatki = new LinkedList<Notatka>();
        przefiltrowaneNotatki = new LinkedList<Notatka>();
        czyPrzefiltrowane = false;
        czyZaladowane = false;

        notatkiViewModel = ViewModelProviders.of(this).get(NotatkiViewModel.class);
        notatkiViewModel.getAllData().observe(this, new Observer<List<Notatka>>() {
            @Override
            public void onChanged(@Nullable final List<Notatka> zaladowaneNotatki) {
                /*
                notatki = zaladowaneNotatki;
                notatkiAdapter.setData(notatki);
                if (!czyZaladowane) {
                    notatkiAdapter.notifyDataSetChanged();
                    czyZaladowane = true;
                } */
            }
        });

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notatnik);

        notatkiRecyclerView = (RecyclerView)findViewById(R.id.rvNotatki);
        notatkiRecyclerView.setHasFixedSize(true);
        notatkiRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notatkiAdapter = new NotatkiAdapter(getApplication(), notatki);

        binding.setZaznaczoneNotatki(notatkiAdapter.getZaznaczoneNotatki().size());
        binding.setNotatkiAdapter(notatkiAdapter);

        notatkiController = new NotatkiController(notatkiAdapter);
        notatkiController.start();
    }

    public void btDodaj_onClick(View view) {
        Notatka notatka = new Notatka();
        Intent edytuj = new Intent(NotatnikActivity.this, EdytujActivity.class);
        edytuj.putExtra("notatka", notatka);
        startActivityForResult(edytuj, DODAJ);
    }

    public void btEdytuj_onClick(View view) {
        Notatka notatka = notatkiAdapter.getZaznaczoneNotatki().get(0);
        Intent edytuj = new Intent(NotatnikActivity.this, EdytujActivity.class);
        edytuj.putExtra("notatka", notatka);
        startActivityForResult(edytuj, EDYTUJ);
    }

    public void btUsun_onClick(View view) {
        for (Notatka zaznaczonaNotatka : notatkiAdapter.getZaznaczoneNotatki()) {
            notatkiAdapter.removeItem(zaznaczonaNotatka);
            //notatkiViewModel.delete(zaznaczonaNotatka);
        }

        int usuniete = notatkiAdapter.getZaznaczoneNotatki().size();
        String message = getResources().getQuantityString(R.plurals.usunieto_msg, usuniete, usuniete);
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();

        notatkiAdapter.getZaznaczoneNotatki().clear();
        binding.setZaznaczoneNotatki(notatkiAdapter.getZaznaczoneNotatki().size());
        binding.executePendingBindings();
    }

    public void btSzukaj_onClick(View view) {
        Button btSzukaj = (Button)view;
        if (!czyPrzefiltrowane) {
            btSzukaj.setPressed(true);
            Intent szukaj = new Intent(NotatnikActivity.this, SzukajActivity.class);
            startActivityForResult(szukaj, SZUKAJ);
        }
        else {
            notatkiAdapter.setData(notatki);
            notatkiAdapter.notifyDataSetChanged();
            btSzukaj.setPressed(false);
            przefiltrowaneNotatki.clear();
            czyPrzefiltrowane = false;
        }
    }

    public void btWyroznij_onClick(View view) {
        notatkiAdapter.wyroznij();
    }

    public void btLink_onClick(View view) {
        Notatka notatka = notatkiAdapter.getZaznaczoneNotatki().get(0);
        String youtubeLink = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/";
        Intent link = null;

        if (Patterns.WEB_URL.matcher(notatka.getTekst()).matches()) {
            link = new Intent(Intent.ACTION_VIEW, Uri.parse(notatka.getTekst()));
            link.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Pattern.matches(youtubeLink, notatka.getTekst()))
                link.setPackage("com.google.android.youtube");
        }
        startActivity(link);
    }

    public void tvNotatkaViewTytul_onClick(View view) {
        LinearLayout notatkaContainer = (LinearLayout)view;
        int zaznaczonyIndeks = notatkiRecyclerView.getChildAdapterPosition(notatkaContainer);
        Notatka notatka = notatkiAdapter.getItem(zaznaczonyIndeks);

        notatkiAdapter.selectItem(notatka);
        binding.setZaznaczoneNotatki(notatkiAdapter.getZaznaczoneNotatki().size());
        binding.executePendingBindings();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DODAJ: {
                    Notatka notatka = (Notatka)data.getSerializableExtra("notatka");
                    ConstraintLayout mainContainer = (ConstraintLayout)findViewById(R.id.clMain);
                    String message = getResources().getString(R.string.dodano_msg, notatka.getTytul());
                    Snackbar snackbar = Snackbar.make(mainContainer, message, Snackbar.LENGTH_SHORT);
                    notatkiAdapter.addItem(notatka);
                    //notatkiViewModel.insert(notatka);
                    snackbar.show();
                } break;

                case EDYTUJ: {
                    Notatka staraNotatka = notatkiAdapter.getZaznaczoneNotatki().get(0), notatka = (Notatka)data.getSerializableExtra("notatka");
                    ConstraintLayout mainContainer = (ConstraintLayout)findViewById(R.id.clMain);
                    String message = getResources().getString(R.string.edytowano_msg, staraNotatka.getTytul());
                    Snackbar snackbar = Snackbar.make(mainContainer, message, Snackbar.LENGTH_SHORT);
                    notatkiAdapter.editItem(staraNotatka, notatka);
                    //notatkiViewModel.update(staraNotatka);
                    snackbar.show();
                } break;

                case SZUKAJ: {
                    Filtr filtr = (Filtr)data.getSerializableExtra("filtr");
                    Button btSzukaj = (Button)findViewById(R.id.btSzukaj);
                    btSzukaj.setPressed(true);
                    for (Notatka notatka : notatki)
                        if (filtr.czyPasuje(notatka))
                            przefiltrowaneNotatki.add(notatka);
                    notatkiAdapter.setData(przefiltrowaneNotatki);
                    notatkiAdapter.notifyDataSetChanged();
                    czyPrzefiltrowane = true;
                    String message = getResources().getQuantityString(R.plurals.znaleziono_msg, przefiltrowaneNotatki.size(), przefiltrowaneNotatki.size());
                    Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
                    toast.show();
                } break;
            }
        }
    }
}
