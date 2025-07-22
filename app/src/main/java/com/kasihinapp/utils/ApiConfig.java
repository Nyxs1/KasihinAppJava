package com.kasihinapp.utils;

public class ApiConfig {
    private static final String BASE_URL = "http://172.16.39.170/KasihinApp/KasihinAppPhp/";

    // Endpoint pakai gateway `index.php?endpoint=...`
    public static final String LOGIN = BASE_URL + "controllers/auth/login.php";
    public static final String REGISTER = BASE_URL + "controllers/auth/register.php";
    public static final String GET_RECIPIENTS = "controllers/user/get_recipients.php";

    public static final String GET_ALL_CAMPAIGNS = BASE_URL + "controllers/campaign/get_all.php";
    public static final String CREATE_CAMPAIGN = BASE_URL + "controllers/campaign/create.php";

    public static final String DONATE = BASE_URL + "controllers/donation/donate.php";
    public static final String DONATE_USER = "controllers/donation/donate_user.php";
    public static final String GET_DONATIONS = BASE_URL + "controllers/donation/history.php";

    public static final String GET_ALL_QUESTS = BASE_URL + "controllers/quest/list.php";
    public static final String SUBMIT_QUEST = BASE_URL + "controllers/quest/submit.php";

    public static final String GET_USER_PROFILE = BASE_URL + "controllers/user/profile.php";
    public static final String UPDATE_USER_PROFILE = BASE_URL + "controllers/user/update.php";
    public static final String GET_USER_DONATION_HISTORY = "controllers/donation/history_user.php";
    public static final String GET_POIN_HISTORY = BASE_URL + "?endpoint=poin_history";
}


