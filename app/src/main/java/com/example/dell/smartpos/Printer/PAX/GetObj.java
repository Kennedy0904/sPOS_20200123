package com.example.dell.smartpos.Printer.PAX;

import android.content.Context;
import android.util.Log;

import com.pax.dal.IDAL;
import com.pax.neptunelite.api.NeptuneLiteUser;

public class GetObj {

    private static IDAL dal, idal;
    public static String logStr = "";
    public static Context context;

    public GetObj(Context context){
        super();
        this.context = context;
    }

    // 获取IDal dal对象
    public static IDAL getDal() {

        try {
            dal = NeptuneLiteUser.getInstance().getDal(GetObj.context);
            if (dal == null) {
                Log.e("NeptuneLiteDemo", "dal is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dal == null) {
            Log.e("NeptuneLiteDemo", "dal is null");
        }
        return dal;
    }

}
