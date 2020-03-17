package com.example.savingservicev2.UI_elements;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.example.savingservicev2.R;

public class FilterActivity extends AppCompatActivity {

    CheckBox vine;
    CheckBox heightAlc;
    CheckBox liker;
    CheckBox beer;
    CheckBox lowAlc;
    CheckBox champagne;
    CheckBox energyDrinks;
    RadioButton none;
    RadioButton asc;
    RadioButton desc;

    int sortStyle = 0;
    int[] categories = new int[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        vine = (CheckBox) findViewById(R.id.vine);
        heightAlc = (CheckBox) findViewById(R.id.heightAlc);
        liker = (CheckBox) findViewById(R.id.liker);
        beer = (CheckBox) findViewById(R.id.beer);
        lowAlc = (CheckBox) findViewById(R.id.lowAlc);
        champagne = (CheckBox) findViewById(R.id.champagne);
        energyDrinks = (CheckBox) findViewById(R.id.energyDrinks);
        none = (RadioButton) findViewById(R.id.none);
        asc = (RadioButton) findViewById(R.id.asc);
        desc = (RadioButton) findViewById(R.id.desc);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            categories = extras.getIntArray("selected");
            sortStyle = extras.getInt("sortStyle");
        }

        if (sortStyle == 0) {
            none.setChecked(true);
        } else if(sortStyle == 1) {
            asc.setChecked(true);
        } else if (sortStyle == 2) {
            desc.setChecked(true);
        }

        if (categories[0] == 1) {
            vine.setChecked(true);
        }
        if (categories[1] == 1) {
            heightAlc.setChecked(true);
        }
        if (categories[2] == 1) {
            liker.setChecked(true);
        }
        if (categories[3] == 1) {
            beer.setChecked(true);
        }
        if (categories[4] == 1) {
            lowAlc.setChecked(true);
        }
        if (categories[5] == 1) {
            champagne.setChecked(true);
        }
        if (categories[6] == 1) {
            energyDrinks.setChecked(true);
        }
    }

    public void acceptFilter(View view) {
        Intent intent = new Intent();

        if (none.isChecked()) {
            sortStyle = 0;
        } else if(asc.isChecked()) {
            sortStyle = 1;
        } else if (desc.isChecked()) {
            sortStyle = 2;
        }

        for (int i = 0; i < 7; i++) {
            categories[i] = 0;
        }
        if (vine.isChecked()) categories[0] = 1;
        if (heightAlc.isChecked()) categories[1] = 1;
        if (liker.isChecked()) categories[2] = 1;
        if (beer.isChecked()) categories[3] = 1;
        if (lowAlc.isChecked()) categories[4] = 1;
        if (champagne.isChecked()) categories[5] = 1;
        if (energyDrinks.isChecked()) categories[6] = 1;

        intent.putExtra("selected", categories);
        intent.putExtra("accept", true);
        intent.putExtra("sortStyle", sortStyle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
