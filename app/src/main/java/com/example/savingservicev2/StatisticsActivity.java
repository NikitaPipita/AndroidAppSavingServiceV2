package com.example.savingservicev2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.savingservicev2.UI_elements.CartActivity;
import com.example.savingservicev2.UI_elements.LoginActivity;
import com.example.savingservicev2.view_elements.SupermarketActivity;
import com.example.savingservicev2.view_elements.UserActivity;

public class StatisticsActivity extends AppCompatActivity {

    EditText fromDate;
    EditText toDate;
    TextView orderCount;
    TextView avgOrderSum;
    TextView loginUsers;
    TextView commentCount;

    Menu mainMenu;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        fromDate = (EditText)findViewById(R.id.fromDate);
        toDate = (EditText)findViewById(R.id.toDate);
        orderCount = (TextView) findViewById(R.id.orderCount);
        avgOrderSum = (TextView) findViewById(R.id.avgOrderSum);
        loginUsers = (TextView)findViewById(R.id.loginUsers);
        commentCount = (TextView)findViewById(R.id.commentCount);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.open();
    }

    @Override
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
            case R.id.action_supermarket :
                intent = new Intent(this, SupermarketActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            case R.id.action_cart:
                intent = new Intent(this, CartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            case R.id.login :
                intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
    }

    public void showStatistic(View view) {
        String fromDateStr = "'" + fromDate.getText().toString() + " 00:00:00'";
        String fromDateStrEndDay = "'" + fromDate.getText().toString() + " 24:00:00'";
        String toDateStr =  "'" + toDate.getText().toString() + " 24:00:00'";

        Cursor orderCountCursor;
        Cursor avgOrderSumCursor;
        Cursor loginUsersCursor;
        Cursor commentCountCursor;

        String orderCountResult = "выберите дату";
        String avgOrderSumResult = "выберите дату";
        String loginUsersResult = "выберите дату";
        String commentCountResult = "выберите дату";

        if (!fromDateStr.equals("' 00:00:00'") || !toDateStr.equals("' 24:00:00'")) {
            if (toDateStr.equals("' 24:00:00'")) {
                orderCountCursor = db.rawQuery("SELECT COUNT(_id) FROM 'order' WHERE date_time BETWEEN "
                        + fromDateStr + " AND " + fromDateStrEndDay, null);
                avgOrderSumCursor = db.rawQuery("SELECT AVG(sum) FROM 'order' WHERE date_time BETWEEN "
                        + fromDateStr + " AND " + fromDateStrEndDay, null);
                loginUsersCursor = db.rawQuery("SELECT COUNT(_id) FROM user WHERE login_date BETWEEN "
                        + fromDateStr + " AND " + fromDateStrEndDay, null);
                commentCountCursor = db.rawQuery("SELECT COUNT(_id) FROM comment WHERE date BETWEEN "
                        + fromDateStr + " AND " + fromDateStrEndDay, null);

                orderCountCursor.moveToFirst();
                avgOrderSumCursor.moveToFirst();
                loginUsersCursor.moveToFirst();
                commentCountCursor.moveToFirst();

                orderCountResult = orderCountCursor.getInt(0) + "";
                avgOrderSumResult = avgOrderSumCursor.getDouble(0) + "";
                loginUsersResult = loginUsersCursor.getInt(0) + "";
                commentCountResult = commentCountCursor.getInt(0) + "";

                orderCountCursor.close();
                avgOrderSumCursor.close();
                loginUsersCursor.close();
                commentCountCursor.close();

            } else if (!fromDateStr.equals("' 00:00:00'")) {
                orderCountCursor = db.rawQuery("SELECT COUNT(_id) FROM 'order' WHERE date_time BETWEEN "
                        + fromDateStr + " AND " + toDateStr, null);
                avgOrderSumCursor = db.rawQuery("SELECT AVG(sum) FROM 'order' WHERE date_time BETWEEN "
                        + fromDateStr + " AND " + toDateStr, null);
                loginUsersCursor = db.rawQuery("SELECT COUNT(_id) FROM user WHERE login_date BETWEEN "
                        + fromDateStr + " AND " + toDateStr, null);
                commentCountCursor = db.rawQuery("SELECT COUNT(_id) FROM comment WHERE date BETWEEN "
                        + fromDateStr + " AND " + toDateStr, null);

                orderCountCursor.moveToFirst();
                avgOrderSumCursor.moveToFirst();
                loginUsersCursor.moveToFirst();
                commentCountCursor.moveToFirst();

                orderCountResult = orderCountCursor.getInt(0) + "";
                avgOrderSumResult = avgOrderSumCursor.getDouble(0) + "";
                loginUsersResult = loginUsersCursor.getInt(0) + "";
                commentCountResult = commentCountCursor.getInt(0) + "";

                orderCountCursor.close();
                avgOrderSumCursor.close();
                loginUsersCursor.close();
                commentCountCursor.close();
            }
        }

        orderCount.setText("Кол-во заказов: " + orderCountResult);
        avgOrderSum.setText("Средняя сумма заказа: " + avgOrderSumResult);
        loginUsers.setText("Активные пользователи: " + loginUsersResult);
        commentCount.setText("Кол-во комментариев: " + commentCountResult);
    }
}
