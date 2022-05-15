package com.example.lostfoundapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.lostfoundapp.model.Advert;
import com.example.lostfoundapp.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "tag";

    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_ADVERT_TABLE = "CREATE TABLE " + Util.TABLE_NAME +
                "(" + Util.ADVERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.POST_TYPE + " INTEGER, "
                + Util.NAME + " TEXT," + Util.PHONE + " TEXT, "
                + Util.DESCRIPTION + " TEXT, " + Util.DATE + " TEXT, "
                + Util.LOCATION + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_ADVERT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + Util.TABLE_NAME + "'");
        onCreate(sqLiteDatabase);
    }

    public long insertAdvert (Advert advert)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.POST_TYPE, advert.getPost_Type());
        contentValues.put(Util.NAME, advert.getName());
        contentValues.put(Util.PHONE, advert.getPhone());
        contentValues.put(Util.DESCRIPTION, advert.getDescription());
        contentValues.put(Util.DATE, advert.getDate());
        contentValues.put(Util.LOCATION, advert.getLocation());
        long newRowId = db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();
        return newRowId;
    }

    public List<Advert> getAllAdverts (){
        List<Advert> advertList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = " SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Advert advert = new Advert();
                advert.setAdvert_id(cursor.getInt(0));
                advert.setPost_Type(cursor.getInt(1));
                advert.setName(cursor.getString(2));
                advert.setPhone(cursor.getString(3));
                advert.setDescription(cursor.getString(4));
                advert.setDate(cursor.getString(5));
                advert.setLocation(cursor.getString(6));
                advertList.add(advert);

            } while (cursor.moveToNext());

        }
        return advertList;
    }

    public boolean deleteAdvert(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Boolean result = db.delete(Util.TABLE_NAME, Util.ADVERT_ID + "=?", new String[]{Integer.toString(id)}) > 0;
        db.close();
        return result;
    }
}
