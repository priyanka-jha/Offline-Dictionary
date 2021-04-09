package com.android.priyanka.dictionary.util;

import android.content.Context;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.android.priyanka.dictionary.R;
import com.android.priyanka.dictionary.database.DatabaseHelper;
import com.android.priyanka.dictionary.view.MainActivity;

import java.io.IOException;



public class LoadDatabaseAsync extends AsyncTask<Void, Void, Boolean> {
    private Context context;
    private AlertDialog alertDialog;
    private DatabaseHelper databaseHelper;

    public LoadDatabaseAsync(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        try {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.alert_dialog_copydatabase,null);
        builder.setTitle("Loading words...");
        builder.setView(dialogView);
        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        databaseHelper = new DatabaseHelper(context);

        try {
            databaseHelper.createDatabase();
        }
        catch ( IOException e){
            e.printStackTrace();
            throw new Error("Database was not created");
        }
        databaseHelper.close();
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        alertDialog.dismiss();
        //open db after async task finishes copying db
        try {

          //to call protected static method

          /*MainActivity mainActivity = new MainActivity();
          mainActivity.getClass().getDeclaredMethod("openDatabase");*/

            //to call public static method
             MainActivity.openDatabase();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("loaddatabaseasync opendatabase error.." +e);
        }
    }
}
