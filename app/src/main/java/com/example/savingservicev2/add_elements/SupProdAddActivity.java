package com.example.savingservicev2.add_elements;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.savingservicev2.DatabaseHelper;
import com.example.savingservicev2.R;

public class SupProdAddActivity extends AppCompatActivity {

    EditText supTitle;
    EditText price;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor supTitleCursor;
    long productId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_prod_add);

        supTitle = (EditText)findViewById(R.id.supTitle);
        price = (EditText)findViewById(R.id.price);
        price.setText("0.0");

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productId = extras.getLong("id");
        }
    }

    public void save(View view){
        String supTitleString = "'" + supTitle.getText().toString() + "'";
        supTitleCursor = db.rawQuery("SELECT supermarket._id FROM supermarket WHERE title = "
                + supTitleString, null);
        supTitleCursor.moveToFirst();
        int supID = supTitleCursor.getInt(0);

        ContentValues cv = new ContentValues();
        cv.put("sup_id", supID);
        cv.put("prod_id", productId);
        cv.put("price", Double.parseDouble(price.getText().toString()));

        db.insert("sup_prod", null, cv);

        goHome();
    }

    private void goHome(){
        supTitleCursor.close();
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
