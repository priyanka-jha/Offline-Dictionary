package com.android.priyanka.dictionary.view;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.priyanka.dictionary.R;
import com.android.priyanka.dictionary.adapter.ViewPagerAdapter;
import com.android.priyanka.dictionary.database.DatabaseHelper;
import com.android.priyanka.dictionary.fragments.Antonyms;
import com.android.priyanka.dictionary.fragments.Definition;
import com.android.priyanka.dictionary.fragments.Example;
import com.android.priyanka.dictionary.fragments.Synonyms;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeaningActivity extends AppCompatActivity {

    @BindView(R.id.btnSpeaker)
    ImageButton btnSpeaker;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    DatabaseHelper databaseHelper;
    Cursor c = null;
    String enWord;
    public String definition="", example="", synonyms="", antonyms="";
    TextToSpeech tts;

    Boolean startedFromShare = false;
    @BindView(R.id.shareFab)
    FloatingActionButton shareFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        enWord = bundle.getString("en_word");

        Intent i = getIntent();
        String action = i.getAction();
        String type = i.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {

            if ("text/plain".equals(type)) {

                String sharedText = i.getStringExtra(i.EXTRA_TEXT);
                startedFromShare = true;

                if (sharedText != null) {
                    Pattern pattern = Pattern.compile("[A-Za-z \\-.]{1,25}");
                    Matcher matcher = pattern.matcher(sharedText);

                    if (matcher.matches()) {
                        enWord = sharedText;
                    } else {
                        enWord = "Not Available";
                    }

                }
            }


        }


        databaseHelper = new DatabaseHelper(this);

        try {
            databaseHelper.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            c = databaseHelper.getMeaning(enWord);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occured");
        }
        if (c.moveToFirst()) {

            do {
                definition = c.getString(c.getColumnIndex("en_definition"));
                definition = definition.substring(0, 1).toUpperCase() + definition.substring(1);

                example = c.getString(c.getColumnIndex("example"));
                example = example.substring(0, 1).toUpperCase() + example.substring(1);
                if (example.equals("NA")) {
                    example = "No example found ";
                }

                /*synonyms = c.getString(c.getColumnIndex("synonyms"));
                if (synonyms.equals("NA")) {
                    synonyms = "No synonym found ";
                }*/

                String synonyms1 = c.getString(c.getColumnIndex("synonyms"));

                String[] synonymsArray = synonyms1.split(",");

                for(int j = 0 ; j < synonymsArray.length; j++)
                {
                    // String synonyms = "";
                    if (!synonyms.equals("")) {
                        synonyms = synonyms + "," + synonymsArray[j].substring(0, 1).toUpperCase() + synonymsArray[j].substring(1).toLowerCase() + " ";
                        System.out.println("synonyms.. " + synonyms);
                    }
                    else {
                        synonyms =  synonymsArray[j].substring(0, 1).toUpperCase() + synonymsArray[j].substring(1).toLowerCase() + " ";

                    }

                }


                if (synonyms.contains(",Na")) {
                    synonyms =  synonyms.replace(",Na","");
                }
                synonyms = synonyms.trim();
                if (synonyms.equals("Na")) {
                    synonyms = "No synonym found ";
                }

                /*antonyms = c.getString(c.getColumnIndex("antonyms"));

                if (antonyms.equals("NA")) {
                    antonyms = "No antonym found ";
                }*/



                String antonyms1 = c.getString(c.getColumnIndex("antonyms")).trim();
                //antonyms = antonyms.substring(0, 1).toUpperCase() + antonyms.substring(1);

                String[] antonymsArray = antonyms1.split(",");

                for(int k = 0 ; k < antonymsArray.length; k++)
                {

                    if (!antonyms.equals("")) {
                        antonyms = antonyms + "," + antonymsArray[k].substring(0, 1).toUpperCase() + antonymsArray[k].substring(1).toLowerCase() + " ";
                    }
                    else {
                        antonyms =  antonymsArray[k].substring(0, 1).toUpperCase() + antonymsArray[k].substring(1).toLowerCase() + " ";

                    }

                }
               /* if (antonyms.equals("Na")) {
                    antonyms = "No antonym found ";
                }
                */
                if (antonyms.contains(",Na")) {
                   antonyms =  antonyms.replace(",Na","");
                   Log.i("antonyms con",antonyms);
                }
                antonyms = antonyms.trim();
                System.out.println("antonyms na.. " + antonyms);
                if (antonyms.equals("Na")) {
                    antonyms = "No antonym found";
                    System.out.println("antonyms last.. " + antonyms);

                }

                databaseHelper.insertHistory(enWord);
            }
            while (c.moveToNext());

        } else {

            enWord = "Not Available";
        }

       c.close();   //


        setSupportActionBar(toolbar);

        String word = enWord.toUpperCase();
        getSupportActionBar().setTitle(word);

        toolbar.setNavigationIcon(R.drawable.back_icon);

        if (viewPager != null) {
            setUpViewPager(viewPager);
        }

        tabLayout.setupWithViewPager(viewPager);

        //   viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                System.out.println("position.." + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    public void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new Definition(), "Definition");
        viewPagerAdapter.addFragment(new Synonyms(), "Synonyms");
        viewPagerAdapter.addFragment(new Antonyms(), "Antonyms");
        viewPagerAdapter.addFragment(new Example(), "Examples");
        viewPager.setAdapter(viewPagerAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ((item.getItemId())) {

            case android.R.id.home:

                if (startedFromShare) {

                    Intent i = new Intent(MeaningActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    onBackPressed();
                }

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();

        if (startedFromShare) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    public void onPause() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // super.onDestroy();

         try {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
       } catch (Exception e) {
            e.printStackTrace();

        }
    }


    @OnClick({R.id.btnSpeaker,R.id.shareFab})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btnSpeaker:
                try {
                    System.out.println("speaker clicked");
                    tts = new TextToSpeech(MeaningActivity.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {

                            if (status == TextToSpeech.SUCCESS) {
                                int result = tts.setLanguage(Locale.getDefault());
                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    Log.e("error", "This language is not supported");
                                    Toast.makeText(MeaningActivity.this, "This language is not supported", Toast.LENGTH_SHORT).show();

                                } else {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        tts.speak(enWord, TextToSpeech.QUEUE_FLUSH, null, null);
                                    } else {
                                        Toast.makeText(MeaningActivity.this, "Speech is not supported ", Toast.LENGTH_SHORT).show();
                                    }
                                    //  tts.speak(enWord,TextToSpeech.QUEUE_FLUSH,null);
                                }


                            } else {
                                Toast.makeText(MeaningActivity.this, "Initialization failed", Toast.LENGTH_SHORT).show();

                                Log.e("error", "Initialisation failed!");

                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.shareFab:
                System.out.println("share clicked");

                try {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String definitions =  definition.substring(0,1).toUpperCase() + definition.substring(1).toLowerCase();
                    String shareBody = "Word: "+enWord+"\nDefinition: "+definitions;
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, enWord);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


        }



    }


}
