package com.immersionslabs.lcatalog.Utils;

import android.util.Log;

import java.util.HashMap;

public class RegisterCollection {
    private static int index = 0;
    private static HashMap<Integer, GetItems> u_hashmap = new HashMap<Integer, GetItems>();
    private String u_userId, u_username, u_useraddress, u_userEmail, u_userPhone, u_userpassword;

    public RegisterCollection(String userId, String userName, String userAddress, String userEmail, String userPhone, String userpassword) {

        GetItems u_register = new GetItems();

        u_register.setUserName(userName);
        Log.e("REGISTER", userName);
        u_register.setUserAddress(userAddress);
        Log.e("REGISTER", userAddress);
        u_register.setUserEmail(userEmail);
        Log.e("REGISTER", userEmail);
        u_register.setUserPhone(userPhone);
        Log.e("REGISTER", userPhone);
        u_register.setUserPassword(userpassword);
        Log.e("RegisterCollection: REGISTER", userpassword);

        u_hashmap.put(++index, u_register);
    }

    public RegisterCollection(String userName, String userEmail, String userMobileNo, String userAddress, String userPassword) {

    }
    public RegisterCollection(){

    }

    public int validator(String r_username, String r_password) {

        Log.e("VALIDATOR", r_username + " // " + r_password);

        GetItems u_register = new GetItems();
        int flag = 0;

        if (u_hashmap.isEmpty()) {
            flag = 0;
            Log.e("VALIDATOR", " There is nothing in the hashmap");

        } else {
            Log.e("VALIDATOR", u_hashmap.toString());
        }

        for (int i = 1; i <= index; i++) {

            u_register = u_hashmap.get(i);

            u_username = u_register.getUserName();
            u_useraddress = u_register.getUserAddress();
            u_userEmail = u_register.getUserEmail();
            u_userPhone = u_register.getUserPhone();


            Log.e("VALIDATOR: ", u_username + " // " +  u_useraddress + " // " + u_userEmail + " // " + u_userPhone);

            if (r_username.equals(u_username) && r_password.equals(u_userpassword)) {
                flag = 1;
                Log.e("VALIDATOR", "flag 1");
                break;

            } else if (!r_username.equals(u_username)) {
                flag = 2;
                Log.e("VALIDATOR", "flag 2");

            } else if (r_username.equals(u_username) && (!r_password.equals(u_userpassword))) {
                flag = 3;
                Log.e("VALIDATOR", "flag 3");
            }
        }
        return flag;
    }

    public GetItems userdetails(String r_username) {

        String u_userid, u_username, u_useraddress, u_useremail, u_userphone;
        GetItems u_register = new GetItems();
        GetItems temp_u_register = u_register;

        for (int i = 1; i <= index; i++) {
            u_register = u_hashmap.get(i);
            u_userid = u_register.getUserId();
            u_username = u_register.getUserName();
            u_useraddress = u_register.getUserAddress();
            u_useremail = u_register.getUserEmail();
            u_userphone = u_register.getUserPhone();


            if (r_username.equals(u_username)) temp_u_register = u_register;
        }

        return temp_u_register;
    }
}