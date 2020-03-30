package com.example.dell.smartpos;

public class Constants {

    private int environment = 0;
    // public final static String current_VersionCode = "3.0.10";
    //public final static String current_VersionCode = "2.0.0";
	public final static String current_VersionCode = BuildConfig.VERSION_NAME;

	public static int LIST_ACTIVITY = 0;
    public static int CHILD_TAB = 0;
    public static int HIS_CHILD_TAB = 0;
    public static int VOID_CHILD_TAB = 0;
    public static boolean finishCheck = false;
    public int CurrCode;

    public static int BLUETOOTH_SEARCH_TIME = 4500;
    public static String marketURI = "market://details?id=com.asiapay.mposplus";
    //	 public static long SESSION_EXPIRY = 15000; //10 seconds
    //	 public static long SESSION_EXPIRY = 50000; //50 seconds
    //	 public static long SESSION_EXPIRY = 1800000; //30 minutes
    public static long SESSION_EXPIRY = 3600000; //1 hr
    //   public static long SESSION_EXPIRY = 5000;//5s
    //	 public static String merName;
    //	 public static String merId;
    //	 public static String currCode;
//    public static String CheckLogout = "CheckLogout";
//    public static String IsAutoLogout = "IsAutoLogout";


    public static String searchString = null;
    public static int ctrSearch = 0;

    public static String curReaderMode = "";

    public static int tabSuccess = 0;

    // STRINGS
    public static final String MERID = "merchantId";
    public static final String SUCCESS_CODE = "successCode";
    public static final String AMOUNT = "Amount";
    public static final String SURCHARGE = "Surcharge";
    public static final String MERREQUESTAMT = "MerRequestAmt";
    public static final String CURRCODE = "CurrCode";
    public static final String CURRENCY = "Currency";
    public static final String MERCHANT_REF = "merRef";
    public static final String RRN = "RRN";
    public static final String APPROVE_CODE = "approveCode";
    public static final String REMARK = "remark";
    public static final String PAYTYPE = "payType";
    public static final String CARDNO = "cardNo";
    public static final String CARDHOLDER = "cardHolder";
    public static final String SECCODE = "cvv";
    public static final String EXPYEAR = "expYear";
    public static final String EXPMONTH = "expMonth";
    public static final String EXPDATE = "expDate";
    public static final String PAYMETHODLIST = "payMethodList";
    public static final String PAYBANKIDLIST = "payBankIdList";
    public static final String PAYMENTOPTION = "paymentOption";
    public static final String PAYBANKID = "bank";
    public static final String PAYBANKNAME = "bankname";
    public static final String PAYMETHOD = "payMethod";
    public static final String PAYREF = "payRef";
    public static final String BANK_REFNO = "bankRefNo";
    public static final String AUTHID = "authID";
    public static final String TXTIME = "txTime";
    public static final String SECHASH = "secHash";
    public static final String OPERATORID = "operatorId";
    public static final String PRE_AMOUNT = "pre_Amount";
    public static final String PRE_MDR_AMOUNT = "pre_mdr_Amount";
    public static final String PRE_MDR = "pre_mdr";
    public static final String PRE_SURCHARGE = "pre_surcharge";
    public static final String INVOICE_NO = "invoiceNo";


	public static final String FIRST_DATA = "First-Data";


	public static final String RAWDATA = "rawData";
    public static final String ENCRYPTMODE = "encryptMode";
    public static final String ENCRYPTALGO = "encryptAlgo";
    public static final String SECON = "secOn";
    public static final String TYPE = "type";

    public static final String PAYGATE = "payGate";
    public static final String MERNAME = "merName";
    public static final String ERRCODE = "errcode";
    public static final String USERID = "userID";
    public static final String FILE_NAME = "fileName";


    public static final String DATE = "date";
    public static final String STATUS = "status";
    public static final String SETTLE = "settle";
    public static final String ACTION = "action";
    public static final String ERRMSG = "errmsg";
    public static final String RESULT = "result";

    public static final String CURR = "currency";

    // DATABASE
    public static String DB_TABLE_ID = "Settings";
    public static String DB_MERCHANT_ID = "merID";
    public static String DB_DEVICE_ID = "deviceID";
    public static String DB_DEVICE_SERIALNO = "deviceSerialNo";
    public static String DB_API_ID = "apiID";
    public static String DB_API_PW = "apipw";
    public static String DB_USER_ID = "userID";
    public static String DB_SECURITY_HASH = "securityhash";
    public static String DB_PMETHOD = "securityhash";
    public static String DB_PMETHOD_REFUND = "pMethod";
    public static String DB_LOGO_PATH = "pMethodRefund";
    public static String DB_MER_CLASS = "merClass";
    public static String DB_ENABLE_SMS = "enableSMS";
    public static String DB_CURRCODE = "currCode";
    public static String DB_MER_NAME = "merName";
    public static String DB_SUR_CAL = "SurchargeCalculation";
    //--Edited 25/07/18 by KJ--//
    public static String DB_ALIPAY_TIMEOUT = "alipayTimeout";
    public static String DB_ALIPAYHK_TIMEOUT = "alipayHKTimeout";
    public static String DB_WECHAT_TIMEOUT = "wechatTimeout";
    public static String DB_WECHATHK_TIMEOUT = "wechatHKTimeout";
    public static String DB_UNIONPAY_TIMEOUT = "unionTimeout";
    public static String DB_OEPAY_TIMEOUT = "oepayTimeout";
    public static String DB_BOOST_TIMEOUT = "boostTimeout";
    public static String DB_CARDPAYMENT_TIMEOUT = "cardTimeout";
    public static String DB_LAST_LOGIN = "lastlogin";
    //--Edited 25/07/18 by KJ--//


    /** Share Preferences **/

    // PREF NAME
    public static String SETTINGS = "Settings";
    public static String USER_DATA = "User Data";
    public static String SESSION_DATA = "Session Data";
    public static String COUNTER = "Counter";
    public static String FIRST_TIME = "First Time";
    public static String BBLQR_TRACENO_COUNTER = "BBL QR TraceNo Counter";
    public static String BBLQR_BATCHNO_COUNTER = "BBL QR BatchNo Counter";
    public static String BATCH_COUNTER = "Batch Counter";
    public static String QRTRACENO_COUNTER = "QR TraceNo Counter";
    public static String CARDTRACENO_COUNTER = "Card TraceNo Counter";

    // PREF USER_DATA LABEL
    public static String pref_MerName = "merName";
    public static String pref_MerID = "merId";
    public static String pref_UserID = "userId";
    public static String pref_CurrCode = "Currcode";
    public static String pref_pMethod = "payMethod";
    public static String pref_pBankId = "payBankId";
    public static String pref_pBankIdArray = "payBankIdArray";

    public static String pref_cardPayBankId = "cardPayBankId";
    public static String pref_Rate = "rate";
    public static String pref_Fixed = "fixed";
    public static String pref_hideSurcharge = "hideSurcharge";
    public static String pref_Partnerlogo = "partnerlogo";
    public static String pref_pMethodRefund = "payMethodRefund";
    public static String pref_visaOR = "visaOnlineRefund";
    public static String pref_masterOR = "masterOnlineRefund";
    public static String pref_amexOR = "amexOnlineRefund";
    public static String pref_jcbOR = "jcbOnlineRefund";
    public static String pref_loginId = "loginId";
    public static String pref_loginPassword = "loginPassword";
    public static String pref_TerminalIdArray = "terminalIdArray";
    public static String pref_TerminalId = "terminalId";
    public static String pref_LoggedIn = "LoggedIn";
    public static String pref_Address_1 = "address1";
    public static String pref_Address_2 = "address2";
    public static String pref_Address_3 = "address3";
    public static String pref_BankMerIdArray = "bankMerIdArray";


    //PREF SETTINGS LABEL
    public static String pref_Model = "Model";
    public static String pref_Lang = "Language";
    public static String pref_PayGate = "PayGate";
    public static String pref_MerRef = "MerchantRef";
    public static String pref_PrintMode = "PrintingMode";
    public static String pref_SecCode = "SecurityCode";
    public static String pref_otherPaymentOption = "OtherPaymentOption";
    public static String pref_surcharge_calculation = "SurchargeCalculation";
    public static String pref_CtyCode = "CountryCode";
    public static String pref_SecHash = "SecurityHash";
    public static String pref_ApiId = "MerApiId";
    public static String pref_ApiPw = "MerApiPassword";
    public static String pref_SupEmail = "SupportEmail";
    public static String pref_order_control = "EmailControl";
    public static String pref_same_day_refund = "SameDayRefund";
    public static String pref_refund_pw_protection = "RefundPasswordProtection";
    public static String pref_void_pw_protection = "VoidPasswordProtection"; // to be removed
    public static String pref_transaction_checking = "TransactionChecking";
    //Kennedy 2018/10/29
    public static String default_password_control = "Off";
    public static String pref_password_refundQR = "RefundQRPassword";
    public static String pref_control_refundQR = "RefundQRControl";
    public static String pref_password_refundCard = "RefundCardPassword";
    public static String pref_control_refundCard = "RefundCardControl";
    public static String pref_password_settlementCard = "SettlementCardPassword";
    public static String pref_control_settlementCard = "SettlementCardControl";
    public static String pref_password_voidCard = "VoidCardPassword";
    public static String pref_control_voidCard = "VoidCardControl";
    public static String pref_control_enableContactless = "EnableContactlessControl";

    // PREF SETTINGS LABEL: PRINTER
    public static String pref_printer_address = "printeraddress";
    public static String pref_printer_name = "printername";

    // PREF SETTINGS LABEL: MODULE PASSWORD
    public static String pref_Mer_SettingPW = "Admin8188";
    public static String admin_PW = "$7777777#";
    public static String adminAction_PW = "123";
    public static String pref_setting_mode = "mode";
    public static String pref_last_login = "26 April 2018 10:53 AM";

    // PREF SETTINGS LABEL: TIMEOUT
    public static String pref_card_timeout = "cardTO";
    public static String pref_alipay_timeout = "alipayTO";
    public static String pref_alipayhk_timeout = "alipayHKTO";
    public static String pref_wechat_timeout = "wechatTO";
    public static String pref_wechathk_timeout = "wechatHKTO";
    public static String pref_boost_timeout = "boostTO";
    public static String pref_oepay_timeout = "oepayTO";
    public static String pref_promptpay_timeout = "promptpayTO";
    public static String pref_unionpay_timeout = "unionpayTO";
    public static String pref_grabpay_timeout = "grabpayTO";
    public static String pref_gcash_timeout = "gcashTO";

    // PREF SETTINGS LABEL: LANGUAGE
    public static String LANG_ENG = "English";
    public static String LANG_TRAD = "Tran. Chinese";
    public static String LANG_SIMP = "Simp. Chinese";
    public static String LANG_THAI = "Thai";

    // PREF SETTINGS FIRSTTIME
    public static String pref_firstTime = "firstTime";

    //READER MODEL
    public static String MODEL_NONE = "None";
    public static String MODEL_PROVA = "Prova-Tech Model MS22x (MSR)";
    public static String MODEL_IPOS = "Sunyard Model IPOS (MSR)";
    public static String MODEL_ACR31 = "ACS Model ACR31 (MSR)";
    //	 public static String MODEL_BRAND3= "(Brand)(Model3)(Type)";
    public static String MODEL_MINT = "Mint Reader";
    public static String MODEL_Vi218 = "BBPOS Chipper(MSR & EMV)";

    // COUNTRY CODE
    public static String countryCode_HK = "852"; //852
    public static String countryCode_PH = "63";
    public static String countryCode_TH = "66";

    // PREF SETTINGS LABEL: MERCHANT REFERENCE NUMBER
    public static String MERREF_AUTO = "Auto";
    public static String MERREF_MANUAL = "Manual";

    // RECEIPT PRINTING MODE
    public static Boolean PRINT_UNSUCCESS_RECEIPT = false;
    public static String PRINT_MODE1 = "Merchant and Consumer (Auto)";
    public static String PRINT_MODE2 = "Merchant (Auto) and Consumer (Optional)";
    public static String PRINT_MODE3 = "Merchant only (Auto)";


    public static String pg_paydollar = "Paydollar";
    public static String pg_pesopay = "Pesopay";
    public static String pg_siampay = "Siampay";


    //DEFAULT VALUES
    public static String default_model = MODEL_NONE;
    public static String default_language = LANG_ENG;
    public static String default_paygate = pg_paydollar;
    public static String default_securitycode = "ON";
    public static String default_otherPaymentOption = "OFF";
    public static String default_surcharge_calculation = "OFF";
    public static String default_countrycode = "";
    public static String default_securityhash = "ON";
    public static String default_merchantref = MERREF_AUTO;
    public static String default_printmode = PRINT_MODE1;
    public static String default_supportEmail = "support_my@asiapay.com";
    public static String default_order_control = "OFF";
    public static String default_same_day_refund = "OFF";
    public static String default_refund_pw_protection = "OFF";
    public static String default_void_pw_protection = "OFF";
    public static String default_settingPW = "Admin8188";
    public static String default_settingPW_2 = "88888888";

    public static String default_setting_mode = "mode3";
    public static String default_payment_timeout = "3";

    public static int default_transaction_checking = 1;
    public static int max_transaction_checking = 30;

    //MDR
    public static String mer_mdr = "mer_MDR";


    // Settings Merchant
    public static String default_control_samedayRefundQR = "OFF";
    public static String pref_control_samedayRefundQR = "SamedayRefundQRControl";

    // ReceiptPrintingMode
    public static String pref_control_printUnsuccessfulReceiptQR = "PrintUnsuccessfulReceiptQR";
    public static String pref_control_printUnsuccessfulReceiptCard = "PrintUnsuccessfulReceiptCard";

    // Password Protection
    public static String default_control_password_protection = "OFF";
    public static String default_protection_password = "";
    public static String pref_control_card_refund_password = "cardRefundPwControl";
    public static String pref_control_card_settlement_password = "cardSettlementPwControl";
    public static String pref_control_card_void_password = "cardVoidPwControl";
    public static String pref_control_qr_refund_password = "QRRefundPwControl";
    public static String pref_control_qr_void_password = "QRVoidPwControl";
    public static String pref_password_card_refund = "cardRefundPassword";
    public static String pref_password_card_settlement = "cardSettlementPassword";
    public static String pref_password_card_void = "cardVoidPassword";
    public static String pref_password_qr_refund = "QRRefundPassword";
    public static String pref_password_qr_void = "QRVoidPassword";

    // BinRange
    public static String visa_bin_range = "visa_bin_range";
    public static String mc_bin_range = "mc_bin_range";
    public static String cup_bin_range = "cup_bin_range";
    public static String amex_bin_range = "amex_bin_range";
    public static String jcb_bin_range = "jcb_bin_range";
    public static String diners_bin_range = "diners_bin_range";
    public static String default_visa_bin_range = "400000_499999";
    public static String default_mc_bin_range = "510000_559999-R-222100_272099";
    public static String default_cup_bin_range = "620000_629999-R-000000_999999";
    public static String default_amex_bin_range = "340000_349999-R-370000_379999";
    public static String default_jcb_bin_range = "352800_358999";
    public static String default_diners_bin_range = "360000_369999-R-300000_305999-R-309500_309599" +
            "-R-380000_399999-R-601100_601199-R-640000_649999-R-650000_659999";

    // EMV_CardDataReadAttempt
    public static final int default_chip_attempt = 3;
    public static final int default_contactless_attempt = 3;

    // EMV_CardScheme
    public static String card_scheme = "card_scheme";
    public static String default_card_scheme = "-visa--mastercard--cup--amex--jcb--diners-";

    // EMV_ContactlessTxnLimit
    public static String default_visa_floor_limit = "3000";
    public static String default_mc_floor_limit = "3000";
    public static String default_cup_floor_limit = "3000";
    public static String default_amex_floor_limit = "3000";
    public static String default_jcb_floor_limit = "3000";
    public static String default_diners_floor_limit = "3000";

    public static String default_visa_cvm_limit = "2000";
    public static String default_mc_cvm_limit = "2000";
    public static String default_cup_cvm_limit = "2000";
    public static String default_amex_cvm_limit = "2000";
    public static String default_jcb_cvm_limit = "2000";
    public static String default_diners_cvm_limit = "2000";

    public static String default_visa_transaction_limit = "1000";
    public static String default_mc_transaction_limit = "1000";
    public static String default_cup_transaction_limit = "1000";
    public static String default_amex_transaction_limit = "1000";
    public static String default_jcb_transaction_limit = "1000";
    public static String default_diners_transaction_limit = "1000";

    public static String visa_floor_limit = "visa_floor_limit";
    public static String mc_floor_limit = "mc_floor_limit";
    public static String cup_floor_limit = "cup_floor_limit";
    public static String amex_floor_limit = "amex_floor_limit";
    public static String jcb_floor_limit = "jcb_floor_limit";
    public static String diners_floor_limit = "diners_floor_limit";

    public static String visa_cvm_limit = "visa_cvm_limit";
    public static String mc_cvm_limit = "mc_cvm_limit";
    public static String cup_cvm_limit = "cup_cvm_limit";
    public static String amex_cvm_limit = "amex_cvm_limit";
    public static String jcb_cvm_limit = "jcb_cvm_limit";
    public static String diners_cvm_limit = "diners_cvm_limit";

    public static String visa_transaction_limit = "visa_transaction_limit";
    public static String mc_transaction_limit = "mc_transaction_limit";
    public static String cup_transaction_limit = "cup_transaction_limit";
    public static String amex_transaction_limit = "amex_transaction_limit";
    public static String jcb_transaction_limit = "jcb_transaction_limit";
    public static String diners_transaction_limit = "diners_transaction_limit";

    // EMV_EntryMode
    public static String entry_mode = "entry_mode";

    // EMV_Masking
    public static String default_online_visa_unmask_pan = "1_4_4";
    public static String default_online_mc_unmask_pan = "1_4_4";
    public static String default_online_cup_unmask_pan = "1_4_4";
    public static String default_online_amex_unmask_pan = "1_4_4";
    public static String default_online_jcb_unmask_pan = "1_4_4";
    public static String default_online_diners_unmask_pan = "1_4_4";

    public static String default_online_visa_unmask_expiry = "1_1_1";
    public static String default_online_mc_unmask_expiry = "1_1_1";
    public static String default_online_cup_unmask_expiry = "1_1_1";
    public static String default_online_amex_unmask_expiry = "1_1_1";
    public static String default_online_jcb_unmask_expiry = "1_1_1";
    public static String default_online_diners_unmask_expiry = "1_1_1";

    public static String default_pre_auth_visa_unmask_pan = "1_4_4";
    public static String default_pre_auth_mc_unmask_pan = "1_4_4";
    public static String default_pre_auth_cup_unmask_pan = "1_4_4";
    public static String default_pre_auth_amex_unmask_pan = "1_4_4";
    public static String default_pre_auth_jcb_unmask_pan = "1_4_4";
    public static String default_pre_auth_diners_unmask_pan = "1_4_4";

    public static String default_pre_auth_visa_unmask_expiry = "1_1_1";
    public static String default_pre_auth_mc_unmask_expiry = "1_1_1";
    public static String default_pre_auth_cup_unmask_expiry = "1_1_1";
    public static String default_pre_auth_amex_unmask_expiry = "1_1_1";
    public static String default_pre_auth_jcb_unmask_expiry = "1_1_1";
    public static String default_pre_auth_diners_unmask_expiry = "1_1_1";

    public static String online_visa_unmask_pan = "online_visa_unmask_pan";
    public static String online_mc_unmask_pan = "online_mc_unmask_pan";
    public static String online_cup_unmask_pan = "online_cup_unmask_pan";
    public static String online_amex_unmask_pan = "online_amex_unmask_pan";
    public static String online_jcb_unmask_pan = "online_jcb_unmask_pan";
    public static String online_diners_unmask_pan = "online_diners_unmask_pan";

    public static String online_visa_unmask_expiry = "online_visa_unmask_expiry";
    public static String online_mc_unmask_expiry = "online_mc_unmask_expiry";
    public static String online_cup_unmask_expiry = "online_cup_unmask_expiry";
    public static String online_amex_unmask_expiry = "online_amex_unmask_expiry";
    public static String online_jcb_unmask_expiry = "online_jcb_unmask_expiry";
    public static String online_diners_unmask_expiry = "online_diners_unmask_expiry";

    public static String pre_auth_visa_unmask_pan = "pre_auth_visa_unmask_pan";
    public static String pre_auth_mc_unmask_pan = "pre_auth_mc_unmask_pan";
    public static String pre_auth_cup_unmask_pan = "pre_auth_cup_unmask_pan";
    public static String pre_auth_amex_unmask_pan = "pre_auth_amex_unmask_pan";
    public static String pre_auth_jcb_unmask_pan = "pre_auth_jcb_unmask_pan";
    public static String pre_auth_diners_unmask_pan = "pre_auth_diners_unmask_pan";

    public static String pre_auth_visa_unmask_expiry = "pre_auth_visa_unmask_expiry";
    public static String pre_auth_mc_unmask_expiry = "pre_auth_mc_unmask_expiry";
    public static String pre_auth_cup_unmask_expiry = "pre_auth_cup_unmask_expiry";
    public static String pre_auth_amex_unmask_expiry = "pre_auth_amex_unmask_expiry";
    public static String pre_auth_jcb_unmask_expiry = "pre_auth_jcb_unmask_expiry";
    public static String pre_auth_diners_unmask_expiry = "pre_auth_diners_unmask_expiry";

    // EMV_TransactionType
    public static String available_entry_mode = "available_entry_mode";
    public static String txn_type = "txn_type";

    // SwingCard
    public static String visa_txn_type = "visa_txn_type";
    public static String mastercard_txn_type = "master_txn_type";
    public static String cup_txn_type = "cup_txn_type";
    public static String amex_txn_type = "amex_txn_type";
    public static String jcb_txn_type = "jcb_txn_type";
    public static String diners_txn_type = "diners_txn_type";
    public static String chip_txn_type = "chip_txn_type";
    public static String swipe_txn_type = "swipe_txn_type";
    public static String manual_key_in_txn_type = "manual_key_in_txn_type";
    public static String fallback_txn_type = "fallback_txn_type";
    public static String contactless_txn_type = "contactless_txn_type";
    public static String ENTRYMODE = "entryMode";

    // TradeResult
    public static String cvmResult_Signature = "Signature";
    public static String cvmResult_OnlinePIN = "Encrypted PIN online";
    public static String cvmResult_NoCVM = "No CVM required";
    public static final String TRACE_NO = "traceNo";
    public static final String BATCH_NO = "batchNo";
    public static final String TERMINAL_ID = "terminalID";
    public static String AID = "AID";
    public static String AppName = "AppName";
    public static String EntryMode = "EntryMode";
    public static String CVMResult = "CVMResult";

    //DE55 Tag Field
    public static String TransactionCurrencyCode = "5F2A";
    public static String ApplicationInterchangeProfile = "82";
    public static String DedicatedFileName = "84";
    public static String TerminalVerificationResults = "95";
    public static String TransactionDate = "9A";
    public static String TransactionType = "9C";
    public static String AmountAuthorised = "9F02";
    public static String AmountOther = "9F03";
    public static String ApplicationVersionNumber = "9F09";
    public static String IssuerApplicationData = "9F10";
    public static String TerminalCountryCode = "9F1A";
    public static String InterfaceDeviceSerialNumber = "9F1E";
    public static String ApplicationCryptogram = "9F26";
    public static String CryptogramInformationData = "9F27";
    public static String TerminalCapabilities = "9F33";
    public static String CVMResults = "9F34";
    public static String TerminalType = "9F35";
    public static String ApplicationTransactionCounter = "9F36";
    public static String UnpredictableNumber = "9F37";
    public static String TransactionSequenceCounter = "9F41";
    public static String ApplicationPANSequenceNumber = "5F34";


    /*End Phase 2 EMV 20191119*/


    //TEST URL

    //	//CHECK VERSION URL
    public static String url_check_version = "https://test2.paydollar.com/b2cDemo/CheckVersion";
    //
    // PayDollar API URLa
    public static String url_paydollar_payForm = "https://test2.paydollar.com/b2cDemo/eng/payment/payForm.jsp";
    public static String url_paydollar_payComp = "https://test2.paydollar.com/b2cDemo/eng/directPay/payCompMPOS.jsp";
    public static String url_paydollar_orderApi = "https://test2.paydollar.com/b2cDemo/eng/merchant/api/orderApi_mpos.jsp";
    //    public static String url_paydollar_merInfo = "https://test2.paydollar.com/b2cDemo/eng/merchant/api/merInfoReturn.jsp";
    public static String url_paydollar_merInfo = "https://test2.paydollar.com/b2cDemo/eng/merchant/api/merInfoReturn_mpos.jsp";
    public static String url_paydollar_sendNoti = "https://test2.paydollar.com/b2cDemo/SendNotification";
    public static String url_paydollar_fileUpload = "https://test2.paydollar.com/b2cDemo/FileUploadServlet";
    public static String url_paydollar_genTxtXML = "https://test2.paydollar.com/b2cDemo/GenTxnXML";
    public static String url_paydollar_genTxtXMLMPOS = "https://test2.paydollar.com/b2cDemo/GenTransactionSPOS";
    //    public static String url_paydollar_genTxtXMLMPOS = "https://test2.paydollar.com/b2cDemo/GenTxnRecord";
    public static String url_paydollar_partnerlogo = "https://test2.paydollar.com/b2cDemo/images/merLogo/";
    public static String url_paydollar_CheckStatus = "https://sit.paydollar.com/b2cDemo/eng/directPay/payComp_test.jsp";
    public static String url_paydollar_sendOrder = "https://test2.paydollar.com/b2cDemo/SendEmailMPOS";
    public static String url_paydollar_settinginfo = "https://test2.paydollar.com/b2cDemo/smartpos/mer_info_setting/getMerchantSettingsInfo.jsp";
    //    public static String url_paydollar_settinginfo = "http://192.168.65.180:8080/Paydollar_Test/getMerchantSettingsInfo.jsp";
    public static String url_paydollar_checkBoostStatus = "https://test2.paydollar.com/b2cDemo/eng/payment/checkStatusBOOSTOFFL_MPOS.jsp";
    public static String url_paydollar_QRAction = "https://test2.paydollar.com/b2cDemo/eng/directPay/payKBankQR_Cancel.jsp";
    public static String url_paydollar_GrabPayAction = "https://test2.paydollar.com/b2cDemo/eng/directPay/mPOS/payGrabMPOS.jsp";
    public static String url_paydollar_payFDMSReturnMPOS = "https://test2.paydollar.com/b2cDemo/eng/directPay/mPOS/payFDMSReturnMPOS.jsp";
    public static String url_paydollar_genTxtXMLMPOS_Settlement = "https://test2.paydollar.com/b2cDemo/GenSettlementTransactionSPOS";

    // PesoPay API URL
    public static String url_pesopay_payForm = "https://test2.pesopay.com/b2cDemo/eng/payment/payForm.jsp";
    public static String url_pesopay_payComp = "https://test2.pesopay.com/b2cDemo/eng/directPay/payCompMPOS.jsp";
    public static String url_pesopay_orderApi = "https://test2.pesopay.com/b2cDemo/eng/merchant/api/orderApi_mpos.jsp";
    public static String url_pesopay_merInfo = "https://test2.pesopay.com/b2cDemo/eng/merchant/api/merInfoReturn_mpos.jsp";
    public static String url_pesopay_sendNoti = "https://test2.pesopay.com/b2cDemo/SendNotification";
    public static String url_pesopay_fileUpload = "https://test2.pesopay.com/b2cDemo/FileUploadServlet";
    public static String url_pesopay_genTxtXML = "https://test2.pesopay.com/b2cDemo/GenTxnXML";
    public static String url_pesopay_genTxtXMLMPOS = "https://test2.pesopay.com/b2cDemo/GenTransactionSPOS";
    public static String url_pesopay_partnerlogo = "https://test2.pesopay.com/b2cDemo/images/merLogo/";
    public static String url_pesopay_QRAction = "https://test2.pesopay.com/b2cDemo/eng/directPay/payKBankQR_Cancel.jsp";
    public static String url_pesopay_GrabPayAction = "https://test2.pesopay.com/b2cDemo/eng/directPay/mPOS/payGrabMPOS.jsp";
    public static String url_pesopay_payFDMSReturnMPOS = "https://test2.pesopay.com/b2cDemo/eng/directPay/mPOS/payFDMSReturnMPOS.jsp";
    public static String url_pesopay_genTxtXMLMPOS_Settlement = "https://test2.pesopay.com/b2cDemo/GenSettlementTransactionSPOS";

    // SiamPay API URL
    public static String url_siampay_payForm = "https://test2.pesopay.com/b2cDemo/eng/payment/payForm.jsp";
    public static String url_siampay_payComp = "https://test2.siampay.com/b2cDemo/eng/directPay/payCompMPOS.jsp";
    public static String url_siampay_orderApi = "https://test2.siampay.com/b2cDemo/eng/merchant/api/orderApi_mpos.jsp";
    public static String url_siampay_merInfo = "https://test2.siampay.com/b2cDemo/eng/merchant/api/merInfoReturn_mpos.jsp";
    public static String url_siampay_sendNoti = "https://test2.siampay.com/b2cDemo/SendNotification";
    public static String url_siampay_fileUpload = "https://test2.siampay.com/b2cDemo/FileUploadServlet";
    public static String url_siampay_genTxtXML = "https://test2.siampay.com/b2cDemo/GenTxnXML";
    public static String url_siampay_genTxtXMLMPOS = "https://test2.siampay.com/b2cDemo/GenTransactionSPOS";
    public static String url_siampay_partnerlogo = "https://test2.siampay.com/b2cDemo/images/merLogo/";
    public static String url_siampay_QRAction = "https://test2.siampay.com/b2cDemo/eng/directPay/payKBankQR_Cancel.jsp";
    public static String url_siampay_payFDMSReturnMPOS = "https://test2.siampay.com/b2cDemo/eng/directPay/mPOS/payFDMSReturnMPOS.jsp";
    public static String url_siampay_genTxtXMLMPOS_Settlement = "https://test2.siampay.com/b2cDemo/GenSettlementTransactionSPOS";

/*    //PRODUCTION URL

    //	//CHECK VERSION URL
    public static String url_check_version = "https://www.paydollar.com/b2c2/CheckVersion";
    //
    // PayDollar API URLa
    public static String url_paydollar_payForm = "https://www.paydollar.com/b2c2/eng/payment/payForm.jsp";
    public static String url_paydollar_payComp = "https://www.paydollar.com/b2c2/eng/directPay/payCompMPOS.jsp";
    public static String url_paydollar_orderApi = "https://www.paydollar.com/b2c2/eng/merchant/api/orderApi_mpos.jsp";
    //    public static String url_paydollar_merInfo = "https://www.paydollar.com/b2c2/eng/merchant/api/merInfoReturn.jsp";
    public static String url_paydollar_merInfo = "https://www.paydollar.com/b2c2/eng/merchant/api/merInfoReturn_mpos.jsp";
    public static String url_paydollar_sendNoti = "https://www.paydollar.com/b2c2/SendNotification";
    public static String url_paydollar_fileUpload = "https://www.paydollar.com/b2c2/FileUploadServlet";
    public static String url_paydollar_genTxtXML = "https://www.paydollar.com/b2c2/GenTxnXML";
    public static String url_paydollar_genTxtXMLMPOS = "https://www.paydollar.com/b2c2/GenTransactionSPOS";
    //    public static String url_paydollar_genTxtXMLMPOS = "https://www.paydollar.com/b2c2/GenTxnRecord";
    public static String url_paydollar_partnerlogo = "https://www.paydollar.com/b2c2/images/merLogo/";
    public static String url_paydollar_CheckStatus = "https://sit.paydollar.com/b2cDemo/eng/directPay/payComp_test.jsp";
    public static String url_paydollar_sendOrder = "https://www.paydollar.com/b2c2/SendEmailMPOS";
    public static String url_paydollar_settinginfo = "https://www.paydollar.com/b2c2/smartpos/mer_info_setting/getMerchantSettingsInfo.jsp";
    //    public static String url_paydollar_settinginfo = "http://192.168.65.180:8080/Paydollar_Test/getMerchantSettingsInfo.jsp";
    public static String url_paydollar_checkBoostStatus = "https://www.paydollar.com/b2c2/eng/payment/checkStatusBOOSTOFFL_MPOS.jsp";
    public static String url_paydollar_QRAction = "https://www.paydollar.com/b2c2/eng/directPay/payKBankQR_Cancel.jsp";
    public static String url_paydollar_GrabPayAction = "https://www.paydollar.com/b2c2/eng/directPay/mPOS/payGrabMPOS.jsp";
    public static String url_paydollar_payFDMSReturnMPOS = "https://www.paydollar.com/b2c2/eng/directPay/mPOS/payFDMSReturnMPOS.jsp";
    public static String url_paydollar_genTxtXMLMPOS_Settlement = "https://www.paydollar.com/b2c2/GenSettlementTransactionSPOS";

    // PesoPay API URL
    public static String url_pesopay_payForm = "https://www.pesopay.com/b2c2/eng/payment/payForm.jsp";
    public static String url_pesopay_payComp = "https://www.pesopay.com/b2c2/eng/directPay/payCompMPOS.jsp";
    public static String url_pesopay_orderApi = "https://www.pesopay.com/b2c2/eng/merchant/api/orderApi_mpos.jsp";
    public static String url_pesopay_merInfo = "https://www.pesopay.com/b2c2/eng/merchant/api/merInfoReturn_mpos.jsp";
    public static String url_pesopay_sendNoti = "https://www.pesopay.com/b2c2/SendNotification";
    public static String url_pesopay_fileUpload = "https://www.pesopay.com/b2c2/FileUploadServlet";
    public static String url_pesopay_genTxtXML = "https://www.pesopay.com/b2c2/GenTxnXML";
    public static String url_pesopay_genTxtXMLMPOS = "https://www.pesopay.com/b2c2/GenTransactionSPOS";
    public static String url_pesopay_partnerlogo = "https://www.pesopay.com/b2c2/images/merLogo/";
    public static String url_pesopay_QRAction = "https://www.pesopay.com/b2c2/eng/directPay/payKBankQR_Cancel.jsp";
    public static String url_pesopay_GrabPayAction = "https://www.pesopay.com/b2c2/eng/directPay/mPOS/payGrabMPOS.jsp";
    public static String url_pesopay_payFDMSReturnMPOS = "https://www.pesopay.com/b2c2/eng/directPay/mPOS/payFDMSReturnMPOS.jsp";
    public static String url_pesopay_genTxtXMLMPOS_Settlement = "https://www.pesopay.com/b2c2/GenSettlementTransactionSPOS";

    // SiamPay API URL
    public static String url_siampay_payForm = "https://test2.pesopay.com/b2c2/eng/payment/payForm.jsp";
    public static String url_siampay_payComp = "https://www.siampay.com/b2c2/eng/directPay/payCompMPOS.jsp";
    public static String url_siampay_orderApi = "https://www.siampay.com/b2c2/eng/merchant/api/orderApi_mpos.jsp";
    public static String url_siampay_merInfo = "https://www.siampay.com/b2c2/eng/merchant/api/merInfoReturn_mpos.jsp";
    public static String url_siampay_sendNoti = "https://www.siampay.com/b2c2/SendNotification";
    public static String url_siampay_fileUpload = "https://www.siampay.com/b2c2/FileUploadServlet";
    public static String url_siampay_genTxtXML = "https://www.siampay.com/b2c2/GenTxnXML";
    public static String url_siampay_genTxtXMLMPOS = "https://www.siampay.com/b2c2/GenTransactionSPOS";
    public static String url_siampay_partnerlogo = "https://www.siampay.com/b2c2/images/merLogo/";
    public static String url_siampay_QRAction = "https://www.siampay.com/b2c2/eng/directPay/payKBankQR_Cancel.jsp";
    public static String url_siampay_payFDMSReturnMPOS = "https://www.siampay.com/b2c2/eng/directPay/mPOS/payFDMSReturnMPOS.jsp";
    public static String url_siampay_genTxtXMLMPOS_Settlement = "https://www.siampay.com/b2c2/GenSettlementTransactionSPOS";*/

/*
//		//CHECK VERSION URL/*
	 	public static String url_check_version = "https://sit.paydollar.com/b2cDemo/CheckVersion";
//
		//PayDollar API URL
			 public static String url_paydollar_payForm = "https://sit.paydollar.com/b2cDemo/eng/payment/payForm.jsp";
			 public static String url_paydollar_payComp = "https://sit.paydollar.com/b2cDemo/eng/directPay/payComp.jsp";
			 public static String url_paydollar_orderApi="https://sit.paydollar.com/b2cDemo/eng/merchant/api/orderApi.jsp";
			 public static String url_paydollar_merInfo = "https://sit.paydollar.com/b2cDemo/eng/merchant/api/merInfoReturn.jsp";
			 public static String url_paydollar_sendNoti = "https://sit.paydollar.com/b2cDemo/SendNotification";
			 public static String url_paydollar_fileUpload ="https://sit.paydollar.com/b2cDemo/FileUploadServlet";
			 public static String url_paydollar_genTxtXML = "https://sit.paydollar.com/b2cDemo/GenTxnXML";
			 public static String url_paydollar_partnerlogo = "https://sit.paydollar.com/b2cDemo/images/";
		//PesoPay API URL
			 public static String url_pesopay_payForm = "https://sit.pesopay.com/b2cDemo/eng/payment/payForm.jsp";
			 public static String url_pesopay_payComp = "https://sit.pesopay.com/b2cDemo/eng/directPay/payComp.jsp";
			 public static String url_pesopay_orderApi="https://sit.pesopay.com/b2cDemo/eng/merchant/api/orderApi.jsp";
			 public static String url_pesopay_merInfo = "https://sit.pesopay.com/b2cDemo/eng/merchant/api/merInfoReturn.jsp";
			 public static String url_pesopay_sendNoti = "https://sit.pesopay.com/b2cDemo/SendNotification";
			 public static String url_pesopay_fileUpload ="https://sit.pesopay.com/b2cDemo/FileUploadServlet";
			 public static String url_pesopay_genTxtXML = "https://sit.pesopay.com/b2cDemo/GenTxnXML";
			 public static String url_pesopay_partnerlogo = "https://sit.pesopay.com/b2cDemo/images/";

		//SiamPay API URL
			 public static String url_siampay_payForm = "https://sit.siampay.com/b2cDemo/eng/payment/payForm.jsp";
			 public static String url_siampay_payComp = "https://sit.siampay.com/b2cDemo/eng/directPay/payComp.jsp";
			 public static String url_siampay_orderApi="https://sit.siampay.com/b2cDemo/eng/merchant/api/orderApi.jsp";
			 public static String url_siampay_merInfo = "https://sit.siampay.com/b2cDemo/eng/merchant/api/merInfoReturn.jsp";
			 public static String url_siampay_sendNoti = "https://sit.siampay.com/b2cDemo/SendNotification";
			 public static String url_siampay_fileUpload ="https://sit.siampay.com/b2cDemo/FileUploadServlet";
			 public static String url_siampay_genTxtXML = "https://sit.siampay.com/b2cDemo/GenTxnXML";
			 public static String url_siampay_partnerlogo = "https://sit.siampay.com/b2cDemo/images/";

 */

    /*
        //CHECK VERSION URL
                     public static String url_check_version = "https://192.168.7.123/b2cDemo/CheckVersion";
    //
                //PayDollar API URL
                     public static String url_paydollar_payForm = "https://192.168.7.123/b2cDemo/eng/payment/payForm.jsp";
                     public static String url_paydollar_payComp = "https://192.168.7.123/b2cDemo/eng/directPay/payComp.jsp";
                     public static String url_paydollar_orderApi="https://192.168.7.123/b2cDemo/eng/merchant/api/orderApi.jsp";
                     public static String url_paydollar_merInfo = "https://192.168.7.123/b2cDemo/eng/merchant/api/merInfoReturn.jsp";
                     public static String url_paydollar_sendNoti = "https://192.168.7.123/b2cDemo/SendNotification";
                     public static String url_paydollar_fileUpload ="https://192.168.7.123/b2cDemo/FileUploadServlet";
                     public static String url_paydollar_genTxtXML = "https://192.168.7.123/b2cDemo/GenTxnXML";
                     public static String url_paydollar_partnerlogo = "https://192.168.7.123/b2cDemo/images/";
                //PesoPay API URL
                     public static String url_pesopay_payForm = "https://192.168.7.123/b2cDemo/eng/payment/payForm.jsp";
                     public static String url_pesopay_payComp = "https://192.168.7.123/b2cDemo/eng/directPay/payComp.jsp";
                     public static String url_pesopay_orderApi="https://192.168.7.123/b2cDemo/eng/merchant/api/orderApi.jsp";
                     public static String url_pesopay_merInfo = "https://192.168.7.123/b2cDemo/eng/merchant/api/merInfoReturn.jsp";
                     public static String url_pesopay_sendNoti = "https://192.168.7.123/b2cDemo/SendNotification";
                     public static String url_pesopay_fileUpload ="https://192.168.7.123/b2cDemo/FileUploadServlet";
                     public static String url_pesopay_genTxtXML = "https://192.168.7.123/b2cDemo/GenTxnXML";
                     public static String url_pesopay_partnerlogo = "https://192.168.7.123/b2cDemo/images/";

                //SiamPay API URL
                     public static String url_siampay_payForm = "https://192.168.7.123/b2cDemo/eng/payment/payForm.jsp";
                     public static String url_siampay_payComp = "https://192.168.7.123/b2cDemo/eng/directPay/payComp.jsp";
                     public static String url_siampay_orderApi="https://192.168.7.123/b2cDemo/eng/merchant/api/orderApi.jsp";
                     public static String url_siampay_merInfo = "https://192.168.7.123/b2cDemo/eng/merchant/api/merInfoReturn.jsp";
                     public static String url_siampay_sendNoti = "https://192.168.7.123/b2cDemo/SendNotification";
                     public static String url_siampay_fileUpload ="https://192.168.7.123/b2cDemo/FileUploadServlet";
                     public static String url_siampay_genTxtXML = "https://192.168.7.123/b2cDemo/GenTxnXML";
                     public static String url_siampay_partnerlogo = "https://192.168.7.123/b2cDemo/images/";
        */
    public static int CURRENT_PAGE_NO = 1;
    public static int VOIDTAB_CURRENT_PAGE_NO = 1;
    public static String VOIDTAB_SEARCHFILTER = "paymentRef";
    public static String VOIDTAB_DATEFILTER = "last2days";
    public static Boolean VOIDTAB_LASTRECORD = false;
    //	 public static boolean plugin = false;

    //    public static ArrayList<Record> record_data;


    public static Boolean isSecCodeOn;
}
