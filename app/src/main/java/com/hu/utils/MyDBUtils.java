package com.hu.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hu.sqlite.SQLiteHelper;
import com.hu.bean.PersonalBean;

public class MyDBUtils {
    private static MyDBUtils instance = null;
    private static SQLiteHelper helper;
    private static SQLiteDatabase db;

    public MyDBUtils(Context context) {
        helper = new SQLiteHelper(context);
        db = helper.getWritableDatabase();
    }
    public static MyDBUtils getInstance(Context context) {
        if (instance == null) {
            instance = new MyDBUtils(context);
        }
        return instance;
    }
    /**
     * 保存用户信息
     */
    public void saveUserInfo(PersonalBean bean) {
        ContentValues cv = new ContentValues();
        cv.put("nickName", bean.getNickName());
        cv.put("userName", bean.getUserName());
        cv.put("phone", bean.getPhone());
        cv.put("qq", bean.getQq());
        cv.put("name", bean.getName());
        cv.put("number", bean.getNumber());
        cv.put("major", bean.getMajor());
        cv.put("grade", bean.getGrade());
        cv.put("classes", bean.getClasses());
        cv.put("level", bean.getLevel());

        db.insert(SQLiteHelper.U_PERSONALINFO, null, cv);
    }
    public PersonalBean getUserInfo(String userName) {
        String sql = "SELECT * FROM " + SQLiteHelper.U_PERSONALINFO + " WHERE userName=?";
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        PersonalBean bean = null;
        while (cursor.moveToNext()) {
            bean = new PersonalBean();
            bean.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
            bean.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
            bean.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            bean.setQq(cursor.getString(cursor.getColumnIndex("qq")));
            bean.setName(cursor.getString(cursor.getColumnIndex("name")));
            bean.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            bean.setMajor(cursor.getInt(cursor.getColumnIndex("major")));
            bean.setGrade(cursor.getInt(cursor.getColumnIndex("grade")));
            bean.setClasses(cursor.getInt(cursor.getColumnIndex("classes")));
            bean.setLevel(cursor.getInt(cursor.getColumnIndex("level")));

        }
        cursor.close();
        return bean;
    }
    public void updateUserInfo(String key, String value, String userName) {
        ContentValues cv = new ContentValues();
        cv.put(key, value);
        db.update(SQLiteHelper.U_PERSONALINFO, cv, "userName=?", new String[]{userName});
    }

}
