package com.example.g.luciddreamgenerator;

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
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ToolsActivity extends AppCompatActivity {
    RadioGroup intervalSelect;
    Switch sw;
    Spinner spin;
    long interval;
    boolean random = false;
    boolean active = false;
    boolean already_active = false;
    String saved_active = "Off";
    String saved_interval = "Half Hour";
    String saved_song = "Coin";
    private static final int uniqueID = 12345;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        interval = AlarmManager.INTERVAL_HALF_HOUR;
        intervalSelect = (RadioGroup) findViewById(R.id.radioGroup);
        spin = (Spinner) findViewById(R.id.spinner2);
        sw = (Switch) findViewById(R.id.switch1);

        //load previous settings
        loadSettings();
        setUpBackButton();

        //set switch to on or off
        if (saved_active.equals("On")) {
            sw.setChecked(true);
        }

        //set radio button to previously saved radio button
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

        //Select sound to be used in the repeating alarm
        List<String> list = new ArrayList<>();
        list.add("Coin");
        list.add("Notification");
        list.add("Alarm");
        list.add("None");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        if (saved_song.equals("Coin")) {spin.setSelection(0);}
        else if (saved_song.equals("Notification")) {spin.setSelection(1);}
        else if (saved_song.equals("Alarm")) {spin.setSelection(2);}
        else if (saved_song.equals("None")) {spin.setSelection(3);}

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saved_song = spin.getSelectedItem().toString();
                setAlarm();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    }

    public void setUpBackButton() {
        Button back_button = (Button) findViewById(R.id.backBtn);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                startActivity(new Intent(ToolsActivity.this, MenuActivity.class));
            }
        });
    }

    public void setAlarm() {
        final AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(ToolsActivity.this, AlarmReceiver.class);
        i.putExtra("SONG_ID", saved_song);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        if (random && active){
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pi);
            already_active = true;
        }
        else if (!random && active) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pi);
            already_active = true;
        }
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
