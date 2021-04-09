package com.android.priyanka.dictionary.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private String DB_PATH = "";
    private static String DB_NAME = "eng_dictionary.db";
    private SQLiteDatabase sqLiteDatabase;
    private final Context mContext;
    public static final String TAG = DatabaseHelper.class.getSimpleName();


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
       // this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        this.DB_PATH = mContext.getApplicationInfo().dataDir+"/databases/";;

        Log.i(TAG, "DB Path.." + DB_PATH);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade");
        //delete the existing db and copy the new db
        try {
            this.getReadableDatabase();
            // this.close();
            mContext.deleteDatabase(DB_NAME);
            copyDatabase();
        }
        catch(IOException e ){
            e.printStackTrace();

                }
    }



    //method to call checkDatabse
    public void createDatabase()  throws  IOException {
        Log.i(TAG, "createDatabase");
        boolean dbExist = checkDatabase();
        if (!dbExist) {   //db doesn't exist
            this.getReadableDatabase();
            //this.close();   //for Pie
            try {
                copyDatabase();

            }
            catch(IOException e) {
                throw new Error("Error copying database");

            }
        }
    }


    private void copyDatabase() throws IOException {
        Log.i(TAG, "copyDatabase");
try {
    InputStream inputStream = mContext.getAssets().open(DB_NAME);
    String outputFileName = DB_PATH + DB_NAME;
    OutputStream outputStream = new FileOutputStream(outputFileName);

    byte[] buffer = new byte[1024];
    int length;

    while ((length = inputStream.read(buffer)) > 0) {
        outputStream.write(buffer, 0, length);
    }

    outputStream.flush();
    outputStream.close();
    inputStream.close();

    Log.i(TAG, "copyDatabse"+ " Database copied");

}
catch(IOException e){
    e.printStackTrace();

}
    }


    //check if DB already exists or not
    public boolean checkDatabase() throws SQLiteException {
        Log.i(TAG, "checkDatabase");
        SQLiteDatabase db = null;
        try {

            String myPath = this.DB_PATH + DB_NAME;
            Log.i(TAG,"myPath"+myPath);
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (Exception e) {
            Log.e(TAG, "SQLite exception.."+e);


        }

        if (db != null) {
            db.close();

        }

        return db != null? true : false;

    }

    //method to open DB
    public void openDatabase() throws SQLiteException {
        Log.i(TAG, "openDatabase");
try {
    String myPath = DB_PATH + DB_NAME;
    sqLiteDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
}
catch(SQLiteException e){
    Log.e(TAG, "sqlite open exception.."+"+"+e);
}
    }

    @Override
    public synchronized void close() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
        super.close();
    }

    public Cursor getMeaning(String text) {
        Cursor c = sqLiteDatabase.rawQuery("SELECT en_definition,example,synonyms,antonyms FROM words WHERE en_word==UPPER('"+text+"')",null);
        return c;
    }

     public Cursor getSuggestions(String text) {
        Cursor c = sqLiteDatabase.rawQuery("SELECT _id,en_word FROM words WHERE en_word LIKE '"+text+"%' LIMIT 40",null);
         System.out.println("c = " + c.getCount());
        return c;
    }

    public void insertHistory(String text) {

        sqLiteDatabase.execSQL("INSERT INTO history(word) VALUES(UPPER('"+text+"'))");
    }

    public Cursor getHistory() {
        Cursor c = sqLiteDatabase.rawQuery("SELECT DISTINCT word,en_definition FROM history h JOIN words w ON h.word==w.en_word ORDER BY h._id DESC",null);
        return c;
    }

    public void deletehistory() {

        sqLiteDatabase.execSQL("DELETE FROM history");
    }

}

