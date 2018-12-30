package com.tonia.notatnik;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.widget.Button;

public class EditTextSelectable extends AppCompatEditText {
    private Button przyciskPogrubienie, przyciskKursywa, przyciskPodkreslenie;

    public EditTextSelectable(Context context) {
        super(context);
    }

    public EditTextSelectable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextSelectable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setButtons(Button przyciskPogrubienie, Button przyciskKursywa, Button przyciskPodkreslenie) {
        this.przyciskPogrubienie = przyciskPogrubienie;
        this.przyciskKursywa = przyciskKursywa;
        this.przyciskPodkreslenie = przyciskPodkreslenie;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if (przyciskPogrubienie != null && przyciskKursywa != null && przyciskPodkreslenie != null) {
            if (selStart != selEnd) {
                przyciskPogrubienie.setEnabled(true);
                przyciskKursywa.setEnabled(true);
                przyciskPodkreslenie.setEnabled(true);
            } else {
                przyciskPogrubienie.setEnabled(false);
                przyciskKursywa.setEnabled(false);
                przyciskPodkreslenie.setEnabled(false);
            }
        }
    }
}
