package com.hst.osa.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.system.Os;

public class PreferenceStorage {

    /*To check welcome screen to launch*/
    public static void setFirstTimeLaunch(Context context, boolean isFirstTime) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(OSAConstants.IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public static boolean isFirstTimeLaunch(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(OSAConstants.IS_FIRST_TIME_LAUNCH, true);
    }
    /*End*/


    /*To save FCM key locally*/
    public static void saveGCM(Context context, String gcmId) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OSAConstants.PARAMS_GCM_KEY, gcmId);
        editor.apply();
    }

    public static String getGCM(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(OSAConstants.PARAMS_GCM_KEY, "");
    }
    /*End*/


    /*To save mobile IMEI number */
    public static void saveIMEI(Context context, String imei) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OSAConstants.KEY_IMEI, imei);
        editor.apply();
    }

    public static String getIMEI(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(OSAConstants.KEY_IMEI, "");
    }
    /*End*/

    /*To store login mode like normal,fb,gplus*/
    public static void saveLoginMode(Context context, int type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(OSAConstants.KEY_LOGIN_MODE, type);
        editor.apply();
    }

    public static int getLoginMode(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Integer mode;
        mode = sharedPreferences.getInt(OSAConstants.KEY_LOGIN_MODE, 0);
        return mode;
    }
    /*End*/
//
//    public static void saveUserType(Context context, String userType) {
//        SharedPreferences sharedPreferences = PreferenceManager
//                .getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(OSAConstants.KEY_USER_TYPE, userType);
//        editor.apply();
//    }
//
//    public static String getUserType(Context context) {
//        SharedPreferences sharedPreferences = PreferenceManager
//                .getDefaultSharedPreferences(context);
//        String userId = sharedPreferences.getString(OSAConstants.KEY_USER_TYPE, "");
//        return userId;
//    }

    // UserId
    public static void saveUserId(Context context, String userId) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OSAConstants.KEY_USER_ID, userId);
        editor.apply();
    }

    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(OSAConstants.KEY_USER_ID, "");
        return userId;
    }

    // UserId
    public static void saveName(Context context, String userId) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OSAConstants.KEY_NAME, userId);
        editor.apply();
    }

    public static String getName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(OSAConstants.KEY_NAME, "");
        return userId;
    }

    // UserId
    public static void saveGender(Context context, String userId) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OSAConstants.KEY_GENDER, userId);
        editor.apply();
    }

    public static String getGender(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(OSAConstants.KEY_GENDER, "");
        return userId;
    }

    // UserId
    public static void saveEmail(Context context, String userId) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OSAConstants.KEY_EMAIL, userId);
        editor.apply();
    }

    public static String getEmail(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(OSAConstants.KEY_EMAIL, "");
        return userId;
    }


    // UserPic
    public static void saveProfilePic(Context context, String userPic) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OSAConstants.KEY_USER_PROFILE_PIC, userPic);
        editor.apply();
    }

    public static String getProfilePic(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(OSAConstants.KEY_USER_PROFILE_PIC, "");
        return userId;
    }
    /*End*/


    public static void saveSocialNetworkProfilePic(Context context, String url) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OSAConstants.KEY_SOCIAL_NETWORK_URL, url);
        editor.commit();

    }
    public static String getSocialNetworkProfileUrl(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String url = sharedPreferences.getString(OSAConstants.KEY_SOCIAL_NETWORK_URL, "");
        return url;

    }


    /*To store mobile number*/
    public static void saveMobileNo(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OSAConstants.KEY_MOBILE_NO, type);
        editor.apply();
    }

    public static String getMobileNo(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(OSAConstants.KEY_MOBILE_NO, "");
        return mobileNo;
    }
    /*End*/

    // LatLng
    public static void saveLat(Context context, String lat) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OSAConstants.KEY_LAT, lat);
        editor.apply();
    }

    public static String getLat(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(OSAConstants.KEY_LAT, "");
        return userId;
    }
    /*End*/

    // LatLng
    public static void saveLng(Context context, String lng) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OSAConstants.KEY_LNG, lng);
        editor.apply();
    }

    public static String getLng(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(OSAConstants.KEY_LNG, "");
        return userId;
    }
    /*End*/

    // LatLng
    public static void saveAddressId(Context context, String addId) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OSAConstants.PARAMS_ADDRESS_ID, addId);
        editor.apply();
    }

    public static String getAddressId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(OSAConstants.PARAMS_ADDRESS_ID, "");
        return userId;
    }
    /*End*/
}
