package com.tonia.notatnik;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tonia.notatnik.databinding.NotatkaViewBinding;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

public class NotatkiAdapter extends RecyclerView.Adapter<NotatkiAdapter.NotatkiViewHolder> {
    private static final String BASE_IMAGE_URL = "https://raw.githubusercontent.com/toniaa/BazaNotatek/master/public/images/";
    private List<Notatka> notatki;
    private List<Notatka> zaznaczoneNotatki;
    private Context context;

    public static class NotatkiViewHolder extends RecyclerView.ViewHolder {
        public ImageView ikonka;
        public NotatkaViewBinding binding;

        public NotatkiViewHolder(View v) {
            super(v);
            ikonka = (ImageView)v.findViewById(R.id.ivNotatkaViewIcon);
            binding = NotatkaViewBinding.bind(v);
        }
    }

    public NotatkiAdapter(Context context, List<Notatka> notatki) {
        this.notatki = notatki;
        this.context = context;
        zaznaczoneNotatki = new LinkedList<Notatka>();
    }

    @Override
    public NotatkiAdapter.NotatkiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View notatkaView = inflater.inflate(R.layout.notatka_view, parent, false);
        NotatkiViewHolder vh = new NotatkiViewHolder(notatkaView);

        return vh;
    }

    @Override
    public void onBindViewHolder(NotatkiViewHolder holder, int position) {
        Notatka notatka = notatki.get(position);
        holder.binding.setNotatka(notatka);
        holder.binding.executePendingBindings();
        if (notatka.getIkonka() != null && !notatka.getIkonka().isEmpty()) {
            RequestOptions options = new RequestOptions().fitCenter().override(100, 100);
            Glide.with(context).load(BASE_IMAGE_URL + notatka.getIkonka()).apply(options).into(holder.ikonka);
        }
    }

    @Override
    public int getItemCount() {
        return notatki.size();
    }

    public void setData(List<Notatka> notatki) {
        this.notatki = notatki;
    }

    public Notatka getItem(int position) {
        return notatki.get(position);
    }

    public int getItemPosition(Notatka item) {
        return notatki.indexOf(item);
    }

    public void removeItem(Notatka item) {
        int position = notatki.indexOf(item);
        notatki.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(Notatka oldItem, Notatka newItem) {
        int position = notatki.indexOf(oldItem);
        oldItem.przepisz(newItem);
        notifyItemChanged(position);
    }

    public void addItem(Notatka item) {
        notatki.add(item);
        notifyItemInserted(notatki.size() - 1);
    }

    public void selectItem(Notatka notatka) {
        int position = notatki.indexOf(notatka);
        if (notatka.getZaznaczona()) {
            notatka.setZaznaczona(false);
            zaznaczoneNotatki.remove(notatka);
        }
        else {
            notatka.setZaznaczona(true);
            zaznaczoneNotatki.add(notatka);
        }
        notifyItemChanged(position);
    }

    public void wyroznij() {
        for (Notatka notatka : zaznaczoneNotatki) {
            if (!notatka.getWyroznienie())
                notatka.setWyroznienie(true);
            else
                notatka.setWyroznienie(false);
        }
        notifyItemRangeChanged(0, notatki.size());
    }

    public List<Notatka> getZaznaczoneNotatki() {
        return zaznaczoneNotatki;
    }

    public List<Notatka> getData() { return notatki; }
}