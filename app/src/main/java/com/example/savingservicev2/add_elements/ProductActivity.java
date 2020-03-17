package com.example.savingservicev2.add_elements;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.database.Cursor;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savingservicev2.DatabaseHelper;
import com.example.savingservicev2.UI_elements.LoginActivity;
import com.example.savingservicev2.MainActivity;
import com.example.savingservicev2.R;
import com.example.savingservicev2.view_elements.SupProdActivity;
import com.example.savingservicev2.UserSession;

public class ProductActivity extends AppCompatActivity {

    EditText title;
    EditText count;
    EditText image;
    EditText category;
    Button addButton;
    Button deleteButton;
    Button addToSupButton;
    Button addToCartButton;
    Button addCommentButton;
    ListView supProdList;
    ListView commentList;
    LinearLayout userContentLayout;
    TextView userContentTitle;
    TextView userContentCount;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor productCursor;
    Cursor supProdCursor;
    Cursor commentCursor;
    long productId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        title = (EditText)findViewById(R.id.title);
        count = (EditText)findViewById(R.id.count);
        image = (EditText)findViewById(R.id.image);
        category = (EditText)findViewById(R.id.category);
        addButton = (Button)findViewById(R.id.addButton);
        deleteButton = (Button)findViewById(R.id.deleteButton);
        addToSupButton = (Button)findViewById(R.id.addToSupButton);
        addToCartButton = (Button) findViewById(R.id.addToCartButton);
        addCommentButton = (Button)findViewById(R.id.addCommentButton);
        supProdList = (ListView)findViewById(R.id.supProdList);
        commentList = (ListView)findViewById(R.id.commentList);
        userContentLayout = (LinearLayout) findViewById(R.id.userContentLayout);
        userContentTitle = (TextView) findViewById(R.id.userContentTitle);
        userContentCount = (TextView) findViewById(R.id.userContentCount);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productId = extras.getLong("id");
        }

        if (productId > 0) {
            productCursor = db.rawQuery("SELECT * FROM product WHERE _id" + "=?",
                    new String[]{String.valueOf(productId)});
            productCursor.moveToFirst();
            title.setText(productCursor.getString(1));
            count.setText(productCursor.getString(2));
            image.setText(productCursor.getString(3));
            category.setText(productCursor.getString(4));

            userContentTitle.setText(productCursor.getString(1));
            userContentCount.setText(productCursor.getString(2));

            productCursor.close();
        } else {
            addToSupButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            addCommentButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (productId > 0) {
            supProdCursor = db.rawQuery("SELECT sup_prod._id, supermarket.title, sup_prod.price " +
                    "FROM supermarket " +
                    "INNER JOIN sup_prod " +
                    "ON supermarket._id = sup_prod.sup_id " +
                    "WHERE sup_prod.prod_id = " + productId, null);
            String[] from = new String[] {"title", "price"};
            int[] to = new int[] {R.id.text1, R.id.text2};
            SimpleCursorAdapter supProdAdapter = new SimpleCursorAdapter(this,
                    R.layout.sup_prod_adapter, null, from, to, 0);
            supProdAdapter.swapCursor(supProdCursor);
            supProdList.setAdapter(supProdAdapter);

            if (UserSession.getRole() != null && UserSession.getRole().equals("admin")) {
                supProdList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), SupProdActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                });
            }

            if (UserSession.getRole() != null && UserSession.getRole().equals("admin")) {
                title.setVisibility(View.VISIBLE);
                count.setVisibility(View.VISIBLE);
                image.setVisibility(View.VISIBLE);
                category.setVisibility(View.VISIBLE);

                addButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                addToSupButton.setVisibility(View.VISIBLE);

                userContentCount.setVisibility(View.GONE);
                userContentTitle.setVisibility(View.GONE);
                userContentLayout.setVisibility(View.GONE);
            } else {
                title.setVisibility(View.GONE);
                count.setVisibility(View.GONE);
                image.setVisibility(View.GONE);
                category.setVisibility(View.GONE);

                addButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                addToSupButton.setVisibility(View.GONE);

                userContentCount.setVisibility(View.VISIBLE);
                userContentTitle.setVisibility(View.VISIBLE);
                userContentLayout.setVisibility(View.VISIBLE);
            }
        }

        if (productId > 0) {
            commentCursor = db.rawQuery("SELECT comment._id, user.name, comment.text, comment.date " +
                    "FROM comment INNER JOIN user " +
                    "ON comment.user_id = user._id " +
                    "WHERE comment.prod_id = " + productId, null);
            String[] from = new String[] {"name", "date", "text"};
            int[] to = new int[] {R.id.text1, R.id.text2, R.id.text3};
            SimpleCursorAdapter commentAdapter = new SimpleCursorAdapter(this,
                    R.layout.comment_adapter, null, from, to, 0);
            commentAdapter.swapCursor(commentCursor);
            commentList.setAdapter(commentAdapter);

            if (UserSession.getRole() != null && UserSession.getRole().equals("admin")) {
                commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), CommentAddActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("productID", 0);
                        startActivity(intent);
                    }
                });
            }
        } else {
            userContentLayout.setVisibility(View.GONE);
            addToCartButton.setVisibility(View.GONE);
        }
    }

    public void save(View view){
        if (title.getText().toString().equals("") || count.getText().toString().equals("") ||
                image.getText().toString().equals("") || category.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Все поля должны быть заполнены", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            ContentValues cv = new ContentValues();
            cv.put("title", title.getText().toString());
            cv.put("count", count.getText().toString());
            cv.put("image", image.getText().toString());
            cv.put("category", category.getText().toString());

            if (productId > 0) {
                db.update("product", cv, "_id" + "=" + String.valueOf(productId), null);
                supProdCursor.close();
                commentCursor.close();
            } else {
                db.insert("product", null, cv);

                Cursor insertProduct = db.rawQuery("SELECT MAX(_id) FROM product", null);
                insertProduct.moveToFirst();
                int id_prod = insertProduct.getInt(0);

                ContentValues cv_sup_prod = new ContentValues();
                cv_sup_prod.put("sup_id", 1);
                cv_sup_prod.put("prod_id", id_prod);
                cv_sup_prod.put("price", 10);
                db.insert("sup_prod", null, cv_sup_prod);

                insertProduct.close();
            }
            goHome();
        }
    }

    public void delete(View view){
        db.delete("product", "_id = ?", new String[]{String.valueOf(productId)});
        goHome();
    }

    public void addProdToSup(View view) {
        Intent intent = new Intent(getApplicationContext(), SupProdAddActivity.class);
        intent.putExtra("id", productId);
        startActivity(intent);
    }

    public void addToCart(View view) {
        if (UserSession.getRole() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            UserSession.addToCart(productId, userContentTitle.getText().toString(), 1);
        }
    }

    public void addComment(View view) {
        if (UserSession.getRole() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), CommentAddActivity.class);
            intent.putExtra("id", 0);
            intent.putExtra("productID", productId);
            startActivity(intent);
        }
    }

    private void goHome(){
        db.close();
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
