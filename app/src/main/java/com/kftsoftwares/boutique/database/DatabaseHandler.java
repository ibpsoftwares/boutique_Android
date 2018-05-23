package com.kftsoftwares.boutique.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kftsoftwares.boutique.Models.CartViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 08/03/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "boutiqueManager";

    // Contacts table name
    private static final String TABLE_BOUTIQUE = "boutique";

    // Contacts Table Columns names
    private static final String USER_ID = "id";
    private static final String CLOTH_NAME = "name";
    private static final String CLOTH_IMAGE = "image";
    private static final String CLOTH_CATEGORY_ID = "cloth_cat";
    private static final String SIZE = "size";
    private static final String SIZE_ID = "size_id";
    private static final String Price = "price";
    private static final String Category = "category";
    private static final String Count = "count";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_BOUTIQUE + "("
                + USER_ID + " TEXT," + Price + " TEXT," + CLOTH_CATEGORY_ID + " TEXT," + Count + " TEXT," + Category + " TEXT," + SIZE + " TEXT," + SIZE_ID + " TEXT," + CLOTH_NAME + " TEXT," + CLOTH_IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOUTIQUE);
        // Create tables again
        onCreate(db);
    }


    public void addContact(CartViewModel cartViewModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, cartViewModel.getClothId());
        contentValues.put(CLOTH_NAME, cartViewModel.getTitle());
        contentValues.put(CLOTH_IMAGE, cartViewModel.getImage1());
        contentValues.put(CLOTH_CATEGORY_ID, cartViewModel.getCategoryId());
        contentValues.put(Price, cartViewModel.getPrice());
        contentValues.put(SIZE, cartViewModel.getSize());
        contentValues.put(SIZE_ID, cartViewModel.getSize_id());
        contentValues.put(Category, cartViewModel.getCat());
        contentValues.put(Count, cartViewModel.getCount());
        // Inserting Row
        long i = db.insert(TABLE_BOUTIQUE, null, contentValues);

        db.close(); // Closing database connection
    }

    // Getting All Contacts
    public List<CartViewModel> getAllDataOfWishlist() {
        List<CartViewModel> contactList = new ArrayList<CartViewModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BOUTIQUE + " WHERE category = 'wishList'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CartViewModel cartViewModel = new CartViewModel();
                cartViewModel.setClothId(cursor.getString(cursor.getColumnIndex(USER_ID)));
                cartViewModel.setTitle(cursor.getString(cursor.getColumnIndex(CLOTH_NAME)));
                cartViewModel.setImage1(cursor.getString(cursor.getColumnIndex(CLOTH_IMAGE)));
                cartViewModel.setPrice(cursor.getString(cursor.getColumnIndex(Price)));
                cartViewModel.setCategoryName(cursor.getString(cursor.getColumnIndex(Category)));
                cartViewModel.setCount(cursor.getString(cursor.getColumnIndex(Count)));
                cartViewModel.setCategoryId(cursor.getString(cursor.getColumnIndex(CLOTH_CATEGORY_ID)));
                // Adding cartViewModel to list
                contactList.add(cartViewModel);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public List<CartViewModel> getAllDataOfCart() {
        List<CartViewModel> contactList = new ArrayList<CartViewModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BOUTIQUE + " WHERE category = 'cart'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CartViewModel cartViewModel = new CartViewModel();
                cartViewModel.setClothId(cursor.getString(cursor.getColumnIndex(USER_ID)));
                cartViewModel.setTitle(cursor.getString(cursor.getColumnIndex(CLOTH_NAME)));
                cartViewModel.setImage1(cursor.getString(cursor.getColumnIndex(CLOTH_IMAGE)));
                cartViewModel.setPrice(cursor.getString(cursor.getColumnIndex(Price)));
                cartViewModel.setCategoryName(cursor.getString(cursor.getColumnIndex(Category)));
                cartViewModel.setCount(cursor.getString(cursor.getColumnIndex(Count)));
                cartViewModel.setSize_id(cursor.getString(cursor.getColumnIndex(SIZE_ID)));
                cartViewModel.setSize(cursor.getString(cursor.getColumnIndex(SIZE)));
                cartViewModel.setCategoryId(cursor.getString(cursor.getColumnIndex(CLOTH_CATEGORY_ID)));
                // Adding cartViewModel to list
                contactList.add(cartViewModel);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public void updateCategory(String value, String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String strSQL = "UPDATE " + TABLE_BOUTIQUE + " SET category = '" + value + "' WHERE " + USER_ID + " = " + user_id;

        db.execSQL(strSQL);
        db.close();
    }
  public void updateCategoryWithSize(String value, String user_id , String size , String size_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String strSQL = "UPDATE " + TABLE_BOUTIQUE + " SET category = '" + value + "' , " + SIZE_ID +" = '" +size_id+ "' ," + SIZE + " = '"+ size +"' WHERE " + USER_ID + " = " + user_id +" AND "+ SIZE +" = 'noData'";

        db.execSQL(strSQL);
        db.close();
    }


    public void removeDataFromWishList(String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BOUTIQUE + " WHERE " + USER_ID + "='" + user_id + "'" + " AND " + Category + "='wishList'");
        db.close();
    }

    public void removeDataFromCart(String user_id , String size) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BOUTIQUE + " WHERE " + USER_ID + "='" + user_id + "'" + " AND " + Category + "='cart'" + " AND " + SIZE + "='"+size+"'");
        db.close();
    }

    public boolean CheckIsDataAlreadyInDBorNot(String fieldValue) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TABLE_BOUTIQUE + " WHERE " + USER_ID + " ='" + fieldValue + "'" + " AND " + Category + "='cart'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        int val = cursor.getCount();
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    } public boolean CheckIsDataAlreadyInDBorNotWithSize(String fieldValue , String size) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TABLE_BOUTIQUE + " WHERE " + USER_ID + " ='" + fieldValue + "'" + " AND " + Category + "='cart'"  + " AND " + SIZE + "='"+size+"'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        int val = cursor.getCount();
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean CheckIsDataAlreadyInWishList(String fieldValue) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TABLE_BOUTIQUE + " WHERE " + USER_ID + " ='" + fieldValue + "'" + " AND " + Category + "='wishList'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        int val = cursor.getCount();
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public List<String> getUser_ID_Data(String category) {
        List<String> contactList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT " + USER_ID + " FROM " + TABLE_BOUTIQUE + " WHERE category = '" + category + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                contactList.add(cursor.getString(cursor.getColumnIndex(USER_ID)));

            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public void DeleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = "delete from " + TABLE_BOUTIQUE;
        db.execSQL(deleteQuery);
        db.close();
    }

}
