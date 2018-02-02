package com.immersionslabs.lcatalog.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.immersionslabs.lcatalog.LoginActivity;

import java.util.HashMap;

public class SessionManager {
    public static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE_NO = "mobile_no";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_GLOBAL_USER_ID = "global_user_id";
    private static final String PREF_NAME = "LCatalog_Preferences";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

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
        editor.clear();
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

    public void logoutUser() {
        editor.clear();
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
}
