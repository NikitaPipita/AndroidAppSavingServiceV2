package com.example.savingservicev2.view_elements;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.savingservicev2.DatabaseHelper;
import com.example.savingservicev2.R;
import com.example.savingservicev2.add_elements.ProductActivity;

public class SupProdActivity extends AppCompatActivity {

    EditText price;
    Button addButton;
    Button deleteButton;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor supProdCursor;
    long supProdId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_prod);

        price = (EditText)findViewById(R.id.price);
        addButton = (Button)findViewById(R.id.addButton);
        deleteButton = (Button)findViewById(R.id.deleteButton);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            supProdId = extras.getLong("id");
        }

        supProdCursor = db.rawQuery("SELECT * FROM sup_prod WHERE _id" + "=?",
                new String[]{String.valueOf(supProdId)});
        supProdCursor.moveToFirst();
        double doublePrice = supProdCursor.getDouble(3);
        String stringPrice = doublePrice + "";
        price.setText(stringPrice);
        supProdCursor.close();
    }

    public void save(View view){
        ContentValues cv = new ContentValues();
        String stringPrice = price.getText().toString();
        double doublePrice = Double.parseDouble(stringPrice);
        cv.put("price", doublePrice);
        db.update("sup_prod", cv, "_id" + "=" + String.valueOf(supProdId), null);

        goHome();
    }

    public void delete(View view){
        db.delete("sup_prod", "_id = ?", new String[]{String.valueOf(supProdId)});
        goHome();
    }

    private void goHome(){
        db.close();
        Intent intent = new Intent(this, ProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
