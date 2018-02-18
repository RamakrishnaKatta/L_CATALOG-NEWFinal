package com.immersionslabs.lcatalog.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.immersionslabs.lcatalog.LoginActivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SessionManager {

    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    private static final String PREF_NAME = "LCatalog_Preferences";
    public static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String KEY_CURRENT_VALUE = "currentvalue";
    public static final String KEY_TOTAL_BUDGET_VALUE = "totalbudgetvalue";
    public static final String KEY_REMAINING_VALUE = "remainingvalue";


    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE_NO = "mobile_no";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_GLOBAL_USER_ID = "global_user_id";
    Set<String> set = new HashSet<String>();


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
//        editor.clear();
//        editor.commit();
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

    public void ADD_ARTICLE(String article_id, Integer price) {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Article_Id = article_id;
        int currentvalue, total, remaining;
        String Unique_Current_Id = Global_id + KEY_CURRENT_VALUE;
        String Unique_Remaining_Id = Global_id + KEY_REMAINING_VALUE;
        String Unique_total_value_Id = Global_id + KEY_TOTAL_BUDGET_VALUE;
        String Unique_Article_Id = Global_id + Article_Id;
        total = pref.getInt(Unique_total_value_Id, 0);
        currentvalue = pref.getInt(Unique_Current_Id, 0);
        currentvalue = currentvalue + price;
        remaining = total - currentvalue;
        set = pref.getStringSet(Global_id, null);
        if (null == set)
            set = new HashSet<String>();
        set.add(Article_Id);
        editor.putInt(Unique_Current_Id, currentvalue);
        editor.putInt(Unique_Remaining_Id, remaining);
        editor.putBoolean(Unique_Article_Id, true);
        editor.putStringSet(Global_id, set);
        editor.commit();

    }

    public void REMOVE_ARTICLE(String article_id, Integer price) {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Article_Id = article_id;
        int currentvalue, total, remaining;
        String Unique_Current_Id = Global_id + KEY_CURRENT_VALUE;
        String Unique_Remaining_Id = Global_id + KEY_REMAINING_VALUE;
        String Unique_total_value_Id = Global_id + KEY_TOTAL_BUDGET_VALUE;
        String Unique_Article_Id = Global_id + Article_Id;
        total = pref.getInt(Unique_total_value_Id, 0);
        currentvalue = pref.getInt(Unique_Current_Id, 0);
        currentvalue = currentvalue - price;
        remaining = total - currentvalue;
        set.remove(Article_Id);
        editor.putInt(Unique_Current_Id, currentvalue);
        editor.putInt(Unique_Remaining_Id, remaining);
        editor.putBoolean(Unique_Article_Id, false);
        editor.putStringSet(Global_id, set);
        editor.commit();
    }

    public void SET_TOTAL_VALUE(int totalval) {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Unique_total_value_Id = Global_id + KEY_TOTAL_BUDGET_VALUE;
        editor.putInt(Unique_total_value_Id, totalval);
        editor.commit();

    }

    public int GET_CURRENT_VALUE() {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Unique_Current_Id = Global_id + KEY_CURRENT_VALUE;
        int returnval;
        returnval = pref.getInt(Unique_Current_Id, 0);
        return returnval;

    }

    public int GET_TOTAL_VALUE() {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Unique_total_value_Id = Global_id + KEY_TOTAL_BUDGET_VALUE;
        int returnval;
        returnval = pref.getInt(Unique_total_value_Id, 0);
        return returnval;

    }

    public int GET_REMAINING_VALUE() {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Unique_Remaining_Id = Global_id + KEY_REMAINING_VALUE;
        int returnval;
        returnval = pref.getInt(Unique_Remaining_Id, 0);
        return returnval;
    }

    public Set<String> ReturnID() {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        set = pref.getStringSet(Global_id, null);
        return set;
    }

    public boolean IS_ARTICLE_EXISTS(String article_id) {
        boolean flag=false;

        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        set = pref.getStringSet(Global_id, null);
        if (null == set) {
            //do nothng
        } else {
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                String value = iterator.next();
                if (value.equals(article_id)) {
                    flag = true;
                }
                else
                {
                    flag=false;
                }

                if(flag)
                break;
            }

        }
        return flag;
    }
}