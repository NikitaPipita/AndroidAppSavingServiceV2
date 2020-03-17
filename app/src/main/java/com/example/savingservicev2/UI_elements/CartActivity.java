package com.example.savingservicev2.UI_elements;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savingservicev2.DatabaseHelper;
import com.example.savingservicev2.MainActivity;
import com.example.savingservicev2.R;
import com.example.savingservicev2.StatisticsActivity;
import com.example.savingservicev2.UserSession;
import com.example.savingservicev2.view_elements.OrderActivity;
import com.example.savingservicev2.view_elements.SupermarketActivity;
import com.example.savingservicev2.view_elements.UserActivity;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {

    ListView productInCartList;
    Menu mainMenu;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        productInCartList = (ListView)findViewById(R.id.productInCartList);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.open();

        productInCartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                HashMap<String, String> selectedHashMap =
                        (HashMap<String, String>) parent.getItemAtPosition(position);
                AlertDialog.Builder dialog = new AlertDialog.Builder(CartActivity.this);
                dialog.setTitle(selectedHashMap.get("TITLE").toString());
                LinearLayout listView = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog, null);
                dialog.setView(listView);

                final TextView itemCount = (TextView) listView.findViewById(R.id.itemCount);
                Button minusButton = (Button) listView.findViewById(R.id.minusButton);
                Button plusButton = (Button) listView.findViewById(R.id.plusButton);
                itemCount.setText(selectedHashMap.get("COUNT").toString());
                minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentItemCount = Integer.parseInt(itemCount.getText().toString()) - 1;
                        if (currentItemCount < 1) {
                            itemCount.setText(1 + "");
                        } else {
                            itemCount.setText(currentItemCount + "");
                        }
                    }
                });
                plusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentItemCount = Integer.parseInt(itemCount.getText().toString()) + 1;
                        if (currentItemCount > 99) {
                            itemCount.setText(99 + "");
                        } else {
                            itemCount.setText(currentItemCount + "");
                        }
                    }
                });

                dialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                UserSession.setItemCount(itemCount.getText().toString(),
                                        position);
                                dialog.cancel();
                                onResume();
                            }
                        });

                dialog.setNegativeButton("Удалить",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                UserSession.deleteItemFromCart(position);
                                dialog.cancel();
                                onResume();
                            }
                        });
                dialog.create();
                dialog.show();
            }
        });
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
            mainMenu.getItem(7).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_product :
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            case
                    R.id.action_user:
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
                mainMenu.getItem(4).setVisible(true);
            }

            if (UserSession.getRole().equals("admin")) {
                mainMenu.getItem(7).setVisible(true);
            } else {
                mainMenu.getItem(7).setVisible(false);
            }
        }

        ArrayList<HashMap<String, Object>> userCart = UserSession.getProductCart();
        String[] from = new String[] {"TITLE", "COUNT"};
        int[] to = new int[] {R.id.text1, R.id.text2};
        ListAdapter cartAdapter = new SimpleAdapter(this, userCart,
                R.layout.product_in_cart_adapter, from, to);
        productInCartList.setAdapter(cartAdapter);
    }

    public void buy(View view){
        ArrayList<HashMap<String, String>> itemsInCart = UserSession.getProductCart();
        if (itemsInCart.size() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Корзина пуста", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            ArrayList<ArrayList<Integer>> supermarketMatrix = new ArrayList<ArrayList<Integer>>();
            ArrayList<Integer> supermarketList = new ArrayList<Integer>();

            for (int i = 0; i < itemsInCart.size(); i++) {
                for (int j = i + 1; j < itemsInCart.size(); j++) {
                    if (Integer.parseInt(itemsInCart.get(i).get("ID")) > Integer.parseInt(itemsInCart.get(j).get("ID"))) {
                        HashMap<String, String> temp = itemsInCart.get(i);
                        itemsInCart.set(i, itemsInCart.get(j));
                        itemsInCart.set(j, temp);
                    }
                }
            }

            for (int i = 0; i < itemsInCart.size(); i++) {
                int productId = Integer.parseInt(itemsInCart.get(i).get("ID"));
                supermarketMatrix.add(new ArrayList<Integer>());
                Cursor currentProductInSups = db.rawQuery("SELECT sup_id FROM sup_prod WHERE" +
                        " prod_id = " + productId, null);
                currentProductInSups.moveToFirst();
                while (currentProductInSups.isAfterLast() == false) {
                    int supId = Integer.parseInt(currentProductInSups.getString(0));
                    supermarketMatrix.get(i).add(supId);
                    if (!supermarketList.contains(supId)) {
                        supermarketList.add(supId);
                    }
                    currentProductInSups.moveToNext();
                }
                currentProductInSups.close();
            }

            ArrayList<Integer> finalSupList = (ArrayList<Integer>) supermarketList.clone();
            for (int i = 0; i < supermarketList.size(); i++) {
                int currentSup = supermarketList.get(i);
                for (int j = 0; j < supermarketMatrix.size(); j++) {
                    if (!supermarketMatrix.get(j).contains(currentSup)) {
                        int indexSupToDelete = finalSupList.indexOf(currentSup);
                        finalSupList.remove(indexSupToDelete);
                        break;
                    }
                }
            }

            final ArrayList<HashMap<String, String>> supAndPrice = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < finalSupList.size(); i++) {
                double totalPriceInSup = 0;
                int supId = finalSupList.get(i);
                String prodList = "(";
                for (int j = 0; j < itemsInCart.size(); j++) {
                    prodList += itemsInCart.get(j).get("ID") + ", ";
                }

                prodList = prodList.substring(0, prodList.length() - 2) + ")";
                Cursor columnPrice = db.rawQuery("SELECT _id, price FROM sup_prod WHERE " +
                        "sup_id = " + supId + " AND prod_id IN " + prodList + " ORDER BY _id", null);
                columnPrice.moveToFirst();
                for (int j = 0; columnPrice.isAfterLast() == false; j++) {
                    totalPriceInSup += columnPrice.getDouble(1) * Double.
                            parseDouble(itemsInCart.get(j).get("COUNT"));
                    columnPrice.moveToNext();
                }
                columnPrice.close();

                Cursor supTitle = db.rawQuery("SELECT title FROM supermarket WHERE _id = " + supId, null);
                supTitle.moveToFirst();
                String supTitleString = supTitle.getString(0);
                supTitle.close();

                HashMap<String, String> currentSupAndPrice = new HashMap<String, String>();
                currentSupAndPrice.put("TITLE", supTitleString);
                currentSupAndPrice.put("PRICE", Double.toString(totalPriceInSup));
                supAndPrice.add(currentSupAndPrice);
            }

            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Выберите супермаркет для покупки");

            LinearLayout listView = (LinearLayout) getLayoutInflater().inflate(R.layout.select_supermarket_dialog, null);
            dialog.setView(listView);
            final ListView supPriceList = (ListView) listView.findViewById(R.id.supPriceList);
            final TextView orderAccept = (TextView) listView.findViewById(R.id.acceptOrder);
            String[] from = new String[]{"TITLE", "PRICE"};
            int[] to = new int[]{R.id.text1, R.id.text2};
            ListAdapter cartAdapter = new SimpleAdapter(this, supAndPrice,
                    R.layout.supermarket_price_adapter, from, to);
            supPriceList.setAdapter(cartAdapter);

            supPriceList.setVisibility(View.VISIBLE);
            orderAccept.setVisibility(View.GONE);

            supPriceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    String supTitle = supAndPrice.get(position).get("TITLE");
                    String priceSum = supAndPrice.get(position).get("PRICE");
                    Cursor getSupId = db.rawQuery("SELECT _id FROM supermarket WHERE title = '" +
                            supTitle + "'", null);
                    getSupId.moveToFirst();
                    int supId = getSupId.getInt(0);
                    getSupId.close();
                    addBuyActionToDb(supId, priceSum);

                    UserSession.clearCart();
                    supPriceList.setVisibility(View.GONE);
                    orderAccept.setVisibility(View.VISIBLE);

                    onResume();
                }
            });

            dialog.create();
            dialog.show();
        }
    }

    public void addBuyActionToDb(int supId, String priceSum) {
        ContentValues cv = new ContentValues();
        cv.put("user_id", UserSession.getUserId());
        cv.put("sup_id", supId);

        Date loginTime = new Date();
        LocalDate loginDate = LocalDate.now();
        String[] time = loginTime.toString().split(" ");
        String loginDateString = loginDate.getYear() + "-" + loginDate.getMonthValue() +
                "-" + loginDate.getDayOfMonth() + " " + time[3];
        cv.put("date_time", loginDateString);
        cv.put("sum", Double.parseDouble(priceSum));
        db.insert("'order'", null, cv);

        Cursor maxOrder = db.rawQuery("SELECT MAX(_id) FROM 'order'", null);
        maxOrder.moveToFirst();
        int orderId = maxOrder.getInt(0);
        maxOrder.close();

        ArrayList<HashMap<String, String>> itemsInCart = UserSession.getProductCart();
        for (int i = 0; i < itemsInCart.size(); i++) {
            ContentValues orderItemCV = new ContentValues();
            int prodId = Integer.parseInt(itemsInCart.get(i).get("ID"));
            Cursor supProdCursor = db.rawQuery("SELECT _id FROM sup_prod WHERE " +
                    "sup_id = " + supId + " AND prod_id = " + prodId, null);
            supProdCursor.moveToFirst();
            int supProdId = supProdCursor.getInt(0);
            supProdCursor.close();

            orderItemCV.put("sup_prod_id", supProdId);
            orderItemCV.put("order_id", orderId);
            orderItemCV.put("count", Integer.parseInt(itemsInCart.get(i).get("COUNT")));
            db.insert("order_list", null, orderItemCV);
        }
    }
}
