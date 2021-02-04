package com.hst.osa.utils;

/**
 * Created by Admin on 23-09-2017.
 */

public class OSAConstants {

    //URL'S
    //BASE URL
    private static final String BASE_URL = "http://happysanz.in/shopping/";

    //Development Mode
    //development
//    public static final String JOINT_URL = "development/";
    //uat
//    public static final String JOINT_URL = "uat/";
    //live
    public static final String JOINT_URL = "";
//
//    //BUILD URL
    public static final String BUILD_URL = BASE_URL + JOINT_URL + "mobileapi/";


    //GET OTP URL
    public static final String GET_DASHBOARD = "homepage";

    //////    Service Params    ///////

    public static String PARAM_MESSAGE = "msg";
    public static String PARAM_MESSAGE_ENG = "msg_en";
    public static String PARAM_MESSAGE_TAMIL = "msg_ta";

    //     Shared preferences file name
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    //    Shared FCM ID
    public static final String KEY_FCM_ID = "fcm_id";

    //    Shared IMEI No
    public static final String KEY_IMEI = "imei_code";

    //    Shared Phone No
    public static final String KEY_MOBILE_NUMBER = "mobile_no";

    //    USER DATA

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_SELECT_USER_PAGE = "select_user_page";
    public static final String KEY_CONSTITUENCY_ID = "constituency_id";
    public static final String KEY_CONSTITUENCY_NAME = "constituency_name";
    public static final String KEY_CLIENT_API_URL = "client_api_url";
    public static final String KEY_JUST_ID = "id";
    public static final String KEY_PARTY_ID = "party_id";
    public static final String KEY_USER_NAME = "full_name";
    public static final String KEY_USER_GENDER = "gender";
    public static final String KEY_USER_ADDRESS = "address";
    public static final String KEY_USER_PROFILE_PIC = "profile_pic";
    public static final String KEY_USER_MAIL = "email";
    public static final String KEY_USER_MAIL_STATUS = "email_verify_status";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_GRIEVANCE_TYPE = "type";
    public static final String KEY_PINCODE = "pin_code";
    public static final String KEY_WHATSAPP_NO = "whatsapp_no";
    public static final String KEY_FATHER_OR_HUSBAND = "father_husband_name";
    public static final String KEY_RELIGION = "religion_name";
    public static final String KEY_PAGUTHI = "paguthi_name";
    public static final String KEY_WARD = "ward_name";
    public static final String KEY_BOOTH = "booth_name";
    public static final String KEY_BOOTH_ADDRESS = "booth_address";
    public static final String KEY_SERIAL_NO = "serial_no";
    public static final String KEY_AADHAAR = "aadhaar_no";
    public static final String KEY_VOTER_ID = "voter_id_no";
    public static final String KEY_DOB = "dob";

    // Alert Dialog Constants
    public static String ALERT_DIALOG_TITLE = "alertDialogTitle";
    public static String ALERT_DIALOG_MESSAGE = "alertDialogMessage";
    public static String ALERT_DIALOG_TAG = "alertDialogTag";
    public static String ALERT_DIALOG_POS_BUTTON = "alert_dialog_pos_button";
    public static String ALERT_DIALOG_NEG_BUTTON = "alert_dialog_neg_button";

    // Login Parameters
    public static String PHONE_NUMBER = "phone_no";
    public static String REFERRAL_CODE = "referral_code";
    public static String OTP = "otp";
    public static String DEVICE_TOKEN = "device_id";
    public static String MOBILE_TYPE = "mobile_type";
    public static String USER_MASTER_ID = "user_master_id";
    public static String UNIQUE_NUMBER = "unique_number";
    public static String MOBILE_KEY = "mobile_key";
    public static String USER_STATUS = "user_stat";
    public static String LANG_ID = "lang_id";

    // Category Parameters
    public static String MAIN_CATEGORY_ID = "main_cat_id";
    public static String CATEGORY_ID = "category_id";
    public static String SUB_CATEGORY_ID = "sub_cat_id";
    public static String SUB_CAT_ID = "sub_category_id";
    public static String CAT_COUNT = "count";

    // Service Parameters
    public static String SERVICE_ID = "service_id";
    public static String SERVICE_ORDER_ID = "service_order_id";
    public static String CANCEL_ID = "cancel_id";
    public static String CANCEL_COMMENTS = "comments";
    public static String SERVICE_RATE = "service_rate";
    public static String SERVICE_COUNT = "service_count";
    public static String SERVICE_DATE = "service_date";
    public static String SERVICE_STATUS = "sat";
    public static String WALLET_STATUS = "wall";
    public static String CART_STATUS = "car_sat";
    public static String FEEDBAC_ID = "feedback_id";
    public static String FEEDBAC_tEXT = "feedback_text";

    // Service Parameters
    public static String CART_ID = "cart_id";
    public static String SERVICE_PERSON_ID = "person_id";

    // Order Parameters
    public static String ORDER_ID = "order_id";
    public static String TIME_INTERVAL = "display_minute";

    // Service Parameters
    public static String SEARCH_STATUS = "search_status";
    public static String SEARCH_TEXT = "service_txt";
    public static String SEARCH_TEXT_TA = "search_txt_ta";

    // Booking Parameters
    public static String CONTACT_PERSON = "contact_person_name";
    public static String CONTACT_PERSON_NUMBER = "contact_person_number";
    public static String SERVICE_LATLNG = "service_latlon";
    public static String SERVICE_LOCATION = "service_location";
    public static String SERVICE_ADDRESS = "service_address";
    public static String ORDER_DATE = "order_date";
    public static String ORDER_NOTES = "order_notes";
    public static String ORDER_TIMESLOT = "order_timeslot_id";
    public static final String PARAMS_AMOUNT = "amount";


    // Advance Payment
    public static String ADVANCE_AMOUNT = "advance_amount";
    public static String ADVANCE_STATUS = "advance_payment_status";
    public static String COUPON_ID = "coupon_id";
    public static String COUPON_TEXT = "coupon_text";

    // Advance Payment
    public static String KEY_RATINGS = "ratings";
    public static String KEY_COMMENTS = "reviews";
    public static String KEY_APP_VERSION = "version_code";
    public static String KEY_APP_VERSION_VALUE = "1";

}
