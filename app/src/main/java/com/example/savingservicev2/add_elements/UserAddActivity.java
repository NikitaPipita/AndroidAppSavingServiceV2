package com.example.savingservicev2.add_elements;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.Cursor;
import android.widget.Toast;

import com.example.savingservicev2.DatabaseHelper;
import com.example.savingservicev2.R;
import com.example.savingservicev2.view_elements.UserActivity;

public class UserAddActivity extends AppCompatActivity {

    EditText phone;
    EditText password;
    EditText name;
    EditText login_date;
    EditText role;
    Button addButton;
    Button deleteButton;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);

        phone = (EditText)findViewById(R.id.phone);
        password = (EditText)findViewById(R.id.password);
        name = (EditText)findViewById(R.id.name);
        login_date = (EditText)findViewById(R.id.login_date);
        role = (EditText)findViewById(R.id.role);
        addButton = (Button)findViewById(R.id.addButton);
        deleteButton = (Button)findViewById(R.id.deleteButton);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }

        if (userId > 0) {
            userCursor = db.rawQuery("SELECT * FROM user WHERE _id" + "=?",
                    new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();

            int phoneInt = userCursor.getInt(1);
            String phoneString = phoneInt + "";
            phone.setText(phoneString);
            name.setText(userCursor.getString(3));
            login_date.setText(userCursor.getString(4));
            role.setText(userCursor.getString(5));
            userCursor.close();

            password.setVisibility(View.GONE);
        } else {
            deleteButton.setVisibility(View.GONE);
        }
    }

    public void save(View view){
        if (phone.getText().toString().equals("") || name.getText().toString().equals("") ||
                login_date.getText().toString().equals("") || role.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Все поля должны быть заполнены", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            ContentValues cv = new ContentValues();
            try {
                cv.put("phone", Integer.parseInt(phone.getText().toString()));
            } catch (Exception e) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Телефон должен состоять только из цифр", Toast.LENGTH_SHORT);
                toast.show();
            }
            cv.put("name", name.getText().toString());
            cv.put("login_date", login_date.getText().toString());
            cv.put("role", role.getText().toString());

            if (userId > 0) {
                db.update("user", cv, "_id" + "=" + String.valueOf(userId), null);
            } else {
                if (password.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Поле с паролем должно быть заполнено", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    cv.put("password", password.getText().toString());
                    db.insert("user", null, cv);
                }
            }
            goHome();
        }
    }

    public void delete(View view){
        db.delete("user", "_id = ?", new String[]{String.valueOf(userId)});
        goHome();
    }

    private void goHome(){
        db.close();
        Intent intent = new Intent(this, UserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
