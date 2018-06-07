package com.example.g.luciddreamgenerator;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ToolsActivity extends AppCompatActivity {
    RadioGroup intervalSelect;
    RadioGroup songSelect;
    Switch sw;
    long interval;
    boolean random = false;
    boolean active = false;
    boolean already_active = false;
    String saved_active = "Off";
    String saved_interval = "Half Hour";
    String saved_song = "Coin";
    // Access a Cloud Firestore instance from your Activity

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        interval = AlarmManager.INTERVAL_HALF_HOUR;
        intervalSelect = (RadioGroup) findViewById(R.id.radioGroup);
        songSelect = (RadioGroup) findViewById(R.id.songRadioGroup);
        sw = (Switch) findViewById(R.id.switch1);

        //load previous settings
        loadSettings();
        setUpPlayCoinButton();
        setUpPlayDreamButton();
        setUpPlayNotifButton();
        //setUpInfoButton();

        //set switch to on or off
        if (saved_active.equals("On")) {
            active = true;
            sw.setChecked(true);
        }

        //set interval radio button to previously saved radio button
        if (saved_interval.equals("Half Hour")) {
            intervalSelect.check(R.id.halfRbtn);
        }
        else if (saved_interval.equals("Hour")) {
            intervalSelect.check(R.id.hourRbtn);
        }
        else if (saved_interval.equals("Two Hours")) {
            intervalSelect.check(R.id.twoRbtn);
        }
        else if (saved_interval.equals("Random")) {
            intervalSelect.check(R.id.randRbtn);
        }

        //set song radio button to previously saved radio button
        if (saved_song.equals("Coin")) {
            songSelect.check(R.id.coinRbtn);
        }
        else if (saved_song.equals("Dream")) {
            songSelect.check(R.id.dreamRbtn);
        }
        else if (saved_song.equals("Notification")) {
            songSelect.check(R.id.notificationRbtn);
        }
        else if (saved_song.equals("None")) {
            songSelect.check(R.id.noneRbtn);
        }

        //Set up interval to be used in the repeating alarm
        intervalSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);

                switch(rb.getId()) {
                    case R.id.halfRbtn:
                        interval = AlarmManager.INTERVAL_HALF_HOUR;
                        saved_interval = "Half Hour";
                        break;
                    case R.id.hourRbtn:
                        interval = AlarmManager.INTERVAL_HOUR;
                        saved_interval = "Hour";
                        break;
                    case R.id.twoRbtn:
                        interval = AlarmManager.INTERVAL_HOUR * 2;
                        saved_interval = "Two Hours";
                        break;
                    case R.id.randRbtn:
                        Random rand = new Random();
                        long randNum = (long) rand.nextInt((int) AlarmManager.INTERVAL_HOUR * 2 -
                                (int) AlarmManager.INTERVAL_HALF_HOUR) + AlarmManager.INTERVAL_HALF_HOUR;
                        interval = randNum;
                        random = true;
                        saved_interval = "Random";
                        break;
                }
                setAlarm();
            }
        });

        //Set up sound to be used in the repeating alarm
        songSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);

                switch(rb.getId()) {
                    case R.id.coinRbtn:
                        saved_song = "Coin";
                        break;
                    case R.id.dreamRbtn:
                        saved_song = "Dream";
                        break;
                    case R.id.notificationRbtn:
                        saved_song = "Notification";
                        break;
                    case R.id.noneRbtn:
                        saved_song = "None";
                        break;
                }
                setAlarm();
            }
        });

        //Turn on the repeating alarm if checked, turn off alarm if not checked
        sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    active = true;
                    saved_active = "On";
                }
                else {
                    active = false;
                    saved_active = "Off";
                }
                setAlarm();
            }
        });

        ImageButton info_button = (ImageButton) findViewById(R.id.imageButton);

        info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showToast();
                Snackbar snackbar;
                snackbar = Snackbar.make(getWindow().getDecorView().getRootView(),
                        "Train your mind to ask yourself if you are dreaming using a unique sound " +
                                "throughout the day and while you are dreaming.\n",
                        10000);
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(5);
                snackbar.show();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            saveSettings();
            finish();
            startActivity(new Intent(ToolsActivity.this, MenuActivity.class));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setUpPlayCoinButton() {
        ImageButton play_coin_button = (ImageButton) findViewById(R.id.playCoinBtn);
        final Uri alert = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.coin);
        play_coin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mp = MediaPlayer.create(ToolsActivity.this, alert);
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer player) {
                        player.release();
                        player = null;
                    }
                });
            }
        });
    }

    public void setUpPlayDreamButton() {
        ImageButton play_coin_button = (ImageButton) findViewById(R.id.playDreamBtn);
        final Uri alert = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dream);
        play_coin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mp = MediaPlayer.create(ToolsActivity.this, alert);
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer player) {
                        player.release();
                        player = null;
                    }
                });
            }
        });
    }

    public void setUpPlayNotifButton() {
        ImageButton play_coin_button = (ImageButton) findViewById(R.id.playNotifBtn);
        final Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        play_coin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mp = MediaPlayer.create(ToolsActivity.this, alert);
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer player) {
                        player.release();
                    }
                });
            }
        });
    }

    public void setAlarm() {
        final AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(ToolsActivity.this, AlarmReceiver.class);
        Bundle extras = new Bundle();
        extras.putString("SONG_ID", saved_song);
        if (random) {extras.putString("RANDOM_BOOL", "true");}
        else {extras.putString("RANDOM_BOOL", "false");}
        i.putExtras(extras);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        if (random && active){
            if (Build.VERSION.SDK_INT < 19) {
                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() - 10000, interval, pi);
            }
            else {
                am.setExact(AlarmManager.RTC_WAKEUP, 10, pi);
            }
            already_active = true;
        }
        else if (!random && active) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() - 10000, interval, pi);
            already_active = true;
        }
        else if (!active) {
            am.cancel(pi);
        }
        saveSettings();
    }

    public void loadSettings() {
        String[] temp_loaded_settings;
        SharedPreferences settings = getApplicationContext().getSharedPreferences("preferences", 0);
        String loaded_settings = settings.getString("settings", "");
        temp_loaded_settings = loaded_settings.split("~");

        if (!temp_loaded_settings[0].isEmpty()) {
            saved_active = temp_loaded_settings[0];
            saved_interval = temp_loaded_settings[1];
            saved_song = temp_loaded_settings[2];
        }
    }

    public void saveSettings() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        StringBuilder sb = new StringBuilder();
        sb.append(saved_active).append("~");
        sb.append(saved_interval).append("~");
        sb.append(saved_song).append("~");
        editor.putString("settings", sb.toString());
        editor.apply();
    }
}
