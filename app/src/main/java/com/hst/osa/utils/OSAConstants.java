package com.hst.osa.utils;

public class OSAConstants {

    public static final String BASE_URL = "https://happysanztech.com/";

    public static final String JOINT_URL = "shopping/";

    public static final String BUILD_URL = BASE_URL + JOINT_URL + "mobileapi/";

    public static final String FB_GPLUS_LOGIN = "socialLogin/";

    public static final String MOBILE_VERIFY = "login_mobile/";

    public static final String NUMBER_LOGIN = "login_mobileotp/";

    public static final String EMAIL_LOGIN = "login/";

    public static final String DASHBOARD = "home_page/";

    public static final String SUB_CATEGORY_LIST = "sub_cat_list/";

    public static final String PRODUCT_LIST = "product_list/";

    public static final String PRODUCT_DETAIL = "product_details/";

    public static final String GET_PRODUCT_SIZE = "get_product_size/";

    public static final String GET_PRODUCT_COLOUR = "get_product_color/";

    public static final String GET_PRODUCT_REVIEWS = "product_reviews/";

    public static final String ADD_TO_CART = "product_cart/";

    public static final String SEARCH_PRODUCT = "search_product/";

    public static final String RECENT_SEARCH = "recent_search_list/";

    //      SignIn Params
    public static final String PARAMS_USERNAME = "username";
    public static final String PARAMS_LOGIN_TYPE = "login_type";
    public static final String PARAMS_MOBILE_TYPE = "mobile_type";
    public static final String PARAMS_GCM_KEY = "mob_key";
    public static final String PARAMS_MOBILE_NUMBER = "mobile_number";
    public static final String PARAMS_OTP = "OTP";
    public static final String PARAMS_LOGIN_PORTAL = "login_portal";
    public static final String PARAMS_EMAIL = "email";
    public static final String PARAMS_PASSWORD = "password";
//    mobile_number,OTP,mob_key,mobile_type,login_type(FB/Gplus/Mobile/Email),login_portal(Web/App)

    //      Login mode constants
    public static final int FACEBOOK = 1;
    public static final int NORMAL_SIGNUP = 2;
    public static final int GOOGLE_PLUS = 3;

    //      Login Params
    public static String PARAM_MESSAGE = "msg";

    //      Shared preferences file name
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    //      Shared IMEI No
    public static final String KEY_IMEI = "imei_code";

    //      Profile Params
    public static final String PARAMS_FIRST_NAME = "first_name";
    public static final String PARAMS_LAST_NAME = "last_name";

    //      Alert Dialog Constants
    public static String ALERT_DIALOG_TITLE = "alertDialogTitle";
    public static String ALERT_DIALOG_MESSAGE = "alertDialogMessage";
    public static String ALERT_DIALOG_TAG = "alertDialogTag";
    public static String ALERT_DIALOG_POS_BUTTON = "alert_dialog_pos_button";
    public static String ALERT_DIALOG_NEG_BUTTON = "alert_dialog_neg_button";

    //      SharedPref
    public static final String KEY_LOGIN_MODE = "loginMode";

    //    USER DATA
    public static String KEY_USER_ID = "user_id";
    public static String KEY_NAME = "name";
    public static String KEY_GENDER = "gender";
    public static String KEY_EMAIL = "email";
    public static String KEY_USER_PROFILE_PIC = "profile_pic";
    public static String KEY_MOBILE_NO = "mobile_no";
    public static String KEY_VERSION = "version_code";
    public static String VERSION_VALUE = "1";
    public static String KEY_CAT_ID = "cat_id";
    public static String KEY_SUB_CAT_ID = "sub_cat_id";
    public static String KEY_SEARCH = "search_name";

    //      SharedPref
    public static final String PARAMS_PROD_ID = "product_id";
    public static final String PARAMS_SIZE_ID = "size_id";
    public static final String PARAMS_COMBINED_ID = "product_comb_id";
    public static final String PARAMS_QUANTITY = "quantity";

    public static final String KEY_SOCIAL_NETWORK_URL = "socialNetworkPicUrl";

}
