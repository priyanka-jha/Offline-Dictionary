package com.android.priyanka.dictionary.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.priyanka.dictionary.BuildConfig;
import com.android.priyanka.dictionary.R;
import com.android.priyanka.dictionary.database.DatabaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.clearHistory)
    TextView clearHistory;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    DatabaseHelper databaseHelper;
    @BindView(R.id.sharAapp)
    TextView sharAapp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");

        toolbar.setNavigationIcon(R.drawable.back_icon);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId())) {

            case android.R.id.home:
                onBackPressed();

        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.clearHistory,R.id.sharAapp})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.clearHistory:

            try {
            databaseHelper = new DatabaseHelper(SettingsActivity.this);
            try {
                databaseHelper.openDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }

                showAlertDialog();

        } catch (Exception e) {
            e.printStackTrace();
        }



            break;


         case R.id.sharAapp:

             try {
                 Intent shareIntent = new Intent(Intent.ACTION_SEND);
                 shareIntent.setType("text/plain");
                 shareIntent.putExtra(Intent.EXTRA_SUBJECT, "English Dictionary");
                 String shareMessage= "\nLet me recommend you this application\n\n";
                 shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                 shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                 startActivity(Intent.createChooser(shareIntent, "choose one"));
             } catch(Exception e) {
                 e.toString();
             }

             break;



        }
    }

    private void showAlertDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.myDialogTheme);
            builder.setTitle("Are you sure?");
            builder.setMessage("All the history will be deleted");

            String positiveText = "Yes";
            builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    databaseHelper.deletehistory();
                    Toast.makeText(SettingsActivity.this, "History deleted!!", Toast.LENGTH_SHORT).show();

                }
            });

            String negativeText = "No";
            builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
