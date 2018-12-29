package com.tonia.notatnik;
import com.tonia.notatnik.databinding.NotatkaViewBinding;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

public class NotatkiAdapter extends RecyclerView.Adapter<NotatkiAdapter.NotatkiViewHolder> {
    private List<Notatka> notatki;
    private List<Notatka> zaznaczoneNotatki;
    private NotatkiViewModel notatkiViewModel;

    public static class NotatkiViewHolder extends RecyclerView.ViewHolder {
        public NotatkaViewBinding binding;

        public NotatkiViewHolder(View v) {
            super(v);
            binding = NotatkaViewBinding.bind(v);
        }
    }

    public NotatkiAdapter(List<Notatka> notatki, NotatkiViewModel notatkiViewModel) {
        this.notatki = notatki;
        this.notatkiViewModel = notatkiViewModel;
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
    }

    @Override
    public int getItemCount() {
        return notatki.size();
    }

    public void setData(List<Notatka> notatki) {
        this.notatki = notatki;
        notifyDataSetChanged();
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
        notatkiViewModel.delete(item);
        notifyItemRemoved(position);
    }

    public void editItem(Notatka oldItem, Notatka newItem) {
        int position = notatki.indexOf(oldItem);
        oldItem.przepisz(newItem);
        notatkiViewModel.update(oldItem);
        notifyItemChanged(position);
    }

    public void addItem(Notatka item) {
        notatki.add(item);
        long id = notatkiViewModel.insert(item);
        item.setId(id);
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
            notatkiViewModel.update(notatka);
        }
        notifyItemRangeChanged(0, notatki.size());
    }

    public List<Notatka> getZaznaczoneNotatki() {
        return zaznaczoneNotatki;
    }

    public List<Notatka> getData() { return notatki; }
}