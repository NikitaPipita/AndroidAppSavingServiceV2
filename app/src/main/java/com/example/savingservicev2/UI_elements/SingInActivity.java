package com.example.savingservicev2.UI_elements;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.savingservicev2.DatabaseHelper;
import com.example.savingservicev2.R;
import com.example.savingservicev2.UI_elements.LoginActivity;
import com.example.savingservicev2.UserSession;

import java.time.LocalDate;
import java.util.Date;

public class SingInActivity extends AppCompatActivity {

    EditText phoneField;
    EditText nameField;
    EditText passwordField;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);

        phoneField = (EditText)findViewById(R.id.phoneField);
        nameField = (EditText)findViewById(R.id.nameField);
        passwordField = (EditText)findViewById(R.id.passwordField);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.open();
    }

    public void singIn(View view) {
        int phone = 0;
        try {
            phone = Integer.parseInt(phoneField.getText().toString());
        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Заполните поле с номером телефона", Toast.LENGTH_SHORT);
            toast.show();
        }
        String name = nameField.getText().toString();
        String password = passwordField.getText().toString();
        if (phone == 0 || name.equals("") || password.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Все поля должны быть заполнены", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Cursor isPhoneInDb = db.rawQuery("SELECT * FROM user WHERE phone = " + phone, null);
            if (isPhoneInDb.getCount() == 0) {
                ContentValues cv = new ContentValues();
                cv.put("phone", phone);
                cv.put("password", password);
                cv.put("name", name);

                Date loginTime = new Date();
                LocalDate loginDate = LocalDate.now();
                String[] time = loginTime.toString().split(" ");
                String loginDateString = loginDate.getYear() + "-" + loginDate.getMonthValue() +
                        "-" + loginDate.getDayOfMonth() + " " + time[3];
                cv.put("login_date", loginDateString);
                db.insert("user", null, cv);

                Cursor getMaxUserId = db.rawQuery("SELECT MAX(_id) FROM user", null);
                getMaxUserId.moveToFirst();

                ContentValues simpleOrderCV = new ContentValues();
                simpleOrderCV.put("user_id", getMaxUserId.getInt(0));
                simpleOrderCV.put("sup_id", 1);
                simpleOrderCV.put("date_time", "2000-01-01");
                simpleOrderCV.put("sum", 0);
                db.insert("order", null, simpleOrderCV);
                Cursor maxOrderId = db.rawQuery("SELECT MAX(_id) FROM 'order'", null);
                maxOrderId.moveToFirst();
                int orderId = maxOrderId.getInt(0);

                UserSession.setRole("user", name, phone, getMaxUserId.getInt(0));

                maxOrderId.close();
                isPhoneInDb.close();
                getMaxUserId.close();
                goHome();
            } else {
                isPhoneInDb.close();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Такой номер телефона уже зарегистрирован", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void goHome(){
        db.close();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
