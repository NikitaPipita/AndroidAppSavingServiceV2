package com.example.savingservicev2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import android.content.Context;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.savingservicev2.UI_elements.CartActivity;
import com.example.savingservicev2.UI_elements.FilterActivity;
import com.example.savingservicev2.UI_elements.InstructionActivity;
import com.example.savingservicev2.UI_elements.LoginActivity;
import com.example.savingservicev2.add_elements.ProductActivity;
import com.example.savingservicev2.view_elements.OrderActivity;
import com.example.savingservicev2.view_elements.SupermarketActivity;
import com.example.savingservicev2.view_elements.UserActivity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    ListView productList;
    TextView elemCount;
    EditText searchLine;
    Button insertInDbButton;
    Button addProductButton;

    ImageButton category1;
    ImageButton category2;
    ImageButton category3;
    ImageButton category4;
    ImageButton category5;
    ImageButton category6;
    ImageButton category7;
    ImageButton category8;
    ImageButton category9;
    ImageButton category10;
    ImageButton loginButton;
    ImageButton cartButton;
    ImageButton filterButton;
    ImageButton searchButton;


    Menu mainMenu;
    LinearLayout adminPanel;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor productCursor;
    SimpleCursorAdapter productAdapter;

    String simpleProductQuery = "SELECT product._id, title, count, image, category, MIN(price) " +
            "FROM product " +
            "INNER JOIN sup_prod " +
            "ON product._id = sup_prod.prod_id";

    String[] categories = {"'Вино'", "'Крепкий алкоголь'", "'Ликеры'", "'Пиво'",
            "'Слабоалкогольные напитки'", "'Шампанское и игристое вино'", "'Энергетические напитки'"};
    String searchQuery = "WHERE";
    String priceAndCategoryQuery = "WHERE";
    int[] selected = {0, 0, 0, 0, 0, 0, 0};
    boolean accept = false;
    int sortStyle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserSession.enterMainActivity();

        productList = (ListView)findViewById(R.id.productList);
        elemCount = (TextView)findViewById(R.id.elemCount);
        searchLine = (EditText)findViewById(R.id.searchLine);
        insertInDbButton = (Button)findViewById(R.id.insertInDbButton);
        addProductButton = (Button)findViewById(R.id.addProductButton);
        adminPanel = (LinearLayout)findViewById(R.id.adminPanel);

        category1 = (ImageButton)findViewById(R.id.category1);
        category2 = (ImageButton)findViewById(R.id.category2);
        category3 = (ImageButton)findViewById(R.id.category3);
        category4 = (ImageButton)findViewById(R.id.category4);
        category5 = (ImageButton)findViewById(R.id.category5);
        category6 = (ImageButton)findViewById(R.id.category6);
        category7 = (ImageButton)findViewById(R.id.category7);
        category8 = (ImageButton)findViewById(R.id.category8);
        category9 = (ImageButton)findViewById(R.id.category9);
        category10 = (ImageButton)findViewById(R.id.category10);
        loginButton = (ImageButton)findViewById(R.id.loginButton);
        cartButton = (ImageButton)findViewById(R.id.cartButton);
        filterButton = (ImageButton)findViewById(R.id.filterButton);
        searchButton = (ImageButton)findViewById(R.id.searchButton);

        category1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    category1.setBackgroundColor(Color.parseColor("#4dc9dc"));
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    category1.setBackgroundColor(Color.parseColor("#f0f2ef"));
                    return true;
                }
                return false;
            }
        });

        category2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    category2.setBackgroundColor(Color.parseColor("#4dc9dc"));
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    category2.setBackgroundColor(Color.parseColor("#f0f2ef"));
                    return true;
                }
                return false;
            }
        });

        category3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    category3.setBackgroundColor(Color.parseColor("#4dc9dc"));
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    category3.setBackgroundColor(Color.parseColor("#f0f2ef"));
                    return true;
                }
                return false;
            }
        });

        category4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    category4.setBackgroundColor(Color.parseColor("#4dc9dc"));
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    category4.setBackgroundColor(Color.parseColor("#f0f2ef"));
                    return true;
                }
                return false;
            }
        });

        category5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    category5.setBackgroundColor(Color.parseColor("#4dc9dc"));
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    category5.setBackgroundColor(Color.parseColor("#f0f2ef"));
                    return true;
                }
                return false;
            }
        });

        category6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    category6.setBackgroundColor(Color.parseColor("#4dc9dc"));
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    category6.setBackgroundColor(Color.parseColor("#f0f2ef"));
                    return true;
                }
                return false;
            }
        });

        category7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    category7.setBackgroundColor(Color.parseColor("#4dc9dc"));
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    category7.setBackgroundColor(Color.parseColor("#f0f2ef"));
                    return true;
                }
                return false;
            }
        });

        category8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    category8.setBackgroundColor(Color.parseColor("#4dc9dc"));
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    category8.setBackgroundColor(Color.parseColor("#f0f2ef"));
                    return true;
                }
                return false;
            }
        });

        category9.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    category9.setBackgroundColor(Color.parseColor("#4dc9dc"));
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    category9.setBackgroundColor(Color.parseColor("#f0f2ef"));
                    return true;
                }
                return false;
            }
        });

        category10.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    category10.setBackgroundColor(Color.parseColor("#4dc9dc"));
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    category10.setBackgroundColor(Color.parseColor("#f0f2ef"));
                    return true;
                }
                return false;
            }
        });

        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    loginButton.setBackgroundColor(Color.parseColor("#4dc9dc"));
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    loginButton.setBackgroundColor(Color.parseColor("#f0f2ef"));
                    login();
                    return true;
                }
                return false;
            }
        });

        cartButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    cartButton.setBackgroundColor(Color.parseColor("#4dc9dc"));
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    cartButton.setBackgroundColor(Color.parseColor("#f0f2ef"));
                    cart();
                    return true;
                }
                return false;
            }
        });

        filterButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    filterButton.setBackgroundColor(Color.parseColor("#4dc9dc"));
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    filterButton.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    addFilter();
                    return true;
                }
                return false;
            }
        });

        searchButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    searchButton.setBackgroundColor(Color.parseColor("#4dc9dc"));
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    searchButton.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    search();
                    return true;
                }
                return false;
            }
        });


        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.create_db();
        db = databaseHelper.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mainMenu = menu;

        mainMenu.getItem(4).setVisible(false);
        mainMenu.getItem(5).setVisible(false);

        if(UserSession.getRole() == null) {
            mainMenu.getItem(4).setVisible(false);
        }

        if (UserSession.getRole() == null || !UserSession.getRole().equals("admin")) {
            mainMenu.getItem(0).setVisible(false);
            mainMenu.getItem(1).setVisible(false);
            mainMenu.getItem(2).setVisible(false);
            mainMenu.getItem(3).setVisible(false);
            mainMenu.getItem(7).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_user:
                intent = new Intent(this, UserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            case R.id.action_supermarket:
                intent = new Intent(this, SupermarketActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            case R.id.action_statistics:
                intent = new Intent(this, StatisticsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            case R.id.action_cart:
                intent = new Intent(this, CartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            case R.id.login:
                intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            case R.id.instruction:
                intent = new Intent(this, InstructionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            case R.id.action_order:
                intent = new Intent(this, OrderActivity.class);
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
                mainMenu.getItem(4).setVisible(false);
                //mainMenu.getItem(4).setVisible(true);
            }
        }

        if (UserSession.getRole() != null && UserSession.getRole().equals("admin")) {
            insertInDbButton.setVisibility(View.VISIBLE);
            addProductButton.setVisibility(View.VISIBLE);
            adminPanel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 3));
            elemCount.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 1));
            productList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 18));

            if (mainMenu != null) {
                mainMenu.getItem(0).setVisible(true);
                mainMenu.getItem(1).setVisible(true);
                mainMenu.getItem(2).setVisible(true);
                mainMenu.getItem(3).setVisible(true);
                mainMenu.getItem(7).setVisible(true);
            }
        } else {
            insertInDbButton.setVisibility(View.GONE);
            addProductButton.setVisibility(View.GONE);
            adminPanel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 0));
            elemCount.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 0));
            productList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 24));

            if (mainMenu != null) {
                mainMenu.getItem(0).setVisible(false);
                mainMenu.getItem(1).setVisible(false);
                mainMenu.getItem(2).setVisible(false);
                mainMenu.getItem(3).setVisible(false);
                mainMenu.getItem(7).setVisible(false);
            }
        }

        if (accept) {
            priceAndCategoryQuery = "WHERE";
            accept = false;

            String categoriesString = "";
            for (int i = 0; i < 7; i++) {
                if (selected[i] != 0) {
                    categoriesString += categories[i] + ", ";
                }
            }
            if (!categoriesString.equals("")) {
                categoriesString = "(" + categoriesString.substring(0, categoriesString.length() - 2) + ")";
                priceAndCategoryQuery += " category IN " + categoriesString;
            }
        }

        productQuery(searchQuery, priceAndCategoryQuery);


    }

    public void openFileAndInsertInfo(View view) {
        openFile(Uri.parse("/sdcard/Documents"));
    }

    // Request code for selecting a PDF document.
    private static final int PICK_FILE = 2;

    private void openFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/comma-separated-values");

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, PICK_FILE);
    }

    private void readTextFromUri(Uri uri) {
        try {
            String uriPath = uri.getPath();
            int lastSlash = uriPath.lastIndexOf("/");
            String fileName = uriPath.substring(lastSlash + 1, uriPath.length());

            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(inputStream), "Cp1251"));
            insertInDB(reader);


        } catch (Exception e) {

        }
    }

    public void insertInDB(BufferedReader reader) {
        try {
            String line = null;
            Scanner scanner = null;
            int index = 0;

            while ((line = reader.readLine()) != null) {
                String[] values = new String[6];
                scanner = new Scanner(line);
                scanner.useDelimiter(";");
                while (scanner.hasNext()) {
                    String data = scanner.next();
                    if (index < 5) {
                        values[index] = data;
                    } else if (index == 5) {
                        if (data.equals("Добавить в корзину")) {

                            //Insert in table product
                            ContentValues cv_prod = new ContentValues();
                            cv_prod.put("title", values[0]);
                            cv_prod.put("count", values[1]);
                            cv_prod.put("image", values[2]);
                            cv_prod.put("category", values[4]);
                            db.insert("product", null, cv_prod);


                            //Insert in table sup_prod
                            Cursor insertProduct = db.rawQuery("SELECT MAX(_id) FROM product", null);
                            insertProduct.moveToFirst();
                            int id_prod = insertProduct.getInt(0);

                            ContentValues cv_sup_prod = new ContentValues();
                            cv_sup_prod.put("sup_id", 2);
                            cv_sup_prod.put("prod_id", id_prod);

                            double productPrice;
                            String[] productPriceSplit = values[3].split(",");
                            if (productPriceSplit.length == 2) {
                                String priceWithDot = productPriceSplit[0] + "." +productPriceSplit[1];
                                productPrice = Double.parseDouble(priceWithDot);
                            } else {
                                productPrice = Double.parseDouble(productPriceSplit[0]);
                            }

                            cv_sup_prod.put("price", productPrice);
                            db.insert("sup_prod", null, cv_sup_prod);

                            insertProduct.close();
                        }
                    } else {
                        System.out.println("Некорректные данные::" + data);
                    }
                    index++;
                }
                index = 0;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        onResume();
    }

    public void add(View view){
        Intent intent = new Intent(this, ProductActivity.class);
        startActivity(intent);
    }

    public void search() {
        String searchSubstring = searchLine.getText().toString();
        if (!searchSubstring.equals("")) {
            String lowerCaseSubstring;
            String upperCaseSubstring;
            String firstLetter = searchSubstring.charAt(0) + "";
            lowerCaseSubstring = "'%" + firstLetter.toLowerCase() + searchSubstring.substring(1) + "%'";
            upperCaseSubstring = "'%" + firstLetter.toUpperCase() + searchSubstring.substring(1) + "%'";
            searchQuery = "WHERE title LIKE " + lowerCaseSubstring +
                    " OR " + upperCaseSubstring;
        } else {
            searchQuery = "WHERE";
        }
        productQuery(searchQuery, priceAndCategoryQuery);
    }

    public void addFilter() {
        Intent intent = new Intent(this, FilterActivity.class);
        intent.putExtra("selected", selected);
        intent.putExtra("sortStyle", sortStyle);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        Uri uri = null;
        uri = data.getData();

        if (uri == null) {
            selected = data.getIntArrayExtra("selected");
            sortStyle = data.getIntExtra("sortStyle", 0);
            accept = data.getBooleanExtra("accept", true);
        } else {
            readTextFromUri(uri);
        }
    }

    public void productQuery(String searchQuery, String priceAndCategoryQuery) {
        String sortQuery = "";
        if (sortStyle == 0) {
            sortQuery = "";
        } else if (sortStyle == 1) {
            sortQuery = " ORDER BY price";
        } else if (sortStyle == 2) {
            sortQuery = " ORDER BY price DESC";
        }

        if (searchQuery.equals("WHERE") && priceAndCategoryQuery.equals("WHERE")) {
            productCursor = db.rawQuery(simpleProductQuery + " GROUP BY product._id" + sortQuery, null);
        } else if (!searchQuery.equals("WHERE") && !priceAndCategoryQuery.equals("WHERE")) {
            productCursor = db.rawQuery("SELECT * FROM ("+ simpleProductQuery + " "
                    + priceAndCategoryQuery + " GROUP BY product._id" + sortQuery +") " + searchQuery, null);
        } else if (!searchQuery.equals("WHERE")) {
            productCursor = db.rawQuery(simpleProductQuery + " " + searchQuery +
                    " GROUP BY product._id" + sortQuery, null);
        } else if (!priceAndCategoryQuery.equals("WHERE")) {
            productCursor = db.rawQuery(simpleProductQuery + " " + priceAndCategoryQuery +
                    " GROUP BY product._id" + sortQuery, null);
        }

        //      Когда-нибудь оно будет выводить картинку
        //String[] from = new String[] {"title", "count", "image", "category", "MIN(price)"};
        //int[] to = new int[] {R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5};

        String[] from = new String[] {"title", "count", "category", "MIN(price)"};
        int[] to = new int[] {R.id.text1, R.id.text2, R.id.text4, R.id.text5};
        productAdapter = new SimpleCursorAdapter(this, R.layout.product_adapter, null, from, to, 0);
        productAdapter.swapCursor(productCursor);
        elemCount.setText("Кол-во элементов в выборке: " + String.valueOf(productCursor.getCount()));
        productList.setAdapter(productAdapter);

        Toast toast = Toast.makeText(getApplicationContext(),
                "Найдено товаров: " + productCursor.getCount(), Toast.LENGTH_SHORT);
        toast.show();
    }

    public void login() {
        Intent intent;
        intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void cart() {
        if (UserSession.getRole() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            Intent intent;
            intent = new Intent(this, CartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        productCursor.close();
        db.close();
    }
}
