package com.example.savingservicev2.add_elements;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.Cursor;
import android.widget.Toast;

import com.example.savingservicev2.DatabaseHelper;
import com.example.savingservicev2.R;
import com.example.savingservicev2.view_elements.SupermarketActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class SupermarketAddActivity extends AppCompatActivity {

    EditText title;
    EditText city;
    Button addButton;
    Button deleteButton;
    Button saveSupInfo;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor supermarketCursor;
    long supermarketId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket_add);

        title = (EditText)findViewById(R.id.title);
        city = (EditText)findViewById(R.id.city);
        addButton = (Button)findViewById(R.id.addButton);
        deleteButton = (Button)findViewById(R.id.deleteButton);
        saveSupInfo = (Button)findViewById(R.id.saveSupInfo);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            supermarketId = extras.getLong("id");
        }

        if (supermarketId > 0) {
            supermarketCursor = db.rawQuery("SELECT * FROM supermarket WHERE _id" + "=?",
                    new String[]{String.valueOf(supermarketId)});
            supermarketCursor.moveToFirst();
            title.setText(supermarketCursor.getString(1));
            city.setText(supermarketCursor.getString(2));
            supermarketCursor.close();
        } else {
            deleteButton.setVisibility(View.GONE);
            saveSupInfo.setVisibility(View.GONE);
        }
    }

    public void save(View view){
        if (title.getText().toString().equals("") || city.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Все поля должны быть заполнены", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            ContentValues cv = new ContentValues();
            cv.put("title", title.getText().toString());
            cv.put("city", city.getText().toString());

            if (supermarketId > 0) {
                db.update("supermarket", cv, "_id" + "=" + String.valueOf(supermarketId), null);
            } else {
                db.insert("supermarket", null, cv);
            }
            goHome();
        }
    }

    public void delete(View view){
        db.delete("supermarket", "_id = ?", new String[]{String.valueOf(supermarketId)});
        goHome();
    }

    public void pdfSaveSupInfo(View view) {
        createFile(Uri.parse("/sdcard/Documents"));
    }

    // Request code for creating a PDF document.
    private static final int CREATE_FILE = 1;

    private void createFile(Uri pickerInitialUri) {
        Cursor supTitle = db.rawQuery("SELECT title FROM supermarket WHERE _id = " +
                supermarketId, null);
        supTitle.moveToFirst();
        String supTitleString = supTitle.getString(0);
        supTitle.close();

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/doc");
        intent.putExtra(Intent.EXTRA_TITLE, supTitleString + "ProductInfo.doc");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, CREATE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        //if (requestCode == your-request-code
                //&& resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                // Perform operations on the document using its URI.
            }
            alterDocument(uri);
        //}
    }

    private void alterDocument(Uri uri) {
        try {
            Cursor supTitle = db.rawQuery("SELECT title FROM supermarket WHERE _id = " +
                    supermarketId, null);
            supTitle.moveToFirst();
            String supTitleString = supTitle.getString(0);
            supTitle.close();

            Date loginTime = new Date();
            LocalDate loginDate = LocalDate.now();
            String[] time = loginTime.toString().split(" ");
            String loginDateString = loginDate.getYear() + "-" + loginDate.getMonthValue() +
                    "-" + loginDate.getDayOfMonth() + " " + time[3];

            String title = "Информация о товарах супермаркета " + supTitleString +
                    " по состоянию на " + loginDateString + "\n\n";

            String productList = "";
            Cursor supInfo = db.rawQuery("SELECT title, price FROM product INNER JOIN sup_prod " +
                    "ON product._id = sup_prod.prod_id WHERE sup_prod.sup_id = " + supermarketId, null);
            supInfo.moveToFirst();
            while (supInfo.isAfterLast() == false) {
                productList += supInfo.getString(0) + "\n Цена: " + supInfo.getDouble(1) + " грн\n\n";
                supInfo.moveToNext();
            }
            supInfo.close();

            ParcelFileDescriptor pfd = this.getContentResolver().
                    openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());
            fileOutputStream.write((title + productList).getBytes());
            // Let the document provider know you're done by closing the stream.
            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goHome(){
        db.close();
        Intent intent = new Intent(this, SupermarketActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
