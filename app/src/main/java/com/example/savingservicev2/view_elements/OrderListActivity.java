package com.example.savingservicev2.view_elements;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.savingservicev2.DatabaseHelper;
import com.example.savingservicev2.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class OrderListActivity extends AppCompatActivity {

    ListView orderListList;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor orderListCursor;
    SimpleCursorAdapter orderListAdapter;
    long orderId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        orderListList = (ListView) findViewById(R.id.orderListList);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            orderId = extras.getLong("id");
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        orderListCursor = db.rawQuery("SELECT * " +
                "FROM product INNER JOIN sup_prod " +
                "ON product._id = sup_prod.prod_id " +
                "INNER JOIN order_list " +
                "ON sup_prod._id = order_list.sup_prod_id " +
                "WHERE order_list.order_id = " + orderId, null);
        String[] from = new String[] {"title", "count"};
        int[] to = new int[] {R.id.text1, R.id.text2};
        orderListAdapter = new SimpleCursorAdapter(this, R.layout.order_list_adapter,
                null, from, to, 0);
        orderListAdapter.swapCursor(orderListCursor);
        orderListList.setAdapter(orderListAdapter);
    }

    public void saveOrderInDoc(View view) {
        createFile(Uri.parse("/sdcard/Documents"));
    }

    // Request code for creating a PDF document.
    private static final int CREATE_FILE = 1;

    private void createFile(Uri pickerInitialUri) {
        Cursor orderNumber = db.rawQuery("SELECT _id FROM 'order' WHERE _id = " +
                orderId, null);
        orderNumber.moveToFirst();
        String orderNumberString = orderNumber.getInt(0) + "";
        orderNumber.close();

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/doc");
        intent.putExtra(Intent.EXTRA_TITLE, "Order_" + orderNumberString + ".doc");
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, CREATE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        Uri uri = null;
        if (resultData != null) {
            uri = resultData.getData();
        }
        alterDocument(uri);
    }

    private void alterDocument(Uri uri) {
        try {
            String title = "Заказ номер " + orderId + "\n\n";

            Cursor supTitle = db.rawQuery("SELECT title FROM 'order' " +
                    "INNER JOIN supermarket " +
                    "ON 'order'.sup_id = supermarket._id " +
                    "WHERE 'order'._id = " + orderId, null);
            supTitle.moveToFirst();
            String supTitleString = supTitle.getString(0);
            supTitle.close();

            Cursor orderDate = db.rawQuery("SELECT date_time FROM 'order' " +
                    "WHERE 'order'._id = " + orderId, null);
            orderDate.moveToFirst();
            String orderDateString = orderDate.getString(0);
            orderDate.close();

            String description = "Куплено в: " + supTitleString + "\n" +
                    "Дата и время: " + orderDateString + "\n\n\n";

            String orderListSting = "";
            Cursor orderList = db.rawQuery("SELECT title, order_list.'count' " +
                    "FROM product INNER JOIN sup_prod " +
                    "ON product._id = sup_prod.prod_id " +
                    "INNER JOIN order_list " +
                    "ON sup_prod._id = order_list.sup_prod_id " +
                    "WHERE order_list.order_id = " + orderId, null);
            orderList.moveToFirst();
            while (orderList.isAfterLast() == false) {
                orderListSting += orderList.getString(0) + "\n" +
                        "Кол-во: " + orderList.getInt(1) + "\n\n";
                orderList.moveToNext();
            }
            orderList.close();

            ParcelFileDescriptor pfd = this.getContentResolver().
                    openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());
            fileOutputStream.write((title + description + orderListSting).getBytes());
            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
