package com.example.savingservicev2.view_elements;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.savingservicev2.UI_elements.CartActivity;
import com.example.savingservicev2.DatabaseHelper;
import com.example.savingservicev2.UI_elements.LoginActivity;
import com.example.savingservicev2.MainActivity;
import com.example.savingservicev2.R;
import com.example.savingservicev2.StatisticsActivity;
import com.example.savingservicev2.UserSession;
import com.example.savingservicev2.add_elements.SupermarketAddActivity;

public class SupermarketActivity extends AppCompatActivity {

    ListView supermarketList;
    TextView elemCount;

    Menu mainMenu;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor supermarketCursor;
    SimpleCursorAdapter supermarketAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket);

        supermarketList = (ListView)findViewById(R.id.supermarketList);
        elemCount = (TextView)findViewById(R.id.elemCount);

        supermarketList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SupermarketAddActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.open();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mainMenu = menu;

        if(UserSession.getRole() == null) {
            mainMenu.getItem(4).setVisible(false);
        }

        if (UserSession.getRole() == null || !UserSession.getRole().equals("admin")) {
            mainMenu.getItem(0).setVisible(false);
            mainMenu.getItem(1).setVisible(false);
            mainMenu.getItem(2).setVisible(false);
            mainMenu.getItem(3).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch(id){
            case R.id.action_product :
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            case R.id.action_user :
                intent = new Intent(this, UserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            case R.id.action_statistics :
                intent = new Intent(this, StatisticsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            case R.id.action_cart:
                intent = new Intent(this, CartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            case R.id.login :
                intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mainMenu != null) {
            if (UserSession.getRole() == null) {
                mainMenu.getItem(4).setVisible(false);
            } else {
                mainMenu.getItem(4).setVisible(true);
            }
        }

        if (UserSession.getRole() != null && UserSession.getRole().equals("admin")) {
            if (mainMenu != null) {
                mainMenu.getItem(0).setVisible(true);
                mainMenu.getItem(1).setVisible(true);
                mainMenu.getItem(2).setVisible(true);
                mainMenu.getItem(3).setVisible(true);
            }
        } else {
            if (mainMenu != null) {
                mainMenu.getItem(0).setVisible(false);
                mainMenu.getItem(1).setVisible(false);
                mainMenu.getItem(2).setVisible(false);
                mainMenu.getItem(3).setVisible(false);
            }
        }

        supermarketCursor = db.rawQuery("SELECT * FROM supermarket", null);
        String[] from = new String[] {"title", "city"};
        int[] to = new int[] {R.id.text1, R.id.text2};
        supermarketAdapter = new SimpleCursorAdapter(this, R.layout.supermarket_adapter,
                null, from, to, 0);
        supermarketAdapter.swapCursor(supermarketCursor);
        elemCount.setText("Кол-во элементов в таблице: " + String.valueOf(supermarketCursor.getCount()));
        supermarketList.setAdapter(supermarketAdapter);
    }

    public void add(View view){
        Intent intent = new Intent(this, SupermarketAddActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        supermarketCursor.close();
        db.close();
    }
}
