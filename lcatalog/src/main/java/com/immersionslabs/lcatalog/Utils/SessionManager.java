package com.immersionslabs.lcatalog.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.immersionslabs.lcatalog.LoginActivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    private static final String PREF_NAME = "LCatalog_Preferences";
    public static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String KEY_CURRENT_VALUE = "currentvalue";
    public static final String KEY_TOTAL_BUDGET_VALUE = "totalbudgetvalue";

    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE_NO = "mobile_no";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_GLOBAL_USER_ID = "global_user_id";
    public static final String KEY_IS_ARTICLE_ADDED = "is_article_added";


    public SessionManager(Context context) {
        this.context = context;
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String globalId, String id, String type, String name, String email, String mobile, String address, String password) {

        editor.putBoolean(IS_USER_LOGIN, true);

        editor.putString(KEY_GLOBAL_USER_ID, globalId);
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE_NO, mobile);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_USER_TYPE, type);
        editor.commit();
    }

    public void signupthings() {
        editor.remove(KEY_GLOBAL_USER_ID);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_NAME);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_MOBILE_NO);
        editor.remove(KEY_ADDRESS);
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_USER_TYPE);
        editor.putBoolean(IS_USER_LOGIN, false);
        editor.commit();
    }

    public void updatedetails(String name, String email, String mobile, String address) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE_NO, mobile);
        editor.putString(KEY_ADDRESS, address);
        editor.commit();
    }

    public boolean checkLogin() {
        if (!this.isUserLoggedIn()) {

            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

            return true;
        }
        return false;
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_GLOBAL_USER_ID, pref.getString(KEY_GLOBAL_USER_ID, null));
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_MOBILE_NO, pref.getString(KEY_MOBILE_NO, null));
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));
        user.put(KEY_USER_TYPE, pref.getString(KEY_USER_TYPE, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        return user;
    }

    public HashMap<String, Integer> getBudgetDetails() {
        String ID = pref.getString(KEY_GLOBAL_USER_ID, null);
        String id_current_budget = ID + KEY_CURRENT_VALUE;
        String id_total_budget = ID + KEY_TOTAL_BUDGET_VALUE;
        HashMap<String, Integer> user = new HashMap<String, Integer>();
        user.put(KEY_TOTAL_BUDGET_VALUE, pref.getInt(id_total_budget, 0));
        user.put(KEY_CURRENT_VALUE, pref.getInt(id_current_budget, 0));

        return user;
    }

    public void logoutUser() {
        editor.remove(KEY_GLOBAL_USER_ID);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_NAME);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_MOBILE_NO);
        editor.remove(KEY_ADDRESS);
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_USER_TYPE);
        editor.putBoolean(IS_USER_LOGIN, false);
        editor.commit();
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public void loginthings() {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.commit();
    }

    public void updatepassword(String password) {
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public void updateCurrentvalue(Integer currentvalue) {

        String ID = pref.getString(KEY_GLOBAL_USER_ID, null);
        String id_current_budget = ID + KEY_CURRENT_VALUE;


            editor.putInt(id_current_budget, currentvalue);

            editor.commit();


    }

    public void updateTotalBudget(Integer totalbudget) {
        String ID = pref.getString(KEY_GLOBAL_USER_ID, null);
        String id_total_budget = ID + KEY_TOTAL_BUDGET_VALUE;
        editor.putInt(id_total_budget, totalbudget);

        editor.commit();
    }

    public Integer BUDGET_VAL() {
        String ID = pref.getString(KEY_GLOBAL_USER_ID, null);
        String id_total_budget = ID + KEY_TOTAL_BUDGET_VALUE;
        Integer budgetval = pref.getInt(id_total_budget, 0);
        return budgetval;
    }
    public Integer CURRENT_VAL() {
        String ID = pref.getString(KEY_GLOBAL_USER_ID, null);
        String id_current_budget = ID + KEY_CURRENT_VALUE;
        Integer currentval = pref.getInt(id_current_budget, 0);
        return currentval;
    }


    public boolean IS_ARTICLE_EXISTS(String article_id) {
        String ID = pref.getString(KEY_GLOBAL_USER_ID, null);
        String id_article_added = ID + article_id + KEY_IS_ARTICLE_ADDED;
      boolean returnval=pref.getBoolean(id_article_added,false);
      return returnval;
    }

    public void ADD_ARTICLE(String article_id) {
        String id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String id_article_added = id + article_id + KEY_IS_ARTICLE_ADDED;
        Set<String> set;
        set = pref.getStringSet(id, null);
        if (set == null)
            set = new HashSet<String>();
        set.add(article_id);
        editor.putStringSet(id, set);
        editor.putBoolean(id_article_added, true);
        editor.commit();
    }

    public void REMOVE_ARTICLE(String aricle_id,String article_price) {
        String id = pref.getString(KEY_GLOBAL_USER_ID, null);
               Set<String> set;
        String id_article_added = id + aricle_id + KEY_IS_ARTICLE_ADDED;
        set = pref.getStringSet(id,null );


        if (set.contains(aricle_id)) {
            set.remove(aricle_id);
            editor.putBoolean(id_article_added,false);

           editor.commit();
        }

    }
}

