package com.example.savingservicev2;


import java.util.ArrayList;
import java.util.HashMap;

public class UserSession {

    static private String role;
    static private String name;
    static private int phone;
    static private int userId;
    static private ArrayList<HashMap<String, String>> productCart;

    static public void enterMainActivity() {
        role = null;
        name = null;
        phone = 0;
        userId = 0;
        productCart = null;
    }

    static public String getRole() {
        return role;
    }

    static public String getName() {
        return name;
    }

    static public int getPhone() {
        return phone;
    }

    static public int getUserId() {
        return userId;
    }

    static public ArrayList getProductCart() {
        return productCart;
    }

    static public void setItemCount(String count, int position) {
        productCart.get(position).replace("COUNT", count);
    }

    static public void deleteItemFromCart(int position) {
        productCart.remove(position);
    }

    static public void clearCart() {
        productCart.clear();
    }

    static public void setRole(String roleSet, String nameSet, int phoneSet,
                               int userIdSet) {
        role = roleSet;
        name = nameSet;
        phone = phoneSet;
        userId = userIdSet;
        productCart = new ArrayList<HashMap<String, String>>();
    }

    static public void addToCart(long productId, String title, int count) {
        boolean isProductInCartChecker = false;
        for (int i = 0; i < productCart.size(); i++) {
            HashMap<String, String> indexHashMap = productCart.get(i);
            if (Integer.parseInt(indexHashMap.get("ID")) == productId) {
                int newCount = Integer.parseInt(indexHashMap.get("COUNT").toString()) + 1;
                String newCountString = newCount + "";
                productCart.get(i).replace("COUNT", newCountString);
                isProductInCartChecker = true;
                break;
            }
        }

        if (!isProductInCartChecker) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("ID", productId + "");
            hashMap.put("TITLE", title);
            hashMap.put("COUNT", count + "");
            productCart.add(hashMap);
        }
    }

    static  public void logOut() {
        role = null;
        name = null;
        phone = 0;
        userId = 0;
        productCart = null;
    }
}
