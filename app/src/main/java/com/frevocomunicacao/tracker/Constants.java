package com.frevocomunicacao.tracker;

/**
 * Created by Vinicius Galvão on 26/01/16.
 */
public class Constants {

    public static final String PREFS_NAME = "tracker_prefs";

    /* USER PREFS */
    public static final String PREFS_KEY_USER_ID = "id";
    public static final String PREFS_KEY_USER_EMPLOYEE_ID = "employee_id";
    public static final String PREFS_KEY_USER_NAME = "name";
    public static final String PREFS_KEY_USER_EMAIL = "email";

    /* API RESPONSE KEYS */
    public static final String RESPONSE_KEY_ERROR = "error";
    public static final String RESPONSE_KEY_MESSAGE = "message";

    /* ENDPOINTS */
    public static final String ENDPOINT_LOGIN = "login";
    public static final String ENDPOINT_IMPORT_DATA = "import_data";
    public static final String ENDPOINT_OCURRENCES = "import_ocurrences";
    public static final String ENDPOINT_SYNC  = "synchronize";

    /* FRAGMENTS VIEW TYPE */
    public static final int FRAGMENT_VIEW_STATUS_VISITS_LIST = 1;
    public static final int FRAGMENT_VIEW_STATUS_MY_VISITS_LIST = 3;


}
