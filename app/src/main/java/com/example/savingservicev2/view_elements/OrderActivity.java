package com.example.savingservicev2.view_elements;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.savingservicev2.DatabaseHelper;
import com.example.savingservicev2.R;
import com.example.savingservicev2.add_elements.SupermarketAddActivity;

public class OrderActivity extends AppCompatActivity {

    ListView orderList;
    TextView orderCountInDB;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor orderCursor;
    SimpleCursorAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orderList = (ListView) findViewById(R.id.orderList);
        orderCountInDB = (TextView) findViewById(R.id.orderCountInDB);

        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.open();
    }

    @Override
    public void onResume(){
        super.onResume();

        //orderCursor = db.rawQuery("SELECT * FROM 'order'", null);

        orderCursor = db.rawQuery("SELECT 'order'._id, name, title, date_time, sum  FROM 'order' INNER JOIN user " +
                "ON 'order'.user_id = user._id " +
                "INNER JOIN supermarket " +
                "ON 'order'.sup_id = supermarket._id", null);
        String[] from = new String[] {"name", "title", "date_time", "sum"};
        int[] to = new int[] {R.id.text1, R.id.text2, R.id.text3, R.id.text4};
        orderAdapter = new SimpleCursorAdapter(this, R.layout.order_adapter,
                null, from, to, 0);
        orderAdapter.swapCursor(orderCursor);
        orderCountInDB.setText("Кол-во элементов в таблице: " + String.valueOf(orderCursor.getCount()));
        orderList.setAdapter(orderAdapter);
    }
}
