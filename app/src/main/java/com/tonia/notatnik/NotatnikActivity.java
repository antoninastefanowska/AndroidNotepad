package com.tonia.notatnik;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.databinding.DataBindingUtil;

import com.tonia.notatnik.databinding.ActivityNotatnikBinding;

import java.util.LinkedList;
import java.util.List;

public class NotatnikActivity extends AppCompatActivity {
    public final static int DODAJ = 1, EDYTUJ = 2, SZUKAJ = 3;

    private List<Notatka> notatki;
    private List<Notatka> przefiltrowaneNotatki;
    private boolean czyPrzefiltrowane;

    public NotatkiAdapter notatkiAdapter;
    private RecyclerView notatkiRecyclerView;
    private ActivityNotatnikBinding binding;

    private NotatkiViewModel notatkiViewModel;
    private Observer<List<Notatka>> obserwator1, obserwator2;
    private Context context;

    private NotatkiController notatkiController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notatnik);

        notatki = new LinkedList<Notatka>();
        przefiltrowaneNotatki = new LinkedList<Notatka>();
        czyPrzefiltrowane = false;

        notatkiViewModel = ViewModelProviders.of(this).get(NotatkiViewModel.class);
        obserwator1 = new Observer<List<Notatka>>() {
            @Override
            public void onChanged(@Nullable final List<Notatka> zaladowaneNotatki) {
                notatki = zaladowaneNotatki;
                notatkiAdapter.setData(notatki);
                notatkiViewModel.getNotatki().removeObserver(obserwator1);
            }
        };
        notatkiViewModel.getNotatki().observe(this, obserwator1);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notatnik);

        notatkiRecyclerView = (RecyclerView) findViewById(R.id.rvNotatki);
        notatkiRecyclerView.setHasFixedSize(true);
        notatkiRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notatkiAdapter = new NotatkiAdapter(getApplication(), notatki, notatkiViewModel);

        binding.setZaznaczoneNotatki(notatkiAdapter.getZaznaczoneNotatki().size());
        binding.setNotatkiAdapter(notatkiAdapter);
        binding.setCzyPrzefiltrowane(czyPrzefiltrowane);

        context = this;
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
        final List<Notatka> zaznaczoneNotatki = notatkiAdapter.getZaznaczoneNotatki();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.usun_btn));
        alertDialogBuilder.setMessage(getResources().getQuantityString(R.plurals.czy_usunac_alert, zaznaczoneNotatki.size(), zaznaczoneNotatki.size()));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.tak), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (Notatka zaznaczonaNotatka : zaznaczoneNotatki)
                    notatkiAdapter.removeItem(zaznaczonaNotatka);

                int usuniete = notatkiAdapter.getZaznaczoneNotatki().size();
                String message = getResources().getQuantityString(R.plurals.usunieto_msg, usuniete, usuniete);
                Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                toast.show();

                notatkiAdapter.getZaznaczoneNotatki().clear();
                binding.setZaznaczoneNotatki(notatkiAdapter.getZaznaczoneNotatki().size());
                binding.executePendingBindings();
            }
        });
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.nie), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.create().show();
    }

    public void btSzczegoly_onClick(View view) {
        Notatka notatka = notatkiAdapter.getZaznaczoneNotatki().get(0);
        Intent szczegoly = new Intent(NotatnikActivity.this, SzczegolyActivity.class);
        szczegoly.putExtra("notatka", notatka);
        startActivity(szczegoly);
    }

    public void btSzukaj_onClick(View view) {
        Button btSzukaj = (Button) view;
        if (!czyPrzefiltrowane) {
            btSzukaj.setPressed(true);
            Intent szukaj = new Intent(NotatnikActivity.this, SzukajActivity.class);
            startActivityForResult(szukaj, SZUKAJ);
        } else {
            notatkiViewModel.getNotatki().observe(this, obserwator1);
            btSzukaj.setPressed(false);
            przefiltrowaneNotatki.clear();
            czyPrzefiltrowane = false;
            binding.setCzyPrzefiltrowane(czyPrzefiltrowane);
            binding.executePendingBindings();
        }
    }

    public void btWyroznij_onClick(View view) {
        notatkiAdapter.wyroznij();
    }

    public void notatkaView_onClick(View view) {
        LinearLayout notatkaContainer = (LinearLayout) view;
        NotatkiAdapter.NotatkiViewHolder vh = (NotatkiAdapter.NotatkiViewHolder) notatkiRecyclerView.getChildViewHolder(notatkaContainer);
        Notatka notatka = vh.binding.getNotatka();

        notatkiAdapter.selectItem(notatka);
        binding.setZaznaczoneNotatki(notatkiAdapter.getZaznaczoneNotatki().size());
        binding.executePendingBindings();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DODAJ: {
                    Notatka notatka = (Notatka) data.getSerializableExtra("notatka");
                    ConstraintLayout mainContainer = (ConstraintLayout) findViewById(R.id.clMain);
                    String message = getResources().getString(R.string.dodano_msg, notatka.getTytul());
                    Snackbar snackbar = Snackbar.make(mainContainer, message, Snackbar.LENGTH_SHORT);
                    notatkiAdapter.addItem(notatka);
                    snackbar.show();
                }
                break;

                case EDYTUJ: {
                    Notatka staraNotatka = notatkiAdapter.getZaznaczoneNotatki().get(0), notatka = (Notatka) data.getSerializableExtra("notatka");
                    ConstraintLayout mainContainer = (ConstraintLayout) findViewById(R.id.clMain);
                    String message = getResources().getString(R.string.edytowano_msg, staraNotatka.getTytul());
                    Snackbar snackbar = Snackbar.make(mainContainer, message, Snackbar.LENGTH_SHORT);
                    notatkiAdapter.editItem(staraNotatka, notatka);
                    snackbar.show();
                }
                break;

                case SZUKAJ: {
                    Filtr filtr = (Filtr) data.getSerializableExtra("filtr");
                    final String query = filtr.buildQuery();
                    Button btSzukaj = (Button) findViewById(R.id.btSzukaj);
                    btSzukaj.setPressed(true);

                    obserwator2 = new Observer<List<Notatka>>() {
                        @Override
                        public void onChanged(@Nullable final List<Notatka> zaladowaneNotatki) {
                            przefiltrowaneNotatki = zaladowaneNotatki;
                            notatkiAdapter.setData(przefiltrowaneNotatki);
                            czyPrzefiltrowane = true;
                            binding.setCzyPrzefiltrowane(czyPrzefiltrowane);
                            binding.executePendingBindings();
                            String message = getResources().getQuantityString(R.plurals.znaleziono_msg, przefiltrowaneNotatki.size(), przefiltrowaneNotatki.size());
                            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                            toast.show();
                            notatkiViewModel.search(query).removeObserver(obserwator2);
                        }
                    };

                    notatkiViewModel.search(query).observe(this, obserwator2);
                }
                break;
            }
        }
    }
}
