package com.example.savingservicev2.add_elements;

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
import com.example.savingservicev2.UserSession;

import java.time.LocalDate;
import java.util.Date;

public class CommentAddActivity extends AppCompatActivity {

    EditText text;
    EditText date;
    Button deleteButton;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor commentCursor;
    long commentId = 0;
    long productId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_add);

        text = (EditText)findViewById(R.id.text);
        date = (EditText)findViewById(R.id.date);
        deleteButton = (Button)findViewById(R.id.deleteButton);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            commentId = extras.getLong("id");
            productId = extras.getLong("productID");
        }

        if (commentId > 0) {
            commentCursor = db.rawQuery("SELECT * FROM comment WHERE _id" + "=?",
                    new String[]{String.valueOf(commentId)});
            commentCursor.moveToFirst();
            text.setText(commentCursor.getString(3));
            date.setText(commentCursor.getString(4));
            commentCursor.close();
        } else {
            deleteButton.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
        }
    }

    public void save(View view){
        ContentValues cv = new ContentValues();
        if (commentId > 0) {
            cv.put("text", text.getText().toString());
            cv.put("date", date.getText().toString());
        } else {
            cv.put("user_id", UserSession.getUserId());
            cv.put("prod_id", productId);
            cv.put("text", text.getText().toString());

            Date loginTime = new Date();
            LocalDate loginDate = LocalDate.now();
            String[] time = loginTime.toString().split(" ");
            String loginDateString = loginDate.getYear() + "-" + loginDate.getMonthValue() +
                    "-" + loginDate.getDayOfMonth() + " " + time[3];
            cv.put("date", loginDateString);
        }

        if (commentId > 0) {
            db.update("comment", cv, "_id" + "=" + String.valueOf(commentId), null);
        } else {
            db.insert("comment", null, cv);
        }
        goHome();
    }

    public void delete(View view){
        db.delete("comment", "_id = ?", new String[]{String.valueOf(commentId)});
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
