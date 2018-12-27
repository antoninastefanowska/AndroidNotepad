package com.tonia.notatnik;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class KategorieAdapter extends ArrayAdapter<Kategoria> {
    private Context context;
    private List<Kategoria> kategorie;

    public KategorieAdapter(Context context, int textViewResourceId, List<Kategoria> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.kategorie = values;
    }

    public void setData(List<Kategoria> kategorie) {
        this.kategorie = kategorie;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() { return kategorie.size(); }

    @Override
    public Kategoria getItem(int position) { return kategorie.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView)super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(kategorie.get(position).getNazwa());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView)super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(kategorie.get(position).getNazwa());
        return label;
    }
}
