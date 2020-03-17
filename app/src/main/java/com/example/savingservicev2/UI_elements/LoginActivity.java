package com.example.savingservicev2.UI_elements;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savingservicev2.DatabaseHelper;
import com.example.savingservicev2.MainActivity;
import com.example.savingservicev2.R;
import com.example.savingservicev2.UserSession;

import java.time.LocalDate;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    TextView userName;
    Button loginButton;
    Button logoutButton;
    Button singInButton;
    EditText phoneField;
    EditText passwordField;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (TextView)findViewById(R.id.userNameText);
        loginButton = (Button)findViewById(R.id.loginButton);
        logoutButton = (Button)findViewById(R.id.logoutButton);
        singInButton = (Button)findViewById(R.id.singInButton);
        phoneField = (EditText) findViewById(R.id.phoneField);
        passwordField = (EditText) findViewById(R.id.passwordField);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.open();
    }

    @Override
    public void onResume(){
        super.onResume();

        if (UserSession.getRole() == null) {
            userName.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);

            loginButton.setVisibility(View.VISIBLE);
            singInButton.setVisibility(View.VISIBLE);
            phoneField.setVisibility(View.VISIBLE);
            passwordField.setVisibility(View.VISIBLE);
        } else {
            loginButton.setVisibility(View.GONE);
            singInButton.setVisibility(View.GONE);
            phoneField.setVisibility(View.GONE);
            passwordField.setVisibility(View.GONE);

            userName.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);

            userName.setText("Здравствуйте, " + UserSession.getName());
        }
    }

    public void login(View view) {
        int phoneNum = 0;
        try {
            phoneNum = Integer.parseInt(phoneField.getText().toString());
        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Логин должен состоять только из цифр", Toast.LENGTH_SHORT);
            toast.show();
        }
        String password = passwordField.getText().toString();
        Cursor loginDataCursor = db.rawQuery("SELECT _id, phone, name, role FROM user WHERE phone = " + phoneNum +
                " AND password = '" + password + "'", null);
        if (loginDataCursor.getCount() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Неверный логин или пароль", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            loginDataCursor.moveToFirst();

            UserSession.setRole(loginDataCursor.getString(3),
                    loginDataCursor.getString(2),
                    loginDataCursor.getInt(1),
                    loginDataCursor.getInt(0));

            Date loginTime = new Date();
            LocalDate loginDate = LocalDate.now();
            String[] time = loginTime.toString().split(" ");
            String loginDateString = loginDate.getYear() + "-" + loginDate.getMonthValue() +
                   "-" + loginDate.getDayOfMonth() + " " + time[3];

            ContentValues cv = new ContentValues();
            cv.put("login_date", loginDateString);
            db.update("user", cv, "_id = " + loginDataCursor.getInt(0), null);
        }
        loginDataCursor.close();
        onResume();
    }

    public void singIn(View view) {
        Intent intent = new Intent(this, SingInActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        UserSession.logOut();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
