package br.uern.ges.med.sim2ped.utils;

public class Constant {

    public static final int TIME_ALARM_LOST = 2; // minutes early

	public static final String BUNDLE_LAST_POSITION = "LAST_POSITION";
	public static final String BUNDLE_NOTIFICATION_ID = "NOTIFICATION_ID";
	public static final String BUNDLE_TIP_ID = "TIP_ID";
	public static final String BUNDLE_CARE_ID = "CARE_ID";
    public static final String BUNDLE_ROUTINE_ID = "ROUTINE_ID";
    public static final String BUNDLE_STATISTIC_ID= "STATISTIC_ID";
	
	/* Prefernces */
	public static final String PREF_LAYOUT_TIPS = "PREF_LAYOUT_TIPS";
	public static final String PREF_FONT_SIZE_TIPS = "PREF_FONT_SIZE_TIPS";
    public static final String PREF_EARLY_MINUTES_FOR_ALARM = "PREF_EARLY_MINUTES_FOR_ALARM";
    public static final String PREF_KEEP_LOGGED = "PREF_KEEP_LOGGED";
    public static final String PREF_USER_CODE_LOGGED = "PREF_USER_CODE_LOGGED";
    public static final String PREF_NUMBER_OF_TIPS_DISPLAYED = "PREF_NUMBER_OF_TIPS_DISPLAYED";
	
	/* Notifications */
	public static final int NOTIF_ID_SYNC = 300514;
	public static final int NOTIF_ID_NEW_TIP = 110614;
	public static final int NOTIF_ID_NEW_CARE = 30714;

	/* LOGS */
	public static final String TAG_LOG = "SIM2PeD";
	
	/* WebServices */
	public static final String WEBSERVICE_LOGIN = "1";
	public static final String WEBSERVICE_PASSWORD = "123456";

    public static final String JSON_OBJECT_TAG_TIP = "TIP";
    public static final String JSON_OBJECT_TAG_CARE = "CARE";
    public static final String JSON_OBJECT_TAG_ROUTINE = "ROUTINE";
    public static final String JSON_OBJECT_TAG_CONTEXT = "CONTEXT";
    public static final String JSON_OBJECT_TAG_CARES_IN_CONTEXT = "CARES_IN_CONTEXT";
    public static final String JSON_OBJECT_TAG_USER = "USER";
    public static final String JSON_OBJECT_TAG_STATISTICS = "STATISTICS";
    public static final String JSON_OBJECT_TAG_HISTORY = "HISTORY";

    public static final String JSON_ACTION_INSERT = "INSERT";
    public static final String JSON_ACTION_UPDATE = "UPDATE";
    public static final String JSON_ACTION_DELETE = "DELETE";

    public static final String JSON_MESSAGE_VALID_LOGIN = "VALID_LOGIN";
    public static final String JSON_MESSAGE_STATUS = "status";
    public static final String JSON_MESSAGE_ID_HISTORY_DELETE_RECEIVED = "id_received";
    public static final String JSON_MESSAGE_ID_STATISTICAL_USAGE_UPDATE_RECEIVED = "id_received";

    public static final String PARAM_WS_VERIFY_LOGIN_USER_CODE = "user_code";
    public static final String PARAM_WS_VERIFY_LOGIN_USER_PASSWORD = "password";
    public static final String PARAM_WS_USER_CODE = "user_code";
    public static final String PARAM_WS_GCM_TOKEN = "gcm_token";


	public static final String WEBSERVICE_URL = "http://med.ges.uern.br/med/api/sim2ped/";
    //public static final String WEBSERVICE_URL = "http://10.0.0.100/med/api/sim2ped/";
    //public static final String WEBSERVICE_URL = "http://10.0.0.111/med/api/avaliacaodospes/";
    public static final String WEBSERVICE_RESPONSE_FORMAT = "format/json";

    //public static final String WEBSERVICE_RELATIVE_URL_REGISTER_GCM = "register_gcm/";
    public static final String WEBSERVICE_RELATIVE_URL_LOGIN = "login_user/";
    public static final String WEBSERVICE_RELATIVE_URL_DOWNLOAD_DATES = "download_data/";
    public static final String WEBSERVICE_RELATIVE_URL_POST_HISTORY = "post_history/";
    public static final String WEBSERVICE_RELATIVE_URL_POST_STATISTICS = "post_statistics/";
	
	/* Google Cloud Messagind */
	public static final String GCM_SENDER_ID = "488043889128";
	// Key do celular: APA91bG42YUgyrEH4i9rLplMDpEShD4sIJbblTcY65prOEzAAY84ZwuZRUN_1_OsdCTXoKIL0N2uEH-NIdeySGSY3cNXT_cqvoCLX6vBcsBoYnQdVMIXbLNQKIv10cB4MVJ77YuWbryJpGJHZzBD_KOy2swUImnAnqvvbNTtZPXR8B8OMYM2pYY
	
	/* DB */
	public static final String DB_NAME = "sim2ped.db";
	public static final int DB_VERSION = 1;

	public static final String[] CREATE_SQL = new String[] {
        "CREATE TABLE IF NOT EXISTS " + Constant.DB_TABLE_CARES + "(" +
            Constant.DB_TABLE_CARES_ROW_ID + " INTEGER NOT NULL, " +
            Constant.DB_TABLE_CARES_ROW_TITLE_CARE + " TEXT NOT NULL, " +
            Constant.DB_TABLE_CARES_ROW_CARE_QUESTION + " TEXT NOT NULL, " +
            Constant.DB_TABLE_CARES_ROW_CREATED_AT + " TIMESTAMP NOT NULL, " +
            Constant.DB_TABLE_CARES_ROW_MODIFIED_AT + " TIMESTAMP NOT NULL, " +
            Constant.DB_TABLE_CARES_ROW_INSERTED_AT + " TIMESTAMP NOT NULL DEFAULT current_timestamp," +
            "PRIMARY KEY ("+ Constant.DB_TABLE_CARES_ROW_ID +" ASC)" +
        ");",

        "CREATE TABLE " + Constant.DB_TABLE_CARES_IN_CONTEXT + "(" +
            Constant.DB_TABLE_CARES_IN_CONTEXT_ROW_CONTEXT_ID + " INTEGER NOT NULL," +
            Constant.DB_TABLE_CARES_IN_CONTEXT_ROW_CARE_ID + " INTEGER NOT NULL," +
            "FOREIGN KEY (" + Constant.DB_TABLE_CARES_IN_CONTEXT_ROW_CONTEXT_ID + ") REFERENCES " + Constant.DB_TABLE_CONTEXTS + "(" + Constant.DB_TABLE_CONTEXTS_ROW_ID + ")," +
            "FOREIGN KEY (" + Constant.DB_TABLE_CARES_IN_CONTEXT_ROW_CARE_ID + ") REFERENCES " + Constant.DB_TABLE_CARES + "(" + Constant.DB_TABLE_CARES_ROW_ID + ")" +
        " );",

        "CREATE TABLE IF NOT EXISTS " + Constant.DB_TABLE_CONTEXTS + "(" +
            Constant.DB_TABLE_CONTEXTS_ROW_ID + " INTEGER NOT NULL, " +
            Constant.DB_TABLE_CONTEXTS_ROW_NAME + " TEXT NOT NULL," +
            "PRIMARY KEY ("+ Constant.DB_TABLE_CONTEXTS_ROW_ID +" ASC)" +
        ");",

        "CREATE TABLE IF NOT EXISTS " + Constant.DB_TABLE_USER + "(" +
            Constant.DB_TABLE_USER_ROW_ID + " INTEGER NOT NULL," +
            Constant.DB_TABLE_USER_ROW_NAME + " TEXT NOT NULL," +
            Constant.DB_TABLE_USER_ROW_AVATAR_PATH + " TEXT," +
            "PRIMARY KEY ("+ Constant.DB_TABLE_USER_ROW_ID +" ASC)" +
         ");",

        "CREATE TABLE IF NOT EXISTS " + Constant.DB_TABLE_ROUTINE + "(" +
            Constant.DB_TABLE_ROUTINE_ROW_ID + " INTEGER PRIMARY KEY NOT NULL," +
            //Constant.DB_TABLE_ROUTINE_ROW_WEEK_DAY + " INTEGER NOT NULL," +
            Constant.DB_TABLE_ROUTINE_ROW_TIME + " TIMESTAMP NOT NULL, " +
            Constant.DB_TABLE_ROUTINE_ROW_CONTEXT_ID + " INTEGER NOT NULL, " +
            Constant.DB_TABLE_ROUTINE_ROW_USER_ID + " INTEGER NOT NULL,"+
            "FOREIGN KEY (" + Constant.DB_TABLE_ROUTINE_ROW_CONTEXT_ID + ") REFERENCES " + Constant.DB_TABLE_CONTEXTS + " (" + Constant.DB_TABLE_CONTEXTS_ROW_ID + ") ON DELETE CASCADE ON UPDATE CASCADE," +
            "FOREIGN KEY (" + Constant.DB_TABLE_ROUTINE_ROW_USER_ID + ") REFERENCES " + Constant.DB_TABLE_USER + " (" + Constant.DB_TABLE_USER_ROW_ID + ") ON DELETE CASCADE ON UPDATE CASCADE" +
        ");",

        "CREATE TABLE IF NOT EXISTS " + Constant.DB_TABLE_TIPS + "(" +
            Constant.DB_TABLE_TIPS_ROW_ID + " INTEGER NOT NULL,"  /*  autoincrement */ +
            Constant.DB_TABLE_TIPS_ROW_CARE_ID + " INTEGER NOT NULL," +
            Constant.DB_TABLE_TIPS_ROW_TEXT + " TEXT NOT NULL," +
            Constant.DB_TABLE_TIPS_ROW_PRIORITY + " INTEGER NOT NULL, " +
            Constant.DB_TABLE_TIPS_ROW_CREATED_AT + " TIMESTAMP NOT NULL, " +
            Constant.DB_TABLE_TIPS_ROW_MODIFIED_AT + " TIMESTAMP NOT NULL, " +
            Constant.DB_TABLE_TIPS_ROW_INSERTED_AT + " TIMESTAMP NOT NULL DEFAULT current_timestamp," +
            "PRIMARY KEY ("+ Constant.DB_TABLE_TIPS_ROW_ID +" ASC)," +
            "FOREIGN KEY (" + Constant.DB_TABLE_TIPS_ROW_CARE_ID + ") REFERENCES " + Constant.DB_TABLE_CARES + " ("+ Constant.DB_TABLE_CARES_ROW_ID + ") ON DELETE CASCADE ON UPDATE CASCADE"+
        "); ",

        "CREATE TABLE IF NOT EXISTS " + Constant.DB_TABLE_HISTORY_RESPONSES + "(" +
            Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            Constant.DB_TABLE_HISTORY_RESPONSES_ROW_CONTEXT_ID + " INTEGER NOT NULL," +
            Constant.DB_TABLE_HISTORY_RESPONSES_ROW_CARE_ID + " INTEGER NOT NULL," +
            Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ROUTINE_ID + " INTEGER NOT NULL, " +
            Constant.DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE_HOUR + " TIMESTAMP NOT NULL DEFAULT current_timestamp," +
            Constant.DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE + " TEXT(1) NOT NULL," +
            Constant.DB_TABLE_HISTORY_RESPONSES_ROW_SENT_SERVER + " INTEGER NOT NULL DEFAULT 0," +
            "FOREIGN KEY (" + Constant.DB_TABLE_HISTORY_RESPONSES_ROW_CONTEXT_ID + ") REFERENCES " + Constant.DB_TABLE_CONTEXTS + " (" + Constant.DB_TABLE_CONTEXTS_ROW_ID + ")," +
            "FOREIGN KEY (" + Constant.DB_TABLE_HISTORY_RESPONSES_ROW_CARE_ID + ") REFERENCES " + Constant.DB_TABLE_CARES + " (" + Constant.DB_TABLE_CARES_ROW_ID + ")," +
            "FOREIGN KEY (" + Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ROUTINE_ID + ") REFERENCES " + Constant.DB_TABLE_ROUTINE + " (" + Constant.DB_TABLE_ROUTINE_ROW_ID + ")" +
         ");",

        "CREATE TABLE IF NOT EXISTS " + Constant.DB_TABLE_STATISTICAL_USAGE + "(" +
            Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ENTRY_TIME + " TIMESTAMP NOT NULL DEFAULT current_timestamp," +
            Constant.DB_TABLE_STATISTICAL_USAGE_ROW_USER_ID + " INTEGER NOT NULL,"+
            Constant.DB_TABLE_STATISTICAL_USAGE_ROW_SENT_SERVER + " INTEGER NOT NULL DEFAULT 0, "+
            "FOREIGN KEY (" + Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ID + ") REFERENCES " + Constant.DB_TABLE_USER + " (" + Constant.DB_TABLE_USER_ROW_ID + ") ON DELETE CASCADE ON UPDATE CASCADE" +
        ");",

        "CREATE TABLE IF NOT EXISTS " + Constant.DB_TABLE_STATISTICAL_READING_TIPS + " (" +
            Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_STATISTICAL_ID + " INTEGER NOT NULL, " +
            Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_TIP_ID + " INTEGER NOT NULL, "+
            Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_HOUR_READING + " TIMESTAMP NOT NULL DEFAULT current_timestamp," +
            Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_USER_ID + " INTEGER NOT NULL,"+
            "FOREIGN KEY (" + Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_STATISTICAL_ID + ") REFERENCES " + Constant.DB_TABLE_STATISTICAL_USAGE + " (" + Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ID + ") ON DELETE CASCADE ON UPDATE CASCADE," +
            "FOREIGN KEY (" + Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_TIP_ID + ") REFERENCES " + Constant.DB_TABLE_TIPS + " (" + Constant.DB_TABLE_TIPS_ROW_ID + ") ON DELETE CASCADE ON UPDATE CASCADE," +
            "FOREIGN KEY (" + Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_USER_ID + ") REFERENCES " + Constant.DB_TABLE_STATISTICAL_USAGE + " (" + Constant.DB_TABLE_STATISTICAL_USAGE_ROW_USER_ID + ") ON DELETE CASCADE ON UPDATE CASCADE" +
        ");",

        "CREATE TABLE IF NOT EXISTS " + Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE + " (" +
            Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_STATISTICAL_ID + " INTEGER NOT NULL, " +
            Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_HOUR_VIEW + " TIMESTAMP NOT NULL DEFAULT current_timestamp, "+
            Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_USER_ID + " INTEGER NOT NULL,"+
            "FOREIGN KEY (" + Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_STATISTICAL_ID + ") REFERENCES " + Constant.DB_TABLE_STATISTICAL_USAGE + " (" + Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ID + ") ON DELETE CASCADE ON UPDATE CASCADE," +
            "FOREIGN KEY (" + Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_USER_ID + ") REFERENCES " + Constant.DB_TABLE_STATISTICAL_USAGE + " (" + Constant.DB_TABLE_STATISTICAL_USAGE_ROW_USER_ID + ") ON DELETE CASCADE ON UPDATE CASCADE" +
        ");"
    };

	/* TABLE CARES */
	public static final String DB_TABLE_CARES = "cares";
	public static final String DB_TABLE_CARES_ROW_ID = "id";
	public static final String DB_TABLE_CARES_ROW_TITLE_CARE =  "titleCare";
	public static final String DB_TABLE_CARES_ROW_CARE_QUESTION = "careQuestion";
    public static final String DB_TABLE_CARES_ROW_CREATED_AT = "createdAt";
    public static final String DB_TABLE_CARES_ROW_MODIFIED_AT = "modifiedAt";
    public static final String DB_TABLE_CARES_ROW_INSERTED_AT = "insertedAt";

    /* TABLE CARES IN CONTEXT */
    public static final String DB_TABLE_CARES_IN_CONTEXT = "cares_in_context";
    public static final String DB_TABLE_CARES_IN_CONTEXT_ROW_CONTEXT_ID = "contextId";
    public static final String DB_TABLE_CARES_IN_CONTEXT_ROW_CARE_ID = "careId";

    /* TABLE CONTEXT */
    public static final String DB_TABLE_CONTEXTS = "contexts";
    public static final String DB_TABLE_CONTEXTS_ROW_ID = "id";
    public static final String DB_TABLE_CONTEXTS_ROW_NAME = "name";

    /* TABLE USER */
    public static final String DB_TABLE_USER = "user";
    public static final String DB_TABLE_USER_ROW_ID = "id";
    public static final String DB_TABLE_USER_ROW_NAME = "name";
    public static final String DB_TABLE_USER_ROW_AVATAR_PATH = "avatarPath";

    /* TABLE ROUTINE */
    public static final String DB_TABLE_ROUTINE = "routine";
    public static final String DB_TABLE_ROUTINE_ROW_ID = "id";
    //public static final String DB_TABLE_ROUTINE_ROW_WEEK_DAY = "weekDay";
    public static final String DB_TABLE_ROUTINE_ROW_TIME = "time";
    public static final String DB_TABLE_ROUTINE_ROW_CONTEXT_ID = "contextId";
    public static final String DB_TABLE_ROUTINE_ROW_USER_ID = "userId";

    /* TABLE TIPS */
    public static final String DB_TABLE_TIPS = "tips";
    public static final String DB_TABLE_TIPS_ROW_ID = "id";
    public static final String DB_TABLE_TIPS_ROW_CARE_ID = "careId";
    public static final String DB_TABLE_TIPS_ROW_TEXT = "text";
    public static final String DB_TABLE_TIPS_ROW_PRIORITY = "priority";
    public static final String DB_TABLE_TIPS_ROW_CREATED_AT = "createdAt";
    public static final String DB_TABLE_TIPS_ROW_MODIFIED_AT = "modifiedAt";
    public static final String DB_TABLE_TIPS_ROW_INSERTED_AT = "insertedAt";

    /* TABLE HISTORY RESPONSES */
    public static final String DB_TABLE_HISTORY_RESPONSES = "history_responses";
    public static final String DB_TABLE_HISTORY_RESPONSES_ROW_ID = "id";
    public static final String DB_TABLE_HISTORY_RESPONSES_ROW_CONTEXT_ID = "contextId";
    public static final String DB_TABLE_HISTORY_RESPONSES_ROW_CARE_ID = "careId";
    public static final String DB_TABLE_HISTORY_RESPONSES_ROW_ROUTINE_ID = "rountineId";
    public static final String DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE_HOUR = "responseHour";
    public static final String DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE = "response";
    public static final String DB_TABLE_HISTORY_RESPONSES_ROW_SENT_SERVER = "sentServer";

    /* TABLE STATISTICAL USAGE */
    public static final String DB_TABLE_STATISTICAL_USAGE = "statistical_usage";
    public static final String DB_TABLE_STATISTICAL_USAGE_ROW_ID = "id";
    public static final String DB_TABLE_STATISTICAL_USAGE_ROW_ENTRY_TIME = "entryTime";
    public static final String DB_TABLE_STATISTICAL_USAGE_ROW_USER_ID = "userId";
    public static final String DB_TABLE_STATISTICAL_USAGE_ROW_SENT_SERVER = "sentServer";

    /* STATISTICAL TIPS READ */
    public static final String DB_TABLE_STATISTICAL_READING_TIPS = "statistical_reading_tips";
    public static final String DB_TABLE_STATISTICAL_READING_TIPS_ROW_ID = "id";
    public static final String DB_TABLE_STATISTICAL_READING_TIPS_ROW_STATISTICAL_ID = "statisticalUsageId";
    public static final String DB_TABLE_STATISTICAL_READING_TIPS_ROW_TIP_ID = "tipId";
    public static final String DB_TABLE_STATISTICAL_READING_TIPS_ROW_USER_ID = "userId";
    public static final String DB_TABLE_STATISTICAL_READING_TIPS_ROW_HOUR_READING = "hourReading";

    /* STATISTICAL PROCEDURES VIEW */
    public static final String DB_TABLE_STATISTICAL_VIEW_PROCEDURE = "statistical_view_procedures";
    public static final String DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_ID = "id";
    public static final String DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_STATISTICAL_ID = "statisticalUsageId";
    public static final String DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_USER_ID = "userId";
    public static final String DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_HOUR_VIEW = "hourView";

    public enum FragmentsMenu {
        TIPS(0), PROCEDURES(1), ABOUT(2), SENT_HISTORY(3), SETTINGS(4);

        public final int value;

        FragmentsMenu(int valueOption) {
            value = valueOption;
        }

    }
}