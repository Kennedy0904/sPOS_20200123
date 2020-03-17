package com.example.dell.smartpos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String Db_Name = "MPOSDB.db";
    private final static int Db_Ver = 3; //prev 2
    private final static String TABLE_ID = "Settings";
    private final static String MERCHANT_ID = "merID";
    private final static String USER_ID = "userID";
    private final static String CURRCODE = "currCode";
    private final static String DEVICE_ID = "deviceID";
    private final static String DEVICE_SERIALNO = "deviceSerialNo";
    private final static String API_ID = "apiID";
    private final static String API_PW = "apipw";
    private final static String SECURITY_HASH = "securityhash";
    private final static String MER_CLASS = "merClass";
    private final static String ENABLE_SMS = "enableSMS";
    private final static String SURCHARGE_CALCULATION = Constants.pref_surcharge_calculation;

    //--Edited 25/07/18 by KJ--//
    private final static String ALIPAY_TIMEOUT = "alipayTimeout";
    private final static String WECHAT_TIMEOUT = "wechatTimeout";
    private final static String UNIONPAY_TIMEOUT = "unionTimeout";
    private final static String LAST_LOGIN = "lastlogin";
    //--done Edited 25/07/18 by KJ--//

    public DatabaseHelper(Context context) {
        super(context, Db_Name, null, Db_Ver);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        //--Edited 25/07/18 by KJ--//
        String sqlsyntax = "CREATE TABLE IF NOT EXISTS " + TABLE_ID +
                "(" +
                MERCHANT_ID + " TEXT PRIMARY KEY, " +
                DEVICE_ID + " TEXT, " +
                DEVICE_SERIALNO + " TEXT, " +
                USER_ID + " TEXT, " +
                API_ID + " TEXT, " +
                API_PW + " TEXT, " +
                MER_CLASS + " TEXT, " +
                SECURITY_HASH + " TEXT, " +
                ENABLE_SMS + " TEXT, " +
                CURRCODE + " TEXT, " +
                SURCHARGE_CALCULATION + " TEXT," +
                ALIPAY_TIMEOUT + " TEXT," +
                WECHAT_TIMEOUT + " TEXT," +
                UNIONPAY_TIMEOUT + " TEXT," +
                LAST_LOGIN + " TEXT" +
                ");";
        //--done Edited 25/07/18 by KJ--//
        db.execSQL(sqlsyntax);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        switch (oldVersion) {
            case 2: //outdated
                String upgradeQuery = "ALTER TABLE " + TABLE_ID + " ADD COLUMN " + ENABLE_SMS + " TEXT";
                String upgradeQuery2 = "ALTER TABLE " + TABLE_ID + " ADD COLUMN " + CURRCODE + " TEXT";

                db.execSQL(upgradeQuery);
                db.execSQL(upgradeQuery2);

//			case 3: //outdated
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

    }

    @Override
    public synchronized void close() {

        super.close();
    }

    public String getSingleData(String tableId, String column, String whereColumn, String[] whereArgs) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT " + column + " FROM " + tableId + " WHERE " + whereColumn + "=?";
        Cursor cursor = db.rawQuery(sql, whereArgs);
        int count = cursor.getCount();
        String result = null;
        if (count != 0) {
            cursor.moveToFirst();
            result = cursor.getString(0);
        }
        cursor.close();
        return result;

    }

    public int update(String tableId, ContentValues values,
                      String whereClause, String[] whereArgs) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = this.getWritableDatabase();
        // updating row
        return db.update(tableId, values, whereClause, whereArgs);

    }

    public long insert(String tableId, ContentValues values) {

        SQLiteDatabase db = this.getWritableDatabase();
        return db.insertWithOnConflict(tableId, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void upsert(String tableId, ContentValues values,
                       String whereClause, String[] whereArgs) {

        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.update(tableId, values, whereClause, whereArgs);
        if (rowsAffected == 0) {
            db.insert(tableId, null, values);
        }

    }

    public String[] getRecords(String tableId, String column, String text) throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT DISTINCT " + column + " FROM " + tableId;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();

        String[] stringArray = new String[count];
        if (count != 0) {
            cursor.moveToFirst();
            int i = 0;
//			System.out.println(text +"=merchaname");
            while (cursor.isAfterLast() == false) {
                DesEncrypter encrypter = new DesEncrypter(text);
                stringArray[i] = encrypter.decrypt(cursor.getString(0));
//            	System.out.println(stringArray[i]);
                cursor.moveToNext();
                i++;
            }
        }

        cursor.close();
        return stringArray;

    }

    public int checkFirstTime(String tableId, String column, String whereColumn, String[] whereArgs) {
        SQLiteDatabase db = this.getReadableDatabase();
        //--Edited 25/07/18 by KJ--//
        String sql = "SELECT " + column + " FROM " + tableId + " WHERE " + whereColumn + "=? " + "AND (" +
                "(" + API_ID + " IS NULL OR " + API_ID + "='')" + " AND " +
                "(" + API_PW + " IS NULL OR " + API_PW + "='')" + " AND " +
                "(" + SECURITY_HASH + " IS NULL OR " + SECURITY_HASH + "='')" + " AND " +
                "(" + ALIPAY_TIMEOUT + " IS NULL OR " + ALIPAY_TIMEOUT + "='')" + " AND " +
                "(" + WECHAT_TIMEOUT + " IS NULL OR " + WECHAT_TIMEOUT + "='')" + " AND " +
                "(" + UNIONPAY_TIMEOUT + " IS NULL OR " + UNIONPAY_TIMEOUT + "='')" + " AND " +
                "(" + LAST_LOGIN + " IS NULL OR " + LAST_LOGIN + "='')" +
                ")";
        //--done Edited 25/07/18 by KJ--//
        Cursor cursor = db.rawQuery(sql, whereArgs);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }

    public boolean isFieldExist(String tableId, String column, String whereColumn, String[] whereArgs) {
        SQLiteDatabase db = this.getReadableDatabase();
//		String sql = "SELECT "+ column+ " FROM " + tableId + " WHERE " + whereColumn + "=?";
        String sql = "SELECT * FROM " + tableId + " WHERE " + column + "=?";
        Log.d("OTTO", "isFieldExist sql:" + sql);
        try {
            Cursor cursor = db.rawQuery(sql, whereArgs);
            if ((cursor != null) || (cursor.getColumnIndex(column) != -1)) {
                cursor.close();
                db.close();
                return true;
            } else {
                cursor.close();
                db.close();
                return false;
            }
        } catch (Exception e) {
            Log.d("OTTO", "DatabaseHelper is FieldExist Exception:" + e);
            return false;
        }
    }

    public boolean alterField(String merId, String tableId, String column, String whereColumn, String[] whereArgs) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String sql = "ALTER TABLE " + tableId + " ADD COLUMN " + column + " TEXT ";
            db.execSQL(sql);
            db.close();
        } catch (Exception e) {
            Log.d("OTTO", "DatabaseHelper alterField Exception:" + e);
        } finally {
            if (db != null) {
                db.close();
            }
        }

        String whereargs[] = {merId};
        boolean isfieldexist = isFieldExist(Constants.DB_TABLE_ID, Constants.DB_SUR_CAL, Constants.DB_MERCHANT_ID, whereargs);
        if (isfieldexist) {
            return true;
        } else {
            return false;
        }
    }

}

